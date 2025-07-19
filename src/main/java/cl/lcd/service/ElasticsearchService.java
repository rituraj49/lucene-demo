package cl.lcd.service;

import cl.lcd.dto.AirportCreateDto;
import cl.lcd.enums.LocationType;
import cl.lcd.model.Airport;
import cl.lcd.model.LocationResponse;
import cl.lcd.util.HelperUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ElasticsearchService {
    private final ElasticsearchClient client;

    @Autowired
    public ElasticsearchService(ElasticsearchClient client) {
        this.client = client;
    }

    public Airport singleUpload(AirportCreateDto data) throws IOException, IOException {
        IndexRequest<AirportCreateDto> airportReq = IndexRequest.of((id -> id
                .index("airport")
                .refresh(Refresh.WaitFor)
                .document(data)));

        IndexResponse res = client.index(airportReq);
        System.out.println(res);

        return new Airport(
                LocationType.valueOf(data.getType().toString()),
                data.getIata(),
//                data.getIcao(),
                data.getName(),
                Double.parseDouble(String.valueOf(data.getLatitude())),
                Double.parseDouble(String.valueOf(data.getLongitude())),
//                data.getElevation(),
//                data.getUrl(),
                data.getTime_zone(),
                data.getCity_code(),
                data.getCountry_code(),
                data.getCity()
//                data.getState(), data.getCounty()
        );
    }

    public void bulkUpload(List<LocationResponse> airports, String indexName) throws IOException {
        int batchSize = 1000;
        int total = airports.size();
        Map<String, List<LocationResponse>> cityGroups = airports.stream().collect(Collectors.groupingBy(LocationResponse::getCityCode));
        for(int i=0; i <= total; i += batchSize) {
            int end = Math.min(i+batchSize, total);

            List<LocationResponse> batch = airports.subList(i, end);
            if(batch.isEmpty()) continue;

            BulkRequest.Builder br = getBuilder(indexName, cityGroups, batch);
            BulkResponse response = client.bulk(br.build());

            if(response.errors()) {
                log.error("errors occurred in batch from: {} to {}", i, end - 1);
            } else {
                log.info("successfully inserted batch from: {} to {}", i, end-1);
            }
        }
    }

    private static BulkRequest.Builder getBuilder(String indexName,
                                                  Map<String, List<LocationResponse>> cityGroups,
                                                  List<LocationResponse> batch) {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for(LocationResponse a: batch) {
            List<LocationResponse> airports = cityGroups.get(a.getCityCode())
                    .stream()
                    .filter(ar -> !a.getIata().equals(ar.getIata()))
                    .toList();

            List<LocationResponse.SimpleAirport> simpleAirports = airports.stream().map(airport -> {
                LocationResponse.SimpleAirport simpleAirport = new LocationResponse.SimpleAirport();
                simpleAirport.setIata(airport.getIata());
                simpleAirport.setName(airport.getName());
                simpleAirport.setCityCode(airport.getCityCode());
                simpleAirport.setCity(airport.getCity());
                simpleAirport.setSubType(airport.getSubType());
                return simpleAirport;
            }).toList();

            a.setGroupData(simpleAirports);

            br.operations(op -> op
                .index(idx -> idx
                        .index(indexName)
                        .id(a.getIata())
                        .document(toLocationDocument(a)
                    )
                )
            );
        }
        return br;
    }

    public static Map<String, Object> toLocationDocument(LocationResponse airport) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("subType", airport.getSubType().toString());
        doc.put("iata", airport.getIata());
        doc.put("name", airport.getName());
        doc.put("latitude", airport.getLatitude());
        doc.put("longitude", airport.getLongitude());
        doc.put("time_zone", airport.getTimeZoneOffset());
        doc.put("city_code", airport.getCityCode());
        doc.put("country_code", airport.getCountryCode());
        doc.put("city", airport.getCity());
        doc.put("group_data", airport.getGroupData());

        if(airport.getLatitude() != null && airport.getLongitude() != null) {
            doc.put("location", Map.of(
                "lat", airport.getLatitude(),
                "lon", airport.getLongitude()
            ));
        }
        return doc;
    }

    public void createIndex(String indexName) {
        try {
            boolean exists = client.indices().exists(e -> e.index("airports")).value();

            if(exists) {
                log.warn("index already exists");
                return;
            }

            CreateIndexResponse response =
                    client.indices().create(cl -> cl
                            .index(indexName)
                            .settings(s -> s
                                    .analysis(an -> an
                                            .analyzer("autocomplete", a -> a
                                                    .custom(c -> c
                                                            .tokenizer("whitespace")
                                                            .filter("lowercase", "autocomplete_filter")
                                                    )
                                            )
                                            .filter("autocomplete_filter", f -> f
                                                    .definition(df -> df
                                                            .edgeNgram(en -> en
                                                                    .minGram(2)
                                                                    .maxGram(20)
                                                            )
                                                    )
                                            )
                                    )
                            )
                            .mappings(m -> m
                                    .properties("code", p -> p.keyword(k -> k))
                                    .properties("icao", p -> p.keyword(k -> k))
                                    .properties("name", p -> p.text(k -> k
                                            .analyzer("autocomplete")))
                                    .properties("latitude", p -> p.double_(k -> k
                                            .fields("raw", t -> t.keyword(r -> r))
                                    ))
                                    .properties("longitude", p -> p.double_(k -> k
                                            .fields("raw", t -> t.keyword(r -> r))
                                    ))
                                    .properties("elevation", p -> p.integer(k -> k))
                                    .properties("url", p -> p.text(k -> k))
                                    .properties("time_zone", p -> p.text(k -> k))
                                    .properties("city_code", p -> p.keyword(k -> k))
                                    .properties("country_code", p -> p.keyword(k -> k))
                                    .properties("city", p -> p.text(k -> k
                                            .analyzer("autocomplete")
                                            .fields("raw", at -> at.keyword(kv -> kv))
                                    ))
                                    .properties("state", p -> p.text(k -> k
                                            .analyzer("autocomplete")
                                            .fields("raw", at -> at.keyword(kv -> kv))
                                    ))
                                    .properties("county", p -> p.text(k -> k
                                            .analyzer("autocomplete")
                                            .fields("raw", at -> at.keyword(kv -> kv))
                                    ))
                            ));
            log.info("index {} created successfully", indexName);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IllegalStateException("failed to create index", e);
        }
    }

    public List<Airport> fetchAll(int page, int size) throws IOException {
        int from = (page - 1) * size;
        SearchResponse<Airport> response = client.search(s -> s
                        .index("airports")
                        .trackTotalHits(t -> t.enabled(true))
                        .from(from)
                        .size(size)
                        .query(q -> q
                                .matchAll(m -> m)
                        ),
                Airport.class
        );
        List<Hit<Airport>> result = response.hits().hits();
        System.out.println("result: "+ result);
        int resSize = result.size();
        System.out.println(resSize);
        List<Airport> airports = new ArrayList<>();

//        long totalRecords = response.hits().total().value();
        for(Hit<Airport> hit: result) {
            airports.add((Airport) hit.source());
        }
        return airports;
    }

    public List<Airport> searchByText(String query, int page, int size) throws IOException {
        String[] queryArr = query.split(":");
        String field = queryArr[0];
        String item = queryArr[1];
        int from = (page - 1) * size;
        SearchResponse<Airport> sr = client.search(s -> s
                        .index("airports")
                        .from(from)
                        .size(size)
                        .query(q -> q
                                .match(t -> t
                                        .field(field)
                                        .query(item)
                                )
                        ),
                Airport.class
        );

        List<Hit<Airport>> hits = sr.hits().hits();

        List<Airport> airports = new ArrayList<>();

        for(Hit<Airport> hit: hits) {
            airports.add(hit.source());
        }
        return airports;
    }

    public List<LocationResponse> searchByKeyword(String keyword, int page, int size) throws IOException {
        log.debug("searching elastic index for keyword: {}, page: {}, size: {}", keyword, page, size);
        int from = (page - 1) * size;
        List<LocationResponse> airports = new ArrayList<>();
        SearchResponse<LocationResponse> sr = client.search(s -> s
                .index("airports")
                .from(from)
                .size(size)
                .query(q -> q
                                .bool(b -> b
                                        .should(sh -> sh.term(t -> t.field("iata.raw").value(keyword).boost(5f)))
                                        .should(sh -> sh.term(t -> t.field("city_code.raw").value(keyword).boost(3f)))
                                        .should(sh -> sh.match(m -> m.field("name").query(keyword).boost(2f)))
                                        .should(sh -> sh.match(m -> m.field("city").query(keyword).boost(1f)))
                                        .minimumShouldMatch("1")
                                )
                ),
                LocationResponse.class
        );

        List<Hit<LocationResponse>> hits = sr.hits().hits();

        for(Hit<LocationResponse> hit: hits) {
            airports.add(hit.source());
        }

//        return HelperUtil.getGroupedLocationData(airports);
        return airports;
    }

    public Map<String, Object> aggregateRecords(String aggField) throws IOException {
        SearchResponse<Void> sr = client.search(s -> s
                        .index("airports")
                        .size(0)
                        .aggregations("country_groups", ag -> ag
                                        .terms(t -> t
                                                        .field(aggField)
//                                .size(10)
                                        )
                        )
                        .aggregations("avg_elevation", ag -> ag
                                .avg(avg -> avg.field("elevation"))
                        )
        );

        Map<String, Aggregate> result = sr.aggregations();
        System.out.println(result);

        Map<String, Object> responseMap = new HashMap<>();

        Aggregate avgAgg = result.get("avg_elevation");

        if(avgAgg.isAvg()) {
            Double avgValue = avgAgg.avg().value();
            responseMap.put("avg_elevation", avgValue);
        }

        Aggregate countryGrouping = result.get("country_groups");

        if(countryGrouping.isSterms()) {
            List<Map<String, Object>> buckets = countryGrouping.sterms().buckets().array().stream()
                    .map(s -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("key", s.key().stringValue());
                        map.put("count", s.docCount());
                        return map;
                    })
                    .toList();
            responseMap.put("country_groups", buckets);
        }
        return responseMap;
    }
}

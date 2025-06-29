package cl.lcd.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cl.lcd.util.HelperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import cl.lcd.model.Airport;
import cl.lcd.model.AirportResponse;
import cl.lcd.enums.LocationType;
import jakarta.annotation.PostConstruct;

@Service
@Slf4j
public class InMemoryLuceneService {

	private Directory inMemoryIndex;
	private Analyzer analyzer;
	
//	Logger logger = LoggerFactory.getLogger(InMemoryLuceneService.class);

	@PostConstruct
	public void init() {
		try {
//			this.inMemoryIndex = FSDirectory.open(Paths.get("airports"));
			this.inMemoryIndex = new ByteBuffersDirectory();
			this.analyzer = buildPerFieldAnalyzer();
			indexData();
		} catch (Exception e) {
			throw new RuntimeException("Lucene initialization failed", e);
		}
	}
	
	public Analyzer buildPerFieldAnalyzer() {
		Map<String, Analyzer> analyzerPerField = new HashMap<>();
		analyzerPerField.put("iata", new IndexingKeywordAnalyzer());
		analyzerPerField.put("icao", new IndexingKeywordAnalyzer());
		analyzerPerField.put("name_autocomplete", new EdgeNGramAnalyzer());
		analyzerPerField.put("name", new StandardAnalyzer());
		analyzerPerField.put("city_code", new IndexingKeywordAnalyzer());
		analyzerPerField.put("city_autocomplete", new EdgeNGramAnalyzer());
		analyzerPerField.put("city", new StandardAnalyzer());
		analyzerPerField.put("state", new EdgeNGramAnalyzer());

		Analyzer defaultAnalyzer = new StandardAnalyzer();
		
		return new PerFieldAnalyzerWrapper(defaultAnalyzer, analyzerPerField);
	}
	
	public List<Airport> readDataFromFile(String file) {
		try(CSVReader reader = new CSVReader(new FileReader(file))) {
			CsvToBean<Airport> csvToBean = new CsvToBeanBuilder<Airport>(reader)
					.withType(Airport.class)
					.withIgnoreLeadingWhiteSpace(true)
					.build();
			
				return csvToBean.parse();
		} catch (IOException e) {
//			e.printStackTrace();
            log.error("Error reading data from file: {}; {}; {}", file, e.getCause(), e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
//	public void indexData(List<Airport> dataList) throws IOException {
		public void indexData() throws IOException {
			List<Airport> dataList = readDataFromFile("airports.csv");

		try(IndexWriter writer = new IndexWriter(inMemoryIndex, new IndexWriterConfig(analyzer))) {
			int batchSize = 1000;
        	int total = dataList.size();
        	
        	for(int i = 0; i <= total; i += batchSize) {
        		int end = Math.min(i + batchSize, total);
        		
        		List<Airport> batch = dataList.subList(i, end);
        		if(batch.isEmpty()) {
        			continue;
        		}
					for(Airport a: batch) {
						Document doc = new Document();
						doc.add(new TextField("iata", a.getIata(), Field.Store.YES));
	//        			doc.add(new TextField("icao", a.getIcao(), Field.Store.YES));
						doc.add(new TextField("name", a.getName(), Field.Store.YES));
						doc.add(new TextField("name_autocomplete", a.getName(), Field.Store.YES));
						doc.add(new DoubleField("latitude", a.getLatitude(), Field.Store.YES));
						doc.add(new DoubleField("longitude", a.getLongitude(), Field.Store.YES));
	//        			doc.add(new IntField("elevation", a.getElevation(), Field.Store.YES));
	//        			doc.add(new TextField("url", a.getUrl(), Field.Store.YES));
	//        			doc.add(new TextField("time_zone", a.getTime_zone(), Field.Store.YES));
						doc.add(new TextField("city_code", a.getCity_code(), Field.Store.YES));
						doc.add(new TextField("country_code", a.getCountry_code(), Field.Store.YES));
						doc.add(new TextField("city", a.getCity(), Field.Store.YES));
        				doc.add(new TextField("city_autocomplete", a.getCity(), Field.Store.YES));
//        			doc.add(new TextField("state", a.getState(), Field.Store.YES));
//        			doc.add(new TextField("county", a.getCounty(), Field.Store.YES));
//        			doc.add(new TextField("type", a.getType(), Field.Store.YES));
        			writer.addDocument(doc);
        		}
        		log.info("added docs from {} to {}", i, end);
        	}
		}
	}
	
	public List<AirportResponse> search(String keyword) throws Exception {
        List<Airport> results = new ArrayList<>();

        try (DirectoryReader reader = DirectoryReader.open(inMemoryIndex)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            String[] edgeFields = {"name", "city"};
            Map<String, Float> boosts = new HashMap<>();
            boosts.put("city", 5.0f);
            boosts.put("name", 2.0f);
            boosts.put("iata", 1.0f);

            List<Query> exactQueries = List.of(
            	    new TermQuery(new Term("iata", keyword.toLowerCase())),
            	    new TermQuery(new Term("icao", keyword.toLowerCase())),
            	    new TermQuery(new Term("city_code", keyword.toLowerCase()))
            	);
            BooleanQuery.Builder finalQuery = new BooleanQuery.Builder();

            Analyzer perFieldAnalyzer = new PerFieldAnalyzerWrapper(
            		new StandardAnalyzer(),
            		Map.of(
            				"iata", new IndexingKeywordAnalyzer(),
            		        "icao", new IndexingKeywordAnalyzer(),
            		        "city_code", new IndexingKeywordAnalyzer(),
            		        "name", new SearchAnalyzer(),
            		        "name_autocomplete", new EdgeNGramAnalyzer(),
            		        "city", new SearchAnalyzer(),
            		        "city_autocomplete", new EdgeNGramAnalyzer()
					)
				);
            MultiFieldQueryParser mfqParser = new MultiFieldQueryParser(edgeFields, perFieldAnalyzer);

			mfqParser.setDefaultOperator(QueryParser.Operator.AND);

            Query edgeQuery = mfqParser.parse(QueryParserBase.escape(keyword.toLowerCase()));
            
            finalQuery.add(new BoostQuery(edgeQuery, 1.0f), BooleanClause.Occur.SHOULD);

			Query cityAutocompleteQuery = new PrefixQuery(new Term("city_autocomplete", keyword.toLowerCase()));
			Query nameAutocompleteQuery = new PrefixQuery(new Term("name_autocomplete", keyword.toLowerCase()));

			finalQuery.add(new BoostQuery(cityAutocompleteQuery, 1.0f), BooleanClause.Occur.SHOULD);
			finalQuery.add(new BoostQuery(nameAutocompleteQuery, 1.0f), BooleanClause.Occur.SHOULD);

			exactQueries.forEach(q -> finalQuery.add(q, BooleanClause.Occur.SHOULD));
            
//            TopDocs hits = searcher.search(query, 10);
            TopDocs hits = searcher.search(finalQuery.build(), 10);

            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = searcher.storedFields().document(scoreDoc.doc);
                results.add(new Airport(
                	LocationType.AIRPORT,
                    doc.get("iata"),
//                    doc.get("icao"),
                    doc.get("name"),
                    Double.parseDouble(doc.get("latitude")), 
                    Double.parseDouble(doc.get("longitude")), 
//                    doc.get("longitude"), 
//                    Integer.parseInt(doc.get("elevation")), 
//                    doc.get("url"), 
                    doc.get("time_zone"), 
                    doc.get("city_code"), 
                    doc.get("country_code"), 
                    doc.get("city") 
//                    doc.get("state"), 
//                    doc.get("county"), 
//                    doc.get("type")
                ));
            }
        }

//        return results;
		return HelperUtil.getGroupedData(results);
    }

	public List<AirportResponse> getGroupedData(List<Airport> data) {
		Map<String, List<Airport>> groupedData = data.stream().collect(Collectors.groupingBy(Airport::getCity_code));
		
		List<AirportResponse> result = new ArrayList<>();
		
		for(Map.Entry<String, List<Airport>> entry : groupedData.entrySet()) {
			System.out.println("map entry: " + entry.toString());
			List<Airport> group = entry.getValue();
			
			Airport airport = group.get(0);
			
			List<Airport> children = group.subList(1, group.size());
			
			AirportResponse parent = new AirportResponse();
			parent.setParent(airport);
			parent.setGroupData(children);
			
			result.add(parent);
		}
		return result;
	}
}

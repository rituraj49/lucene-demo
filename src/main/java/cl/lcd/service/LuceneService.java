package cl.lcd.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;
import cl.lcd.LuceneDemoApplication;
import cl.lcd.model.Airport;

@Service
public class LuceneService {

//    private final LuceneDemoApplication luceneDemoApplication;

	private static final String INDEX_DIR = "airports";

//    LuceneService(LuceneDemoApplication luceneDemoApplication) {
//        this.luceneDemoApplication = luceneDemoApplication;
//    }
	
	private Directory getDirectory() throws IOException {
		return FSDirectory.open(Paths.get(INDEX_DIR));
	}
	
	private Analyzer getAnalyzer() {
//		return new StandardAnalyzer();
		return new EdgeNGramAnalyzer();
	}
	
	public void indexData(List<Airport> dataList) throws IOException {
        try(
        		Directory dir = getDirectory();
        		IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(getAnalyzer()))
			) {
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
        			doc.add(new StringField("iata", a.getIata(), Field.Store.YES));
//        			doc.add(new StringField("icao", a.getIcao(), Field.Store.YES));
        			doc.add(new TextField("name", a.getName(), Field.Store.YES));
//        			doc.add(new StringField("latitude", a.getLatitude(), Field.Store.YES));
//        			doc.add(new StringField("longitude", a.getLongitude(), Field.Store.YES));
//        			doc.add(new IntField("elevation", a.getElevation(), Field.Store.YES));
//        			doc.add(new StringField("url", a.getUrl(), Field.Store.YES));
//        			doc.add(new TextField("time_zone", a.getTime_zone(), Field.Store.YES));
        			doc.add(new StringField("city_code", a.getCityCode(), Field.Store.YES));
        			doc.add(new StringField("country_code", a.getCountryCode(), Field.Store.YES));
        			doc.add(new TextField("city", a.getCity(), Field.Store.YES));
//        			doc.add(new TextField("state", a.getState(), Field.Store.YES));
//        			doc.add(new TextField("county", a.getCounty(), Field.Store.YES));
//        			doc.add(new StringField("type", a.getType(), Field.Store.YES));
        			writer.addDocument(doc);
        		}
        		System.out.println("added docs from " + i + " to " + end);
        	}
		}
	}
	
	public List<Airport> search(String keyword) throws Exception {
        List<Airport> results = new ArrayList<>();

        try (Directory dir = getDirectory();
             DirectoryReader reader = DirectoryReader.open(dir)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("title", getAnalyzer());
            Query query = parser.parse(keyword);

            TopDocs hits = searcher.search(query, 10);

            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = searcher.storedFields().document(scoreDoc.doc);
//                results.add(new Airport(
//                        doc.get("iata"),
//                        doc.get("icao"),
//                        doc.get("name"),
//                        doc.get("latitude"), 
//                        doc.get("longitude"), 
//                        Integer.parseInt(doc.get("elevation")), 
//                        doc.get("url"), 
//                        doc.get("time_zone"), 
//                        doc.get("city_code"), 
//                        doc.get("country_code"), 
//                        doc.get("city"), 
//                        doc.get("state"), 
//                        doc.get("county"), 
//                        doc.get("type")
//                ));
            }
        }

        return results;
    }
	
	public static void analyzeText() throws IOException {
		SearchAnalyzer analyzer = new SearchAnalyzer();
		TokenStream stream = analyzer.tokenStream("field", "new");
		CharTermAttribute attr = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		
		while(stream.incrementToken()) {
			System.out.println(attr.toString());
		}
		
		stream.end();
		stream.close();
	}
}

package cl.lcd.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class EdgeNGramAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {

		Tokenizer tokenizer = new EdgeNGramTokenizer(1, 20);
		TokenStream tokenStream = new LowerCaseFilter(tokenizer);
		
		return new TokenStreamComponents(tokenizer, tokenStream);
	}

}

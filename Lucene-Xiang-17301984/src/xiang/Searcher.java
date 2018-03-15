package xiang ;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Searcher {

	private Analyzer analyzer;
	private int hitsPerPage;
	private IndexReader reader;
	private IndexSearcher searcher;
	private TopScoreDocCollector collector;
	private BufferedWriter bmWriter,vsmWriter,booleanWriter;

	public Searcher(Analyzer analyzer, Directory index) throws IOException
	{
		this.analyzer = analyzer;
		hitsPerPage = 1400;
	    reader = DirectoryReader.open(index);
	}

	public void searchQueryVector(String querystr, int id) throws ParseException, IOException
	{
		double map = 0, recall = 0;
		searcher = new IndexSearcher(reader);
	    collector = TopScoreDocCollector.create(hitsPerPage);
//	    String[] fields = {"word"};
		searcher.setSimilarity(new ClassicSimilarity());
	    Query q = new MultiFieldQueryParser(new String[]{"word","title","author", "biography"} , analyzer).parse(querystr);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    System.out.println("\nVector Space Results");
	    printResults(hits,querystr,id,vsmWriter);
	}

	public void searchQueryBM25(String querystr, int id) throws ParseException, IOException
	{
        double map = 0, recall = 0;
		searcher = new IndexSearcher(reader);
	    collector = TopScoreDocCollector.create(hitsPerPage);
//	    String[] fields = {"word","title","author"};
	    searcher.setSimilarity(new BM25Similarity());
	    Query q = new MultiFieldQueryParser(new String[]{"word","title","author", "biography"}, analyzer).parse(querystr);
	    searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    System.out.println("\nBM25 Results");
	    printResults(hits,querystr,id,bmWriter);
	}

	/*public void searchQueryBoolean(String querystr,int id) throws ParseException, IOException
	{	
		searcher = new IndexSearcher(reader);
	    TokenStream stream = analyzer.tokenStream("word",new StringReader(querystr));
	    
	    
	    stream.reset();
	    int hitsPP = tokenCount(stream);
	    stream.end();
	    stream.close();
	    stream = analyzer.tokenStream("word",new StringReader(querystr));
		stream.reset();
//		System.out.println("Tokens: " + hitsPP);
		ScoreDoc[][] hits=new ScoreDoc[hitsPP][];
		int i=0;
		while (stream.incrementToken())
		{
			collector = TopScoreDocCollector.create((hitsPP>10 ? 2 : 3 ), true);
			Query q = new TermQuery(new Term("word", stream.getAttribute(CharTermAttribute.class).toString()));
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(q, BooleanClause.Occur.SHOULD);
//			System.out.println("Query: " + booleanQuery.toString());
		    searcher.search(booleanQuery, collector);
		    hits[i] = collector.topDocs().scoreDocs;
		    i++;
		}
		stream.end();
		stream.close();
		 System.out.println("\nBoolean Results");
	    printResultsB(hits,querystr,id);
	}*/

	public int tokenCount(TokenStream s) throws IOException
	{
		int tokens=0;
		while (s.incrementToken())
		{
			tokens++;
		}
		return tokens;
	}

	public void printResultsB(ScoreDoc[][] hits,String q,int id) throws IOException
	{
		System.out.println("Found " + ((hits.length*hits[0].length > 20) ? 20 : hits.length) + " hits");
		int k=0;
	    for(int i=0;i<hits.length;++i) {
	    	for(int j=0;j<hits[i].length;j++){
	    		int docId = hits[i][j].doc;
	    		Document d = searcher.doc(docId);
	    		System.out.println(id + " " + d.get("id"));
	    		booleanWriter.append(id + " " + d.get("id")+"\n");
	    		k++;
	    		if(k==20)
	    			return;
	    	}
	    }
	}

	public void printResults(ScoreDoc[] hits,String q,int id,BufferedWriter writer) throws IOException
	{
		System.out.println("Found " + hits.length + " hits");
	    for(int i=0;i<hits.length;++i) {
	    	int docId = hits[i].doc +1;
//	    	Document d = searcher.doc(docId);
	    	System.out.println(id + " " +  (docId)+ " " + hits[i].score);

	    	writer.append(id + " 0 " + (docId)+ " " + (i) + " " + hits[i].score + " Exp\n");
	    }
	}


	public void initializeWriters(String path) throws IOException
	{
		File parent = new File(path);
		if (!parent.exists()) {
			parent.mkdirs();
		}
		bmWriter = new BufferedWriter(new FileWriter(new File(path + File.separator + "bm_output.txt")));
		vsmWriter = new BufferedWriter(new FileWriter(new File(path + File.separator + "vsm_output.txt")));
		//booleanWriter = new BufferedWriter(new FileWriter(new File("boolean_output.txt")));
	}

	public void closeWriters() throws IOException
	{
		bmWriter.close();
		vsmWriter.close();
		//booleanWriter.close();
	}
}

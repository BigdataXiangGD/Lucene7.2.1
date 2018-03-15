package xiang;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

import static org.apache.lucene.index.IndexWriterConfig.OpenMode.CREATE;
import static org.apache.lucene.index.IndexWriterConfig.OpenMode.CREATE_OR_APPEND;


public class Indexer {
	

	public Directory index;
	public IndexWriterConfig config;
	public Analyzer analyzer;
	public IndexWriter w;
	public String path;
	
	public Indexer(String path) throws IOException{
		analyzer = new StandardAnalyzer();
		this.path = path;
		index = FSDirectory.open(Paths.get("index", path));
		config = new IndexWriterConfig(analyzer);
		config.setOpenMode(CREATE);
		w = new IndexWriter(index, config);
	}
	
	public void addDoc_to_Index(Documents cur_doc) throws IOException
	{
		Document doc = new Document();
		doc.add(new TextField("title", cur_doc.title, Field.Store.YES));
		doc.add(new TextField("word", cur_doc.words, Field.Store.YES));
		doc.add(new TextField("author", cur_doc.author, Field.Store.YES));
		doc.add(new StringField("id", cur_doc.id, Field.Store.YES));
		doc.add(new TextField("biography", cur_doc.biography, Field.Store.YES));
		w.addDocument(doc);
	}
	
	public void closeIndexWriter() throws IOException
	{
		w.close();
	}

}

package xiang;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

	
	public static void main(String[] args) {
		try {
			Documenizer dmz = new Documenizer("cran/cran.all.1400");
			Querizer qrz = new Querizer("cran/cran.qry");
			Indexer idxer = new Indexer("standard");
			TrecEvalRunner trecEvalRunner = new TrecEvalRunner();
			runSearchEngine(dmz, qrz, idxer);
			trecEvalRunner.run("cran/QRelsCorrectedforTRECeval", "standard/bm_output.txt", "standard/bm_output_trec_eval.txt");
			trecEvalRunner.run("cran/QRelsCorrectedforTRECeval", "standard/vsm_output.txt", "standard/vsm_output_trec_eval.txt");
			Indexer customIndexer = new Indexer("custom");
			customIndexer.analyzer = new CustomAnalyzer();
			runSearchEngine(dmz, qrz, customIndexer);
			trecEvalRunner.run("cran/QRelsCorrectedforTRECeval", "custom/bm_output.txt", "custom/bm_output_trec_eval.txt");
			trecEvalRunner.run("cran/QRelsCorrectedforTRECeval", "custom/vsm_output.txt", "custom/vsm_output_trec_eval.txt");

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (ParseException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	static void runSearchEngine(Documenizer dmz, Querizer qrz, Indexer idxer) throws IOException, ParseException {


		dmz.createDocumentVector();
		for(int i=0;i<dmz.docs.size();i++)
            idxer.addDoc_to_Index(dmz.docs.elementAt(i));
		idxer.closeIndexWriter();

		Searcher srch = new Searcher(idxer.analyzer,idxer.index);
		qrz.createQueryVector();
		srch.initializeWriters(idxer.path);
		for(int i=0;i<qrz.queries.size();i++)
        {
            String q = qrz.queries.elementAt(i).words;
            int id = qrz.queries.elementAt(i).id;
            System.out.println("\nQUERY ID : "+ id);
            srch.searchQueryVector(q,i+1);
            srch.searchQueryBM25(q,i+1);
            //srch.searchQueryBoolean(q,i+1);
        }
		srch.closeWriters();
	}

}

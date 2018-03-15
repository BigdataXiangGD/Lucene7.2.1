package xiang;

import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 */
public class TrecEvalRunner {

    public void run(String qrel, String result, String trecEvalResult) {
        try {
            //"-l", "3",
            ProcessBuilder pb = new ProcessBuilder("./trec_eval",  "-m", "all_trec", qrel, result);

//            pb.directory(Paths.get("").toFile());
            Process process = pb.start();

            InputStream processInputStream = process.getInputStream();
            Files.copy(processInputStream, Paths.get(trecEvalResult), REPLACE_EXISTING);
            IOUtils.close(processInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

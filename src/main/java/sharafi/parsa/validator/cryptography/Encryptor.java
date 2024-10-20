package sharafi.parsa.validator.cryptography;

import java.io.File;
import java.io.Reader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service
public class Encryptor {
	
	private static SecretKey key = new SecretKeySpec(Arrays.copyOf(Base64.getDecoder().decode("CryptographicKey"), 16), "AES");
	private static Cipher cipher;
	static { try { cipher = Cipher.getInstance("AES"); cipher.init(Cipher.ENCRYPT_MODE, key); } catch (Exception e) {e.printStackTrace();} }
	private static Logger logger = LoggerFactory.getLogger(Encryptor.class);

	@Async("encryptionTaskExecutor")
	public CompletableFuture<File> encrypt(File file, boolean isAccountsFile) {
		
		logger.info("Encrypting Data By " + Thread.currentThread().getName());
		File encryptedFile = new File(Paths.get("target", "encrypted-" + file.getName()).toString());
		
        try ( BufferedWriter writer = Files.newBufferedWriter(encryptedFile.toPath());
        		Reader reader = Files.newBufferedReader(file.toPath());
				CSVParser csvParser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader);
        		CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        ) {
        	csvPrinter.printRecord(csvParser.getHeaderNames());
        	
        	if (isAccountsFile)
        		encryptAccounts(csvParser, csvPrinter);
        	else
        		encryptCustomers(csvParser, csvPrinter);
        	
            csvPrinter.flush();
            
        } catch (IOException e) {
        	logger.error("IO exception in encryptor");
        } finally {
        	try {
        		Files.deleteIfExists(file.toPath());
        	} catch (IOException e) {
        		logger.error("error while deleting files after encryption");
        	}
        }
        
        return CompletableFuture.completedFuture(encryptedFile);
    }
	
	private void encryptAccounts(CSVParser csvParser, CSVPrinter csvPrinter) throws IOException {
		for (CSVRecord csvRecord : csvParser) {
			if (csvRecord.size() == 7)
        		csvPrinter.printRecord(csvRecord.get(0), encrypt(csvRecord.get(1)), csvRecord.get(2), csvRecord.get(3), 
        				csvRecord.get(4), csvRecord.get(5), encrypt(csvRecord.get(6)));
			else
				csvPrinter.printRecord(csvRecord);
		}
	}
	
	private void encryptCustomers(CSVParser csvParser, CSVPrinter csvPrinter) throws IOException {
		for (CSVRecord csvRecord : csvParser) {
			if (csvRecord.size() == 8)
    			csvPrinter.printRecord(csvRecord.get(0), csvRecord.get(1), encrypt(csvRecord.get(2)), encrypt(csvRecord.get(3)), 
    					csvRecord.get(4), csvRecord.get(5), encrypt(csvRecord.get(6)), csvRecord.get(7));
			else
				csvPrinter.printRecord(csvRecord);
		}
	}
	
	public String encrypt(String input) {
	   try {
		   byte[] cipherText = cipher.doFinal(input.getBytes("UTF-8"));
		   return Base64.getEncoder().encodeToString(cipherText);
		} catch (Exception e) { throw new RuntimeException("Error occured while encrypting data", e); }
   }
}
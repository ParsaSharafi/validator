package sharafi.parsa.validator.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sharafi.parsa.validator.cryptography.Encryptor;
import sharafi.parsa.validator.service.AccountService;
import sharafi.parsa.validator.service.CustomerService;

/*
 * will run every five seconds and process a new input file
 */

@Component
public class ScheduledTask {

	private final CustomerService customerService;
	private final AccountService accountService;
	private final Encryptor encryptor;
	
	public ScheduledTask(CustomerService customerService, AccountService accountService, Encryptor encryptor) {
		this.customerService = customerService;
		this.accountService = accountService;
		this.encryptor = encryptor;
	}
	
	public static volatile Queue<File[]> filesQueue = new LinkedList<>();
	private static Logger logger = LoggerFactory.getLogger(Encryptor.class);
	public static long processCounter = 0;
	
	@Async("scheduledTaskExecutor")
	@Scheduled(fixedRate = 5_000)
	public void run() {
		
		if(filesQueue.isEmpty())
			return;
		
		logger.info("new process started");
		File[] workingFiles = filesQueue.remove();
		
		long start = System.currentTimeMillis();
		
		List<CompletableFuture<File>> accountEncryptionFutures = new ArrayList<>();
		List<CompletableFuture<File>> customerEncryptionFutures = new ArrayList<>();
		
		for (File file : FileSplitter.split(workingFiles[0]))
			accountEncryptionFutures.add(encryptor.encrypt(file, true));
		for (File file : FileSplitter.split(workingFiles[1]))
			customerEncryptionFutures.add(encryptor.encrypt(file, false));
		
		for (CompletableFuture<File> future : accountEncryptionFutures)
			future.join();
		for (CompletableFuture<File> future : customerEncryptionFutures)
			future.join();
		
		long end = System.currentTimeMillis();
		logger.info("encryption finished in {} millisecond", end - start);
		start = end;
		
		List<CompletableFuture<String>> validationFutures = new ArrayList<>();
		try {
			
			BufferedWriter errorWriter = new BufferedWriter(new FileWriter(workingFiles[2], true));
			PrintWriter errorPrinter = new PrintWriter(errorWriter);
			errorPrinter.print("[");
			
			for (CompletableFuture<File> future : customerEncryptionFutures)
				validationFutures.add(customerService.saveCustomers(future.get(), errorPrinter));
			for (CompletableFuture<String> future : validationFutures)
				future.join();
			
			logger.info("finished validating customer records and inserting them into database in {} seconds", System.currentTimeMillis() - start);
			validationFutures.clear();
			
			for (CompletableFuture<File> future : accountEncryptionFutures)
				validationFutures.add(accountService.saveAccounts(future.get(), errorPrinter));
			for (CompletableFuture<String> future : validationFutures)
				future.join();
			
			errorPrinter.print("]");
			errorPrinter.close();
			
		} catch (InterruptedException | ExecutionException | IOException e) {
			logger.error("scheduled task interrupted");
		} finally {
			try {
				Files.deleteIfExists(workingFiles[0].toPath());
				Files.deleteIfExists(workingFiles[1].toPath());
			} catch (IOException e) {
				logger.error("error while deleting files after encryption");
			}
		}
		
		processCounter++;
		logger.info("process {} finished in {} millisecond", processCounter, System.currentTimeMillis() - start);
	}
}

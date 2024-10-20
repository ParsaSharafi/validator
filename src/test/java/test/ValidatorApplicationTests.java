package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sharafi.parsa.validator.ValidatorApplication;
import sharafi.parsa.validator.config.ScheduledTask;

/*
 * validating 5 pairs of files with around 0.1% chance of error which will be generated in target folder
 */

@SpringBootTest(classes = ValidatorApplication.class)
class ValidatorApplicationTests {
	
	@Test
	void test() throws IOException, InterruptedException {
		
		int recordsCount = 100_000;
		int filePairCount = 5;
		
		for (int i = 1; i <= filePairCount; i++)
			generateAndSend(recordsCount, i);
		
		long start = System.currentTimeMillis();
		while (ScheduledTask.processCounter < filePairCount)
			Thread.sleep(1000);
		System.out.println("ALL DONE IN " + (System.currentTimeMillis() - start) + " MILLISECONDS");
	}
	
	void generateAndSend(int recordsCount, int filePairCount) throws IOException {
		
		StringBuilder accounts = new StringBuilder("RECORD_NUMBER,ACCOUNT_NUMBER,ACCOUNT_TYPE,"
				+ "ACCOUNT_CUSTOMER_ID,ACCOUNT_LIMIT,ACCOUNT_OPEN_DATE,ACCOUNT_BALANCE\n");
		StringBuilder customers = new StringBuilder("RECORD_NUMBER,CUSTOMER_ID,CUSTOMER_NAME,CUSTOMER_SURNAME,"
				+ "CUSTOMER_ADDRESS,CUSTOMER_ZIP_CODE,CUSTOMER_NATIONAL_ID,CUSTOMER_BIRTH_DATE\n");
		Random random = new Random();
		int rand = 0;

		String comma = ",";
		String zero = "0";
		String[] types = {"savings", "Recurring deposit", "Fixed deposit account"};
		String dash = "-";
		String newline = "\n";
		String name = "Name";
		String surname = "Surname";
		String address = "\"Some Province, Some City, Street ";
		String quote = "\"";
		
		int from = (filePairCount - 1) * recordsCount + 1;
		int to = recordsCount * filePairCount;
		for(int i = from; i <= to; i++)
		{
			accounts.append(i);
			accounts.append(comma);
			accounts.append(zero.repeat(22 - (int)(Math.log10(i) + 1)));
			accounts.append(i);
			accounts.append(comma);
			accounts.append(types[random.nextInt(3)]);
			accounts.append(comma);
			accounts.append(i);
			accounts.append(comma);
			accounts.append(random.nextInt(1000) + 1000);
			accounts.append(comma);
			accounts.append(random.nextInt(24) + 2000);
			accounts.append(dash);
			rand = random.nextInt(12) + 1;
			if (rand < 10)
				accounts.append(zero);
			accounts.append(rand);
			accounts.append(dash);
			rand = random.nextInt(28) + 1;
			if (rand < 10)
				accounts.append(zero);
			accounts.append(rand);
			accounts.append(comma);
			accounts.append(random.nextInt(1050));
			accounts.append(newline);
			
			customers.append(i);
			customers.append(comma);
			customers.append(i);
			customers.append(comma);
			customers.append(name);
			customers.append(random.nextInt(1000));
			customers.append(comma);
			customers.append(surname);
			customers.append(random.nextInt(1000));
			customers.append(comma);
			customers.append(address);
			customers.append(random.nextInt(1000));
			customers.append(quote);
			customers.append(comma);
			customers.append(random.nextInt(1_000_000_000) + 1_000_000_000);
			customers.append(comma);
			customers.append(1_000_000_000 + i);
			customers.append(comma);
			rand = random.nextInt(100);
			customers.append(1950 + (rand == 50 ? random.nextInt(50) : random.nextInt(45)));
			customers.append(dash);
			rand = random.nextInt(12) + 1;
			if (rand < 10)
				customers.append(zero);
			customers.append(rand);
			customers.append(dash);
			rand = random.nextInt(28) + 1;
			if (rand < 10)
				customers.append(zero);
			customers.append(rand);
			customers.append(newline);
		}
		
		File accountsFile = new File(Paths.get("target", "AccountsTest" + filePairCount + ".csv").toString());
		FileWriter aWriter = new FileWriter(accountsFile);
		aWriter.write(accounts.toString());
		aWriter.close();
		
		File customersFile = new File(Paths.get("target", "CustomersTest" + filePairCount + ".csv").toString());
		FileWriter cWriter = new FileWriter(customersFile);
		cWriter.write(customers.toString());
		cWriter.close();
		
		File errors = new File(Paths.get("target", "ErrorsTest" + filePairCount + ".json").toString());
		Files.deleteIfExists(errors.toPath());
		
		File[] workingFiles = {accountsFile, customersFile, errors};
		ScheduledTask.filesQueue.add(workingFiles);
	}
}

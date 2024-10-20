package sharafi.parsa.validator.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import sharafi.parsa.validator.cryptography.Decryptor;
import sharafi.parsa.validator.model.Account;
import sharafi.parsa.validator.repository.AccountRepository;
import sharafi.parsa.validator.validation.OnParsedValidationGroup;
import sharafi.parsa.validator.validation.ParsableValidationGroup;

@Service
public class AccountService {
	
	private final AccountRepository accountRepository;
	private final CustomerService customerService;
	private final ErrorService errorService;
	private final Decryptor decryptor;
	private static final String HEADER_MISMATCH_MESSAGE = "values count does not match expected account headers";
	private static final String CUSTOMER_ID_MESSAGE = "account customer id is registered with another account";
	
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();
	Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	public AccountService(AccountRepository accountRepository, CustomerService customerService, ErrorService errorService, Decryptor decryptor) {
		this.accountRepository = accountRepository;
		this.customerService = customerService;
		this.errorService = errorService;
		this.decryptor = decryptor;
	}
	
	@Async("accountsTaskExecutor")
	public CompletableFuture<String> saveAccounts(File accountsFile, PrintWriter errorPrinter) {

		logger.info("Saving Accounts By " + Thread.currentThread().getName());

		try (Reader reader = Files.newBufferedReader(accountsFile.toPath());
				CSVParser csvParser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader);) {

			for (CSVRecord csvRecord : csvParser) {
				
				String recordNumber = csvRecord.get("RECORD_NUMBER");
				
				if (csvRecord.size() != 7) {
					errorService.printError(recordNumber, HEADER_MISMATCH_MESSAGE, errorPrinter);
					continue;
				}
				
				Account account = new Account();
				
				account.setRecordNumber(recordNumber);
				
				account.setAccountNumber(decryptor.decrypt(csvRecord.get("ACCOUNT_NUMBER")));
				
				account.setAccountType(csvRecord.get("ACCOUNT_TYPE"));
				
				account.setAccountCustomerId(customerService.getCustomer(csvRecord.get("ACCOUNT_CUSTOMER_ID")));
				
				String accountLimitString = csvRecord.get("ACCOUNT_LIMIT");
				account.setAccountLimitString(accountLimitString);
				
				String accountOpenDateString = csvRecord.get("ACCOUNT_OPEN_DATE");
				account.setAccountOpenDateString(accountOpenDateString);
				
				String accountBalanceString = decryptor.decrypt(csvRecord.get("ACCOUNT_BALANCE"));
				account.setAccountBalanceString(accountBalanceString);
				
				Set<ConstraintViolation<Account>> violations = validator.validate(account, ParsableValidationGroup.class);
				if (violations.isEmpty()) {
					account.setAccountLimit(Long.parseLong(accountLimitString));
					account.setAccountOpenDate(LocalDate.parse(accountOpenDateString));
					account.setAccountBalance(Long.parseLong(accountBalanceString));
					violations = validator.validate(account, OnParsedValidationGroup.class);
				}
				if (violations.isEmpty())
					violations = validator.validate(account);
				else
					Optional.ofNullable(validator.validate(account)).ifPresent(violations::addAll);
				
				if (violations.isEmpty()) {
					try {
						accountRepository.save(account);
					} catch (DataIntegrityViolationException ex) {
						errorService.printError(recordNumber, CUSTOMER_ID_MESSAGE, errorPrinter);
					}
					continue;
				}
				
				for (ConstraintViolation<Account> violation : violations)
					errorService.printError(recordNumber, violation.getMessage(), errorPrinter);
			}
		} catch (IOException e) {
			logger.error("IO exception in account service");
		} finally {
			try {
				Files.deleteIfExists(accountsFile.toPath());
			} catch (IOException e) {
				logger.error("error while deleting files after saving accounts");
			}
		}
		return CompletableFuture.completedFuture("done");
	}
}

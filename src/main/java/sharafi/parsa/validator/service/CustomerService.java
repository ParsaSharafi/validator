package sharafi.parsa.validator.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import sharafi.parsa.validator.cryptography.Encryptor;
import sharafi.parsa.validator.model.Account;
import sharafi.parsa.validator.model.Customer;
import sharafi.parsa.validator.model.HighBalanceDto;
import sharafi.parsa.validator.repository.CustomerRepository;
import sharafi.parsa.validator.validation.OnParsedValidationGroup;
import sharafi.parsa.validator.validation.ParsableValidationGroup;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final ErrorService errorService;
	private final Decryptor decryptor;
	private final Encryptor encryptor;
	private static final String HEADER_MISMATCH_MESSAGE = "values count does not match expected customer headers";
	private static final String NATIONAL_ID_MESSAGE = "customer national id must be unique";
	
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();
	Logger logger = LoggerFactory.getLogger(CustomerService.class);
	
	public CustomerService(CustomerRepository customerRepository, ErrorService errorService, Decryptor decryptor, Encryptor encryptor) {
		this.customerRepository = customerRepository;
		this.errorService = errorService;
		this.decryptor = decryptor;
		this.encryptor = encryptor;
	}
	
	@Async("customersTaskExecutor")
	public CompletableFuture<String> saveCustomers(File customersFile, PrintWriter errorPrinter) throws DataIntegrityViolationException {

		logger.info("Saving Customers By " + Thread.currentThread().getName());
		
		try ( Reader reader = Files.newBufferedReader(customersFile.toPath());
				CSVParser csvParser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader); ) {

			for (CSVRecord csvRecord : csvParser) {
				
				String recordNumber = csvRecord.get("RECORD_NUMBER");
				
				if (csvRecord.size() != 8) {
					errorService.printError(recordNumber, HEADER_MISMATCH_MESSAGE, errorPrinter);
					continue;
				}
				
				Customer customer = new Customer();
				
				customer.setRecordNumber(recordNumber);
				
				customer.setCustomerId(csvRecord.get("CUSTOMER_ID"));
				
				customer.setCustomerName(decryptor.decrypt(csvRecord.get("CUSTOMER_NAME")));
				
				customer.setCustomerSurname(decryptor.decrypt(csvRecord.get("CUSTOMER_SURNAME")));
				
				customer.setCustomerAddress(csvRecord.get("CUSTOMER_ADDRESS"));
				
				customer.setCustomerZipCode(csvRecord.get("CUSTOMER_ZIP_CODE"));
				
				customer.setCustomerNationalId(decryptor.decrypt(csvRecord.get("CUSTOMER_NATIONAL_ID")));
				
				String customerBirthDateString = csvRecord.get("CUSTOMER_BIRTH_DATE");
				customer.setCustomerBirthDateString(customerBirthDateString);
				
				Set<ConstraintViolation<Customer>> violations = validator.validate(customer, ParsableValidationGroup.class);
				if (violations.isEmpty()) {
					customer.setCustomerBirthDate(LocalDate.parse(customerBirthDateString));
					violations = validator.validate(customer, OnParsedValidationGroup.class);
				}
				if (violations.isEmpty())
					violations = validator.validate(customer);
				else
					Optional.ofNullable(validator.validate(customer)).ifPresent(violations::addAll);
				
				if (violations.isEmpty()) {
					try {
						customerRepository.save(customer);
					} catch (DataIntegrityViolationException ex) {
						errorService.printError(recordNumber, NATIONAL_ID_MESSAGE, errorPrinter);
					}
					continue;
				}
				
				for (ConstraintViolation<Customer> violation : violations)
					errorService.printError(recordNumber, violation.getMessage(), errorPrinter);
			}
		} catch (IOException e) {
			logger.error("IO exception in customer service");
		}
		finally {
			try {
				Files.deleteIfExists(customersFile.toPath());
			} catch (IOException e) {
				logger.error("error while deleting files after saving customers");
			}
		}
		return CompletableFuture.completedFuture("done");
	}
	
	public Customer getCustomer(String id) {
		return customerRepository.findById(id).orElse(null);
	}
	
	public List<HighBalanceDto> getHighBalanceCustomers() {
		
		List<HighBalanceDto> HighBalanceDtos = new ArrayList<>();
		List<Customer> customers = customerRepository.findHighBalanceCustomers();
		
		for (Customer customer : customers) {
			Account account = customer.getAccount();
			HighBalanceDto HighBalanceDto = new HighBalanceDto(customer.getCustomerId(), customer.getCustomerName(), customer.getCustomerSurname(),
					customer.getCustomerNationalId(), encryptor.encrypt(account.getAccountNumber()),
					String.valueOf(account.getAccountOpenDate()), encryptor.encrypt(String.valueOf(account.getAccountBalance())));
			HighBalanceDtos.add(HighBalanceDto);
		}
		return HighBalanceDtos;
	}
}

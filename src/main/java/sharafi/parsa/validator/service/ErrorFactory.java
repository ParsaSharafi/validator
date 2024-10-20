package sharafi.parsa.validator.service;

import sharafi.parsa.validator.model.Error;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class ErrorFactory {

	public static Error getError(String recordNumber, String errorDescription) {
		
		final String accountsFileName = "Accounts.csv";
		final String customersFileName = "Customers.csv";
		
		return switch (errorDescription) {
			case "account record number must not be blank" -> 
				new Error(accountsFileName, recordNumber, 1, "null input error", errorDescription, LocalDateTime.now().toString());
			case "customer record number must not be blank" -> 
				new Error(customersFileName, recordNumber, 2, "null input error", errorDescription, LocalDateTime.now().toString());
			case "customer id must not be blank" -> 
				new Error(customersFileName, recordNumber, 3, "null input error", errorDescription, LocalDateTime.now().toString());
			case "customer name must not be blank" -> 
				new Error(customersFileName, recordNumber, 4, "null input error", errorDescription, LocalDateTime.now().toString());
			case "customer surname must not be blank" -> 
				new Error(customersFileName, recordNumber, 5, "null input error", errorDescription, LocalDateTime.now().toString());
			case "customer address must not be blank" -> 
				new Error(customersFileName, recordNumber, 6, "null input error", errorDescription, LocalDateTime.now().toString());
			case "customer zip code must not be blank" -> 
				new Error(customersFileName, recordNumber, 7, "null input error", errorDescription, LocalDateTime.now().toString());
			case "customer not found" -> 
				new Error(accountsFileName, recordNumber, 8, "not found error", errorDescription, LocalDateTime.now().toString());
			case "values count does not match expected account headers" -> 
				new Error(accountsFileName, recordNumber, 9, "invalid input error", errorDescription, LocalDateTime.now().toString());
			case "values count does not match expected customer headers" -> 
				new Error(customersFileName, recordNumber, 10, "invalid input error", errorDescription, LocalDateTime.now().toString());
			case "customer national id must be 10 digits" -> 
				new Error(customersFileName, recordNumber, 11, "invalid input error", errorDescription, LocalDateTime.now().toString());
			case "account number must be 22 digits with leading zeros" -> 
				new Error(accountsFileName, recordNumber, 12, "invalid input error", errorDescription, LocalDateTime.now().toString());
			case "invalid value for account type" -> 
				new Error(accountsFileName, recordNumber, 13, "invalid input error", errorDescription, LocalDateTime.now().toString());
			case "invalid value for account limit" -> 
				new Error(accountsFileName, recordNumber, 14, "invalid input error", errorDescription, LocalDateTime.now().toString());
			case "invalid value for account balance" -> 
				new Error(accountsFileName, recordNumber, 15, "invalid input error", errorDescription, LocalDateTime.now().toString());
			case "invalid value for account open date" -> 
				new Error(accountsFileName, recordNumber, 16, "invalid input error", errorDescription, LocalDateTime.now().toString());
			case "invalid value for customer birth date" -> 
				new Error(customersFileName, recordNumber, 17, "invalid input error", errorDescription, LocalDateTime.now().toString());
			case "customer birth date must be before 1995" -> 
				new Error(customersFileName, recordNumber, 18, "illegal input error", errorDescription, LocalDateTime.now().toString());
			case "customer national id must be unique" -> 
				new Error(customersFileName, recordNumber, 19, "illegal input error", errorDescription, LocalDateTime.now().toString());
			case "account customer id is registered with another account" -> 
				new Error(accountsFileName, recordNumber, 20, "illegal input error", errorDescription, LocalDateTime.now().toString());
			case "account balance must be lower than account limit" -> 
				new Error(accountsFileName, recordNumber, 21, "illegal input error", errorDescription, LocalDateTime.now().toString());
			default -> null;
		};
	}
}

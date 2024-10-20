package sharafi.parsa.validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HighBalanceDto {

	public HighBalanceDto(String customerId, String customerName, String customerSurname, String customerNationalId,
			String accountNumber, String accountOpenDate, String accountBalance) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerSurname = customerSurname;
		this.customerNationalId = customerNationalId;
		this.accountNumber = accountNumber;
		this.accountOpenDate = accountOpenDate;
		this.accountBalance = accountBalance;
	}
	
	public HighBalanceDto() {
		
	}
	
	@JsonProperty("CUSTOMER_ID")
	private String customerId;
	
	@JsonProperty("CUSTOMER_NAME")
	private String customerName;
	
	@JsonProperty("CUSTOMER_SURNAME")
	private String customerSurname;
	
	@JsonProperty("CUSTOMER_NATIONAL_ID")
	private String customerNationalId;
	
	@JsonProperty("ACCOUNT_NUMBER")
	private String accountNumber;
	
	@JsonProperty("ACCOUNT_OPEN_DATE")
	private String accountOpenDate;
	
	@JsonProperty("ACCOUNT_BALANCE")
	private String accountBalance;
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerSurname() {
		return customerSurname;
	}

	public void setCustomerSurname(String customerSurname) {
		this.customerSurname = customerSurname;
	}

	public String getCustomerNationalId() {
		return customerNationalId;
	}

	public void setCustomerNationalId(String customerNationalId) {
		this.customerNationalId = customerNationalId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountOpenDate() {
		return accountOpenDate;
	}

	public void setAccountOpenDate(String accountOpenDate) {
		this.accountOpenDate = accountOpenDate;
	}

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

	@Override
	public String toString() {
		return "SelectorDto [customerId=" + customerId + ", customerName=" + customerName + ", customerSurname="
				+ customerSurname + ", customerNationalId=" + customerNationalId + ", accountNumber=" + accountNumber
				+ ", accountOpenDate=" + accountOpenDate + ", accountBalance=" + accountBalance + "]";
	}
	
}
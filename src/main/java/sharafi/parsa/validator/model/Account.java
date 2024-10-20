package sharafi.parsa.validator.model;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import sharafi.parsa.validator.validation.AccountTypeValidation;
import sharafi.parsa.validator.validation.BalanceValidation;
import sharafi.parsa.validator.validation.DateValidation;
import sharafi.parsa.validator.validation.OnParsedValidationGroup;
import sharafi.parsa.validator.validation.ParsableValidationGroup;

@Entity
@BalanceValidation(groups = OnParsedValidationGroup.class)
public class Account {
	
	public Account(@NotBlank(message = "account record number must not be blank") String recordNumber,
			@Pattern(regexp = "^00[0-9]{20}$", message = "account number must be 22 digits with leading zeros") String accountNumber,
			String accountType, @NotNull(message = "customer not found") Customer accountCustomerId, long accountLimit,
			LocalDate accountOpenDate, long accountBalance, 
			@Pattern(regexp = "^[0-9]+$", message = "invalid value for account limit") String accountLimitString,
			String accountOpenDateString,
			@Pattern(regexp = "^[0-9]+$", message = "invalid value for account balance") String accountBalanceString) {
		super();
		this.recordNumber = recordNumber;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.accountCustomerId = accountCustomerId;
		this.accountLimit = accountLimit;
		this.accountOpenDate = accountOpenDate;
		this.accountBalance = accountBalance;
		this.accountLimitString = accountLimitString;
		this.accountOpenDateString = accountOpenDateString;
		this.accountBalanceString = accountBalanceString;
	}

	public Account() {
		
	}

	@Column(name = "account_record_number")
	@NotBlank(message = "account record number must not be blank")
	private String recordNumber;
	
	@Id
	@Column(length = 22, name = "account_number")
	@Pattern(regexp = "^00[0-9]{20}$", message = "account number must be 22 digits with leading zeros")
	private String accountNumber;
	
	@Column(length = 21, name = "account_type")
	@AccountTypeValidation
	private String accountType;
	
	@OneToOne(optional = false)
    @JoinColumn(name = "account_customer_id", referencedColumnName = "customer_id")
	@NotNull(message = "customer not found")
	private Customer accountCustomerId;
	
	@Column(name = "account_limit")
	private long accountLimit;
	
	@Column(name = "account_open_date")
    private LocalDate accountOpenDate;
	
	@Column(name = "account_balance")
	private long accountBalance;
	
	@Transient
	@Pattern(regexp = "^[0-9]+$", message = "invalid value for account limit", groups = ParsableValidationGroup.class)
	private String accountLimitString;
	
	@Transient
	@DateValidation(message = "invalid value for account open date", groups = ParsableValidationGroup.class)
	private String accountOpenDateString;
	
	@Transient
	@Pattern(regexp = "^[0-9]+$", message = "invalid value for account balance", groups = ParsableValidationGroup.class)
	private String accountBalanceString;

	public String getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(String recordNumber) {
		this.recordNumber = recordNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Customer getAccountCustomerId() {
		return accountCustomerId;
	}

	public void setAccountCustomerId(Customer accountCustomerId) {
		this.accountCustomerId = accountCustomerId;
	}

	public long getAccountLimit() {
		return accountLimit;
	}

	public void setAccountLimit(long accountLimit) {
		this.accountLimit = accountLimit;
	}

	public LocalDate getAccountOpenDate() {
		return accountOpenDate;
	}

	public void setAccountOpenDate(LocalDate accountOpenDate) {
		this.accountOpenDate = accountOpenDate;
	}

	public long getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(long accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getAccountLimitString() {
		return accountLimitString;
	}

	public void setAccountLimitString(String accountLimitString) {
		this.accountLimitString = accountLimitString;
	}

	public String getAccountOpenDateString() {
		return accountOpenDateString;
	}

	public void setAccountOpenDateString(String accountOpenDateString) {
		this.accountOpenDateString = accountOpenDateString;
	}

	public String getAccountBalanceString() {
		return accountBalanceString;
	}

	public void setAccountBalanceString(String accountBalanceString) {
		this.accountBalanceString = accountBalanceString;
	}

	@Override
	public String toString() {
		return "Account [recordNumber=" + recordNumber + ", accountNumber=" + accountNumber + ", accountType="
				+ accountType + ", accountCustomerId=" + accountCustomerId + ", accountLimit=" + accountLimit
				+ ", accountOpenDate=" + accountOpenDate + ", accountBalance=" + accountBalance
				+ ", accountLimitString=" + accountLimitString
				+ ", accountOpenDateString=" + accountOpenDateString + ", accountBalanceString=" + accountBalanceString
				+ "]";
	}
}

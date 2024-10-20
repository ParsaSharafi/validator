package sharafi.parsa.validator.model;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import sharafi.parsa.validator.validation.BirthDateValidation;
import sharafi.parsa.validator.validation.DateValidation;
import sharafi.parsa.validator.validation.OnParsedValidationGroup;
import sharafi.parsa.validator.validation.ParsableValidationGroup;

@Entity
public class Customer {
	
	public Customer(@NotBlank(message = "customer record number must not be blank") String recordNumber,
			@NotBlank(message = "customer id must not be blank") String customerId,
			@NotBlank(message = "customer name must not be blank") String customerName,
			@NotBlank(message = "customer surname must not be blank") String customerSurname,
			@NotBlank(message = "customer address must not be blank") String customerAddress,
			@Pattern(regexp = "^[0-9]{10}$", message = "customer zip code must be 10 digits") String customerZipCode,
			@Pattern(regexp = "^[0-9]{10}$", message = "customer national id must be 10 digits") String customerNationalId,
			LocalDate customerBirthDate, Account account, String customerBirthDateString) {
		super();
		this.recordNumber = recordNumber;
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerSurname = customerSurname;
		this.customerAddress = customerAddress;
		this.customerZipCode = customerZipCode;
		this.customerNationalId = customerNationalId;
		this.customerBirthDate = customerBirthDate;
		this.account = account;
		this.customerBirthDateString = customerBirthDateString;
	}

	public Customer() {
		
	}

	@Column(name = "customer_record_number")
	@NotBlank(message = "customer record number must not be blank")
	private String recordNumber;
	
	@Id
	@Column(name = "customer_id")
	@NotBlank(message = "customer id must not be blank")
	private String customerId;
	
	@Column(length = 50, name = "customer_name")
	@NotBlank(message = "customer name must not be blank")
	private String customerName;
	
	@Column(length = 50, name = "customer_surname")
	@NotBlank(message = "customer surname must not be blank")
	private String customerSurname;
	
	@Column(name = "customer_address")
	@NotBlank(message = "customer address must not be blank")
	private String customerAddress;
	
	@Column(length = 10, name = "customer_zip_code")
	@NotBlank(message = "customer zip code must not be blank")
	private String customerZipCode;
	
	@Column(unique = true, length = 10, name = "customer_national_id")
	@Pattern(regexp = "^[0-9]{10}$", message = "customer national id must be 10 digits")
	private String customerNationalId;
	
	@Column(name = "customer_birth_date")
	@BirthDateValidation(groups = OnParsedValidationGroup.class)
	private LocalDate customerBirthDate;
	
	@OneToOne(mappedBy = "accountCustomerId")
	private Account account;
	
	@Transient     
	@DateValidation(message = "invalid value for customer birth date", groups = ParsableValidationGroup.class)
	private String customerBirthDateString;

	public String getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(String recordNumber) {
		this.recordNumber = recordNumber;
	}

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

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerZipCode() {
		return customerZipCode;
	}

	public void setCustomerZipCode(String customerZipCode) {
		this.customerZipCode = customerZipCode;
	}

	public String getCustomerNationalId() {
		return customerNationalId;
	}

	public void setCustomerNationalId(String customerNationalId) {
		this.customerNationalId = customerNationalId;
	}

	public LocalDate getCustomerBirthDate() {
		return customerBirthDate;
	}

	public void setCustomerBirthDate(LocalDate customerBirthDate) {
		this.customerBirthDate = customerBirthDate;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getCustomerBirthDateString() {
		return customerBirthDateString;
	}

	public void setCustomerBirthDateString(String customerBirthDateString) {
		this.customerBirthDateString = customerBirthDateString;
	}

	@Override
	public String toString() {
		return "Customer [recordNumber=" + recordNumber + ", customerId=" + customerId + ", customerName="
				+ customerName + ", customerSurname=" + customerSurname + ", customerAddress=" + customerAddress
				+ ", customerZipCode=" + customerZipCode + ", customerNationalId=" + customerNationalId
				+ ", customerBirthDate=" + customerBirthDate + ", account=" + account + ", customerBirthDateString="
				+ customerBirthDateString + "]";
	}
}

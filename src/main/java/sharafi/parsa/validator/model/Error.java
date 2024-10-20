package sharafi.parsa.validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

	public Error(String fileName, String recordNumber, int errorCode, String errorClassificationName,
			String errorDescription, String errorDate) {
		super();
		this.fileName = fileName;
		this.recordNumber = recordNumber;
		this.errorCode = errorCode;
		this.errorClassificationName = errorClassificationName;
		this.errorDescription = errorDescription;
		this.errorDate = errorDate;
	}
	
	public Error() {
		
	}

	@JsonProperty("FILE_NAME")
	private String fileName;
	
	@JsonProperty("RECORD_NUMBER")
	private String recordNumber;
	
	@JsonProperty("ERROR_CODE")
	private int errorCode;
	
	@JsonProperty("ERROR_CLASSIFICATION_NAME")
	private String errorClassificationName;
	
	@JsonProperty("ERROR_DESCRIPTION")
	private String errorDescription;
	
	@JsonProperty("ERROR_DATE")
	private String errorDate;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(String recordNumber) {
		this.recordNumber = recordNumber;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorClassificationName() {
		return errorClassificationName;
	}

	public void setErrorClassificationName(String errorClassificationName) {
		this.errorClassificationName = errorClassificationName;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getErrorDate() {
		return errorDate;
	}

	public void setErrorDate(String date) {
		this.errorDate = date;
	}

	@Override
	public String toString() {
		return "Error [fileName=" + fileName + ", recordNumber=" + recordNumber + ", errorCode=" + errorCode
				+ ", errorClassificationName=" + errorClassificationName + ", errorDescription=" + errorDescription
				+ ", errorDate=" + errorDate + "]";
	}
}

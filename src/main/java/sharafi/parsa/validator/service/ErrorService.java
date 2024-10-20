package sharafi.parsa.validator.service;

import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class ErrorService {

	private static ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
	
	public synchronized void printError(String recordNumber, String errorDescription, PrintWriter errorPrinter) {
		
		try {
			String json = mapper.writeValueAsString(ErrorFactory.getError(recordNumber, errorDescription));
			errorPrinter.print(json);
			errorPrinter.println(',');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

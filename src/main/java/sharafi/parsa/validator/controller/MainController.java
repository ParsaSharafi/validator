package sharafi.parsa.validator.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sharafi.parsa.validator.config.ScheduledTask;
import sharafi.parsa.validator.cryptography.Encryptor;
import sharafi.parsa.validator.model.HighBalanceDto;
import sharafi.parsa.validator.service.CustomerService;

@RestController
@RequestMapping("/validator")
public class MainController {
	
	private final CustomerService customerService;
	private static Logger logger = LoggerFactory.getLogger(Encryptor.class);
	private static long counter = 0;
	
	public MainController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping
	public List<HighBalanceDto> showHighBalanceCustomers() {
		return customerService.getHighBalanceCustomers();
	}
	
	@PostMapping()
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile[] input, RedirectAttributes redirectAttributes) {
		try {
			if (input[0].isEmpty() || input[1].isEmpty() || input.length != 2)
				return new ResponseEntity<>("input does not match expected 2 non-empty files", HttpStatus.BAD_REQUEST);
			
			boolean accountsFileFirst;
			if (input[0].getOriginalFilename().equals("Accounts.csv") && input[1].getOriginalFilename().equals("Customers.csv"))
				accountsFileFirst = true;
			else if (input[1].getOriginalFilename().equals("Accounts.csv") && input[0].getOriginalFilename().equals("Customers.csv"))
				accountsFileFirst = false;
			else
				return new ResponseEntity<>("input does not match expected file names: Accounts.csv & Customers.csv", HttpStatus.BAD_REQUEST);
			
			counter++;
			File accountsFile = new File(Paths.get("target", "Accounts" + counter + ".csv").toString());
			File customersFile = new File(Paths.get("target", "Customers" + counter + ".csv").toString());
			File errorsFile = new File(Paths.get("target", "Errors" + counter + ".json").toString());
			Files.deleteIfExists(errorsFile.toPath());
			
			if (accountsFileFirst) {
				input[0].transferTo(accountsFile.toPath());
				input[1].transferTo(customersFile.toPath());
			}
			else {
				input[0].transferTo(customersFile.toPath());
				input[1].transferTo(accountsFile.toPath());
			}
			
			File[] workingFiles = {accountsFile, customersFile, errorsFile};
			ScheduledTask.filesQueue.add(workingFiles);
			
			logger.info("new files uploaded");
			return new ResponseEntity<>("new process is scheduled to run every 5 seconds", HttpStatus.ACCEPTED);
			
		} catch (IOException e) {
			logger.error("IO exception in controller");
			return new ResponseEntity<>("failure", HttpStatus.SERVICE_UNAVAILABLE);
		}
	}
}

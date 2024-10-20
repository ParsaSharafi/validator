package sharafi.parsa.validator.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;

/*
 * splitting input file to ten so each thread can handle a chunk
 */

public class FileSplitter {
	
	public static List<File> split(File mainFile) {
		
		List<File> chunkFiles = new ArrayList<>();
		String mainFileName = FilenameUtils.removeExtension(mainFile.getName());
		Path filePath = mainFile.toPath();
		
		int limit = 1000;
		try ( Stream<String> stream = Files.lines(filePath) ) { limit = (int) Math.ceil(stream.count()/10); } catch (IOException e) {e.printStackTrace();}
		
		try (BufferedReader bufferedReader = Files.newBufferedReader(filePath)) {
			
			String header = bufferedReader.readLine();
			
			while (true) {
				File currentFile = new File(Paths.get("target", mainFileName + chunkFiles.size() + ".csv").toString());
				int currentFileRecordCount = 0;
				BufferedWriter currentBufferedWriter = new BufferedWriter(new FileWriter(currentFile));
				currentBufferedWriter.write(header);
				currentBufferedWriter.newLine();
				
				String line = null;
				while (currentFileRecordCount <= limit && (line = bufferedReader.readLine()) != null) {
					currentBufferedWriter.write(line);
					currentBufferedWriter.newLine();
					currentFileRecordCount++;
				}
				
				currentBufferedWriter.close();
				chunkFiles.add(currentFile);
				
				if (line == null)
					break;
			}
			
		} catch (IOException e) {e.printStackTrace();}

		return chunkFiles;
	}
}
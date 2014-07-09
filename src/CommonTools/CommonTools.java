package CommonTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CommonTools {

	public static void processError(String errorText){
		System.out.println(errorText);
		System.exit(0);
	}
	
	public static BufferedReader loadReadFile(String filePath){
		File myFile = new File(filePath);
		FileReader myFileReader = null;
		BufferedReader br = null;
		
		//Only try to load if the save file actually exists
		if(myFile.exists()){
			//Try to open the file.  If the file exists but can't
			//be opened, then tell the user and exit.
			try {
				myFileReader = new FileReader(myFile);
			} catch (FileNotFoundException e) {
				CommonTools.processError("Error loading file from disk.");
			}
			
			//Create a BufferedReader from the already-opened file
			br = new BufferedReader(myFileReader);
			
			return br;
		}
		
		//Return a null value if the file doesn't exist.
		return null;
	}
	
	public static BufferedWriter openWriteFile(String filePath){
		File myFile = new File(filePath);
		return openWriteFile(myFile);
	}
	
	public static BufferedWriter openWriteFile(File writeFile){
		FileWriter myFileWriter = null;
		BufferedWriter bw;

		try {
			//If the parent directory doesn't exist, then create it
			if(!writeFile.getParentFile().exists()){
				writeFile.getParentFile().mkdirs();
			}
			
			//If the file doesn't exist, then create it.
			if(!writeFile.exists()){
				writeFile.createNewFile();
			}
		} catch (IOException e) {
			CommonTools.processError("Error creating save file.");
		}
		
		//Open the file for writing whether it already existed or not.
		try {
			myFileWriter = new FileWriter(writeFile);
		} catch (IOException e1) {
			CommonTools.processError("Error Opening File Writer.");
		}
		
		bw = new BufferedWriter(myFileWriter);
		
		return bw;
	}
}

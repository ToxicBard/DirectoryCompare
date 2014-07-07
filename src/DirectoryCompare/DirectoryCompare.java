package DirectoryCompare;

import java.io.File;
import javax.swing.JFileChooser;

public class DirectoryCompare {

	//TODO Implement directory saving
	//TODO Add some sort of result organization/sorting
	public static void main(String[] args) {
		
		String formerDirectory;
		String latterDirectory;
		
		//Allow the user to select the directories
		formerDirectory = chooseDirectory("Select the former directory.");
		if(formerDirectory != null){
			System.out.println(formerDirectory);
		}
		else{
			System.out.println("You must select the former directory.");
			return;
		}
		
		latterDirectory = chooseDirectory("Select the latter directory.");
		if(latterDirectory != null){
			System.out.println(latterDirectory);
		}
		else{
			System.out.println("You must select the latter directory.");
			return;
		}
		
		compareDirectories(formerDirectory, latterDirectory);
		
	}
	
	private static void compareDirectories(String formerDirectory, String latterDirectory){
		System.out.println("Checking former directory...");
		traverseDirectory(formerDirectory, latterDirectory, true);
		
		System.out.println("");
		
		System.out.println("Checking latter directory...");
		traverseDirectory(latterDirectory, formerDirectory, false);
		
	}
	
	private static void traverseDirectory(String sourceDir, String targetDir, boolean firstCheck){
		File[] myFiles = new File(sourceDir).listFiles();
		
		traverseDirectory(myFiles, sourceDir, targetDir, firstCheck);
		
	}
	
	private static void traverseDirectory(File[] myFiles, String baseSourceDir, String baseTargetDir, boolean firstCheck){
		FileComparisonResult fileCompare;
		
		for(File file : myFiles){
			fileCompare = compareFiles(file, baseSourceDir, baseTargetDir);
			
			processCompareResult(fileCompare, file, firstCheck);
			
			if(file.isDirectory()){
				traverseDirectory(file.listFiles(), baseSourceDir, baseTargetDir, firstCheck);
			}
		}
	}
	
	private static void processCompareResult(FileComparisonResult compareResult, File sourceFile, boolean firstCheck){
		switch (compareResult){
			case doesntExist:	System.out.println("Doesn't Exist: " + sourceFile.getAbsoluteFile());
								break;
								
								//This case is only relevant for the first traversal
			case differentSize:	if(firstCheck){
									System.out.println("Different Size: " + sourceFile.getAbsoluteFile());
								}
								break;
								
								//This case is also only relevant for the first traversal
			case differentDate:	if(firstCheck){
									System.out.println("Different Size: " + sourceFile.getAbsoluteFile());
								}
								break;
			case match:			//Do nothing
		}
	}
	
	private enum FileComparisonResult{
		doesntExist, differentSize, differentDate, match
	}
	
	private static FileComparisonResult compareFiles(File checkFile, String baseSourceDir, String baseTargetDir){
		String relativeFilePath = checkFile.getAbsolutePath().replaceFirst(baseSourceDir, "");
		String targetPath = baseTargetDir + relativeFilePath;
		File targetFile = new File(targetPath);
		
		if(targetFile.exists()){
			if(checkFile.length() != targetFile.length()){
				return FileComparisonResult.differentSize;
			}
			if(checkFile.lastModified() != targetFile.lastModified()){
				return FileComparisonResult.differentDate;
			}
			return FileComparisonResult.match;
		}
		return FileComparisonResult.doesntExist;
	}
	
	private static String chooseDirectory(String dialogTitle){
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setDialogTitle(dialogTitle);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
			return fileChooser.getSelectedFile().toString();
		}
		
		return null;
	}

}

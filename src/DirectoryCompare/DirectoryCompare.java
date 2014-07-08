package DirectoryCompare;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;

public class DirectoryCompare {
	
	private static ArrayList<File> mDoesntExistResults = new ArrayList<File>();
	private static ArrayList<File> mDifferentSizeResults = new ArrayList<File>();
	private static ArrayList<File> mDifferentDateResults = new ArrayList<File>();

	//TODO Write output to a text file, rather than to the console
	//TODO Add comments
	//TODO Redesign with an object-oriented design?
	public static void main(String[] args) {
		
		CompareDirectories myDirs = new CompareDirectories();
		
		compareDirectories(myDirs);
		displayResults();
		
	}
	
	private static void compareDirectories(CompareDirectories compareDirs){
		System.out.println("Checking former directory...");
		startDirectoryTraversal(compareDirs.getFormerDirectory(), compareDirs.getLatterDirectory(), true);
		
		System.out.println("");
		
		System.out.println("Checking latter directory...");
		startDirectoryTraversal(compareDirs.getLatterDirectory(), compareDirs.getFormerDirectory(), false);
		
	}
	
	private static void displayResults(){
		
		System.out.println("Doesn't Exist Results:");
		for(File file : mDoesntExistResults){
			System.out.println(file.getAbsoluteFile());
		}
		System.out.println("");
		
		System.out.println("Different Size Results:");
		for(File file : mDifferentSizeResults){
			System.out.println(file.getAbsoluteFile());
		}
		System.out.println("");
		
		System.out.println("Different Date Results:");
		for(File file : mDifferentDateResults){
			System.out.println(file.getAbsoluteFile());
		}
		System.out.println("");
		
	}
	
	private static void startDirectoryTraversal(File sourceDir, File targetDir, boolean firstCheck){
		File[] myFiles = sourceDir.listFiles();
		
		traverseDirectory(myFiles, sourceDir.getAbsolutePath(), targetDir.getAbsolutePath(), firstCheck);
		
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
			case doesntExist:	mDoesntExistResults.add(sourceFile);
								break;
								
								//This case is only relevant for the first traversal
			case differentSize:	if(firstCheck){
									mDifferentSizeResults.add(sourceFile);
								}
								break;
								
								//This case is also only relevant for the first traversal
			case differentDate:	if(firstCheck){
									mDifferentDateResults.add(sourceFile);
								}
								break;
			case match:			//Do nothing
		}
		
	}
	
	private enum FileComparisonResult{
		doesntExist, differentSize, differentDate, match
	}
	
	private static FileComparisonResult compareFiles(File checkFile, String baseSourceDir, String baseTargetDir){
		String relativeFilePath = checkFile.getAbsolutePath().replace(baseSourceDir, "");
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
	
	public static void processError(String errorText){
		System.out.println(errorText);
		System.exit(0);
	}

}

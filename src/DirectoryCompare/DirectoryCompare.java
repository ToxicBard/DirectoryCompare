package DirectoryCompare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import CommonTools.CommonTools;

public class DirectoryCompare {
	
	private static ArrayList<File> mDoesntExistResults = new ArrayList<File>();
	private static ArrayList<File> mDifferentSizeResults = new ArrayList<File>();
	private static ArrayList<File> mDifferentDateResults = new ArrayList<File>();

	//TODO Add some sort of "in-progress" indicator
	//TODO Add comments
	//TODO Abstract the File object so that FTP support can be added
	//TODO Add FTP support
	//TODO Redesign with an object-oriented design?
	public static void main(String[] args) {
		
		CompareDirectories myDirs = new CompareDirectories();
		
		compareDirectories(myDirs);
		writeResults();
		
	}
	
	private static void compareDirectories(CompareDirectories compareDirs){
		System.out.println("Checking former directory...");
		startDirectoryTraversal(compareDirs.getFormerDirectory(), compareDirs.getLatterDirectory(), true);
		
		System.out.println("");
		
		System.out.println("Checking latter directory...");
		startDirectoryTraversal(compareDirs.getLatterDirectory(), compareDirs.getFormerDirectory(), false);
		
	}
	
	private static void writeResults(){
		File writeFile = new File("out/results.txt");
		BufferedWriter bw = CommonTools.openWriteFile(writeFile);
		String filePath = writeFile.getAbsolutePath();
		
		writeResultType(bw, mDoesntExistResults, "Doesn't Exist Results:");
		writeResultType(bw, mDifferentSizeResults, "Different Size Results:");
		writeResultType(bw, mDifferentDateResults, "Different Date Results:");
		
		try {
			bw.close();
		} catch (IOException e) {
			CommonTools.processError("Error closing Result Writer");
		}
		
		System.out.println("File written to " + filePath);
		
	}
	
	private static void writeResultType(BufferedWriter writer, ArrayList<File> resultFiles, String headerLine){
		try {
			writer.write(headerLine);
			writer.newLine();
			for(File file : resultFiles){
				writer.write(file.getAbsolutePath());
				writer.newLine();
			}
			writer.newLine();
		} catch (IOException e) {
			CommonTools.processError("Error writing results to file.");
		}
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

}

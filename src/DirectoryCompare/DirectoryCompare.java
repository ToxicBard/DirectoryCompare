package DirectoryCompare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import CommonTools.CommonTools;
import CommonTools.LoadingThread;

public class DirectoryCompare {
	
	private static ArrayList<File> mDoesntExistFileResults = new ArrayList<File>();
	private static ArrayList<File> mDoesntExistFolderResults = new ArrayList<File>();
	private static ArrayList<File> mDifferentSizeFileResults = new ArrayList<File>();
	private static ArrayList<File> mDifferentSizeFolderResults = new ArrayList<File>();
	private static ArrayList<File> mDifferentDateFileResults = new ArrayList<File>();

	//TODO Redesign with an object-oriented design?
	//TODO Add comments
	//TODO Abstract the File object so that FTP support can be added
	//TODO Add FTP support
	public static void main(String[] args) {
		
		CompareDirectories myDirs = new CompareDirectories();
		
		compareDirectories(myDirs);
		writeResults();
		
		
	}
	
	private static void compareDirectories(CompareDirectories compareDirs){
		LoadingThread progressDisplay = null;
		
		System.out.println("Checking former directory...");
		progressDisplay = new LoadingThread(50);
		progressDisplay.start();
		startDirectoryTraversal(compareDirs.getFormerDirectory(), compareDirs.getLatterDirectory(), true);
		progressDisplay.stopRunning();
		
		System.out.println("");
		
		System.out.println("Checking latter directory...");
		progressDisplay = new LoadingThread(50);
		progressDisplay.start();
		startDirectoryTraversal(compareDirs.getLatterDirectory(), compareDirs.getFormerDirectory(), false);
		progressDisplay.stopRunning();
		
	}
	
	private static void writeResults(){
		LoadingThread progressDisplay = new LoadingThread(50);
		File writeFile;
		BufferedWriter bw;
		String filePath;
		
		System.out.println("Writing results to file...");
		
		progressDisplay.start();
		
		writeFile = new File("out/results.txt");
		bw = CommonTools.openWriteFile(writeFile);
		filePath = writeFile.getAbsolutePath();
		
		writeResultType(bw, mDoesntExistFolderResults, "Doesn't Exist Folder Results:");
		writeResultType(bw, mDoesntExistFileResults, "Doesn't Exist File Results:");
		writeResultType(bw, mDifferentSizeFolderResults, "Different Size Folder Results:");
		writeResultType(bw, mDifferentSizeFileResults, "Different Size File Results:");
		writeResultType(bw, mDifferentDateFileResults, "Different Date File Results:");
		
		try {
			bw.close();
		} catch (IOException e) {
			CommonTools.processError("Error closing Result Writer");
		}
		progressDisplay.stopRunning();
		
		System.out.println("File written to " + filePath);
		
	}
	
	private static void writeResultType(BufferedWriter writer, ArrayList<File> resultFiles, String headerLine){
		try {
			//If there are no matches of this type then there's no point in writing anything to file for it.
			if(resultFiles.size() > 0){
				writer.write(headerLine);
				writer.newLine();
				for(File file : resultFiles){
					writer.write(file.getAbsolutePath());
					writer.newLine();
				}
				writer.newLine();
			}
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
		
		for(File loopFile : myFiles){
			fileCompare = compareFiles(loopFile, baseSourceDir, baseTargetDir);
			
			processCompareResult(fileCompare, loopFile, firstCheck);
			
			/* 
			 * For some reason in Windows some folders (such as System Volume Information) 
			 * return null for listFiles, thus we don't count those folders
			 */
			if(loopFile.isDirectory() && loopFile.listFiles() != null){
				traverseDirectory(loopFile.listFiles(), baseSourceDir, baseTargetDir, firstCheck);
			}
		}
	}
	
	private static void processCompareResult(FileComparisonResult compareResult, File sourceFile, boolean firstCheck){
		
		
		if(firstCheck){
			//Most cases are relevant for the first check
			switch(compareResult){
				case doesntExistFile:
					mDoesntExistFileResults.add(sourceFile);
					break;
					
				case doesntExistFolder:
					mDoesntExistFolderResults.add(sourceFile);
					break;	
					
				case differentSizeFile:	
					mDifferentSizeFileResults.add(sourceFile);
					break;
	
				case differentSizeFolder:	
					mDifferentSizeFolderResults.add(sourceFile);
					break;	
									
				case differentDateFile:	
					mDifferentDateFileResults.add(sourceFile);
					break;
					
				case differentDateFolder:
					//Do nothing
				case match:	
					//Do nothing
			}
		}
		//Only the doesntExistFile and doesntExistFolder cases are relevant after firstCheck
		else{
			switch(compareResult){
				case doesntExistFile:
					mDoesntExistFileResults.add(sourceFile);
					break;
				case doesntExistFolder:
					mDoesntExistFolderResults.add(sourceFile);
					break;
				default:
					break;
			}
		}
		
	}
	
	private enum FileComparisonResult{
		doesntExistFile, doesntExistFolder, differentSizeFile, differentSizeFolder, differentDateFile, differentDateFolder, match
	}
	
	private static FileComparisonResult compareFiles(File checkFile, String baseSourceDir, String baseTargetDir){
		String relativeFilePath = checkFile.getAbsolutePath().replace(baseSourceDir, "");
		String targetPath = baseTargetDir + relativeFilePath;
		File targetFile = new File(targetPath);
		
		if(targetFile.exists()){
			if(checkFile.length() != targetFile.length()){
				if(checkFile.isDirectory()){
					return FileComparisonResult.differentSizeFolder;
				}
				else{
					return FileComparisonResult.differentSizeFile;
				}
			}
			if(checkFile.lastModified() != targetFile.lastModified()){
				if(checkFile.isDirectory()){
					return FileComparisonResult.differentDateFolder;
				}
				else{
					return FileComparisonResult.differentDateFile;
				}
			}
			return FileComparisonResult.match;
		}
		if(checkFile.isDirectory()){
			return FileComparisonResult.doesntExistFolder;
		}
		else{
			return FileComparisonResult.doesntExistFile;
		}
	}

}

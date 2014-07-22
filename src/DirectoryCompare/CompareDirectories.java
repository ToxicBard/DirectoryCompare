package DirectoryCompare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import commonTools.CommonTools;

public class CompareDirectories {
	private File mFormerDirectory;
	private File mLatterDirectory;
	private boolean mSaveFileLocsChanged = false;
	private final String mSaveFilePath = "out/savedPrefs.cfg";
	
	public CompareDirectories(){
		this.initDirectories();
	}
	
	private void initDirectories(){
		//Load from disk
		this.loadDirectoriesFromDisk();
		
		//Get user input
		this.getDirectoriesFromUser();
		
		//Save user's choice to disk if the user specified a new location
		if(mSaveFileLocsChanged == true){
			this.saveDirectoriesToDisk();
		}
		
	}
	
	private void loadDirectoriesFromDisk(){
		BufferedReader br = CommonTools.loadReadFile(mSaveFilePath);
		String inputFormerDirectory = null;
		String inputLatterDirectory = null;
		
		//Only try to read the file if it actually exists
		if(br != null){

			try {
				inputFormerDirectory = br.readLine();
				inputLatterDirectory = br.readLine();
			} catch (IOException e) {
				CommonTools.processError("Error reading File.");
			}
			
			mFormerDirectory = new File(inputFormerDirectory);
			mLatterDirectory = new File(inputLatterDirectory);
			
			/*
			 * If the filenames provided by the file don't exist and aren't directories,
			 * then clear out the directory properties.
			 * In this case we want to fail silently and continue execution.
			 */
			if(this.directoriesExist() == false){
				mFormerDirectory = null;
				mLatterDirectory = null;
			}
			
			try {
				br.close();
			} catch (IOException e) {
				CommonTools.processError("Error closing file reader");
			}
		}
		
		
	}
	
	private void saveDirectoriesToDisk(){
		BufferedWriter bw = CommonTools.openWriteFile(mSaveFilePath);
		
		//Write to and close the file.
		try {
			bw.write(mFormerDirectory.getAbsolutePath());
			bw.newLine();
			bw.write(mLatterDirectory.getAbsolutePath());
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			CommonTools.processError("Error writing directories");
		}
		
	}
	
	private boolean directoriesExist(){
		if(mFormerDirectory.exists() && mLatterDirectory.exists() && mFormerDirectory.isDirectory() && mLatterDirectory.isDirectory()){
			return true;
		}
		return false;
	}
	
	private void getDirectoriesFromUser(){
		String originalFormerDirectoryPath = "";
		String originalLatterDirectoryPath = "";
		
		if(mFormerDirectory != null && mLatterDirectory != null){
			originalFormerDirectoryPath = mFormerDirectory.getAbsolutePath();
			originalLatterDirectoryPath = mLatterDirectory.getAbsolutePath();
		}
		
		
		//Allow the user to select the directories.
		//Use the existing directory Files as the starting directory because
		//by this point they've been loaded from the save file if relevant.
		mFormerDirectory = askUserForDirectory("Select the former directory.", mFormerDirectory);
		if(mFormerDirectory == null){
			//If the user didn't pick anything, then we exit the program because we have nothing to go on.
			CommonTools.processError("You must select the former directory.");
		}
		
		mLatterDirectory = askUserForDirectory("Select the latter directory.", mLatterDirectory);
		if(mLatterDirectory == null){
			//If the user didn't pick anything, then we exit the program because we have nothing to go on.
			CommonTools.processError("You must select the latter directory.");
		}
		
		/* 
		 * If the directory specified by the user is different from the directory loaded from disk, then mark
		 * a flag telling to save the new paths because there's no point in saving each time the program is run
		 * if the directories are not changed.
		 */
		if(!mFormerDirectory.getAbsolutePath().equals(originalFormerDirectoryPath) || !mLatterDirectory.getAbsolutePath().equals(originalLatterDirectoryPath)){
			mSaveFileLocsChanged = true;
		}
	}
	
	//Asks the user for a directory based on a starting point, which has presumably been loaded from disk.
	private static File askUserForDirectory(String dialogTitle, File startingDirectory){
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setCurrentDirectory(startingDirectory);
		fileChooser.setDialogTitle(dialogTitle);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
			return fileChooser.getSelectedFile();
		}
		
		return null;
	}
	
	public File getFormerDirectory(){
		return mFormerDirectory;
	}
	
	public File getLatterDirectory(){
		return mLatterDirectory;
	}

}

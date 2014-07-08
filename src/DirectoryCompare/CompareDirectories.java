package DirectoryCompare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

public class CompareDirectories {
	private File mFormerDirectory;
	private File mLatterDirectory;
	private final String mSaveFilePath = "out/savedPrefs.cfg";
	
	public CompareDirectories(){
		this.initDirectories();
	}
	
	private void initDirectories(){
		//Load from disk
		this.loadDirectoriesFromDisk();
		
		//Get user input
		this.getDirectoriesFromUser();
		
		//Save user's choice to disk
		this.saveDirectoriesToDisk();
		
	}
	
	private void loadDirectoriesFromDisk(){
		File myFile = new File(mSaveFilePath);
		FileReader myFileReader = null;
		BufferedReader br = null;
		String inputFormerDirectory = null;
		String inputLatterDirectory = null;
		
		
		//Only try to load if the save file actually exists
		if(myFile.exists()){
			//Try to open the file.  If the file exists but can't
			//be opened, then tell the user and exit.
			try {
				myFileReader = new FileReader(myFile);
			} catch (FileNotFoundException e) {
				DirectoryCompare.processError("Error loading file from disk.");
			}
			
			//Create a BufferedReader from the already-opened file
			br = new BufferedReader(myFileReader);
			
			try {
				inputFormerDirectory = br.readLine();
				inputLatterDirectory = br.readLine();
			} catch (IOException e) {
				DirectoryCompare.processError("Error reading File.");
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
				myFileReader.close();
			} catch (IOException e) {
				DirectoryCompare.processError("Error closing file reader");
			}
		}
		
		
	}
	
	private void saveDirectoriesToDisk(){
		File myFile = new File(mSaveFilePath);
		FileWriter myFileWriter = null;
		BufferedWriter bw;

		try {
			//If the parent directory doesn't exist, then create it
			if(!myFile.getParentFile().exists()){
				myFile.getParentFile().mkdirs();
			}
			
			//If the file doesn't exist, then create it.
			if(!myFile.exists()){
				myFile.createNewFile();
			}
		} catch (IOException e) {
			DirectoryCompare.processError("Error creating save file.");
		}
		
		//Open the file for writing whether it already existed or not.
		try {
			myFileWriter = new FileWriter(myFile);
		} catch (IOException e1) {
			DirectoryCompare.processError("Error Opening File Writer.");
		}
		
		bw = new BufferedWriter(myFileWriter);
		
		//Write to and close the file.
		try {
			bw.write(mFormerDirectory.getAbsolutePath());
			bw.newLine();
			bw.write(mLatterDirectory.getAbsolutePath());
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			DirectoryCompare.processError("Error writing directories");
		}
		
	}
	
	private boolean directoriesExist(){
		if(mFormerDirectory.exists() && mLatterDirectory.exists() && mFormerDirectory.isDirectory() && mLatterDirectory.isDirectory()){
			return true;
		}
		return false;
	}
	
	private void getDirectoriesFromUser(){
		//Allow the user to select the directories.
		//Use the existing directory Files as the starting directory because
		//by this point they've been loaded from the save file if relevant.
		mFormerDirectory = askUserForDirectory("Select the former directory.", mFormerDirectory);
		if(mFormerDirectory == null){
			//If the user didn't pick anything, then we exit the program because we have nothing to go on.
			DirectoryCompare.processError("You must select the former directory.");
		}
		
		mLatterDirectory = askUserForDirectory("Select the latter directory.", mLatterDirectory);
		if(mLatterDirectory == null){
			//If the user didn't pick anything, then we exit the program because we have nothing to go on.
			DirectoryCompare.processError("You must select the latter directory.");
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
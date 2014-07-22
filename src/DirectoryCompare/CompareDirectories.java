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
	
	public File getFormerDirectory(){
		return mFormerDirectory;
	}
	
	public File getLatterDirectory(){
		return mLatterDirectory;
	}

}

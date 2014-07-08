package DirectoryCompare;

import java.io.File;

import javax.swing.JFileChooser;

public class CompareDirectories {
	private File mFormerDirectory;
	private File mLatterDirectory;
	
	public CompareDirectories(){
		this.initDirectories();
	}
	
	private void initDirectories(){
		//TODO Add loading from disk
		
		this.getDirectoriesFromUser();
		
		//TODO Save user's choice to disk
		
	}
	
	private void getDirectoriesFromUser(){
		//Allow the user to select the directories.
		//Use the existing directory Files as the starting directory because
		//by this point they've been loaded from the save file if relevant.
		mFormerDirectory = askUserForDirectory("Select the former directory.", mFormerDirectory);
		if(mFormerDirectory == null){
			//If the user didn't pick anything, then we exit the program because we have nothing to go on.
			System.out.println("You must select the former directory.");
			System.exit(0);
		}
		
		mLatterDirectory = askUserForDirectory("Select the latter directory.", mLatterDirectory);
		if(mLatterDirectory == null){
			//If the user didn't pick anything, then we exit the program because we have nothing to go on.
			System.out.println("You must select the latter directory.");
			System.exit(0);
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

package DirectoryCompare;

import javax.swing.JFileChooser;

public class DirectoryCompare {

	public static void main(String[] args) {
		
		String sourceDirectory;
		String targetDirectory;
		
		//Allow the user to select the directories
		sourceDirectory = chooseDirectory("Select a source directory.");
		if(sourceDirectory != null){
			System.out.println(sourceDirectory);
		}
		else{
			System.out.println("You must select a source directory.");
			return;
		}
		
		targetDirectory = chooseDirectory("Select a target directory.");
		if(targetDirectory != null){
			System.out.println(targetDirectory);
		}
		else{
			System.out.println("You must select a target directory.");
			return;
		}
		
		//TODO Compare source and target directories
		
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

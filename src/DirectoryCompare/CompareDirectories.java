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
import commonTools.FileTools;

public class CompareDirectories {
	private File mFormerDirectory;
	private File mLatterDirectory;
	
	public CompareDirectories(){
		this.initDirectories();
	}
	
	private void initDirectories(){
		//Get former directory
		mFormerDirectory = FileTools.selectSavedDirectory("Select the former directory", "cfg/formerDirectory.cfg");
		
		//Get latter directory
		mLatterDirectory = FileTools.selectSavedDirectory("Select the latter directory", "cfg/latterDirectory.cfg");
		
	}
	
	public File getFormerDirectory(){
		return mFormerDirectory;
	}
	
	public File getLatterDirectory(){
		return mLatterDirectory;
	}

}

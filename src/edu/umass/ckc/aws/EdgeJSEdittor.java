package edu.umass.ckc.aws;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EdgeJSEdittor {
	
	public static File edgeWriter(File file) throws IOException {
		File newFile =  new File(file.getPath()+".temp");
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(newFile)));
		BufferedReader br = null;
		FileReader reader = null;
		try {
		    reader = new FileReader(file);
		    br = new BufferedReader(reader);
		    String line;
		while ((line = br.readLine()) != null) {
			if(line.contains("'../js/problemUtils.js'")) {
				System.out.println("Found Line to change");
				if(line.contains("'../js/problemUtils.js'//,"))
					line = line.replace("'../js/problemUtils.js'//,","'../js/problemUtils.js','../js/problemInitUtils.js'");
				else
					line = line.replace("'../js/problemUtils.js'","'../js/problemUtils.js','../js/problemInitUtils.js'");
			}
		    // Always write the line, whether you changed it or not.
		    writer.println(line);
		}
		return newFile;
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;
		}finally{
		    reader.close();
		    writer.close();
		}
	}
	
	public static void displayDirectoryContents(File dir) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					//System.out.println("directory:" + file.getCanonicalPath());
					displayDirectoryContents(file);
				} else if (file.getName().contains("_edgeActions.js")){
					int indexOF = file.getName().indexOf("_edgeActions.js");
					System.out.print("'"+file.getName().substring(0,indexOF)+"'"+",");
					/*File newFile = edgeWriter(file);
					Files.deleteIfExists(Paths.get(file.getPath()));
					newFile.renameTo(file);*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		File currentDir = new File("C:\\Neeraj\\Spring-Boot\\mathspring-V2\\src\\edu\\umass\\ckc\\aws"); // current directory
		displayDirectoryContents(currentDir);
	}

}

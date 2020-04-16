package opendata.scholia.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DiskWriter {
	private static String path = "/tmp/pages/";
	
	public static void write(String fileName, String content) {
		 	FileWriter fileWriter;
			try {
				fileWriter = new FileWriter(path+ fileName.replaceAll("\\W+", "") );
			    PrintWriter printWriter = new PrintWriter(fileWriter);
			    printWriter.print(content);
			    printWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
}

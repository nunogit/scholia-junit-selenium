package opendata.scholia.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class GitReader {
	String sUrl;
	
	
	public void setURL(String url) {
		 this.sUrl  = url;
	}
	
    public GitReader() {
    }

	public List getList() {

		return readFile();
		
	}
	
	private List<String> readFile() {
		
		URL url;
		try {
			url = new URL(this.sUrl);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			System.out.println("Error \""+sUrl+ "\" is not a valid URL. Returning an empty scholia validation URL list");
			return new ArrayList<String>();
		}
		
		List<String> line = new Vector<String>();
		
        URLConnection connection = null;
		try {
			connection = url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        InputStreamReader input = null;
		try {
			input = new InputStreamReader(connection.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        BufferedReader buffer = null;
        String l = "";

        try {

            buffer = new BufferedReader(input);
            while ((l = buffer.readLine()) != null) {
            	l = checkLine(l);
            	if(l!=null) {
            		System.out.println("Reading url "+l);
            		line.add(l);
            	}
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    	return line;
	}
	
	
	private String checkLine(String line) {
		if( line.trim().startsWith("#") ) return null;  //discard lines with comments
		
		//tokenize
		line = line.replace(" #",  "COMMENT_TOKEN");
		line = line.replace("\t#", "COMMENT_TOKEN");
				
		if(line.contains("COMMENT_TOKEN")) //check if it contains a comment
			return line.split("COMMENT_TOKEN")[0].trim(); //split by comment identifier. Pick firs substring, clean
		else return line;
		
	}


}

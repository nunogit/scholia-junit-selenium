package opendata.scholia.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Vector;


public class GitReader {

	URL url;
	
	
	public void setURL(String url) throws MalformedURLException {
		 this.url = new URL(url);
	}
	
    public GitReader() {
    }

	public List getList() {

		return readFile();
		
	}
	
	private List<String> readFile() {
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
            		System.out.println("Reading url"+l);
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
		line = line.replace(" #",  "{{comment}}");
		line = line.replace("\t#", "{{comment}}");
				
		if(line.contains("{{comment}}")) //check if it contains a comment
			return line.split("{{comment}}")[0].trim(); //split by comment identifier. Pick firs substring, clean
		else return line;
		
	}


}

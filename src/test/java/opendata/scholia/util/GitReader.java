package opendata.scholia.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class GitReader {

    public GitReader() {

        URL url = null;
		try {
			url = new URL("http://gist.githubusercontent.com/yonbergman/7a0b05d6420dada16b92885780567e60/raw/114aa2ffb1c680174f9757431e672b5df53237eb/data.csv");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
        String line = "";
        String csvSplitBy = ",";

        try {

            buffer = new BufferedReader(input);
            while ((line = buffer.readLine()) != null) {
                String[] room = line.split(csvSplitBy);
                System.out.println(line);
                System.out.println("room [capacity =" + room[0] + " , price=" + room[1]);
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

    }

}

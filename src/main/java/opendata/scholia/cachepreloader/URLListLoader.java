package opendata.scholia.cachepreloader;

import java.util.List;

import opendata.scholia.util.GitReader;

public class URLListLoader {
	
	String sUrl;
	
	public URLListLoader() {
		
	}
	
	public void setUrl(String sUrl) {
		this.sUrl = sUrl;
	} 
	
	
	public List<String> getList(){
		GitReader gitReader = new GitReader();
		gitReader.setURL(this.sUrl);
		gitReader.getList();
		return null;
	}
	
}
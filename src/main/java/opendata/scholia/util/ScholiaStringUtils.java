package opendata.scholia.util;

public class ScholiaStringUtils {
	
	public static String parseSparqlQueryFromURL(String sUrl) {
		String query = sUrl.replaceAll("^"+"https://query.wikidata.org/embed.html", "");
		return query;
	}

}

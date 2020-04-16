package opendata.scholia.exporter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import opendata.scholia.Pages.Abstract.ScholiaContentPage;
import opendata.scholia.Tests.SPARQLWidgetTest;
import opendata.scholia.Tests.TestBase;

public class SPARQLParseExport {
	public static void main(String [] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException
	{
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("/tmp/queryList.txt"));
				
		List<String> pageList = SPARQLWidgetTest.loadFromGit();
		System.out.println("Read "+ pageList.size()	+" URLs");
		
		List<ScholiaContentPage> scholiaContentPageList = SPARQLWidgetTest.getParameters();
		
		for(ScholiaContentPage scholiaContentPage : scholiaContentPageList) {
			int i = 0;
			List<WebElement> webElementList = scholiaContentPage.getWebElementList();
			for(WebElement webElement : webElementList) {
				String src = webElement.getAttribute("src");
				String query = src.replaceAll("^"+"https://query.wikidata.org/embed.html", "");
				query = java.net.URLDecoder.decode(query);
				System.out.println(query);
				writer.write("#"+scholiaContentPage.getURL()+" iframe: "+ (i++) +" \n"+query+"\n\n");
				
			}
		}
		writer.close();
	}
}

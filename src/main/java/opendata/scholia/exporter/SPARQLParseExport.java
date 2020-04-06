package opendata.scholia.exporter;

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
	public static void main(String [] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, MalformedURLException
	{
				
		List<String> pageList = SPARQLWidgetTest.loadFromGit();
		System.out.println("Read "+ pageList.size()	+" URLs");
		
		List<ScholiaContentPage> scholiaContentPageList = SPARQLWidgetTest.getParameters();
		
		for(ScholiaContentPage scholiaContentPage : scholiaContentPageList) {
			List<WebElement> webElementList = scholiaContentPage.getWebElementList();
			for(WebElement webElement : webElementList) {
				String src = webElement.getAttribute("src");
				System.out.println(src);
			}
			
		}
		
	}
}

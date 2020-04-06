package opendata.scholia.Tests;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import opendata.scholia.Pages.*;


import opendata.scholia.Pages.Author;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;
import opendata.scholia.report.ReportStatistics;
import opendata.scholia.util.GitReader;
import opendata.scholia.util.PageType;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class SPARQLWidgetTest extends TestBase {
	
	private ScholiaContentPage scholiaContentPage;
	private WebElement webElement;
	
	public SPARQLWidgetTest(ScholiaContentPage scholiaContentPage, WebElement webElement) {
		super();
		this.scholiaContentPage = scholiaContentPage;
		this.webElement = webElement;
	}
	

	
	void testPage(ScholiaContentPage scPage, WebElement webelement) {
		if(scPage.iframeWidgetHasError(webelement)){
			System.out.println("found error");
		} else System.out.println("success");
		assertTrue(scPage.iframeWidgetHasError(webelement));
	}
	
	@Test
	public void testDataTables3() {
		System.out.println("testing...");
		scholiaContentPage.setDriver(driver);
		System.out.println("testing  " + scholiaContentPage);
		testPage(scholiaContentPage, webElement);
	}
		
	
	public static List<String> loadFromGit(){
		GitReader gitReader = new GitReader();
		try {
			gitReader.setURL("https://raw.githubusercontent.com/nunogit/scholia-junit-selenium/master/pages/pagetotest.csv");
			//gitReader.setURL("https://raw.githubusercontent.com/nunogit/scholia-junit-selenium/master/pages/smalltestset.csv");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("can't connect to retrieve test list");
			System.out.flush();
			e.printStackTrace();
		}
		
		return gitReader.getList();
	}
	
	public static List<ScholiaContentPage> getScholiaContentPageList(List<String> sUrlList){
		
		TestBase.loadLocalDriver();		
		
		List<ScholiaContentPage> scholiaContentPageList = new Vector<ScholiaContentPage>();
		
		for(String sURL : sUrlList) {
			Class[] cArg = new Class[0];
	
			Class classtype = null;
			try {
				classtype = PageType.getPageType(new URL(sURL));
				System.out.println("Page type "+ classtype);
				
				ScholiaContentPage scholiaContentPage = (ScholiaContentPage) classtype.getDeclaredConstructor().newInstance();
				scholiaContentPage.setURL(sURL);
				scholiaContentPageList.add(scholiaContentPage);
			} catch (MalformedURLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				System.out.println("URL "+sURL+" is invalid");
			}

		}
		
		return scholiaContentPageList;
	}
	

	public static List<ScholiaContentPage> getParameters() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, MalformedURLException {
		TestBase.loadLocalDriver();		
		
		System.out.println("loading URL list from Git...");
		System.out.flush();
				
		List<String> pageList = SPARQLWidgetTest.loadFromGit();
		System.out.println("Read "+ pageList.size()	+" URLs");
		
		List<ScholiaContentPage> scholiaContentPageList = SPARQLWidgetTest.getScholiaContentPageList(pageList);
				
		for(ScholiaContentPage scholiaContentPage : scholiaContentPageList) {
			System.out.println("loading... "+scholiaContentPage.getURL() );
			driver.get(scholiaContentPage.getURL());	
			List<WebElement> webElementList = driver.findElements(By.tagName("iframe"));
			scholiaContentPage.addWebElementList(webElementList);
		}
		return scholiaContentPageList;
	}
	
	@Parameters
	public static Collection<Object[]> setupParameters() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, MalformedURLException {
		Collection<Object[]> stringURLCollection = new ArrayList<Object[]>();
		
		List<ScholiaContentPage> webelementList = SPARQLWidgetTest.getParameters();
				
		for(ScholiaContentPage scholiaContentPage: webelementList) {
			for(WebElement webElement : scholiaContentPage.getWebElementList()) {
				stringURLCollection.add( new Object[]{scholiaContentPage, webElement} );
			}
		}
		
		return stringURLCollection;
	}
	

}

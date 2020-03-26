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
import org.openqa.selenium.WebDriver;


import opendata.scholia.Pages.*;


import opendata.scholia.Pages.Author;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;
import opendata.scholia.report.ReportStatistics;
import opendata.scholia.util.GitReader;
import opendata.scholia.util.PageType;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class TableTest extends TestBase {
	
	private ScholiaContentPage scholiaContentPage;
	private String widgetId;
	
	public TableTest(ScholiaContentPage scholiaContentPage, String widgetId) {
		super();
		this.scholiaContentPage = scholiaContentPage;
		this.widgetId = widgetId;
	}
	
	//@Test
	public void testDataTables() {
				
		//List<Class<ScholiaContentPage>> pageTestList = new Vector<Class<ScholiaContentPage>>();
		Map<Class<?>, String> pageTestList = new HashMap<Class<?>, String>();
		pageTestList.put(Author.class, "https://tools.wmflabs.org/scholia/author/Q97270");
		pageTestList.put(Award.class, "https://tools.wmflabs.org/scholia/award/Q35637");
		pageTestList.put(Catalogue.class, "https://tools.wmflabs.org/scholia/catalogue/Q42661788");
		pageTestList.put(Chemical.class, "https://tools.wmflabs.org/scholia/chemical/Q2270");
		pageTestList.put(ChemicalClasses.class, "https://tools.wmflabs.org/scholia/chemical-class/Q41581");
		pageTestList.put(ChemicalElement.class, "https://tools.wmflabs.org/scholia/chemical-element/Q623");
		pageTestList.put(ClinicalTrial.class, "https://tools.wmflabs.org/scholia/clinical-trial/");
		pageTestList.put(Country.class, "https://tools.wmflabs.org/scholia/country/Q35");
		pageTestList.put(Disease.class, "https://tools.wmflabs.org/scholia/disease/Q41112");
		pageTestList.put(Event.class, "https://tools.wmflabs.org/scholia/event/");
		pageTestList.put(EventSeries.class, "https://tools.wmflabs.org/scholia/event-series/");
		pageTestList.put(Gene.class, "https://tools.wmflabs.org/scholia/gene/Q18030793");
		pageTestList.put(Location.class, "https://tools.wmflabs.org/scholia/location/Q5465");
		pageTestList.put(Organization.class, "https://tools.wmflabs.org/scholia/organization/Q131626");
		pageTestList.put(Pathway.class, "https://tools.wmflabs.org/scholia/pathway/Q30225516");
		pageTestList.put(Printer.class, "https://tools.wmflabs.org/scholia/printer/");
		pageTestList.put(Project.class, "https://tools.wmflabs.org/scholia/project/Q27990087");
		pageTestList.put(Protein.class, "https://tools.wmflabs.org/scholia/protein/");
		pageTestList.put(Publisher.class, "https://tools.wmflabs.org/scholia/publisher/Q127992");
		pageTestList.put(Sponsor.class, "https://tools.wmflabs.org/scholia/sponsor/Q4314967");
		pageTestList.put(Series.class, "https://tools.wmflabs.org/scholia/series/Q924044");
		pageTestList.put(Taxon.class, "https://tools.wmflabs.org/scholia/taxon/Q15978631");
		pageTestList.put(Topic.class, "https://tools.wmflabs.org/scholia/topic/Q52");
		pageTestList.put(Use.class, "https://tools.wmflabs.org/scholia/use/Q5140318");
		pageTestList.put(Venue.class, "https://tools.wmflabs.org/scholia/venue/Q4775205");
		pageTestList.put(Work.class, "https://tools.wmflabs.org/scholia/work/Q21090025");
		
		
		//Author author = new Author(driver);
		
		for(Map.Entry me : 	pageTestList.entrySet()) {
		
			Class<?> className = (Class<?>) me.getKey();
			
			Class[] cArg = new Class[1]; //Our constructor has 1 arguments
			cArg[0] = WebDriver.class; 
		
			ScholiaContentPage scpage = null;
			try {
				scpage = (ScholiaContentPage) className.getDeclaredConstructor(cArg).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//settingArgentina
			
			scpage.setURL((String) me.getValue());
			scpage.visitPage();
			
			List<String> idList = scpage.dataTableIdList();
			
			for(String id : idList) {
				System.out.println( me.getValue());
				System.out.println("-- " + id+ " " + scpage.getDataTableSize(id));
				assertTrue(id, scpage.getDataTableSize(id) > 0);		
			}
		}	
	}
	
	void testPage(ScholiaContentPage scPage, String widgetId) {
		System.out.println("visitingPage");
		scPage.visitPage();
		System.out.println("visitedPage");

		List<String> idList = scPage.dataTableIdList();
		
		int dataTableSize = scPage.getDataTableSize(widgetId);
		System.out.println("-- " + widgetId+ " " + scPage.getDataTableSize(widgetId));

	}
	
	@Test
	public void testDataTables3() {
		scholiaContentPage.setDriver(driver);
		System.out.println("testing  " + scholiaContentPage + "#" +widgetId);
		testPage(scholiaContentPage, widgetId);
	}
	
	//@Test
	public void testDataTables2() {
		Author author = new Author(driver);
	
		//settingArgentina
		author.setURL("https://tools.wmflabs.org/scholia/country/Q414");
		author.visitPage();
		
		List<String> idList = author.dataTableIdList();
		
		for(String id : idList) {
			assertTrue(id, author.getDataTableSize(id) > 0);		
		}
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
	
	@Parameters
	public static Collection<Object[]> setupParameters() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, MalformedURLException {
		
		System.out.println("loading URL list from Git...");
		System.out.flush();
				
		List<String> pageList = TableTest.loadFromGit();
		System.out.println("Read "+ pageList.size()	+" URLs");
		
		List<ScholiaContentPage> scholiaContentPageList = TableTest.getScholiaContentPageList(pageList);
		Collection<Object[]> stringURLCollection = new ArrayList<Object[]>();
		
		
		for(ScholiaContentPage scholiaContentPage : scholiaContentPageList) {
			
			List<String> dataTableIdList = scholiaContentPage.dataTableIdList();
			System.out.println("There are "+ dataTableIdList.size()	+" dataTables in " + scholiaContentPage.getURL());	
			
			for(String dataTableId: dataTableIdList) {
				//Object obj = new Object[][] { {scholiaContentPage, dataTableId},  {scholiaContentPage, dataTableId} };
				stringURLCollection.add( new Object[]{scholiaContentPage, dataTableId} );
			}
		}
		
		System.out.println("Git list read success...");
		System.out.flush();

		return stringURLCollection;
	}
	

}

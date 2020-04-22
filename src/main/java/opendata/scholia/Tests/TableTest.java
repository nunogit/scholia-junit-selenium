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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import opendata.scholia.Pages.*;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;
import opendata.scholia.report.ReportStatistics;
import opendata.scholia.util.GitReader;
import opendata.scholia.util.PageType;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class TableTest extends TestBase {
	
	private static final Logger logger = LogManager.getLogger(TableTest.class);

	
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
		scPage.visitPage();

		List<String> idList = scPage.dataTableIdList();
		
		int dataTableSize = scPage.getDataTableSize(widgetId);
		logger.debug(dataTableSize + " rows in" + widgetId+ " @ " + scPage.getURL());
		assertTrue(dataTableSize > 0);

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
	


	
	@Parameters
	public static Collection<Object[]> setupParameters() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, MalformedURLException {
						
		List<String> pageList = TableTest.loadFromGit();
		
		List<ScholiaContentPage> scholiaContentPageList = TableTest.getScholiaContentPageList(pageList);
		Collection<Object[]> stringURLCollection = new ArrayList<Object[]>();
		
		
		for(ScholiaContentPage scholiaContentPage : scholiaContentPageList) {
			
			List<String> dataTableIdList = scholiaContentPage.dataTableIdList();
			logger.debug("There are "+ dataTableIdList.size()	+" dataTables in " + scholiaContentPage.getURL());	
			
			for(String dataTableId: dataTableIdList) {
				//Object obj = new Object[][] { {scholiaContentPage, dataTableId},  {scholiaContentPage, dataTableId} };
				stringURLCollection.add( new Object[]{scholiaContentPage, dataTableId} );
			}
		}
		
		
	
	

		return stringURLCollection;
	}
	

}

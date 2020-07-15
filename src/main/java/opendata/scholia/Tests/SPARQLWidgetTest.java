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

	private static final Logger logger = LogManager.getLogger(SPARQLWidgetTest.class);

	private ScholiaContentPage scholiaContentPage;
	private String sURL;
	private String iframeHeader;

	private int iframeSeqId;

	// public SPARQLWidgetTest(ScholiaContentPage scholiaContentPage, int
	// iframeSeqId) {
	// super();
	// this.scholiaContentPage = scholiaContentPage;
	// this.iframeSeqId = iframeSeqId;
	// }

	public SPARQLWidgetTest(ScholiaContentPage scholiaContentPage, String sURL, int iframeSeqId, String iframeHeader) {
		super();
		this.scholiaContentPage = scholiaContentPage;
		this.sURL = sURL;
		this.iframeSeqId = iframeSeqId;
		this.iframeHeader = iframeHeader;
	}

	void testPage(ScholiaContentPage scPage, String sURL, int iframeSeqId) {
		int iframeRuntime = scPage.checkIframeWidgetRuntime(sURL, iframeSeqId);
		
		//TODO add load times registration
		scPage.addTestResult(iframeRuntime > 0, ScholiaContentPage.SPARQL_IFRAME_WIDGET, "iframe #"+iframeSeqId, iframeHeader, iframeRuntime * 1000, sURL);
		System.out.println("Took "+iframeRuntime+" seconds");
		assertTrue("WikiData iframe SPARQL based widget fully rendered. iframe #"+iframeSeqId+" Header: "+ iframeHeader, iframeRuntime > 0);
	}

	@Test
	public void testDataTables3() {
		System.out.println("[WidgetTest] testing " + scholiaContentPage.getURL() + " #" + iframeSeqId);
		scholiaContentPage.setDriver(driver);
		testPage(scholiaContentPage, sURL, this.iframeSeqId);
	}



	public static List<ScholiaContentPage> getParameters()
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, MalformedURLException {
		TestBase.loadLocalDriver();



		List<String> urlStringList = TableTest.loadFromGit();
		System.out.println("Read " + urlStringList.size() + " URLs");

		List<ScholiaContentPage> scholiaContentPageList = TestBase.getScholiaContentPageList(urlStringList);

		return scholiaContentPageList;
	}

	@Parameters
	public static Collection<Object[]> setupParametersURL()
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, MalformedURLException {
		Collection<Object[]> stringURLCollection = new ArrayList<Object[]>();

		List<ScholiaContentPage> scholiaContentPageList = SPARQLWidgetTest.getParameters();

		
		for (ScholiaContentPage scholiaContentPage : scholiaContentPageList) {
			int iFrameSeqId = 0;
			for (String urlString : scholiaContentPage.iframeWidgetURLList()) {
				String headerString = scholiaContentPage.getIframeHeader(iFrameSeqId);
				stringURLCollection.add(new Object[] { scholiaContentPage, urlString, iFrameSeqId++, headerString });
			}
		}

		return stringURLCollection;
	}

	// old @parameters
	public static Collection<Object[]> setupParameters()
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, MalformedURLException {
		Collection<Object[]> stringURLCollection = new ArrayList<Object[]>();

		List<ScholiaContentPage> scholiaContentPageList = SPARQLWidgetTest.getParameters();

		for (ScholiaContentPage scholiaContentPage : scholiaContentPageList) {
			int iframeSeqId = 0;
			for (WebElement webElement : scholiaContentPage.getWebElementList()) {
				stringURLCollection.add(new Object[] { scholiaContentPage, iframeSeqId++ });
			}
		}

		return stringURLCollection;
	}

}

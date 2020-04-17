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
	private String urlString;

	private int iframeSeqId;

	// public SPARQLWidgetTest(ScholiaContentPage scholiaContentPage, int
	// iframeSeqId) {
	// super();
	// this.scholiaContentPage = scholiaContentPage;
	// this.iframeSeqId = iframeSeqId;
	// }

	public SPARQLWidgetTest(ScholiaContentPage scholiaContentPage, String urlString, int iframeSeqId) {
		super();
		this.scholiaContentPage = scholiaContentPage;
		this.urlString = urlString;
		this.iframeSeqId = iframeSeqId;
	}

	void testPage(ScholiaContentPage scPage, String urlString, int iframeSeqId) {
		assertTrue("WikiData iframe SPARQL based widget fully rendered", scPage.iframeWidgetHasError(urlString, iframeSeqId));
	}

	@Test
	public void testDataTables3() {
		System.out.println("testing... " + scholiaContentPage.getURL());
		scholiaContentPage.setDriver(driver);
		System.out.println("testing  " + scholiaContentPage);
		testPage(scholiaContentPage, urlString, this.iframeSeqId);
	}

	public static List<ScholiaContentPage> getScholiaContentPageList(List<String> sUrlList) {
		TestBase.loadLocalDriver();

		List<ScholiaContentPage> scholiaContentPageList = new Vector<ScholiaContentPage>();

		for (String sURL : sUrlList) {
			Class[] cArg = new Class[0];

			Class classtype = null;
			try {
				classtype = PageType.getPageType(new URL(sURL));

				logger.debug("Page type identified" + classtype);

				ScholiaContentPage scholiaContentPage = (ScholiaContentPage) classtype.getDeclaredConstructor()
						.newInstance();
				scholiaContentPage.setURL(sURL);
				scholiaContentPage.setDriver(driver);
				scholiaContentPageList.add(scholiaContentPage);
			} catch (MalformedURLException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				logger.warn("URL " + sURL + " is invalid, skipping");
			}

		}

		return scholiaContentPageList;
	}

	public static List<ScholiaContentPage> getParameters()
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, MalformedURLException {
		TestBase.loadLocalDriver();



		List<String> urlStringList = TableTest.loadFromGit();
		System.out.println("Read " + urlStringList.size() + " URLs");

		List<ScholiaContentPage> scholiaContentPageList = SPARQLWidgetTest.getScholiaContentPageList(urlStringList);

		return scholiaContentPageList;
	}

	@Parameters
	public static Collection<Object[]> setupParametersURL()
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, MalformedURLException {
		Collection<Object[]> stringURLCollection = new ArrayList<Object[]>();

		List<ScholiaContentPage> scholiaContentPageList = SPARQLWidgetTest.getParameters();

		int iFrameSeqId = 0;
		for (ScholiaContentPage scholiaContentPage : scholiaContentPageList) {
			for (String urlString : scholiaContentPage.iframeWidgetURLList()) {
				stringURLCollection.add(new Object[] { scholiaContentPage, urlString, iFrameSeqId++ });
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

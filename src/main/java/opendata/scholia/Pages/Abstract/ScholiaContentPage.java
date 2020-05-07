package opendata.scholia.Pages.Abstract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.prometheus.client.Counter;
import opendata.scholia.Tests.TestBase;
import opendata.scholia.util.ConfigManager;
import opendata.scholia.util.DiskWriter;

import org.openqa.selenium.support.ui.ExpectedConditions;

public abstract class ScholiaContentPage{
	
	private static final Logger logger = LogManager.getLogger(ScholiaContentPage.class);
	
	private String pageTypeId = "_unknown";
	
	public static final String SPARQL_IFRAME_WIDGET = "SPARQL_IFRAME_WIDGET";
	public static final String SPARQL_DATATABLE_WIDGET = "SPARQL_IFRAME_WIDGET";
	
	private int webpageTimeout;
	
	protected WebDriver driver;
	
	private Map<String, WebElement> dataTableMap;
	private Map<String, WebElement> widgetMap;
	
	private HashMap<String, ArrayList<String>> failureList = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> successList = new HashMap<String, ArrayList<String>>();
	
	private List<WebElement> webElementList;
	
	private String url = "";
	
	private long backendPerformance = -1;
	private long frontendPerformance = -1;
	
	//static final Counter widgetsTested = Counter.build()
	//	      .name("widgets_tested")
	//	      .help("Tested.").register();
	//static final Counter widgetsFailed = Counter.build()
	//	      .name("widgets_failed")
	//	      .help("Failed.").register();
	
    public ScholiaContentPage(WebDriver driver) {
    	    initDriver(driver);
    	    init();
    }
    
    public List<WebElement> getWebElementList() {
			logger.info( "loading... "+this.getURL() );
			//if(driver.getCurrentUrl()!=this.getURL())
			driver.get(this.getURL());	
			
			//driver.findElements(By.ByXPath());
			
			
			//h2/following::iframe
			//h2/following::iframe
			//iframe/ancestor::h2
			
			List<WebElement> webElementList = driver.findElements(By.tagName("iframe"));
			return webElementList;
    }
    
    public ScholiaContentPage() {
    	    init();
    }
    
    private void initDriver(WebDriver driver) {
    	//TODO improve
    	if(driver==null)
    		logger.debug("Driver null");
    	
    	this.driver = driver;
    	PageFactory.initElements(driver, this);
    }
    
    private void init() {
    	webpageTimeout = ConfigManager.instance().getConfig().getInt("webpageTimeout", 11);
    	System.out.println("--------------------------");
    	System.out.println("timeout... "+ webpageTimeout);
    	dataTableMap = new HashMap();
    	widgetMap = new HashMap();
    }
    
    public void setURL(String url) {
    	this.url = url;
    };
    
    public void visitPage() {
    	//TODO  improve
    	if(driver==null)
    		logger.debug("driver null");
    	
    	
    	if(!url.contentEquals(this.driver.getCurrentUrl())) {
    		logger.debug("Loading URL: "+this.driver.getCurrentUrl());
    		this.driver.get(url);
    		
    		//A page can  be loaded more than once
    		//We only want to use the load time of the first usage
    		//Subsequent loads can have substancial better results due to proxy/caching mechanisms
    		if(backendPerformance==-1) {
    			JavascriptExecutor js = (JavascriptExecutor) driver;  
    			long navigationStart = Long.parseLong( js.executeScript("return window.performance.timing.navigationStart").toString() );
    			long responseStart =  Long.parseLong(js.executeScript("return window.performance.timing.responseStart").toString() );
    			long domComplete =  Long.parseLong( js.executeScript("return window.performance.timing.domComplete").toString() );
    			System.err.print(navigationStart + " - " +responseStart+" - "+domComplete);
    			
    			backendPerformance = responseStart - navigationStart;
    			frontendPerformance = domComplete - responseStart;
    		}
    	} else
    		logger.debug("URL already visited: "+this.driver.getCurrentUrl());
	}
    
	public void addDataTable(String widgetId) {
		//WebElement webelement = driver.findElement(By.id(widgetId));
		//WebElement webelement = driver.findElement(By.id(widgetId));
		this.dataTableMap.put(widgetId, null);
	}
	
	public void addWidget(String widgetId) {
		WebElement webelement = driver.findElement(By.id(widgetId));
		this.widgetMap.put(widgetId, webelement);
	}
	
	public List<String> dataTableIdList(){
		List<String> idList = new Vector<String>();
		for (Map.Entry<String,WebElement> entry : dataTableMap.entrySet()) {
			idList.add(entry.getKey());
		}
		
		return idList;
	} 
	
	public List<String> iframeWidgetURLList(){
		List<String> iframeWidgetURLList = new ArrayList<String>();
		
		List<WebElement> webElementList = this.getWebElementList();
		
		for(WebElement webElement : webElementList) {
			String url = webElement.getAttribute("src");
			iframeWidgetURLList.add(url);
		}
		
		return iframeWidgetURLList;
	}	
	
	
	public boolean checkDataTables() {
		
		for (Map.Entry<String,WebElement> entry : dataTableMap.entrySet()) {  
			//widgetsTested.inc();
			List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\""+entry.getKey()+ "\"]/tbody/tr"));
			if(rows.size()==0) {
				//widgetsFailed.inc();
				return false;
			}
		}
		return true;
	}
	
	//TODO improve this in the future
	public int checkIframeWidgetRuntime(String urlString, int iframeSeqid) {
		
		System.out.println("Testing "+urlString);
		driver.get(urlString);
		
		//apparently the iframe was not rendering properly.
		//now loading iframes in an independent way
		/*webelement.driver.switchTo().frame(0);
		driver.findElement(by.)		
		System.out.println(driver.getPageSource());*/
		
		String pageSource = "";
		int waitForSeconds = 0;
		int checkOnceMore = 0;
		boolean queryFinished = false;
		
		// Getting the page source once is not enough. The content changes with ajax async calls
		// Polling, to find status changes
		// Maximum running limit 60 seconds
		while(waitForSeconds++ < webpageTimeout && !(queryFinished  && checkOnceMore > 0)) {
			SimpleDateFormat formatter= new SimpleDateFormat("mmss");
			Date date = new Date(System.currentTimeMillis());
			String suffix = formatter.format(date);
		
			pageSource = driver.getPageSource().toLowerCase();
			
			//DiskWriter.write(this.url + "x" + iframeSeqid +"x"+ waitForSeconds , pageSource);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Waiting for..."+waitForSeconds);

			if(!pageSource.contains("running query") && !pageSource.contains("<svg") ){
				//not needed; present for documentation for logic purposes;
		    } else if(pageSource.contains("running query") && !pageSource.contains("<svg") ) {
		    	//present for logic purposes. Not needed, may be removed in the future
		    } else if(pageSource.contains("running query") && pageSource.contains("<svg") ){
		    	//ambiguous state, the svg graph is not totally loaded, but the query is still running
		    	//not sure if this state is achievable; present for documentation purposes
		    } else if(!pageSource.contains("running query") && pageSource.contains("<svg") ) {
		    	queryFinished = true;
		    	checkOnceMore++; //checkOnceMore. Updates on HTML may still happen and this gives it an extra oportunity
		    }
						
		}
		
		
		// compared with lower case
		if(waitForSeconds >= webpageTimeout || 
		   pageSource.contains("query timeout limit reached") ||
		   pageSource.contains("unable to display result") ||
		   pageSource.contains("server error: unexpected end of json input")
		   ) {
			return -1;
		 }
		
        //driver.switchTo().defaultContent();
		return waitForSeconds;
	}
	
	public int getDataTableSize(String dataTableId) {
		List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\""+dataTableId+ "\"]/tbody/tr"));
		return rows.size();

	}

	public void setDriver(WebDriver driver) {
		initDriver(driver);
	}

	public String getURL() {
		return this.url;
	}

	public void addWebElementList(List<WebElement> webElementList) {
		this.webElementList = webElementList;
	}
	
	public String getPageTypeId() {
		return pageTypeId;
	}
	
	public void setPageTypeId(String pageTypeId) {
		this.pageTypeId = pageTypeId;
	}

	public void addTestResult(boolean successResult, String tag,  String failureDescription) {
		HashMap<String, ArrayList<String>> resultList;

		resultList = successResult ? successList : failureList;

		ArrayList list = resultList.get(tag);
		if(list==null) {
			list = new ArrayList();
			resultList.put(tag, list);
		}
		
		list.add(failureDescription);

	}
	
	public List<String> getSuccessTestResultList() {
		List<String> result = new ArrayList<String>();
		for (List<String> value : successList.values()) {
		 	result.addAll(value);
		}
		return result;
	}
	
	public List<String> getSuccessTestResultList(String tag) {
		return successList.get(tag);
	}
	
	public List<String> getFailureTestResultList() {
		List<String> result = new ArrayList<String>();
		for (List<String> value : failureList.values()) {
		 	result.addAll(value);
		}
		return result;
	}
	
	public List<String> getFailureTestResultList(String tag) {
		return failureList.get(tag);
	}
	
	public long getBackendPerformance() {
		return this.backendPerformance;
	}
	
	public long getFrontendPerformance() {
		return this.frontendPerformance;
	}
	
}

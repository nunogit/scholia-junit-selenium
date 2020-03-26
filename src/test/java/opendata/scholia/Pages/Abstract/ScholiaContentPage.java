package opendata.scholia.Pages.Abstract;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.prometheus.client.Counter;
import opendata.scholia.Tests.TestBase;

import org.openqa.selenium.support.ui.ExpectedConditions;

public abstract class ScholiaContentPage{
	
	protected WebDriver driver;
	
	Map<String, WebElement> dataTableMap;
	Map<String, WebElement> widgetMap;
	
	private String url = "";
	
	static final Counter widgetsTested = Counter.build()
		      .name("widgets_tested")
		      .help("Tested.").register();
	static final Counter widgetsFailed = Counter.build()
		      .name("widgets_failed")
		      .help("Failed.").register();
	
    public ScholiaContentPage(WebDriver driver) {
    	initDriver(driver);
    	init();
      
    }
    
    public ScholiaContentPage() {
    	init();
    }
    
    private void initDriver(WebDriver driver) {
    	//TODO improve
    	if(driver==null) System.out.println("no driver");
    	
    	this.driver = driver;
    	PageFactory.initElements(driver, this);
    }
    
    private void init() {
    	dataTableMap = new HashMap();
    	widgetMap = new HashMap();
    }
    
    public void setURL(String url) {
    	this.url = url;
    };
    
    public void visitPage() {
    	//TODO  improve
    	if(driver==null)
    		System.out.println("no driver");
    	
    	if(!url.contentEquals(this.driver.getCurrentUrl()))
    		this.driver.get(url);
    	else
    		System.out.println("Already visited.");
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
	
	
	public boolean checkDataTables() {
		
		for (Map.Entry<String,WebElement> entry : dataTableMap.entrySet()) {  
			widgetsTested.inc();
			List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\""+entry.getKey()+ "\"]/tbody/tr"));
			if(rows.size()==0) {
				widgetsFailed.inc();
				return false;
			}
		}
		return true;
	}
	
	public int getDataTableSize(String dataTableId) {
		List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\""+dataTableId+ "\"]/tbody/tr"));
		return rows.size();

	}

	public void setDriver(WebDriver driver) {
		initDriver(driver);
	}
	
}

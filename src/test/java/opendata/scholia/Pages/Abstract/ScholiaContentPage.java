package opendata.scholia.Pages.Abstract;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Tests.TestBase;

import org.openqa.selenium.support.ui.ExpectedConditions;

public abstract class ScholiaContentPage{
	
	protected WebDriver driver;
	
	Map<String, WebElement> dataTableMap;
	Map<String, WebElement> widgetMap;
	
	private String url = "";
	
	
    public ScholiaContentPage(WebDriver driver) {
    	this.driver = driver;
    	PageFactory.initElements(driver, this);
    	
    	dataTableMap = new HashMap();
    	dataTableMap = new HashMap();
    }
    
    public void setURL(String url) {
    	this.url = url;
    };
    
    public void visitPage() {
        this.driver.get(url);
    }

	
	public void addDataTable(String widgetId) {
		//WebElement webelement = driver.findElement(By.id(widgetId));
		WebElement webelement = driver.findElement(By.id(widgetId));
		this.widgetMap.put(widgetId, webelement);
	}
	
	public void addWidget(String widgetId) {
		WebElement webelement = driver.findElement(By.id(widgetId));
		this.widgetMap.put(widgetId, webelement);
	}
	
	
	public boolean checkDataTables() {
		for (Map.Entry<String,WebElement> entry : dataTableMap.entrySet()) {  
			List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\""+entry.getKey()+ "\"]/tbody/tr"));
			if(rows.size()==0) return false;
		}
		return true;
	}
	
}

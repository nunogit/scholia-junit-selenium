package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Country;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class GenericPage extends ScholiaContentPage{



    public GenericPage(WebDriver driver) {
    	super(driver);
        init();
    }
    
    public GenericPage() {
    	super();
    	init();
    }
    
    private void init() {
        this.addDataTable("list-of-publications");
        this.addDataTable("venue-statistics");
        this.addDataTable("topics");
        this.addDataTable("most-cited-works");
        this.addDataTable("citing-authors");
        this.addDataTable("events");
    }


}

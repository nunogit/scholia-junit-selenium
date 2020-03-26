package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Country;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Printer extends ScholiaContentPage{



    public Printer(WebDriver driver) {
    	super(driver);
        init();
        
        this.addDataTable("printed-works-per-publisher");
        
    }
    
    public Printer() {
    	super();
    	init();
    }
    
    private void init() {
    	
    }
}

package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Country;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Catalogue extends ScholiaContentPage{



    public Catalogue(WebDriver driver) {
    	super(driver);
        init();
    }
    
    public Catalogue() {
    	super();
    	init();
    }
    
    private void init() {
    	 this.addDataTable("items-in-catalogue");
    }


}

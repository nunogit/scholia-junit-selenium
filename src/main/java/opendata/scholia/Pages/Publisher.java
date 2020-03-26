package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Country;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Publisher extends ScholiaContentPage{




    public Publisher(WebDriver driver) {
    	super(driver);
        init();
    }
    
    public Publisher() {
    	super();
    	init();
    }
    
    private void init() {
        this.addDataTable("journals");
        //this.addDataTable("list-of-editors");
        this.addDataTable("mostCited");
    }

}

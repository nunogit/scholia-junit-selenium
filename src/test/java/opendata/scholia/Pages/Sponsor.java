package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Country;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Sponsor extends ScholiaContentPage{



    public Sponsor(WebDriver driver) {
    	super(driver);
        init();
    }
    
    public Sponsor() {
    	super();
    	init();
    }

    private void init() {
        this.addDataTable("recently-published-sponsored-work");
        this.addDataTable("authors-on-sponsored-work");
        this.addDataTable("co-sponsors");
    }
}

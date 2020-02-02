package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Country;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Venue extends ScholiaContentPage{



    public Venue(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        
        this.addDataTable("recently-published-works");
        this.addDataTable("topics");
        this.addDataTable("prolific-authors");
        
        //https://tools.wmflabs.org/scholia/venue/Q4775205
        //can be 0?
        //driver.this.addDataTable("co-authors");
        
        this.addDataTable("cited-venues");
        this.addDataTable("citing-venues");
        this.addDataTable("most-cited-works");
        this.addDataTable("most-cited-authors");
        this.addDataTable("gender-distribution");
        this.addDataTable("authorships-gender-distribution");
        this.addDataTable("author-awards");
        
    }


}

package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Country;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Work extends ScholiaContentPage{



    public Work(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        
        this.addDataTable("list-of-authors");
        this.addDataTable("related-works");
        this.addDataTable("citations-to-the-work");
        this.addDataTable("cited-works");
        this.addDataTable("statements");
        
    }


}

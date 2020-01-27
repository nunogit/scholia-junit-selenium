package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Country;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Organization extends ScholiaContentPage{




    public Organization(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        
        this.addDataTable("recent-publications");
        this.addDataTable("recent-citations");
        this.addDataTable("awards");
        this.addDataTable("gender-distribution");
        
    }


}

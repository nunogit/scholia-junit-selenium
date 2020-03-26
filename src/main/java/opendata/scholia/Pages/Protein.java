package opendata.scholia.Pages;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Country;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Protein extends ScholiaContentPage{



    public Protein(WebDriver driver) {
    	super(driver);
        init();
    }
    
    public Protein() {
    	super();
    	init();
    }

    private void init() {
    
    }
}

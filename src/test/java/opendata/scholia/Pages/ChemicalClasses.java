package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class ChemicalClasses extends ScholiaContentPage{

 //  @FindBy(linkText = "i am a link")
 //  private WebElement theActiveLink;

 //  @FindBy(id = "your_comments")
 //  private WebElement yourCommentsSpan;

 //  @FindBy(id = "comments")
 //  private WebElement commentsTextAreaInput;

 //  @FindBy(id = "submit")
 //  private WebElement submitButton;
    


    public ChemicalClasses(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        
        	
    }
    




}

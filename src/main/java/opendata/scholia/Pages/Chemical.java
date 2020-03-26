package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Chemical extends ScholiaContentPage{

 //  @FindBy(linkText = "i am a link")
 //  private WebElement theActiveLink;

 //  @FindBy(id = "your_comments")
 //  private WebElement yourCommentsSpan;

 //  @FindBy(id = "comments")
 //  private WebElement commentsTextAreaInput;

 //  @FindBy(id = "submit")
 //  private WebElement submitButton;
    


    public Chemical(WebDriver driver) {
    	super(driver);
        init();    	
    }
    
    public Chemical() {
    	super();
    	init();
    }
    

    private void init() {
        this.addDataTable("structures");
        this.addDataTable("identifiers");
        this.addDataTable("related");
        this.addDataTable("properties");
        this.addDataTable("recentArticles");
    }


}

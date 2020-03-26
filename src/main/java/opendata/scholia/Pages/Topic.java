package opendata.scholia.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import opendata.scholia.Pages.Abstract.ScholiaContentPage;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Topic extends ScholiaContentPage{

 //  @FindBy(linkText = "i am a link")
 //  private WebElement theActiveLink;

 //  @FindBy(id = "your_comments")
 //  private WebElement yourCommentsSpan;

 //  @FindBy(id = "comments")
 //  private WebElement commentsTextAreaInput;

 //  @FindBy(id = "submit")
 //  private WebElement submitButton;
    


    public Topic(WebDriver driver) {
    	super(driver);
        init();
    }
    
    public Topic() {
    	super();
    	init();
    }

    private void init() {
        this.addDataTable("recently-published-works");
        this.addDataTable("authors");
        this.addDataTable("topics");
        this.addDataTable("author-score");
        this.addDataTable("venues");
        this.addDataTable("topCited");
        this.addDataTable("most-cited-authors");
        //can be 0
        //driver.this.addDataTable("author-awards");
    }


}

package opendata.scholia.Tests;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import opendata.scholia.Pages.PageFactory;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;
import opendata.scholia.report.ReportStatistics;
import opendata.scholia.util.ConfigManager;
import opendata.scholia.util.GitReader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;


//Only if using saucelabs
//import com.saucelabs.common.SauceOnDemandAuthentication;
//import com.saucelabs.common.SauceOnDemandSessionIdProvider;
//import com.saucelabs.junit.ConcurrentParameterized;
//import com.saucelabs.junit.SauceOnDemandTestWatcher;



/**
 * Demonstrates how to write a JUnit test that runs tests against Sauce Labs using multiple browsers in parallel.
 * <p/>
 * The test also includes the {@link SauceOnDemandTestWatcher} which will invoke the Sauce REST API to mark
 * the test as passed or failed.
 *
 * @author Neil Manvar
 */
//@Ignore
//@RunWith(ConcurrentParameterized.class)
public class TestBase /*implements  SauceOnDemandSessionIdProvider  */{

	
	private static final Logger logger = LogManager.getLogger(TestBase.class);
	
    public static String username = System.getenv("SAUCE_USERNAME");
    public static String accesskey = System.getenv("SAUCE_ACCESS_KEY");
    public static String seleniumURI;
    public static String buildTag;
    
   
    
    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
    //public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    //@Rule
    //public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);

    //@Rule
    //public TestName name = new TestName() {
    //    public String getMethodName() {
    //        return String.format("%s", super.getMethodName());
    //    }
    //};

    protected String browser;
    protected String os;
    protected String version;
    protected String deviceName;
    protected String deviceOrientation;
    protected String sessionId;
    protected static WebDriver driver;


    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     * @param os
     * @param version
     * @param browser
     * @param deviceName
     * @param deviceOrientation
     */

   // public TestBase(String os, String version, String browser, String deviceName, String deviceOrientation) {
   //     super();
   //     this.os = os;
   //     this.version = version;
   //     this.browser = browser;
   //     this.deviceName = deviceName;
   //     this.deviceOrientation = deviceOrientation;
   // }

    /**
     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
     * in the String array are used as part of the invocation of the test constructor
     */
    //@ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList browsers = new LinkedList();

        browsers.add(new String[]{"Windows 10", "14.14393", "MicrosoftEdge", null, null});
        browsers.add(new String[]{"Windows 10", "49.0", "firefox", null, null});
        browsers.add(new String[]{"Windows 7", "11.0", "internet explorer", null, null});
        browsers.add(new String[]{"OS X 10.11", "10.0", "safari", null, null});
        browsers.add(new String[]{"OS X 10.10", "54.0", "chrome", null, null});
        return browsers;
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the {@link #browser},
     * {@link #version} and {@link #os} instance variables, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the {@link RemoteWebDriver} instance.
     */
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        capabilities.setCapability(CapabilityType.VERSION, version);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("device-orientation", deviceOrientation);
        //capabilities.setCapability(CapabilityType.PLATFORM, os);

        //String methodName = name.getMethodName();
        //capabilities.setCapability("name", methodName);

        //Getting the build name.
        //Using the Jenkins ENV var. You can use your own. If it is not set test will run without a build id.
        if (buildTag != null) {
            capabilities.setCapability("build", buildTag);
        }
        
        //TODO Improve driver selection
        
        logger.info("Loading driver...");
        
        //WebDriver driver = new FirefoxDriver();
		//comment the above 2 lines and uncomment below 2 lines to use Chrome
        
        TestBase.loadLocalDriver();
        

  		//this.reportStatistics = new ReportStatistics();
  		//this.reportStatistics.executeBatchJob();


  		
      	//this.driver = new RemoteWebDriver(
        //        new URL("https://" + username+ ":" + accesskey + seleniumURI +"/wd/hub"),
        //        capabilities);
        //this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
        
    }

    protected static WebDriver loadLocalDriver() {
    	if(TestBase.driver != null) {
    		logger.debug("Driver already loaded... returning");
    		return TestBase.driver;
    	}
        
    	String javaHome = System.getenv("DRIVER");
		
        if(javaHome != null && javaHome.length() > 0)
        	System.setProperty("webdriver.chrome.driver", javaHome);
        else {
        	System.setProperty("webdriver.chrome.driver", "/chromedriver");
        }

        logger.debug("System var webdriver.chrome.driver set: " + javaHome);
		
		try {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");
			options.addArguments("start-maximized"); 
			options.addArguments("enable-automation");
			options.addArguments("--no-sandbox"); 
			options.addArguments("--disable-infobars"); 
			options.addArguments("--disable-dev-shm-usage"); 
			options.addArguments("--disable-browser-side-navigation"); 
			options.addArguments("--disable-gpu");
			
			TestBase.driver = new ChromeDriver(options);
			TestBase.driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			
			logger.info("Driver loaded...");
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return TestBase.driver;
	}

	@After
    public void tearDown() throws Exception {
    	//ReportStatistics.getReportStatistics().executeBatchJob();
        driver.quit();
        driver = null;
    }

    /**
     * @return the value of the Sauce Job id.
     */
    public String getSessionId() {
        return sessionId;
    }

    @BeforeClass
    public static void setupClass() {
        //get the uri to send the commands to.
        seleniumURI = "@ondemand.saucelabs.com:443";
        //If available add build tag. When running under Jenkins BUILD_TAG is automatically set.
        //check for travis
        buildTag = System.getenv("BUILD_TAG");
        if (buildTag == null) {
            buildTag = System.getenv("SAUCE_BUILD_NAME");
        }
    }
    
	public static List<String> loadFromGit(){
		GitReader gitReader = new GitReader();
		try {
			String sURL = ConfigManager.instance().getConfig().getString("pagetestset");
			gitReader.setURL(sURL);
			//gitReader.setURL("https://raw.githubusercontent.com/nunogit/scholia-junit-selenium/master/pages/smalltestset.csv");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("can't connect to retrieve test list");
			System.out.flush();
			e.printStackTrace();
		}
		
		return gitReader.getList();
	}
	
	public static List<ScholiaContentPage> getScholiaContentPageList(List<String> sUrlList){
		TestBase.loadLocalDriver();

		List<ScholiaContentPage> scholiaContentPageList = new Vector<ScholiaContentPage>();

		for (String sURL : sUrlList) {
			Class[] cArg = new Class[0];

			Class classtype = null;
			
			ScholiaContentPage scholiaContentPage= PageFactory.instance().getPage(sURL);
			scholiaContentPage.setDriver(driver);
			scholiaContentPageList.add(scholiaContentPage);
		}

		return scholiaContentPageList;
	}
    
}

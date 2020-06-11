package opendata.scholia.prometheus.exporter;


import java.net.URISyntaxException;
import java.security.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;
import opendata.scholia.Pages.PageFactory;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;
import opendata.scholia.Tests.SPARQLWidgetTest;
import opendata.scholia.Tests.TableTest;
import opendata.scholia.util.ConfigManager;
import opendata.scholia.util.GitWriter;
import opendata.scholia.util.Uname;
import opendata.scholia.util.model.TestResult;

public class HttpExporter {
	//TOOD a bit ugly. Improve code
	
	private static final Logger logger  = LogManager.getLogger(HttpExporter.class);

    static final Gauge pagesTotal = Gauge.build().name("scholia_pagestested").help("total pages tested").register();
   
    static final Gauge datatablesTotal = Gauge.build().name("scholia_widgets_datatables").help("total datatables tested").labelNames("page_family").register();
    static final Gauge datatablesErrors = Gauge.build().name("scholia_widgets_datatables_errors").help("errors in datatables").labelNames("page_family").register();
    
    static final Gauge sparqlWidgetsTotal  = Gauge.build().name("scholia_widgets_sparqliframe").help("total datatables tested").labelNames("page_family").register();
 	static final Gauge sparqlWidgetsErrors = Gauge.build().name("scholia_widgets_sparqliframe_errors").help("total datatables tested").labelNames("page_family").register();
    
	static final Counter seleniumRunsTotal = Counter.build().name("scholia_seleniumtest_runs_total").help("total amount of times selenium has run").register();
    static final Gauge totalTimeRunning = Gauge.build().name("scholia_seleniumtest_runtime_seconds").help("total datatables tested").register();
    static final Gauge memoryProcess = Gauge.build().name("scholia_seleniumtest_memory_processusage_bytes").help("memory spent by the exporter").register();

   // static final Gauge backendperformance = Gauge.build().name("scholia_backendperformance_seconds").help("backendperformance").register();
   // static final Gauge frontendperformance = Gauge.build().name("scholia_frontendperformance_seconds").help("backendperformance").register();

    static final Histogram backendperformance = Histogram.build().name("scholia_backendperformance_seconds").help("backendperformance").labelNames("page_family").register();
    static final Histogram frontendperformance = Histogram.build().name("scholia_frontendperformance_seconds").help("backendperformance").labelNames("page_family").register();
    
    static final Gauge nodeInfo = Gauge.build().name("scholia_node_info").labelNames("sysname","nodename","release","version","machine","domainname","scholiaid").help("node information").register();
        		
    		
    //node_uname_info{domainname="(none)",machine="x86_64",nodename="ubuntu-s-1vcpu-1gb-ams3-01",release="4.15.0-96-generic",sysname="Linux",version="#97-Ubuntu SMP Wed Apr 1 03:25:46 UTC 2020"} 1

    
    //static final Counter c = Counter.build().name("counter").help("meh").register();
    //static final Summary  s = Summary.build().name("summary").help("meh").register();
    //static final Histogram h = Histogram.build().name("histogram").help("meh").register();
    //static final Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();

    public static void main(String[] args) throws Exception {
        new HTTPServer(1234);
        
        
        Uname uname = new Uname();
        nodeInfo.labels(uname.sysname, uname.nodename, uname.release, uname.version, uname.machine, uname.domainname, uname.scholiaid).set(1);
        
        Thread bgThread = new Thread(() -> {
            while (true) {
                try {
                    List<String> sUrlList = TableTest.loadFromGit();
                    
                    //HashMap<String, List<Long>> backendPerformanceBag  = new HashMap<String, List<Long>>();
                    //HashMap<String, List<Long>> frontendPerformanceBag = new HashMap<String, List<Long>>();
                    
                    List<ScholiaContentPage> scholiaContentPageList = TableTest.getScholiaContentPageList(sUrlList);
                    int dataTableWidgetTotal  = 0;
                    for(ScholiaContentPage scp : scholiaContentPageList) {
                    	scp.clearResults();
                    	dataTableWidgetTotal += scp.dataTableIdList().size();
                    }
                    
                    
                    
                    long start = System.currentTimeMillis();
                    
                    JUnitCore junit = new JUnitCore();
                    junit.addListener(new TextListener(System.out));
                    
                    Result result = junit.run(TableTest.class);
                    List<Failure> failureList = result.getFailures();
                    int failureCount = result.getFailureCount();
                    int runCount = result.getRunCount();
                    
                    Result result2 = junit.run(SPARQLWidgetTest.class);
                    List<Failure> failureList2 = result2.getFailures();
                    int failureCount2 = result2.getFailureCount();
                    int runCount2 = result2.getRunCount();
                    
                    
                    List<ScholiaContentPage> scholiaContentPageL = PageFactory.instance().pageList();                   
                    
                    int tFailures = 0;
                    int tSuccess = 0;
                    int tTotal = 0;
                    for(ScholiaContentPage scp : scholiaContentPageL){
                    	tFailures += scp.getFailureTestResultList().size();
                    	tSuccess += scp.getSuccessTestResultList().size();
                    }
                    tTotal = tFailures + tSuccess;
                    
                    long end = System.currentTimeMillis();

                    float deltaTime = (end - start) / 1000;
                    
                    Runtime runtime = Runtime.getRuntime();
                    // force running the garbage collector
                    runtime.gc();
                    
                    //collect memory being used (to find possible memory leaks)
                    long memory = runtime.totalMemory() - runtime.freeMemory();
                    logger.info("Used memory (in megabytes): "
                            + bytesToMegabytes(memory));
                    
                    
                    pagesTotal.set(sUrlList.size());
                    
                    totalTimeRunning.set(deltaTime);
                    
                    memoryProcess.set(memory);
                    String successLog4Git = "";
                    String failureLog4Git = "";
                    String failureLogDiff4Git = "";
                    String performanceReport = "";

                    
                    List<ScholiaContentPage> scholiaContentPageList2 = PageFactory.instance().pageList();
                    for(ScholiaContentPage scp : scholiaContentPageList2) {
	                    	
                    	double dtFailureCount = scp.getFailureTestResultList(ScholiaContentPage.SPARQL_DATATABLE_WIDGET).size();
                    	double dtSuccessCount = scp.getSuccessTestResultList(ScholiaContentPage.SPARQL_DATATABLE_WIDGET).size();
                    	double dtTotalTests   = dtFailureCount + dtSuccessCount;
                    	
                    	double isFailureCount = scp.getFailureTestResultList(ScholiaContentPage.SPARQL_IFRAME_WIDGET).size();
                    	double isSuccessCount = scp.getSuccessTestResultList(ScholiaContentPage.SPARQL_IFRAME_WIDGET).size();
                    	double isTotalTests   = dtFailureCount + dtSuccessCount;
                    			
                    	datatablesTotal.labels(scp.getPageTypeId()).set(dtTotalTests);
                    	datatablesErrors.labels(scp.getPageTypeId()).set(dtFailureCount);
                    	sparqlWidgetsTotal.labels(scp.getPageTypeId()).set(isTotalTests);
                    	sparqlWidgetsErrors.labels(scp.getPageTypeId()).set(isFailureCount);
                    	
                    	//List<Long> backendbag = backendPerformanceBag.get(scp.getPageTypeId()) !=null ? backendPerformanceBag.get(scp.getPageTypeId()) : new ArrayList<Long>();  
                    	//backendbag.add(scp.getBackendPerformance());
                    	//backendPerformanceBag.put(scp.getPageTypeId(), backendbag);
                    	
                    	//List<Long> frontendbag = backendPerformanceBag.get(scp.getPageTypeId()) !=null ? backendPerformanceBag.get(scp.getPageTypeId()) : new ArrayList<Long>();  
                    	//frontendbag.add(scp.getBackendPerformance());
                    	//backendPerformanceBag.put(scp.getPageTypeId(), frontendbag);
                    	
                        System.out.println("Adding front end performance ("+scp.getPageTypeId()+"):"+scp.getFrontendPerformance());
                        System.out.println("Adding back  end performance ("+scp.getPageTypeId()+"):"+scp.getBackendPerformance());

                    	backendperformance.labels(scp.getPageTypeId()).observe(scp.getBackendPerformance()/1000);
                    	frontendperformance.labels(scp.getPageTypeId()).observe(scp.getFrontendPerformance()/1000);
                    	
                    	for(TestResult failure : scp.getFailureTestResultList()) {
                       	 failureLog4Git += scp.getURL() + "\t" + scp.getPageTypeId() + "\t" + failure.getMessage() + "\n";
                    	 performanceReport += scp.getURL() + "\t" + scp.getPageTypeId() + "\t" + failure.getMessage() + "\t" + failure.getTestDuration() + "\tfailure\n";
                    	}
                    	for(TestResult success : scp.getSuccessTestResultList()) {
                    	 successLog4Git += scp.getURL() + "\t" + scp.getPageTypeId() + "\t" + success.getMessage() + "\n";
                    	 performanceReport += scp.getURL() + "\t" + scp.getPageTypeId() + "\t" + success.getMessage() + "\t" + success.getTestDuration() + "\tsuccess\n";
                    	}
                    	
                    	for(TestResult diffFailure: scp.getFailureTestResultDiffList()) {
                    	  failureLogDiff4Git += scp.getURL() + "\t" + scp.getPageTypeId() + "\t" + diffFailure.getMessage() + "\n";
                    	}
                    	 
                    	
                    	//tested_datatables_total.labels(labelValues)
                    	//backendperformance. = scp.getBackendPerformance();
                    	
                        //scp.getSuccessTestResultList();
                    	//scp.getFailureTestResultList();
                    	
                    }
                    
                	try {
                		System.out.println("Writing to git...");
                		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
                		SimpleDateFormat sdf4dir = new SimpleDateFormat("yyyy/MM/dd");
      
                		
                		long timestamp = System.currentTimeMillis();
                		String sTimestamp = sdf.format(timestamp);
                		String directoryDate = sdf4dir.format(timestamp);
                		System.out.println("FAILURE LOG"+failureLog4Git);
                		
						GitWriter.write("/"+directoryDate, sTimestamp+".log", failureLog4Git);
						GitWriter.write("/"+directoryDate, "success-"+sTimestamp+".log", successLog4Git);
						GitWriter.write("/"+directoryDate, "diff-"+sTimestamp+".log", failureLogDiff4Git);
						
						performanceReport = "Iteration: "+ seleniumRunsTotal.get() + " Runtime: " + directoryDate + "\n" + performanceReport;
						
						GitWriter.write("/"+directoryDate, "performance-"+sTimestamp+".log", performanceReport);

                	
                	} catch (IllegalStateException | GitAPIException | URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    seleniumRunsTotal.inc();
                    
                    //logger.info("Total test result: " + totalSuccess + " (total success) /" + totalRanTests + " (total number tests)");
                    logger.info("Run time Delta: "+deltaTime+" seconds");
                        
                    int timeBetweenTests = ConfigManager.instance().getConfig().getInt("timeBetweenTests", 0);	
                    Thread.sleep(1000 * 60* timeBetweenTests); //interval between tests

                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
        });
        bgThread.start();
        
        
    }
    
    private static final long MEGABYTE = 1024L * 1024L;

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;

      //  g.set(1);
      //  c.inc(2);
      //  s.observe(3);
      //  h.observe(4);
      //  l.labels("foo").inc(5);
    }
    
    
    
     
    
}
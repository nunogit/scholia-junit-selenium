package opendata.scholia.prometheus.exporter;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;
import opendata.scholia.Tests.SPARQLWidgetTest;
import opendata.scholia.Tests.TableTest;

public class HttpExporter {
	//TOOD a bit ugly. Improve code
	
	private static final Logger logger  = LogManager.getLogger(HttpExporter.class);

	
    static final Counter tested_pages_total = Counter.build().name("scholia_pagestested_total").help("total pages tested").register();
   
    static final Counter tested_datatables_total = Counter.build().name("scholia_widgets_datatables_total").help("total datatables tested").register();
    static final Counter datatables_errors =       Counter.build().name("scholia_widgets_datatables_errors_total").help("errors in datatables").register();
    
    static final Counter tested_SPARQLWidgets_total  = Counter.build().name("scholia_widgets_sparqliframe_total").help("total datatables tested").register();
 	static final Counter tested_SPARQLWidgets_errors = Counter.build().name("scholia_widgets_sparqliframe_errors_total").help("total datatables tested").register();
    
    static final Gauge total_time_running = Gauge.build().name("scholia_seleniumtest_runtime_seconds").help("total datatables tested").register();
    static final Gauge memory_process = Gauge.build().name("scholia_seleniumtest_memory_processusage_bytes").help("memory spent by the exporter").register();

    static final Histogram backendperformance = Histogram.build().name("scholia_backendperformance_seconds").help("backendperformance").register();
    static final Histogram frontendperformance = Histogram.build().name("scholia_frontendperformance_seconds").help("backendperformance").register();

    
    
    //static final Counter c = Counter.build().name("counter").help("meh").register();
    //static final Summary  s = Summary.build().name("summary").help("meh").register();
    //static final Histogram h = Histogram.build().name("histogram").help("meh").register();
    //static final Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();

    public static void main(String[] args) throws Exception {
        new HTTPServer(1234);
        
        
        Thread bgThread = new Thread(() -> {
            while (true) {
                try {
                    List<String> sUrlList = TableTest.loadFromGit();
                     
                    
                    List<ScholiaContentPage> scholiaContentPageList = TableTest.getScholiaContentPageList(sUrlList);
                    int dataTableWidgetTotal  = 0;
                    for(ScholiaContentPage scp : scholiaContentPageList) 
                    	dataTableWidgetTotal += scp.dataTableIdList().size();
                    
                    
                    
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
                    
                    
                    long end = System.currentTimeMillis();
                    
                    float deltaTime = (end - start) / 1000;
                    
                    
                    Runtime runtime = Runtime.getRuntime();
                    // force running the garbage collector
                    runtime.gc();
                    
                    //collect memory being used (to find possible memory leaks)
                    long memory = runtime.totalMemory() - runtime.freeMemory();
                    logger.info("Used memory (in megabytes): "
                            + bytesToMegabytes(memory));
                    
                    //for(Failure failure : failureList) {
                    //	System.out.println(failure.getMessage());
                    //}
   
                    int totalFailures = failureCount + failureCount2;
                    int totalRanTests = runCount + runCount2;
                    int totalSuccess  = totalRanTests - totalFailures;
                    
                    
                    datatables_errors.inc(failureCount);
                    tested_datatables_total.inc(dataTableWidgetTotal);
                    
                    tested_SPARQLWidgets_errors.inc(failureCount2);
                    tested_SPARQLWidgets_total.inc(runCount2);
                    
                    tested_pages_total.inc(sUrlList.size());
                    
                    total_time_running.inc(deltaTime);
                    
                    memory_process.set(memory);
                    
                    logger.info("Run time delta: "+deltaTime+" seconds");
                    logger.info("Total test result: " + totalSuccess + " (total success) /" + totalRanTests + " (total number tests)");
                    logger.info("Run time Delta: "+deltaTime+" seconds");
                        
                    Thread.sleep(1000 * 60* 60); //every hour
                    

                    
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
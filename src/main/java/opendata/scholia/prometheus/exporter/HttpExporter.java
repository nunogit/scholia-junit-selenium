package opendata.scholia.prometheus.exporter;


import java.util.List;

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
import opendata.scholia.Tests.TableTest;

public class HttpExporter {
	//TOOD a bit ugly. Improve code
	
    static final Gauge tested_pages_total = Gauge.build().name("tested_pages_total").help("total pages tested").register();
   
    static final Gauge tested_datatables_total = Gauge.build().name("tested_datatables_total").help("total datatables tested").register();
    static final Gauge datatables_errors = Gauge.build().name("erros_datatables_total").help("errors in datatables").register();
    
    static final Gauge tested_SPARQLWidgets_total  = Gauge.build().name("tested_SPARQLWidgets_total").help("total datatables tested").register();
 	static final Gauge tested_SPARQLWidgets_errors = Gauge.build().name("tested_SPARQLWidgets_errors_total").help("total datatables tested").register();
    
    static final Gauge total_time_running = Gauge.build().name("test_time_running_seconds_total").help("total datatables tested").register();
   
    static final Gauge memory_process = Gauge.build().labelNames("process_memory").help("memory spent by the exporter").register();
    
    
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
                    tested_pages_total.set(sUrlList.size());  
                    
                    List<ScholiaContentPage> scholiaContentPageList = TableTest.getScholiaContentPageList(sUrlList);
                    int dataTableWidgetTotal  = 0;
                    for(ScholiaContentPage scp : scholiaContentPageList) 
                    	dataTableWidgetTotal += scp.dataTableIdList().size();
                    
                    tested_datatables_total.set(dataTableWidgetTotal);
                    
                    long start = System.currentTimeMillis();
                    
                    JUnitCore junit = new JUnitCore();
                    junit.addListener(new TextListener(System.out));
                    
                    Result result = junit.run(TableTest.class);
                    List<Failure> failureList = result.getFailures();
                    int failureCount = result.getFailureCount();
                    int runCount = result.getRunCount();
                    
                    Result result2 = junit.run(TableTest.class);
                    List<Failure> failureList2 = result2.getFailures();
                    int failureCount2 = result.getFailureCount();
                    int runCount2 = result.getRunCount();
                    
                    
                    long end = System.currentTimeMillis();
                    
                    float deltaTime = (end - start) / 1000;
                    total_time_running.set(deltaTime);
                    System.out.println("Delta "+deltaTime);
                    
                    for(Failure failure : failureList) {
                    	System.out.println(failure.getMessage());
                    }
                    
                    datatables_errors.set(failureCount);
                    
                    System.out.println("run count " + runCount);
                    System.out.println("failure count " + (failureCount + failureCount2));

                    Runtime runtime = Runtime.getRuntime();
                    // Run the garbage collector
                    runtime.gc();
                    
                    long memory = runtime.totalMemory() - runtime.freeMemory();
                    System.out.println("Used memory is bytes: " + memory);
                    System.out.println("Used memory is megabytes: "
                            + bytesToMegabytes(memory));
                    
                    
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
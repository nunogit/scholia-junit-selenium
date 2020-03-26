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
import opendata.scholia.Tests.TableTest;

public class HttpExporter {

    static final Gauge g = Gauge.build().name("gauge").help("blah").register();
    static final Counter c = Counter.build().name("counter").help("meh").register();
    static final Summary s = Summary.build().name("summary").help("meh").register();
    static final Histogram h = Histogram.build().name("histogram").help("meh").register();
    static final Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();

    public static void main(String[] args) throws Exception {
        new HTTPServer(1234);
     
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        Result result = junit.run(TableTest.class);
        List<Failure> failureList = result.getFailures();
        int failureCount = result.getFailureCount();
        int runCount = result.getRunCount();
        
        for(Failure failure : failureList) {
        	System.out.println(failure.getMessage());
        }
        System.out.println("run count " + runCount);
        System.out.println("failure count " + failureCount);

        g.set(1);
        c.inc(2);
        s.observe(3);
        h.observe(4);
        l.labels("foo").inc(5);
    }
}
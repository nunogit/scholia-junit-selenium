package opendata.scholia.prometheus.metrics;


import io.prometheus.client.Collector;
import io.prometheus.client.Gauge;
import io.prometheus.client.GaugeMetricFamily;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;
import opendata.scholia.Tests.TableTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class CustomMetrics {

    static class RAMCollector extends Collector {
        public List<MetricFamilySamples> collect() {

            Gauge tested_pages_total = Gauge.build().name("tested_pages_total").help("total pages tested").register();
            Gauge tested_datatables_total = Gauge.build().name("tested_datatables_total").help("total datatables tested").register();
            Gauge datatables_errors = Gauge.build().name("erros_datatables_total").help("errors in datatables").register();
            Gauge total_time_running = Gauge.build().name("test_time_running_seconds_total").help("total datatables tested").register();


            List<MetricFamilySamples> mfs = new ArrayList<>();
            
            List<String> sUrlList = TableTest.loadFromGit();
            tested_pages_total.set(sUrlList.size());  
            
            List<ScholiaContentPage> scholiaContentPageList = TableTest.getScholiaContentPageList(sUrlList);
            int dataTableWidgetTotal  = 0;
            for(ScholiaContentPage scp : scholiaContentPageList) 
            	dataTableWidgetTotal =+scp.dataTableIdList().size();
            
            tested_datatables_total.set(dataTableWidgetTotal);
            
            long start = System.currentTimeMillis();
            
            JUnitCore junit = new JUnitCore();
            junit.addListener(new TextListener(System.out));
            Result result = junit.run(TableTest.class);
            List<Failure> failureList = result.getFailures();
            int failureCount = result.getFailureCount();
            int runCount = result.getRunCount();
            
            long end = System.currentTimeMillis();
            
            float deltaTime = (end - start) / 1000F; 
            total_time_running.set(deltaTime);
            
            for(Failure failure : failureList) {
            	System.out.println(failure.getMessage());
            }
            
            datatables_errors.inc(failureCount);
            
            GaugeMetricFamily ramGauge = new GaugeMetricFamily("ram", "Ram usage", Collections.singletonList("type"));
            ramGauge.addMetric(Collections.singletonList("ram_used"), tested_pages_total);
            ramGauge.addMetric(Collections.singletonList("ram_total"), tested_pages_total);
            
            

            mfs.add(ramGauge);
            return mfs;
        }
    }

}

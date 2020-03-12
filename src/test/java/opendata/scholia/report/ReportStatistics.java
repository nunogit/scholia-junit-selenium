package opendata.scholia.report;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.BasicAuthHttpConnectionFactory;
import io.prometheus.client.exporter.PushGateway;
import io.prometheus.client.exporter.common.TextFormat;

public class ReportStatistics {

	
	Counter requests = Counter.build().name("requests_total").help("Total requests.").labelNames("path").register();
	private static ReportStatistics reportStatistics;
	
	//Counter widgetsTested = Counter.build()
	//	      .name("widgets_tested")
	//	      .help("Tested.").register();
	//Counter widgetsFailed = Counter.build()
	//	      .name("widgets_failed")
	//	      .help("Failed.").register();

	HashMap<String, Integer> counter = new HashMap<String, Integer>();

	
	private ReportStatistics() {
		counter = new HashMap<String, Integer>();
	}

	
	public static ReportStatistics getReportStatistics(){
		setup();
		return ReportStatistics.reportStatistics;
	}

	
	private static void setup() {
		if(reportStatistics!=null)
			reportStatistics = new ReportStatistics();
	}
	
	public void incrementCounter(String name, String label) {
		if (counter.containsKey(name))
			counter.put(name, counter.get(name).intValue() + 1);
		else
			counter.put(name, 1);
	}

	public void executeBatchJob() throws Exception {
		CollectorRegistry registry = new CollectorRegistry();
		Gauge duration = Gauge.build().name("my_batch_job_duration_seconds")
				.help("Duration of my batch job in seconds.").register(registry);
		Gauge.Timer durationTimer = duration.startTimer();
		try {

			Iterator it = counter.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();

				Counter count = Counter.build().name("requests_total").help("Total requests.").labelNames("path")
						.register(registry);

				// pair.getValue().intValue();
				count.inc();

				it.remove(); // avoids a ConcurrentModificationException
			}

			Gauge lastSuccess = Gauge.build().name("my_batch_job_last_success")
					.help("Last time my batch job succeeded, in unixtime.").register(registry);
			lastSuccess.setToCurrentTime();
		} finally {
			System.out.println("going to push");
			durationTimer.setDuration();
			
			final Writer writer1 = new StringWriter();
            TextFormat.write004(writer1, registry.metricFamilySamples());
            System.out.println(writer1.toString());
            
            FileWriter fileWriter = new FileWriter("prometheus.log");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("Some String");
            printWriter.printf(writer1.toString());
            printWriter.close();
			
			//PushGateway pg = new PushGateway("146.185.130.187:9091");
			//pg.setConnectionFactory(new BasicAuthHttpConnectionFactory("HelLo", "HelLo"));
			//pg.pushAdd(registry, "my_batch_job");
		}
	}

}

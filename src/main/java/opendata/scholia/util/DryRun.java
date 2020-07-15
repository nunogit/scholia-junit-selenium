package opendata.scholia.util;

import java.util.List;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import opendata.scholia.Pages.PageFactory;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;
import opendata.scholia.Tests.SPARQLWidgetTest;
import opendata.scholia.Tests.TableTest;
import opendata.scholia.util.model.TestResult;

public class DryRun {

	public static void main(String[] args) {
		

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
            
            Result result2 = junit.run(SPARQLWidgetTest.class);
            
            List<ScholiaContentPage> scholiaContentPageList2 = PageFactory.instance().pageList();
            
            for(ScholiaContentPage scp : scholiaContentPageList2) {
		
           
		 	for(TestResult failure : scp.getFailureTestResultList()) {
	            System.out.println(scp.getURL() + "\t" + scp.getPageTypeId() + "\t" + failure.getIdentifier() +"\t"+ failure.getDescription() + "\t" + "\n");	       	
	       	}
	       	for(TestResult success : scp.getSuccessTestResultList()) {
	       		System.out.println(scp.getURL() + "\t" + scp.getPageTypeId() + "\t" +  success.getIdentifier() + "\t" + success.getDescription() + "\t" +"\n");
	       	}
	       	
	       	

	}

}
}

package opendata.scholia.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import opendata.scholia.Pages.Author;
import opendata.scholia.Pages.Award;
import opendata.scholia.Pages.Catalogue;
import opendata.scholia.Pages.Chemical;
import opendata.scholia.Pages.ChemicalClasses;
import opendata.scholia.Pages.ChemicalElement;
import opendata.scholia.Pages.ClinicalTrial;
import opendata.scholia.Pages.Country;
import opendata.scholia.Pages.Disease;
import opendata.scholia.Pages.Event;
import opendata.scholia.Pages.EventSeries;
import opendata.scholia.Pages.Gene;
import opendata.scholia.Pages.GenericPage;
import opendata.scholia.Pages.Location;
import opendata.scholia.Pages.Organization;
import opendata.scholia.Pages.Pathway;
import opendata.scholia.Pages.Printer;
import opendata.scholia.Pages.Project;
import opendata.scholia.Pages.Protein;
import opendata.scholia.Pages.Publisher;
import opendata.scholia.Pages.Series;
import opendata.scholia.Pages.Sponsor;
import opendata.scholia.Pages.Taxon;
import opendata.scholia.Pages.Topic;
import opendata.scholia.Pages.Use;
import opendata.scholia.Pages.Venue;
import opendata.scholia.Pages.Work;
import opendata.scholia.Pages.Abstract.ScholiaContentPage;
import opendata.scholia.util.model.Pair;

public class PageType {
	
	public static Pair<Class, String> getPageType(URL url) {
		
		HashMap<Class, String> pageType = new HashMap<Class, String>();
		
		String path = url.getPath();
		String q_pattern = "Q[0-9]*$";
		String start_pattern = ".*";
		
		pageType.put(Author.class, start_pattern + "/author/"+q_pattern);
		pageType.put(Award.class, start_pattern + "/award/"+q_pattern);
		pageType.put(Catalogue.class, start_pattern + "/catalogue/"+q_pattern);
		pageType.put(Chemical.class, start_pattern + "/chemical/"+q_pattern);
		pageType.put(ChemicalClasses.class, start_pattern + "/chemical-class/"+q_pattern);
		pageType.put(ChemicalElement.class, start_pattern + "/chemical-element/"+q_pattern);
		pageType.put(ClinicalTrial.class, start_pattern + "/clinical-trial/");
		pageType.put(Country.class, start_pattern + "/country/"+q_pattern);
		pageType.put(Disease.class, start_pattern + "/disease/"+q_pattern);
		pageType.put(Event.class, start_pattern + "/event/");
		pageType.put(EventSeries.class, start_pattern + "/event-series/");
		pageType.put(Gene.class, start_pattern + "/gene/"+q_pattern);
		pageType.put(Location.class, start_pattern + "/location/"+q_pattern);
		pageType.put(Organization.class, start_pattern + "/organization/"+q_pattern);
		pageType.put(Pathway.class, start_pattern + "/pathway/"+q_pattern);
		pageType.put(Printer.class, start_pattern + "/printer/");
		pageType.put(Project.class, start_pattern + "/project/"+q_pattern);
		//TODO fix protein repeaated - protein with q_pattern not handled (only one Protein.class)
		pageType.put(Protein.class, start_pattern + "/protein/");
		pageType.put(Protein.class, start_pattern + "/protein/"+q_pattern);
		pageType.put(Publisher.class, start_pattern + "/publisher/"+q_pattern);
		pageType.put(Sponsor.class, start_pattern + "/sponsor/"+q_pattern);
		pageType.put(Series.class, start_pattern + "/series/"+q_pattern);
		pageType.put(Taxon.class, start_pattern + "/taxon/"+q_pattern);
		pageType.put(Topic.class, start_pattern + "/topic/"+q_pattern);
		pageType.put(Use.class, start_pattern + "/use/"+q_pattern);
		pageType.put(Venue.class, start_pattern + "/venue/"+q_pattern);
		pageType.put(Work.class, start_pattern + "/work/"+q_pattern);
		
		for( Entry<Class, String> entry : pageType.entrySet() ) {
			if(path.matches(entry.getValue()))
				return Pair.makePair(entry.getKey(), entry.getValue());
		}
		
		return Pair.makePair(GenericPage.class, "_unknown");
	}
	

}

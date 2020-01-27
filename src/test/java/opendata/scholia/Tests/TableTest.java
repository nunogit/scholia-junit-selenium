package opendata.scholia.Tests;

import static org.junit.Assert.*;
import org.junit.Test;

import opendata.scholia.Pages.Author;


public class TableTest extends TestBase {

	@Test
	public void testDataTables() {
		Author author = new Author(driver);
		
		//settingArgentina
		author.setURL("https://tools.wmflabs.org/scholia/country/Q414");
		author.visitPage();
		
		assertTrue(author.checkDataTables());
		
		
	}

}

package opendata.scholia.util.model;

public class TestResult {
	String identifier;
	String description;
	int testDuration = -1000; // miliseconds
	String extendedDescription = "";
	
	
	
	public TestResult(String identifier, String description, int testDuration) {
		super();
		this.identifier = identifier;
		this.description = description;
		this.testDuration = testDuration;
	}
	
	public TestResult(String identifier, String description, int testDuration, String extendedDescription) {
		super();
		this.identifier = identifier;
		this.description = description;
		this.testDuration = testDuration;
		this.extendedDescription = extendedDescription;
	}
	
	public void setExtendedMessage(String extendedDescription) {
		this.extendedDescription = extendedDescription;
	}
	
	public String getExtendedDescription() {
		return extendedDescription;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public int getTestDuration() {
		return testDuration;
	}
	public void setTestDuration(int testDuration) {
		this.testDuration = testDuration;
	}
	
	
}

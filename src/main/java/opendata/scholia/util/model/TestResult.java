package opendata.scholia.util.model;

public class TestResult {
	String message = "";
	int testDuration = -1; // miliseconds
	
	
	
	
	public TestResult(String message, int testDuration) {
		super();
		this.message = message;
		this.testDuration = testDuration;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTestDuration() {
		return testDuration;
	}
	public void setTestDuration(int testDuration) {
		this.testDuration = testDuration;
	}
	
	
}

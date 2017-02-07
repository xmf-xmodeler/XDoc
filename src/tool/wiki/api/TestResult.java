package tool.wiki.api;

import java.util.Date;

public class TestResult {
	public Date time;
	public boolean success;
	public String comment;

	public TestResult(Date time, boolean success, String comment) {
		super();
		this.time = time;
		this.success = success;
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "TestResult [time=" + time + ", success=" + success + ", comment=" + comment + "]";
	}
	
	
}

package tool.wiki.api;

import java.util.Date;
import java.util.TimeZone;

public class TestResult {
	public Date time;
	public boolean success;
	public String comment;
	public TimeZone timeZone;

	public TestResult(Date time, boolean success, String comment, TimeZone timeZone) {
		super();
		this.time = time;
		this.success = success;
		this.comment = comment;
		this.timeZone = timeZone==null?TimeZone.getDefault():timeZone;
	}

	@Override
	public String toString() {
		return "TestResult [time=" + time + ", success=" + success + ", comment=" + comment + "]";
	}
}

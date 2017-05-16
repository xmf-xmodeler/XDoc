package tool.wiki.api;

import java.util.Date;
import java.util.TimeZone;

public class TestResult {
	public Date time;
	public boolean success;
	public String comment;
	public TimeZone timeZone;
	public String user;
	public String version;

	public TestResult(Date time, boolean success, String comment, TimeZone timeZone) {
		super();
		this.time = time;
		this.success = success;
		this.comment = comment;
		this.timeZone = timeZone==null?TimeZone.getDefault():timeZone;
		this.user = "";
		this.version = "";
	}
	
	public TestResult(Date time, boolean success, String comment, TimeZone timeZone, String user) {
		super();
		this.time = time;
		this.success = success;
		this.comment = comment;
		this.timeZone = timeZone==null?TimeZone.getDefault():timeZone;
		this.user = user;
		this.version = "";
	}
	
	public TestResult(Date time, boolean success, String comment, TimeZone timeZone, String user, String version) {
		super();
		this.time = time;
		this.success = success;
		this.comment = comment;
		this.timeZone = timeZone==null?TimeZone.getDefault():timeZone;
		this.user = user;
		this.version = version;
	}

	@Override
	public String toString() {
		return "TestResult [time=" + time + ", success=" + success + ", comment=" + comment + "]";
	}
	
	public Object[] getContent(){
		return new Object[]{time,success?"Success":"Fail",comment,user,version};
	}
}

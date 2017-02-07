package tool.wiki.api;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdom2.Element;

import javax.swing.GroupLayout.Alignment;

public class Test {
	private String testName;
	private String preCondition;
	private String postCondition;
	private String action;
	private Vector<TestResult> testResults = new Vector<TestResult>();
	
	double priority = 1.0;
	long lastTestedOn;
	String lastTestedBy = "N/A";
	String lastResult;
	String freeText = "";
	boolean hasProblem;
	
	public Test(String testName) {
		this.testName = testName;
	}
	
	public String getTestName() {
		return testName;
	}
	
//	public void setTestName(String testName) {
//		this.testName = testName;
//	}
//	public String getPrecondition() {
//		return preCondition;
//	}
//	public void setPrecondition(String precondition) {
//		this.preCondition = precondition;
//	}
//	public String getPostcondition() {
//		return postCondition;
//	}
//	public void setPostcondition(String postcondition) {
//		this.postCondition = postcondition;
//	}
//	public String getAction() {
//		return action;
//	}
//	public void setAction(String action) {
//		this.action = action;
//	}
	
	public boolean isValid() {
		return testName != null && preCondition != null && postCondition != null && action != null;
	}
	@Override
	public String toString() {
		return "Test [testName=" + testName + ", precondition=" + preCondition + ", postcondition=" + postCondition
				+ ", action=" + action + "]";
	}
	public void addText(String key, String text) {
		if("Preconditions".equals(key)) {
			preCondition = text;
		}
		if("Action".equals(key)) {
			action = text;
		}
		if("Postconditions".equals(key)) {
			postCondition = text;
		}
	}


	protected int checkIsDue() {
		if((System.currentTimeMillis() - lastTestedOn) * (priority+.05) > 2000l * 60 * 60 * 24 * 20) return 3;
		if((System.currentTimeMillis() - lastTestedOn) * (priority+.05) > 2000l * 60 * 60 * 24 *  7) return 2;
		if((System.currentTimeMillis() - lastTestedOn) * (priority+.05) > 2000l * 60 * 60 * 24 *  3) return 1;
		return 0;
	}

//	@Override
//	public int compareTo(Test that) {
//		return this.getPrio().compareTo(that.getPrio());
//	}

//	private Double getPrio() {
//		return hasProblem?-1.:(System.currentTimeMillis() - lastTestedOn) * (priority+.05);
//	}
	
	public JPanel createPanel() {
		TestPanel p = new TestPanel();
			
		return p;
	}

	JTextArea freeTextField;
	JTextArea preField;
	JTextArea actionField;
	JTextArea postField;
	
	JTextField lastTestedOnField;
	JTextArea lastTestedResultField;
	JTextField priorityField;
	private String tocID;
	private WikiInterface wiki;
	private String pageName;
		
	private class TestPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private TestPanel() {
			freeTextField = new JTextArea(freeText);
			preField = new JTextArea(preCondition);
			actionField = new JTextArea(action);
			postField = new JTextArea(postCondition);

			lastTestedOnField = new JTextField();
			lastTestedOnField.setEditable(false);
			setlastTestedOnDate();
			lastTestedResultField = new JTextArea(lastResult);
			priorityField = new JTextField(priority+"");

			JScrollPane freeTextScroll = new JScrollPane(freeTextField);
			JScrollPane preScroll = new JScrollPane(preField);
			JScrollPane actionScroll = new JScrollPane(actionField);
			JScrollPane postScroll = new JScrollPane(postField);
			JScrollPane resultScroll = new JScrollPane(lastTestedResultField);

			JLabel freeTextLabel = new JLabel("Comment");
			JLabel preLabel = new JLabel("Preconditions");
			JLabel actionLabel = new JLabel("Action");
			JLabel postLabel = new JLabel("Postconditions");

			JLabel lastTestedOnLabel = new JLabel("Last tested");
			JLabel lastTestedResultLabel = new JLabel("Result");
			JLabel priorityLabel = new JLabel("Priority");

			JButton reportTestButton = new JButton("Report Test Result");
			JButton uploadButton = new JButton("Save (UM-Wiki:"+pageName+"/"+tocID+")");
			
//			CheckAgainstBox checkAgainstBox = new CheckAgainstBox(hasToBeCheckedAgainstList, getTree(), false, TestNode.this);
//			JLabel checkAgainstLabel = new JLabel("Check against:");
//			CheckAgainstBox linkedNodesBox = new CheckAgainstBox(linkedNodeList, getTree(), true, TestNode.this);
//			JLabel linkedNodesLabel = new JLabel("Linked nodes:");
			
			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			
			final int GAP = 3;
			

			final int BOXWIDTH = 370;
			
			layout.setHorizontalGroup(layout.createSequentialGroup()
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(freeTextLabel)
							.addComponent(preLabel)
							.addComponent(actionLabel)
							.addComponent(postLabel)
							.addComponent(lastTestedOnLabel)
							.addComponent(lastTestedResultLabel)
							.addComponent(priorityLabel)
//							.addComponent(checkAgainstLabel)
//							.addComponent(linkedNodesLabel)
							)
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(freeTextScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(preScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(actionScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(postScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(lastTestedOnField, 150,150,150)
							.addComponent(resultScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(priorityField, 60,60,60)
//							.addComponent(checkAgainstBox, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
//							.addComponent(linkedNodesBox, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addGroup(layout.createSequentialGroup()
									.addComponent(reportTestButton)
									.addGap(GAP)
									.addComponent(uploadButton)))
					.addGap(GAP)
					);
			
			final int BOXHEIGHT = 60;
			
			layout.setVerticalGroup(layout.createSequentialGroup()
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(freeTextLabel)
							.addComponent(freeTextScroll, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(preLabel)
							.addComponent(preScroll, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(actionLabel)
							.addComponent(actionScroll, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(postLabel)
							.addComponent(postScroll, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lastTestedOnLabel)
							.addComponent(lastTestedOnField))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lastTestedResultLabel)
							.addComponent(resultScroll, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(priorityLabel)
							.addComponent(priorityField))
//					.addGap(GAP)
//					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
//							.addComponent(checkAgainstLabel)
//							.addComponent(checkAgainstBox, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
//					.addGap(GAP)
//					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
//							.addComponent(linkedNodesLabel)
//							.addComponent(linkedNodesBox, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(reportTestButton)
							.addComponent(uploadButton))
					.addGap(GAP)
					);
			
			reportTestButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = JOptionPane.showConfirmDialog(
							TestPanel.this, 
							"Were the results as predicted?", 
							"Report Test Result", 
							JOptionPane.YES_NO_CANCEL_OPTION, 
							JOptionPane.QUESTION_MESSAGE, null);
					switch(result) {
					case JOptionPane.YES_OPTION : {
						lastTestedOn = System.currentTimeMillis();
						lastTestedBy = "todo";//DocFrame.user;
						setlastTestedOnDate();
						lastResult = "Success";
						lastTestedResultField.setText(lastResult);
						hasProblem = false;
						testResults.insertElementAt(new TestResult(new Date(), true, ""), 0);
						break;
					}
					case JOptionPane.NO_OPTION : {
						lastTestedOn = System.currentTimeMillis();
						lastTestedBy = "todo";//DocFrame.user;
						setlastTestedOnDate();
						lastResult = "Fail";
						lastTestedResultField.setText(lastResult);
						hasProblem = true;
						testResults.insertElementAt(new TestResult(new Date(), false, ""), 0);
						break;
					}
					}
					
				}
			});
			
			uploadButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String prefix = 
							"==Test Results==\n" +
							"{| class=\"wikitable mw-collapsible mw-collapsed\"\n" +
							"! colspan=\"3\" | Test results\n" + 
							"|-\n" + 
							"! Date \n" + 
							"! Result\n" + 
							"! Comment\n";
					String suffix = "|}";
					String content = "";
					for(TestResult testResult : testResults) {
						content = content + 
							"|-\n" + 
							"| " + df.format(testResult.time) + "\n" + 
							"| style=\"background: " + (testResult.success?"green":"red") + ";\" | " + (testResult.success?"Success":"Fail") + "\n" + 
							"| " + testResult.comment + "\n";
					}
					
					String text = prefix + content + suffix;
					
					
					
					try {
						wiki.setPageWiki(
							pageName, 
							text, 
//							"==Test Results==\n" +
//							"{| class=\"wikitable mw-collapsible mw-collapsed\"\n" +
//							"! colspan=\"3\" | Test results\n" + 
//							"|-\n" + 
//							"! Date \n" + 
//							"! Result\n" + 
//							"! Comment\n" + 
//							"|-\n" + 
//							"| Mon 11 Feb 2017 22:00:00 CET\n" + 
//							"| style=\"background: green;\" | Success ||\n" + 
//							"|-\n" + 
//							"| Mon 10 Feb 2017 21:00:00 NZDT\n" + 
//							"| style=\"background: red;\" | Fail\n" + 
//							"| Did not work...\n" + 
//							"|}",
							"Upload via Test Form", 
							tocID);
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}

		private void setlastTestedOnDate() {
			lastTestedOnField.setText(new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss").format(new Date(lastTestedOn)));			
		}
		
	}
	
//	private final static Calendar cal = new GregorianCalendar();
	final static DateFormat df = new SimpleDateFormat("E dd MMM yyyy hh:mm:ss z");
	
	public void readTestResults(Element testResultTable) {
		if(testResultTable == null) return;
		testResults = new Vector<TestResult>();
		for(Element row : testResultTable.getChildren()) {
			Vector<Element> cells = new Vector<Element>(row.getChildren("td"));
			if(cells.size() == 3) { // possible data row
				
				String dateText =  cells.get(0).getText().trim();
				boolean result = "Success".equals(cells.get(1).getText().trim());
				String comment =  cells.get(2).getText().trim();
				
//				DateFormat df = new SimpleDateFormat("E dd MMM yyyy hh:mm:ss z");
				try {
					Date date = df.parse(dateText);
					testResults.add(new TestResult(date, result, comment));
//					System.err.println("date: " + date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.err.println("testResults: " + testResults);
	}

	public void addTestTocID(String pageName, String tocID) {
		this.tocID = tocID;
		this.pageName = pageName;
	}
	
	public void setWikiInterface(WikiInterface wiki) {
		this.wiki = wiki;
	}

}

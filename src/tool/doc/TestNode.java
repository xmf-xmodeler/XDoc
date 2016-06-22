package tool.doc;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdom2.Element;


import javax.swing.GroupLayout.Alignment;

public class TestNode extends MyTreeNode {
	private static final long serialVersionUID = 1L;
	
	public TestNode(String userObject) {
		super(userObject);
		title = userObject;
	}	
	
	public TestNode(Element node) {
		super(node);
		title = node.getAttributeValue("name");
		load(node);
	}

	public String getType() {
		return "test";
	}
	
	String title;
	double priority = 1.0;
	long lastTestedOn;
	String lastTestedBy = "N/A";
	String lastResult;

	String freeText = "";
	String preConditions = "";
	String actions = "";
	String postconditions = "";
	boolean hasProblem;
	
	public ImageIcon getIcon(Image defaultIcon) {
		try {
			boolean hasProblem2 = hasProblem||!hasToBeCheckedAgainstList.isEmpty();
			
			Image icon = new ImageIcon(hasProblem2?"icons/ListError.png":"icons/List.gif").getImage();
			if(hasProblem2) {
//				icon = MyTreeCellRenderer.addProblem(icon).getImage();
				return new ImageIcon(icon);
			}
			int checkIsDue = checkIsDue();
			if(checkIsDue > 0) icon = MyTreeCellRenderer.addClock(icon, checkIsDue).getImage();
			return new ImageIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageIcon("icons/Error.gif") ;
		}
 	}
	
	protected int checkIsDue() {
		if((System.currentTimeMillis() - lastTestedOn) * (priority+.05) > 1000l * 60 * 60 * 24 * 30) return 3;
		if((System.currentTimeMillis() - lastTestedOn) * (priority+.05)> 1000l * 60 * 60 * 24 * 10) return 2;
		if((System.currentTimeMillis() - lastTestedOn) * (priority+.05)> 1000l * 60 * 60 * 24 * 3) return 1;
		return 0;
	}

	protected boolean hasProblem() {
		return hasProblem||!hasToBeCheckedAgainstList.isEmpty();
	}

	
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
		
	private class TestPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private TestPanel() {
			freeTextField = new JTextArea(freeText);
			preField = new JTextArea(preConditions);
			actionField = new JTextArea(actions);
			postField = new JTextArea(postconditions);

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
			
			CheckAgainstBox checkAgainstBox = new CheckAgainstBox(hasToBeCheckedAgainstList, getTree(), false, TestNode.this);
			JLabel checkAgainstLabel = new JLabel("Check against:");
			CheckAgainstBox linkedNodesBox = new CheckAgainstBox(linkedNodeList, getTree(), true, TestNode.this);
			JLabel linkedNodesLabel = new JLabel("Linked nodes:");
			
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
							.addComponent(checkAgainstLabel)
							.addComponent(linkedNodesLabel))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(freeTextScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(preScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(actionScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(postScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(lastTestedOnField, 150,150,150)
							.addComponent(resultScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(priorityField, 60,60,60)
							.addComponent(checkAgainstBox, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(linkedNodesBox, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(reportTestButton))
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
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(checkAgainstLabel)
							.addComponent(checkAgainstBox, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(linkedNodesLabel)
							.addComponent(linkedNodesBox, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
					.addGap(GAP)
					.addComponent(reportTestButton)
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
						lastTestedBy = DocFrame.user;
						setlastTestedOnDate();
						lastResult = "Success";
						lastTestedResultField.setText(lastResult);
						hasProblem = false;
						break;
					}
					case JOptionPane.NO_OPTION : {
						lastTestedOn = System.currentTimeMillis();
						lastTestedBy = DocFrame.user;
						setlastTestedOnDate();
						lastResult = "Fail";
						lastTestedResultField.setText(lastResult);
						hasProblem = true;
						break;
					}
					}
					
				}
			});
		}

		private void setlastTestedOnDate() {
			lastTestedOnField.setText(new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss").format(new Date(lastTestedOn)));			
		}
		
	}
	
	@Override
		public String toString() {
			return title;
		}

	@Override
	public void storeValues() {
		try{
			priority = Double.parseDouble(priorityField.getText());
		} catch (Exception e) {priority = 1; System.err.println("\t\t\t\tNumber not recognized.");}
		
		lastResult = lastTestedResultField.getText();
		preConditions = preField.getText();
		actions = actionField.getText();
		postconditions = postField.getText();
	}

	@Override
	public void save(PrintStream out) {
		super.save(out);
		out.print(" freeText = \""+XMLHelper.protectSpecialCharacters(freeText)+"\"");
		out.print(" preConditions = \""+XMLHelper.protectSpecialCharacters(preConditions)+"\"");
		out.print(" actions = \""+XMLHelper.protectSpecialCharacters(actions)+"\"");
		out.print(" postconditions = \""+XMLHelper.protectSpecialCharacters(postconditions)+"\"");
		out.print(" lastResult = \""+XMLHelper.protectSpecialCharacters(lastResult)+"\"");
		out.print(" priority = \""+priority+"\"");
		out.print(" lastTestedOn = \""+lastTestedOn+"\"");
		out.print(" lastTestedBy = \""+lastTestedBy+"\"");
		out.print(" hasProblem = \""+(hasProblem?"YES":"NO")+"\"");
	}
	
	public void load(Element node) {
		super.load(node);
		freeText = node.getAttributeValue("freeText");
		preConditions = node.getAttributeValue("preConditions");
		actions = node.getAttributeValue("actions");
		postconditions = node.getAttributeValue("postconditions");
		lastResult = node.getAttributeValue("lastResult");
		priority = Double.parseDouble(node.getAttributeValue("priority"));
		lastTestedOn = Long.parseLong(node.getAttributeValue("lastTestedOn"));
		hasProblem =node.getAttributeValue("hasProblem") != null && "YES".equals(node.getAttributeValue("hasProblem"));
		lastTestedBy = node.getAttributeValue("lastTestedBy"); if(lastTestedBy == null) lastTestedBy = "N/A";
	}
	
	public void setName(String name2) {
		title = name2;
	}

	public void report(PrintStream out) {
		out.print("<tr><td bgColor=\"#"+(hasProblem()?"ff0000":isDueColor())+"\">" + id);
		out.print("</td><td>" + title);
		out.print("</td><td>" + new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss").format(new Date(lastTestedOn)));
		out.print("</td><td>" + lastTestedBy);
		out.print("</td><td>" + lastResult);
		out.print("&nbsp;</td><td>" + XMLHelper.printIntList(hasToBeCheckedAgainstList));
		out.print("&nbsp;</td></tr>\n");
	}

	private String isDueColor() {
		int isDue = checkIsDue();
		if(isDue == 0) return "00bb77";
		if(isDue == 1) return "77bb00";
		if(isDue == 2) return "ffee00";
		if(isDue == 3) return "ff8800";
		return "000000";
	}
}

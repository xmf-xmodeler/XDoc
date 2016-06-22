package tool.doc;

import java.awt.Image;
import java.io.PrintStream;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;

import org.jdom2.Element;

public class SpecificationNode extends MyTreeNode {

	String title;
	String text;
	
	private static final long serialVersionUID = 1L;

	public SpecificationNode(String userObject) {
		super(userObject);
		title = userObject;
	}	
	
	public SpecificationNode(Element node) {
		super(node);
		title = node.getAttributeValue("name");
		load(node);
	}

	@Override
	public ImageIcon getIcon(Image defaultIcon) {
		defaultIcon = new ImageIcon("icons/Specification.gif").getImage();
		if(!hasToBeCheckedAgainstList.isEmpty()) {
			defaultIcon = MyTreeCellRenderer.addProblem(defaultIcon).getImage();
		}
		return new ImageIcon(defaultIcon);
 	}
	
	@Override
	public String getType() {
		return "specification";
	}
	
	@Override
	public Vector<MyTreeNode> getDependentNodes() {
		Vector<MyTreeNode> result = new Vector<MyTreeNode>();
		for(int i = 0; i < parent.getChildCount(); i++ ) {
			MyTreeNode child = (MyTreeNode) parent.getChildAt(i);
			if(child instanceof TestNode)
			result.addElement(child);
		}
		for(MyTreeNode node : linkedNodeList) {
			result.add(node);
		}
		return result;
	}
	
	@Override
	public void save(PrintStream out) {
		super.save(out);
		out.print(" text = \""+XMLHelper.protectSpecialCharacters(text)+"\"");
	}
	
	@Override
	public void storeValues() {
		String newText = textField.getText();

		if(!newText.equals(text)) {
			notifyChanged();
		}
		
		text = newText;
	}
	
	@Override
	public void load(Element node) {
		super.load(node);
		text = node.getAttributeValue("text");
	}	
	
	@Override
	public JPanel createPanel() {
		SpecificationPanel p = new SpecificationPanel();
			
		return p;
	}
	
	private JTextArea textField;
	
	private class SpecificationPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private SpecificationPanel() {
			textField = new JTextArea(text);
			JScrollPane textScroll = new JScrollPane(textField);
			JLabel textLabel = new JLabel("Text");
			
			CheckAgainstBox checkAgainstBox = new CheckAgainstBox(hasToBeCheckedAgainstList, getTree(), false, SpecificationNode.this);
			JLabel checkAgainstLabel = new JLabel("Check against:");
			CheckAgainstBox linkedNodesBox = new CheckAgainstBox(linkedNodeList, getTree(), true, SpecificationNode.this);
			JLabel linkedNodesLabel = new JLabel("Linked nodes:");
			
			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			
			final int GAP = 6;
			
			final int BOXWIDTH = 470;
			
			layout.setHorizontalGroup(layout.createSequentialGroup()
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(textLabel)
							.addComponent(linkedNodesLabel)
							.addComponent(checkAgainstLabel))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(textScroll, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(checkAgainstBox, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE)
							.addComponent(linkedNodesBox, BOXWIDTH, BOXWIDTH, Integer.MAX_VALUE))
					.addGap(GAP)
					);
			
			final int BOXHEIGHT = 100;
			
			layout.setVerticalGroup(layout.createSequentialGroup()
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(textLabel)
							.addComponent(textScroll, BOXHEIGHT*2, BOXHEIGHT*2, BOXHEIGHT*4))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(checkAgainstLabel)
							.addComponent(checkAgainstBox, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
					.addGap(GAP)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(linkedNodesLabel)
							.addComponent(linkedNodesBox, BOXHEIGHT, BOXHEIGHT, BOXHEIGHT*2))
					.addGap(GAP)
					);
		}
		
	}

}

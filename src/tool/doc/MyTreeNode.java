package tool.doc;

import java.awt.Image;
import java.io.PrintStream;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdom2.Element;


public class MyTreeNode extends DefaultMutableTreeNode{
	private static final long serialVersionUID = 1L;
	
	String name;
	TreeNodeType type;
	String content;
	Vector<MyTreeNode> hasToBeCheckedAgainst;
	int id;

	public MyTreeNode(Object userObject) {
		super(userObject);
	}

	public String getType() {
		return "default";
	}

	public JPanel createPanel() {
		JPanel p = new JPanel();
		return p;
	}

	public ImageIcon getIcon(Image defaultIcon) {
		try {
			if(hasProblem()) {
				defaultIcon = MyTreeCellRenderer.addProblem(defaultIcon).getImage();
				return new ImageIcon(defaultIcon);
			}
			int checkIsDue = checkIsDue();
			if(checkIsDue > 0) defaultIcon = MyTreeCellRenderer.addClock(defaultIcon, checkIsDue).getImage();
			return new ImageIcon(defaultIcon);
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageIcon("icons/Tools/Delete.gif") ;
		}
 	}

	protected int checkIsDue() {
		int checkIsDue = 0;
		for(int i = 0; i < getChildCount(); i++) {
			checkIsDue  = Math.max(checkIsDue, ((MyTreeNode) getChildAt(i)).checkIsDue());
		}
		return checkIsDue;
	}

	protected boolean hasProblem() {
		boolean hasProblem = false;
		for(int i = 0; i < getChildCount() && ! hasProblem; i++) {
			hasProblem |= ((MyTreeNode) getChildAt(i)).hasProblem();
		}
		return hasProblem;
	}

	public void storeValues() {}

	public void save(PrintStream out) {
		out.print(" id=\"" + id + "\"");
		out.print(" name=\""+toString()+"\"");	
		out.print(" type=\""+getType()+"\"");
	}
	
	public void load(Element node) {
		
		if(node.getAttributeValue("id") == null) {
			createID();
		} else {
			id = Integer.parseInt(node.getAttributeValue("id"));
		}
	}

	private void createID() {
		id = getRootNode().getnextID();		
	}

	private int getnextID() {
		int i = 1;
		while(numberExists(i)) {
			i++;
		}
		return i;
	}

	private boolean numberExists(int i) {
		if(id == i) return true;
		for(int x = 0; x < getChildCount(); x++ ) {
			if(((MyTreeNode)getChildAt(x)).numberExists(i)) return true;
		}
		return false;
	}

	private MyTreeNode getRootNode() {
		return(MyTreeNode) getRoot();
	}

	public void setName(String name2) {
		setUserObject(name2);
	}
	
	public Vector<MyTreeNode> getDependentNodes() {
		Vector<MyTreeNode> result = new Vector<MyTreeNode>();
		for(int i = 0; i < parent.getChildCount(); i++ ) {}
		return result;
	}
	
	public void notifyChanged() {
		for(MyTreeNode node : getDependentNodes()) {
			node.addCheckAgainst(this);
		}
	}

	private void addCheckAgainst(MyTreeNode myTreeNode) {
		if(!hasToBeCheckedAgainst.contains(myTreeNode)) hasToBeCheckedAgainst.addElement(myTreeNode);
	}
	
	
}

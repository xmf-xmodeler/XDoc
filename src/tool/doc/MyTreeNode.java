package tool.doc;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;
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
	Vector<MyTreeNode> hasToBeCheckedAgainstList = new Vector<MyTreeNode>();
	Vector<MyTreeNode> linkedNodeList = new Vector<MyTreeNode>();
	transient String hasToBeCheckedAgainstText;
	transient String linkedNodeText;
	int id;

	public MyTreeNode(String s) {
		super(s);
	}
	
	public MyTreeNode(Element node) {
		super(node.getAttributeValue("name"));
		load(node);
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
		out.print(" checkAgainst = \""+XMLHelper.printIntList(hasToBeCheckedAgainstList)+"\"");
		out.print(" linkedNodes = \""+XMLHelper.printIntList(linkedNodeList)+"\"");
	}
	
	public void load(Element node) {
		
		if(node.getAttributeValue("id") == null || 1 >= Integer.parseInt(node.getAttributeValue("id"))) {
			id = 0;
		} else {
			id = Integer.parseInt(node.getAttributeValue("id"));
		}

		hasToBeCheckedAgainstText = node.getAttributeValue("checkAgainst");
		linkedNodeText = node.getAttributeValue("linkedNodes");
	}

	public void createID() {
		id = getRootNode().getnextID();		
	}

	private int getnextID() {
		int i;
		int iMax = 32;
		do {
			i = new Random().nextInt(iMax);
			iMax *= 2;
			if(iMax < 0) iMax = 32;
		} while(numberExists(i));
		return i;
	}

	private boolean numberExists(int i) {
		if(i <= 1) return true;
 		if(id == i) return true;
		for(int x = 0; x < getChildCount(); x++) {
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

	private void addCheckAgainst(MyTreeNode node) {
		if(!hasToBeCheckedAgainstList.contains(node)) hasToBeCheckedAgainstList.addElement(node);
	}
	
	public void addLinkedNode(MyTreeNode node) {
		if(!linkedNodeList.contains(node)) linkedNodeList.addElement(node);
		
	}

	public void updateLinks() {
		updateMyLinks(hasToBeCheckedAgainstList, hasToBeCheckedAgainstText);
		updateMyLinks(linkedNodeList, linkedNodeText);
		for(int i = 0; i < getChildCount(); i++ ) {
			((MyTreeNode)getChildAt(i)).updateLinks();
		}
		hasToBeCheckedAgainstText = null;
	}

	private void updateMyLinks(Vector<MyTreeNode> list, String text) {
		if(text != null) {
			if("".equals(text)) return;
			String[] links = text.split(";");
			for(int i = 0; i < links.length; i++) {
				String idS = links[i];
				Integer id = Integer.parseInt(idS);
				MyTreeNode node = getRootNode().findNode(id);
				if(node == null) throw new IllegalArgumentException();
				if(!(list.contains(node))) {
					list.addElement(node);
				}
			}
		}
	}

	private MyTreeNode findNode(Integer ID) {
		if(id == ID) return this;
		for(int i = 0; i < getChildCount(); i++ ) {
			MyTreeNode found = ((MyTreeNode)getChildAt(i)).findNode(ID);
			if(found != null) return found;
		}
		return null;
	}
	
	public void setTree(MyTree tree) {
		this.tree = tree;
	}
	
	private MyTree tree;
	
	public MyTree getTree() {
		if(getRootNode() == this) return tree;
		return getRootNode().getTree();
	}

	public void report() {
		try {
			Vector<MyTreeNode> allNodes = getAllChildren();
			File file = new File("doc/mainDocReport.html");
			PrintStream out;
			out = new PrintStream(file, "UTF-8");
			out.print("<html><head><title>Test Report</title></head><body><table border=\"1\" width=\"100%\">\n");
			out.print("<tr><th>ID");
			out.print("</th><th width=\"18%\">Title");
			out.print("</th><th>tested on");
			out.print("</th><th>tested by");
			out.print("</th><th width=\"55%\">lastResult");
			out.print("</th><th>check against");
			out.print("</th></tr>\n");
			for(MyTreeNode node : allNodes) {
				if(node instanceof TestNode) {
					TestNode testNode = (TestNode) node;
					testNode.report(out);
				}
			}
			out.print("</table></body></html>");
			out.close();
		} catch (FileNotFoundException e) {e.printStackTrace();
		} catch (UnsupportedEncodingException e) {e.printStackTrace();
		}
	}

	private Vector<MyTreeNode> getAllChildren() {
		Vector<MyTreeNode> result = new Vector<MyTreeNode>();
		result.add(this);
		
		for(int i = 0; i < getChildCount(); i++ ) {
			result.addAll(((MyTreeNode)getChildAt(i)).getAllChildren());
		}
		
		return result;
	}


	
	
}

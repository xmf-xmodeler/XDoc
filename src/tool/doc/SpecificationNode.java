package tool.doc;

import java.awt.Image;
import java.io.PrintStream;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.jdom2.Element;

public class SpecificationNode extends MyTreeNode {

	String title;
	String text;
	
	private static final long serialVersionUID = 1L;

	public SpecificationNode(String userObject) {
		super(userObject);
		title = userObject;
	}	
	
	public SpecificationNode(Element  node) {
		super(node.getAttributeValue("name"));
		title = node.getAttributeValue("name");
//		load(node);
	}

	public ImageIcon getIcon(Image defaultIcon) {
		return new ImageIcon("icons/Specification.gif") ;
 	}
	
	public String getType() {
		return "specification";
	}
	
	public Vector<MyTreeNode> getDependentNodes() {
		Vector<MyTreeNode> result = new Vector<MyTreeNode>();
		for(int i = 0; i < parent.getChildCount(); i++ ) {
			MyTreeNode child = (MyTreeNode) parent.getChildAt(i);
			if(child instanceof TestNode)
			result.addElement(child);
		}
		return result;
	}
	
	public void save(PrintStream out) {
		super.save(out);
		out.print(" text = \""+XMLHelper.protectSpecialCharacters(text)+"\"");
	}
	
	public void load(Element node) {
		super.load(node);
		text = node.getAttributeValue("text");
	}
	
}

package tool.doc;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.jdom2.Element;

public class SpecificationNode extends MyTreeNode {

	String title;
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
}

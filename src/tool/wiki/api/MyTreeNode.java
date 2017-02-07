package tool.wiki.api;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

public abstract class MyTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = -1185962780130183623L;

	public MyTreeNode() {
		super();
	}

	public MyTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public MyTreeNode(Object userObject) {
		super(userObject);
	}

	public abstract ImageIcon getIcon(Image image);
	

}

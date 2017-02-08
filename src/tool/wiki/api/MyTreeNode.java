package tool.wiki.api;

import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
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

	public ImageIcon getIcon(Image image) {
		return new ImageIcon(image);
	}
	
	protected void loadTests(WikiInterface wiki, JFrame frame) {
		Vector<Test> tests = new Vector<Test>();
		try {
			String html = wiki.getPageHTML(this.getUserObject()+"");
			tests = wiki.parseHTML2Meta(html, this.toString());
		} catch (Exception e) {
			System.err.println("No Test found in " + this + "("+e+")");
//			e.printStackTrace();
		}
		for(int i = 0; i < this.getChildCount(); i++) {
//			wiki.loadTests((MyTreeNode) this.getChildAt(i));
			((MyTreeNode)this.getChildAt(i)).loadTests(wiki, frame);
		}		
		for(Test test : tests) {
			this.add(new TestTreeNode(test));
			frame.repaint();
		}
	}

}

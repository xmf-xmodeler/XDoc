package tool.wiki.api.FX;

import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;

public abstract class MyTreeNode extends TreeItem<Object> {
	private static final long serialVersionUID = -1185962780130183623L;
	
	public MyTreeNode() {
		super();
	}

	public MyTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject);
		setExpanded(allowsChildren);
	}

	public MyTreeNode(Object userObject) {
		super(userObject);
	}

	public ImageIcon getIcon(Image image) {
		return new ImageIcon(image);
	}
	
/*	public synchronized Vector<MyTreeNode> getChildren() {
	    Vector<MyTreeNode> result = new Vector<MyTreeNode>();
	    	for(int i = 0; i < getChildCount(); i++) {
	    		result.add((MyTreeNode)getChildAt(i));
	    	}
		return result;
	}
	*/
	protected void loadTests(WikiInterfaceFX wiki, JFrame frame) {
		Vector<Test> tests = new Vector<Test>();
		try {
			String html = wiki.getPageHTML(this.getValue()+"");
			tests = wiki.parseHTML2Meta(html, this.toString());
		} catch (Exception e) {
			System.err.println("No Test found in " + this + "("+e+")");
//			e.printStackTrace();
		}
		for(int i = 0; i < this.getChildren().size(); i++) {
//			wiki.loadTests((MyTreeNode) this.getChildAt(i));
			((MyTreeNode)this.getChildren().get(i)).loadTests(wiki, frame);
		}		
		for(Test test : tests) {
			this.getChildren().add(new TestTreeNode(test));
			frame.repaint();
		}
	}	
	
	public boolean hasTests() {
		for(TreeItem<Object> child : getChildren()) {
			if(((MyTreeNode) child).hasTests()) return true;
		}
		return false;
	}
	
	public boolean hasWarning() {
		for(TreeItem<Object> child : getChildren()) {
			if(((MyTreeNode) child).hasWarning()) return true;
		}
		return false;
	}
	
	public boolean hasError() {
		for(TreeItem<Object> child : getChildren()) {
			if(((MyTreeNode) child).hasError()) return true;
		}
		return false;
	}

	public Node createPanel(WikiInterfaceFX wiki) {
		return new Pane();
	}
	
	public int checkIsDue(){
		int i=0;
		for(TreeItem<Object> child : getChildren()){
			if(((MyTreeNode) child).checkIsDue()>i)
				i=((MyTreeNode) child).checkIsDue();
		}
		return i;
	}


}

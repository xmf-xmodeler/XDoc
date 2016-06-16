package tool.doc;

import java.awt.Image;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

public class MyTreeNode extends DefaultMutableTreeNode{
	private static final long serialVersionUID = 1L;
	
	String name;
	TreeNodeType type;
	String content;

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

	public void save(PrintStream out) {}

	public void setName(String name2) {
		setUserObject(name2);
	}
}

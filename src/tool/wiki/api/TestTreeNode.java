package tool.wiki.api;

import java.awt.Image;

import javax.swing.ImageIcon;

import tool.doc.MyTreeCellRenderer;

public class TestTreeNode extends MyTreeNode{
	private static final long serialVersionUID = 7115216766256828677L;
	
	Test test;

	public TestTreeNode(Test test) {
		super(test.getTestName());
		this.test = test;
	}

	@Override
	public ImageIcon getIcon(Image defaultIcon) {
		try {
			boolean hasProblem2 = test.hasProblem;//||!test.hasToBeCheckedAgainstList.isEmpty();
			
			Image icon = new ImageIcon(hasProblem2?"icons/ListError.png":"icons/List.gif").getImage();
			if(hasProblem2) {
				icon = MyTreeCellRenderer.addProblem(icon).getImage();
				return new ImageIcon(icon);
			}
			int checkIsDue = 0;
			if(checkIsDue > 0) icon = MyTreeCellRenderer.addClock(icon, checkIsDue).getImage();
			return new ImageIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageIcon("icons/Error.gif") ;
		}
 	}

}

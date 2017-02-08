package tool.wiki.api;

import java.awt.Image;

import javax.swing.ImageIcon;

import tool.doc.MyTreeCellRenderer;

public class WikiPageTreeNode extends MyTreeNode{
	private static final long serialVersionUID = 1L;

	public WikiPageTreeNode(String category) {
		super(category);
	}
	
	@Override
	public ImageIcon getIcon(Image defaultIcon) {
		try {
//			boolean hasProblem2 = test.hasProblem;//||!test.hasToBeCheckedAgainstList.isEmpty();
			Image icon = new ImageIcon("icons/um-wiki.png").getImage();
//			if(hasProblem2) {
//				icon = MyTreeCellRenderer.addProblem(icon).getImage();
//				return new ImageIcon(icon);
//			}
			int checkIsDue = 0;
			if(checkIsDue > 0) icon = MyTreeCellRenderer.addClock(icon, checkIsDue).getImage();
			return new ImageIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageIcon("icons/Error.gif") ;
		}
 	}

}

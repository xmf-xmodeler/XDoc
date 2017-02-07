package tool.wiki.api;

import java.awt.Image;

import javax.swing.ImageIcon;

public class CategoryTreeNode extends MyTreeNode{
	private static final long serialVersionUID = 7115216766256828677L;

	public CategoryTreeNode(String category) {
		super(category);
	}

	@Override
	public ImageIcon getIcon(Image defaultIcon) {
//		try {
//			boolean hasProblem2 = true;
//			
//			Image icon = new ImageIcon(hasProblem2?"icons/ListError.png":"icons/List.gif").getImage();
//			if(hasProblem2) {
////				icon = MyTreeCellRenderer.addProblem(icon).getImage();
//				return new ImageIcon(icon);
//			}
//			int checkIsDue = 0;
//			if(checkIsDue > 0) icon = MyTreeCellRenderer.addClock(icon, checkIsDue).getImage();
//			return new ImageIcon(icon);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ImageIcon("icons/Error.gif") ;
//		}
		return new ImageIcon(defaultIcon);
 	}

}

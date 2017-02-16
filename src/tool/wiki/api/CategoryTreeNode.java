package tool.wiki.api;

import java.awt.Image;

import javax.swing.ImageIcon;

import tool.doc.MyTreeCellRenderer;

public class CategoryTreeNode extends MyTreeNode{
	private static final long serialVersionUID = 7115216766256828677L;

	public CategoryTreeNode(String category) {
		super(category);
	}

	@Override
	public ImageIcon getIcon(Image defaultIcon) {
//		if(hasTests()) defaultIcon = MyTreeCellRenderer.addIcon(defaultIcon, new ImageIcon("icons/CloudSmall.gif").getImage()).getImage();
//		if(hasError()) return new ImageIcon(MyTreeCellRenderer.addIcon(defaultIcon, new ImageIcon("icons/Achtung.png").getImage()).getImage());
//		if(hasWarning()) return new ImageIcon(MyTreeCellRenderer.addIcon(defaultIcon, new ImageIcon("icons/CalYellow.gif").getImage()).getImage());

		return new ImageIcon(defaultIcon);
 	}

}

package tool.wiki.api;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

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
			if(hasTests()) icon = MyTreeCellRenderer.addIcon(icon, new ImageIcon("icons/CloudSmall.gif").getImage()).getImage();
			return new ImageIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageIcon("icons/Error.gif") ;
		}
 	}

	@Override
	public Component createPanel(WikiInterface wiki) {
		final JEditorPane textField = new JEditorPane();
		textField.setContentType("text/html");
		final JEditorPane wikiField = new JEditorPane();
		final JEditorPane metaField = new JEditorPane();
		JSplitPane split3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(wikiField),  new JScrollPane(metaField));
		JSplitPane split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(textField),  split3);	
		split2.setDividerLocation(400);
		split3.setDividerLocation(400);
		
		try {
			String html = wiki.getPageHTML(getUserObject()+"");
			textField.setText(html);
			wikiField.setText(wiki.getPageWiki(getUserObject()+""));
			metaField.setText(html);
//			metaField.setText(parseHTML2Meta(html).toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return split2;
	}

}

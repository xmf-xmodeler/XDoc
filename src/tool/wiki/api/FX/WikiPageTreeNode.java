package tool.wiki.api.FX;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import tool.doc.MyTreeCellRenderer;

public class WikiPageTreeNode extends MyTreeNode{
	private static final long serialVersionUID = 1L;

	public WikiPageTreeNode(String category) {
		super(category);
		try {
		//setGraphic(new ImageView(new javafx.scene.image.Image(new File("icons\\um-wiki.png").toURI().toString())));
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
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
//			int checkIsDue = 0;
//			if(checkIsDue > 0) icon = MyTreeCellRenderer.addClock(icon, checkIsDue).getImage();
			if(hasTests()) icon = MyTreeCellRenderer.addIcon(icon, new ImageIcon("icons/CloudSmall.gif").getImage()).getImage();
			return new ImageIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageIcon("icons/Error.gif") ;
		}
 	}

	@Override
	public Node createPanel(WikiInterfaceFX wiki) {
		//final TextArea textField = new TextArea();
		final WebView textField = new WebView();
		textField.getEngine().loadContent("");
		//textField.setContentType("text/html");
		final TextArea wikiField = new TextArea();
		final TextArea metaField = new TextArea();
		SplitPane split3 = new SplitPane(wikiField, metaField);
		SplitPane split2 = new SplitPane(textField,  split3);	
		split2.setDividerPosition(0, 0.6);
		split2.setOrientation(Orientation.VERTICAL);
		//split2.setDividerLocation(400);
		//split3.setDividerLocation(400);
		
		try {
			String html = wiki.getPageHTML(getValue()+"");
			textField.getEngine().loadContent(html);
			wikiField.setText(wiki.getPageWiki(getValue()+""));
			metaField.setText(html);
//			metaField.setText(parseHTML2Meta(html).toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return split2;
	}

}

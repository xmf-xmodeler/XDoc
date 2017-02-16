package tool.wiki.api;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Component defaultResult = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		JLabel l = (JLabel) defaultResult;
		
		
		if(! (value instanceof MyTreeNode)) {
			return defaultResult;
		}
		
		MyTreeNode node = (MyTreeNode) value;
		
		try{
			Image icon = node.getIcon(((ImageIcon)l.getIcon()).getImage()).getImage();
			if(node.hasTests() &&! (node instanceof TestTreeNode)) icon = MyTreeCellRenderer.addIcon(icon, new ImageIcon("icons/CloudSmall.gif").getImage()).getImage();
			if(node.hasError()) icon = MyTreeCellRenderer.addIcon(icon, new ImageIcon("icons/Achtung.png").getImage()).getImage();
			else if(node.hasWarning()) icon = MyTreeCellRenderer.addIcon(icon, new ImageIcon("icons/CalYellow.gif").getImage()).getImage();
			if(icon != null) {l.setIcon(new ImageIcon(icon));}
		} catch (Exception e) {
			System.err.println("\t\t\t\tTrouble with icon. Using default.");
		}
		

		return defaultResult;
	}
	
	public static ImageIcon addIcon(Image background, Image foreground) {
        final BufferedImage combinedImage = new BufferedImage( 
                16,//imgBG.getWidth(), 
                16,//imgBG.getHeight(), 
                BufferedImage.TYPE_INT_ARGB );
        Graphics2D g = combinedImage.createGraphics();
        g.drawImage(background,0,0,null);
        g.drawImage(foreground,0,0,null);
        g.dispose();
        return new ImageIcon(combinedImage);
	}
	
	public static ImageIcon addClock(Image background, int checkIsDue) {
        final BufferedImage combinedImage = new BufferedImage( 
                16,//imgBG.getWidth(), 
                16,//imgBG.getHeight(), 
                BufferedImage.TYPE_INT_ARGB );
        Graphics2D g = combinedImage.createGraphics();
        g.drawImage(background,0,0,null);
        g.drawImage(new ImageIcon(
        		checkIsDue==1?"icons/CalWhite.gif":
        	    checkIsDue==2?"icons/CalYellow.gif":"icons/CalRed.gif").getImage(),0,0,null);
        g.dispose();
        return new ImageIcon(combinedImage);
	}	
	
	public static ImageIcon addProblem(Image background) {
        final BufferedImage combinedImage = new BufferedImage( 
                16,//imgBG.getWidth(), 
                16,//imgBG.getHeight(), 
                BufferedImage.TYPE_INT_ARGB );
        Graphics2D g = combinedImage.createGraphics();
        g.drawImage(background,0,0,null);
        g.drawImage(new ImageIcon("icons/Achtung.png").getImage(),0,0,null);
        g.dispose();
        return new ImageIcon(combinedImage);
	}
}

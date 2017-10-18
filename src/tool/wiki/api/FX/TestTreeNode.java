package tool.wiki.api.FX;

import java.awt.Image;

import javax.swing.ImageIcon;

import javafx.scene.Node;
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
			boolean hasProblem = test.hasError();//test.hasProblem;//||!test.hasToBeCheckedAgainstList.isEmpty();
			
			Image icon = new ImageIcon(hasProblem?"icons/ListError.png":"icons/List.gif").getImage();
			if(hasProblem) {
				icon = MyTreeCellRenderer.addProblem(icon).getImage();
				return new ImageIcon(icon);
			}
//			int checkIsDue = 0;
//			if(test.hasWarning()) //icon = MyTreeCellRenderer.addClock(icon, checkIsDue).getImage();
//				icon = MyTreeCellRenderer.addIcon(icon, new ImageIcon("icons/CalYellow.gif").getImage()).getImage();
			return new ImageIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
			return new ImageIcon("icons/Error.gif") ;
		}
 	}

	@Override
	public boolean hasTests() {return true;}
	
	@Override
	public boolean hasWarning() {return test.hasWarning();}
	
	@Override
	public boolean hasError() {return test.hasError();}

	@Override
	public Node createPanel(WikiInterfaceFX wiki) {
		return test.createPanel();
	}
	public int checkIsDue(){
		return test.checkIsDue();
	}
	
}

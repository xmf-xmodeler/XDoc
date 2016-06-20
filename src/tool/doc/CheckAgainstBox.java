package tool.doc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

public class CheckAgainstBox extends JScrollPane {
	private static final long serialVersionUID = 1L;
	
	private JList<MyTreeNode> list;

	public CheckAgainstBox(Vector<MyTreeNode> hasToBeCheckedAgainst) {
		MyListModel listModel = new MyListModel(hasToBeCheckedAgainst);
		list = new JList<>(listModel);
		list.addMouseListener(listModel);
		setViewportView(list);
	}
	
	private class MyListModel extends AbstractListModel<MyTreeNode> implements MouseListener {
		private static final long serialVersionUID = 1L;
		private Vector<MyTreeNode> hasToBeCheckedAgainst;
		
		private MyListModel(Vector<MyTreeNode> hasToBeCheckedAgainst) {
			this.hasToBeCheckedAgainst = hasToBeCheckedAgainst;
		}

		@Override
		public MyTreeNode getElementAt(int nr) {
			return hasToBeCheckedAgainst.get(nr);
		}

		@Override
		public int getSize() {
			return hasToBeCheckedAgainst.size();
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {

			final MyTreeNode selectedNode = list.getSelectedValue();
			if(selectedNode == null) return;
			
			if(e.getButton() == MouseEvent.BUTTON3) {
				JPopupMenu menu = new JPopupMenu();
				JMenuItem gotoMenu = new JMenuItem("(Go To Node)");
				gotoMenu.addActionListener(new ActionListener() {			
					@Override public void actionPerformed(ActionEvent e) {goToNode(selectedNode);}

				});
				menu.add(gotoMenu);
				JMenuItem deleteMenu = new JMenuItem("Remove from List");
				deleteMenu.addActionListener(new ActionListener() {			
					@Override public void actionPerformed(ActionEvent e) {deleteNode(selectedNode);}

				});
				menu.add(deleteMenu);
				menu.show(CheckAgainstBox.this, e.getX(), e.getY());
			}
		}

		private void goToNode(MyTreeNode selectedNode) {
			// TODO Auto-generated method stub
			
		}
		
		private void deleteNode(MyTreeNode selectedNode) {
			int pos = hasToBeCheckedAgainst.indexOf(selectedNode);
			hasToBeCheckedAgainst.remove(selectedNode);
			fireIntervalRemoved(this, pos, pos);
		}

		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
		@Override public void mousePressed(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
	}
	
	
}

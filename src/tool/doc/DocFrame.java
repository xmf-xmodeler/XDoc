package tool.doc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class DocFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	JSplitPane split;
	JScrollPane right;
	MyTree tree;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				DocFrame frame = new DocFrame();
				final MyTree tree = new MyTree();
				frame.tree = tree;
				JScrollPane left = new JScrollPane(tree);
				frame.right = new JScrollPane();
				frame.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, frame.right);
				frame.split.setDividerLocation(300);
				frame.setContentPane(frame.split);

				frame.pack();

				frame.setLocation(400, 200);
				frame.setSize(900, 600);
				frame.setTitle("XDoc");

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				final MyTreeModel model = new MyTreeModel(frame);
				tree.setModel(model);

				JMenuBar menubar = new JMenuBar();
				JMenu menu = new JMenu("File");
				JMenuItem menuItem1 = new JMenuItem("Save");
				menuItem1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tree.save();
					}
				});
				menu.add(menuItem1);
				JMenuItem menuItem2 = new JMenuItem("Load");
				menuItem2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tree.load();
					}
				});
				menu.add(menuItem2);
				menubar.add(menu);

				frame.setJMenuBar(menubar);

				frame.setVisible(true);
			}
		});
	}

	public void setEditPanel(JPanel p) {	
		right.setViewportView(p);
	}

}

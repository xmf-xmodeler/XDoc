package tool.doc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class DocFrame extends JFrame implements WindowListener{
	private static final long serialVersionUID = 1L;
	
	JSplitPane split;
	JScrollPane right;
	MyTree tree;
	
	public static String user = null;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				
				DocFrame frame = new DocFrame();
				
				readUser(frame);
				
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

				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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
				JMenuItem menuItem3 = new JMenuItem("Report");
				menuItem3.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tree.report();
					}
				});
				menu.add(menuItem3);
				menubar.add(menu);
				
				JMenu menu2 = new JMenu("Test");
				JMenuItem menuItem21 = new JMenuItem("Go to most urgent Test");
				menuItem21.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tree.goToMostUrgent();
					}
				});
				menu2.add(menuItem21);
				JMenuItem menuItem22 = new JMenuItem("Show most urgent Tests");
				menuItem22.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tree.showMostUrgent(frame);
					}
				});
				menu2.add(menuItem22);
				JMenuItem menuItem23 = new JMenuItem("Go to random Test");
				menuItem23.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tree.goToRandom();
					}
				});
				menu2.add(menuItem23);
				menubar.add(menu2);
				
								

				frame.setJMenuBar(menubar);

				frame.setVisible(true);
				
				frame.addWindowListener(frame);
				
				Timer t = new Timer(250, new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						frame.repaint();
					}
				});
				
				t.start();
			}
		});
	}

	public void setEditPanel(JPanel p) {	
		right.setViewportView(p);
	}
	
	private static void readUser(JFrame parent) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("user.txt"));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append("\n");
					line = br.readLine();
				}
				user = sb.toString();
			} finally {
				br.close();
			}

			if (user.equals("")) {
				try {
					File file2 = new File("user.txt");
					PrintStream out;
					out = new PrintStream(file2, "UTF-8");
					out.print("newUserName");
					user = "anonymous";
					out.close();
					JOptionPane.showMessageDialog(parent, "No username found. Write name into user.txt and restart");
					System.exit(0);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		int save = JOptionPane.showConfirmDialog(this, "Save before EXIT?");
		switch (save) {
		case JOptionPane.YES_OPTION:
			tree.getModel().save();
		case JOptionPane.NO_OPTION:
			System.exit(0);
		}
	}

	@Override public void windowActivated(WindowEvent e) {}
	@Override public void windowClosed(WindowEvent e) {}
	@Override public void windowDeactivated(WindowEvent e) {}
	@Override public void windowDeiconified(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e) {}
	@Override public void windowOpened(WindowEvent e) {}
}

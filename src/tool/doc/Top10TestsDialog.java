package tool.doc;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.LineNumberInputStream;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Top10TestsDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	TestNode selectedNode = null;
	
	public Top10TestsDialog(Frame owner, Vector<TestNode> mostUrgent) {
		super(owner, "Top 10 Tests", true);
		
		JPanel panel = new JPanel();
		JList<TestNode> list = new JList<TestNode>(mostUrgent);
		
		JScrollPane scrollPane = new JScrollPane(list);
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGap(4)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(scrollPane)
				.addGroup(layout.createSequentialGroup()
					.addGap(4,4,Integer.MAX_VALUE)
					.addComponent(okButton)
					.addGap(4)
					.addComponent(cancelButton)))
			.addGap(4));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGap(4)
			.addGroup(layout.createSequentialGroup()
				.addComponent(scrollPane)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(okButton)
					.addComponent(cancelButton)))
			.addGap(4));
		
		setContentPane(panel);
		
		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		
		pack();
		
		setLocation(owner.getLocation().x + owner.getWidth()/2 - getWidth()/2,
				    owner.getLocation().y + owner.getHeight()/2 - getHeight()/2);

		okButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				selectedNode = list.getSelectedValue();
				setVisible(false);
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					int index = list.locationToIndex(e.getPoint());
					if(index >= 0) {
						selectedNode = list.getSelectedValue();
						setVisible(false);
					}
				}
			}
		});
	}
	
	public TestNode getChosenNode() {
		return selectedNode;
	}
     
}

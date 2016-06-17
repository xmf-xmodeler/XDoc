package tool.doc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class MyTreeModel extends DefaultTreeModel {

	private static final long serialVersionUID = 1L;
	private DocFrame frame;
	
	private transient DefaultMutableTreeNode clipboard;
	
	public MyTreeModel(DocFrame parent) {
		super(new MyTreeNode("Root"));
		this.frame = parent;
		load();
	}

	public void actionRename(MyTreeNode node) {
		String name = JOptionPane.showInputDialog(frame, "New name:", node);
		if(name != null) node.setName(name);
		nodeChanged(node);
	}

	public void actionAdd(DefaultMutableTreeNode parent) {
		String name = JOptionPane.showInputDialog(frame, "New node:", "new node");
		if(name != null) {
			MyTreeNode child = new MyTreeNode(name);
			insertNodeInto(child, parent, parent.getChildCount());
		}
	}

	public void actionAddRequirements(DefaultMutableTreeNode parent) {
		RequirementsNode child = new RequirementsNode("Requirements");
		insertNodeInto(child, parent, parent.getChildCount());
	}

	public void actionAddTests(DefaultMutableTreeNode parent) {
		TestsNode child = new TestsNode("Tests");
		insertNodeInto(child, parent, parent.getChildCount());
	}

	public void actionAddRequirement(DefaultMutableTreeNode parent) {
		String name = JOptionPane.showInputDialog(frame, "New Requirement:", "new Requirement");
		RequirementNode child = new RequirementNode(name);
		insertNodeInto(child, parent, parent.getChildCount());
	}

	public void actionAddTest(DefaultMutableTreeNode parent) {
		String name = JOptionPane.showInputDialog(frame, "New Test:", "new Test");
		TestNode child = new TestNode(name);
		insertNodeInto(child, parent, parent.getChildCount());
	}
	
	public void actionAddSpecification(DefaultMutableTreeNode parent) {
		String name = JOptionPane.showInputDialog(frame, "New Specification:", "new Specification");
		SpecificationNode child = new SpecificationNode(name);
		insertNodeInto(child, parent, parent.getChildCount());
	}

	public void save() {
		try {
			File file = new File("doc/mainDoc.xdoc");
			PrintStream out;
			out = new PrintStream(file, "UTF-8");
			out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<XModelerDocumentation>\n");
			writeTree(out, (MyTreeNode) getRoot(), "  ");
			out.print("</XModelerDocumentation>");
			out.close();
		} catch (FileNotFoundException e) {e.printStackTrace();
		} catch (UnsupportedEncodingException e) {e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void writeTree(PrintStream out, MyTreeNode node, String prefix) {
		out.print(prefix + "<Node");
		node.save(out);
		if(node.getChildCount() > 0) {
			out.print(">\n");
			for(Object o : Collections.list(node.children())) {
				writeTree(out, (MyTreeNode) o, prefix + "  ");
			}
			out.print(prefix + "</Node>\n");
		} else {
			out.print("/>\n");
		}
		
	}

	public void load() {

		try {
			File fXmlFile = new File("doc/mainDoc.xdoc");
			if (fXmlFile.exists()) {
			    SAXBuilder builder = new SAXBuilder();
				Document document = (Document) builder.build(fXmlFile);
		        Element rootNode = document.getRootElement();
				if (rootNode.getName().equals("XModelerDocumentation")) {
					MyTreeNode root = loadTree(rootNode.getChildren().get(0));
					setRoot(root);
				} else {
				}
			}
		} catch (IOException e) { e.printStackTrace();
		} catch (JDOMException e) { e.printStackTrace();
		}

	}

	private MyTreeNode loadTree(Element node) {
		String name = node.getAttributeValue("name");
		String type = node.getAttributeValue("type");
		
		MyTreeNode treeNode;
		
		if("test".equals(type)) {
			treeNode = new TestNode(node);
		} else if("requirement".equals(type)) {
			treeNode = new RequirementNode(node);
		} else if("specification".equals(type)) {
			treeNode = new SpecificationNode(node);
		} else {
			treeNode = new MyTreeNode(name);
		}
	
		List<Element> children = node.getChildren();
		for(int i = 0; i < children.size(); i++) {
			Element child = children.get(i);
			MyTreeNode childTreeNode = loadTree(child);
			treeNode.add(childTreeNode);
		}
		return treeNode;
	}

	public void actionCopy(DefaultMutableTreeNode node) {
		clipboard = node;		
	}

	public void actionCut(DefaultMutableTreeNode node) {
		actionCopy(node);
		actionDelete(node);
	}

	public void actionPaste(DefaultMutableTreeNode parent) {
		MyTreeNode newNode = copyTree((MyTreeNode) clipboard);
		insertNodeInto(newNode, parent, parent.getChildCount());
	}

	private MyTreeNode copyTree(MyTreeNode original) {
		MyTreeNode newNode = new MyTreeNode(original.getUserObject()+"");
		for(int i = 0; i < original.getChildCount(); i++) {
			MyTreeNode child = (MyTreeNode) original.getChildAt(i);
			newNode.add(copyTree(child));
		}
		return newNode;
	}

	public void actionDelete(DefaultMutableTreeNode node) {
//		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
//		parent.remove(node);
		removeNodeFromParent(node);
	}

	public void actionUp(DefaultMutableTreeNode node) {
		// TODO Auto-generated method stub
		
	}

	public void actionDown(DefaultMutableTreeNode node) {
		// TODO Auto-generated method stub
		
	}

	public void showNodePanel(MyTreeNode node) {
		JPanel p = node.createPanel();
		frame.setEditPanel(p);
	}

	public void storeValues(MyTreeNode node) {
		if(node != null) node.storeValues();
	}


}

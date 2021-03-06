package tool.wiki.api;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class WikiInterface {
	
	private String user;
	private String pass;
	private ArrayList<Test> changedTests;
	private String testedVersion;
		
	public WikiInterface() {
		System.setProperty("jsse.enableSNIExtension", "false");
		
		java.net.CookieManager cm = new java.net.CookieManager();
		java.net.CookieHandler.setDefault(cm);
	}
	
	public void login(String user, String pass) {
		JsonObject tokenQueryResponse = excutePostJson("action=query&meta=tokens&type=login");
		String token = tokenQueryResponse
	    	.get("query").getAsJsonObject()
	    	.get("tokens").getAsJsonObject()
	    	.get("logintoken").getAsString();
		
		token = token.substring(0, token.length()-2) + "%2B%5C";

		JsonObject loginResult = excutePostJson("action=login"
				+ "&lgname=" + user
				+ "&lgpassword=" + pass
				+ "&lgtoken=" + token);
		
		String result = loginResult+"";
		System.out.println(result);
		//if(!result.contains("\"result\":\"Success\"")) throw new RuntimeException("Login failed: " + result);
		//create Loginwindow, if connection fails
		if(!result.contains("\"result\":\"Success\"")) {
			JTextField fieldUser = new JTextField();
			JPasswordField fieldPassword = new JPasswordField();
			Object[] Inputs = { "Please enter Username and Password",
					"Username: ", fieldUser,
					"Password: ", fieldPassword};
			int option = JOptionPane.showConfirmDialog(null, Inputs,"Wrong username or password",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
			if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION){
				throw new RuntimeException("Login failed: " + result);
			}
			this.user = fieldUser.getText();
			this.pass = new String(fieldPassword.getPassword());
			
			try{
			PrintStream out = new PrintStream("wiki-user.txt");
			out.println(this.user);
			out.append(this.pass);
			out.close();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			login(this.user,this.pass);
		}
	}
	
	public void setPageWiki(String pageName, String text, String comment, String section) throws UnsupportedEncodingException {
		String tokenText = excutePost("action=query&meta=tokens");//action=query&prop=info|revisions&intoken=edit&format=json&titles=" + pageName);
		int tokenStart = tokenText.indexOf("\"editToken\":") + 13; // "edittoken":"
		int tokenEnd = tokenText.indexOf("+", tokenStart);
		String token = tokenText.substring(tokenStart, tokenEnd);
		
//		String result = 
				excutePost("action=edit"
				+ "&title=" + pageName
				+ "&text=" + URLEncoder.encode(text, "UTF-8")
				+ "&summary=" + URLEncoder.encode(comment, "UTF-8")
				+ (section != null ? ("&section=" + section) : "")
				+ "&token=" + token + "%2B%5C"
				+ "&starttimestamp=now");
	}

	public String getPageHTML(String pageName) {
		JsonObject jsonObject = excutePostJson("action=parse&prop=text&page=" + pageName);

		return jsonObject.get("parse")
				.getAsJsonObject().get("text")
				.getAsJsonObject().get("*")
				.getAsString();
	}

	public String getPageWiki(String pageName) throws JDOMException, IOException {
		String xml = excutePost("action=query&prop=revisions&rvprop=content&format=xml&titles=" + pageName);
		
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(xml));

		Element rootNode = document.getRootElement();

		Element query = rootNode.getChildren("query").get(0);
		Element pages = query.getChildren("pages").get(0);
		Element page = pages.getChildren("page").get(0);
		Element revisions = page.getChildren("revisions").get(0);
		Element rev = revisions.getChildren("rev").get(0);
		
		return rev.getText();
	}
	
	JFrame frame;

	public static void main(String[] args) throws Exception {
		
		final WikiInterface wiki = new WikiInterface();

		wiki.readUser(null);
		wiki.login(wiki.user, wiki.pass);
		
		wiki.changedTests = new ArrayList<Test>();
//		String wikiText = wiki.getPageWiki("Hauptseite");
		
//		String htmlText = wiki.getPageHTML("Hauptseite");
		
//		wiki.setPageWiki("APITestPage", "Hier kommt der neue Text", "Edit via API", null);
		
//		System.out.println(wiki.getCategoryMembers("XModeler", true, false));
		
		wiki.frame = new JFrame();
		wiki.createMenuBar();
		wiki.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		wiki.frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				if (!wiki.changedTests.isEmpty()){
					int result = JOptionPane.showConfirmDialog(wiki.frame,
							"There are unsaved test results, would you like to submit them?",
							"Confirm Dialog",
							JOptionPane.YES_NO_CANCEL_OPTION);
					switch(result){
					case JOptionPane.YES_OPTION :	wiki.saveChanges(); System.exit(0);	break;
					case JOptionPane.NO_OPTION :	System.exit(0);	break;		
					case JOptionPane.CANCEL_OPTION: break;
					case JOptionPane.CLOSED_OPTION : break;
					}
				}
				else
					System.exit(0);
			}	
		});
		wiki.frame.setSize(1200, 800);
		wiki.frame.setLocation(200, 100);
		wiki.frame.setTitle("Wiki Interface Test");
		wiki.versionDialog();
		final CategoryTreeNode root = (CategoryTreeNode) wiki.getCategoryTree("XModeler", true);
		JTree tree = new JTree(root);
//		final JEditorPane textField = new JEditorPane();
//		textField.setContentType("text/html");
//		final JEditorPane wikiField = new JEditorPane();
//		final JEditorPane metaField = new JEditorPane();
//		JSplitPane split3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(wikiField),  new JScrollPane(metaField));
//		JSplitPane split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(textField),  split3);	
		final JSplitPane split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tree),  new JPanel());
//		split1.setDividerLocation(400);
//		split2.setDividerLocation(400);
//		split3.setDividerLocation(400);
		
		tree.setCellRenderer(new MyTreeCellRenderer());
		
//		tree.setCellRenderer(new TreeCellRenderer() {
//			final TreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
//			
//			@Override
//			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
//					boolean leaf, int row, boolean hasFocus) {
//				Component component = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
//				JLabel label = (JLabel) component; 
//				DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) value;
//				return component;
//			}
//		});	
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				MyTreeNode dmtn = (MyTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
				int div = split1.getDividerLocation();
				split1.setRightComponent(dmtn.createPanel(wiki));
				split1.setDividerLocation(div);
//				if(dmtn instanceof TestTreeNode) {
//					split1.setRightComponent(((TestTreeNode)dmtn).test.createPanel());
//				} else try {
//					String html = wiki.getPageHTML(dmtn.getUserObject()+"");
//					textField.setText(html);
//					wikiField.setText(wiki.getPageWiki(dmtn.getUserObject()+""));
//					metaField.setText(html);
////					metaField.setText(parseHTML2Meta(html).toString());
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
			}
		});
		
		wiki.frame.setContentPane(split1);
		wiki.frame.setVisible(true);
		
		
		Thread loadTestThread = new Thread(new Runnable() {
			@Override
			public void run() {
				wiki.loadTests(root);
			}
		});
		
		loadTestThread.start();
	}

	protected void loadTests(MyTreeNode root) {
		Vector<Test> tests = new Vector<Test>();
		try {
			String html = getPageHTML(root.getUserObject()+"");
			tests = parseHTML2Meta(html, root.toString());
		} catch (Exception e) {
			System.err.println("No Test found in " + root + "("+e+")");
//			e.printStackTrace();
		}
		for(int i = 0; i < root.getChildCount(); i++) {
			loadTests((MyTreeNode) root.getChildAt(i));
		}		
		for(Test test : tests) {
			root.add(new TestTreeNode(test));
			frame.repaint();
		}
	}

	public Vector<Test> parseHTML2Meta(String html, String pageName) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		html = "<html>" + html + "</html>";
		Document document = (Document) builder.build(new StringReader(html));

		Element rootNode = document.getRootElement();
		Vector<Test> tests = new Vector<Test>();
		Test test = new Test("VOID");
		String h2KeyArmed = null;
		String currentTocID = null;
		
		for(int i = 0; i < rootNode.getChildren().size(); i++) {
			Element node = rootNode.getChildren().get(i);
			if(node.getName().equals("h1")) {
				/* Found Header 1. This could be a test. 
				 * If it has the required h2 headers it can be recognized. 
				 * First: check if the existing test is valid and put it into a list.*/
				if(test.isValid()) tests.add(test);
				Element span = node.getChildren("span").get(0);
				String testName = span.getText();
				test = new Test(testName);
				h2KeyArmed = null;
			}
			if(node.getName().contentEquals("p") && h2KeyArmed == null){
				String text = node.getText();
				if (!text.equals("")&&text.startsWith("Priority")){ //possible priority value
					//System.out.println(text);
					try{
					test.priority = Double.parseDouble(text.split("\\s+")[2]);	}
					catch (Exception e){
						System.err.println("Could not parse priority information");
					}
				}
			}
			if(node.getName().equals("h2")) {
				Element span = node.getChildren("span").get(0);
				String text = span.getText();
				if("Preconditions".equals(text)
						|| "Postconditions".equals(text) 
						|| "Action".equals(text) 
						|| "Test Results".equals(text)) {
					h2KeyArmed = text;
				}
				currentTocID = readTocID(node);
				if("Test Results".equals(h2KeyArmed) && test != null) {
					test.setWikiInterface(this);
					test.addTestTocID(pageName, currentTocID);
				}
			}
			if(node.getName().equals("p") && h2KeyArmed != null && !"Test Results".equals(h2KeyArmed)) {
				String text = node.getText();
				if(text == null || "".equals(text.trim())) {
					text = "Empty <p> node: "+node;
				}
				test.addText(h2KeyArmed, text);
			}
			if(node.getName().equals("table") && "Test Results".equals(h2KeyArmed)) {
				test.readTestResults(node);
				h2KeyArmed = null;
			}
		}

		if(test.isValid()) tests.add(test); // Check last remaining Test
		
		return tests;
	}
	
	private static String readTocID(Element hNode) {
	    Element spanNode = hNode.getChildren("span").get(1); // 0 is name // 1 is the edit link
	    Element aNode = spanNode.getChild("a"); // the link has two spans for [ and ] and a link in between 
	    String hrefText = aNode.getAttributeValue("href"); // the reference contains the section number
	    int start = hrefText.indexOf("section=") + "section=".length();
	    int end   = hrefText.indexOf(";", start);
	    if(start < 0) return null;
	    if(end < 0) return hrefText.substring(start);
	    if(end < start) return null;
		return hrefText.substring(start, end);
	}

	public Vector<String> getCategoryMembers(String categoryName, boolean categories, boolean pages) throws UnsupportedEncodingException {
		categoryName = URLEncoder.encode(categoryName, "UTF-8");
		String options = categories&&pages?"":
			categories&&!pages?"&cmtype=subcat":"&cmtype=page";
		JsonObject o = excutePostJson("action=query&list=categorymembers&cmtitle=Category:"+categoryName+options);
		o = o.get("query").getAsJsonObject();
		
		System.err.println(o);

		JsonArray list = o.get("categorymembers").getAsJsonArray();
		Vector<String> result = new Vector<String>();
		for(int i = 0; i < list.size(); i++) {
			result.add(list.get(i).getAsJsonObject().get("title").getAsString());
		}
		return result;
	}
	
	public MyTreeNode getCategoryTree(String name, boolean isCategory) throws UnsupportedEncodingException {
		MyTreeNode treeNode = isCategory 
				? new CategoryTreeNode(name)
				: new WikiPageTreeNode(name);
		for(String childCategoryNameWithPrefix : getCategoryMembers(name, true, false)) {
			String childCategoryName = childCategoryNameWithPrefix.substring(childCategoryNameWithPrefix.indexOf(":")+1);
			MyTreeNode childCategoryTreeNode = getCategoryTree(childCategoryName,true);
			treeNode.add(childCategoryTreeNode);
		}
		for(String pageName : getCategoryMembers(name, false, true)) {
			MyTreeNode pageTreeNode = getCategoryTree(pageName,false);
			treeNode.add(pageTreeNode);
		}
		return treeNode;
	}

	private static JsonObject excutePostJson(String urlParameters) {
		String jsonText = excutePost(urlParameters + "&format=json");
		return new JsonParser().parse(jsonText).getAsJsonObject();
	}
	
	@Deprecated
	private static String excutePost(String urlParameters) {
		String targetURL = "https://www.wi-inf.uni-duisburg-essen.de/xmodeler-wiki/api.php";
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush();
	      wr.close();

	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append("\n\r");
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {

	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	  }

	private void readUser(JFrame parent) {
		String text = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("wiki-user.txt"));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append("\n");
					line = br.readLine();
				}
				text = sb.toString();
				
				String[] text2 = text.split("\\r\\n|\\n|\\r");
				user = text2[0];
				pass = text2[1];
			} finally {
				br.close();
			}
	    } catch (Exception e) {}

		if (text == null || text.equals("")) {
			try {
				File file2 = new File("wiki-user.txt");
				PrintStream out;
				out = new PrintStream(file2, "UTF-8");
				JTextField fieldUser = new JTextField();
				JPasswordField fieldPassword = new JPasswordField();
				Object[] Inputs = { "Please enter Username and Password",
						"Username: ", fieldUser,
						"Password: ", fieldPassword};
				int option = JOptionPane.showConfirmDialog(null, Inputs,"Logindata is missing",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION){
					out.close();
					System.exit(0);
				}
				out.println(fieldUser.getText());
				out.println(fieldPassword.getPassword());
				user = fieldUser.getText();
				pass = new String(fieldPassword.getPassword());
				out.close();
				//JOptionPane.showMessageDialog(parent, "No username found. Write name into wiki-user.txt and restart");
				//System.exit(0);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveChanges(){
		if (!changedTests.isEmpty() && changedTests != null){
			for (Test test : changedTests){
				test.submitResults();
				removeChangedTest(test);
			}
		}
		else System.out.println("Error while saving; ");
	}
	public void addChangedTest(Test pTest){
		changedTests.add(pTest);
		//System.out.println("Added Test: "+ pTest.getTestName());
	}
	public void removeChangedTest(Test pTest){
		if (changedTests.contains(pTest)) {
			changedTests.remove(pTest);
			//System.out.println("Removed Test: "+ pTest.getTestName());
		}
	}
	public boolean changedTestsIsEmpty(){
		return changedTests.isEmpty();
	}
	private void createMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		
		//save option
		JMenuItem menuItem = new JMenuItem("Save All Tests");
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!changedTestsIsEmpty()){
					int result = JOptionPane.showConfirmDialog(frame,
							"Would you like to save all unsaved Testresults?",
							"Confirm Dialog",
							JOptionPane.OK_CANCEL_OPTION);
					switch(result){
					case JOptionPane.YES_OPTION :	saveChanges();	break;
					case JOptionPane.CLOSED_OPTION:	break;
					case JOptionPane.CANCEL_OPTION: break;
					}
				}
				else JOptionPane.showMessageDialog(frame, "There are no unsaved Testresults");
			}
		});
		menu.add(menuItem);
		
		//set version number
		menuItem = new JMenuItem("Set Version Number");
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				versionDialog();
				
			}
		});
		menu.add(menuItem);
		
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
	}
	public String getVersion(){
		if (testedVersion != null)
			return testedVersion;
		else return "not set";
	}
	public int versionDialog(){
		JTextField fieldVersion = new JTextField();
		Object[] Inputs = { "Please enter build number",
				fieldVersion};
		int option = JOptionPane.showConfirmDialog(frame, Inputs,"Build number not set",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
		if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION){
			testedVersion = "not set";
			System.exit(0);
		}
		if (option == JOptionPane.OK_OPTION){
			if (!fieldVersion.getText().isEmpty()){
			testedVersion = fieldVersion.getText();
			}
			else {
				testedVersion = "not set";
			}
		}
		return option;
	}

}


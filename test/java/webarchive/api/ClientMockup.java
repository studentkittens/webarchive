package webarchive.api;

import java.io.*;
import java.net.Inet4Address;
import java.util.List;
import java.util.Scanner;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import webarchive.api.model.CommitTag;
import webarchive.api.model.MetaData;
import webarchive.api.select.SelectCommitTag;
import webarchive.api.select.SelectMetaByCommit;
import webarchive.api.xml.DataElement;
import webarchive.api.xml.TagName;
import webarchive.api.xml.XmlEditor;

/**
 *
 * @author ccwelich
 */
public class ClientMockup {

	public static void main(String[] args) throws Exception {
		WebarchiveClientFactory factory = new WebarchiveClientFactory(new Inet4Address(
			"localhost"));
		WebarchiveClient client = factory.getInstance();

		System.out.println("client::select: commitTags");
		List<CommitTag> commits = client.select(new SelectCommitTag(null, null,
			"commitTime DESC"));
		for (CommitTag t : commits) {
			System.out.println("  " + t.getCommitTime().getXmlFormat() + ", " + t.
				getDomain());
		}
		List<MetaData> metaList = client.select(new SelectMetaByCommit(commits.
			subList(0, 1), null, null, null, "createTime ASC"));
		System.out.println("client::select: MetaData by CommitTag (1st element)");
		for (MetaData m : metaList) {
			System.out.println("  " + m);
		}

		MetaData meta = metaList.get(0);
		fileListOf(meta, client);

		System.out.println("client::getInputStream: data");
		int wc = wc(client.getInputStream(meta, new File("data")));
		System.out.println("  wordcount = " + wc);

		System.out.println("client::getOutputStream: write out word count");
		PrintWriter pw = new PrintWriter(client.getOutputStream(meta, new File(
			"wc.txt")));
		pw.println("wordcount=" + wc);
		System.out.println("  done");

		fileListOf(meta, client);

		System.out.println("client::getXmlEditor");
		XmlEditor xmlEditor = client.getXMLEditor(meta);
		DataElement dataElement = xmlEditor.createDataElement(new TagName(
			"wordcount"));
		Element element = xmlEditor.createElement(new TagName("value"));
		System.out.println("Element to add: (value of wordcount)");
		printNodes("  ", element);
		dataElement.appendChild(element);
		System.out.println("client::addDataElement");
		try {
			xmlEditor.addDataElement(dataElement);
		} catch (SAXException ex) {
			System.out.println("asserted SAXException:");
			System.out.println(ex);
		}
		
		System.out.println("client::getDataElement: wordcount");
		dataElement = xmlEditor.getDataElement(new TagName(("wordcount")));
		NodeList nodes = dataElement.getChildNodes();
		for(int i=0; i<nodes.getLength(); i++) {
			printNodes("  ", nodes.item(i));
		}


	}

	private static void fileListOf(MetaData meta, WebarchiveClient client) throws
		Exception {
		System.out.println("client::getFileList: of element: " + meta);
		List<File> files = client.getFileList(meta);
		for (File f : files) {
			System.out.println("  " + f);
		}
	}

	public static int wc(InputStream in) throws IOException {
		Scanner sc = new Scanner(in);
		int cnt = 0;
		while (sc.hasNext()) {
			sc.next();
			cnt++;
		}
		in.close();
		return cnt;
	}

	public static void printNodes(String indent, Node n) {
		if (n == null) {
			return;
		}
		System.out.println(indent + n);
		NamedNodeMap attributes = n.getAttributes();
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				Node att = attributes.item(i);
				System.out.println(indent + att);
			}
		}
		NodeList children = n.getChildNodes();
		indent += "  ";
		for (int i = 0; i < children.getLength(); i++) {
			printNodes(indent, children.item(i));
		}
	}
}

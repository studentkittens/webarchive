package test;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import webarchive.api.model.MetaData;
import webarchive.api.model.TimeStamp;
import webarchive.api.select.SelectCommitTag;
import webarchive.api.xml.DataElement;
import webarchive.api.xml.TagName;
import webarchive.api.WebarchiveClient;
import webarchive.api.WebarchiveObserver;

public class Testclient {
	private List<MetaData> metaList;
	private List<File> FileList; 
	private MetaData meta;
	private File file;
	private InputStream save;
	private String text;
	private int counter;
	private FileWriter writer;
	private File countFile;
	private WebarchiveClient client; 
	
Testclient() throws Exception{		
		test();
	}
	
	private void test() throws Exception{
		connect();
		select();
		dataSelect();
		filewrite();
		upload();
		editXML();	
		}	
	
	private void connect() throws Exception {
		WebarchiveObserver observer = (WebarchiveObserver) this;	
		client.addObserver(observer);	
		}
	
	@SuppressWarnings("unchecked")
	private void select() throws Exception{
		int i=0;
		int var=-1;
		TimeStamp date = new TimeStamp(new Date ());
		
		String where = "commitTime > " + date.getXmlFormat(); 
		String orderBy = "commitTime ASC";
		metaList =client.select(new SelectCommitTag(where, null, orderBy));
		
		for ( MetaData c : metaList ) {System.out.println(i+". "+c);}
		Scanner kbReader = new Scanner(System.in);
		while(true){			
					try{					
					System.out.println("gib zahl ein");
					var = kbReader.nextInt() ;
					if(var<0||var>metaList.size()-1){continue;}
					else{break;}			
					}catch(Exception e){}
					kbReader.next();				
				}		
		 meta= 	metaList.get(var);	
		 FileList = client.getFileList(meta);
		 file= 	FileList.get(0);	
	}
	
	private void dataSelect() throws Exception{
		char i;
		
		save = client.getInputStream( meta, file);
		if (save!=null){
		text = save.toString();
			for(int a=0;  a<=text.length();a++){
				i=text.charAt(a);
				if(i==32){counter++;}
				// vom stream -> string
			}	
		}	
	}
		
	private void filewrite(){
		
			countFile = new File("wordcounted.txt");
			try {
			      
			       writer = new FileWriter(countFile ,true);
			       writer.write(""+counter+" words");
			       writer.flush();
			       writer.close();
			    } catch (IOException e) {}
			
		}
		
	private void upload() throws Exception{
		client.getOutputStream(meta, countFile);	
	}
	
	private void editXML() throws Exception{
		String name ="X:Count:Words"+counter;
		TagName tagName;
		DataElement element;
		
		api.xml.XmlEditor test = client.getXMLEditor(meta);
		tagName = new api.xml.TagName(name);
		element = test.createDataElement(tagName);
		test.createElement(tagName);
		test.addDataElement(element);
	
	}
			
}

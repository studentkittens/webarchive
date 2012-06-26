

import java.io.*;
import java.util.List;
import java.util.Scanner;
import webarchive.api.*;


/*
 * TODO Check declarations; 
 */


public class Testclient<c> {
	private List<Metadata> metaList;
	private List<File> FileList; 
	private metadata meta;
	private File file;
	private Byte reciveFile;
	private InputStreamReader save;
	private String text;
	private int counter;
	FileWriter writer;
	  File countFile;
	
	
	Testclient(){
		test();
	}
	
private void test(){
	connect();
	select();
	dataSelect();
	filewrite();
	upload();
	editXML();
	
	
}	
	
private void connect() {
		
		WebarchiveClient client=webarchiv.api.WebarchiveClientFactory.getInstance(); 
		
	}
private void select(){
	metaList = new webarchiv.api.WebarchiveClient.select(Select); //add select, TestData needed
	int i=0;
	int var=-1;
	for ( metadata c : metaList ) {System.out.println(i+". "+c);}
	
	Scanner kbReader = new Scanner(System.in);

	while(true){
				
				try{
					
				System.out.println("gib zahl ein");
				var = kbReader.nextInt() ;
				if(var<0||var>metaList.size()-1){continue;}
				else{break;}
				
				}catch(Exception e){}
				String errStr = kbReader.next();
				
			}
		
	 meta= 	metaList.get(var);
	FileList = getFileList(meta);
	file= 	FileList.get(0);
	
}

private void dataSelect(){
	char i;
	int  lenght;
	save = webarchiv.api.getInputStream( meta, file);
	if (save!=null){
	text = save.getEncoding();
	lenght=text.length();
		for(int a=0;  a<=text.length();a++){
			i=text.charAt(a);
			if(i==32){counter++;}
			
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
	
private void upload(){
	Object answer;
	new webarchiv.api.WebarchiveClient.getOutputStream(meta, countFile);
	
}

private void editXML(){
	String name ="Words"; 
	String tagName;
	
	tagName = meta.addPrefixTo(name);
	webarchiv.api.meta.createElement(tagName);
	
	webarchiv.api.XmlEditor.addDataElement(webarchiv.api.meta.createDataElement(tagName));
	
	
}
	
	
	
}

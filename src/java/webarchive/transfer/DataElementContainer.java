package webarchive.transfer;

import java.io.Serializable;

import webarchive.xml.DataElement;
/**
 * 
 * @author ccwelich
 *
 */
public class DataElementContainer implements Serializable {
	private DataElement dataElement;
	private FileDescriptor xmlFile;
	public DataElementContainer(DataElement dataElement, FileDescriptor xmlFile) {
		super();
		this.dataElement = dataElement;
		this.xmlFile = xmlFile;
	}
	public DataElement getDataElement() {
		return dataElement;
	}
	public FileDescriptor getXmlFile() {
		return xmlFile;
	}
	
}

#!/usr/bin/env python
# encoding: utf-8


XSD_TEMPLATE ="""
<?xml version="1.0"?>
<xs:schema version="1.0"
           xmlns:xs="http://www.w3.org/2001/XMLSchema"
           xmlns="http://www.hof-university.de/webarchive"
           targetNamespace="http://www.hof-university.de/webarchive"
  elementFormDefault="qualified"
           attributeFormDefault="qualified"
>
    <xs:element name="file">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="meta">
                    <xs:complexType>
                        <xs:sequence>
                            <xs:element name="commitTag">
                                <xs:complexType>
                                    <xs:attribute name="commitTime" type="xs:dateTime" use="required" />
                                    <xs:attribute name="domain" type="xs:string" use="required" />
                                </xs:complexType>
                            </xs:element>
                        </xs:sequence>
                        <xs:attribute name="url" type="xs:anyURI" use="required"/>
                        <xs:attribute name="mimeType" type="xs:string" use="required" />
                        <xs:attribute name="path" type="xs:anyURI" use="required" />
                        <xs:attribute name="createTime" type="xs:dateTime" use="required" />
                        <xs:attribute name="title" type="xs:string" use="optional" />
                    </xs:complexType>
                </xs:element>
                <xs:element name="data">
                    <xs:complexType>
                        <xs:sequence>
                            <xs:element ref="dataElement"  minOccurs="0" maxOccurs="unbounded"/>
                        </xs:sequence>
                    </xs:complexType>
                </xs:element>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
    <xs:element name="dataElement" abstract="true"/>
</xs:schema>
"""

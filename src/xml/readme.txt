xml tips:
validation:
	linux shell:
	command xmllint:
		"xmllint --noout --schema file.xsd example.xml"
		where --noout surpresses formatted xml output,
			--schema accept an xml-schema file
			last argument: xml list to validate
	NetBeans:
	press <Alt> + <Shift> + <F9>

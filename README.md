# XMLParser
A very simple java XMLParser in one single class!

# GOALS
Simple and flexible parser for fast parse XML data from file or web. Dont worry about schema, data models, deserializers and other stuff. No other dependencies is needed.

# EXAMPLES  
```java
String xml =
                "<import>" +
                    "<student id=1570900      course= TI test = HAI>" +
                        "<name>Noah Goldsmid</name>" +
                        "<username>s1570900</username>" +
                        "<password>awesome</password>" +
                    "</student>" +
                "</import>";
        XMLObject xmlObject;
        try {
            xmlObject = new XMLObject(xml);
            Map<String, String> attr = xmlObject.getChildNodes().get(0).getAttributes();
            for (String name : attr.keySet()) {
                System.out.println("Attribute in Student named " + name + " = " + attr.get(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
```

# CHANELOG (SINCE FORK)
* Improve parsing processor, more flexibly, less errors. 
* Silent Contructor without throwing exceptions.
* Support URL string with double-forwardslash --> **//**
* Support whitespace in attributes values

# ISSUE  
If you get any error while parsing file with this library, please create Issue topic, and paste used XML.

# XMLParser
A very simple java XMLParser in one single class!

#EXAMPLES
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

#ISSUE
If you get any error while parsing file with this library, please create Issue topic, and paste used XML.

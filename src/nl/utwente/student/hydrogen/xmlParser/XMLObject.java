package nl.utwente.student.hydrogen.xmlParser;


package pl.fpsystem.tmobile.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.Characters;

public class XMLObject {
	/**
	 * Create XMLObject without throwing any exceptions
	 * @param xml String contains xml data.
	 * @return XMLObject with parsed data.
	 */
	public static XMLObject createSilent(String xml){
		try {
			XMLObject simpleXml = new XMLObject(xml);
			return simpleXml;
		} catch (Exception e) {
			return new XMLObject();
		}
	}
	
	private List<XMLObject> children;
	private Map<String, String> attributes;
	private String name;
	private String value;

	/**
	 * Empty constructor, only for internal usage.
	 */
	private XMLObject(){
		
	}
	
	public XMLObject(String xml) throws Exception {
		xml = removeHeaderIfExist(xml);
		xml = xml.trim();
		children = new ArrayList<>();
		attributes = new HashMap<>();
		findName(xml);
		if (xml.charAt(0) != '<' || xml.charAt(xml.length() - 1) != '>') {
			System.err.println("error xml: " + xml);
			throw new Exception("Invalid XML. No matching opening/closing tags in: " + xml);
		}

		if (xml.charAt(name.length() + 1) == ' ') {
			// System.out.println("Attributes in " + name + "!");
			String attributeName = "";
			String attributeValue = "";
			for (int i = name.length() + 2; xml.charAt(i) != '>' && i < xml.length(); i++) {
				if (xml.charAt(i) == ' ')
					continue;
				if (xml.charAt(i) == '=') {
					int quoteCount = 0;
					int startPosition = i + 1;
					for (i = i + 1; quoteCount < 2 && isEndOfOpenElement(xml.charAt(i)) && i < xml.length(); i++) {
						if (xml.charAt(i) == '"') {
							quoteCount++;
						}
						
						/**
						 * Break reading value of attribute if string of value does not contains quotes, and space occurs after first letter.
						 */
						if(quoteCount == 0 && xml.charAt(i) == ' ' && i > startPosition && xml.charAt(i - 1) != xml.charAt(i)) break;
						attributeValue += xml.charAt(i);
					}
					attributes.put(attributeName.trim(), attributeValue.trim());
					attributeName = "";
					attributeValue = "";
				}
				attributeName += xml.charAt(i);
			}
		}

		if (xml.charAt(xml.length() - 2) == '/') {
			String check = xml.substring(1, xml.length() - 2);
			if (check.contains("<") || check.contains(">")) {
				throw new Exception("Invalid XML. Unexpected empty element close tag at: " + xml);
			}
		} else {
			value = xml.substring(2 + name.length(), xml.length() - 3 - name.length());
			int position = 0;
			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) == '<') {
					position = i++;
					String nodeName = "";

					boolean isNameWriter = true;
					for (; i < value.length(); i++) {
						if (value.charAt(i) == ' ' || value.charAt(i) == '<' )
							isNameWriter = false;
						else if (value.charAt(i) == '>' || (value.charAt(i) == '/' && isSpecialChar(i))) {
							break;
						}
						if (isNameWriter)
							nodeName += value.charAt(i);
					}
					if (value.charAt(i) == '/' && isSpecialChar(i)) { //shortest element 
						children.add(new XMLObject(value.substring(position, i+2)));
					} else {
						String matchFilter = "</" + nodeName + ">"; //normal element
						int endPos = value.substring(position).indexOf(matchFilter) + matchFilter.length() + position;
						i = endPos;
						children.add(new XMLObject(value.substring(position, i--)));
					}
				}
			}
		}
		// System.out.println(name + " : " + value);
	}

	private boolean isSpecialChar(int position){
		if(value.charAt(position) == '/' && (position + 1 >= value.length() || value.charAt(position + 1) == '>')) return true;
		if(value.charAt(position) == '>') return true;
		if(value.charAt(position) == '"') return true;
		if(value.charAt(position) == '&') return true;
		if(value.charAt(position) == '\'') return true;
		if(value.charAt(position) == '<') return true;
		if(value.charAt(position) == '>') return true;
		
		return false;
		
	}
	
	/**
	 * Check that is special char in XML, close element declaration:  > or />
	 * @param charAt character to chceck
	 * @return true if is close of element
	 */
	private boolean isEndOfOpenElement(char charAt) {
		return charAt != '/' && charAt != '>';		
	}



	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public List<XMLObject> getChildNodes() {
		return children;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	private String removeHeaderIfExist(String xml) {
		if (xml.charAt(0) == '<' && xml.charAt(1) == '?') {
			xml = xml.substring(xml.indexOf("?>") + 2);
		}
		
		return xml;
	}
	
	private void findName(String xml) {
		name = "";
		for (int i = 1; i < xml.length(); i++) {
			if (xml.charAt(i) == ' ' || xml.charAt(i) == '>' || xml.charAt(i) == '/') {
				return;
			}
			name += xml.charAt(i);
		}
	}

	@Override
	public String toString() {
		return "XMLObject [" + (children != null ? "children=" + children + ", " : "")
				+ (attributes != null ? "attributes=" + attributes + ", " : "")
				+ (name != null ? "name=" + name + ", " : "") + (value != null ? "value=" + value : "") + "]";
	}

	
}

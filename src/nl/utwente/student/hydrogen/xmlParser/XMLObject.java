package nl.utwente.student.hydrogen.xmlParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLObject {
	private List<XMLObject> children;
	private Map<String, String> attributes;
	private String name;
	private String value;

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
					for (i = i + 1; quoteCount < 2 && isEndOfOpenElement(xml.charAt(i)) && i < xml.length(); i++) {
						if (xml.charAt(i) == '"') {
							quoteCount++;
						}
						attributeValue += xml.charAt(i);
					}
					attributes.put(attributeName.trim(), attributeValue);
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
						else if (value.charAt(i) == '>' || value.charAt(i) == '/') {
							break;
						}
						if (isNameWriter)
							nodeName += value.charAt(i);
					}
					if (value.charAt(i) == '/') {
						children.add(new XMLObject(value.substring(position, i+2)));
					} else {
						String matchFilter = "</" + nodeName + ">";
						int endPos = value.substring(position).indexOf(matchFilter) + matchFilter.length() + position;
						i = endPos;
						children.add(new XMLObject(value.substring(position, i--)));
					}
				}
			}
		}
		// System.out.println(name + " : " + value);
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

}

package com.chessmagister.io;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;

public class CMXmlIo
{
	public static void readFromXml(CMSerializableComplex destination, String filePath) throws Exception
	{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new File(filePath));
		doc.getDocumentElement().normalize ();
		NodeList rootElement = doc.getElementsByTagName(destination.getID());
		
		if(rootElement == null || rootElement.getLength() == 0)
		{
			throw new SerializationException("No root element in XML: " + destination.getID());
		}
		else if(rootElement.getLength() > 1)
		{
			throw new SerializationException("Corrupted format - multiple root nodes: " + destination.getID());
		}
		
		readComplexNode(destination, rootElement.item(0));
	}

	protected static void readComplexNode(CMSerializableComplex destination, Node nodeToRead) throws Exception
	{
		if(nodeToRead.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new SerializationException("XMLNode is not an element: " + nodeToRead.getLocalName());
		}

		Element element = (Element)nodeToRead;

		Field objFields[] = destination.getClass().getFields();
		for(Field field : objFields)
		{
			Annotation annotation = null;
			field.setAccessible(true);

			if(field.getAnnotations() == null || field.getAnnotations().length == 0)
			{
				continue;
			}

			if((annotation = field.getAnnotation(CMXmlNode.class)) != null)
			{
				// Simple XML Node found
				CMSerializableData subObj = (CMSerializableData)field.get(destination);
				List<Node> nodes = getChildNodesById(element, (subObj.getID()));
				if(nodes.size() == 0)
				{
					throw new SerializationException("No element found in XML: " + subObj.getID());
				}
				else if(nodes.size() > 1)
				{
					throw new SerializationException("Corrupted format - multiple ambigious nodes: " + subObj.getID());
				}

				// Very important to set value to field again - for primitive types parameter
				// is not changed
				field.set(destination, readNode(subObj, nodes.get(0)));
			}
			else if((annotation = field.getAnnotation(CMXmlComplexNode.class)) != null)
			{
				// Complex XML Node found
				CMSerializableComplex subObj = (CMSerializableComplex)field.get(destination);
				List<Node> nodes = getChildNodesById(element, (subObj.getID()));
				if(nodes.size() == 0)
				{
					throw new SerializationException("No element found in XML: " + subObj.getID());
				}
				else if(nodes.size() > 1)
				{
					throw new SerializationException("Corrupted format - multiple ambigious nodes: " + subObj.getID());
				}

				readComplexNode(subObj, nodes.get(0));
			}
			else if((annotation = field.getAnnotation(CMXmlMapNode.class)) != null)
			{
				// Map XML Node found
				List<Node> nodes = getChildNodesById(element, CMXmlConstants.MAP_ID);
				if(nodes.size() == 0)
				{
					throw new SerializationException("No map element found in XML");
				}

				Map subObj = (Map)field.get(destination);
				
				for(int i = 0; i < nodes.size(); i++)
				{
					Node node = nodes.get(i);
					if(node.getNodeType() != Node.ELEMENT_NODE)
					{
						throw new SerializationException("XMLNode is not an element: " + node.getLocalName());
					}
					
					Element mapElement = (Element)node;
					String mapName = mapElement.getAttribute(CMXmlConstants.MAP_NAME_PROPERTY_ID);
					
					if(mapName == null)
					{
						throw new SerializationException("Map name cannot be null: " + node.getLocalName());
					}

					if(mapName.equals(((CMXmlMapNode)annotation).id()))
					{
						readMapElement(subObj, mapElement, (CMXmlMapNode)annotation);
					}
				}
			}
			else if((annotation = field.getAnnotation(CMXmlListNode.class)) != null)
			{
				// List XML Node found
				List<Node> nodes = getChildNodesById(element, CMXmlConstants.LIST_ID);
				if(nodes.size() == 0)
				{
					throw new SerializationException("No list element found in XML");
				}

				List subObj = (List)field.get(destination);

				for(int i = 0; i < nodes.size(); i++)
				{
					Node node = nodes.get(i);
					if(node.getNodeType() != Node.ELEMENT_NODE)
					{
						throw new SerializationException("XMLNode is not an element: " + node.getLocalName());
					}

					Element listElement = (Element)node;
					String listName = listElement.getAttribute(CMXmlConstants.LIST_NAME_PROPERTY_ID);
					
					if(listName == null)
					{
						throw new SerializationException("List name cannot be null: " + node.getLocalName());
					}

					if(listName.equals(((CMXmlListNode)annotation).id()))
					{
						readListElement(subObj, listElement, (CMXmlListNode)annotation);
					}
				}
			}
		}

		destination.fireUpdatedEvent();
	}

	protected static void readListElement(List destinationList, Element listElement, CMXmlListNode annotation) throws Exception
	{
		destinationList.clear();

		List<Node> elements = getChildNodesById(listElement, annotation.elementId());
		
		for(int i = 0; i < elements.size(); i++)
		{
			Node listNode = elements.get(i);
			if(listNode.getNodeType() != Node.ELEMENT_NODE)
			{
				throw new SerializationException("list element Node is not an element: " + listNode.getLocalName());
			}
			
			Element listItem = (Element)listNode;
			
			String itemClassString = listItem.getAttribute(CMXmlConstants.LIST_CLASS_PROPERTY_ID);

			if(itemClassString == null || itemClassString.isEmpty())
			{
				throw new SerializationException("List key or value has no class specified: " + listNode.getLocalName());
			}
			
			Object item = Class.forName(itemClassString).newInstance();

			if(item instanceof CMSerializableData)
			{
				// Assign value to item, to save data in primitives
				item = readNode((CMSerializableData)item, listNode);
			}
			else if(item instanceof CMSerializableComplex)
			{
				readComplexNode((CMSerializableComplex)item, listNode);
			}

			destinationList.add(item);
		}
	}
	
	protected static void readMapElement(Map destinationMap, Element mapElement, CMXmlMapNode annotation) throws Exception
	{
		destinationMap.clear();
		NodeList keyElements = mapElement.getElementsByTagName(CMXmlConstants.MAP_KEY_ID);
		NodeList objElements = mapElement.getElementsByTagName(CMXmlConstants.MAP_OBJ_ID);

		if(keyElements == null || objElements == null)
		{
			return;
		}
		
		if(keyElements.getLength() != objElements.getLength())
		{
			throw new SerializationException("Map corrupted, number of keys not equal to num of values: " + mapElement.getLocalName());
		}
		
		for(int i = 0; i < keyElements.getLength(); i++)
		{
			Node keyNode = keyElements.item(i);
			if(keyNode.getNodeType() != Node.ELEMENT_NODE)
			{
				throw new SerializationException("Key Node is not an element: " + keyNode.getLocalName());
			}
			
			Node objNode = objElements.item(i);
			if(objNode.getNodeType() != Node.ELEMENT_NODE)
			{
				throw new SerializationException("Map value is not an element: " + objNode.getLocalName());
			}
			
			Element keyElement = (Element)keyNode;
			Element objElement = (Element)objNode;
			String keyClassString = keyElement.getAttribute(CMXmlConstants.MAP_KEY_CLASS_PROPERTY_ID);
			String objClassString = objElement.getAttribute(CMXmlConstants.MAP_OBJ_CLASS_PROPERTY_ID);

			if(keyClassString == null || objClassString == null || keyClassString.isEmpty() || objClassString.isEmpty())
			{
				throw new SerializationException("Map key or value has no class specified: " + objNode.getLocalName());
			}

			Object key = Class.forName(keyClassString).newInstance();
			Object obj = Class.forName(objClassString).newInstance();

			if(key instanceof CMSerializableData)
			{
				CMSerializableData keyData = (CMSerializableData)key;
				// Get Key sub node
				List<Node> keySubNodes = getChildNodesById(keyNode, keyData.getID());
				if(keySubNodes.size() == 0 || keySubNodes.size() > 1)
				{
					throw new SerializationException("Map key must have 1 child: " + keyNode.getLocalName());
				}
				// Assign value back to keyData, to handle primitives properly
				keyData = readNode(keyData, keySubNodes.get(0));
			}
			else if(key instanceof CMSerializableComplex)
			{
				CMSerializableComplex keyData = (CMSerializableComplex)key;
				// Get Key sub node
				List<Node> keySubNodes = getChildNodesById(keyNode, keyData.getID());
				if(keySubNodes.size() == 0 || keySubNodes.size() > 1)
				{
					throw new SerializationException("Map key must have 1 child: " + keyNode.getLocalName());
				}
				readComplexNode((CMSerializableComplex)key, keySubNodes.get(0));
			}
			
			if(obj instanceof CMSerializableData)
			{
				CMSerializableData objData = (CMSerializableData)obj;
				// Get Key sub node
				List<Node> objSubNodes = getChildNodesById(objNode, objData.getID());
				if(objSubNodes.size() == 0 || objSubNodes.size() > 1)
				{
					throw new SerializationException("Map obj must have 1 child: " + objNode.getLocalName());
				}
				// Assign value back to object, to handle primitives properly
				obj = readNode((CMSerializableData)obj, objSubNodes.get(0));
			}
			else if(obj instanceof CMSerializableComplex)
			{
				CMSerializableComplex objData = (CMSerializableComplex)obj;
				// Get Key sub node
				List<Node> objSubNodes = getChildNodesById(objNode, objData.getID());
				if(objSubNodes.size() == 0 || objSubNodes.size() > 1)
				{
					throw new SerializationException("Map obj must have 1 child: " + objNode.getLocalName());
				}
				readComplexNode((CMSerializableComplex)obj, objSubNodes.get(0));
			}

			destinationMap.put(key, obj);
		}
	}
	
	protected static CMSerializableData readNode(CMSerializableData destination, Node nodeToRead)
	{
		if(nodeToRead.getNodeType() != Node.ELEMENT_NODE)
		{
			throw new SerializationException("XMLNode is not an element: " + nodeToRead.getLocalName());
		}

		Element element = (Element)nodeToRead;
		if(destination instanceof CMSerializableTextData)
		{
			Node child = element.getFirstChild();
			((CMSerializableTextData)destination).setValue(child != null ? child.getNodeValue() : "");
		}
		else if(destination instanceof CMSerializableEnumData)
		{
			destination = ((CMSerializableEnumData)destination).valueFromString(element.getFirstChild().getNodeValue());
		}
		
		return destination;
	}

	public static void writeToXml(Object obj, String filePath) throws Exception
	{
		DocumentBuilderFactory documentBuilderFactory = 
		DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = 
		documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();

		Element rootElement = processComplexNodeObject(obj, document);
		document.appendChild(rootElement);
		
		DOMSource source = new DOMSource(document);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
		File outFile = new File(filePath);
		StreamResult result =  new StreamResult(outFile);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(source, result);
	}
	
	protected static Element processTextDataObject(Field field, Object complexNode, Document document) throws IllegalArgumentException, IllegalAccessException
	{
		CMSerializableData data = (CMSerializableData)field.get(complexNode);
		return processSerializableTextDataObject(data, document);
	}
	
	protected static Element processSerializableTextDataObject(CMSerializableData object, Document document) throws IllegalArgumentException, IllegalAccessException
	{
		Element dataElement = document.createElement(object.getID());
		dataElement.appendChild(document.createTextNode(object.getValue()));
		return dataElement;
	}
	
	protected static Element processComplexNodeObject(Object complexNode, Document document) throws Exception
	{
		CMSerializableComplex currentObj = (CMSerializableComplex)complexNode;
		currentObj.performSave();
		Element element = document.createElement(currentObj.getID());
		
		Field objFields[] = complexNode.getClass().getFields();
		for(Field field : objFields)
		{
			Annotation annotation = null;

			if(field.getAnnotations() == null || field.getAnnotations().length == 0)
			{
				continue;
			}

			field.setAccessible(true);
			
			if((annotation = field.getAnnotation(CMXmlNode.class)) != null)
			{
				// Simple XML Node found
				element.appendChild(processTextDataObject(field, complexNode, document));
			}
			else if((annotation = field.getAnnotation(CMXmlComplexNode.class)) != null)
			{
				// Complex XML Node found
				element.appendChild(processComplexNodeObject(field.get(complexNode), document));
			}
			else if((annotation = field.getAnnotation(CMXmlMapNode.class)) != null)
			{
				Map mapField = (Map)field.get(complexNode);
				
//				if(mapField.size() == 0)
//				{
//					continue;
//				}
				
				Element mapElement = document.createElement(CMXmlConstants.MAP_ID);
				mapElement.setAttribute(CMXmlConstants.MAP_NAME_PROPERTY_ID,
										((CMXmlMapNode)annotation).id());
				element.appendChild(mapElement);

				Set keys = mapField.keySet();
				for(Object key : keys)
				{
					Element keyElement = document.createElement(CMXmlConstants.MAP_KEY_ID);
					keyElement.setAttribute(CMXmlConstants.MAP_KEY_CLASS_PROPERTY_ID,
											key.getClass().getCanonicalName());
					mapElement.appendChild(keyElement);
					if(key instanceof CMSerializableData)
					{
						keyElement.appendChild(
								processSerializableTextDataObject(
										(CMSerializableData)key, document));
					}
					else if(key instanceof CMSerializableComplex)
					{
						keyElement.appendChild(processComplexNodeObject(key, document));
					}
					else
					{
						throw new SerializationException("Key is not serializable");
					}

					Element valueElement = document.createElement(CMXmlConstants.MAP_OBJ_ID);
					mapElement.appendChild(valueElement);
					Object value = mapField.get(key);
					valueElement.setAttribute(CMXmlConstants.MAP_OBJ_CLASS_PROPERTY_ID,
							value.getClass().getCanonicalName());

					if(value instanceof CMSerializableData)
					{
						keyElement.appendChild(
								processSerializableTextDataObject(
										(CMSerializableData)value, document));
					}
					else if(value instanceof CMSerializableComplex)
					{
						valueElement.appendChild(processComplexNodeObject(value, document));
					}
					else
					{
						throw new SerializationException("Value is not serializable");
					}
				}
			}
			else if((annotation = field.getAnnotation(CMXmlListNode.class)) != null)
			{
				List listField = (List)field.get(complexNode);
				
//				if(listField.size() == 0)
//				{
//					continue;
//				}
				
				Element listElement = document.createElement(CMXmlConstants.LIST_ID);
				listElement.setAttribute(CMXmlConstants.LIST_NAME_PROPERTY_ID,
						((CMXmlListNode)annotation).id());
				element.appendChild(listElement);

				for(Object object : listField)
				{
					if(object instanceof CMSerializableData)
					{
						Element childElement = processSerializableTextDataObject((CMSerializableData)object, document);
						childElement.setAttribute(	CMXmlConstants.LIST_CLASS_PROPERTY_ID,
													object.getClass().getCanonicalName());
						listElement.appendChild(childElement);
					}
					else if(object instanceof CMSerializableComplex)
					{
						Element childElement = processComplexNodeObject(object, document);
						childElement.setAttribute(	CMXmlConstants.LIST_CLASS_PROPERTY_ID,
													object.getClass().getCanonicalName());
						listElement.appendChild(childElement);
					}
					else
					{
						throw new SerializationException("Object in the list is not serializable");
					}
				}
			}
		}

		return element;
	}
	
	protected static List<Node> getChildNodesById(Node node, String id)
	{
		List<Node> result = new Vector<Node>();
		NodeList childNodes = node.getChildNodes();
		for(int i = 0; i < childNodes.getLength(); i++)
		{
			Node currentNode = childNodes.item(i);
			if(currentNode.getNodeName().equals(id))
			{
				result.add(currentNode);
			}
		}
		
		return result;
	}
}

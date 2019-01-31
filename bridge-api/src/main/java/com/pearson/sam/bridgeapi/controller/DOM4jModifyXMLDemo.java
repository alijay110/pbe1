package com.pearson.sam.bridgeapi.controller;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class DOM4jModifyXMLDemo {

   public static void main(String[] args) {
   
      try {
         File inputFile = new File("request.xml");
         SAXReader reader = new SAXReader();
         Document document = reader.read( inputFile );

         Element classElement = document.getRootElement();

         List<Node> nodes = document.selectNodes("/Request/Attributes[@Category = 'urn:oasis:names:tc:xacml:3.0:attribute-category:action']/Attribute[@AttributeId = 'urn:oasis:names:tc:xacml:1.0:action:action-id']" );
         
         for (Node node : nodes) {
            Element element = (Element)node;
            Iterator<Element> iterator = element.elementIterator("AttributeValue");

            while(iterator.hasNext()) {
               Element marksElement = (Element)iterator.next();
               marksElement.setText("800");
            }
         }

         // Pretty print the document to System.out
         OutputFormat format = OutputFormat.createPrettyPrint();
         XMLWriter writer;
         writer = new XMLWriter( System.out, format );
         writer.write( document );
      } catch (DocumentException e) {
         e.printStackTrace();
      } catch (UnsupportedEncodingException e) {         
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static void main1(String[] args) {
   
      try {
         File inputFile = new File("input.txt");
         SAXReader reader = new SAXReader();
         Document document = reader.read( inputFile );

         Element classElement = document.getRootElement();

         List<Node> nodes = document.selectNodes("/class/student[@rollno = '493']" );
         
         for (Node node : nodes) {
            Element element = (Element)node;
            Iterator<Element> iterator = element.elementIterator("marks");

            while(iterator.hasNext()) {
               Element marksElement = (Element)iterator.next();
               marksElement.setText("80");
            }
         }

         // Pretty print the document to System.out
         OutputFormat format = OutputFormat.createPrettyPrint();
         XMLWriter writer;
         writer = new XMLWriter( System.out, format );
         writer.write( document );
      } catch (DocumentException e) {
         e.printStackTrace();
      } catch (UnsupportedEncodingException e) {         
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
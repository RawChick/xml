/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package stax.filter;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import javax.xml.stream.*;
import javax.xml.stream.events.* ;
import java.util.Date;
import javax.xml.namespace.QName;

/**
 * MyNamespaceFilter sample is used to demonstrates the use
 * of STAX stream filter api's. This filter accepts only
 * StartElement events that belong to a particular namespace and filters out rest of
 * the events.
 */

public class MyNamespaceFilter implements javax.xml.stream.StreamFilter {
    
    private static String namespace = null;
    private static String filename = null;
    
    
    private static void printUsage() {
        System.out.println("usage: java -Djava.endorsed.dirs=<jaxp dist/lib directory> stax.filter.MyNamespaceFilter <namespace> <xmlfile>");
    }
    
    /**
     * 
     * @param args 
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
                
        if (args.length != 2) {
            printUsage() ;
        }
        
        namespace = args[0];
        filename = args[1];

        XMLInputFactory xmlif = null ;
        try{
            xmlif = XMLInputFactory.newInstance();
            xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,Boolean.TRUE);
            xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);
            xmlif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE , Boolean.TRUE);
            xmlif.setProperty(XMLInputFactory.IS_COALESCING , Boolean.TRUE);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println("XMLInputFactory: " + xmlif);
        System.out.println("namespace = "+ namespace);
        System.out.println("filename = "+ filename);
        
        
        
        try {
            
            FileInputStream fis = new FileInputStream(filename);
            
            XMLStreamReader xmlr = xmlif.createFilteredReader(xmlif.createXMLStreamReader(fis), new MyNamespaceFilter());
            
            int eventType = xmlr.getEventType();
            printEvent(xmlr, eventType);
            while(xmlr.hasNext()) {
                eventType = xmlr.next();
                printEvent(xmlr, eventType);
            }
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void printEvent(XMLStreamReader xmlr, int eventType) {

        System.out.println("<Event>");
        
        printEventType(eventType);
        printName(xmlr,eventType);
        printText(xmlr);
        if (xmlr.isStartElement()) {
            printAttributes(xmlr);
        }
        printPIData(xmlr);
                
        System.out.println("</Event>");
    }
    
    /**
     * @param eventType
     * @return
     */
    public final static String getEventTypeString(int eventType) {
        switch (eventType){
            case XMLEvent.START_ELEMENT:
                return "START_ELEMENT";
            case XMLEvent.END_ELEMENT:
                return "END_ELEMENT";
            case XMLEvent.PROCESSING_INSTRUCTION:
                return "PROCESSING_INSTRUCTION";
            case XMLEvent.CHARACTERS:
                return "CHARACTERS";
            case XMLEvent.COMMENT:
                return "COMMENT";
            case XMLEvent.START_DOCUMENT:
                return "START_DOCUMENT";
            case XMLEvent.END_DOCUMENT:
                return "END_DOCUMENT";
            case XMLEvent.ENTITY_REFERENCE:
                return "ENTITY_REFERENCE";
            case XMLEvent.ATTRIBUTE:
                return "ATTRIBUTE";
            case XMLEvent.DTD:
                return "DTD";
            case XMLEvent.CDATA:
                return "CDATA";
        }
        return "UNKNOWN_EVENT_TYPE";
    }
    
    private static void printEventType(int eventType) {
        System.out.println("\t" + getEventTypeString(eventType) + "(" + eventType + ")");
    }
    
    private static void printName(XMLStreamReader xmlr, int eventType) {
        
        if (xmlr.hasName()) {
            System.out.println("\tName: " + xmlr.getName());
        }
    }
    
    private static void printText(XMLStreamReader xmlr) {
        
        if (xmlr.hasText()) {
            System.out.println("\tText: " + xmlr.getText());
        }
    }
    
    private static void printPIData(XMLStreamReader xmlr){
        if (xmlr.getEventType() == XMLEvent.PROCESSING_INSTRUCTION){
            System.out.println(" PI target = " + xmlr.getPITarget() ) ;
            System.out.println(" PI Data = " + xmlr.getPIData() ) ;
        }
    }
    
    private static void printAttributes(XMLStreamReader xmlr) {
        
        if (xmlr.getAttributeCount() > 0) {
            
            int count = xmlr.getAttributeCount() ;
            for(int i = 0 ; i < count ; i++) {
                
                QName name = xmlr.getAttributeName(i) ;
                String namespace = xmlr.getAttributeNamespace(i) ;
                String  type = xmlr.getAttributeType(i) ;
                String prefix = xmlr.getAttributePrefix(i) ;
                String value = xmlr.getAttributeValue(i) ;
                
                System.out.println("\tAttribute: {" + namespace + "}:" + name.toString() + "(" + type + ")=" + value);
            }
        }
    }
    
    /**
     * Accept only StartElement events that belong to desired Namespace.
     * Filters out rest of the events.
     *
     * @param reader 
     * @return 
     */
    public boolean accept(XMLStreamReader reader) {

        boolean DEBUG = true;
        
        // only interested in START_ELEMENT Events
        if (!reader.isStartElement()) {
            
            if (DEBUG) {
                System.err.println("accept -> false: not START_ELEMENT");
            }

            return false;
        }
        
        // only interested in my desired Namespace
        String startElementNamespace = reader.getNamespaceURI();
        if (startElementNamespace == null) {
            
            if (DEBUG) {
                System.err.println("accept -> false: NamespaceURI == null");
            }
            
            return false;
        }
        if (!startElementNamespace.equals(namespace)) {

            if (DEBUG) {
                System.err.println("accept -> false: {" + startElementNamespace + "} != {" + namespace + "}");
            }

            return false;
        }
        
        // of interest
        if (DEBUG) {
            System.err.println("accept -> : true");
        }

        return true;
        
    }
}

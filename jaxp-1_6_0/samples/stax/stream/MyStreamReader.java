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

package stax.stream;

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

public class MyStreamReader {
    
    private static String filename = null;
    
    
    private static void printUsage() {
        System.out.println("usage: java -Djava.endorsed.dirs=<jaxp dist/lib directory> stax.stream.MyStreamReader <xmlfile>");
    }
    
    /**
     * 
     * @param args 
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
                
        if (args.length != 1) {
            printUsage() ;
        }
        
        filename = args[0];
        
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
        System.out.println("filename = "+ filename);
                
        
        XMLStreamReader xmlr = null;
        try {
            
            FileInputStream fis = new FileInputStream(filename);
            
            xmlr = xmlif.createXMLStreamReader(fis);
                        
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        for (int event = xmlr.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlr.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {
              String element = xmlr.getLocalName();
            }
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
}

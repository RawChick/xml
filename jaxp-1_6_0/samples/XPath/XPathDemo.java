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

//$Id: XPathDemo.java,v 1.2 2007-07-19 04:36:17 ofung Exp $

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XPathDemo {
    
    /**
     * <p>usage java XPathDemo &lt;XML file&gt; &lt;XPath Expression&gt;</p>
     *
     * <p>Apply XPath Express against XML file and
     * output resulting NodeList.</p>
     */
    public static void main(String[] args) {
        
        // must have exactly 2 args
        if (args.length != 2) {
            System.err.println("Usage:"
                    + "java XPathDemo <XML file>"
                    + " <XPath Expression>");
            System.exit(1);
        }
        
        String xmlFile = args[0];
        String xpathExpression = args[1];
        
        // create XPath
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        
        NamespaceContextImpl namespaceContextImpl = new NamespaceContextImpl();
        
        namespaceContextImpl.bindPrefixToNamespaceURI(
                XMLConstants.DEFAULT_NS_PREFIX,
                "http://schemas.xmlsoap.org/wsdl/");
        namespaceContextImpl.bindPrefixToNamespaceURI(
                "tns",
                "http://hello.org/wsdl");
        namespaceContextImpl.bindPrefixToNamespaceURI(
                "ns2",
                "http://hello.org/types");
        namespaceContextImpl.bindPrefixToNamespaceURI(
                "xsd",
                "http://www.w3.org/2001/XMLSchema");
        namespaceContextImpl.bindPrefixToNamespaceURI(
                "soap",
                "http://schemas.xmlsoap.org/wsdl/soap/");
        namespaceContextImpl.bindPrefixToNamespaceURI(
                "soap11-enc",
                "http://schemas.xmlsoap.org/soap/encoding/");
        namespaceContextImpl.bindPrefixToNamespaceURI(
                "xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        namespaceContextImpl.bindPrefixToNamespaceURI(
                "wsdl",
                "http://schemas.xmlsoap.org/wsdl/");
        
        xpath.setNamespaceContext(namespaceContextImpl);
        
        NamespaceContext namespaceContext = xpath.getNamespaceContext();
        String namespaceContextClassName = null;
        if (namespaceContext != null) {
            namespaceContextClassName = namespaceContext.getClass().getName();
        } else {
            namespaceContextClassName = "null";
        }
        System.out.println(
                "XPath.getNamespaceContext() = "
                + namespaceContextClassName);
        
        // SAX as data model
        FileInputStream saxStream = null;
        try {
            saxStream = new FileInputStream(xmlFile);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            System.exit(1);
        }
        
        NodeList saxNodeList = null;
        try {
            saxNodeList = (NodeList)xpath.evaluate(xpathExpression,
                    new InputSource(saxStream),
                    XPathConstants.NODESET);
        } catch (XPathExpressionException xpathExpressionException) {
            xpathExpressionException.printStackTrace();
            System.exit(1);
        }
        
        dumpNode("SAX via InputSource", xmlFile, xpathExpression, saxNodeList);
    }
    
    static void dumpNode(String objectModel,
            String inputFile,
            String xpathExpression,
            NodeList nodeList) {
        
        System.out.println("Object model: " + objectModel + "created from: " + inputFile + "\n"
                + "XPath expression: " + xpathExpression + "\n"
                + "NodeList.getLength(): " + nodeList.getLength());
        
        // dump each Node's info
        for (int onNode = 0; onNode < nodeList.getLength(); onNode++) {
            
            Node node = nodeList.item(onNode);
            String nodeName = node.getNodeName();
            String nodeValue = node.getNodeValue();
            if (nodeValue == null) {
                nodeValue = "null";
            }
            String namespaceURI = node.getNamespaceURI();
            if (namespaceURI == null) {
                namespaceURI = "null";
            }
            String namespacePrefix = node.getPrefix();
            if (namespacePrefix == null) {
                namespacePrefix = "null";
            }
            String localName = node.getLocalName();
            if (localName == null) {
                localName = "null";
            }
            
            System.out.println("result #: " + onNode + "\n"
                    + "\tNode name: " + nodeName + "\n"
                    + "\tNode value: " + nodeValue + "\n"
                    + "\tNamespace URI: " + namespaceURI + "\n"
                    + "\tNamespace prefix: " + namespacePrefix + "\n"
                    + "\tLocal name: " + localName);
        }
        // dump each Node's info
    }
}

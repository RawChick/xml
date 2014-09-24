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

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Crimson" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, Sun Microsystems, Inc., 
 * http://www.sun.com.  For more information on the Apache Software 
 * Foundation, please see <http://www.apache.org/>.
 */

// JAXP packages
package ivh5;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.*;
import org.w3c.dom.*;

import java.io.*;

/**
 */
public class DomInIVH5 {

	/** All output will use this encoding */
	static final String outputEncoding = "UTF-8";

	/** Output goes here */
	private PrintWriter out;

	/** Indent level */
	private int indent = 0;

	/** Indentation will be in multiples of basicIndent */
	private final String basicIndent = "  ";

	/** Constants used for JAXP 1.2 */
	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String xmlFile = "resources/library.xml";
		String schemaFile = "resources/library.xsd";

		// Print out the DOM tree
		OutputStreamWriter outWriter = new OutputStreamWriter(System.out, outputEncoding);
		DomInIVH5 dom = new DomInIVH5(new PrintWriter(outWriter, true));

		// Get the xml file into the DOM document
		Document document = dom.getDocument(xmlFile, schemaFile);
		if (document == null) {
			System.out.println("Validation did not succeed.");
		} else {
			
			// Add a new element to the DOM
			String[] elementToAdd = new String[] { "Marieke", "Smits", "Middenweg", "4", "Breda" };
			dom.addMember(elementToAdd, document);

			// Transform DOM to output in console
			StreamResult sr = new StreamResult(System.out);
			dom.transform(document, sr);

			// Transform DOM to output to file
			StreamResult sr2 = new StreamResult(new File("resources/output.xml"));
			dom.transform(document, sr2);
		}

	}

	/**
	 * 
	 * @param out
	 */
	public DomInIVH5(PrintWriter out) {
		this.out = out;
	}

	/**
	 * 
	 * @param filename
	 * @param xmlSchema
	 * @return
	 */
	private Document getDocument(String filename, String xmlSchema) {

		boolean dtdValidate = false;
		boolean xsdValidate = false;

		boolean ignoreWhitespace = false;
		boolean ignoreComments = false;
		boolean putCDATAIntoText = false;
		boolean createEntityRefs = false;

		xsdValidate = true;
		ignoreWhitespace = true;
		ignoreComments = true;
		putCDATAIntoText = true;
		createEntityRefs = true;

		// Step 1: create a DocumentBuilderFactory and configure it
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// Set namespaceAware to true to get a DOM Level 2 tree with nodes
		// containing namespace information. This is necessary because the
		// default value from JAXP 1.0 was defined to be false.
		dbf.setNamespaceAware(true);

		// Set the validation mode to either: no validation, DTD
		// validation, or XSD validation
		dbf.setValidating(dtdValidate || xsdValidate);
		if (xsdValidate) {
			try {
				dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
			} catch (IllegalArgumentException x) {
				// This can happen if the parser does not support JAXP 1.2
				System.err
						.println("Error: JAXP DocumentBuilderFactory attribute not recognized: "
								+ JAXP_SCHEMA_LANGUAGE);
				System.err
						.println("Check to see if parser conforms to JAXP 1.2 spec.");
				System.exit(1);
			}
		}

		// Set the schema source, if any. See the JAXP 1.2 maintenance
		// update specification for more complex usages of this feature.
		if (xmlSchema != null) {
			dbf.setAttribute(JAXP_SCHEMA_SOURCE, new File(xmlSchema));
		}

		// Optional: set various configuration options
		dbf.setIgnoringComments(ignoreComments);
		dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
		dbf.setCoalescing(putCDATAIntoText);
		// The opposite of creating entity ref nodes is expanding them inline
		dbf.setExpandEntityReferences(!createEntityRefs);

		// Step 2: create a DocumentBuilder that satisfies the constraints
		// specified by the DocumentBuilderFactory
		DocumentBuilder db;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();

			// Set an ErrorHandler before parsing
			OutputStreamWriter errorWriter = new OutputStreamWriter(System.err,
					outputEncoding);
			db.setErrorHandler(new MyErrorHandler(new PrintWriter(errorWriter,
					true)));

			// Step 3: parse the input file. Parsing also validates it.
			doc = db.parse(new File(filename));

		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}

		return doc;

	}

	/**
	 * <member membershipNumber="nummer"> <firstname>Text</firstname>
	 * <lastname>Text</lastname> <street>Text</street>
	 * <housenumber>integer</housenumber> <city>Text</city>
	 * <phoneNumber>number</phoneNumber>
	 * <emailaddress>mailaddress</emailaddress> <fine>double(5, 2)</fine>
	 * <loans> <loan copyid="integer" lendingperiod="integer">
	 * <isbn>number</isbn> </loan> <loan copyid="integer"
	 * lendingperiod="integer"> <isbn>number</isbn> </loan> </loans> </member>
	 * 
	 * @param element
	 * @param document
	 */
	private void addMember(String[] element, Document document) {

		try {
			// Get the members element by tag name
			Node members = document.getElementsByTagName("members").item(0);
			if(members == null) {
				System.out.println("Members is null");
			} else {

				Element member = document.createElement("member");
				member.setAttribute("membershipNumber", "2000");
				members.appendChild(member);
	
				Element firstname = document.createElement("firstname");
				firstname.setTextContent(element[0]);
				member.appendChild(firstname);
	
				Element lastname = document.createElement("lastname");
				lastname.setTextContent(element[1]);
				member.appendChild(lastname);
	
				Element street = document.createElement("street");
				street.setTextContent(element[2]);
				member.appendChild(street);
	
				Element housenumber = document.createElement("housenumber");
				housenumber.setTextContent(element[3]);
				member.appendChild(housenumber);
	
				Element city = document.createElement("city");
				city.setTextContent(element[4]);
				member.appendChild(city);
	
				Element phonenumber = document.createElement("phoneNumber");
				phonenumber.setTextContent("0000000000");
				member.appendChild(phonenumber);
	
				Element emailaddress = document.createElement("emailaddress");
				emailaddress.setTextContent("test@test.nl");
				member.appendChild(emailaddress);
	
				Element fine = document.createElement("fine");
				fine.setTextContent("0.00");
				member.appendChild(fine);

				Element loans = document.createElement("loans");
				member.appendChild(loans);
			}

		} catch (NullPointerException e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
			// Here we exit the program; a better solution is to handle the
			// error.
			System.exit(1);
		} catch (DOMException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @param document
	 */
	private void transform(Document document, StreamResult result) {

		try {
			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();

			/**
			 * You can specify a variety of output properties for transformer
			 * objects, as defined in the W3C specification at
			 * http://www.w3.org/TR/xslt#output. For example, to get indented
			 * output, you can invoke the following method:
			 */
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);

		} catch (TransformerConfigurationException tce) {
			System.out.println("* Transformer Factory error");
			System.out.println(" " + tce.getMessage());

			Throwable x = tce;
			if (tce.getException() != null)
				x = tce.getException();
			x.printStackTrace();
		} catch (TransformerException te) {
			System.out.println("* Transformation error");
			System.out.println(" " + te.getMessage());

			Throwable x = te;
			if (te.getException() != null)
				x = te.getException();
			x.printStackTrace();
		}

	}

	// Error handler to report errors and warnings
	private static class MyErrorHandler implements ErrorHandler {
		/** Error handler output goes here */
		private PrintWriter out;

		MyErrorHandler(PrintWriter out) {
			this.out = out;
		}

		/**
		 * Returns a string describing parse exception details
		 */
		private String getParseExceptionInfo(SAXParseException spe) {
			String systemId = spe.getSystemId();
			if (systemId == null) {
				systemId = "null";
			}
			String info = "\n - URI = " + systemId + " (line "
					+ spe.getLineNumber() + ")\n - " + spe.getMessage();
			return info;
		}

		// The following methods are standard SAX ErrorHandler methods.
		// See SAX documentation for more info.

		public void warning(SAXParseException spe) throws SAXException {
			out.println("Warning: " + getParseExceptionInfo(spe));
		}

		public void error(SAXParseException spe) throws SAXException {
			String message = "Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}

		public void fatalError(SAXParseException spe) throws SAXException {
			String message = "Fatal Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}
	}
}

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

package validation;

import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
/**
 * SchemaValidator demonstrates the use of jaxp validation apis. 
 *
 */
public class SchemaValidator {
    
    protected static class Handler extends DefaultHandler {
        
        /**
         *
         * @param sAXParseException
         * @throws SAXException
         */
        public void error(SAXParseException sAXParseException) throws SAXException {
            System.out.println(sAXParseException);
        }
        
        /**
         *
         * @param sAXParseException
         * @throws SAXException
         */
        public void fatalError(SAXParseException sAXParseException) throws SAXException {
            System.out.println(sAXParseException);
        }
        
        /**
         *
         * @param sAXParseException
         * @throws SAXException
         */
        public void warning(org.xml.sax.SAXParseException sAXParseException) throws org.xml.sax.SAXException {
            System.out.println(sAXParseException);
        }
        
    }
    
    protected static class Resolver implements LSResourceResolver{
        
        /**
         *
         * @param str
         * @param str1
         * @param str2
         * @param str3
         * @param str4
         * @return
         */
        public org.w3c.dom.ls.LSInput resolveResource(String str, String str1, String str2, String str3, String str4) {
            //System.out.println("Resolving : "+str+ ":"+str1+":"+str2+":"+str3+":"+str4);
            return null;
            
        }
        
    }
    
    /**
     *
     * @param args
     */
    public static void main(String [] args) {
        try {
            if(args.length <2){
                printUsage();
                return;
            }
            
            Handler handler = new Handler();
            
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            schemaFactory.setErrorHandler(handler);
            //create a grammar object.
            Schema schemaGrammar = schemaFactory.newSchema(new File(args[0]));
            
            System.out.println("Created Grammar object for schema : "+args[0]);
            
            Resolver resolver = new Resolver();
            //create a validator to validate against grammar sch.
            Validator schemaValidator = schemaGrammar.newValidator();
            schemaValidator.setResourceResolver(resolver);
            schemaValidator.setErrorHandler(handler);
            
            System.out.println("Validating "+args[1] +" against grammar "+args[0]);
            //validate xml instance against the grammar.
            schemaValidator.validate(new StreamSource(args[1]));
            
        } catch (Exception e) {
            e.printStackTrace();
            exit(1, "Fatal Error: " + e);
        }
    }
    
    /**
     *
     * @param errCode
     * @param msg
     */
    public static void exit(int errCode, String msg) {
        System.out.println(msg);
    }
    
    public static void printUsage(){
        System.out.println("Usage : validation.SchemaValidator <schemaFile>  <xmlFile>");
    }
}

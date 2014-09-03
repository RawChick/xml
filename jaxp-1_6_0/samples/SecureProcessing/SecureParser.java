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

package SecureProcessing;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/*
 * SecureParser demonstrates the use of SecureProcessing feature in JAXP 1.3.
 * Secureprocessing feature was in added in JAXP 1.3 to prevent denial of service attack.
 */

public class SecureParser {
    
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String [] args){
        try{
            if(args.length < 2){
                printUsage();
                return;
            }
            //Set the limit for parser specific properties.
            //for more information on parser specific properties
            //refer to jaxp1.3 release notes.
            
            System.setProperty("elementAttributeLimit", "1");
            System.setProperty("entityExpansionLimit", "2");
            
            int index = args[0].indexOf("=");
            if(index <=0 ){
                printUsage();
                return;
            }
            String value = args[0].substring(index+1);
            boolean enableSecureProcessing = false;
            if(value != null){
                enableSecureProcessing = Boolean.valueOf(value).booleanValue();
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //enable or disable secureprocessing feature provided by jaxp1.3
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,enableSecureProcessing);
            
            DocumentBuilder parser =  dbf.newDocumentBuilder();
            parser.parse(args[1]);
            
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    public static void printUsage(){
        System.out.println("Usage :java  SecureProcessing.SecureParser -enableSecureProcessing=<true/false> <xmlFile>");
    }
}

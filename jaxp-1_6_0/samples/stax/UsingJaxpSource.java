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

package stax;

import java.io.File;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

/*
 * UsingJaxpSource.java
 *
 * Created on June 24, 2004, 11:38 AM
 */

/**
 *
 * @author  Neeraj Bajaj, Sun Microsystems
 */
public class UsingJaxpSource {
    
    /** Creates a new instance of UsingJaxpSource */
    public UsingJaxpSource() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try{
            // TODO code application logic here
            StreamSource stsource = new StreamSource(new FileInputStream("../data/city.xml"));
            XMLStreamReader streamReader =  factory.createXMLStreamReader(stsource);
            while(streamReader.hasNext()){
                System.out.println(streamReader.next());
            }
        }
        catch(java.io.FileNotFoundException fne){
            fne.printStackTrace();
        }
        catch(javax.xml.stream.XMLStreamException xse){
            xse.printStackTrace();
        }
        System.out.println("------------------------");
        System.out.println("XMLStreamReader finished");
        System.out.println("------------------------");
        
        try{
            StreamSource stsource = new StreamSource("../data/city.xml");
            XMLEventReader eventReader = factory.createXMLEventReader(stsource);
            while(eventReader.hasNext()){
                System.out.println(eventReader.nextEvent());
            }
        }
        catch(javax.xml.stream.XMLStreamException xse){
            xse.printStackTrace();
        }
     
        System.out.println("------------------------");
        System.out.println("XMLEventReader finished");
        System.out.println("------------------------");
        
        try{
            Source source = new SourceImpl();
            source.setSystemId("../data/city.xml");
            XMLEventReader eventReader = factory.createXMLEventReader(source);
            while(eventReader.hasNext()){
                System.out.println(eventReader.nextEvent());
            }
        }
        catch(javax.xml.stream.XMLStreamException xse){
            xse.printStackTrace();
        }
        catch(java.lang.UnsupportedOperationException uoe){
            System.out.println("Operation not supported.");
            System.out.println("uoe.getMessage() = " + uoe.getMessage());
        }

        System.out.println("------------------------");
        System.out.println("Testing XMLEventWriter ");
        System.out.println("------------------------");
        
        try{
            Result result = new ResultImpl();
            Source source = new SourceImpl();
            source.setSystemId("../data/city.xml");
            factory.setProperty("http://java.sun.com/xml/stream/properties/report-cdata-event", Boolean.TRUE);
            XMLEventReader eventReader = factory.createXMLEventReader(source);            
            result.setSystemId("../data/city output.xml");
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(result);
            eventWriter.add(eventReader);
            eventWriter.flush();
        }
        catch(javax.xml.stream.XMLStreamException xse){
            xse.printStackTrace();
        }
        catch(java.lang.UnsupportedOperationException uoe){
            System.out.println("Operation not supported.");
            System.out.println("uoe.getMessage() = " + uoe.getMessage());
        }
        
    }
    
    static class SourceImpl implements Source{
        public SourceImpl(){
            
        }

        String systemId ;
        public String getSystemId(){
            return systemId;
        }
        public void setSystemId(String id){
            systemId = id ;
        }
    }
    
    static class ResultImpl implements Result{
        
        String systemId ;
        public String getSystemId(){
            return systemId;
        }
        public void setSystemId(String id){
            systemId = id ;
        }
    }
    
}

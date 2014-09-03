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

package stax.cursor2event;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.util.XMLEventAllocator;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;

import com.sun.xml.internal.stream.events.XMLEventAllocatorImpl;

/**
 * StAX has two approach to parse XML. 
 * <li>
 *     <ul> Cursor : <code>XMLStreamReader</code> </ul>
 *     <ul> Event : <code>XMLEventReader</code> </ul>
 * </li>
 * 
 * <code>XMLStreamReader</code> returns the integer constant corresponding to particular event and 
 * application need to call relevant function to get information about that event. This is the most
 * efficient way to parse XML.
 *
 * <code>XMLEventReader</code> returns immutable and persistent event objects. 
 * All the information related to particular event is encapsulated in the returned <code>XMLEvent</code>
 * object. In this approach user need not worry about how to get relevant information corresponding to 
 * particular event,as in case of XMLStreamReader. This makes it pretty easy for the user to work with 
 * event approach. 
 * 
 * The disadvantage of event approach is the extra overhead of creating every event objects which
 * consumes both time and memory. However, It is possible to get event information as an <code>XMLEvent<code/> 
 * object even when using cursor approach ie. <code>XMLStreamReader</code> using <code>XMLEventAllocator</code>.
 * 
 * This class shows how application can get information as an <code>XMLEvent</code> object when using cursor 
 * approach. 
 * 
 *
 * @author Neeraj Bajaj, Sun Microsystems.
 *
 */

public class CursorApproachEventObject {
    
    
    static XMLEventAllocator allocator = null;
    
    private static void printUsage() {
        System.out.println("usage: java -Djava.endorsed.dirs=<jaxp dist/lib directory> stax.cursor2event.CursorApproachEventObject <xmlfile>");
    }
    
    public static void main(String[] args) throws Exception {
        
        String filename = null;
        
        try {
            filename = args[0];
        } catch (ArrayIndexOutOfBoundsException aioobe){
            printUsage();
            System.exit(0);
        } catch (Exception ex){
            printUsage();
            ex.printStackTrace() ;
        }
        
        try{
            XMLInputFactory xmlif = XMLInputFactory.newInstance();        
            System.out.println("FACTORY: " + xmlif);        
            xmlif.setEventAllocator(new XMLEventAllocatorImpl());
            allocator = xmlif.getEventAllocator();
            XMLStreamReader xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(filename));

            int eventType = xmlr.getEventType();
            while(xmlr.hasNext()){
                eventType = xmlr.next();
                //Get all "Book" elements as XMLEvent object
                if(eventType == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("Book")){
                    //get immutable XMLEvent
                    StartElement event = getXMLEvent(xmlr).asStartElement();                        
                    System.out.println("EVENT: " + event.toString());
                }
            }
        }catch(XMLStreamException ex){
            ex.printStackTrace();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
    }
    
    
    /** Get the immutable XMLEvent from given XMLStreamReader using XMLEventAllocator */
    private static XMLEvent getXMLEvent(XMLStreamReader reader) throws XMLStreamException{
        return allocator.allocate(reader);
    }    
    
}

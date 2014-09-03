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

package stax.readnwrite;

import java.util.Calendar;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

/*
 * Stax has writing APIs. <code>XMLEventWriter</code> extends from <code>XMLEventConsumer</code>
 * interface. So <code>XMLEventWriter</code> acts as a consumer which can consume events. 
 * Event producer <code>XMLEventReader</code> and consumer <code>XMLEventWriter</code> mechanism
 * makes it possible to read XML from one stream sequentially and simultaneously write to other stream. 
 *
 * This makes reading and writing very fast. This sample shows how StAX producer/consumer mechanism can be 
 * used to read and write simultaneously. This sample also shows that stream can be modified 
 * or new events can be added dynamically and then written to different stream using <code>XMLEventWriter</code> 
 * APIs.
 *
 * EventProducerConsumer.java
 *
 * 
 */

/**
 *
 * @author Neeraj Bajaj
 */
public class EventProducerConsumer {
    
    XMLEventFactory m_eventFactory = XMLEventFactory.newInstance();
    
    /** Creates a new instance of EventProducerConsumer */
    public EventProducerConsumer() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("Usage: Specify XML File Name");
        }
        try{
            EventProducerConsumer ms = new EventProducerConsumer();
            
            XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(new java.io.FileInputStream(args[0]));
            XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(System.out);

            while(reader.hasNext()){
                XMLEvent event = (XMLEvent)reader.next();
                //write this event to Consumer (XMLOutputStream)
                if(event.getEventType() == event.CHARACTERS){
                    //character events where text "Name1" is replaced with text output 
                    //of following function call Calendar.getInstance().getTime().toString()
                    writer.add(ms.getNewCharactersEvent(event.asCharacters()));
                }
                else{
                    writer.add(event);
                }
            }
            writer.flush();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    } 
        
    /** New Character event (with text containing current time) is created using XMLEventFactory in case the 
     *  Characters event passed matches the criteria.
     *
     *  @param Characters Current character event.
     *  return Characters New Characters event.
     */
    Characters getNewCharactersEvent(Characters event){
        if(event.getData().equalsIgnoreCase("Name1")){            
            return m_eventFactory.createCharacters(Calendar.getInstance().getTime().toString());
            
        }
        //else return the same event
        else return event;
    }
    
}

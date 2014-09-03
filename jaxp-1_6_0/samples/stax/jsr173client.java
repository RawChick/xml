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

import javax.xml.stream.*;
import javax.xml.stream.events.*;

import java.io.*;

public class jsr173client {

static String filename;

  public static void main(String[] args) throws Exception {
    
    filename = args[0];

    // create XMLInputFactory
    System.out.println("Getting XMLInputFactory...");
    XMLInputFactory xmlif = XMLInputFactory.newInstance();
    System.out.println("XMLInputFactory == " + xmlif );

    
    int state = -1;
    
System.out.println("Warming up for 100 parse ");

for(int i = 0 ; i < 100 ; i++) {

    //System.out.println("Getting XMLStreamReader...");
    XMLStreamReader xmlr = xmlif.createXMLStreamReader(new FileInputStream(filename));    
    //System.out.println("Got XMLStreamReader.");
        
    while(xmlr.hasNext()){
        state = xmlr.next();

    }

}

System.out.println("Finished warming for 100 parse ");

long timeBefore = System.currentTimeMillis();

for(int i = 0 ; i < 100 ; i++) {

    XMLStreamReader xmlr = xmlif.createXMLStreamReader(new FileInputStream(filename));    
        
    while(xmlr.hasNext()){

        state = xmlr.next();
    }
}
    
    long timeAfter = System.currentTimeMillis();
    System.out.println("Total time = " + (timeAfter - timeBefore) );
    System.out.println("Time per parse = " + (timeAfter - timeBefore) / 100 ); 
  
  }
  
  public final static String getEventTypeString(int eventType) {
    switch (eventType){
    case XMLEvent.START_DOCUMENT:
      return "START_DOCUMENT";
    case XMLEvent.END_DOCUMENT:
      return "END_DOCUMENT";
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

    }
    return "UNKNOWN_EVENT_TYPE";
  }

  private static void printEventType(int eventType) {
    System.out.print("EVENT TYPE("+eventType+"):");
    System.out.println(getEventTypeString(eventType));
  }

}
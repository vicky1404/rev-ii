package cl.mdr.ifrs.ejb.test;
/*
 *  Copyright 2007-2008, Plutext Pty Ltd.
 *
 *  This file is part of docx4j.
    docx4j is licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import javax.xml.bind.JAXBException;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.XmlUtils;
import org.docx4j.model.structure.DocumentModel;
import org.docx4j.model.structure.PageDimensions;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.CTRel;
import org.docx4j.wml.P;
import org.docx4j.wml.P.Hyperlink;
import org.docx4j.wml.SectPr;

/**
 * Master and Subdocuments.
 *
 * A subdocument is just an external part.
 *
 * So this sample is very similar to the hyperlink one.
 *
 * @author Jason Harrop
 * @version 1.0
 */
public class WordDocumentTest3 {
    
    public static void main(String[] args) throws Exception {
        System.out.println("Creating package..");
        WordprocessingMLPackage wordMLPackage = createPackage();
        wordMLPackage.save(new java.io.File("c:/doc1-landscape.docx"));                
        System.out.println("Done.");
    }
    
    public static List<MainDocumentPart> getParts(){
        List<MainDocumentPart> parts = new ArrayList<MainDocumentPart>();
        
        org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
        org.docx4j.wml.Body body = factory.createBody();
        org.docx4j.wml.Document wmlDocumentEl = factory.createDocument();
        wmlDocumentEl.setBody(body);
        PageDimensions page = new PageDimensions();
        page.setPgSize(PageSizePaper.A4, true);
        SectPr sectPr = factory.createSectPr();
        body.setSectPr(sectPr);
        sectPr.setPgSz(page.createPgSize());
        sectPr.setPgMar(page.createPgMar());
        return parts;
    }
    
    

    public static WordprocessingMLPackage createPackage() throws InvalidFormatException, JAXBException {
        // Create a package
        WordprocessingMLPackage wmlPack = WordprocessingMLPackage.createPackage();
        // Create main document part
        MainDocumentPart wordDocumentPart = new MainDocumentPart();
        
               
        org.docx4j.wml.Body body = Context.getWmlObjectFactory().createBody();
        org.docx4j.wml.Document wmlDocumentEl = Context.getWmlObjectFactory().createDocument();
        
        // Create a basic sectPr using our Page model
        PageDimensions page = new PageDimensions();
        page.setPgSize(PageSizePaper.A4, true);
        SectPr sectPr = Context.getWmlObjectFactory().createSectPr();        
        sectPr.setPgSz(page.createPgSize());
        sectPr.setPgMar(page.createPgMar());
        
        
        // Create a basic sectPr using our Page model
        PageDimensions page2 = new PageDimensions();
        page2.setPgSize(PageSizePaper.A4, false);
        SectPr sectPr2 = Context.getWmlObjectFactory().createSectPr();        
        sectPr2.setPgSz(page.createPgSize());
        sectPr2.setPgMar(page.createPgMar());
         

        wmlDocumentEl.setBody(body);
        // Put the content in the part
        wordDocumentPart.setJaxbElement(wmlDocumentEl);
    
        // Add the main document part to the package relationships
        // (creating it if necessary)
        wmlPack.addTargetPart(wordDocumentPart);  
                                
        // Return the new package
        return wmlPack;

    }

}


/*
 * Copyright 2016 MIR@MU Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.muni.fi.mir.mathmlunificator.utils;

import cz.muni.fi.mir.mathmlunificator.AbstractXMLTransformationTest;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Michal Růžička
 */
public class DOMBuilderTest extends AbstractXMLTransformationTest {

    private static final String testFile = "multiple-formulae";

    private Document doc = null;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.parse(getXMLTestResource(testFile));
    }

    @After
    public void tearDown() {
        doc = null;
    }

    @Test
    public void testBuildDocFromFilepath() throws Exception {
        Document testedDoc = DOMBuilder.buildDocFromFilepath(getXMLTestResourceAsFilepath(testFile));
        if (isMathMLElementsDOMEqual(doc, testedDoc)) {
            fail("Produced W3C DOM is not equivalent to the expected W3C DOM.");
        }
    }

    @Test
    public void testBuildDoc_String() throws Exception {
        Document testedDoc = DOMBuilder.buildDoc(IOUtils.toString(getXMLTestResource(testFile)));
        if (isMathMLElementsDOMEqual(doc, testedDoc)) {
            fail("Produced W3C DOM is not equivalent to the expected W3C DOM.");
        }
    }

    @Test
    public void testBuildDoc_File() throws Exception {
        Document testedDoc = DOMBuilder.buildDoc(new File(getXMLTestResourceAsFilepath(testFile)));
        if (isMathMLElementsDOMEqual(doc, testedDoc)) {
            fail("Produced W3C DOM is not equivalent to the expected W3C DOM.");
        }
    }

    @Test
    public void testBuildDoc_InputSource() throws Exception {
        Document testedDoc = DOMBuilder.buildDoc(new InputSource(getXMLTestResource(testFile)));
        if (isMathMLElementsDOMEqual(doc, testedDoc)) {
            fail("Produced W3C DOM is not equivalent to the expected W3C DOM.");
        }
    }

    @Test
    public void testBuildDoc_InputStream() throws Exception {
        Document testedDoc = DOMBuilder.buildDoc(getXMLTestResource(testFile));
        if (isMathMLElementsDOMEqual(doc, testedDoc)) {
            fail("Produced W3C DOM is not equivalent to the expected W3C DOM.");
        }
    }

}

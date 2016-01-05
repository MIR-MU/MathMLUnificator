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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.xml.parsers.ParserConfigurationException;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Michal Růžička
 */
public class XMLOutTest extends AbstractXMLTransformationTest {

    private static final String testFile = "multiple-formulae";

    @Before
    public void setUp() {
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Test
    public void testXmlSerializer() {
        try {
            Document inputDoc = DOMBuilder.buildDoc(getInputXMLTestResource(testFile));
            OutputStream os = new ByteArrayOutputStream();
            XMLOut.xmlSerializer(inputDoc, os);
            testXML(testFile, os.toString());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testXmlStdoutSerializer() {
        try {
            Document inputDoc = DOMBuilder.buildDoc(getInputXMLTestResource(testFile));
            ByteArrayOutputStream stdoutContent = new ByteArrayOutputStream();
            PrintStream stdout = System.out;
            System.setOut(new PrintStream(stdoutContent));
            XMLOut.xmlStdoutSerializer(inputDoc);
            System.setOut(stdout);
            testXML(testFile, stdoutContent.toString());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testXmlStringSerializer() {
        try {
            Document inputDoc = DOMBuilder.buildDoc(getInputXMLTestResource(testFile));
            String outputDoc = XMLOut.xmlStringSerializer(inputDoc);
            testXML(testFile, outputDoc);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            fail(ex.getMessage());
        }
    }

}

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
package cz.muni.fi.mir.mathmlunificator;

import static cz.muni.fi.mir.mathmlunificator.config.Constants.MATHML_NS;
import cz.muni.fi.mir.mathmlunificator.utils.XMLOut;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.*;
import org.junit.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Michal Růžička
 */
public class MathMLUnificatorTest extends AbstractXMLTransformationTest {

    @Test
    public void testUnifyMathML_Document() {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getInputXMLTestResource("multiple-formulae.document-unification"));
            Document expectedDoc = docBuilder.parse(getExpectedXMLTestResource("multiple-formulae.document-unification"));

            MathMLUnificator.unifyMathML(doc);

            System.out.println("testUnifyMathML_Document – output:\n" + XMLOut.xmlStringSerializer(doc));
            testXML(expectedDoc, doc);

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testUnifyMathML_InputStream_OutputStream() throws Exception {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document expectedDoc = docBuilder.parse(getExpectedXMLTestResource("multiple-formulae.document-unification"));

            ByteArrayOutputStream osDoc = new ByteArrayOutputStream();
            MathMLUnificator.unifyMathML(getInputXMLTestResource("multiple-formulae.document-unification"), osDoc);
            String doc = osDoc.toString("UTF-8");

            System.out.println("testUnifyMathML_InputStream_OutputStream – output:\n" + doc);
            testXML(expectedDoc, doc);

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    public void testUnifyMathMLNode() {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getInputXMLTestResource("single-formula.node-unification"));
            Document expectedDoc = docBuilder.parse(getExpectedXMLTestResource("single-formula.node-unification"));

            NodeList nodeList = doc.getElementsByTagNameNS(MATHML_NS, "mfrac");
            assertEquals(1, nodeList.getLength());

            Node node = nodeList.item(0);
            MathMLUnificator.unifyMathMLNode(node);

            System.out.println("testUnifyMathMLNode – output:\n" + XMLOut.xmlStringSerializer(doc));
            testXML(expectedDoc, doc);

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    public void testReplaceNodeWithUnificator_nonOperator() {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getInputXMLTestResource("single-formula.non-operator"));
            Document expectedDoc = docBuilder.parse(getExpectedXMLTestResource("single-formula.non-operator"));

            NodeList nodeList = doc.getElementsByTagNameNS(MATHML_NS, "msqrt");
            assertEquals(1, nodeList.getLength());

            Node node = nodeList.item(0);
            MathMLUnificator.replaceNodeWithUnificator(node);

            System.out.println("testReplaceNodeWithUnificator_nonOperator – output:\n" + XMLOut.xmlStringSerializer(doc));
            assertTrue(isDOMEqual(expectedDoc, doc));

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    public void testReplaceNodeWithUnificator_operator() {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getInputXMLTestResource("single-formula.operator"));
            Document expectedDoc = docBuilder.parse(getExpectedXMLTestResource("single-formula.operator"));

            NodeList nodeList = doc.getElementsByTagNameNS(MATHML_NS, "mo");
            assertEquals(1, nodeList.getLength());

            Node node = nodeList.item(0);
            MathMLUnificator.replaceNodeWithUnificator(node);

            System.out.println("testReplaceNodeWithUnificator_operator – output:\n" + XMLOut.xmlStringSerializer(doc));
            assertTrue(isDOMEqual(expectedDoc, doc));

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            fail(ex.getMessage());
        }

    }

}

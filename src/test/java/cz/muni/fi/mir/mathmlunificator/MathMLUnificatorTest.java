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
import cz.muni.fi.mir.mathmlunificator.utils.DOMBuilder;
import cz.muni.fi.mir.mathmlunificator.utils.XMLOut;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
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

            DocumentBuilder docBuilder = DOMBuilder.getDocumentBuilder();
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

            DocumentBuilder docBuilder = DOMBuilder.getDocumentBuilder();
            Document expectedDoc = docBuilder.parse(getExpectedXMLTestResource("multiple-formulae.document-unification"));

            ByteArrayOutputStream osDoc = new ByteArrayOutputStream();
            MathMLUnificator.unifyMathML(getInputXMLTestResource("multiple-formulae.document-unification"), osDoc);
            String doc = osDoc.toString();

            System.out.println("testUnifyMathML_InputStream_OutputStream – output:\n" + doc);
            testXML(expectedDoc, doc);

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    public void testUnifyMathMLNode() {
        try {

            DocumentBuilder docBuilder = DOMBuilder.getDocumentBuilder();
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
    public void testGetUnifiedMathMLNodes() {

        try {

            // Expected collection
            HashMap<Integer, Node> expectedNodeList = new HashMap<>(4);
            Document node1 = DOMBuilder.buildDoc(
                    "<math xmlns:uni=\"http://mir.fi.muni.cz/mathml-unification/\"\n"
                    + "    uni:unification-level=\"1\" uni:unification-max-level=\"4\" xmlns=\"http://www.w3.org/1998/Math/MathML\">\n"
                    + "    <msup>\n"
                    + "        <mi>a</mi>\n"
                    + "        <mn>2</mn>\n"
                    + "    </msup>\n"
                    + "    <mo>+</mo>\n"
                    + "    <mfrac>\n"
                    + "        <msqrt>\n"
                    + "            <mi>◍</mi>\n"
                    + "        </msqrt>\n"
                    + "        <mi>c</mi>\n"
                    + "    </mfrac>\n"
                    + "</math>");
            expectedNodeList.put(1, node1.getDocumentElement());
            Document node2 = DOMBuilder.buildDoc(
                    "<math xmlns:uni=\"http://mir.fi.muni.cz/mathml-unification/\"\n"
                    + "    uni:unification-level=\"2\" uni:unification-max-level=\"4\" xmlns=\"http://www.w3.org/1998/Math/MathML\">\n"
                    + "    <msup>\n"
                    + "        <mi>◍</mi>\n"
                    + "        <mi>◍</mi>\n"
                    + "    </msup>\n"
                    + "    <mo>+</mo>\n"
                    + "    <mfrac>\n"
                    + "        <mi>◍</mi>\n"
                    + "        <mi>◍</mi>\n"
                    + "    </mfrac>\n"
                    + "</math>");
            expectedNodeList.put(2, node2.getDocumentElement());
            Document node3 = DOMBuilder.buildDoc(
                    "<math xmlns:uni=\"http://mir.fi.muni.cz/mathml-unification/\"\n"
                    + "    uni:unification-level=\"3\" uni:unification-max-level=\"4\" xmlns=\"http://www.w3.org/1998/Math/MathML\">\n"
                    + "    <mi>◍</mi>\n"
                    + "    <mo>+</mo>\n"
                    + "    <mi>◍</mi>\n"
                    + "</math>");
            expectedNodeList.put(3, node3.getDocumentElement());
            Document node4 = DOMBuilder.buildDoc(
                    "<math xmlns:uni=\"http://mir.fi.muni.cz/mathml-unification/\"\n"
                    + "    uni:unification-level=\"4\" uni:unification-max-level=\"4\" xmlns=\"http://www.w3.org/1998/Math/MathML\">\n"
                    + "    <mi>◍</mi>\n"
                    + "    <mo>◍</mo>\n"
                    + "    <mi>◍</mi>\n"
                    + "</math>");
            expectedNodeList.put(4, node4.getDocumentElement());

            // Produced collection
            DocumentBuilder docBuilder = DOMBuilder.getDocumentBuilder();
            Document doc = docBuilder.parse(getInputXMLTestResource("single-formula.node-unification"));
            HashMap<Integer, Node> docNodeList = MathMLUnificator.getUnifiedMathMLNodes(doc.getDocumentElement());

            System.out.println("testGetUnifyMathMLNodes – output:");
            for (Node n : docNodeList.values()) {
                XMLOut.xmlStdoutSerializer(n.getOwnerDocument());
            }

            assertEquals(expectedNodeList.keySet(), docNodeList.keySet());
            for (Integer i : expectedNodeList.keySet()) {
                testXML("Different unification at level " + Integer.toString(i), expectedNodeList.get(i).getOwnerDocument(), docNodeList.get(i).getOwnerDocument());
            }
            //assertTrue(docNodeList.equals(expectedNodeList));

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    public void testReplaceNodeWithUnificator_nonOperator() {

        try {

            DocumentBuilder docBuilder = DOMBuilder.getDocumentBuilder();
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

            DocumentBuilder docBuilder = DOMBuilder.getDocumentBuilder();
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

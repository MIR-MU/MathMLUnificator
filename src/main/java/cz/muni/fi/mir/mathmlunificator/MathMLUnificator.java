/*
 * Copyright 2016 Michal Růžička.
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

import static cz.muni.fi.mir.mathmlunificator.config.Constants.*;
import cz.muni.fi.mir.mathmlunificator.utils.DOMBuilder;
import cz.muni.fi.mir.mathmlunificator.utils.XMLOut;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Michal Růžička
 */
public class MathMLUnificator {

    private static final int NUMOFMINORLEVELS = 2;
    private HashMap<NodeLevel<Integer, Integer>, List> nodesByDepth;

    public static void unifyMathML(Document doc) {

        List<Node> mathNodes = DocumentParser.findMathMLNodes(doc);
        for (Node mathNode : mathNodes) {
            MathMLUnificator.unifyMathMLNode(mathNode);
        }

    }

    public static void unifyMathML(InputStream inDoc, OutputStream outDoc) throws IOException {

        String originalDoc = IOUtils.toString(inDoc, "UTF-8");

        try {
            Document doc = DOMBuilder.buildDocFromFile(new ByteArrayInputStream(originalDoc.getBytes("UTF-8")));
            unifyMathML(doc);
            XMLOut.xmlSerializer(doc, outDoc);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(MathMLUnificator.class.getName()).log(Level.SEVERE, "MathML Unification from string failed, input string not modified.", ex);
            IOUtils.write(originalDoc, outDoc);
        }

    }

    public static void unifyMathMLNode(Node mathNode) {

        new MathMLUnificator().unifyMathMLNodeImpl(mathNode);

    }

    private void unifyMathMLNodeImpl(Node mathNode) {

        nodesByDepth = new HashMap<>();

        Node unifiedMathNode = mathNode.getOwnerDocument().createElementNS(UNIFIEDMATHNS, UNIFIEDMATHROOTELMNT);
        mathNode.getParentNode().replaceChild(unifiedMathNode, mathNode);

        unifiedMathNode.appendChild(mathNode.cloneNode(true));

        // Parse input file and remember elements by their depth.
        rememberLevelsOfNodes(mathNode);

        // Print unified document level by level.
        NodeLevel<Integer, Integer> level = new NodeLevel<>(getMaxMajorNodesLevel(), NUMOFMINORLEVELS);
        while (level.major > 0) {
            if (nodesByDepth.containsKey(level)) {
                if (unifyAtLevel(level)) {
                    unifiedMathNode.appendChild(mathNode.cloneNode(true));
                }
            }
            level.minor--;
            if (level.minor <= 0) {
                level.major--;
                level.minor = NUMOFMINORLEVELS;
            }
        }

    }

    private void rememberLevelsOfNodes(Node rootNode) {

        NodeList nodeList = rootNode.getChildNodes();

        rememberNodesStartingAtLevel(1, nodeList);

    }

    private void rememberNodesStartingAtLevel(int level, NodeList nodeList) {

        if (nodeList != null && nodeList.getLength() > 0) {

            NodeLevel<Integer, Integer> levelOperators = new NodeLevel<>(level, NUMOFMINORLEVELS - 1);
            NodeLevel<Integer, Integer> levelNonOperators = new NodeLevel<>(level, NUMOFMINORLEVELS);
            List<Node> nodesNonOperator = new ArrayList(nodeList.getLength());
            List<Node> nodesOperator = new ArrayList(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeName().equals(PMATHMLOPERATOR)) {
                    nodesOperator.add(node);
                } else {
                    nodesNonOperator.add(node);
                }
            }

            nodesByDepth.merge(levelNonOperators, nodesNonOperator, (List t, List u) -> {
                t.addAll(u);
                return t;
            });
            nodesByDepth.merge(levelOperators, nodesOperator, (List t, List u) -> {
                t.addAll(u);
                return t;
            });

            for (Node node : nodesNonOperator) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    rememberNodesStartingAtLevel(level + 1, node.getChildNodes());
                }
            }

        }

    }

    private boolean unifyAtLevel(NodeLevel<Integer, Integer> level) {

        boolean modified = false;

        if (level.major <= 0) {
            throw new IllegalArgumentException("Major level must be integer greather than 0.");
        } else if (level.minor <= 0) {
            throw new IllegalArgumentException("Minor level must be integer greather than 0.");
        } else if (nodesByDepth.containsKey(level)) {

            List<Node> nodes = nodesByDepth.get(level);
            for (Node node : nodes) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    replaceNodeWithUnificator(node);
                    modified = true;
                }
            }

        }

        return modified;

    }

    public static void replaceNodeWithUnificator(Node oldNode) {

        Node parentNode = oldNode.getParentNode();

        if (parentNode == null) {
            throw new IllegalArgumentException("Cannot replace node [" + oldNode + "] that has no parent.");
        } else {
            String unificatorElementType = oldNode.getNodeName().equals(PMATHMLOPERATOR) ? PMATHMLOPERATOR : PMATHMLIDENTIFIER;
            Node newNode = oldNode.getOwnerDocument().createElementNS(oldNode.getNamespaceURI(), unificatorElementType);
            newNode.setTextContent(UNIFICATOR);
            parentNode.replaceChild(newNode, oldNode);
        }

    }

    private int getMaxMajorNodesLevel() {

        int maxInt = 0;

        for (NodeLevel<Integer, Integer> level : nodesByDepth.keySet()) {
            if (maxInt < level.major) {
                maxInt = level.major;
            }
        }

        return maxInt;

    }

}

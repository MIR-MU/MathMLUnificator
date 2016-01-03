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

import cz.muni.fi.mir.mathmlunificator.config.Constants;
import static cz.muni.fi.mir.mathmlunificator.config.Constants.*;
import cz.muni.fi.mir.mathmlunificator.utils.DOMBuilder;
import cz.muni.fi.mir.mathmlunificator.utils.XMLOut;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.xml.sax.SAXException;

/**
 * MathML Unificator is a tool which performs simple MathML (Mathematical Markup
 * Language) unification
 * <a href="http://research.nii.ac.jp/ntcir/workshop/OnlineProceedings11/pdf/NTCIR/Math-2/07-NTCIR11-MATH-RuzickaM.pdf#page=7&zoom=page-fit">as
 * proposed</a>, i.e. single MathML formula transforms to series of formulae
 * with leaf elements substituted gradually for a special unification
 * representing symbol <code>&#x25CD;</code> (see {@link Constants#UNIFICATOR}).
 *
 * @author Michal Růžička
 */
public class MathMLUnificator {

    /**
     * Number of second level categories for ordering of elements in
     * {@link #nodesByDepth}.
     */
    private static final int NUMOFMINORLEVELS = 2;

    /**
     * Data structure represents ordered series of gradually unified MathML
     * formulae. The order is represented in two levels by
     * {@link NodeLevel<Integer, Integer>} object: The major level represents
     * depth of the {@link org.w3c.dom.Node} in the XML document DOM. The minor
     * level represents type of the {@link org.w3c.dom.Node} in MathML:
     * <code>1</code> represents elements of MathML operators, <code>2</code>
     * represents other types of MathML elements.
     */
    private HashMap<NodeLevel<Integer, Integer>, List> nodesByDepth;

    /**
     * <p>
     * In the given W3C DOM represented XML document find all maths nodes (see
     * {@link DocumentParser#findMathMLNodes(org.w3c.dom.Document)}) and
     * substitute them for series of formulae with leaf elements substituted
     * gradually for a special unification representing symbol
     * <code>&#x25CD;</code> (see {@link Constants#UNIFICATOR}).
     * </p>
     * <p>
     * Resulting series of the original and unified MathML nodes is itself
     * encapsulated in a new element &lt;unified-math&gt; (see
     * {@link Constants#UNIFIEDMATHROOTELMNT}) in XML namespace
     * <code>http://mir.fi.muni.cz/mathml-unification/</code> (see
     * {@link Constants#UNIFIEDMATHNS}).
     * </p>
     *
     * @param doc W3C DOM representation of the XML document to work on.
     * @see DocumentParser#findMathMLNodes(org.w3c.dom.Document)
     */
    public static void unifyMathML(Document doc) {

        List<Node> mathNodes = DocumentParser.findMathMLNodes(doc);
        for (Node mathNode : mathNodes) {
            MathMLUnificator.unifyMathMLNode(mathNode);
        }

    }

    /**
     * <p>
     * In the given W3C DOM represented XML document find all maths nodes (see
     * {@link DocumentParser#findMathMLNodes(org.w3c.dom.Document)}) and
     * substitute them for series of formulae with leaf elements substituted
     * gradually for a special unification representing symbol
     * <code>&#x25CD;</code> (see {@link Constants#UNIFICATOR}).
     * </p>
     * <p>
     * Resulting series of the original and unified MathML nodes is itself
     * encapsulated in a new element &lt;unified-math&gt; (see
     * {@link Constants#UNIFIEDMATHROOTELMNT}) in XML namespace
     * <code>http://mir.fi.muni.cz/mathml-unification/</code> (see
     * {@link Constants#UNIFIEDMATHNS}).
     * </p>
     * <p>
     * Untouched input will be returned and error logged in case of any error
     * during the processing.
     * </p>
     *
     * @param is Input stream with UTF-8 encoded string representation of the
     * XML document to work on.
     * @param os Output stream to write UTF-8 encoded string representation of
     * the processed XML document.
     * @throws java.io.IOException If any I/O error occurs while reading the
     * input stream.
     * @see DocumentParser#findMathMLNodes(org.w3c.dom.Document)
     */
    public static void unifyMathML(InputStream is, OutputStream os) throws IOException {

        String originalDoc = IOUtils.toString(is, "UTF-8");

        try {
            Document doc = DOMBuilder.buildDoc(new ByteArrayInputStream(originalDoc.getBytes("UTF-8")));
            unifyMathML(doc);
            XMLOut.xmlSerializer(doc, os);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(MathMLUnificator.class.getName()).log(Level.SEVERE, "MathML Unification from string failed, input string not modified.", ex);
            IOUtils.write(originalDoc, os);
        }

    }

    /**
     * <p>
     * Substitute the given MathML {@link Node} for series of MathML formulae
     * with leaf elements substituted gradually for a special unification
     * representing symbol <code>&#x25CD;</code> (see
     * {@link Constants#UNIFICATOR}).
     * </p>
     * <p>
     * Resulting series of the original and unified MathML nodes is itself
     * encapsulated in a new element &lt;unified-math&gt; (see
     * {@link Constants#UNIFIEDMATHROOTELMNT}) in XML namespace
     * <code>http://mir.fi.muni.cz/mathml-unification/</code> (see
     * {@link Constants#UNIFIEDMATHNS}).
     * </p>
     *
     * @param mathNode W3C DOM XML document representation attached MathML node
     * to work on.
     */
    public static void unifyMathMLNode(Node mathNode) {

        new MathMLUnificator().unifyMathMLNodeImpl(mathNode);

    }

    /**
     * <p>
     * Implementation of MathML unification. In the given W3C DOM represented
     * XML document find all maths nodes (see
     * {@link DocumentParser#findMathMLNodes(org.w3c.dom.Document)}) and
     * remember links to operator elements and other elements in
     * {@link #nodesByDepth} data structure. Then substitute them gradualy for
     * series of formulae with leaf elements substituted for a special
     * unification representing symbol <code>&#x25CD;</code> (see
     * {@link Constants#UNIFICATOR}).
     * </p>
     * <p>
     * Resulting series of the original and unified MathML nodes is itself
     * encapsulated in a new element &lt;unified-math&gt; (see
     * {@link Constants#UNIFIEDMATHROOTELMNT}) in XML namespace
     * <code>http://mir.fi.muni.cz/mathml-unification/</code> (see
     * {@link Constants#UNIFIEDMATHNS}) and put to the place of the original
     * math element {@link Node} in the XML DOM representation the node is
     * attached to.
     * </p>
     */
    private void unifyMathMLNodeImpl(Node mathNode) {

        nodesByDepth = new HashMap<>();

        Node unifiedMathNode = mathNode.getOwnerDocument().createElementNS(UNIFIEDMATHNS, UNIFIEDMATHROOTELMNT);
        mathNode.getParentNode().replaceChild(unifiedMathNode, mathNode);

        // New element encapsulating the series of unified formulae.
        unifiedMathNode.appendChild(mathNode.cloneNode(true));

        // Parse XML subtree starting at mathNode and remember elements by their depth.
        rememberLevelsOfNodes(mathNode);

        // Build series of formulae of level by level unified MathML.
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

    /**
     * Parse XML subtree starting at the given node and remember the type and
     * depth of all found (sub)elements in the XML DOM representation using
     * {@link #nodesByDepth}.
     *
     * @param rootNode XML DOM node to use as root element for processing.
     */
    private void rememberLevelsOfNodes(Node rootNode) {

        NodeList nodeList = rootNode.getChildNodes();

        rememberNodesStartingAtLevel(1, nodeList);

    }

    /**
     * Parse XML subtrees starting by the given nodes with the given starting
     * level and remember the type and depth of all found (sub)elements in the
     * XML DOM representation using {@link #nodesByDepth}.
     *
     * @param level The level the starting nodes will be rembered at. The
     * subelements of these nodes will be remeber on higher levels relative to
     * this starting level.
     * @param nodeList Collection of nodes to use as root elements for
     * processing.
     */
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

    /**
     * Get nodes rembemerd in {@link #nodesByDepth} of this instance at level
     * given by an instance of {@link NodeLevel} and replace these nodes (if
     * applicable, i.e. nodes are {@link Node#ELEMENT_NODE} elements) with
     * unification elements containing unification representing symbol
     * <code>&#x25CD;</code> (see {@link Constants#UNIFICATOR}).
     *
     * @param level Level in {@link #nodesByDepth} to work on.
     * @return <code>true</code> if any unifiable nodes were found at given
     * level and an unification was done, <code>false</code> otherwise.
     * @see #replaceNodeWithUnificator(org.w3c.dom.Node)
     */
    private boolean unifyAtLevel(NodeLevel<Integer, Integer> level) {

        boolean modified = false;

        if (level.major <= 0) {
            throw new IllegalArgumentException("Major level must be greather than 0.");
        } else if (level.minor <= 0) {
            throw new IllegalArgumentException("Minor level must be greather than 0.");
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

    /**
     * Replace the given node with unification element containing unification
     * representing symbol <code>&#x25CD;</code> (see
     * {@link Constants#UNIFICATOR}).
     *
     * @param oldNode The node to be replaced with the unification representing
     * element.
     * @throws IllegalArgumentException If the given node does not have parent.
     */
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

    /**
     * Get the highest major level from {@link #nodesByDepth} of this instance.
     *
     * @return The highest major level from {@link #nodesByDepth} of this
     * instance.
     * @see NodeLevel#major
     */
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

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
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Michal Růžička
 */
public class DocumentParser {

    public static List<Node> findMathMLNodes(Document doc) {

        NodeList mathNodeList = doc.getElementsByTagNameNS(MATHMLNS, MATHMLROOTELMNT);
        if (mathNodeList.getLength() == 0) {
            // If no math elements are found maybe the input DOM was created as
            // namespace unaware. In this case try to use any math-named element
            // regardless of its namespace.
            mathNodeList = doc.getElementsByTagName(MATHMLROOTELMNT);
        }

        List mathNodes = new ArrayList(mathNodeList.getLength());
        for (int i = 0; i < mathNodeList.getLength(); i++) {
            mathNodes.add(mathNodeList.item(i));
        }

        return mathNodes;

    }

}

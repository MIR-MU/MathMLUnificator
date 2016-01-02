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

import cz.muni.fi.mir.mathmlunificator.utils.DOMBuilder;
import static cz.muni.fi.mir.mathmlunificator.utils.XMLOut.xmlStdoutSerializer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Michal Růžička
 */
public class MathMLUnificatorCommandLineTool {

    public static void main(String argv[]) throws ParserConfigurationException {

        for (String filepath : argv) {

            try {

                System.out.println("\n###\n### Processing file '" + filepath + "' ###\n###");

                Document doc = DOMBuilder.buildDocFromFilepath(filepath);

                System.out.println("\n## Input document ##");
                xmlStdoutSerializer(doc);

                MathMLUnificator.unifyMathML(doc);

                System.out.println("\n## Output document ##");
                xmlStdoutSerializer(doc);

            } catch (SAXException | IOException ex) {
                Logger.getLogger(MathMLUnificatorCommandLineTool.class.getName()).log(Level.SEVERE, "Failed processing of file: " + filepath, ex);
            }

        }

    }

}

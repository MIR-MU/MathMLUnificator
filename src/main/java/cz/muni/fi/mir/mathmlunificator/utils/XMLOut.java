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
package cz.muni.fi.mir.mathmlunificator.utils;

import java.io.OutputStream;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * @author Michal Růžička
 */
public class XMLOut {

    public static void xmlStdoutSerializer(Document doc) {
        xmlSerializer(doc, System.out);
    }

    public static void xmlSerializer(Document doc, OutputStream os) {
        DOMImplementation domImpl = doc.getImplementation();
        DOMImplementationLS ls = (DOMImplementationLS) domImpl;
        LSOutput lso = ls.createLSOutput();
        LSSerializer lss = ls.createLSSerializer();
        lss.getDomConfig().setParameter("xml-declaration", true);
        lss.getDomConfig().setParameter("format-pretty-print", true);
        lso.setByteStream(os);
        lss.write(doc, lso);
    }

}

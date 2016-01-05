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

import cz.muni.fi.mir.mathmlunificator.utils.DOMBuilder;
import static cz.muni.fi.mir.mathmlunificator.utils.XMLOut.xmlStdoutSerializer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Command line tool that enables the user to select multiple input files and
 * print results of MathML unification by {@link MathMLUnificator} to the
 * application standard output.
 *
 * @author Michal Růžička
 */
public class MathMLUnificatorCommandLineTool {

    /**
     * Name of the JARFILE for prints in help.
     */
    private static final String JARFILE = "mathml-unificator.jar";

    /**
     * Main (starting) method of the command line application.
     *
     * @param argv Array of command line arguments that are expected to be
     * filesystem paths to input XML documents with MathML to be unified.
     * @throws ParserConfigurationException If a XML DOM builder cannot be
     * created with the configuration requested.
     */
    public static void main(String argv[]) throws ParserConfigurationException {

        final Options options = new Options();
        options.addOption("h", "help", false, "print help");

        final CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, argv);
        } catch (ParseException ex) {
            printHelp(options);
            System.exit(1);
        }

        if (line != null) {
            if (line.hasOption('h')) {
                printHelp(options);
                System.exit(0);
            }

            final List<String> arguments = Arrays.asList(line.getArgs());
            if (arguments.size() > 0) {
                for (String filepath : arguments) {
                    try {

                        System.out.println("\n###\n### Processing file '" + filepath + "' ###\n###");

                        Document doc = DOMBuilder.buildDocFromFilepath(filepath);

                        System.out.println("\n## Input document ##");
                        xmlStdoutSerializer(doc);

                        MathMLUnificator.unifyMathML(doc);

                        System.out.println("\n## Output document ##");
                        xmlStdoutSerializer(doc);

                    } catch (SAXException | IOException ex) {
                        Logger.getLogger(MathMLUnificatorCommandLineTool.class
                                .getName()).log(Level.SEVERE, "Failed processing of file: " + filepath, ex);
                    }
                }
            } else {
                printHelp(options);
                System.exit(0);
            }
        }

    }

    /**
     * Print summary of command line arguments and usage examples.
     *
     * @param options Options recognized by the application.
     */
    private static void printHelp(Options options) {

        System.err.println("Usage:");
        System.err.println("\tjava -jar " + JARFILE + " input.xml [ input.xml ... ]");
        System.err.println("\tjava -jar " + JARFILE + " -h");
        System.err.println("Options:");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printOptions(new PrintWriter(System.err, true), 80, options, 8, 8);

    }

}

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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 * @author Michal Růžička
 */
public class MathMLUnificatorCommandLineToolTest extends AbstractXMLTransformationTest {

    @Test
    public void testMain() throws Exception {

        String testFile = "multiple-formulae";
        String[] argv = {getTestResourceAsFilepath(testFile + ".input.xml")};

        ByteArrayOutputStream stdoutContent = new ByteArrayOutputStream();

        PrintStream stdout = System.out;
        System.setOut(new PrintStream(stdoutContent));
        MathMLUnificatorCommandLineTool.main(argv);
        System.setOut(stdout);

        String output = stdoutContent.toString();
        output = output.replaceAll("(### Processing file ').*(' ###)", "$1PATH REMOVED$2");

        assertEquals(IOUtils.toString(getTestResource(testFile + ".expected-output.txt")), output);

    }

}

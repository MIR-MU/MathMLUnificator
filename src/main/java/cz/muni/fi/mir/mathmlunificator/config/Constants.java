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
package cz.muni.fi.mir.mathmlunificator.config;

/**
 * Various constants definitions used by MathML Unificator.
 *
 * @author Michal Růžička
 */
public class Constants {

    /**
     * MathML XML namespace
     */
    public static final String MATHMLNS = "http://www.w3.org/1998/Math/MathML";
    /**
     * MathML XML root element name
     */
    public static final String MATHMLROOTELMNT = "math";

    /**
     * Presentation MathML operator element name
     */
    public static final String PMATHMLOPERATOR = "mo";
    /**
     * Presentation MathML identifier element name
     */
    public static final String PMATHMLIDENTIFIER = "mi";

    /**
     * MathML Unificator XML markup namespace
     */
    public static final String UNIFIEDMATHNS = "http://mir.fi.muni.cz/mathml-unification/";
    /**
     * MathML Unificator XML root element name
     */
    public static final String UNIFIEDMATHROOTELMNT = "unified-math";

    /**
     * Symbol used in MathML Unificator XML markup for XML tree substitutions
     */
    public static final String UNIFICATOR = "\u25CD";

}

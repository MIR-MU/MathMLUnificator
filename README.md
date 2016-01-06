# MathML Unificator

MathML Unificator is a tool which performs simple MathML (Mathematical Markup 
Language) unification as proposed in
[RŮŽIČKA, Michal, Petr SOJKA and Martin LÍŠKA.
 Math Indexer and Searcher under the Hood:
 History and Development of a Winning Strategy.
 In Noriko Kando, Hideo Joho, Kazuaki Kishida.
 Proceedings of the 11th NTCIR Conference on Evaluation of Information Access
 Technologies.
 Tokyo: National Institute of Informatics, 2-1-2 Hitotsubashi, Chiyoda-ku, Tokyo
 101-8430 Japan, 2014. p. 127-134, 8 pp. ISBN 978-4-86049-065-2]
(http://research.nii.ac.jp/ntcir/workshop/OnlineProceedings11/pdf/NTCIR/Math-2/07-NTCIR11-MATH-RuzickaM.pdf#page=7&zoom=page-fit).

For more information see [https://mir.fi.muni.cz/](https://mir.fi.muni.cz/).


## Usage

```none
Usage:
	java -jar mathml-unificator.jar input.xml [ input.xml ... ]
	java -jar mathml-unificator.jar -h
Options:
        -h,--help        print help
```

### File encoding on Windows

On Windows, file encoding defaults to system-language-specific single-byte
encoding. To ensure JVM uses UTF-8 start JVM with command line argument
`-Dfile.encoding=UTF-8`:

```none
java -Dfile.encoding=UTF-8 -jar mathml-unificator.jar
```

However, be aware the default Windows command line shell has significant
problems with Unicode in the default configuration. Try Lucida console font with
appropriate [shell code page setting](http://stackoverflow.com/questions/388490/unicode-characters-in-windows-command-line-how/388500#388500):

```shell
chcp 65001
```

### Example

```xml
$ java -jar mathml-unificator.jar sample-data/single-formula.xml
<?xml version="1.0" encoding="UTF-8"?>
<unified-math xmlns="http://mir.fi.muni.cz/mathml-unification/">
    <math xmlns="http://www.w3.org/1998/Math/MathML">
        <msup>
            <mi>a</mi>
            <mn>2</mn>
        </msup>
        <mo>+</mo>
        <mfrac>
            <msqrt>
                <mi>b</mi>
            </msqrt>
            <mi>c</mi>
        </mfrac>
    </math>
    <math xmlns:uni="http://mir.fi.muni.cz/mathml-unification/"
        uni:unification-level="1" uni:unification-max-level="4" xmlns="http://www.w3.org/1998/Math/MathML">
        <msup>
            <mi>a</mi>
            <mn>2</mn>
        </msup>
        <mo>+</mo>
        <mfrac>
            <msqrt>
                <mi>◍</mi>
            </msqrt>
            <mi>c</mi>
        </mfrac>
    </math>
    <math xmlns:uni="http://mir.fi.muni.cz/mathml-unification/"
        uni:unification-level="2" uni:unification-max-level="4" xmlns="http://www.w3.org/1998/Math/MathML">
        <msup>
            <mi>◍</mi>
            <mi>◍</mi>
        </msup>
        <mo>+</mo>
        <mfrac>
            <mi>◍</mi>
            <mi>◍</mi>
        </mfrac>
    </math>
    <math xmlns:uni="http://mir.fi.muni.cz/mathml-unification/"
        uni:unification-level="3" uni:unification-max-level="4" xmlns="http://www.w3.org/1998/Math/MathML">
        <mi>◍</mi>
        <mo>+</mo>
        <mi>◍</mi>
    </math>
    <math xmlns:uni="http://mir.fi.muni.cz/mathml-unification/"
        uni:unification-level="4" uni:unification-max-level="4" xmlns="http://www.w3.org/1998/Math/MathML">
        <mi>◍</mi>
        <mo>◍</mo>
        <mi>◍</mi>
    </math>
</unified-math>
```


## Licence
MathML Unificator is licensed under the terms of the Apache License, Version 2.0.

MathML Unificator – Generalizes mathematical formulae for structural unification
================================================================================
[![ci](https://github.com/MIR-MU/MathMLUnificator/workflows/Build/badge.svg)][ci]

 [ci]: https://github.com/MIR-MU/MathMLUnificator/actions (GitHub Actions)

[MathML Unificator][mathmlunificator] is a tool which performs structural
MathML unification as proposed by [Růžička, Sojka, and Líška,
2016][ruzickaetal16].

 [mathmlunificator]: https://mir.fi.muni.cz/mathml-normalization/#mathml-unificator
 [ruzickaetal16]: http://research.nii.ac.jp/ntcir/workshop/OnlineProceedings12/pdf/ntcir/MathIR/05-NTCIR12-MathIR-RuzickaM.pdf

Usage
=====
File encoding on Windows
------------------------

On Windows, file encodings default to system-language-specific single-byte
encodings. To ensure that JVM uses UTF-8, start JVM with command line argument
`-Dfile.encoding=UTF-8` as follows:

```
java -Dfile.encoding=UTF-8 -jar mathml-unificator.jar
```

However, be aware the default Windows command line shell has significant
problems with Unicode in the default configuration. Try Lucida console font with
appropriate [shell code page setting][stack-overflow].

 [stack-overflow]: https://stackoverflow.com/a/41787848/657401

Example
-------

Executing the following command derives a series of four increasingly general
formulae from an [example formula](sample-data/single-formula.xml):

```xml
$ java -jar mathml-unificator.jar -p sample-data/single-formula.xml
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

Citing MathML Unificator
========================
Text
----
RŮŽIČKA, Michal, Petr SOJKA a Martin LÍŠKA. Math Indexer and Searcher under
the Hood: Fine-Tuning Query Expansion and Unification Strategies. In
*Proceedings of the 12th NTCIR Conference on Evaluation of Information Access
Technologies.* Tokyo: National Institute of Informatics, 2-1-2 Hitotsubashi,
Chiyoda-ku, Tokyo 101-8430 Japan, 2016. 7 pp. 

BibTeX
------
``` bib
@inproceedings{RuzickaSojkaLiska16Math,
     author = "Michal R\r{u}\v{z}icka and Petr Sojka and Martin L{\' i}ska",
      title = "{Math Indexer and Searcher under the Hood: Fine-tuning Query
                Expansion and Unification Strategies.}",
  booktitle = "{Proceedings of the 12th NTCIR Conference on Evaluation of
                Information Access Technologies}",
     editor = "{Noriko Kando et al.}",
      pages = "331--337",
       year = 2016,
}
```

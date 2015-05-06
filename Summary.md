## Final Summary ##
### Development organisation ###
This project was first supposed to be developed in cooperation of four team members. Yet since **SVN [revision 40](https://code.google.com/p/parser-z-xsd-schematu/source/detail?r=40)** deployment other team members have resigned and the whole development process was managed only by me (Michal Štefánik).
<br><br>
I hereby declare that, all the following parts of Project are my personal work:<br>
<br>-all the source codes of the application core (Main, Binder, Marshaler) (package 'source'),<br>
<br>-all the testing sources (package 'input'), except complexXML.xml a complexXSD.xsd (randomly gathered from the internet, source: <a href='http://docstore.mik.ua/orelly/xml/schema/ch02_01.htm'>http://docstore.mik.ua/orelly/xml/schema/ch02_01.htm</a>),<br>
<br>-JUnit tests for Binder<br>
<br>-Interfaces attached to output, implemented by generated classes (SimpleType, ComplexType)<br>
<br>
<h3>Development description</h3>
Since that first partial implementation of the application has too high complexities, and many bugs (as indicated by existing JUnit tests), I have decided to start the implementation from scratch. <br>
Objective decomposition of the new solution is obviously inspired by JAXB: application core consists of classes: Binder and Marshaler (detailed in <a href='https://code.google.com/p/parser-z-xsd-schematu/wiki/Analyza'>https://code.google.com/p/parser-z-xsd-schematu/wiki/Analyza</a>). <br>
Static method in Binder class are crucial for correct application functionality and therefore their correctness is extra verified by JUnit tests.<br>
Major methods bind() in Binder and marshal() in Marshaleri profusely use tested methods and are not tested itself. Their correctness is verified by a valid and syntax-correct output of the application. <br>
To enable automatic testing of Project were attached testing XML Schemas and Documents (package 'input').<br>
To ease possible further development above this application, together with the generated classes are attached their interfaces (SimpleType, ComplexType, ParserInterface).
Project, as inspired by JAXB, contains core classes Binder a Marshaler called from executable class Main.
### Binder ###
Binder's input is XMl Schema. <br>
First, all the elements ('xsd:element') are extracted from it, then for every element is generated relevant java class. (containind empty variables (of specified type) for element content and every attribute.<br>
Binder's output are generated java classes and a collection describing Content tree defined by XML Schema.<br>
<h3>Marshaler</h3>
Marshaler takes as input: 1) initialized Collection and 2) XML document valid for input XML Schema <br>
Marshaler for each element of Collection browses input XML for relevant element and initializes variables from it to relevant class. <br>
Initialization is realized by generated class Parser.<br>
Súčasťou inicializácie je, okrem naplnenia tried dátami z XML, aj hierarchické usporiadanie elementov (v parseri = premenných) podľa vstupnej XML Schemy, pomocou ID. Pre hierarchický model stromu sa používa trieda ParsedTree.java, pre reprezentáciu XML uzlov trieda Node.java ( obe v package 'source'). <br>
Marshaler's output is generated and initialised class Parser, ready for further usage. <br>
<br>
Both main classes use static methods from Binder class (for Schema analysis), those methods are verified by JUnit tests. <br>
<br>
This project's code does not itself verify whether the input XML Schema is valid, nor whether it accords with embedded XML document. This approach, under certain circumstances, gives programmer more freedom with usage. If necessary, a programmer is given many existing tools to verify Schema. If input Schema and XML documents are not in accordance, and XML source is not boundable to the generated classes, core class Marshaller throws undetermined exception.<br>

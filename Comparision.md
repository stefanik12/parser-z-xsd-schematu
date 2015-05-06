## Introduction ##
This project is conceptually inspired by JAXB architecture, therefore impmeneted features if this project have significant intersection with JAXB.
### About JAXB ###
JAXB (Java Architecture for XML Binding) is an approach to access structured data implemented in Java. In compare to JAXP (with its accessing through callback methods over SAX model, direct path addressing over DOM model) in JAXB, first step is to initialize objects (to represent XML). <br> Initialization steps: <br>
1. XML elements binding into objects (from XML Schema) <br>
2. Data insertion into generated objects (refered as unmarshaling), and object ordering. This ordering aims to describe the document structure and results in generating a tree structure (so called Content tree).<br>
<br>
<br>
In initialization process is a collection of objects filled up with data from XML document(s) and ready to offer a developer a convenient access over the data through generated objects.<br>
<br>
<h2>Comparision</h2>

Since the project was aimed to emulate the features of JAXB, it is very similar to some core functionality of JAXB:<br>
<h3>Initialisation</h3>
Is processed in similar event order: In project:<br> 1) class Binder performs element mapping above the relevant XML Schema and generates appropriate classes (objects) that implement relevant interfaces (simpleType - complexType interface).<br>
<br>2) Class Marshaler then generated Parser class, that is meant to fulfill the objects from Binder with relevant data from XML Document(s). Together with that, class ParsedTree is initialised. This class describes the structure of input XML document as a tree structure (functionally parallel to Content Tree in JAXB).<br>
<br>
<br>
JAXB, in compare to Project, contains some features, which this project has not intended to implement:<br>
<h3>JAXB moreover contains</h3>
- Possibility of reversed generation of XML document from Content tree and binded objects (referred as "Marshalling")<br>
- Validation of input XML document aganist input XML Schema<br>
- Ablity to manually choose data type that data input from XML will be bound to.<br>

<h3>Project approach moreover enables</h3>
- Transparent object initialization in Parser, easily editable<br>
- Under some circumstances marshal XML to objects from its non-valid XML Schema<br>
- Extension to use one Schema for multiple XML dokuments<br>
## Záverečná správa ##
### Organizačné pozadie ###
Projekt mal byť pôvodne riešený s rozdelením úloh uvedeným v https://code.google.com/p/parser-z-xsd-schematu/wiki/RozdelenieVyvoja.
Po neúspešných snahách o implementáciu jadra Binder-u sa členovia David Fatěna, Sven Relovský a vedúci projektu Marek Burda rozhodli rezignovať a projekt nedokončiť. Od **SVN rev.40** som pokračoval vo vývoji ja sám (Michal Štefánik).

### Skutočné rozdelene vývoja ###
Prehlasujem, že mojim (Michal Štefánik) osobným dielom na projekte sú
<br>-všetky zdrojové súbory jadra aplikácie (Main, Binder, Marshaler) (package 'source'),<br>
<br>-vstupné súbory pre testy a určené na testovanie celkového výstupu aplikácie (package 'input') okrem complexXML.xml a complexXSD.xsd (zdroj: <a href='http://docstore.mik.ua/orelly/xml/schema/ch02_01.htm'>http://docstore.mik.ua/orelly/xml/schema/ch02_01.htm</a>),<br>
<br>-JUnit testy pre Binder<br>
<br>-Rozhrania pre vygenerované triedy (SimpleType, ComplexType)<br>
<br>
<h3>Popis môjho riešenia</h3>
Na vymyslený koncept som čiastočne nadviazal, pokiaľ sa zhodoval s konceptom JAXB. Predchádzajúca čiastočná implementácia však mala veľké zložitosti a veľa chýb (podľa existujúcich JUnit testov), preto som sa rozhodol začať s vývojom sám odznova. <br>
Pri objektovej dekompozícii riešenia som sa inšpiroval JAXB: jadro aplikácie sa skladá z tried Binder a Marshaler (detailne v <a href='https://code.google.com/p/parser-z-xsd-schematu/wiki/Analyza'>https://code.google.com/p/parser-z-xsd-schematu/wiki/Analyza</a>). <br>
Nad statickými metódami triedy Binder boli ako prvé vytvorené aj JUnit testy. Tieto metódy sú použité aj v triede Marshaler a sú kritické pre korektnosť aplikácie. Hlavné metódy bind() v Binderi a marshal() v Marshaleri používajú testované metódy a samé potom nie sú testované. Ich testom môže byť samotný korektný výstup aplikácie do vygenerovaných tried. <br>
Na otestovanie celkového výstupu riešenia boli v projekte priložené testovacie súbory rôznej zložitosti.<br>
Pre sprahľadnenie ďaľsieho použitia Aplikácie sú v balíku 'generatedInterface' priložené rozhrania vygenerovaných tried, s prázdnym rozhraním ParserInterface, ktoré môže byť doplnené v nadväznosti na tento projekt pri ďaľšom vývoji.
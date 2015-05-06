XML a Java jsou nejlepší stavební kameny pro tvorbu webových služeb a webových aplikací využívající webové služby. Java API (_Java Application Programming Interface_) nazývány **JAXP** a **JAXB** pomáhají přistupovat aplikacím napsaných v programovacím jazyce Java k XML dokumentům.



# JAXP #

**JAXP** (výslovnost _|ˈdʒækspiː|_) je zkratka pro _Java API for XML Processing_. Slouží ke zpracování XML dat za použití JAVA aplikací.


Využívá parsovací standardy

  * **SAX** (_Simple API for XML Parsing_) - parsuje data jako proud událostí
  * **DOM** (_Document Object Model_) - parsuje data jako objekt
  * **StAX** (_Streaming API for XML_) - od verze 1.4


Dále podporuje

  * **XSLT standard** (_Extensible Stylesheet Language Transformations_) pro lepší kontrolu reprezentace dat a zároveň umožňuje transformovat data do jiných XML dokumentů nebo jiných formátů, jako např. HTML.
  * **namespace** - podporuje práci s DTD, které by za různých okolností měly konflikty jmen.



Silná stránka JAXP je flexibilita. Umožňuje použití libovolného parseru kompatibilního s XML uvnitř aplikace za použití tzv. nástavbové vrstvy, která umožňuje implementaci SAX nebo DOM API nebo dokonce XSL procesoru pro lepší kontrolu vzhledu XML dat.




# JAXB #

**JAXB** (výslovnost _|ˈdʒæksbiː|_) je zkratka pro _Java Architecture for XML Binding_. Slouží k serializování Java objektů na XML a opačně, a tím umožňuje uložení a opětovné získání dat z paměti v jakémkoli XML formátu bez nutnosti implementovat specifické struktury pro načítání a ukládání XML.

Při **procesu svazování** JAXB vygeneruje a zkompiluje JAXB třídy ze zdrojového schématu a sestaví aplikací, která tyto třídy implementuje. Poté spustí aplikaci na rozřazení, použití, schválení a seřazení XML obsahu pomocí JAXB svazujícícho rámce.


**Proces svázání po krocích**

  * **1. generování tříd** - XML schéma je použito jako vstup pro JAXB kompilátor, generují se JAXB třídy založeny na tomto schématu
  * **2. kompilování tříd** - všechny generované třídy, zdrojové soubory a kódy musí být zkompilovány
  * **3. deserializace** - XML dokumenty ve zdrojovém schématu musí být deserializovány JAXB binding framowerkem.
  * **4. generování obsahového stromu** - deserializovací proces generuje obsahový strom objektů, tento strom reprezentuje strukturu a obsah zdrojového XML dokumentu
  * **5. validace** - validace zdrojového XML dokumentu
  * **6. zpracování obsahu** - klient může modifikovat XML data reprezentována obsahovým stromem použitím rozhraní generovaného kompilátorem
  * **7. serializace** - zpracovaný obsahový strom je serializován do jednoho nebo více XML dokumentů


![http://docs.oracle.com/javase/tutorial/figures/jaxb/jaxb-dataBindingProcess.gif](http://docs.oracle.com/javase/tutorial/figures/jaxb/jaxb-dataBindingProcess.gif)




# Rozdíly mezi JAXP a JAXB #

TBD
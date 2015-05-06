Projekt obsahuje podľa vzoru JAXB hlavné triedy Binder a Marshaler volané z exekučnej triedy Main.

Binder má na vstupe XML Schemu.
Z tej extrahuje všetky elementy ('xsd:element'), podľa ktorých potom vygeneruje príslušné javové triedy (obsahujúce inicializované premenné pre obsah (content) a atribúty) a tie uloží do balíka 'generated'.
Výstupom sú javové triedy v balíku generated a inicializovaná štruktúra Collection obsahujúca pre každý element zo Schémy relevantné dáta, použité v Marshaleri.

Marshaler má na vstupe 1) inicializovanú Collection a 2) XML dokument validný pre vstupnú XML schemu.
Marshaler pre každý element z Collection vyhľadá vo vstupnom XML dokumente relevantný element a z neho inicializuje premenné relevantnej triedy.
Inicializácia prebieha vo vygenerovanej triede Parser.java.
Súčasťou inicializácie je, okrem naplnenia tried dátami z XML, aj hierarchické usporiadanie elementov (v parseri = premenných) podľa vstupnej XML Schemy, pomocou ID. Pre hierarchický model stromu sa používa trieda ParsedTree.java, pre reprezentáciu XML uzlov trieda Node.java ( obe v package 'source').
Výstupom Marshaler-u je vygenerovaná inicializovaná trieda Parser, pripravená na ďaľšie použitie.

Obe triedy používajú metódy z triedy Binder (per analýzu XML Schemy), pre tieto metódy sú vytvorené aj JUnit testy.

Kód sám neoveruje, či je vstupný XML dokument validný pre vstupnú XML Schemu, na čo slúžia iné nástroje. V prípade, že sa Schema nezhoduje so vstupným XML, Marshaler vyhodí na nešpecifikovanom mieste výnimku.
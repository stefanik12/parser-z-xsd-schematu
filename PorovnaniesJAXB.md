Porovnanie možností tohto projektu s možnosťami JAXB API

## Úvod ##
Tento projekt je koncepčne inšpirovaný architektúrou JAXB, možnosti jeho riešenia majú teda veľký prienik s možnosťami JAXB.
### O JAXB ###
JAXB je API koncepčne odlišné od tradičného prístupu k dátam v štrukturovanom formáte (zvyčajne XML). Samotný prístup k dátam (pomocou callback metód SAX modelu, alebo priamou adresáciou pri DOM modeli)
predchádza pri JAXB inicializácia objektov. <br> Časti inicializácie: <br>
1. Mapovanie (Binding) elementov XML podľa jeho Schemy na objekty <br>
2. Zaradenie dát a usporiadanie objektov (Unmarshaling) do hierarchickej štruktúry odpovedajúcej vstupnému XML. Šttruktúra je reprezentovaná Obsahovým stromom (Content tree).<br>
<br>
<br>
Po procese inicializácie je kolekcia objektov (implementujúcich relevantné rozhrania) naplnená dátami a pripravená poskytnúť developerovi prístup k dátam cez tieto objekty.<br>
<br>
<h2>Porovnanie</h2>

Keďže Projekt mal za cieľ napodobniť a priblížiť sa možnostiam JAXB, v mnohom sa s JAXB zhoduje:<br>
<h3>Inicializácia</h3>
Prebieha v podobnom slede udalostí: V Projekte:<br> 1) trieda Binder prevedie zmapovanie elementov popísaných v Scheme a vygenerovanie príslušných tried implementujúcich relevantné rozhranie (simpleType - complexType interface).<br>
<br>2) Trieda Marshaler potom vygeneruje triedu Parser, ktorá naplní vygenerované triedy (objekty) Binder-u dátami. Inicializáciou triedy ParsedTree sa popíše štruktúra vstupného XML (paralelne ku Content Tree v JAXB).<br>
<br>
<br>
Na rozdiel od Projektu, v JAXB sú obsiahnuté aj ďaľšie nástroje, ktoré Projekt nemal v zámere realizovať:<br>
<h3>JAXB obsahuje naviac</h3>
- Možnosť spätného generovania XML podľa Content tree a zmapovaných objektov (odkazovanú ako "Marshalling")<br>
- Validáciu vstupného XML voči vstupnej Scheme pri Unmarshallingu<br>
- Voľbu dátového typu na uloženie simpleType elementov<br>

<h3>Projektové riešenie umožňuje naviac</h3>
- Transparentnú inicializáciu objektov v Parser-i, ľahko upraviteľnú<br>
- Možnosť za určitých okolností inicializovať objekty z XML, ktoré nie je validné pre namapovanú XML Schemu<br>
- Rozšírenie pre použitie namapovanej Schemy pre viac XML dokumentov<br>
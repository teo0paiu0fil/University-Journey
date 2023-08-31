## Paiu Teofil 323CB
----
Pentru implementarea temei am ales java deoarece sunt mai familiar cu acesta si imi permite lucruri cu String-uri mult mai usor.

---
### Structura 

Am creat 3 clase care imi realizeaza urmatoarele operati:
* Graf: ce imi pastreaza matricea de adiacenta si numarul de noduri.
* Sat: ce are 2 metode una pentru a crea literari simpli si negati cu ajutorul parametului tip ```public static String creazaLiteral (int tip, int index1, int index2)``` . Si cealata care imi converteste forma (x11^x12)V(...)... in DIMACS si realizez aceasta trecere mapand literari cu valori numerice incepand de la 1 cu ajutorul unui hashmap
* kCliqueToSat: este reprezentata de cele 3 conditi din cerinta scrise in functii diferite si metoada main ce citeste apeleaza metodele si scrie output-ul in fisierul de iesire. Aceste functi parcurg fiecare nod precum este descris in cerinta. 


verificNod - imi adauga toate combinațiile între noduri si valorile pana la k

elementeUnice - imi verifica sa nu ai un nod în doua poziții diferite

verificareClica - se uita daca doua noduri n-au muchie intre ele sa nu fie ambele noduri in clica

----


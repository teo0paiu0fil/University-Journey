### Descriere clase
* Clasa `Application` se ocupa de gestionarea si executarea comenzilor, 
aceasa are ca si componente doua hashmap-uri pentru chestionarele si useri
din sistem, o clasa ce reprezinta clientul de la baza de date (in acest exemplu
baza de date este inlocuita cu o clasa care implementeaza o interfata cu 3 
 metode definite write, read si reset, iar cu acestea scrie in fisiere salvate text
datele primite).

* Clasa `User` reprezinta ultilizatorul aplicatiei aceasta ca si atribute numele acestuia
si parola , doua liste pentru intrebarile create de acesta si unul pentru contorizarea 
chestionarelor la care utilizatorul a raspuns si niste atribute statice pentru generarea unor
id-uri unice.

* Clasa `Question` are ca atribute body-ul intrebari, tipul intrebari, un id unic si o lista
de raspunsuri.

* Clasa `Quiz` reprezinta chestionarul in sine, acesta este creat de utilizator cu intrebarile
adaugate tot de acesta, are ca atribute o lista de intrebari, autorul, un hashmap cu punctajul
optinut de utilizatori care au parcurs acest chestionar un id unic si o pondere ce reprezinta
punctajul pe intrebare.

* Clasa `Answer` reprezinta raspunsul cu atributele sale: body-ul raspunsului, id generat unic,
daca raspunsul este corect sau nu.

* Clasa `MyDatabase` reprezinta baza de date aceasta implementeaza o interfata foarte cu niste
functi specifice unei baze de date, primeste ca parametri colectia (fisierul) unde va scrie, citi,
reseta datele.

### Metode si implementare 

* Pentru rularea comenzilor apelez metoda `run()` din clasa aplication aceasta verifica daca datele au
din baza de date au fost incarcate in aplicatie, daca nu se apleaza metoda `load()` ce incarca datele
din fisierele din vectorulul de collections acesta citeste linie cu line din ele si retine informatia intr-o
lista lucru realizat de metoda `read()` prelucreaza output-ul pentru a crea instantele necesare rulari programului.
Dupa care in functie de argumentul transmis comenzi run se efectueaza comanda dorita si se intoarce log-ul potrivit.

* Toate ce tin de comenzi in clasa `Application` verifica argumentele primite si intorc cod de eroare daca un parametru
nu a fost oferit.

### Pentru BONUS

* Un chestionar sa nu poata avea aceasi intrebare pusa de 2 ori
* Un user sa nu poata sterge un chestionar ce nu a fost creat de acesta


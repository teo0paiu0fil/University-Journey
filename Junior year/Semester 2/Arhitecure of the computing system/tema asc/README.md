Nume: PAIU TEOFIL
Grupă: 333CB

# Tema 1 Le Stats Sportif

Organizare
-

Serverul are un obiect threadpool care gestioneaza computarea taskurilor si informatile
joburilor submise.
Threadpool porneste si verifica daca exista o variabila de enviroment cu care sa deschida un numar egal de threaduri, acesta are in componenta sa 2 liste in care stocheaza taskurile terminate si cele care ruleaza in acel moment si o coada in care asteapta taskurile submise prin routele api-ului.
Din cele documentate operatile de pe queue si lista sunt thread safe, deci nu necesita sincronizare
Dupa urmeaza filtrarea datelor in functie de nevoile pentru calcule.
Iar in final scrierea rezultatului in fisierul cu id identic cu al jobului.

##  Consideri că tema este utilă?
Tema a fost interesanta si distractiva pana intr-un punct (unittesting) care a facut-o mai degraba o povara dar lasand la deoparte aceea sectiune cred ca a fost cea mai practica tema pe care am realizat-o in 3 ani de facultate.

## Consideri implementarea naivă, eficientă, se putea mai bine?
Cred ca implementarea este naiva, cred ca puteam folosi cateva event-uri pentru a semnala diferite actiuni
iar per total sa fac procesul putin mai eficient. Personal sunt fericit cu implementarea mea.

Implementare
-

Am implementat aproape intregul enunt cu mentiunea ca partea de unittesting nu e completa nici 25

## Dificultăți întâmpinate
La realizare testari rutei graceful_shutdown am sesizat ca odata ce testez un singur test serverul ramane inchis si nu mai accepta taskuri - doream sa realizez testare job_submision - graceful_shutdown - get_job_result, graceful_shutdown - job_submision - raspuns mesaj de eroare, etc.

A fost destul de greu sa parcurg documentatia de la logging personal nu am vibrat foarte bine cu ea.

Resurse utilizate
-

documentatile de la logging, pylint, unittesting, si exemplul dat din schelet pentru toate rutele 

Git
-
Am introdus folderul .git si outputul comenzi git log in fisierul workflow.git.log

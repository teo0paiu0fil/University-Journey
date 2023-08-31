# Loader Executioner

## Introductie 

In elaborarea temei am realizat un **`handler`** a semanului **`SIGSEGV`** (cod 11) care se 
ocupa cu maparea, distribuirea drepturilor de read write execute si de oprirea 
programului cu codul potrivit de `exit (139)` in cazul unui acces de memorie nepermis.

## Continut si desfasurarea functiei 

Caut sa vad daca semnalul s-a petrecut in segmentele executabilului meu, in caz
contrar voi rula **`handler-ul default`** (imi creez o noua `structura sigaction`
cu care voi apela funtia default de sa_sigaction)
Verificarea se petrece comparand addresa de unde incepe segmentul si unde se 
termina.

![Segments](./schem.png)

Adresa de inceput a unui segement este: `exec->segments[i].vaddr` unde i reprezinta
numarul segmentelor executabilului cu conditia ca i < exec->segemnts_no.

Ultima adresa a segementului o aflam adunand dimensiunea memoriei unui segment cu
adresa la care incepe segementul: `exec->segments[i].vaddr + exec->segments[i].mem_size`.

Daca adresa din `info->si_addr` se afla intre aceste 2 valori pentru oricare `i segment`
verific ce a cauzat declansarea acestuia, in cazul unui acces nepermis la acea zona 
voi rula **`handler-ul default`** iar in cazul in care pagina nu a fost mapata
in memorie o sa o mapez impreuna cu permisiunile necesare.

Tin cont de paginile mapate in field-ul data al fiecarui segment conform 
recomandari de pe [ocw](https://ocw.cs.pub.ro/courses/so/teme/tema-3) la 
Interfața parser explicatile strucurilor folosite de parser : `"data - un pointer`
`opac pe care îl puteți folosi să atașați informații proprii legate de segmentul`
`curent (spre exemplu, puteți stoca aici informații despre paginile din segment` 
`deja mapate)"` si notez cu 1 paginile mapate.

Dupa mapare trebuie sa scriu in zona mapata informatile din executabilul dat ca 
argument asa ca folosesc conform restrictilor funtii POSIX, functile open si close pentru
a deschide fisierul cu calea data ca si argument in functia `so_execute`. Functia open 
imi returneaza un file descriptor din care voi citi si voi scrie in paginile mapate 
informatia daca `exec->segments[i].file_size > numarul_pagini_ce_trebuie_mapate_de_la`
`inceputul_segmentului(pageNumber) * dimensiunea unei pagini(page_size)`.

In cadrul mapari folosesc flag-ul MAP_ANONYMOUS care conform [man mmap](https://man7.org/linux/man-pages/man2/mmap.2.html)
initializeaza zona cu 0.

## ***Surse:*** 
* ### [posix function](https://www.ibm.com/docs/en/zos/2.1.0?topic=functions-open-open-file)
* ### [mmap && mprotect](https://man7.org/linux/man-pages/man2/mmap.2.html)
* ### [signal](https://man7.org/linux/man-pages/man2/signal.2.html)
* ### [structura sigaction](https://man7.org/linux/man-pages/man2/sigaction.2.html)

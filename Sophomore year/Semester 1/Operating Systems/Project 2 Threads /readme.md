In realizarea temei am folosit librariile si API-ul POSIX al sistemului de operare 
pentru a crea un planificator de thread-uri ce permite rularea a unui singur thread 
de o data cu ajutorul semafoarelor.

In so_init am initializat structura mea ce retine ce thread ruleaza, cat si 
threadurile blocate si cele care gata sa porneasca. Am verifiact parametri primiti

In functia so_fork creez propriu zis noul thread si verific daca poate sa porneasca 
sau trebuie sa isi astepte randul pentru a rula tot odata scad cuanta de timp (la 
fel si in so_exec )a threadului ce ruleaza si verific daca este necesar preemtatia 
treadului

Pentru functile so_wait si so_signal doar verific parametri primit si returnez 
codurile de eroare cuvenite

Iar in final dezaloc si astept rularea fiecarui thread in functia so_end.
Paiu Teofil 313CB

Algoritmul proximal:
  Functia proximal_2x2 determina punctele celor 4 pixeli ce inconjoara punctul xp , yp ,
creez un vector ce imi va tine distantele dintre puncte calculate in functie de step, pe care il parcurg
in acelasi timp in care parcurg matricea de output daca valoarea din vector e mai mica dacat 1,5 si pe linii 
si pe coloane atunci fiecare element al matrici va lua valoarea f(1,1) , daca va fi mai mare pe linie de 1,5 dar
mai mica pe coloane vom pune valoarea f(1,2) si asa mai departe.
  Functia proximal_resize calculeaza noi coeficienti si aplica o matrice de tansformare pentru a afla valorile initiale
  Functia proximal_rotate creez o matrice de rotatie pe care o aplic fiecaruli punct din matrice
  Functia proximal_coef calculeaza coeficienti necesari rotiri imagini calculata din sistemul liniar descris in cerinta temei
  Functile *_RGB descompune imaginea in 3 matrici care contine canalul culorilor rosu verde respectiv albastru
aplic transformarea cuvenita fiecareia dinte ele si la final le combin la loc cu functia cat

Algoritmul Bicubic:
  Functia precalc_d calculeaza derivatele punctelor unor imagini si le retine intr-o matrice aceasta se foloseste de functile fx, fy , fxy
care verifica daca valorile x si y sunt cumva pe marginea matrici , daca da atunci valoarea rezultata va fi 0 si in caz contrat
aplic formulele din cerinta temei
  Functia bicubic_coef calculeaza coeficienti necesari pentru redimensionarea matrici creez matircile necesare calculari coeficientilor si rezolv sistemul
  Functia bicubic_resize apeleaza cele 2 functii parcurge matricea pe linii si coloane aplica transformarea pe xp si yp si in final calculeaza
noua imagine folosind sistemul descris in cerinta temei

Algoritmul Lanczos:
  Am incercat sa adaptez pseudocodul oferit in tema
  




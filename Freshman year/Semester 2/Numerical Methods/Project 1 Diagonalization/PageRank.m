function [R1 R2] = PageRank(nume, d, eps)

  % calculez prin cele doua algoritmuri R1 si R2
  R1 = Iterative(nume, d, eps);
  R2 = Algebraic(nume, d);
  
  % deshid fisierul pentru a putea citi numarul de pagini si cele 2 valori pentru 
  % gradul de apartanenta
  file = fopen(nume, "r");
  dates = dlmread(file);
  fclose(file);
  
  %contcatenez numele fisierului deschis cu stringul ".out"
  numeiesire = strcat(nume,".out");
  % deschid acest nou fisier pentru a putea scrie datele in el
  out = fopen(numeiesire,'w');
  % pe prima linie se afla numarul de pagini
  fprintf(out, "%d\n", dates(1,1));
  
  % pe urmatoarele lini se vor afla datele calculate la primul
  for i = 1: dates(1,1)
    fprintf(out, "%0.6f\n", R1(i));
  endfor
    
    fprintf(out,"\n");
  % respectiv al doilea algoritm
  for i = 1: dates(1,1)
    fprintf(out, "%0.6f\n", R2(i));
  endfor
   
    fprintf(out,"\n");
   
   % sortez vectorul R2 descrescator
   SortR1 = sort(R2, "descend");
   
   val1 = dates(dates(1,1)+2 ,1);
   val2 = dates(dates(1,1)+3 ,1);
   
   % calculez gradul de apartanenta
   A = Apartenenta(SortR1, val1, val2);
 
   n = dates(1,1);
   % creez topul 
   for i = 1:n
    % primul for corespunde cu locul ocupat
    for j = 1:n
      % iar al doilea cu numarul nodului (site-ului) respectiv
      if (R2(j) == SortR1(i))
        fprintf(out, "%d %d %0.6f\n", i, j, A(i));
        j = n+1;
      endif
    endfor
  endfor

  fclose(out);
endfunction
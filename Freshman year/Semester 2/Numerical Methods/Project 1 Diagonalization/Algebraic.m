

function R = Algebraic(nume, d)
  
  %d eschid fisierul pentru citire
  fid = fopen(nume, "r");
  
  % ma folosesc de functia dlmread pentru a citi informatile din fisier intr-o matrice 
  dates = dlmread(fid, ' ');
  
  %inchid fisierul
  fclose(fid);
  
  % numarul de pagini se afla pe prima pozitie a matrici
  nr = dates(1,1);
  
  % pentru a parcurge legaturile arborilor pornim de la pozitile 2 si 3 ce reprezinta 
  % inceputul nodurilor ce sunt legate de primul nod
  
  i = 2;
  j = 3;
  
  % parcurg cu un i fiecare nod
  while ( i <= nr + 1 )
    % atribui intr-o variabila numarul de legaturi
    NrLegaturi = dates(i,2);
    j = 3 ;
    % parcurg linia curenta cu un j
    while ( j <= dates(i,2) + 2)
       % daca nodul are conexiune la el insusi il scad din numarul de legaturi
        if dates(i,j) == dates(i,1)
            NrLegaturi = NrLegaturi - 1;
            break;
        endif
        j = j + 1;
    endwhile
    % reinitializez j cu 3 pentru a crea matricea de adiecenta
    j = 3 ;
    
    while ( j <= dates(i,2) + 2)
        % adaug valorile matricei, inafara de nodurile care legaturi catre ele insisi
        if dates(i,j) != dates(i,1)
            MatAdiacenta(i - 1, dates(i,j) ) = 1/NrLegaturi;
        endif

        j = j + 1;
    endwhile
    
    i = i + 1;
  endwhile
  
  % creez vectorul R
  R(1:nr) = 1/ nr;
  % pentru a simplifica programul voi lucra cu transpusa acestuia
  R = R';
 
  MatAdiacenta = MatAdiacenta';
  %aplic formula de la sursa data
  vector1 = ones(nr,1);
  R = PR_Inv(eye(nr) - d * MatAdiacenta) * (((1 - d) / nr) * vector1);
  
  endfunction
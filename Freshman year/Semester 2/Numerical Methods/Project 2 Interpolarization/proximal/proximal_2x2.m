function out = proximal_2x2(f, STEP = 0.1)
    % ===================================================================================
    % Aplica Interpolare Proximala pe imaginea 2 x 2 f cu puncte intermediare echidistante.
    % f are valori cunoscute �n punctele (1, 1), (1, 2), (2, 1) ?i (2, 2).
    % Parametrii:
    % - f = imaginea ce se va interpola;
    % - STEP = distan?a dintre dou? puncte succesive.
    % ===================================================================================
    
    % TODO: Defineste coordonatele x si y ale punctelor intermediare.

    f = double(f);
    
    vec = zeros(1/STEP + 1, 1);
    
    vec(1,1) = 1;
    
    for i = 1:(1/STEP)
       vec(i+1,1) = vec(i,1) + STEP; 
       if i == fix((1/STEP + 1)/2) + 1
         vec(i,1) = vec(i,1) + STEP;
       endif
    endfor
    
    % Se afl? num?rul de puncte.
    n = length(vec);

    % TODO: Cele 4 puncte �ncadratoare vor fi aceleasi pentru toate punctele din interior.
    
    % TODO: Initializeaza rezultatul cu o matrice nula n x n.
    vec  = double(vec);
    out = zeros(n);
    out = double(out);
    % Se parcurge fiecare pixel din imaginea finala.
    for i = 1 : n
        for j = 1 : n
            % TODO: Afla cel mai apropiat pixel din imaginea initiala.
              if vec(i,1) < 1.5 && vec(j,1) < 1.5
                 out(i,j) = f(1,1);
              endif
              if vec(i,1) > 1.5 && vec(j,1) < 1.5
                 out(i,j) = f(2,1);
              endif
              if vec(i,1) > 1.5 && vec(j,1) > 1.5
                 out(i,j) = f(2,2);
              endif
              if vec(i,1) < 1.5 && vec(j,1) > 1.5
                 out(i,j) = f(1,2);
              endif
            % TODO: Calculeaza pixelul din imaginea finala.
        endfor
    endfor

endfunction
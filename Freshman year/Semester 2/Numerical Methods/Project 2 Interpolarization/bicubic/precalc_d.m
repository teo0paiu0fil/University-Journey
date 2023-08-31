function [Ix, Iy, Ixy] = precalc_d(I)
    % =========================================================================
    % Prealculeaza matricile Ix, Iy si Ixy ce contin derivatele dx, dy, dxy ale 
    % imaginii I pentru fiecare pixel al acesteia.
    % =========================================================================
    
    % Obtinem dimensiunea imaginii.
    [m n nr_colors] = size(I);
    
    Ix =zeros(m,n);
    Iy =zeros(m,n);
    Ixy =zeros(m,n);
    % TODO: Tranforma matricea I in double.

    I = double(I);
    Ix =double(Ix);
    Iy =double(Iy);
    Ixy =double(Ixy);
    for x = 1:m
       for y = 1:n
        
        % TODO: Calculeaza matricea cu derivate fata de x Ix.
       
       Ix(x,y) = fx(I, x, y);
     
        % TODO: Calculeaza matricea cu derivate fata de y Iy.
 
       Iy(x,y) = fy(I, x , y);

        % TODO: Calculeaza matricea cu derivate fata de xy Ixy.

       Ixy(x,y) = fxy(I , x, y);
       
       endfor
    endfor
    
endfunction

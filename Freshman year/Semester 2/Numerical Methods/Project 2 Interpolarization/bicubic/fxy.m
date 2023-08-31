function r = fxy(f, x, y)
    % =========================================================================
    % Aproximeaza derivata fata de x si y a lui f in punctul (x, y).
    % =========================================================================
    
    % TODO: Calculeaza derivata.
    
      [m n colour] = size(f);
    
    if x == 1 || x == m || y == 1 || y == n
      r = 0;
    else
      r=(f(x-1,y-1)+f(x+1,y+1)-f(x+1,y-1)-f(x-1,y+1))/4;
    endif
    
endfunction
function r = fy(f, x, y)
    % =========================================================================
    % Aproximeaza derivata fata de y a lui f in punctul (x, y).
    % =========================================================================
    
    % TODO: Calculeaza derivata.
    
    [m n colour] = size(f);
    
     if x ==1 || x == m
      r = 0;
    else 
      r = (f(x + 1, y) - f(x - 1, y))/2;
    endif
    
endfunction
function r = fx(f, x, y)
    % =========================================================================
    % Aproximeaza derivata fata de x a lui f in punctul (x, y).
    % =========================================================================

    % TODO: Calculeaza derivata.
    [m n colour] = size(f);
    
     if y == 1 || y == n
      r = 0;
    else
      r = (f(x, y + 1) - f(x, y - 1))/2;
    endif

endfunction
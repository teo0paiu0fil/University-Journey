function y = Apartenenta(x, val1, val2)
  
  nr = size(x,1);
  y = zeros(nr,1);
  
  a = 1 / (val2 - val1); 
  b = - val1 / (val2 - val1);
  % definitia functiei membru = gradul de apartenenta
  i = 1;
  while  i <= nr
    if ((x(i) >= val1) && (x(i) <= val2))
      y(i) = a * x(i) + b;
    else
      if ((x(i) > val2) && (x(i) <= 1))
        y(i) = 1;
      endif
      
    endif
    i= i +1 ;
   endwhile

endfunction

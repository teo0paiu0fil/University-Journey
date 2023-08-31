function B = PR_Inv(A)
	 
  %realizam Factorizarea Q R
  r = zeros(size(A));
  
	q = A;
  [dim dim]= size(A);
  
	for i = 1:dim
	  r(i, i) = norm(q(:, i));
		q(:, i) = q(:, i) / r(i, i);
		r(i, (i+1):dim) = q(:, i)' * q(:, (i+1):dim);
		q(:, (i+1):dim) -= q(:, i) * q(:, i)' * q(:, (i+1):dim);
	endfor
      
  [dim dim] = size(r);
  % rezolv ecuatile pentru calcularea inversei;
  for k = 1:dim
    b = zeros(dim ,1);	
    for i = dim:-1:1
      b(i) = (q(k,i) - r(i,i+1:dim) * b(i+1:dim)) / r(i,i);
	  endfor
     B(:,k) = b;
  endfor
  
endfunction
     
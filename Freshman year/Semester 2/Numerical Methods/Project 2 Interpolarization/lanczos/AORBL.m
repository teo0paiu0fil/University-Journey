function [Am Bm Cm] = AORBL(A,B,C,t1,t2,tol)
  
  epsilon = 1;
  m = 1;
  [x y] = size(A);
  H1 = eye(y);
  
  while epsilon > tol
    
    [Vm Wm] = lanczosRationalBlock(A,B,C);
    
    Am = Wm'*A*Vm;
    Bm = Wm'*B;
    Cm = C*Vm;
    
    H2 = H1;
    
    for i = 1:x
       for j = 1:y
         H1 = H2 - C* inv((H2(i,j) * eye(y) - A)) *(V*W'*eye(y)*A*V(:,j);
       endfor
    endfor
    
    epsilon = norm(H1(m,1) - H2(m-1,1));
    m = m + 1;
  endwhile
endfunction
function out = lanczos_nonsymetrical(A,V,W,m)
  
  W_tran = W';
  
  [Q R] = qr( W_tran' * V);
    
  V1(1) = V * inv(R);
  W1(1) = W * Q;
  V2(1) = A * V1;
  W2(1) = A' * W1;

  for i = 1:m
    a(i) = W(i)' * V2(i +1);
    V2(i+1) = V2(i+1) - V1(i) *a(i);
    W2(i+1) = W2(i+1) - W1(i) *a(i)';
    
    b = V2(i +1)/V1(i+1);
    c = W2(i +1)/W1(i+1);
    
    [U, S, Z] = svd(W(i+1)' * V(i +1));
    
    # out = ....; 
    
  endfor
  
endfunction
function A = bicubic_coef(f, Ix, Iy, Ixy, x1, y1, x2, y2)
    % =========================================================================
    % Calculeaz? coeficien?ii de Interpolare Bicubic? pentru 4 puncte al?turate
    % =========================================================================

     A = zeros(4,4);
     
    % TODO: Calculeaz? matricile intermediare.

    B = [ f(y1,x1) , f(y2,x1), Iy(y1,x1), Iy(y2,x1);
          f(y1,x2) , f(y2,x2), Iy(y1,x2), Iy(y2,x2);
          Ix(y1,x1), Ix(y2,x1), Ixy(y1,x1), Ixy(y2,x1);
          Ix(y1,x2), Ix(y2,x2), Ixy(y1,x2), Ixy(y2,x2)];
          
    C = [ 1 , 0 , 0 , 0 ;
          1 , 1 , 1 , 1 ;
          0 , 1 , 0 , 0 ;
          0 , 1 , 2 , 3 ];
          
    C = inv(C);
          
    C_trans = C';
    
    % TODO: Converte?te matricile intermediare la double.

    B = double(B);
    C = double(C);
    C_trans = double(C_trans);
    A = double(A);
    
    % TODO: Calculeaz? matricea final?.
    
    A = C * B * C_trans;
    

endfunction
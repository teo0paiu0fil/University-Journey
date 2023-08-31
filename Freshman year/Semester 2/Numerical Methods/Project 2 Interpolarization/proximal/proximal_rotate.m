function R = proximal_rotate(I, rotation_angle)
    % =========================================================================
    % Roteste imaginea alb-negru I de dimensiune m x n cu unghiul rotation_angle,
    % aplic�nd Interpolare Proximala.
    % rotation_angle este exprimat �n radiani.
    % =========================================================================
    [m n nr_colors] = size(I);
    
    % Se converteste imaginea de intrare la alb-negru, daca este cazul.
    if nr_colors > 1
        R = -1;
        return
    endif

    % Obs:
    % Atunci c�nd se aplica o scalare, punctul (0, 0) al imaginii nu se va deplasa.
    % �n Octave, pixelii imaginilor sunt indexati de la 1 la n.
    % Daca se lucreaza �n indici de la 1 la n si se inmultesc x si y cu s_x respectiv s_y,
    % atunci originea imaginii se va deplasa de la (1, 1) la (sx, sy)!
    % De aceea, trebuie lucrat cu indici �n intervalul [0, n - 1].

    % TODO: Calculeaza cosinus si sinus de rotation_angl
 
    Trot = zeros(2,2);
    
    % TODO: Initializeaza matricea finala.
     
    R = zeros(m,n);
    
    % TODO: Calculeaza matricea de transformare.

    Trot(1,1) = cos(rotation_angle);
    Trot(2,1) = sin(rotation_angle);
    Trot(1,2) = -sin(rotation_angle);
    Trot(2,2) = cos(rotation_angle);
     
    % TODO: Inverseaza matricea de transformare, FOLOSIND doar functii predefinite!
    
    Trot = inv(Trot);

    % Se parcurge fiecare pixel din imagine.
    for y = 0 : m - 1
        for x = 0 : n - 1
            % TODO: Aplica transformarea inversa asupra (x, y) si calculeaza x_p si y_p
            % din spatiul imaginii initiale.

            v = Trot * [x;y];
            
            % TODO: Trece (xp, yp) din sistemul de coordonate [0, n - 1] �n
            % sistemul de coordonate [1, n] pentru a aplica interpolarea.

            v(1,1) = v(1,1) + 1;
            v(2,1) = v(2,1) + 1;
            
            % TODO: Daca xp sau yp se afla �n exteriorul imaginii,
            % se pune un pixel negru si se trece mai departe.
            
            if v(1,1) < 1 || v(1,1) > n || v(2,1) < 1 || v(2,1) > m
               R(y+1,x+1) = 0;
            else 
            % TODO: Afla punctele ce �nconjoara(xp, yp).
 
            a = ceil(v(1,1));
            b = floor(v(1,1));
            c = ceil(v(2,1));
            d = floor(v(2,1));
            
            vec_coef = proximal_coef(I,b,d,a,c);
 
            % TODO: Calculeaza coeficientii de interpolare notati cu a
            % Obs: Se poate folosi o functie auxiliara �n care sau se calculeze coeficientii,
            % conform formulei.
            
            % TODO: Calculeaza valoarea interpolata a pixelului (x, y).
            
            R(y+1,x+1) = vec_coef(1,1) + vec_coef(2,1)*v(1,1) + vec_coef(3,1)*v(2,1) + vec_coef(4,1)*v(1,1)*v(2,1);
            
            endif
        endfor
    endfor

    % TODO: Transforma matricea rezultata �n uint8 pentru a fi o imagine valida.
    R = uint8(R);
    
endfunction

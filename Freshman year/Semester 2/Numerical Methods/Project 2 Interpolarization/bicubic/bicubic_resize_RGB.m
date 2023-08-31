function out = bicubic_resize_RGB(img, p, q)
    % =========================================================================
    % Redimensioneaza imaginea img astfel �nc�t aceasta save fie de dimensiune p x q.
    % Imaginea img este colorata.
    % =========================================================================

    % TODO: Extrage canalul rosu al imaginii.
    Red = img(:,:,1);
    
    % TODO: Extrage canalul verde al imaginii.
    
    Green = img(:,:,2);
    
    % TODO: Extrage canalul albastru al imaginii.
    
    Blue = img(:,:,3);
   
    % TODO: Aplica functia bicubic pe cele 3 canale ale imaginii.
    R = bicubic_resize(Red, p, q);
    
    G = bicubic_resize(Green, p, q);
    
    B = bicubic_resize(Blue, p, q);
    % TODO: Formeaza imaginea finala concatenand cele 3 canale de culori.

    out = cat(3 , R ,G ,B);
    
endfunction

function out = proximal_resize_RGB(img, p, q)
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
    
    % TODO: Aplica functia proximal pe cele 3 canale ale imaginii.
    
    R = proximal_resize(Red, p ,q);
    G = proximal_resize(Green, p, q);
    B = proximal_resize(Blue, p, q);
    
    out = cat(3, R, G, B);
    
    % TODO: Formeaza imaginea finala concaten�nd cele 3 canale de culori.

endfunction

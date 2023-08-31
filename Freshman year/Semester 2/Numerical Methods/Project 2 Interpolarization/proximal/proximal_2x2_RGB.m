function out = proximal_2x2_RGB(img, STEP = 0.1)
    % ==============================================================================================
    % Aplica Interpolare Proximala pe imaginea 2 x 2 definita img cu puncte intermediare echidistante.
    % img este o imagine colorata RGB -Red, Green, Blue.
    % =============================================================================================

    % TODO: Extrage canalul rosu al imaginii.
    
    Red = img(:,:,1);
    
    % TODO: Extrage canalul verde al imaginii.
    
    Green = img(:,:,2);
    
    % TODO: Extrage canalul albastru al imaginii.
    
    Blue = img(:,:,3);
    
    % TODO: Aplic? functia proximal pe cele 3 canale ale imaginii.
    
    R = proximal_2x2(Red,STEP);
    G = proximal_2x2(Green,STEP);
    B = proximal_2x2(Blue,STEP);
   
    % TODO: Formeaza imaginea finala concaten�nd cele 3 canale de culori.
    
    out = cat(3, R, G, B);
    
endfunction

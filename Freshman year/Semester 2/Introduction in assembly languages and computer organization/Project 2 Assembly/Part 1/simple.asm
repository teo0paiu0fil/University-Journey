%include "../include/io.mac"
;; Paiu Teofil 313 CB
section .text
    global simple
    extern printf

simple:
    ;; DO NOT MODIFY
    push    ebp
    mov     ebp, esp
    pusha

    mov     ecx, [ebp + 8]  ; len
    mov     esi, [ebp + 12] ; plain
    mov     edi, [ebp + 16] ; enc_string
    mov     edx, [ebp + 20] ; step

    ;; DO NOT MODIFY
   
    ;; Your code starts here

    while:
        xor eax, eax                    ;; initializez eax cu 0
        mov al , byte [esi + ecx - 1]   ;; aduc la registurul al caracterul pe care urmeaza sa il modific
        add al , dl                     ;; adun pasul
        cmp al , 'Z'                    ;; verific daca pasul depaseste Z
        jle skip
        sub al, byte 26                 ;; revin in range-ul literelor
    skip:
        mov byte [edi + ecx - 1], al    ;; adaug rezultatul in enc_string
    loop while

    ;; Your code ends here
    
    ;; DO NOT MODIFY

    popa
    leave
    ret
    
    ;; DO NOT MODIFY

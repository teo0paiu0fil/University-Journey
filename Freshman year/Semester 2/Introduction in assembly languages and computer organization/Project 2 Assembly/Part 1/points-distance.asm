%include "../include/io.mac"
;; Paiu Teofil 313 CB
struc point
    .x: resw 1
    .y: resw 1
endstruc

section .text
    global points_distance
    extern printf

points_distance:
    ;; DO NOT MODIFY
    
    push ebp
    mov ebp, esp
    pusha

    mov ebx, [ebp + 8]      ; points
    mov eax, [ebp + 12]     ; distance
    ;; DO NOT MODIFY
   
    ; Your code starts here

    push eax                         ;; pun valoarea eax pe stiva sa nu o pierd
    xor eax, eax                     ;; initializez cu 0 registrele
    xor edx, edx
    mov dx, word [ebx]
    mov ax, word [ebx + point_size ] ;; iau componenta x din ambele structuri 
    cmp dx, ax
    je CalculezY                     ;; verific daca sunt egale sa aflu daca sunt pe OX sau OY
    cmp dx, ax
    jg calc1    
    xchg dx, ax                      ;; verific daca x2 e mai mare fata de x1 si le interschimb
    jmp calc1    
CalculezY: 
    mov dx, word [ebx + point_size + point.x + point.y]
    mov ax, word [ebx + point.x + point.y]  ;; iau y din ambele structuri
    cmp dx, ax
    jg calc1    
    xchg dx, ax                      ;; verific daca y2 e mai mare fata de y1 si le interschimb
calc1:
    sub dx, ax                       ;; scad valoarea mai mica din cea care e mai mare        
    pop eax

    mov [eax], edx                   ;; returnez rezultatul in distance

    ;; DO NOT MODIFY
    popa
    leave
    ret

    ;; DO NOT MODIFY
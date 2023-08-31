%include "../include/io.mac"
;; Paiu Teofil 313 CB
struc point
    .x: resw 1
    .y: resw 1
endstruc

section .text
    global road
    extern printf

road:
    ;; DO NOT MODIFY
    push ebp
    mov ebp, esp
    pusha

    mov eax, [ebp + 8]      ; points
    mov ecx, [ebp + 12]     ; len
    mov ebx, [ebp + 16]     ; distances
    ;; DO NOT MODIFY
   
    ;; Your code starts here
    
    ;; folosesc aceasi implementare de la task-ul precedent doar ca parcurg 
    ;; fiecare structura din vector
    ;; returnand len -1 destinati
while:
    push ebx
    xor edx, edx
    xor ebx, ebx
    mov dx, word [eax + point_size*(ecx - 1) + point.x ]
    mov bx, word [eax + point_size*(ecx - 2) + point.x]
    cmp dx, bx
    je CalculezY
    cmp dx, bx
    jg calc1
    xchg dx, bx
    jmp calc1   
    CalculezY:
        mov dx, word [eax + point_size*(ecx - 1) + point.x + point.y]
        mov bx, word [eax + point_size*(ecx - 2) +point.x + point.y] 
        cmp dx, bx
        jg calc1
        xchg dx, bx
    calc1:
    sub dx, bx
    pop ebx
    mov [ebx+ point_size*(ecx - 2)], edx
    cmp ecx, 2
    je out
    loop while
out:


    ;; Your code ends here
    
    ;; DO NOT MODIFY
    popa
    leave
    ret
    ;; DO NOT MODIFY
%include "../include/io.mac"
;; Paiu Teofil 313 CB
section .text
    global spiral
    extern printf
section .data
    col dd 0
    row dd 0
    top dd 0
    bottom dd 0
    left dd 0
    right dd 0
    dir dd 0
    dim dd 0



; void spiral(int N, char *plain, int key[N][N], char *enc_string);
spiral:
    ;; DO NOT MODIFY
    push ebp
    mov ebp, esp
    pusha

    mov eax, [ebp + 8]  ; N (size of key line)
    mov ebx, [ebp + 12] ; plain (address of first element in string)
    mov ecx, [ebp + 16] ; key (address of first element in matrix)
    mov edx, [ebp + 20] ; enc_string (address of first element in string)
    ;; DO NOT MODIFY
    ;; TODO: Implement spiral encryption
    ;; FREESTYLE STARTS HERE

    mov [top], dword 0   ;; top reprezinta limta la linia de sus din matrice
    mov [left], dword 0    ;; left reprezinta limta la coloana din stanga din matrice
    mov [col],  eax     ;; col reprezinta coloanele din matrice
    mov [row],  eax         ;; row reprezinta coloanele din matrice
    dec eax
    mov [bottom], eax        ;; bottom reprezinta limta la linia de jos din matrice
    mov [right], eax         ;; right reprezinta limta la coloana din dreapta din matrice
    inc eax
    mov [dir], dword 1      ;; dir reprezinta parcurgerea  in sensul acelor de ceasornic
    mul eax       
    mov [dim], eax          ;; dim reprezinta elementele din matrice
    xchg eax, ecx
    xor ecx, ecx

while:

    push edx        ;; dau push pe stack la enc_string
    xor edx, edx       
    xor eax,eax

    cmp [dir], dword 1  ;; realizez operatile
    jpe for1
    cmp [dir], dword 2
    jpe for2
    cmp [dir], dword 3
    jpe for3
    cmp [dir], dword 4
    jpe for4

for1:
    mov edx, dword [top]
    inc edx
    mov [top], edx
    mov [dir], dword 2
    jmp continue
for2:
    mov edx, dword [right]
    dec edx
    mov [right], edx
    mov [dir], dword 3
    jmp continue
for3:
    mov edx, dword [bottom]
    dec edx
    mov [bottom], edx
    mov [dir], dword 4
    jmp continue
for4:
    mov edx, dword [left]
    inc edx
    mov [left], edx
    mov [dir], dword 1
continue:
    pop edx
    add ecx, dword 1
    mov eax, dword [dim]
    cmp ecx, eax
    jne while



    ;; FREESTYLE ENDS HERE
    ;; DO NOT MODIFY
    popa
    leave
    ret
    ;; DO NOT MODIFY

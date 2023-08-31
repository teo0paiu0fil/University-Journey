%include "../include/io.mac"
;; Paiu Teofil 313 CB
section .text
    global beaufort
    extern printf
section .data
    string: times 201 db 0
    len dd 0
; void beaufort(int len_plain, char *plain, int len_key, char *key, char tabula_recta[26][26], char *enc) ;
beaufort:

    push ebp
    mov ebp, esp
    pusha

    mov eax, [ebp + 8]  ; len_plain
    mov ebx, [ebp + 12] ; plain (address of first element in string)
    mov ecx, [ebp + 16] ; len_key
    mov edx, [ebp + 20] ; key (address of first element in matrix)
    mov edi, [ebp + 24] ; tabula_recta
    mov esi, [ebp + 28] ; enc

    ;; TODO: Implement spiral encryption
    ;; FREESTYLE STARTS HERE

    xchg eax,ecx ;; ecx va acea len_plain
    push edi
    push esi
    push eax
    xor edi, edi
    xor esi,esi
    mov [len], ecx ;; salvez intr-o variabila aceasta valoare
   
while:
    pop eax
    cmp eax, edi   
    jg caz2
    xor edi, edi
caz2:
    push eax
    mov al, byte [edx + edi] ;; construiesc cheia de repetand-o
    mov byte [string + esi], al ;; pana cand aceasta e de aceasi marime a plain text
    inc esi 
    inc edi
    loop while ;; parcurg while pana cand ecx e 0 

    pop eax
    pop esi
    pop edi
    mov ecx, [len] ;; restitui valoarea lui ecx
    
while2:
    xor edx, edx
    xor eax, eax
    mov al, byte [ebx + ecx -1] ;; scot litera din plain text
    push ebx 
    xor ebx, ebx 
    mov bl, byte [string + ecx -1] ;; scot litera din key
    cmp eax, ebx ;; verific care valoare este mai mare
    jl skip
    jg skip2
skip:               ;; in functie de rezultat am ajuns la formule
    sub bl, al              ;; pentru key mai mare decat plain scad key plain
    xchg bl, al             ;; interschimb valorile
    add al , byte 65        ;; si adaug "A"
    jmp aici                
skip2:
    sub al, bl          ;; pentru key mai mic ca plain scand din plain key
    mov bl, byte 90     ;;  scad din 'Z' valoare respectiva
    sub bl, al          ;; interschimb registrele
    xchg al, bl
    inc al
aici:
    mov byte [esi + ecx - 1], al    ;;pun rezultatul in sirul encriptat
    pop ebx
    loop while2


    ;; FREESTYLE ENDS HERE

    popa
    leave
    ret
  

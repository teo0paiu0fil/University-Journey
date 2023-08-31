%include "../include/io.mac"
;; Paiu Teofil 313 CB
section .text
    global is_square
    extern printf

is_square:
    ;; DO NOT MODIFY
    push ebp
    mov ebp, esp
    pusha

    mov ebx, [ebp + 8]      ; dist
    mov eax, [ebp + 12]     ; nr
    mov ecx, [ebp + 16]     ; sq
    ;; DO NOT MODIFY
   
    ;; Your code starts here

    push ecx    ;; mut valoarea in stiva
    mov ecx, eax    ;; si din eax in ecx pentru a utiliza functia loop
    xor eax, eax    ;; initializez eax = 0
    
while:
    mov edx, dword [ ebx + 4 * (ecx - 1)] ;; mut valoarea din vector edx
    push ebx
    push ecx
    mov ebx, edx  ;; mut valoarea vectorului in edx
    mov ecx, 0  ;; fac ecx 0
for:
    mov eax, ecx    ;; mut ecx in eax pentru a calcula puterea
    inc ecx         ;; cresc ecx
    mul eax
    cmp eax, ebx    ;; daca elementul nostru este mai mic decat patratul perfect si nu 
    jz este            ;; a fost niciodata egal cu o putere caculata pun 0 on vector
    cmp eax, ebx
    jl for
nu_este:
    pop ecx
    pop ebx
    pop eax
    mov [eax + 4*(ecx-1)], dword 0  ;; numarul a intrat pe acest label deci nu e patratperfect
    push eax
    push ecx
    jmp afara     ;; ies afara din loop-ul de puteri
este:
    pop ecx
    pop ebx
    pop eax 
    mov [eax + 4*(ecx-1)], dword 1    ;; numarul este patrat perfect
    push eax
    push ecx
afara:
    pop ecx
    loop while
    pop ecx

    ;; Your code ends here
    
    ;; DO NOT MODIFY
    popa
    leave
    ret
    ;; DO NOT MODIFY
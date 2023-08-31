section .text
	global cpu_manufact_id
	global features
	global l2_cache_info

;; void cpu_manufact_id(char *id_string);
;
;  reads the manufacturer id string from cpuid and stores it in id_string

cpu_manufact_id:
	enter 0, 0
	mov esi, [ebp + 8]
	
	push ebx ;; deoarece ebx este folosit de apelul functiei printf si in el
	;; se afla sirul de formatare care inca nu a fost pus pe stiva deoarece se asteapta
	;; valoarea de return a functiei noastre trebuie sa pastram valoarea acestuia

	mov eax, dword 0 ; mut in eax 0 deoarece doresc sa aflu informati despre manufacturer
	cpuid

	mov dword [esi], ebx        ;; constuiesc numele dezvoltatorului
	mov dword [esi + 4], edx
	mov dword [esi + 8], ecx

	pop ebx	;; restitui ebx

	leave
	ret

;; void features(int *apic, int *rdrand, int *mpx, int *svm)
;
;  checks whether apic, rdrand and mpx / svm are supported by the CPU
;  MPX should be checked only for Intel CPUs; otherwise, the mpx variable
;  should have the value -1
;  SVM should be checked only for AMD CPUs; otherwise, the svm variable
;  should have the value -1

features:
	enter 	0, 0
	mov esi, [ebp + 8] ; preiau valoarea de return pentru apic

	push ebx ;; deoarece ebx este folosit de apelul functiei printf si in el
	;; se afla sirul de formatare care inca nu a fost pus pe stiva deoarece se asteapta
	;; valoarea de return a functiei noastre trebuie sa pastram valoarea acestuia

	mov eax, dword 1
	cpuid 

	push ecx ; deoarece vrem sa verificam RDRAND mai tarziu
	mov ecx, 0x0200 ; al 9-lea bit din registrul edx indica daca acesta foloseste APIC sau nu
	add edx, ecx	; asadar vreau sa aplic and logic si sa testez aceasta
	test edx, edx
	jnz else
	mov dword [esi], dword 0 ; returnez 0 daca in urma testului valoarea e  zero
	jmp skip
else:
	mov dword [esi], dword 1 ; returnez 1 daca in urma testului valoarea e diferita de zero
skip:

	mov esi, [ebp + 12] ; preiau valoarea de return pentru rdrand

	pop ecx 
	push ecx
	mov ebx, 0x40000000 ; al 30-lea bit din registrul ecx 
	and ecx, ebx  ; testetz bitul
	test ecx, ecx
	jnz else1
	mov dword [esi], dword 0 ; returnez 0 daca in urma testului valoarea e  zero
	jmp skip1
else1:
	mov dword [esi], dword 1 ; returnez 1 daca in urma testului valoarea e diferita de zero
skip1:

	mov esi, [ebp +16]; preiau valoarea de return pentru MPX
	pop ecx
	mov ebx, 0x1000 ; al 13-lea bit din registrul ecx indica daca suporta mpx sau nu
	and ecx, ebx  ; testetz bitul
	test ecx, ecx
	jnz else2
	mov dword [esi], dword 0 ; returnez 0 daca in urma testului valoarea e  zero
	jmp skip2
else2:
	mov dword [esi], dword 1 ; returnez 1 daca in urma testului valoarea e diferita de zero
skip2:

	mov esi, dword [ebp + 20] ; preiau valoarea pentru SVM

	mov eax, 0x80000001 ;; valoare luata din manualul AMD din task4.md
	cpuid
	
	mov ebx, 0x4 ; al 2-lea bit din registrul ecx indica daca suporta SVM sau nu
	and ecx, ebx  ; testetz bitul
	test ecx, ecx
	jnz else3
	mov dword [esi], dword 0 ; returnez 0 daca in urma testului valoarea e  zero
	jmp skip3
else3:
	mov dword [esi], dword 1 ; returnez 1 daca in urma testului valoarea e diferita de zero
skip3:

	pop ebx ;; restaurez ebx

	leave
	ret

;; void l2_cache_info(int *line_size, int *cache_size)
;
;  reads from cpuid the cache line size, and total cache size for the current
;  cpu, and stores them in the corresponding parameters
l2_cache_info:
	enter 	0, 0

	push ebx ; pastrez valoarea lui ebx pe stiva
	mov eax, 0x80000006 ; valoarea luata din task4.md

	cpuid

	mov esi, dword [ebp + 8] ;; line_size

	push ecx
	mov edx, 0xff ; imi creez o masca de tipul 00000000 00000000 00000000 11111111 pentru a afla 
	and ecx, edx ; valoarea liniei
	mov dword [esi], ecx ; pun valoarea liniei in primul parametru

	mov esi, dword [ebp + 12] ; cache size 
	
	pop ecx; restaurez ecx
	mov edx, 0xffffffff ; echivalentul a 11111111 11111111 11111111 11111111 pentru a afla memory cache-ul
	add ecx, edx
	shr ecx, 16 ; deoarece avem rezultatul in partea dreapta al registrului
	mov dword [esi], ecx ; pun valoarea cache in al doilea parametru

	pop ebx ; precum am spus si mai sus oastrez valoarea lui ebx

	leave
	ret

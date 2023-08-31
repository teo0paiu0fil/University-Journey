section .text
	global cmmmc

section .data
	string db "aex ceva blabla %d", 10 , 0

extern printf
;; int cmmmc(int a, int b)
;
;; calculate least common multiple fow 2 numbers, a and b
cmmmc:
	pop edi; adresa de return a functie in main
	pop eax; a
	pop edx; b
	push edx
	push eax ; restituim stiva
	push edi
	
	push ebx ;; deoarece ebx este folosit de apelul functiei printf si in el
	;; se afla sirul de formatare care inca nu a fost pus pe stiva deoarece se asteapta
	;; valoarea de return a functiei noastre trebuie sa pastram valoarea acestuia

	;; implementez algoritmul lui euclid

	push edx
	push eax	
	pop ecx ; a
	pop ebx ; b

while:
	cmp eax, edx ; daca eax(b) este mai mic decat edx(b) atunci valoare lui eax va fi egala cu
	je skip		 ; eax(a) + ecx(a)
	jg else      
	add eax, ecx 
	jmp skip
else:			; atfel valuarea lui edx(b) creste cu ebx(b)
	add edx, ebx
skip:
	cmp eax, edx ; in final vom avea cmmmc si in eax si in edx
	jne while

	pop ebx ;; restitui sirul de formatare

	ret

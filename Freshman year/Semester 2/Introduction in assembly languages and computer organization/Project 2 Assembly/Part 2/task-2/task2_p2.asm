section .text
	global par

;; int par(int str_length, char* str)
;
; check for balanced brackets in an expression
par:
	pop edi ; adresa de return a functie in main
	pop edx ; str_lenght
	pop esi ; *str
	push esi
	push edx ; restituim stiva
	push edi
	
	push ebx ; deoarece ebx este folosit de apelul functiei printf si in el
	; se afla sirul de formatare care inca nu a fost pus pe stiva deoarece se asteapta
	; valoarea de return a functiei noastre trebuie sa pastram valoarea acestuiA

	xor ecx, ecx
	push ecx ;; pun pe stiva valoarea 0 daca voi scoate 0 vreodata voi considera "stiva vida"
while:
	cmp byte [esi + ecx], "(" ; verific daca paranteza este deschisa
	je skip1
	pop ebx 	;; altfel imi asum ca este una inchisa si scot de pe stiva 
	cmp bl, byte 0	;; daca elementul scos  este 0 opresc executia si returnez 0
	je false
	jmp skip2
skip1:
	push dword 40 ; in acest caz o introduc pe stiva ASCII 40 --> "("
skip2:
	inc ecx    ;; parcurg sirul in continuare
	cmp ecx, edx
	jne while

	pop ebx		; dupa ce am terminat de parcurs sirul verific daca urmatorul
	cmp bl, byte 0 ; element de pe stiva e 0 daca da inseama ca secventa e corecta
	je true

while2:			; altfel scot restul de elemente pana ajung la 0 si returnez 0
	pop ebx
	cmp bl, byte 0
	jne while2
false:
	push dword 0 ; return 0
	pop eax
	jmp afara
true:
	push dword 1 ; return 1
	pop eax
afara:
	pop ebx ; restitui sirul de formatare

	ret

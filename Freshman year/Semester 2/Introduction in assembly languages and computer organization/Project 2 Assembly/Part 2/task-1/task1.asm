section .text
	global sort

section .data
	mysring2 db "Valoarea lui eax pentru primul nod %d", 10, 0

; struct node {
;     	int val;
;    	struct node* next;
; };

extern printf

;; struct node* sort(int n, struct node* node);
; 	The function will link the nodes in the array
;	in ascending order and will return the address
;	of the new found head of the list
; @params:
;	n -> the number of nodes in the array
;	node -> a pointer to the beginning in the array
; @returns:
;	the address of the head of the sorted list
sort:
	enter 0, 0

	push ebx
	push ecx
	push edx ; pastrez valoarea registrelor pe stiva deoarece am primit de cateva ori segfault in printf 
	push esi ; si tind sa cred ca e din cauza ca modific unul dintre registre
	push edi

	mov eax, [ebp + 12] ;; mut primul nod in eax
	mov ecx, [ebp + 8] ;; mut numarul de elemente in ecx
	
	mov ebx, dword 1 ; cautarea incepe de la primul element
	mov esi, ecx ; salvez in esi numarul de elemente din vector sa stiu de cate ori trebuie a parcurg urmatorele operati

cauta:
	lea edx, [eax + (ecx - 1) * 8] ; preiau adresa ultimului element
	cmp ecx, dword 0 ; verific daca am terminat vectorul
	je aici
	dec ecx ; decrementez iteratorul
	cmp ebx, dword [edx] ; verific daca este elementul pe care il caut
	jne cauta	; daca nu atunci creez un while
	je leaga ; daca da atunci leg nodurile
aici:
	inc ebx ;in caz ca am terminat vectorul incrementez elementul pe care il caut
	jmp cauta

prima:
	push edx ; daca e primul nod in pun pe stiva ca la finalul iteratilor sa ii dau po in eax
	add edx, dword 4 ; adaug patru sa ajung la poinbterul next din stuctura
	push edx ; pun pe stiva adresa acestui element
	inc ebx ; trec la urmatorul element pe care il caut
	dec esi ; scad numarul total de elemente 
	mov ecx, [ebp + 8] ; reiau ecx de pe stiva ca sa parcurg tot vectorul din nou
	jmp cauta

leaga:
	cmp dword [edx], dword 1 ; daca acesta este primul element se sare mai sus
	je prima 
	lea ecx , [edx] ; iau adresa acestuia
	pop edi ; dau pop la node.next a elementul anterior
	mov dword [edi], ecx ;mut in ea adresa elementului curent
	add edx, dword 4 ; ma duc la node.next pentru elementul curent
	push edx ; il pun pe stiva pentru urmatorul element
	mov ecx, [ebp + 8] ; reiau ecx sa parcurg din nou vectorul
	inc ebx ;trec la urmatorul element cautat
	dec esi ; scad numarul total de elemente
	cmp esi, dword 0 ; verific daca acesta nu a ajuns la final
	jg cauta

	pop eax ;scot node.next al ultimului element din lista
	pop eax ; preiau primul element

	pop edi
	pop esi
	pop edx
	pop ecx
	pop ebx
	
	leave
	ret

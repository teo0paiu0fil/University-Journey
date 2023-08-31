global get_words
global compare_func
global sort

section .text

extern strtok
extern strcmp
extern strlen
extern qsort

section .data
    delimitatori db " ", 10, 0

;; sort(char **words, int number_of_words, int size)
;  functia va trebui sa apeleze qsort pentru soratrea cuvintelor 
;  dupa lungime si apoi lexicografix
sort:
    enter 0, 0
    
    




    leave
    ret

;; get_words(char *s, char **words, int number_of_words)
;  separa stringul s in cuvinte si salveaza cuvintele in words
;  number_of_words reprezinta numarul de cuvinte
get_words:
    enter 0, 0
    mov esi, dword [ebp + 8] ;; *s
    mov edx, dword [ebp + 12] ;; **words
    mov ecx, dword [ebp + 16] ;; number_of_words

    push delimitatori
    push esi
    call strtok

    push

    leave
    ret

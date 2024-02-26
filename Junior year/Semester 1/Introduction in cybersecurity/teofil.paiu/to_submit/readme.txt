Paiu Teofil 333CB

Task: Crypto-attack
    - dupa examinarea fisierului am dedus ca avem o encryptare RSA care a fost transformata
    in base64
    - dupa decode-ul din base64 a message.txt am descoperit ca am cheia publica dar si mesajul criptat 

    "flag": AQG5WV6dHTNgONLTrgke3Ar/U4Y3OJBDr6sIm9QAinGOCC/g4JC5oOY64aFcKxkmEWwhsRtgNgB+nLvxV2/u4/ax+EnMd
    L4fERtqRjPRZdyqZxj+rHG9uaQR924qy6WiB1vVTRlFI0783HkgT02pJQC3ED/dILuAISLEPnAkXteyXquDgesT8f+L+kM4FP+oeN
    EKtOp1vIkbotnsA9aAwBh9WITjEiqcheWGogeqQWk4qokAV23/1NWDgfPqNLlgPMwNnulVCxEo1o/LKJWp1e2tQerIj9kuCVujy0bi
    Omica2aSgzqauqeKyMwU+B1Ini9RI7ezoDd0K0mTGKCHMQAQ

    - primul hint imi spune deja ce am aflat: RSA chosen ciphertext attack (caesar pe ascii table ca alfabet)
    - al doilea hit aflat la fel ca primul: https www.youtube.com watch?v dQw4w9WgXcQ (nice joke)

    - dupa niste research pe Choosen Cipher Attack si am urmat un model in care inmultesc la cifru valoarea 2^e % nice
    iar dupa impart de la rezultat valoarea 2 si primesc mesajul decriptat operatile au fost facute in
    fisierul script_crypto.py

Task: linux-acl
    - cum am intrat in sistem am cautat un sa vad daca exita un fisier numit flag => find / -name "*flag*"  2> /dev/null 
    cu raspunsul => /.you.are.never/.gonnafindthis/ouflagfrumos

    - la inspectare cu ls am vazut ca are + pentru ACL asa ca am rulat => getfacl ouflagfrumos  de unde 
    am vazut ca e detinut de root dar si ul alt utilizator care are drepturi pe el ci anume user:elboss:rwx

    - am folosit hitul cu implementarea propriului sudo si am am cautat =>  find / -name "*sudo*" 2> /dev/null
    cu rezultatul /usr/local/bin/robot-sudo
    - dupa un cat pe acesta si inspectarea fisierului am gasit -> /etc/.extra/hidden/r0b0t3rs.conf unde se pare
    ca exista o lista de comenzi permise pe utilizatori

    - dupa o cautare care ar fi fost frumos sa se intample cu cateva ore inainte sa incep cerinta si am gasit /var/.hints.txt,
    iar acum pus pe calea cea buna cu o inspectarea cu ltrace am aflat ca comparatia se realizeaza cu strncmp pe dimensiune liniei
    citite deci pot crea un alt script cu numele vacuum_control1 care sa lanseze un shell ca utilizatorul iamrobot conectat ca si 
    iamrobot am rulat bo5s.exe si am primit acces denied, la o inspectare cu strigs am vazut un string aratand ca un hash,
    iar in objdump se vedea ca compara primul argument cu acesta iar dupa rulare am optinut flagul
    
    comanda finala a fost 'robot-sudo /usr/games/hunt/manele/b05s.exe 05b17838011414662f9f977b841d7acf'

Task: binary-exploit
    - dupa niste rulari si o inspectie in ghidra am realizat ca trebuie sa apelez funtia win cu un parametru egal cu lucky_number 
    - dupa ce analizez mai bine main si loop am realizat ca lucky number se recalculeaza si afiseaza de fiecare data cand accept continuarea
    jocului
    - zis si facum pasi mei erau rularea unei partide sub 13 numere ( dimensiunea buffarului meu), continuarea jocului, attack-ul in sine
    - attackul propriu zis era suprascrierea adresei de return a loop-ului (eip) astfel incat sa continue cu executia functiei win
    - paddingul de care am avut nevoie e adresa bufferului (din objdump [esp + esi * 4 + 0x38] -> 0xffffd090) - save_eip (info frame -> 0xffffd0ec) 
    care este egal cu 92 si avand vedere ca citesc unsigned int-uri 92 / 4 = 23 de byte urmati de adresa functiei win (din objdump) urmata
    de suprascrierea lui ebp si dupa de parametrul functie win
    input-ul ce a provocat bof a fost '1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 20 1 2 3 134517302 0 ${valoarea data a lui lucky_number dupa primul loop}'
    - valoare 134517302 este reprezentarea in decimal a adresei lui win


    
    




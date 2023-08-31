Paiu Teofil 323CB

Pentru multiplexare am ales sa folosesc API poll și sa aștept pentru evenimente POLLIN
Am creat 2 functii ajutătoare pentru crearea și binding-ul socketurilor de tcp și udp pe care le
apelez în main.
Serverul se ocupa cu parcarea mesajelor, clientul primește deja contentul pe care trebuie sa îl afișeze. Am doi vectori globali, clienți cunoscuți și topicurile valabile dacă un client se abonează la un topic se va parca o referință a acestuia și valoarea de la sf în caz de sf = 1 și clientul e deconectat curs_fd = -1 toate mesajele sunt puse intr-o coada urmând sa o gonesc când se conectează 
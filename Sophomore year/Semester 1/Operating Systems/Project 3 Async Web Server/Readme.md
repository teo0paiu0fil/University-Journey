# Server Web 
### Paiu Teofil 323CB

## Implementare 
Pentru realizarea temei m-am inspirat din sample-urile puse la dispozitie din resursele temei ci mai precis `epoll_echo_server.c` si `test_get_request_path.c`.
Am realizat doar partea de fisiere statice dar cu mici dificulati la clientul http wget prin debugging am observat ca nu primesc toata cererea de la acesta la rularea checker-ului dar daca rulez executabilul local si manual pe masina de so nu am avut aceasta problema. Din lipsa de timp si find aglomerat nu am mai abordat rezolvarea acestei probleme

## Functionare
Serverul asteapta si asculta la portul 8888 pentru conexiuni dupa ce sa realizat una cu ajutorul epoll-ului realizam comunicarea intre acesta si client.

Cu functile handler_client_request preiau cererea clientului si o trec prin parser-ul http pus la dispozitie pentru a optine calea fisierului solicitat.

Urmand sa trimit un mesaj de inaintare daca cererea este valida sau nu, iar mai apoi fisierul.
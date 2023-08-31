FLAGS= -DDEBUG -DLOG_LEVEL=LOG_DEBUG -Wall -Wextra -g -laio -fPIC -I.

.PHONY = all build clean

build: aws

aws: aws.o sock_util.o http_parser.o
	gcc $^ -o $@ $(FLAGS)

aws.o: server.c
	gcc -c server.c -o $@ $(FLAGS)

sock_util.o: sock_util.c
	gcc -c sock_util.c -o $@ $(FLAGS)
	
clean:
	rm -rf aws sock_util.o aws.o

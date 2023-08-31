FLAGS = -g -Wall -Wextra 
CC = gcc

.PHONY: all server subscriber clean

all: server subscriber

server: server.o
	$(CC) $< -o $@ -lm $(FLAGS)

server.o: server.c utils.h
	$(CC) -c $<  $(FLAGS)

subscriber: subscriber.o
	$(CC) $< -o $@ -lm $(FLAGS)

subscriber.o: subscriber.c utils.h
	$(CC) -c $<  $(FLAGS)

clean:
	rm -rf *.o subscriber server

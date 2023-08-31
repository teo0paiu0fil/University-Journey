CC=gcc
CFLAGS=-I.

client: client.c requests.c helpers.c buffer.c
	$(CC) -o $@ $^ -Wall -g

run: client
	./client

clean:
	rm -f *.o client

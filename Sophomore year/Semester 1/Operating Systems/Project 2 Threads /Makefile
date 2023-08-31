CC = gcc
CFLAGS = -Wall -fPIC -g

.PHONY: build
build: libscheduler.so

libscheduler.so: so_scheduler.o
	$(CC) $(CFLAGS) -shared -o $@ $^ 

so_scheduler.o: ./src/so_scheduler.c ./util/so_scheduler.h
	$(CC) $(CFLAGS) -o $@ -c $<

.PHONY: clean
clean:
	rm -f exec_parser.o loader.o libso_loader.so
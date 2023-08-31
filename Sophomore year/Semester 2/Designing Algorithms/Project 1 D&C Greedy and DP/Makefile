CC = g++
CCFLAGS = -Wall -Wextra -std=c++17 -O0 -lm

.PHONY: build clean

build: ./nostory ./semnale ./sushi ./feribot

run-p1:
	./feribot
run-p2:
	./nostory
run-p3:
	./sushi
run-p4:
	./semnale

./feribot: feribot.cpp
	$(CC) -o $@ $^ $(CCFLAGS) -g
./nostory: nostory.cpp
	$(CC) -o $@ $^ $(CCFLAGS) -g
./sushi: sushi.cpp
	$(CC) -o $@ $^ $(CCFLAGS) -g
./semnale: semnale.cpp
	$(CC) -o $@ $^ $(CCFLAGS) -g

# Vom șterge executabilele.
clean:
	rm -f ./feribot ./nostory ./sushi ./semnale

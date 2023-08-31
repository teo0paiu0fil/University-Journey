all: build

build: kCliqueToSAT.class
	cp main.sh main && chmod +x main

kCliqueToSAT.class:
	javac kCliqueToSAT.java

clean:
	rm -rf main *.class
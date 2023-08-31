CCFLAGS := -std=c++17 -Wall -Wextra -O0 -lm -g
JFLAGS := -Xlint:unchecked

SRC_DIR := .
OUT_DIR := .

CC_SRC := $(wildcard $(SRC_DIR)/*.cpp)
JAVA_SRC := $(wildcard $(SRC_DIR)/*.java)

CC_EXECS := $(CC_SRC:$(SRC_DIR)/%.cpp=$(OUT_DIR)/%)
JAVA_CLASSES := $(JAVA_SRC:$(SRC_DIR)/%.java=$(OUT_DIR)/%.class)
TARGETS := $(CC_EXECS) $(JAVA_CLASSES)


.PHONY: build clean

build: $(TARGETS)

clean:
	rm -f $(TARGETS) $(OUT_DIR)/*.class

run-p1:
	./$(OUT_DIR)/supercomputer
run-p4:
	./$(OUT_DIR)/magazin

$(CC_EXECS): $(OUT_DIR)/%: $(SRC_DIR)/%.cpp
	g++ -o $@ $^ $(CCFLAGS)

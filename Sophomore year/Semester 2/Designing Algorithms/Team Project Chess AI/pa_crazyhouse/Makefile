# Define compiler and flags
JC = javac
JFLAGS = -g

# Define source and build directories
SRCDIR = src
BUILDDIR = build

# Define source files
SOURCES = $(wildcard $(SRCDIR)/*.java)

# Define object files
OBJECTS = $(SOURCES:$(SRCDIR)/%.java=$(BUILDDIR)/%.class)

# Define executable
EXECUTABLE = pa_crazyhouse

# Build target
build: $(EXECUTABLE)

$(EXECUTABLE): $(OBJECTS)
	javac -g -d build src/*.java


$(BUILDDIR)/%.class: $(SRCDIR)/%.java
	$(JC) $(JFLAGS) -d $(BUILDDIR) $<

# Run target
run:
	java -classpath $(BUILDDIR) src.Main

# Clean target
clean:
	rm -rf build/

.PHONY: build run clean

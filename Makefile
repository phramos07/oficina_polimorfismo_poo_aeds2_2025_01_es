# Compilador
JC = javac
# JVM
JVM = java
# Arquivos fonte
SOURCES = src/produto/Produto.java src/produto/ProdutoPerecivel.java src/produto/ProdutoNaoPerecivel.java src/App.java
# Arquivos .class (compilados)
CLASSES = $(SOURCES:.java=.class)

# Target default - compile and run
all: compile
	cd src && $(JVM) App

compile: $(SOURCES)
	mkdir -p bin
	$(JC) -d bin $(SOURCES)

# Limpar .class
clean:
	rm -rf bin

.PHONY: all compile clean

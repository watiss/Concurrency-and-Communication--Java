.SUFFIXES: .p.java .java .class

TAR=tar
## Utiliser /usr/local/bin/tar dans le PATH ou bien faire TAR=gtar

JAVAPREP=cpp -x c
PREPFLAGS=-C -P
JAVACFLAGS=

SOURCES=\
Actor.java\
Agent.java\
BoundedBuffer.java\
Channel.java\
Context.java\
Main.java\
Message.java\
Node.java\
Proxy.java\
SemBoundedBuffer.java\
Semaphore.java\

CLASSES=$(SOURCES:.java=.class)

%.java: %.p.java
	$(JAVAPREP) $(PREPFLAGS) $< $@

%.class: %.java
	javac $(JAVACFLAGS) $<

default : $(CLASSES) 
all: $(CLASSES) index.html

$(CLASSES): $(SOURCES)

student:
	make

teacher:
	make PREPFLAGS="$(PREPFLAGS) -DTEACHER=true"

index.html: index.texi
	makeinfo --no-headers --html --ifinfo --no-split --css-include=style.css $< > $@

clean:
	-rm *~ *.class

force:
	cvs update -l *.texi
	sleep 2
	cvs update -l

install: force veryclean
	make student
	tar cf src.tar $(SOURCES) nodes Makefile
	gzip -f src.tar
	chmod g=u * Makefile
	chmod o-rwx *
	chmod o=u-w $(SOURCES) index.html style.css src.tar.gz

veryclean: clean
	-rm $(SOURCES)

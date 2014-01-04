.SUFFIXES: .p.java .java .class .idl

JAVAPREP=cpp -x c
PREPFLAGS=-C -P
JAVACFLAGS=

IDL=Peer

IDLFILE=$(IDL).idl

IDLSOURCES=\
_$(IDL)Stub.java\
$(IDL)POATie.java\
$(IDL)POA.java\
$(IDL)Operations.java\
$(IDL)Holder.java\
$(IDL)Helper.java\
$(IDL).java\

JAVASOURCES=\
$(IDL)Impl.java\
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

SOURCES=\
$(IDLSOURCES)\
$(JAVASOURCES)\

CLASSES=$(SOURCES:.java=.class)

%.java: %.p.java
	$(JAVAPREP) $(PREPFLAGS) $< $@

%.class: %.java
	javac $(JAVACFLAGS) $<

default : ref $(CLASSES)

all: ref $(CLASSES) index.html

ref:
	$(RM) *.ref

$(CLASSES): $(SOURCES)

$(IDLSOURCES): $(IDLFILE)
	idl $(IDLFILE)
	javac $(IDLSOURCES)

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
	tar fc src.tar $(JAVASOURCES) $(IDLFILE) nodes Makefile
	gzip -f src.tar
	chmod g=u * Makefile
	chmod o-rwx *
	chmod o=u-w $(SOURCES) index.html style.css src.tar.gz

veryclean: clean
	-rm $(SOURCES)

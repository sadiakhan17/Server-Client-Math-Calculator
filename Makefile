JAVAC = javac
JAVA = java

#java code files
SERVER_CODE = TCPServer.java
CLIENT_CODE = TCPClient.java

#the classes (compiled)
SERVER_CLASS = TCPServer.class
CLIENT_CLASS = TCPClient.class

#target so make compiles both
all: server client

server: $(SERVER_CLASS)

client: $(CLIENT_CLASS)

$(SERVER_CLASS): $(SERVER_CODE)
	$(JAVAC) $(SERVER_CODE)

$(CLIENT_CLASS): $(CLIENT_CODE)
	$(JAVAC) $(CLIENT_CODE)

#running each of the files
run-server: server
	$(JAVA) TCPServer

run-client: client
	$(JAVA) TCPClient

#delete the class so they can be recompiled
clean:
	rm -f $(SERVER_CLASS) $(CLIENT_CLASS)

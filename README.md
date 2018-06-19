# pre-message

pre-message is the server of an encrypted messaging application. It is written in Clojure to take advantage of its interoperability with Java, its functional paradigm and its LISP macros. It uses a re-encryption proxy to distribute de message between users, the proxy uses an AFGH scheme, by using the NICS Crypto library.

This server is just a proof of concept, it uses a mock sqlite3 database and may not be ready for production usage.

## Installation

Clone this repository and compile with leiningen to an uberjar:

```
lein uberjar
```

## Usage

After compilation the project will be found in the target directory. To run use the java runtime environment. Java should be 9 or greater. It is necessary to add the `java.xml.bind` module.

    $ java --add-modules java.xml.bind -jar target/uberjar/pre-message-0.1.0-standalone.jar [args]

It will open the server in the 8080 port.

## License

Distributed under the Eclipse Public License either version 1.0

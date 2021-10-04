keytool -v -list -storepass WLrWINQ1nX2lz95aP9mGN94 -keystore key.p12
keytool -delete -alias default -storepass WLrWINQ1nX2lz95aP9mGN94 -keystore key.p12
keytool -delete -alias testKey2 -storepass WLrWINQ1nX2lz95aP9mGN94 -keystore key.p12

openssl req -x509 -newkey rsa:4096 -sha256 -nodes -subj '/CN=localhost' -addext "subjectAltName = DNS:localhost,IP:127.0.0.1" -days 365 -keyout tls.key -out tls.crt
# openssl req -x509 -newkey rsa:4096 -sha256 -nodes -subj '/CN=localhost' -addext "subjectAltName = DNS:localhost,IP:127.0.0.1" -days 365 -out cert.pem -keyout cert.pem
openssl pkcs12 -export -inkey tls.key -in tls.crt -out server.p12 -name testKey1
pass: changeit

keytool -importkeystore -deststorepass WLrWINQ1nX2lz95aP9mGN94 -destkeypass WLrWINQ1nX2lz95aP9mGN94 -destkeystore key.p12 -srckeystore server.p12 -srcstoretype PKCS12 -srcstorepass changeit -alias testKey1
keytool -importcert -file tls.crt -alias "testKey2" -storepass WLrWINQ1nX2lz95aP9mGN94 -keystore key.p12

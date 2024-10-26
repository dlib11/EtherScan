Istruzioni per eseguire l'applicazione
======================================
Per eseguire l'applicazione è necessario avere installato Docker e Docker Compose. 
\
Non è necessario installare Postgres in quanto è già presente nel docker-compose.yml.

L'ApiKey di Etherscan è già presente nel Dockerfile
```:
1. Scaricare il progetto con il comando git-clone
2. Posizionarsi nella cartella src da terminale
3. Eseguire il comando ./mvnw clean package -D skipTests 
    Verificare di avere i permessi di esecuzione sul file mvnw, altrimenti eseguire il comando chmod +x mvnw
4. Eseguire il comando docker build -t ethscanner .
5. Posizionarsi con il terminale in src/main/docker
6. Eseguire il comando docker-compose up

L'applicazione sarà disponibile all'indirizzo http://localhost:9090
```


Istruzioni per eseguire i test
==============================

1. Endpoint per aggiungere un indirizzo
```:Endpoint
 POST http://localhost:9090/api/v1/addAddress
 
 body: {"address": "0x0063ec2896db3d25194667c9b5ccb69bd860b928"}
```
Chiamata da fare con Curl o Postman
```
curl -X POST http://localhost:9090/api/v1/addAddress \
  -H "Content-Type: application/json" \
  -d '{"address": "0x0063ec2896db3d25194667c9b5ccb69bd860b928"}'
```




2. Endpoint per ottenere la lista delle transazioni ed il balance
```:Endpoint 
 GET http://localhost:9090/api/v1/transactions
 
 Parametri:
    address: Indirizzo di cui si vuole ottenere le transazioni
    order: Ordinamento delle transazioni (asc o desc)
    page: Pagina da visualizzare
    size: Numero di transazioni per pagina
```
Chiamata da fare con Curl o Postman
```
curl -X GET "http://localhost:9090/api/v1/transactions?address=0x0063ec2896db3d25194667c9b5ccb69bd860b928&order=desc&page=0&size=40"
```


Sviluppi futuri
==============================

1. Implementare un sistema di autenticazione per l'accesso alle API
2. Utilizzare il modulo WebFlux e la programmazione reattiva per gestire le chiamate asincrone

Problematiche riscontrate sul modello
==============================
1. Se carichiamo due volte la stessa transazione per due indirizzi diversi il modello va in errore poichè è chiave primaria

Si potrebbe risolvere creando una tabella di join tra transazioni e indirizzi però ho preferito attenermi al modello stabilito
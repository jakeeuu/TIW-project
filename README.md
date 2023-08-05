# Progetto di Tecnologie Informatiche per il Web 2023

### voto: 30/30

- ####  Chiara Auriemma &nbsp;([@ChiaraAuriemma](https://github.com/ChiaraAuriemma))<br> 10722613&nbsp;&nbsp; chiara.auriemma@mail.polimi.it

- ####  Giacomo Ballabio &nbsp;([@Jackeeuu](https://github.com/jakeeuu))<br> 10769576&nbsp;&nbsp; giacomo2.ballabio@mail.polimi.it

<br>

### Traccia progetto:

### Versione HTML pura

Un’applicazione di commercio elettronico consente all’utente (acquirente) di visualizzare un catalogo di prodotti venduti da diversi fornitori, inserire prodotti in un carrello della spesa e creare un ordine di acquisto a partire dal contenuto del carrello. Un prodotto ha un codice (campo chiave), un nome, una descrizione, una categoria merceologica e una foto. Lo stesso prodotto (cioè codice prodotto) può essere venduto da più fornitori a prezzi differenti. Un fornitore ha un codice, un nome, una valutazione da 1 a 5 stelle e una politica di spedizione. Un utente ha un nome, un cognome, un’e-mail, una password e un indirizzo di spedizione. La politica di spedizione precisa il prezzo della spedizione in base al numero di articoli ordinati. Ogni fornitore è libero di definire fasce di spesa. Una fascia di spesa ha un numero minimo, un numero massimo e un prezzo. Ad esempio: da 1 a 3 articoli 15€, da 4 a 10 articoli 20€, oltre a 10 articoli,  ecc. Oltre alla fascia di spesa, il fornitore può anche indicare un importo in euro oltre al quale la spedizione è gratuita. Se il totale supera la soglia per la gratuità della spedizione, la spedizione è gratuita indipendentemente dal numero di articoli. Dopo il login, l’utente accede a una pagina HOME che mostra (come tutte le altre pagine) un menù con i link HOME, CARRELLO, ORDINI, un campo di ricerca e una lista degli ultimi cinque prodotti visualizzati dall’utente. Se l’utente non ha visualizzato almeno cinque prodotti, la lista è completata con prodotti in offerta scelti a caso in una categoria di default. L’utente può inserire una parola chiave di ricerca nel campo di input e premere INVIO. A seguito dell’invio compare una pagina RISULTATI con prodotti che contengono la chiave di ricerca nel nome o nella descrizione. L’elenco mostra solo il codice, il nome del prodotto e il prezzo minimo di vendita del prodotto da parte dei fornitori che lo vendono (lo stesso prodotto può essere  venduto da diversi fornitori a prezzi diversi e l’elenco mostra il minimo valore di tali prezzi). L’elenco è ordinato in modo crescente in base al prezzo minimo di vendita del prodotto da parte dei fornitori che lo offrono. L’utente può selezionare mediante un click un elemento dell'elenco e visualizzare nella stessa pagina i dati completi e l’elenco dei fornitori che lo vendono a vari prezzi (questa azione rende il prodotto “visualizzato”). Per ogni fornitore in tale elenco compaiono: nome, valutazione, prezzo unitario, fasce di spesa di spedizione, importo minimo della spedizione gratuita e il numero dei prodotti e valore totale dei prodotti di quel fornitore che l’utente ha già messo nel carrello. Accanto all’offerta di ciascun fornitore compare un campo di input intero (quantità) e un bottone METTI NEL CARRELLO. L’inserimento nel carrello di una quantità maggiore di zero di prodotti comporta l’aggiornamento del contenuto del  carrello e la visualizzazione della pagina CARRELLO. Questa mostra i prodotti inseriti, raggruppati per fornitore. Per ogni fornitore nel carrello si vedono la lista dei prodotti, il prezzo totale dei prodotti e il prezzo della spedizione calcolato in base alla politica del fornitore. Per ogni fornitore  compare un bottone ORDINA. Premere il bottone comporta l’eliminazione dei prodotti del fornitore dal carrello e la creazione di un ordine corrispondente. Un ordine ha un codice, il nome del fornitore, l’elenco dei prodotti, un valore totale composto dalla somma del valore dei prodotti e delle spese di spedizione, una data di spedizione e l’indirizzo di spedizione dell’utente. I valori degli attributi di un ordine sono memorizzati esplicitamente nella base di dati indipendentemente dai dati del carrello. In ogni momento l’utente può accedere tramite il menu alle pagine HOME, ORDINI e CARRELLO. La pagina ORDINI mostra l’elenco ordinato per data decrescente degli ordini con tutti i dati associati.
L’applicazione NON salva il carrello nella base di dati ma solo gli ordini.

<br>

### Versione con JavaScript

Si realizzi un’applicazione client server web che estende e/o modifica le specifiche precedenti come segue:

-	Dopo il login dell’utente, l’intera applicazione è realizzata con un’unica pagina.
-	Ogni interazione dell’utente è gestita senza ricaricare completamente la pagina, ma produce l’invocazione asincrona del server e l’eventuale modifica del contenuto da aggiornare a seguito dell’evento.
-	L’applicazione memorizza il contenuto del carrello a lato client.
-	Nella pagina RISULTATI l’elenco dettagliato dei prodotti già nel carrello da parte di un fornitore compare mediante una finestra sovrapposta quando si passa con il mouse sopra il numero che indica quanti     prodotti del medesimo fornitore sono già nel carrello.




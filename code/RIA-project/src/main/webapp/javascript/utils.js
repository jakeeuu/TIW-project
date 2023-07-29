
function makeCall(method, url, formElement, callBack , objectToSend , reset = true) {
    let request = new XMLHttpRequest(); // crea una richiesta
    request.onreadystatechange = function() {
        callBack(request);
    };
    //opening the request con Get o Post
    request.open(method, url);

	// controlla se ci sono dati da inviare
    if (formElement == null && objectToSend == null) {
        request.send();
    } else if(formElement != null){
		var formdata = new FormData(formElement);
		console.log(formdata.get("mail")); 
		console.log(formdata.get("password")); 
        request.send(formdata); //invia i dati del form
    } else {
        let toSend = JSON.stringify(objectToSend); //se è stato fornito objectToSend, invia una richiesta con i dati JSON
        request.send(toSend);
    }

    if (formElement !== null && reset === true) {
        formElement.reset();
    }
}




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
        request.send(new FormData(formElement)); //invia i dati del form
    } else {
		request.setRequestHeader("Content-Type", "application/json");
        let toSend = JSON.stringify(objectToSend);
        request.send(toSend);
    }

    if (formElement !== null && reset === true) {
        formElement.reset();
    }
}



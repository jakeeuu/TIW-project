
function makeCall(method, url, formElement, callBack , objectToSend , reset = true) {
    let request = new XMLHttpRequest();
    request.onreadystatechange = function() {
        callBack(request);
    };
    
    request.open(method, url);


    if (formElement == null && objectToSend == null) {
        request.send();
    } else if(formElement != null){
        request.send(new FormData(formElement));
    } else {
		request.setRequestHeader("Content-Type", "application/json");
        let toSend = JSON.stringify(objectToSend);
        request.send(toSend);
    }

    if (formElement !== null && reset === true) {
        formElement.reset();
    }
}



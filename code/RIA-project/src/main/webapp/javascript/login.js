(function() {
    document.getElementById("login").addEventListener("submit", (e) =>{
		e.preventDefault();
        let form = e.target;
        console.log(form); //utile per il debuggin

        if(form.checkValidity() === true){
            makeCall("POST", 'CheckLogin', form, function (req) {
                if(req.readyState === XMLHttpRequest.DONE){
                    let message = req.responseText;
                    switch(req.status){
						case 200:
							sessionStorage.setItem('mail', message);
                			window.location.href = "Market.html";
                			break;
              			case 400: // bad request
            				document.getElementById("error").textContent = message;
                			break;
              			case 401: // unauthorized
            				document.getElementById("error").textContent = message;
							break;
              			case 500: // server error
            				document.getElementById("error").textContent = message;
                			break;
                	}
            	}
            }, null, false);
            
        }else{  
            form.reportValidity();
        }
        });
	})();
	
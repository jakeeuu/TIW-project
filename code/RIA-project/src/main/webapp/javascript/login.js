(function() {
    document.getElementById("loginButton").addEventListener('click', (e) =>{
		e.preventDefault();
		sessionStorage.removeItem("cart");
		var form = e.target.closest("form");

        if(form.checkValidity() === true){
            makeCall("POST", 'CheckLogin', e.target.closest("form"), function (req) {
                if(req.readyState === XMLHttpRequest.DONE){
                    let message = req.responseText;
                    switch(req.status){
						case 200:
							sessionStorage.setItem("mail", message);
                			window.location.href = "Market.html";
                			break;
              			case 400:
            				document.getElementById("error").textContent = message;
                			break;
              			case 500:
            				document.getElementById("error").textContent = message;
                			break;
                	}
            	}
            });
            
        }else{  
            form.reportValidity();
        }
        });
	})();
	
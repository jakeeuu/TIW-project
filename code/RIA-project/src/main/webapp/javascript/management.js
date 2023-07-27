(function () {

	var pageOrchestrator = new PageOrchestrator();

	window.addEventListener("load", () => {
		if (sessionStorage.getItem("username") == null) {
			window.location.href = "LoginPage.html";
		} else {			
			pageOrchestrator.start();
			pageOrchestrator.refresh();
		} 
	}, false);




	function PageOrchestrator(){
		var alert = document.getElementById("id_alert");



		this.start = function () {
			

			/**
			 * 
			 * HOME PAGE
			 * 
			 */
			visualizeProduct = new VisualizeProduct(alert, document.getElementById("prodVisTable"), document.getElementById("prodVisBody"));
			searchForm = new SearchForm(document.getElementById("searchForm"), alert);
			searchForm.registerEvents(this);

			/**
			 * 
			 * RESULT PAGE
			 * 
			 */
			visualizeSearchProduct = new VisualizeSearchProduct(alert, document.getElementById("tableResults"), document.getElementById("bodyResults"));

		}
	}




	function VisualizeProduct(alertIn, containerIn, bodyIn){
		this.alert = alertIn;
		this.container = containerIn;
		this.body = bodyIn;

		this.reset = function (){
			this.container.style.visibility = "hidden";
		}

		this.show = function(){
			let self = this;
			makeCall("GET", "GoToHome", null,
				function(req) {

					if (req.readyState == 4) {
						let message = req.responseText;
						if (req.status == 200) {
							let products = JSON.parse(req.responseText);
							self.update(products); 
						}else if(req.status == 403){
							window.location.href = req.getResponseHeader("Location");
                  			window.sessionStorage.removeItem('mail');
						}
					} else {
						self.alert.textContent = "Something went wrong while exchanging messages with the server";
					}

				}
			);	
		}


		this.update = function(product){

			let row, cell, img;
			this.body.innerHTML=""; // cosa serve ?? svuota il contenuto della tabella ???

			let self = this;

			products.forEach(function(p){

				row = document.createElement("tr");

					cell = document.createElement("td");
					cell.textContent = p.code;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = p.name;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = p.description;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = p.category;
					row.appendChild(cell);

					cell = document.createElement("td");
					img = document.createElement("img");
					img.src = "imgstiww\\" + p.photo; // al posto di imgstiww metto la cartella in cui tengo le foto (chiedo a fra)

					img.alt = "image";
					img.width = 200;///tengo??
					img.height = 150;///tengo??

					cell.appendChild(img);

				row.appendChild(cell);

				self.body.appendChild(row);
			});

			this.container.style.visibility = "visible";
		};

	}	



	function SearchForm(searchFormIn, alertIn){
		this.searchForm = searchFormIn;
		this.alert = alertIn;

		this.show = function(){
			var fieldsets = document.querySelectorAll("#" + this.searchForm.id + " fieldset");
			fieldsets[0].hidden = false;
		}

		this.reset = function(){
			var fieldsets = document.querySelectorAll("#" + this.searchForm.id + " fieldset");
			fieldsets[0].hidden = true;
		}
		
		this.registerEvents = function(orchestrator) {
			this.searchForm.querySelector("input[type='button'].submit").addEventListener('click', (e) => {
				var eventfieldset = e.target.closest("fieldset");
				if(eventfieldset.elements[0].checkValidity()){
					var self = this;
					makeCall("POST", "Result?key_word=" + /*aggiungo qui la chiave di ricerca del form*/, e.target.closest("form"),
						function(req) {
							if (req.readyState == 4) {
								var message = req.responseText;
								if (req.status == 200) {
									let products = JSON.parse(req.responseText);
									if (products.length == 0) {
										self.alert.textContent = "You don't have any product to visualize";
										return;
									}
									//gestisco orchestrator, result con quello che mi arriva come risultato dalla chiave di ricerca
									//con l'orchestrato 
								}
								if (req.status == 403) {//qual'è la differenza tra questo errore e quello sotto??
									window.location.href = req.getResponseHeader("Location");
									window.sessionStorage.removeItem('mail');
								}
								else if (req.status != 200) {
									self.alert.textContent = "Error - some fields weren't completed correctly";
									self.reset();
								}
							} else {
								self.alert.textContent = "Something went wrong while exchanging messages with the server";
							}
						}
					);
				}else{
					eventfieldset.elements[0].reportValidity();
				}
			}	
		}
	}


	function VisualizeSearchProduct(alertIn, containerIn, bodyIn){
		this.alert = alertIn;
		this.container = containerIn;
		this.body = bodyIn;

		this.reset = function(){
			this.container.style.visibility = "hidden";
		}

		this.updateProduct = function(products){
			var row, cell, linkcell, anchor;
			this.body.innerHTML="";
			
			var self = this;
 
			products.forEach(function(p){

				row = document.createElement("tr");
				row.setAttribute("id", p.code);

					linkcell = document.createElement("td");
			        anchor = document.createElement("a");
			        linkcell.appendChild(anchor);
			        linkText = document.createTextNode("p.code");
			        anchor.appendChild(linkText);

			        anchor.setAttribute('product_code',  p.code);
			     	anchor.addEventListener("click", (e) => {
						self.showDetails(e.target.getAttribute("product_code"),products);
					}, false);
			     	anchor.href = "#";//non so a che serve ma tutti lo mettono

				row.appendChild(linkcell);

					linkcell = document.createElement("td");
			        anchor = document.createElement("a");
			        linkcell.appendChild(anchor);
			        linkText = document.createTextNode("p.name");
			        anchor.appendChild(linkText);

			        anchor.setAttribute('product_code',  p.code);
			     	anchor.addEventListener("click", (e) => {
						self.showDetails(e.target.getAttribute("product_code"));
					}, false);
			     	anchor.href = "#";//non so a che serve ma tutti lo mettono

			    row.appendChild(linkcell);

			    	cell = document.createElement("td");
					cell.textContent = p.minimumPrice;
				row.appendChild(cell);
				self.body.appendChild(row);


			});
			this.container.style.visibility = "visible";
		}


		this.showDetails = function(productCode,products) {
			var self = this;
			makeCall("POST", "ProductDetails?product_code=" + productCode,null,
				function(req) {
					if (req.readyState == 4) {
						let message = req.responseText;
						if (req.status == 200) {
							let suppliers = JSON.parse(req.responseText);
							if (suppliers.length == 0) {
								self.alert.textContent = "You don't have any supplier to visualize";
								return;
							}
							self.updateSupplier(products, suppliers, productCode); 
						}
						if (req.status == 403) {//qual'è la differenza tra questo errore e quello sotto??
							window.location.href = req.getResponseHeader("Location");
							window.sessionStorage.removeItem('mail');
						}
						else if (req.status != 200) {
							self.alert.textContent = "Error - some fields weren't completed correctly";
							self.reset();
						}
					} else {
						self.alert.textContent = "Something went wrong while exchanging messages with the server";
					}
				}
			);
		}


		this.updateSupplier = function (products, suppliers, productCode){
			var row, cell, img;
			this.body.innerHTML="";
			
			var self = this;

			var rightRow = document.getElementById("productCode"); 

			i = 0;
			while(products[i].code != productCode){
				i++
			}

			var rightProduct = products[i];

			var detailsRow = document.getElementById("moreDetails"); 

			var text = document.getElementById("description");
			text.textContent = rightProduct.description; //funziona?? fare check
			detailsRow.appendChild(text);

			/*
				var cell = document.createElement("tr");
				cell.textContent = rightProduct.category;
			detailsRow.appendChild(cell);

				var cell = document.createElement("tr");
				cell.textContent = rightProduct.photo;
			detailsRow.appendChild(cell);

			var nextRow = rightRow.nextElementSibling;
			rightRow.parentNode.insertBefore(detailsRow, nextRow);
			*/  // anche qui si sostituisce con la roba nel template


			suppliers.forEach(function(s){
				/*
				row = document.
				row.setAttribute("id", "sup" + s.code);  //lo metto pure io magari è utile 

					var cell = document.createElement("td");
					cell.textContent = s.name;
				row.appendChild(cell);

					var cell = document.createElement("td");
					cell.textContent = s.score;
				row.appendChild(cell);

					var cell = document.createElement("td");
					cell.textContent = s.unitaryPrice;
				row.appendChild(cell);

					var cell = document.createElement("td");
					cell.textContent = s.totalProductsPrice;
				row.appendChild(cell);

					var cell = document.createElement("td");
					cell.textContent = s.totalProductsPrice;
				row.appendChild(cell);

					var cell = document.createElement("td");
					cell.textContent = s.totalNumber;
				row.appendChild(cell);

					var cell = document.createElement("td");
					cell.textContent = s.freeShipping;
				row.appendChild(cell);

				supplier.spendingRanges.forEach(function(s){
					var cell = document.createElement("td");
					cell.textContent = 
    						
				}
				*/ // penso di prendere direttamente dal template

				
			});
			this.container.style.visibility = "visible";				
		}

	}


})();



			
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


			/**
			 * 
			 * CART PAGE
			 * 
			 */
			visualizeCartProduct = new VisualizeCartProduct(alert, document.getElementById("cartTable"),document.getElementById("bodyCart"));

			/**
			 * 
			 * ORDER PAGE
			 * 
			 */
			visualizeOrderProduct = new VisualizeOrderProduct(alert, document.getElementById("cartTable"),document.getElementById("bodyCart"));
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
						}else if(req.status == 400){
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
					img.src = p.photo; 

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
								if (req.status == 400) {//qual'è la differenza tra questo errore e quello sotto??
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
						if (req.status == 400) {//qual'è la differenza tra questo errore e quello sotto??
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
			var rightRow, cell, text,des, img, rightProduct ,detailsRow, nextRow, row, el1, el2, el3,el4;
			this.body.innerHTML="";
			
			var self = this;

			rightRow = document.getElementById("productCode"); 

			i = 0;
			while(products[i].code != productCode){
				i++
			}

			rightProduct = products[i];

			detailsRow = document.createElement("tr"); 

			cell = document.createElement("td");
			des = document.cretateElement("b");
			des.textContent = "description:";
			cell.appendChild(des);
			text = document.createElement("p");
			text.textContent = rightProduct.description; 
			cell.appendChild(text);
			detailsRow.appendChild(cell);

			cell = document.createElement("td");
			des = document.cretateElement("b");
			des.textContent = "category:";
			cell.appendChild(des);
			text = document.createElement("p");
			text.textContent = rightProduct.category; 
			cell.appendChild(text);
			detailsRow.appendChild(cell);

			cell = document.createElement("td");
			text = document.createElement("img");
			text.src = rightProduct.photo; 

			nextRow = rightRow.nextElementSibling;
			rightRow.parentNode.insertBefore(detailsRow, nextRow);


			suppliers.forEach(function(s){
				row = document.createElement("tr"); 
				row.setAttribute("id", "sup" + s.code);  //lo metto pure io magari è utile 
				
				cell = document.createElement("td");
				cell.colspan = "3";
				row.appendChild(cell);
				
				//-----------------------------
				
				el1 = document.createElement("p");
				cell.appendChild(el1);
				
				el2 = document.createElement("b");
				el2.textContent = "Supplier name: ";
				el1.appendChild(el2);
				
				el2 = document.createElement("span");
				el2.textContent = s.name;
				el1.appendChild(el2);
				
				//--------------------------------------------
				el1 = document.createElement("p");
				cell.appendChild(el1);
				
				el2 = document.createElement("b");
				el2.textContent = "score: ";
				el1.appendChild(el2);
				
				el2 = document.createElement("span");
				el2.textContent = s.score;
				el1.appendChild(el2);
				
				el2 = document.createElement("b");
				el2.textContent = "price: ";
				el1.appendChild(el2);
				
				el2 = document.createElement("span");
				el2.textContent = s.unitaryPrice;
				el1.appendChild(el2);
				
				el2 = document.createElement("span");
				el2.textContent = "$";
				el1.appendChild(el2);
				
				el2 = document.createElement("b");
				el2.textContent = "Total in cart: ";
				el1.appendChild(el2);
				
				el2 = document.createElement("span");
				el2.textContent = s.totalProductsPrice;
				el1.appendChild(el2);
				
				el2 = document.createElement("span");
				el2.textContent = "$";
				el1.appendChild(el2);
				
				//---------------------------------------------
				
				el1 = document.createElement("p");
				cell.appendChild(el1);
				
				el2 = document.createElement("b");
				el2.textContent = "Product already in cart:  ";
				el1.appendChild(el2);
				
				el2 = document.createElement("span");
				el2.textContent = s.totalNumber;
				el1.appendChild(el2);
				
				//---------------------------------------------
				
				el1 = document.createElement("p");
				cell.appendChild(el1);
				
				el2 = document.createElement("b");
				el2.textContent = "Minimum price for free shipping: ";
				el1.appendChild(el2);
				
				el2 = document.createElement("span");
				el2.textContent = s.freeShipping;
				el1.appendChild(el2);
				
				el2 = document.createElement("span");
				el2.textContent = "$";
				el1.appendChild(el2);
				
				//------------------------------------------------
				
				el1 = document.createElement("b");
				el1.textContent = "Spending ranges: ";
				cell.appendChild(el1);
					
				el1 = document.createElement("ul");
				cell.appendChild(el1);
				
				suppliers.spendingRanges.forEach(function(s){
					el2 = document.createElement("li");
					el1.appendChild(el2);
					
					el3 = document.createElement("p");
					el2.appendChild(el3);
					
					el4 = document.createElement("span");
					if(s.minimumN !== s.maximumN){
						el4.textContent = s.minimumN;    
						el3.appendChild(el4);
						
						el4 = document.createElement("span");
						el4.textContent = "-";
						el3.appendChild(el4);
						
						el4 = document.createElement("span");
						el4.textContent = s.maximumN;
						el3.appendChild(el4);
					}else{
						el4.textContent = "More than";    
						el3.appendChild(el4);
						
						el4 = document.createElement("span");
						el4.textContent = s.minimumN;
						el3.appendChild(el4);
					}
					
					el4 = document.createElement("span");
					el4.textContent = "products =";
					el3.appendChild(el4);
					
					el4 = document.createElement("span");
					el4.textContent = s.price;
					el3.appendChild(el4);
					
					el4 = document.createElement("span");
					el4.textContent = "$";
					el3.appendChild(el4);
					
					el1 = document.createElement("form");
					el1.id="formForAddToCart";
					cell.appendChild(el1);	
					
					el2 = document.createElement("input");
					el2.type="number";
					el2.name = "quantity";
					el1.appendChild(el2);
					
					el2 = document.createElement("input");
					el2.type="submit";
					el2.name = "Add to Cart";
					el1.appendChild(el2);			
					
					//TO DO: AGGIUNGERE EVENT LISTENER PER LA FORM
				});
				
				
			});
			this.container.style.visibility = "visible";				
		}

	}


	function VisualizeCartProduct(alertIn, containerIn, bodyIn){
		this.alert = alertIn;
		this.container = containerIn;
		this.body = bodyIn;

		this.reset = function() {
			this.container.style.visibility = "hidden";
		}

		this.show = function(){
			/////TODO
		}

		this.update = function(){
			var row, cell, form, button;
			this.body.innerHTML="";
			
			var self = this;
			cart.forEach(function(cs){

				row = document.createElement("tr");

					cell = document.createElement("td");
					cell.textContent = cs.code;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = cs.name;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = cs.totalPrice;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = cs.shippingPrice;
					row.appendChild(cell);

					//metto bottone
					cell = document.createElement("td");

						form = document.createElement("form");
						form.setAttribute("action","#");

							button = document.createElement("button");
							button.setAttribute("supplier_code",cs.code);
							button.textContent = "Order";
							button.addEventListener("click", (e) => {
								self.createOrder(e.target.getAttribute("supplier_code"));
							}, false);
							form.appendChild(button);

						cell.appendChild(form);

					row.appendChild(cell);	

				self.body.appendChild(row);

				var paragraph,span,bold;
				cs.products.forEach(function(p){
					row = document.createElement("tr");

						cell = document.createElement("td");
						cell.setAttribute("colspan",3);

							paragraph = document.createElement("p");

							span = document.createElement("span");
							span.textContent = p.quantity;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " x ";
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = p.name;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " ( product code: ";
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = p.code;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " ) ";
							paragraph.appendChild(span);

						cell.appendChild(paragraph);	


						cell = document.createElement("td");
						cell.setAttribute("colspan",2);

							bold = document.createElement("b");
							bold.textContent = p.price;

						cell.appendChild(bold);

							paragraph = document.createElement("p");

							span = document.createElement("span");
							span.textContent = p.price;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " $";
							paragraph.appendChild(span);

						cell.appendChild(paragraph);	
					self.body.appendChild(row);
				});


			});
		}


		this.createOrder = function(supplierCode) {
			var self = this;
			for(var cartSupplier : cart){
				if(cartSupplier.code === supplierCode){
					break;
				}
			}
			var JSONvalue = JSON.stringify(cartSupplier);

			makeCall("POST", "CreateOrder", null,
				function(req) {
					if (req.readyState == 4) {
						let message = req.responseText;
						if (req.status == 200) {
							let orders = JSON.parse(req.responseText);
							if (orders.length == 0) {
								self.alert.textContent = "You don't have any supplier to visualize";
								return;
							}
							//tolgo cartSupplier da carrello
							/////quando mi arriva la risposta chiamo l'orchestrator che mi mostra la order page 
						}
						if (req.status == 400) {
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
				},JSONvalue
			);
		}
		
	}

	function VisualizeOrderProduct(alertIn, containerIn, bodyIn){
		this.alert = alertIn;
		this.container = containerIn;
		this.body = bodyIn;

		this.reset = function() {
			this.container.style.visibility = "hidden";
		}

		this.show = function(){
			/////TODO
		}

		this.update = function(){
			var row, cell;
			this.body.innerHTML="";
			
			var self = this;
			orders.forEach(function(order){

				row = document.createElement("tr");

					cell = document.createElement("td");
					cell.textContent = order.code;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = order.supplierName;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = order.totalPrice;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = order.date;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = order.address;
					row.appendChild(cell);


				self.body.appendChild(row);

				var paragraph,span,bold;
				order.products.forEach(function(p){
					row = document.createElement("tr");

						cell = document.createElement("td");
						cell.setAttribute("colspan",3);

							paragraph = document.createElement("p");

							span = document.createElement("span");
							span.textContent = p.quantity;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " x ";
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = p.name;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " ( product code: ";
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = p.code;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " ) ";
							paragraph.appendChild(span);

						cell.appendChild(paragraph);	


						cell = document.createElement("td");
						cell.setAttribute("colspan",2);

							bold = document.createElement("b");
							bold.textContent = p.price;

						cell.appendChild(bold);

							paragraph = document.createElement("p");

							span = document.createElement("span");
							span.textContent = p.price;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " $";
							paragraph.appendChild(span);

						cell.appendChild(paragraph);	
					self.body.appendChild(row);
				});


			});
		}
	}
	
})();



			
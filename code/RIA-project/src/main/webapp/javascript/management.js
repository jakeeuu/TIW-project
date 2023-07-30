{

	var pageOrchestrator = new PageOrchestrator();
	var cart = [];

	window.addEventListener("load", () => {
		if (sessionStorage.getItem("mail") == null) {
			window.location.href = "LoginPage.html";
			sessionStorage.setItem("cart", JSON.stringify(cart));
		} else {			
			pageOrchestrator.start();
			//pageOrchestrator.refresh();
		} 
	}, false);


	function CartSupplier(code, name, totalPrice){
		this.code = code;
		this.name = name;
		this.products = [];
		this.totalPrice = totalPrice;
		this.shippingPrice = shippingPrice = null;

		this.setShippingPrice = function(shippingPrice){
			this.shippingPrice = shippingPrice;
		}

		this.addProduct = function(product) {
			this.products.push(product);
		}

		this.setTotalPrice = function(totalPrice) {
			this.totalPrice = totalPrice;
		}
	}

	function Product(code, name, quantity, price){
		this.code = code;
		this.name = name;
		this.quantity = quantity;
		this.price = price;

		this.setQuantity = function(quantity){
			this.quantity = quantity;
		}
	}


	function PageOrchestrator(){
		var alert = document.getElementById("id_alert");



		this.start = function () {
			

			/**
			 * 
			 * HOME PAGE
			 * 
			 */
			visualizeProduct = new VisualizeProduct(alert, document.getElementById("prodVisTable"), document.getElementById("prodVisBody"));
			visualizeProduct.show();
			searchForm = new SearchForm(document.getElementById("searchForm"), alert);
			searchForm.registerEvents(this);

			/**
			 * 
			 * RESULT PAGE
			 * 
			 */
			visualizeSearchProduct = new VisualizeSearchProduct(alert, document.getElementById("tableResults"), document.getElementById("resultsBody"), this);


			/**
			 * 
			 * CART PAGE
			 * 
			 */
			visualizeCartProduct = new VisualizeCartProduct(alert, document.getElementById("cartTable"),document.getElementById("cartBody"));

			/**
			 * 
			 * ORDER PAGE
			 * 
			 */
			visualizeOrderProduct = new VisualizeOrderProduct(alert, document.getElementById("orderTable"),document.getElementById("orderBody"));

			/**
			 * 
			 * LINK MENU
			 * 
			 */
			document.getElementById("goToHome").addEventListener("click", (e) => {
				this.refresh();
				this.showHome();
			}, false);

			document.getElementById("goToCart").addEventListener("click", (e) => {
				this.refresh();
				this.showCart();
			}, false);

			document.getElementById("goToOrder").addEventListener("click", (e) => {
				this.refresh();
				this.showOrder();
			}, false);

			//LOGOUT LINK
			document.querySelector("a[href='Logout']").addEventListener("click", (e) =>{
				e.preventDefault();
		        makeCall("GET", 'Logout', null, function (res) {
		            if(res.readyState === 4){
		                let message = res.responseText;
		                if(res.status === 200){
		                    sessionStorage.clear();
		                    window.location.href = "LoginPage.html";
			            }else if(res.status === 400){
			            	sessionStorage.clear();
							window.location.href = res.getResponseHeader("location");
			            }else{
			            	document.getElementById("error").textContent = message;
			            }       
		            }
		        }, null, false);
			},false);


		}

		this.showHome = function(){
			document.getElementById("homePage").style.display = "block";
			visualizeProduct.show();
		}

		this.showResult = function(products){
			this.refresh();
			document.getElementById("resultPage").style.display = "block";
			visualizeSearchProduct.updateProduct(products);
		}

		this.showCart = function(){
			document.getElementById("cartPage").style.display = "block";
			visualizeCartProduct.show();
		}

		this.showOrder = function(){
			document.getElementById("orderPage").style.display = "block";
			visualizeOrderProduct.show();
		}

		this.refresh = function(){
			document.getElementById("homePage").style.display = "none";
			visualizeProduct.reset();
			document.getElementById("resultPage").style.display = "none";
			visualizeSearchProduct.reset();
			document.getElementById("cartPage").style.display = "none";
			visualizeCartProduct.reset();
			document.getElementById("orderPage").style.display = "none";
			visualizeOrderProduct.reset();
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
			var self = this;
			makeCall("GET", "GoToHome", null,
				function(req) {
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							var products = JSON.parse(req.responseText);
							self.update(products); 
						}else if(req.status == 400){
							window.location.href = req.getResponseHeader("Location");
                  			window.sessionStorage.removeItem('mail');
						}
					} else {
						self.alert.textContent = "Siamo nello stato: " + req.readyState;
					}

				}
			);	
		}


		this.update = function(products){

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

					/*
					img.alt = "image";
					img.width = 200;///tengo??
					img.height = 150;///tengo??
					*/

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
			var div = document.querySelector("#" + this.searchForm.id + " div");
			div.hidden = false;
		}

		this.reset = function(){
			var div = document.querySelector("#" + this.searchForm.id + " div");
			div.hidden = true;
		}
		
		this.registerEvents = function(orchestrator) {
			this.searchForm.querySelector("input[type='submit']").addEventListener('click', (e) => {
				e.preventDefault;
				var eventform = e.target.closest("form");
				if(eventform.checkValidity()){
					var self = this;
					makeCall("POST", "GoToResults", e.target.closest("form"),
						function(req) {
							if (req.readyState == 4) {
								var message = req.responseText;
								if (req.status == 200) {
									let products = JSON.parse(req.responseText);
									if (products.length == 0) {
										self.alert.textContent = "You don't have any product to visualize";
										return;
									}
									orchestrator.showResult(products);
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
					eventform.reportValidity();
				}
			});	
		}
	}


	function VisualizeSearchProduct(alertIn, containerIn, bodyIn, orchestrator){
		this.alert = alertIn;
		this.container = containerIn;
		this.body = bodyIn;
		this.orchestrator = orchestrator;

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
			        linkText = document.createTextNode(p.code);
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
			        linkText = document.createTextNode(p.name);
			        anchor.appendChild(linkText);

			        anchor.setAttribute('product_code',  p.code);
			     	anchor.addEventListener("click", (e) => {
						self.showDetails(e.target.getAttribute("product_code"),products);
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
			var rightRow, cell, text, des, img, rightProduct ,detailsRow, nextRow, row, span, innerSpan, 
				overlay, paragraph, bold, ul, li, form, input, div;
			
			var self = this;
							
				
			div = document.getElementById("productDetails");
			if(div !== null){
				this.body.removeChild(div);
			}
				
			rightRow = document.getElementById(productCode); 

			i = 0;
			while(products[i].code != productCode){
				i++
			}

			rightProduct = products[i];

			detailsRow = document.createElement("tr"); 

				cell = document.createElement("td");
				des = document.createElement("b");
				des.textContent = "description:";
				cell.appendChild(des);
				text = document.createElement("p");
				text.textContent = rightProduct.description; 
				cell.appendChild(text);
			detailsRow.appendChild(cell);

				cell = document.createElement("td");
				des = document.createElement("b");
				des.textContent = "category:";
				cell.appendChild(des);
				text = document.createElement("p");
				text.textContent = rightProduct.category; 
				cell.appendChild(text);
			detailsRow.appendChild(cell);

				cell = document.createElement("td");
				img = document.createElement("img");
				img.src = rightProduct.photo;
				cell.appendChild(img); 
			detailsRow.appendChild(cell);

			div = document.createElement("div");
			div.setAttribute("id","productDetails");
			div.appendChild(detailsRow);

			self.body.appendChild(div);

			nextRow = rightRow.nextElementSibling;
			self.body.insertBefore(div, nextRow);

			suppliers.forEach(function(s){
				
				
				row = document.createElement("tr");
				div.appendChild(row);  
				
				cell = document.createElement("td");
				//cell.colspan = "3";
				row.appendChild(cell);
				
				//-----------------------------
				
					text = document.createElement("p");
					
					des = document.createElement("b");
					des.textContent = "Supplier name: ";
					text.appendChild(des);
					
					span = document.createElement("span");
					span.textContent = s.name;
					text.appendChild(span);
				
				cell.appendChild(text);
				//--------------------------------------------
					text = document.createElement("p");
					
					des = document.createElement("b");
					des.textContent = "score: ";
					text.appendChild(des);
					
					span = document.createElement("span");
					span.textContent = s.score;
					text.appendChild(span);
					
					des = document.createElement("b");
					des.textContent = " price: ";
					text.appendChild(des);
					
					span = document.createElement("span");
					span.textContent = s.unitaryPrice;
					text.appendChild(span);
					
					span = document.createElement("span");
					span.textContent = "$";
					text.appendChild(span);
					
					des = document.createElement("b");
					des.textContent = " Total in cart: ";
					text.appendChild(des);
					
					span = document.createElement("span");
					span.textContent = s.totalProductsPrice;
					text.appendChild(span);
					
					span = document.createElement("span");
					span.textContent = "$";
					text.appendChild(span);
				
				cell.appendChild(text);
				//---------------------------------------------
				
					text = document.createElement("p");
					
					des = document.createElement("b");
					des.textContent = "Product already in cart:  ";
					text.appendChild(des);
					
					span = document.createElement("span");
					cartSup = null;
					var cart = JSON.parse(sessionStorage.getItem("cart"));
					if(cart === null){
						cart = [];
					}
					for(cs of cart ){
						if(cs.code === s.code){
							cartSup = cs;
							break;
						}
					}

					s.totalNumber = 0;
					if(cartSup !== null){
							for(product of cartSup.products){
							s.totalNumber = s.totalNumber + product.quantity;
						}
					}
					span.textContent = s.totalNumber; 
					// finestra sovrapposta che parte da questo elemento
					span.addEventListener('mouseover', (e) => {
						overlay= document.createElement("overlay");
						
						if(cartSup !== null){
							cartSup.products.forEach(function(p){
							InnerRow = document.createElement("tr");

							cell = document.createElement("td");
							InnerRow.appendChild(cell);
							
							paragraph = document.createElement("p");
							cell.appendChild(paragraph);

							InnerSpan = document.createElement("span");
							InnerSpan.textContent = p.quantity;
							paragraph.appendChild(InnerSpan);

							InnerSpan = document.createElement("span");
							InnerSpan.textContent = " x ";
							paragraph.appendChild(InnerSpan);

							InnerSpan = document.createElement("span");
							InnerSpan.textContent = p.name;
							paragraph.appendChild(InnerSpan);

							InnerSpan = document.createElement("span");
							InnerSpan.textContent = " ( product code: ";
							paragraph.appendChild(InnerSpan);

							InnerSpan = document.createElement("span");
							InnerSpan.textContent = p.code;
							paragraph.appendChild(InnerSpan);

							InnerSpan = document.createElement("span");
							InnerSpan.textContent = " ) ";
							paragraph.appendChild(InnerSpan);

							
							cell = document.createElement("td");
							InnerRow.appendChild(cell);

							bold = document.createElement("b");
							bold.textContent = p.price;
							cell.appendChild(bold);

							paragraph = document.createElement("p");
							cell.appendChild(paragraph);

							InnerSpan = document.createElement("span");
							InnerSpan.textContent = p.price;
							paragraph.appendChild(InnerSpan);

							InnerSpan = document.createElement("span");
							InnerSpan.textContent = " $";
							paragraph.appendChild(InnerSpan);
							});
						}
						
						overlay.id = "overlay";
						overlay.style.display = 'block';  //forse questo lo toglierò 
					});
					
					span.addEventListener('mouseout', (e) => {
						overlay = document.getElementById("overlay");
						overlay.style.display = 'none';  
					});
					text.appendChild(span);
				
				cell.appendChild(text);
				//---------------------------------------------
				
					text = document.createElement("p");
										
					des = document.createElement("b");
					des.textContent = "Minimum price for free shipping: ";
					text.appendChild(des);
					
					span = document.createElement("span");
					span.textContent = s.freeShipping;
					text.appendChild(span);
					
					span = document.createElement("span");
					span.textContent = "$";
					text.appendChild(span);
				
				cell.appendChild(text);

				//------------------------------------------------
					des = document.createElement("b");
					des.textContent = "Spending ranges: ";
				cell.appendChild(des);
					
					ul = document.createElement("ul");
				
					s.spendingRanges.forEach(function(sp){
						li = document.createElement("li");
						
							text = document.createElement("p");
							
							span = document.createElement("span");
							if(sp.minimumN !== sp.maximumN){
								span.textContent = sp.minimumN;    
								text.appendChild(span);
								
								span = document.createElement("span");
								span.textContent = " - ";
								text.appendChild(span);
								
								span = document.createElement("span");
								span.textContent = sp.maximumN;
								text.appendChild(span);
							}else{
								span.textContent = "More than ";    
								text.appendChild(span);
								
								span = document.createElement("span");
								span.textContent = sp.minimumN;
								text.appendChild(span);
							}
							
							span = document.createElement("span");
							span.textContent = " products = ";
							text.appendChild(span);
							
							span = document.createElement("span");
							span.textContent = sp.price;
							text.appendChild(span);
							
							span = document.createElement("span");
							span.textContent = "$";
							text.appendChild(span);

						li.appendChild(text);

					ul.appendChild(li);		
					});

				cell.appendChild(ul);

				form = document.createElement("form");
					//el1.id="formForAddToCart";
					form.setAttribute("action","#");
					form.setAttribute("id",rightProduct.code);

						input = document.createElement("input");
						input.setAttribute("type","number");
						input.setAttribute("name","quantity");
						form.appendChild(input);

						input = document.createElement("input");
						input.setAttribute("type","submit");
						input.setAttribute("value","Add To Cart");
						input.addEventListener("click", (e) => {
							e.preventDefault;
							form = document.querySelector("form[id='" + rightProduct.code + "']");
							self.addToCart(form.querySelector("input[type='number']").value,rightProduct,s);
						}, false);
						form.appendChild(input);

				cell.appendChild(form);	

			self.container.style.visibility = "visible";				

			});
		}

		this.addToCart = function(quantity, product, supplier){
			console.log(quantity);
			console.log(product.name);
			console.log(supplier.name);
			var cartProduct = null;
			var cartSupplier = null;
			var cart = JSON.parse(sessionStorage.getItem("cart"));
			if(cart !== null){
				for(cs of cart){
					if(cs.code === supplier.code){
						cartSupplier = cs;
						break;
					}
				}
			}else{
				cart = [];
			}
			

			if(cartSupplier === null){
				cartProduct = new Product(product.code,product.name, quantity, supplier.unitaryPrice);
				let total = supplier.unitaryPrice * quantity;
				cartSupplier = new CartSupplier(supplier.code,supplier.name,total);
				cartSupplier.addProduct(cartProduct);
				cart.push(cartSupplier);
			}else{
				for(p of cartSupplier.products){
					if(p.code === product.code){
						cartProduct = p;
					}
				}

				if(cartProduct === null){
					cartProduct = new Product(product.code,product.name, quantity, product.price);
					cartSupplier.addProduct(cartProduct);
				}else{
					let prevQuant = cartProduct.quantity;
					cartProduct.setQuantity(prevQuant + quantity);
				}
				let tmp = cartSupplier.totalPrice;
				cartSupplier.setTotalPrice(tmp + product.price * quantity);
			}

			if(cartSupplier.shippingPrice !== 0){
				if(cartSupplier.totalPrice >= supplier.freeShipping){
					cartSupplier.setShippingPrice(0);
				}else{
					for(sp of supplier.spendingRanges){
						if(supplier.totalNumber >= sp.maximumN && (supplier.totalNumber <= sp.maximumN || sp.minimumN === sp.maximumN)){
							cartSupplier.setShippingPrice(sp.price);
						}
					}
				}
			}
			sessionStorage.setItem("cart", JSON.stringify(cart));
			self.orchestrator.refresh();
			self.orchestrator.showCart();
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
			this.update();
		}

		this.update = function(){
			var row, cell, form, button;
			this.body.innerHTML="";
			
			var self = this;
			var cart = JSON.parse(sessionStorage.getItem("cart"));
			if(cart !== null){
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
							row.appendChild(cell);


							cell = document.createElement("td");
							cell.setAttribute("colspan",2);

								bold = document.createElement("b");
								bold.textContent = "product price:";

							cell.appendChild(bold);

								paragraph = document.createElement("p");

								span = document.createElement("span");
								span.textContent = p.price;
								paragraph.appendChild(span);

								span = document.createElement("span");
								span.textContent = " $";
								paragraph.appendChild(span);

							cell.appendChild(paragraph);
							row.appendChild(cell);	
						self.body.appendChild(row);
					});
					
				});
			}
			
			this.container.style.visibility = "visible";
		}


		this.createOrder = function(supplierCode) {
			var self = this;
			var cart = JSON.parse(sessionStorage.getItem("cart"));
			if(cart === null){
				cart = [];
			}
			for(cartSupplier of cart){
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
							for(let i = 0; i < cart.length; i++){
								if( cart[i].code === cartSupplier.code){
									cart.splice(i,1);
									break;
								}
							}
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
				},JSONvalue);
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
			var self = this;
			makeCall("POST", "GoToOrder",null,
				function(req) {
					if (req.readyState == 4) {
						let message = req.responseText;
						if (req.status == 200) {
							let orders = JSON.parse(req.responseText);
							if (orders.length == 0) {
								self.alert.textContent = "You don't have any supplier to visualize";
								return;
							}
							self.update(orders);
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

		this.update = function(orders){
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
						row.appendChild(cell);	


						cell = document.createElement("td");
						cell.setAttribute("colspan",2);

							bold = document.createElement("b");
							bold.textContent = "product price:";

						cell.appendChild(bold);

							paragraph = document.createElement("p");

							span = document.createElement("span");
							span.textContent = p.price;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " $";
							paragraph.appendChild(span);

						cell.appendChild(paragraph);
						row.appendChild(cell);
					self.body.appendChild(row);
				});
			});
			this.container.style.visibility = "visible";
		}
	}
	
};




			
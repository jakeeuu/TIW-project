{

	var pageOrchestrator = new PageOrchestrator();
	var cart = [];

	window.addEventListener("load", () => {
		if (sessionStorage.getItem("mail") == null) {
			window.location.href = "LoginPage.html";
			sessionStorage.setItem("cart", JSON.stringify(cart));
		} else {			
			pageOrchestrator.start();
		} 
	}, false);


	function CartSupplier(code, name, totalPrice){
		this.code = code;
		this.name = name;
		this.products = [];
		this.totalPrice = totalPrice;
		this.shippingPrice = null;

		this.setShippingPrice = function(shippingPrice){
			this.shippingPrice = shippingPrice;
		}

		this.addProduct = function(product) {
			this.products.push(product);
		}

		this.setTotalPrice = function(totalPrice) {
			this.totalPrice = totalPrice;
		}

		this.setProducts = function(products){
			this.products.splice(0,this.products.length);
			this.products = products.slice(0, product.length);
		}
	}

	function Product(code, name, quantity, price){
		this.code = code;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
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
			searchForm.show();

			/**
			 * 
			 * RESULT PAGE
			 * 
			 */
			visualizeSearchProduct = new VisualizeSearchProduct(alert, document.getElementById("tableResults"), document.getElementById("resultsBody"), this);
			visualizeSearchProduct.reset();

			/**
			 * 
			 * CART PAGE
			 * 
			 */
			visualizeCartProduct = new VisualizeCartProduct(alert, document.getElementById("cartTable"),document.getElementById("cartBody"), this);
			visualizeCartProduct.reset();
			/**
			 * 
			 * ORDER PAGE
			 * 
			 */
			visualizeOrderProduct = new VisualizeOrderProduct(alert, document.getElementById("orderTable"),document.getElementById("orderBody"));
			visualizeOrderProduct.reset();
			/**
			 * 
			 * LINK MENU
			 * 
			 */
			document.getElementById("goToHome").addEventListener("click", (e) => {
				e.preventDefault();
				this.refresh();
				this.showHome();
			}, false);

			document.getElementById("goToCart").addEventListener("click", (e) => {
				e.preventDefault();
				this.refresh();
				this.showCart();
			}, false);

			document.getElementById("goToOrder").addEventListener("click", (e) => {
				e.preventDefault();
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
			            }else if(res.status === 403){
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
			document.getElementById("goToHome").parentNode.setAttribute("class","active");
			document.getElementById("homePage").style.display = "block";
			visualizeProduct.show();
			searchForm.show();
			
		}

		this.showResult = function(products){
			this.refresh();
			document.getElementById("resultPage").style.display = "block";
			visualizeSearchProduct.updateProduct(products);
			searchForm.show();
		}

		this.showCart = function(){
			document.getElementById("goToCart").parentNode.setAttribute("class","active");
			document.getElementById("cartPage").style.display = "block";
			visualizeCartProduct.show();
		}

		this.showOrder = function(){
			document.getElementById("goToOrder").parentNode.setAttribute("class","active");
			document.getElementById("orderPage").style.display = "block";
			visualizeOrderProduct.show();
		}

		this.showOrders = function(orders){
			this.refresh();
			document.getElementById("goToOrder").parentNode.setAttribute("class","active");
			document.getElementById("orderPage").style.display = "block";
			visualizeOrderProduct.update(orders);
		}

		this.refresh = function(){
			alert.textContent = "";
			document.getElementById("goToHome").parentNode.setAttribute("class","");
			document.getElementById("homePage").style.display = "none";
			visualizeProduct.reset();
			searchForm.reset();
			document.getElementById("resultPage").style.display = "none";
			visualizeSearchProduct.reset();
			document.getElementById("goToCart").parentNode.setAttribute("class","");
			document.getElementById("cartPage").style.display = "none";
			visualizeCartProduct.reset();
			document.getElementById("goToOrder").parentNode.setAttribute("class","");
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
			document.getElementById("goToHome").setAttribute("class","");
			document.getElementById("homeTitle").style.visibility = "hidden";
		}

		this.show = function(){
			var self = this;
			makeCall("GET", "GoToHome", null,
				function(req) {
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							var products = JSON.parse(req.responseText);
							document.getElementById("homeTitle").style.visibility = "visible";
							self.update(products); 
						}else if(req.status == 403){
							window.location.href = req.getResponseHeader("Location");
                  			window.sessionStorage.removeItem('mail');
						}else{
							self.alert.textContent = message;
						}
					}

				}
			);	
		}


		this.update = function(products){

			let row, cell, img;
			this.body.innerHTML="";

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
				e.preventDefault();
				var eventform = e.target.closest("form");
				if(eventform.checkValidity()){
					var self = this;
					makeCall("POST", "GoToResults", e.target.closest("form"),
						function(req) {
							if (req.readyState == 4) {
								var message = req.responseText;
								if (req.status == 200) {
									let products = JSON.parse(req.responseText);
									orchestrator.showResult(products);
								}else if (req.status == 403) {
									window.location.href = req.getResponseHeader("Location");
									window.sessionStorage.removeItem('mail');
								}else{
									self.alert.textContent = message;
								}
							}
						}
					);
				}else{
					this.alert.textContent = "You have to write something in the search box";
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
			     		e.preventDefault();
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
			     		e.preventDefault();
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
						}else if (req.status == 403) {
							window.location.href = req.getResponseHeader("Location");
							window.sessionStorage.removeItem('mail');
						}else{
							self.alert.textContent = message;
						}
					}
				}
			);
		}


		this.updateSupplier = function (products, suppliers, productCode){
			var rightRow, cell, text, des, img, rightProduct ,detailsRow, nextRow, row, span, innerSpan, 
				 paragraph, bold, ul, li, form, input, div, table ,newWindow, content, cartSup, title, rows;
			
			var self = this;				
	
			rows = document.querySelectorAll("#productDetails");
			if(rows !== null){
				for(row of rows){
					this.body.removeChild(row);
					row.remove();
				}
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

			detailsRow.setAttribute("id","productDetails");
			self.body.appendChild(detailsRow);
			nextRow = rightRow.nextElementSibling;
			self.body.insertBefore(detailsRow, nextRow);

			suppliers.forEach(function(s){
				
				
				row = document.createElement("tr");

				row.setAttribute("id","productDetails");
				self.body.appendChild(row);
				nextRow = detailsRow.nextElementSibling;
				self.body.insertBefore(row, nextRow);

				cell = document.createElement("td");
				cell.setAttribute("colspan",3);
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
							s.totalNumber = parseInt(s.totalNumber) + parseInt(product.quantity);
						}
					}
					span.textContent = s.totalNumber;
					if(cartSup !== null)
						span.setAttribute("cartSup", cartSup.code);
			
					// finestra sovrapposta che parte da questo elemento
					span.addEventListener('mouseover', (e) => {
						e.preventDefault();
						let cartSup = null;
						
						var cart = JSON.parse(sessionStorage.getItem("cart"));
						if(cart === null){
							cart = [];
						}
						
						if(e.target.hasAttribute("cartSup")){
							for(cs of cart){
								if(cs.code == e.target.getAttribute("cartSup")){
									cartSup = cs;
									break;
								}
							}
						}	
							
						newWindow = document.getElementById("newWindow");
						content = document.createElement("div");
						content.id="contentWindow";
						
						if(cartSup !== null){
							title = document.createElement("h1");
							title.textContent = "These are the other products in the cart from the same supplier";
							title.setAttribute("class", "title");
							content.appendChild(title);
							
							div = document.createElement("div");
							div.setAttribute("class", "container");
							content.appendChild(div);
							
							table = document.createElement("table");
							div.appendChild(table);
						
							cartSup.products.forEach(function(p){
								InnerRow = document.createElement("tr");
								table.appendChild(InnerRow);
								
	
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
								bold.textContent = "product price: ";
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
							
						}else{
							title = document.createElement("h1");
							title.textContent = "there are no products from the same supplier in the cart";
							title.setAttribute("class", "title");
							content.appendChild(title);
						}
						
						newWindow.appendChild(content);
						newWindow.style.display = "block";
						
						  
					});
					
					
					span.addEventListener('mouseout', (e) => {
						e.preventDefault();
						newWindow.style.display = "none";
						newWindow.removeChild(content);
						content.remove();
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
					form.setAttribute("action","#");
					form.setAttribute("id",s.code);

						input = document.createElement("input");
						input.setAttribute("type","number");
						input.setAttribute("name","quantity");
						form.appendChild(input);

						input = document.createElement("input");
						input.setAttribute("type","submit");
						input.setAttribute("value","Add To Cart");
						input.addEventListener("click", (e) => {
							e.preventDefault();
							let cartForm = document.querySelector("form[id='" + s.code + "']");
							self.addToCart(cartForm.querySelector("input[type='number']").value,rightProduct,s);
						}, false);
						form.appendChild(input);

				cell.appendChild(form);	

			});
			self.container.style.visibility = "visible";
		}

		this.addToCart = function(quantity, product, supplier){
			self = this;

			if(parseInt(quantity) > 0 && !isNaN(parseInt(quantity))){
				var cartProduct = null;
				var cartSupplier = null;
				var cart = JSON.parse(sessionStorage.getItem("cart"));
				if(cart !== null){
					for(var cs of cart){
						if(cs.code === supplier.code){
							cartSupplier = cs;
							break;
						}
					}
				}else{
					cart = [];
				}
				
				var isNew = false;
				if(cartSupplier === null){
					cartProduct = new Product(product.code,product.name, quantity, supplier.unitaryPrice);
					let total = parseFloat(supplier.unitaryPrice) * parseInt(quantity);
					cartSupplier = new CartSupplier(supplier.code,supplier.name,total);
					cartSupplier.addProduct(cartProduct);
					isNew = true;
				}else{
					cartSupplier = new CartSupplier(cs.code, cs.name, cs.totalPrice);
					cartSupplier.setShippingPrice(cs.shippingPrice);
					var products = [];
					for(p of cs.products){
						products.push(new Product(p.code, p.name, p.quantity, p.price));
						if(p.code === product.code){
							cartProduct = p;
						}
					}

					if(cartProduct === null){
						cartProduct = new Product(product.code,product.name, quantity, supplier.unitaryPrice);
						products.push(cartProduct);

					}else{
						let prevQuant = parseInt(cartProduct.quantity);
						cartProduct = new Product(product.code,product.name, prevQuant + parseInt(quantity), supplier.unitaryPrice);

						for(let i = 0; i < products.length; i++){
							if(products[i].code === cartProduct.code){
								products.splice(i,1,cartProduct);
							}
						}
					}
					cartSupplier.setProducts(products);
					let tmp = parseFloat(cartSupplier.totalPrice);
					cartSupplier.setTotalPrice(tmp + parseFloat(supplier.unitaryPrice) * parseInt(quantity));
				}

				if(cartSupplier.shippingPrice !== 0){
					if(cartSupplier.totalPrice >= supplier.freeShipping){
						cartSupplier.setShippingPrice(0);
					}else{
						supplier.totalNumber = supplier.totalNumber + parseInt(quantity);
						for(sp of supplier.spendingRanges){
							if(supplier.totalNumber >= sp.minimumN && (supplier.totalNumber <= sp.maximumN || sp.minimumN === sp.maximumN)){
								cartSupplier.setShippingPrice(sp.price);
							}
						}
					}
				}

				if(isNew){
					cart.push(cartSupplier);
				}else{
					for(let i = 0; i < cart.length; i++){
						if(cart[i].code === cartSupplier.code){
							cart.splice(i,1,cartSupplier);
						}
					}
				}

				sessionStorage.setItem("cart", JSON.stringify(cart));
				self.orchestrator.refresh();
				self.orchestrator.showCart();
			}else{
				self.alert.textContent = "you have to write a number > 0 in the quantity box";
			}
			
		}

	}


	function VisualizeCartProduct(alertIn, containerIn, bodyIn, orchestratorIn){
		this.alert = alertIn;
		this.container = containerIn;
		this.body = bodyIn;
		this.orchestrator = orchestratorIn;

		this.reset = function() {
			this.container.style.visibility = "hidden";
			document.getElementById("cartTitle").style.display = "none";
		}

		this.show = function(){
			this.update();
			
		}

		this.update = function(){
			var row, cell, form, button, paragraph, span, bold;
			this.body.innerHTML="";
			
			var self = this;
			var cart = JSON.parse(sessionStorage.getItem("cart"));
			
			let title = document.getElementById("cartTitle");
			title.style.display = "block";

			if(cart !== null && cart.length > 0){
				cart.forEach(function(cs){

					row = document.createElement("tr");

						cell = document.createElement("td");
						cell.textContent = cs.code;
						row.appendChild(cell);

						cell = document.createElement("td");
						cell.textContent = cs.name;
						row.appendChild(cell);

						cell = document.createElement("td");
						paragraph = document.createElement("p");

							span = document.createElement("span");
							span.textContent = cs.totalPrice;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " $";
							paragraph.appendChild(span);
							
						cell.appendChild(paragraph);
						row.appendChild(cell);

						cell = document.createElement("td");
						paragraph = document.createElement("p");

							span = document.createElement("span");
							span.textContent = cs.shippingPrice;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " $";
							paragraph.appendChild(span);
							
						cell.appendChild(paragraph);
						row.appendChild(cell);

						//metto bottone
						cell = document.createElement("td");

							form = document.createElement("form");
							form.setAttribute("action","#");

								button = document.createElement("button");
								button.setAttribute("supplier_code",cs.code);
								button.textContent = "Order";
								button.addEventListener("click", (e) => {
									e.preventDefault();
									self.createOrder(e.target.getAttribute("supplier_code"));
								}, false);
								form.appendChild(button);

							cell.appendChild(form);

						row.appendChild(cell);	

					self.body.appendChild(row);

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
				this.container.style.visibility = "visible";
				title.querySelector("#emptyTitleCart").style.display = "none";
				title.querySelector("#normalTitleCart").style.display = "block";
			}else{
				title.querySelector("#emptyTitleCart").style.display = "block";
				title.querySelector("#normalTitleCart").style.display = "none";
			}

			
			
			
		}


		this.createOrder = function(supplierCode) {
			var self = this;
			var cart = JSON.parse(sessionStorage.getItem("cart"));
			if(cart === null){
				cart = [];
			}
			for(var cartSupplier of cart){
				if(cartSupplier.code == supplierCode){
					break;
				}
			}

			makeCall("POST", "CreateOrder", null,
				function(req) {
					if (req.readyState == 4) {
						let message = req.responseText;
						if (req.status == 200) {
							let orders = JSON.parse(req.responseText);
							if (orders == null) {
								let title = document.getElementById("orderTitle");
								title.style.display = "block";
								title.querySelector("#emptyTitleOrders").style.display = "block";
								title.querySelector("#normalTitleOrders").style.display = "none";
								return;
							}
							//tolgo cartSupplier da carrello
							for(let i = 0; i < cart.length; i++){
								if( cart[i].code === cartSupplier.code){
									cart.splice(i,1);
									sessionStorage.setItem("cart", JSON.stringify(cart));
									break;
								}
							}
							/////quando mi arriva la risposta chiamo l'orchestrator che mi mostra la order page
							self.orchestrator.showOrders(orders); 
						}else if (req.status == 403) {
							window.location.href = req.getResponseHeader("Location");
							window.sessionStorage.removeItem('mail');
						}else{
							self.alert.textContent = message;
						}
					}
				},cartSupplier);
		}
		
	}

	function VisualizeOrderProduct(alertIn, containerIn, bodyIn){
		this.alert = alertIn;
		this.container = containerIn;
		this.body = bodyIn;

		this.reset = function() {
			this.container.style.visibility = "hidden";
			let title = document.getElementById("orderTitle")
			title.style.display = "none";
			title.querySelector("#emptyTitleOrders").style.display = "none";
			title.querySelector("#normalTitleOrders").style.display = "none";
		}

		this.show = function(){
			var self = this;
			makeCall("POST", "GoToOrder",null,
				function(req) {
					if (req.readyState == 4) {
						let message = req.responseText;
						if (req.status == 200) {
							let orders = JSON.parse(req.responseText);
							if (orders === null) {
								let title = document.getElementById("orderTitle");
								title.style.display = "block";
								title.querySelector("#emptyTitleOrders").style.display = "block";
								title.querySelector("#normalTitleOrders").style.display = "none";
								return;
							}
							self.update(orders);
						}else if (req.status == 403) {
							window.location.href = req.getResponseHeader("Location");
							window.sessionStorage.removeItem('mail');
						}else{
							self.alert.textContent = message;
						}
					}
				}
			);
		}

		this.update = function(orders){
			var row, cell, paragraph, span, bold;
			this.body.innerHTML="";
			
			let title = document.getElementById("orderTitle");
			title.style.display = "block";
			title.querySelector("#emptyTitleOrders").style.display = "none";
			title.querySelector("#normalTitleOrders").style.display = "block";
			
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
						paragraph = document.createElement("p");

							span = document.createElement("span");
							span.textContent = order.totalPrice;
							paragraph.appendChild(span);

							span = document.createElement("span");
							span.textContent = " $";
							paragraph.appendChild(span);

						cell.appendChild(paragraph);
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = order.date;
					row.appendChild(cell);

					cell = document.createElement("td");
					cell.textContent = order.address;
					row.appendChild(cell);


				self.body.appendChild(row);

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




			
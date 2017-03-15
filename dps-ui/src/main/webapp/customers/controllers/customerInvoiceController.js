angular.module("customerInvoiceApp", ['angularUtils.directives.dirPagination', 'smoothScroll', 'ngMessages'])
	.controller("customerInvoiceController", function($scope, $rootScope, $timeout, $q, smoothScroll, 
			getCustomersForInvoiceService, getProductsForInvoiceService, getSuppliersForInvoiceService,
			buildInvoiceCalculateService, pdfItemizationService, imageItemizationService){
		
		$scope.showSuccessBox = false; /* Hide the success box */
        $scope.showErrorBox = false; /* Hide the error box */
        $scope.successMessage = ""; /* Set success message to blank */
        $scope.errorMessage = ""; /* Set error message to blank */
        $scope.searchProductSection = false; /* Hide the search product section */
        $scope.productDetailsSection = false; /* Hide the Products details section */
        $scope.addedProductsSection = false; /* Hide the products' added table */
        $scope.invoiceSummarySection = false; /* Hide Customer invoice summary section */
        $scope.editDisabled = true; /* Enable the Edit button for products added table */
        $scope.deleteDisabled = true; /* Disable the Delete button for products added table */
        $scope.allProducts; /* Get all the products for auto complete */
        $scope.querySearch = querySearch; /* Call the querySearch function for auto complete */
        $scope.searchText = null; /* Set search text as null */
        $scope.searchedProduct = null; /* Set searched product as null */
        $scope.showAddBtn = true; /* Show Add Button & hide Update Button */
        $scope.productsList = []; /* List of Products Added in the table */
        $scope.calcProductsList = [];
        $scope.selectedRows = []; /* Array for toggleAll function */
        $scope.selectAll = false; /* Set toggle all to false */
        $scope.sortOrder = false; /* set the default sort order */
        $scope.sortType = 'productCode'; /* set the default sort type */
        $scope.invoiceDate = new Date(); /* By default set todays date as order date */
        $scope.invoiceSummary = []; /* Object containing all the products added to invoice summary  */
        $scope.grandTotal = 0;
		$scope.totalCartoons = 0;
        $scope.productDetails = {/* Object to show product details based on auto complete result */
                "id": "",
            	"productCode": "",
                "supplierInitials": "",
                "price": "",
                "moq": "",
                "cartoonQuantity": "",
                "productQuantity": "",
                "remarks": "",
                "isChecked": false
            };
        
        /* Function will be executed after the page is loaded */
        $scope.$on('$viewContentLoaded', function () {
        	/* Get list of all Customers */
        	getCustomersForInvoiceService.query().$promise.then(function(data) {
            	$scope.customers = data;
            }); 
            
            /* Get list of all Products */
        	getProductsForInvoiceService.query().$promise.then(function(data) {
            	$scope.allProducts = data;
            });                
        });
        
        $scope.showSearchSection = function () {
            if ($scope.customerShipmark !== undefined) {
                $scope.searchProductSection = true;
                $scope.invoiceSummarySection = false;
            }
        };
        
        /* Auto search for products */
        function querySearch(query) {
            var results = query ? $scope.allProducts.filter(createFilterFor(query)) : $scope.allProducts;
            var deferred = $q.defer();
            $timeout(function () {
                deferred.resolve(results);
            }, Math.random() * 800, false);
            return deferred.promise;
        }

        /* Create filter function for a query string */
        function createFilterFor(query) {
            return function filterFn(product) {
                return (product.productCode.toLowerCase().indexOf(query.toLowerCase()) !== -1);
            };
        }

        $scope.getProductDetails = function () {
            if ($scope.searchedProduct !== null) {
            	angular.element(document.querySelector('.loader')).addClass('show');  
            	console.log($scope.productDetails)
                $timeout(function () {
                	$scope.productDetails.id = $scope.searchedProduct.id;
                	$scope.productDetails.productCode = $scope.searchedProduct.productCode;
                	$scope.productDetails.supplierProductInfoList = $scope.searchedProduct.supplierProductInfoList
                	$scope.productDetails.supplierInitials =  $scope.searchedProduct.supplierProductInfoList[0].supplierInitials;
                	$scope.productDetails.price = $scope.searchedProduct.supplierProductInfoList[0].supplierPrice;
                	$scope.productDetails.moq = $scope.searchedProduct.moq;
                	$scope.productDetails.cartoonQuantity = $scope.searchedProduct.cartoonQuantity;
                    $scope.productDetailsSection = true;
                    $scope.showAddBtn = true; /* Show Update Button & hide Add Button */
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = true; 
                    angular.forEach($scope.productsList, function (product) {
                        product.isChecked = false; /* Uncheck the checkbox */
                    });
                    angular.element(document.querySelector('.loader')).removeClass('show');
                    $timeout(function () {
                        angular.element(document.querySelector('#productQuantity')).focus();
                    }, 100);
                }, 500);
            }
        };
        
        $scope.getSupplierProductPrice = function () {
        	angular.forEach($scope.productDetails.supplierProductInfoList, function (supplierProductInfo) {
        		if($scope.productDetails.supplierInitials === supplierProductInfo.supplierInitials){
        			$scope.productDetails.price = supplierProductInfo.supplierPrice;
        		}
        	});
        };
        
        $scope.addProduct = function () { /* Function to add Product in the table */
        	angular.element(document.querySelector('.loader')).addClass('show');
        	$scope.addedProductsSection = true;
        	$timeout(function(){
        		$scope.productsList.push({
                    "productCode": $scope.productDetails.productCode,
                    "productId": $scope.productDetails.id,
                    "selectedSupplierInitials": $scope.productDetails.supplierInitials,
                    "quantity": $scope.productDetails.productQuantity,
                    "cartoonQuantity": $scope.productDetails.cartoonQuantity,
                    "remarks": $scope.productDetails.remarks,
                    "unitCost": $scope.productDetails.price,
                    "moq" : $scope.productDetails.moq,
                    "supplierProductInfoList" : $scope.productDetails.supplierProductInfoList
                });
        		
                $scope.calcProductsList.push({
                    "productCode": $scope.productDetails.productCode,
                    "productId": $scope.productDetails.id,
                    "selectedSupplierInitials": $scope.productDetails.supplierInitials,
                    "quantity": $scope.productDetails.productQuantity,
                    "remarks": $scope.productDetails.remarks,
                    "unitCost": $scope.productDetails.price,
                });
                
                $scope.searchText = null;                
                $scope.productDetails={};
                $scope.productDetails.isChecked = false;
                $scope.productDetailsSection = false;                   
                smoothScroll(document.getElementById('addedProductsSection')); /* Scroll to the added products section */
                angular.element(document.querySelector('.loader')).removeClass('show');
                $timeout(function () {
                    angular.element(document.querySelector('#autocompleteProductField')).focus();
                    $scope.showSuccessBox = false;
                    $scope.showErrorBox = false;
                    $scope.selectAll = false;
                    $scope.selectedRows = [];
                }, 100);
        	}, 500);                
        };
        
        /* Trigger addProduct() function on "enter" key press in remark field */
        $scope.triggerAddProduct = function(event){            	
        	if(event.which === 13 && $scope.productDetails.productQuantity > 0) {
        		event.preventDefault();
        		if($scope.showAddBtn){
        			$scope.addProduct(); /* Add function */
        		}
        		else{
        			$scope.updateAddedProduct(); /* Update function */
        		}
            }
        };
        
        $scope.editAddedProduct = function () {/* Function to edit the Product in added products table */
            angular.forEach($scope.productsList, function (product) {
                if (product.isChecked) {
                    $scope.productDetails.productCode = product.productCode;
                    $scope.productDetails.supplierInitials = product.selectedSupplierInitials;
                    $scope.productDetails.price = product.unitCost;
                    $scope.productDetails.moq = product.moq;
                    $scope.productDetails.cartoonQuantity = product.cartoonQuantity;
                    $scope.productDetails.productQuantity = product.quantity;
                    $scope.productDetails.remarks = product.remarks;
                    $scope.productDetails.supplierProductInfoList = product.supplierProductInfoList
                    $scope.showAddBtn = false; /* Show Update Button & hide Add Button */
                    $scope.productDetailsSection = true;
                    smoothScroll(document.getElementById('productDetailsSection')); /* Scroll to the product details section */
                    $timeout(function () {
                        angular.element(document.querySelector('#productQuantity')).focus();
                    }, 100);
                }
            });
        };
        
        $scope.updateAddedProduct = function () {/* Function to update the Product in added products table */
            angular.forEach($scope.productsList, function (product) {
                if (product.isChecked) {
                    product.productCode = $scope.productDetails.productCode;
                    product.selectedSupplierInitials = $scope.productDetails.supplierInitials;
                    product.unitCost = $scope.productDetails.price;
                    product.moq = $scope.productDetails.moq;
                    product.remarks = $scope.productDetails.remarks;
                    product.quantity = $scope.productDetails.productQuantity;
                    product.cartoonQuantity = $scope.productDetails.cartoonQuantity;
                    product.isChecked = false;
                    
                    angular.forEach($scope.calcProductsList, function (calcProduct) {
                    	if(calcProduct.productId === product.productId){
                    		calcProduct.productCode = $scope.productDetails.productCode;
                    		calcProduct.selectedSupplierInitials = $scope.productDetails.supplierInitials;
                    		calcProduct.quantity = $scope.productDetails.productQuantity;
                    		calcProduct.remarks = $scope.productDetails.remarks;
                    		calcProduct.unitCost = $scope.productDetails.price;
                    	}
                    });
                }
            });
            $scope.productDetails.productCode = "";
            $scope.productDetails.supplierCode = "";
            $scope.productDetails.price = "";
            $scope.productDetails.productMOQ = "";
            $scope.productDetails.remarks = "";
            $scope.productDetails.productQuantity = "";
            $scope.productDetails.cartoonQuantity = "";
            $scope.productDetailsSection = false;
            $scope.showAddBtn = true; /* Show Add Button & hide Update Button */
            $scope.editDisabled = true;
            $scope.deleteDisabled = true;
            $timeout(function () {
                angular.element(document.querySelector('#autocompleteProductField')).focus();
                $scope.showSuccessBox = false;
                $scope.showErrorBox = false;                    
                $scope.selectAll = false;
                $scope.selectedRows = [];
            }, 100);
        };
        
        /* Function to delete the selected product from added products table */
        $scope.deleteAddedProduct = function () {
            var newProductsList = [];      
            angular.forEach($scope.productsList, function (product) {
                if (!product.isChecked) {
                    newProductsList.push(product);            
                }
            });
            $scope.productsList = newProductsList;               
            $scope.calcProductsList = angular.copy($scope.productsList); /* Copy the new productList to calcProductList */     
            /* Remove unwanted properties from $scope.calcProductsList object */
            angular.forEach($scope.calcProductsList, function (calcProduct) {
            	delete calcProduct.cartoonQuantity;
            	delete calcProduct.moq;
            	delete calcProduct.supplierProductInfoList;
            	delete calcProduct.isChecked;
            });
            $scope.editDisabled = true;
            $scope.deleteDisabled = true;                
            $scope.selectAll = false;
            angular.forEach($scope.productsList, function (product) {
            	product.isChecked = $scope.selectAll;
                $scope.selectedRows = [];
            });
        };
        
        /* Function to select/unselect all the rows in products added table */
	    $scope.toggleAll = function () {
	        if ($scope.selectAll) {
	            $scope.selectAll = true;
	            $scope.editDisabled = true;
	            $scope.deleteDisabled = false;
	            $scope.selectedRows = [];
	            angular.forEach($scope.productsList, function (product) {
	            	product.isChecked = $scope.selectAll;
	                $scope.selectedRows.push(1);
	            });
	        }
	        else {
	            $scope.selectAll = false;
	            $scope.editDisabled = true;
	            $scope.deleteDisabled = true;
	            angular.forEach($scope.productsList, function (product) {
	            	product.isChecked = $scope.selectAll;
	                $scope.selectedRows = [];
	            });
	        }
	    };
        
        /* Function to select/unselect the row in products added table */
	    $scope.toggle = function (element) {
	        if (element.isChecked) {
	            $scope.selectedRows.push(1);
	            $scope.editDisabled = false;
	            $scope.deleteDisabled = false;
	        }
	        else {
	            $scope.selectedRows.pop();
	        }
	        if ($scope.productsList.length === $scope.selectedRows.length) {
	            $scope.selectAll = true;
	            $scope.editDisabled = true;
	        }
	        else {
	            $scope.selectAll = false;
	        }
	        if ($scope.selectedRows.length === 0) {
	            $scope.editDisabled = true;
	            $scope.deleteDisabled = true;
	        }
	        else if ($scope.selectedRows.length > 1) {
	            $scope.editDisabled = true;
	        }
	        else {
	            $scope.editDisabled = false;
	        }
	    };
	    
	    $scope.saveInoiceSummary = function () {
        	angular.element(document.querySelector('.loader')).addClass('show');                                   
            var cartJson = {
            		"customerShipmark" : $scope.customerShipmark,
            		"orderDate" : $scope.invoiceDate,
            		"orderItems" : $scope.calcProductsList
            	} 
            console.log("data sent to /bill/calculate-----");
            console.log(cartJson)
            $scope.invoiceSummary = buildInvoiceCalculateService.save(cartJson, function(){/* Success Callback */                	
            	$timeout(function(){
            		console.log("Success: data returned from /bill/calculate-----");
            		console.log($scope.invoiceSummary)
            		$scope.invoiceSummarySection = true;
            		angular.forEach($scope.invoiceSummary.orderItems, function(product){
                    	$scope.grandTotal =  $scope.grandTotal + (product.unitCost * product.quantity); 
                    	$scope.totalCartoons =  $scope.totalCartoons + product.quantity; 
                    });
            		$scope.grandTotal = ($scope.grandTotal).toFixed(6);
                    smoothScroll(document.getElementById('invoiceSummarySection')); /* Scroll to the invoice summary section */
                    $scope.successMessage = "Itemization list saved successfully";
                    $scope.showErrorBox = false;
                    $scope.showSuccessBox = true;
                    angular.element(document.querySelector('.loader')).removeClass('show');
                    $scope.searchProductSection = false;
                    $scope.addedProductsSection = false;                        
            	}, 500)
            }, function(error){/* Error Callback */
            	$timeout(function(){
            		console.log("Error: data returned from /bill/calculate-----");
            		console.log($scope.invoiceSummary)
                    smoothScroll(document.getElementsByTagName('body')); /* Scroll to top of the page */
                    $scope.errorMessage = "Itemization list could not be saved. Please try again after some time";
                    $scope.showErrorBox = true;
                    $scope.showSuccessBox = false;
                    angular.element(document.querySelector('.loader')).removeClass('show');     
            	}, 500)
            });
        };
        
        $scope.editInvoiceSummary = function(){
        	$scope.searchProductSection = true;
        	$scope.addedProductsSection = true;
        	$scope.invoiceSummarySection = false;
        	$scope.showSuccessBox = false;
        	$scope.showErrorBox = false;
        };
        	    
	    /* Function to generate PDF invoice for Current orders */
	    $scope.generatePDFInvoice = function(){
	    	angular.element(document.querySelector('.loader')).addClass('show'); 
	    	console.log("Invoice object-------")
	    	console.log($scope.invoiceSummary)
	    	response = pdfItemizationService.save($scope.invoiceSummary, function(){/* Success Callback */
	    		$scope.showSuccessBox = true;
            	$scope.successMessage = "PDF itemization for Customer " + $scope.customerShipmark + " generated successfully"
			    $scope.showErrorBox = false;                          
                smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
                angular.element(document.querySelector('.loader')).removeClass('show');
	    	}, function(){/* Error Callback */
	    		$scope.showErrorBox = true; 
		    	$scope.errorMessage = "PDF itemization for Customer " + $scope.customerShipmark + " could not be generated. Please try again after some time."
		    	$scope.showSuccessBox = false;
                smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
		    	angular.element(document.querySelector('.loader')).removeClass('show');
	    	});
	    };
	    
	    /* Function to generate Image invoice for Current orders */
    	$scope.generateImageInvoice = function(){
    		angular.element(document.querySelector('.loader')).addClass('show');
    		console.log($scope.invoiceSummary);
    		angular.forEach($scope.invoiceSummary.orderItems, function (product) {
                console.log("sfs "+product.unitCost);
            });
	    	response = imageItemizationService.save($scope.invoiceSummary, function(){/* Success Callback */ 
	    		console.log(response)
	    		$scope.showSuccessBox = true;
            	$scope.successMessage = "Image itemization for Customer " + $scope.customerShipmark + " generated successfully"
			    $scope.showErrorBox = false;                          
                smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
                angular.element(document.querySelector('.loader')).removeClass('show');
	    	}, function(){/* Error Callback */
	    		$scope.showErrorBox = true; 
		    	$scope.errorMessage = "Image itemization for Customer " + $scope.customerShipmark + " could not be generated. Please try again after some time."
		    	$scope.showSuccessBox = false;
                smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
		    	angular.element(document.querySelector('.loader')).removeClass('show');
	    	}); 
	    };
        
        $scope.cancelInvoicing = function () {  
        	angular.element(document.querySelector('.modal')).css('display', "none");    
            $timeout(function () {
            	$scope.showSuccessBox = false;
                $scope.showErrorBox = false;
            	$scope.customerShipmark = "";
                $scope.productsList = [];
                $scope.calcProductsList = [];
                $scope.invoiceSummary = [];
                $scope.productDetails = {};
                $scope.searchProductSection = false; /* Hide the search product section */
                $scope.productDetailsSection = false; /* Hide the Products details section */
                $scope.addedProductsSection = false; /* Hide the products' added table */
                $scope.invoiceSummarySection = false; /* Hide Customer invoice summary section */
                smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
                angular.element(document.querySelector('.loader')).removeClass('show');
            }, 500);
        };
	});
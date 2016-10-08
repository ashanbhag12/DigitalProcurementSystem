angular.module('buildOrderApp', ['angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('buildOrderController', function ($scope, $rootScope, $timeout, $q, smoothScroll, getCustomersForBuildOrderService, 
        		getSuppliersForBuildOrderService, getProductsForBuildOrderService, buildOrderCalculateService, saveOrderService) {

            $scope.showSuccessBox = false; /* Hide the success box */
            $scope.showErrorBox = false; /* Hide the error box */
            $scope.successMessage = ""; /* Set success message to blank */
            $scope.errorMessage = ""; /* Set error message to blank */
            $scope.editDisabled = true; /* Enable the Edit button for products added table */
            $scope.deleteDisabled = true; /* Disable the Delete button for products added table */
            $scope.searchProductSection = false; /* Hide the search product section */
            $scope.productDetailsSection = false; /* Hide the Products details section */
            $scope.addedProductsSection = false; /* Hide the products' added table */
            $scope.orderSummarySection = false; /* Hide Customer order summary section */
            $scope.productsList = []; /* List of Products Added in the table */
            $scope.calcProductsList = [];
            $scope.showAddBtn = true; /* Show Add Button & hide Update Button */
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.selectAll = false; /* Set toggle all to false */
            $scope.sortOrder = false; /* set the default sort order */
            $scope.sortType = 'productCode'; /* set the default sort type */
            $scope.orderDate = new Date(); /* By default set todays date as order date */
            $scope.querySearch = querySearch; /* Call the querySearch function for auto complete */
            $scope.searchText = null; /* Set search text as null */
            $scope.searchedProduct = null; /* Set searched product as null */
            $scope.allProducts; /* Get all the products for auto complete */ 
            $scope.orderSummary = []; /* Object containing all the products added to cart  */
            $scope.editOrderSummmaryRow = {}; /* Object for inline editing in Order Summary table */
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
            	/* List of all Customers */
                getCustomersForBuildOrderService.query().$promise.then(function(data) {
                	$scope.customers = data;
                }); 
                
                /* List of all Products */
                getProductsForBuildOrderService.query().$promise.then(function(data) {
                	$scope.allProducts = data;
                });                
            });

            $scope.showSearchSection = function () {
                if ($scope.customerShipmark !== undefined) {
                    $scope.searchProductSection = true;
                }
            };
            
            $scope.supplierProductPrice = function () {
            	angular.forEach($scope.productDetails.supplierProductInfoList, function (supplierProductInfo) {
            		if($scope.productDetails.supplierInitials === supplierProductInfo.supplierInitials){
            			$scope.productDetails.price = supplierProductInfo.supplierPrice;
            		}
            	});
            };

            /* Set variable for inline editing in order summary table */
            for (var i = 0; i < $scope.orderSummary.length; i++) {
                $scope.editOrderSummmaryRow[i] = false;
            }

            /* Search for products */
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
                    return (product.productCode.toLowerCase().indexOf(query.toLowerCase()) === 0);
                };
            }

            $scope.getProductDetails = function () {
                if ($scope.searchedProduct !== null) {
                	angular.element(document.querySelector('.loader')).addClass('show');                	
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
            
            /* Trigger addProduct() function on "enter" key press in remark field */
            $scope.triggerAddProduct = function(event){
            	if(event.which === 13) {
                    event.preventDefault();
                    $scope.addProduct();
                }
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
                    }, 100);
            	}, 500);                
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

            $scope.editAddedProductsListRow = function () {/* Function to edit the Product in added products table */
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

            $scope.updateAddedProductsListRow = function () {/* Function to update the Product in added products table */
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
            $scope.deleteAddedProductsListRow = function () {
                var newProductsList = [];      
                angular.forEach($scope.productsList, function (product) {
                    if (!product.isChecked) {
                        newProductsList.push(product);            
                    }
                });
                $scope.productsList = newProductsList;
                $scope.editDisabled = true;
                $scope.deleteDisabled = true;
            };

            $scope.submitCart = function () {
            	angular.element(document.querySelector('.loader')).addClass('show');                                   
                var cartJson = {
                		"customerShipmark" : $scope.customerShipmark,
                		"orderDate" : $scope.orderDate,
                		"orderItems" : $scope.calcProductsList
                	}                
                $scope.orderSummary = buildOrderCalculateService.save(cartJson, function(){/* Success Callback */                	
                	$timeout(function(){
                		$scope.orderSummarySection = true;
                        smoothScroll(document.getElementById('orderSummarySection')); /* Scroll to the order summary section */
                        $scope.successMessage = "Added products list saved successfully";
                        $scope.showErrorBox = false;
                        $scope.showSuccessBox = true;
                        angular.element(document.querySelector('.loader')).removeClass('show');
                        $scope.searchProductSection = false;
                        $scope.addedProductsSection = false;                        
                	}, 500)
                }, function(error){/* Error Callback */
                	$timeout(function(){
                        smoothScroll(document.getElementsByTagName('body')); /* Scroll to top of the page */
                        $scope.errorMessage = "Order cart could not be saved. Please try again after some time";
                        $scope.showErrorBox = true;
                        $scope.showSuccessBox = false;
                        angular.element(document.querySelector('.loader')).removeClass('show');     
                	}, 500)
                });
            };
            
            /* Function to calculate Grand Total of Order Summary */
            $scope.calculateGrandTotal = function(){
                var total = 0;
                angular.forEach($scope.orderSummary.orderItems, function(product){
                  total += (product.unitCost * product.quantity);                  
                });
                return total;
            };
            
            $scope.editOrder = function(){
            	$scope.addedProductsSection = true;
            	$scope.orderSummarySection = false;
            	$scope.showSuccessBox = false;
            };

            $scope.saveOrder = function () {    
            	angular.element(document.querySelector('.loader')).addClass('show');
            	saveOrderService.save($scope.orderSummary, function(){/* Success Callback */            		 
                     $timeout(function () {
                    	 $scope.productsList = [];
                    	 $scope.calcProductsList = [];
                    	 $scope.successMessage = "Order saved successfully";
                         $scope.showSuccessBox = true;
                         $scope.showErrorBox = false;
                         smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
                         $scope.addedProductsSection = false;
                         $scope.orderSummarySection = false;
                         angular.element(document.querySelector('.loader')).removeClass('show');
                     }, 500);
            	}, function(error){/* Error Callback */
            		$timeout(function () {
                   	 	$scope.errorMessage = "Order could not be saved. Please try again after some time";
                        $scope.showSuccessBox = false;
                        $scope.showErrorBox = true;
                        smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
                        angular.element(document.querySelector('.loader')).removeClass('show');
                    }, 500);
            	});   
            };

            $scope.cancelOrder = function () {
            	angular.element(document.querySelector('.modal')).css('display', "none");                
                $timeout(function () {
                	$scope.showSuccessBox = false;
                    $scope.showErrorBox = false;
                	$scope.customerShipmark = "";
                    $scope.productsList = [];
                    $scope.calcProductsList = [];
                    $scope.orderSummary = [];
                    $scope.productDetails = {};
                    $scope.searchProductSection = false; /* Hide the search product section */
                    $scope.productDetailsSection = false; /* Hide the Products details section */
                    $scope.addedProductsSection = false; /* Hide the products' added table */
                    $scope.orderSummarySection = false; /* Hide Customer order summary section */
                    smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
                    angular.element(document.querySelector('.loader')).removeClass('show');
                }, 500);
            };
        }); 
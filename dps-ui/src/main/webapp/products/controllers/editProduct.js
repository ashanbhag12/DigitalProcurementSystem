angular.module('editProductApp', ['ngMessages', 'angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('editProductController', function ($rootScope, $scope, $timeout, getProductsService, 
        		modifyProductsService, deleteProductsService, getSuppliersInitialsService, smoothScroll) {
            $scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.editDisabled = true; /* Disable the Edit button */
            $scope.deleteDisabled = true; /* Disable the Delete button */
            $scope.editProductForm = false; /* Hide the edit Product Form */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* set the default sort order */
            $scope.sortBy = 'name'; /* set the default sort type */
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.products = []; /* Array of all Products */            
            $scope.searchProductCode = ''; /* Code for product search */
            $scope.selectAll = false; /* Set toggle all to false */
            $scope.maskColumns = true; /* Hide the columns */
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {   
            	getSuppliersInitialsService.query().$promise.then(function(data) {
                	$scope.allSupplierInitials = data.map(function (initial) { return { abbrev: initial }; });
                });
            });

            /* Product object to be edited */
            /* changed name from editProduct with product because it was conflicting with form name */
            $scope.product = {                	
            		"id":"",
            		"productCode":"",
            		"cartoonQuantity":"",
            		"cbm":"",
            		"weight":"",
            		"description":"",
            		"moq":"",
            		"defaultMargin":"",
            		"supplierProductInfoList": [{
            			"supplierInitials": "",
            			"supplierProductCode": "",
            			"supplierPrice": ""
            		}, {
            			"supplierInitials": "",
            			"supplierProductCode": "",
            			"supplierPrice": ""
            		}, {
            			"supplierInitials": "",
            			"supplierProductCode": "",
            			"supplierPrice": ""
            		}],
            		"isValid":"false"
            };
            
            /* Function to select/unselect all the Products */
            $scope.toggleAll = function () {
                if ($scope.selectAll) {
                    $scope.selectAll = true;
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = false;
                    $scope.selectedRows = [];
                    angular.forEach($scope.products, function (product) {
                        product.isChecked = $scope.selectAll;
                        $scope.selectedRows.push(1);
                    });
                }
                else {
                    $scope.selectAll = false;
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = true;
                    angular.forEach($scope.products, function (product) {
                        product.isChecked = $scope.selectAll;
                        $scope.selectedRows = [];
                    });
                }
            };

            /* Function to select/unselect the Product */
            $scope.toggle = function (element) {
                if (element.isChecked) {
                    $scope.selectedRows.push(1);
                    $scope.editDisabled = false;
                    $scope.deleteDisabled = false;
                }
                else {
                    $scope.selectedRows.pop();
                }
                if ($scope.products.length === $scope.selectedRows.length) {
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
            
            /* Function to search for Products */
            $scope.searchProduct = function () {    
            	angular.element(document.querySelector('.loader')).addClass('show');
                /* Service Call to retrieve searched product */
                $scope.products = getProductsService.query({code:$scope.searchProductCode}, function(){/* Success Callback */
    	        	$timeout(function(){
    	        		$scope.searchedResults = true;
    	        		angular.element(document.querySelector('.loader')).removeClass('show');
    	        	}, 500);
    	        }, function(){ /* Error Callback */
    	        	$timeout(function(){
    	        		$scope.errorMessage = "Product not found. Please try again";
    	        		angular.element(document.querySelector('.loader')).removeClass('show');
    	        	}, 500);
    	        });
            };
            
            /* Concat all Supplier Initials and display in table */
            $scope.getSupplierInitials = function(supplierProductInfoList){            	
            	var supplierInitials = [];            	
            	angular.forEach(supplierProductInfoList, function (supplierProductInfo) {
            		supplierInitials.push(supplierProductInfo.supplierInitials);
            	});
            	return supplierInitials.join("; ");
            };
            
            /* Concat all Supplier Product Code and display in table */
            $scope.getSupplierProductCode = function(supplierProductInfoList){            	
            	var supplierProductCodes = [];            	
            	angular.forEach(supplierProductInfoList, function (supplierProductInfo) {
            		supplierProductCodes.push(supplierProductInfo.supplierProductCode);
            	});            	
            	return supplierProductCodes.join("; ");
            };   
            
            /* Concat all Supplier Product Price and display in table */
            $scope.getSupplierPrices = function(supplierProductInfoList){            	
            	var supplierProductPrices = [];            	
            	angular.forEach(supplierProductInfoList, function (supplierProductInfo) {
            		supplierProductPrices.push(supplierProductInfo.supplierPrice);
            	});            	
            	return supplierProductPrices.join("; ");
            };

            /* Function to edit the selected Product */
            $scope.edit = function () {
                angular.forEach($scope.products, function (product) {
                    if (product.isChecked) {
                    	smoothScroll(document.getElementById("editProductForm")); /* Scroll to the form */
                        $scope.editProductForm = true;
                        $scope.product.productCode = product.productCode;
                        $scope.product.cartoonQuantity = product.cartoonQuantity;
                        $scope.product.cbm = product.cbm;
                        $scope.product.weight = product.weight;
                        $scope.product.description = product.description;
                        $scope.product.moq = product.moq;
                        $scope.product.defaultMargin = product.defaultMargin;
                        for(i=0;i<3;i++){
                        	if(product.supplierProductInfoList[i] != undefined){
                            	$scope.product.supplierProductInfoList[i].supplierInitials = product.supplierProductInfoList[i].supplierInitials;
                                $scope.product.supplierProductInfoList[i].supplierProductCode = product.supplierProductInfoList[i].supplierProductCode;
                                $scope.product.supplierProductInfoList[i].supplierPrice = product.supplierProductInfoList[i].supplierPrice;;
                            }else{
                            	$scope.product.supplierProductInfoList[i].supplierInitials = "";
                                $scope.product.supplierProductInfoList[i].supplierProductCode = "";
                                $scope.product.supplierProductInfoList[i].supplierPrice = "";
                            }
                        }
                        if(product.isValid == true){
                        	$scope.product.isValid = "true";
                        }else{
                        	$scope.product.isValid = "false";
                        }
                    }
                });
            };

            /* Function to delete the selected Products */
            $scope.deleteProduct = function () {                
                angular.forEach($scope.products, function (product) {
                    if (product.isChecked) {
                    	angular.element(document.querySelector('.loader')).addClass('show');
                    	response = deleteProductsService.remove({productId : product.id}, function(){/* Success Callback */                    		
    	        	        $timeout(function () {	      
    	        	        	angular.element(document.querySelector('.modal')).css('display', "none");
    	        	            $scope.selectAll = false;
    	        	            /* WS call to get all Products */
    	                        $scope.products = getProductsService.query({code:$scope.searchProductCode}, function(){
    	                        	/* Success callback */
    	                        	$timeout(function () {
    	                        		$scope.selectedRows = [];
    	    	        	            $scope.editDisabled = true;
    	    	        	            $scope.deleteDisabled = true;
    	    	        	            $scope.showSuccessBox = true;
    	    	    			        $scope.successMessage = "Products deleted successfully";
    	    	    		            $scope.showErrorBox = false;
    	    	    		            angular.element(document.querySelector('.loader')).removeClass('show');
    	                        	}, 500);
    	                        }, function(){ /* Error callback */
    	                        	$timeout(function () {
    	                        		$scope.selectedRows = [];
    	    	        	            $scope.editDisabled = true;
    	    	        	            $scope.deleteDisabled = true;
    	    	        	            $scope.showErrorBox = true;
    	    	    			        $scope.errorMessage = "Could not retrieve products. Please try again after some time";
    	    	    		            $scope.showSuccessBox = false;
    	    	    		            angular.element(document.querySelector('.loader')).removeClass('show');
    	                        	}, 500);
    	                        });    	        	            
    	        	        }, 500);
                    	}, function(){/* Error Callback */
                    		angular.element(document.querySelector('.modal')).css('display', "none");     
                    		$timeout(function () {	                    			
    	            			$scope.selectAll = false;
    	        	            $scope.selectedRows = [];
    	        	            $scope.editDisabled = true;
    	        	            $scope.deleteDisabled = true;
    					    	$scope.showSuccessBox = false;
    				            $scope.showErrorBox = true;
    				            $scope.errorMessage = "Products could not be deleted. Please try again after some time";
    				            $rootScope.hideModal('deleteProductModal');
    					    }, 500);
                    	});
                    }
                });
            };

            /* Function to update the selected Product */
            /* added keepgoing check for performance. Because for very first product if isChecked condition is true, that product will be updated and for loop breaks
             * Note: no break; concept for angularJs forEach */
            
            $scope.update = function () {
            	var keepGoing = true;
                angular.forEach($scope.products, function (product) {
                	if(keepGoing) {
                		if (product.isChecked) {
                			angular.element(document.querySelector('.loader')).addClass('show');
                			
                			/* So that supplierProductInfoList is of size 3, having 3 objs */
                			product.supplierProductInfoList = [{
								                    			"supplierInitials": "",
								                    			"supplierProductCode": "",
								                    			"supplierPrice": ""
								                    		}, {
								                    			"supplierInitials": "",
								                    			"supplierProductCode": "",
								                    			"supplierPrice": ""
								                    		}, {
								                    			"supplierInitials": "",
								                    			"supplierProductCode": "",
								                    			"supplierPrice": ""
								                    		}],
                			
                            product.productCode = $scope.product.productCode;
                            product.cartoonQuantity = $scope.product.cartoonQuantity;
                            product.cbm = $scope.product.cbm;
                            product.weight = $scope.product.weight;
                            product.description = $scope.product.description;
                            product.moq = $scope.product.moq;
                            product.defaultMargin = $scope.product.defaultMargin;
                            product.supplierProductInfoList[0].supplierInitials = $scope.product.supplierProductInfoList[0].supplierInitials;
                            product.supplierProductInfoList[0].supplierProductCode = $scope.product.supplierProductInfoList[0].supplierProductCode;
                            product.supplierProductInfoList[0].supplierPrice = $scope.product.supplierProductInfoList[0].supplierPrice;
                            product.supplierProductInfoList[1].supplierInitials = $scope.product.supplierProductInfoList[1].supplierInitials;
                            product.supplierProductInfoList[1].supplierProductCode = $scope.product.supplierProductInfoList[1].supplierProductCode;
                            product.supplierProductInfoList[1].supplierPrice = $scope.product.supplierProductInfoList[1].supplierPrice;
                            product.supplierProductInfoList[2].supplierInitials = $scope.product.supplierProductInfoList[2].supplierInitials;
                            product.supplierProductInfoList[2].supplierProductCode = $scope.product.supplierProductInfoList[2].supplierProductCode;
                            product.supplierProductInfoList[2].supplierPrice = $scope.product.supplierProductInfoList[2].supplierPrice;
                            product.isValid = $scope.product.isValid;   
                            delete product.isChecked;	
                            keepGoing = false;
                            $scope.updateProductJson = angular.toJson(product); 
                        }                    
                	}
                });
                
                /* Service call to update product */
    		    response = modifyProductsService.save($scope.updateProductJson, function(){/* Success Callback */
    		    	$timeout(function () {		   
    		    		/* WS call to get all Products */
    	    		    $scope.products = getProductsService.query({code:$scope.searchProductCode});
    	    		    $scope.editProductForm = false;
    	                $scope.showSuccessBox = true;
    	                $scope.showErrorBox = false;
    			        $scope.selectAll = false;
        	            $scope.selectedRows = [];
        	            $scope.editDisabled = true;
        	            $scope.deleteDisabled = true;
    			        $scope.successMessage = "Product details updated successfully";		            
    		            smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
    		    		angular.element(document.querySelector('.loader')).removeClass('show');
    		    	}, 500);
    		    }, function(){/* Error Callback */
    		    	$timeout(function () {		   
    			    	$scope.showSuccessBox = false;
    			    	$scope.showErrorBox = true;
    			    	$scope.selectAll = false;
        	            $scope.selectedRows = [];
        	            $scope.editDisabled = true;
        	            $scope.deleteDisabled = true;
    			        $scope.errorMessage = "Product details could not be updated. Please try again after some time";	            
    		            smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
    		    		angular.element(document.querySelector('.loader')).removeClass('show');
    		    	}, 500);
    		    });
            };
            
            $scope.reset = function () {
                $scope.product = {};
                $scope.product.isValid="false";
                $scope.editProduct.$setPristine();
                $scope.editProduct.productCode.$touched = false;
                $scope.editProduct.cartoonQuantity.$touched = false;
                $scope.editProduct.cbm.$touched = false;
                $scope.editProduct.weight.$touched = false;
                $scope.editProduct.description.$touched = false;
                $scope.editProduct.moq.$touched = false;
                $scope.editProduct.defaultMargin.$touched = false;
                $scope.editProduct.supplierInitials1.$touched = false;
                $scope.editProduct.supplierProductCode1.$touched = false;
                $scope.editProduct.supplierPrice1.$touched = false;
                $scope.editProduct.supplierInitials2.$touched = false;
                $scope.editProduct.supplierProductCode2.$touched = false;
                $scope.editProduct.supplierPrice2.$touched = false;
                $scope.editProduct.supplierInitials3.$touched = false;
                $scope.editProduct.supplierProductCode3.$touched = false;
                $scope.editProduct.supplierPrice3.$touched = false;
                $scope.editProduct.isValid.$touched = false;                
                $scope.showSuccessBox = false;
            };	
        });
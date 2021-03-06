angular.module('editProductApp', ['ngMessages', 'angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('editProductController', function ($rootScope, $scope, $timeout, getProductsService, 
        		modifyProductsService, deleteProductsService, getSuppliersInitialsService, exportProductsToExcelService, smoothScroll) {
            $scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.editDisabled = true; /* Disable the Edit button */
            $scope.deleteDisabled = true; /* Disable the Delete button */
            $scope.editProductForm = false; /* Hide the edit Product Form */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* set the default sort order */
            $scope.sortBy = 'productCode'; /* set the default sort type */
            $scope.selectedRows = []; /* Array for toggleAll function */          
            $scope.products = []; /* Array of all Products */           
            $scope.searchProductCode = ''; /* Code for product search */
            $scope.selectAll = false; /* Set toggle all to false */
            $scope.maskColumns = true; /* Hide the columns */ 
            $scope.excelDisabled = true; /* Disable the Excel button */
            var scrollOptions = { /* Set offset to scroll to search table */
            	    offset: -185,
            	    callbackAfter: function(element) {
            	    	angular.element(document.getElementsByName("searchTableText")).focus();
            		}
            	};
            
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
            		"dummyCode":"",
            		"productCode":"",
            		"cartoonQuantity":"",
            		"cbm":"",
            		"weight":"",
            		"description":"",
            		"moq":"",
            		"defaultMargin":"",
            		"defaultMarginPercentage":"",
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
                    $scope.excelDisabled = false;
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
                    $scope.excelDisabled = true;
                    angular.forEach($scope.products, function (product) {
                        product.isChecked = $scope.selectAll;                        
                    });
                    $scope.selectedRows = [];
                }
            };

            /* Function to select/unselect the Product */
            $scope.toggle = function (element) {
                if (element.isChecked) {
                    $scope.selectedRows.push(1);
                    $scope.editDisabled = false;
                    $scope.deleteDisabled = false;
                    $scope.excelDisabled = false;
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
                    $scope.excelDisabled = true;
                }
                else if ($scope.selectedRows.length > 1) {
                    $scope.editDisabled = true;
                    $scope.excelDisabled = false;
                }
                else {
                    $scope.editDisabled = false;
                    $scope.excelDisabled = false;
                }
            };
            
            /* Function to search for Products */
            $scope.searchProduct = function () {    
            	angular.element(document.querySelector('.loader')).addClass('show');
                /* Service Call to retrieve searched product */
                $scope.products = getProductsService.query({code:$scope.searchProductCode}, function(){/* Success Callback */
    	        	$timeout(function(){
    	        		$scope.searchedResults = true;
    	        		$scope.selectAll = false;
    	        		$scope.selectedRows = [];
    	        		$scope.showSuccessBox = false; /* Hide the Success Box */
    	                $scope.showErrorBox = false; /* Hide the Error Box */
    	                $scope.excelDisabled = true; /* Disable the Excel button */
    	                $scope.editDisabled = true;
                        $scope.deleteDisabled = true;
    	        		angular.element(document.querySelector('.loader')).removeClass('show');
    	        		smoothScroll(document.getElementsByClassName("searchedResults"), scrollOptions); /* Scroll to the table */
    	        	}, 500);    	        	
    	        }, function(){ /* Error Callback */
    	        	$timeout(function(){
    	        		$scope.showSuccessBox = false; /* Hide the Success Box */
    	                $scope.showErrorBox = true; /* Hide the Error Box */
    	        		$scope.errorMessage = "Product not found. Please try again after some time";
    	        		angular.element(document.querySelector('.loader')).removeClass('show');
    	        		smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
    	        	}, 500);
    	        });
            };
            
            /* Trigger searchProduct() function on "enter" key press in search product field */
            $scope.triggerSearchProduct = function(event){
            	if(event.which === 13) {
                    event.preventDefault();
                    $scope.searchProduct();
                }
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

            /* Function to update Product Margin */
    	    $scope.updateProductMargin = function(){
    	    	$timeout(function(){
	    	    	$scope.product.defaultMargin = parseFloat($scope.product.defaultMarginPercentage);
	            	if($scope.product.defaultMargin >= 0){
	            		$scope.product.defaultMargin = (1 / (1 - (Math.abs($scope.product.defaultMargin)/100))).toFixed(6);
	        		}
	            	else{
	            		$scope.product.defaultMargin = (1 - (Math.abs($scope.product.defaultMargin)/100)).toFixed(6);            		
	            	}
    	    	},0);
    	    };
            
            /* Function to edit the selected Product */
            $scope.edit = function () {
                angular.forEach($scope.products, function (product) {
                    if (product.isChecked) {
                    	smoothScroll(document.getElementById("editProductForm")); /* Scroll to the form */
                        $scope.editProductForm = true;
                        $scope.product.dummyCode = product.dummyCode;
                        $scope.product.productCode = product.productCode;
                        $scope.product.cartoonQuantity = product.cartoonQuantity;
                        $scope.product.cbm = product.cbm;
                        $scope.product.weight = product.weight;
                        $scope.product.description = product.description;
                        $scope.product.moq = product.moq;
                        $scope.product.defaultMarginPercentage = product.defaultMarginPercentage;
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
                        $timeout(function () {
                        	angular.element(document.getElementsByName("productCode")).focus();
                        }, 500);
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
    	    	    		            angular.element(document.getElementsByName("searchTableText")).focus();
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
            	$scope.updateProductMargin();
            	$timeout(function(){
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
                    			
    							product.dummyCode = $scope.product.dummyCode;
                                product.productCode = $scope.product.productCode;
                                product.cartoonQuantity = $scope.product.cartoonQuantity;
                                product.cbm = $scope.product.cbm;
                                product.weight = $scope.product.weight;
                                product.description = $scope.product.description;
                                product.moq = $scope.product.moq;
                                product.defaultMargin = $scope.product.defaultMargin;
                                product.defaultMarginPercentage = $scope.product.defaultMarginPercentage;
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
        		    		angular.element(document.getElementsByName("searchTableText")).focus();
        		    	}, 500);
        		    }, function(){/* Error Callback */
        		    	$timeout(function () {		   
        			    	$scope.showSuccessBox = false;
        			    	$scope.showErrorBox = true;
        			    	$scope.selectAll = false;
            	            $scope.selectedRows = [];
            	            $scope.editDisabled = true;
            	            $scope.deleteDisabled = true;
            	            $scope.excelDisabled = true; /* Disable the Excel button */
        			        $scope.errorMessage = "Product details could not be updated. Please try again after some time";	            
        		            smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
        		    		angular.element(document.querySelector('.loader')).removeClass('show');
        		    	}, 500);
        		    });
            	},50);            	
            };
            
            $scope.cancel = function () {
            	$scope.editProductForm = false;
            	$scope.selectAll = false;
            	$scope.selectedRows = [];
            	angular.forEach($scope.products, function (product) {
                    product.isChecked = $scope.selectAll;                    
                });
            	$scope.editDisabled = true;
                $scope.deleteDisabled = true;
            	smoothScroll(document.getElementsByTagName('body')); /* Scroll to top of the page */
            	angular.element(document.getElementsByName("searchTableText")).focus();
            };
            
            /* Function to export selected products to Excel */
            $scope.exportToExcel = function(){
            	angular.element(document.querySelector('.loader')).addClass('show');
			    response = exportProductsToExcelService.save($scope.products, function(){/* Success Callback */
			    	$timeout(function () {
	                    $scope.showSuccessBox = true;
	                    $scope.successMessage = "Products exported to excel successfully";
					    $scope.showErrorBox = false;				    
					    smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
					    angular.element(document.querySelector('.loader')).removeClass('show');
					    angular.element(document.getElementsByName("searchTableText")).focus();
	                }, 500);
			    }, function(error){/* Error Callback */			    	
			    	$timeout(function () {
			    		$scope.showErrorBox = true; 
				    	$scope.errorMessage = "Products could not be exported to excel. Please try again after some time";
				    	$scope.showSuccessBox = false;
			    		smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
	                    angular.element(document.querySelector('.loader')).removeClass('show');
	                }, 500);
			    });  
            };
        });
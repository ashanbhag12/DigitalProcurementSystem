angular.module('editProductApp', ['ngMessages', 'angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('editProductController', function ($rootScope, $scope, $timeout, getProductsService, 
        		modifyProductsService, deleteProductsService, getSuppliersInitialsService, smoothScroll) {
            $scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.editDisabled = false; /* Enable the Edit button */
            $scope.deleteDisabled = true; /* Disable the Delete button */
            $scope.editProductForm = false; /* Hide the edit Product Form */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* set the default sort order */
            $scope.sortBy = 'name'; /* set the default sort type */
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.products = []; /* Array of all Products */            
            $scope.searchProductCode = ''; /* Code for product search */
            
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
            		"price":"",
            		"weight":"",
            		"description":"",
            		"moq":"",
            		"defaultMargin":"",
            		"supplierProductInfoList": [{
            			"supplierInitials": "",
            			"supplierProductCode": ""
            		}, {
            			"supplierInitials": "",
            			"supplierProductCode": ""
            		}, {
            			"supplierInitials": "",
            			"supplierProductCode": ""
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

            /* Function to edit the selected Product */
            $scope.edit = function () {
                angular.forEach($scope.products, function (product) {
                    if (product.isChecked) {
                    	smoothScroll(document.getElementById("editProductForm")); /* Scroll to the form */
                        $scope.editProductForm = true;
                        $scope.product.productCode = product.productCode;
                        $scope.product.cartoonQuantity = product.cartoonQuantity;
                        $scope.product.cbm = product.cbm;
                        $scope.product.price = product.price;
                        $scope.product.weight = product.weight;
                        $scope.product.description = product.description;
                        $scope.product.moq = product.moq;
                        $scope.product.defaultMargin = product.defaultMargin;
                        $scope.product.supplierProductInfoList[0].supplierInitials = product.supplierProductInfoList[0].supplierInitials;
                        $scope.product.supplierProductInfoList[0].supplierProductCode = product.supplierProductInfoList[0].supplierProductCode;
                        $scope.product.supplierProductInfoList[1].supplierInitials = product.supplierProductInfoList[1].supplierInitials;
                        $scope.product.supplierProductInfoList[1].supplierProductCode = product.supplierProductInfoList[1].supplierProductCode;
                        $scope.product.supplierProductInfoList[2].supplierInitials = product.supplierProductInfoList[2].supplierInitials;
                        $scope.product.supplierProductInfoList[2].supplierProductCode = product.supplierProductInfoList[2].supplierProductCode;
                        $scope.product.isValid = "false";
                    }
                });
            };

            /* Function to delete the selected Products */
            $scope.deleteProduct = function () {                
                angular.forEach($scope.products, function (product) {
                    if (product.isChecked) {
                    	angular.element(document.querySelector('.loader')).addClass('show');
                    	response = deleteProductsService.remove({productId : product.id}, function(){/* Success Callback */
                    		angular.element(document.querySelector('.modal')).css('display', "none");
    	        	        $timeout(function () {	        	            
    	        	            $scope.selectAll = false;
    	        	            /* WS call to get all Products */
    	                        $scope.products = getProductsService.query({code:$scope.searchProductCode});
    	        	            $scope.selectedRows = [];
    	        	            $scope.editDisabled = true;
    	        	            $scope.deleteDisabled = true;
    	        	            $scope.showSuccessBox = true;
    	    			        $scope.successMessage = "Products deleted successfully";
    	    		            $scope.showErrorBox = false;
    	    		            angular.element(document.querySelector('.loader')).removeClass('show');
    	        	        }, 500);
                    	}, function(){/* Error Callback */
                    		$timeout(function () {	
    	            			$scope.selectAll = false;
    	        	            $scope.selectedRows = [];
    	        	            $scope.editDisabled = true;
    	        	            $scope.deleteDisabled = true;
    					    	$scope.showSuccessBox = false;
    				            $scope.showErrorBox = true;
    				            $scope.errorMessage = "Products could not be deleted. Please try again after some time";
    				            angular.element(document.querySelector('.loader')).removeClass('show');
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
                            product.productCode = $scope.product.productCode;
                            product.cartoonQuantity = $scope.product.cartoonQuantity;
                            product.cbm = $scope.product.cbm;
                            product.price = $scope.product.price;
                            product.weight = $scope.product.weight;
                            product.description = $scope.product.description;
                            product.moq = $scope.product.moq;
                            product.defaultMargin = $scope.product.defaultMargin;
                            product.supplierProductInfoList[0].supplierInitials = $scope.product.supplierProductInfoList[0].supplierInitials;
                            product.supplierProductInfoList[0].supplierProductCode = $scope.product.supplierProductInfoList[0].supplierProductCode;
                            product.supplierProductInfoList[1].supplierInitials = $scope.product.supplierProductInfoList[1].supplierInitials;
                            product.supplierProductInfoList[1].supplierProductCode = $scope.product.supplierProductInfoList[1].supplierProductCode;
                            product.supplierProductInfoList[2].supplierInitials = $scope.product.supplierProductInfoList[2].supplierInitials;
                            product.supplierProductInfoList[2].supplierProductCode = $scope.product.supplierProductInfoList[2].supplierProductCode;
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

            /* Function to search for Products */
            $scope.searchProduct = function () {
                $scope.selectAll = false;
                $scope.searchedResults = true;
                $scope.editDisabled = true;
                $scope.deleteDisabled = true;
                
                /* Service Call to retrieve searched product */
                $scope.products = getProductsService.query({code:$scope.searchProductCode});
            };

            /* Global function to show Modal Window */
            $rootScope.showModal = function () {
                angular.element(document.querySelector('.loader')).addClass('show');
                angular.element(document.querySelector('.modal')).css('display', "block");
            };

            /* Global function to hide Modal Window */
            $rootScope.hideModal = function () {
                angular.element(document.querySelector('.loader')).removeClass('show');
                angular.element(document.querySelector('.modal')).css('display', "none");
            };
            
            $scope.reset = function () {
                $scope.product = {};
                $scope.product.isValid="false";
                $scope.editProduct.$setPristine();
                $scope.editProduct.productCode.$touched = false;
                $scope.editProduct.cartoonQuantity.$touched = false;
                $scope.editProduct.cbm.$touched = false;
                $scope.editProduct.price.$touched = false;
                $scope.editProduct.weight.$touched = false;
                $scope.editProduct.description.$touched = false;
                $scope.editProduct.moq.$touched = false;
                $scope.editProduct.defaultMargin.$touched = false;
                $scope.editProduct.supplierInitials1.$touched = false;
                $scope.editProduct.supplierProductCode1.$touched = false;
                $scope.editProduct.supplierInitials2.$touched = false;
                $scope.editProduct.supplierProductCode2.$touched = false;
                $scope.editProduct.supplierInitials3.$touched = false;
                $scope.editProduct.supplierProductCode3.$touched = false;
                $scope.editProduct.isValid.$touched = false;                
                $scope.showSuccessBox = false;
            };
        });
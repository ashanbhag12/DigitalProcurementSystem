angular.module('addProductApp', ['ngMessages', 'smoothScroll'])
        .controller('addProductController', function ($scope, $timeout, addProductsService, getSuppliersInitialsService, smoothScroll) {
            $scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            
            $scope.product = { /* Product Object */                	
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
            
            /* Function will be executed after the page is loaded */
    	    $scope.$on('$viewContentLoaded', function () {
    	    	// WebService should be called to fetch initials for Suppliers
                getSuppliersInitialsService.query().$promise.then(function(data) {
                	$scope.allSupplierInitials = data.map(function (initial) { return { abbrev: initial }; });
                });
    	    });
    	    
    	    /* Function to update Calculate Product Margin */
    	    $scope.calculateProductMargin = function(){
    	    	$scope.product.defaultMargin = parseFloat($scope.product.defaultMarginPercentage);
            	if($scope.product.defaultMargin >= 0){
            		$scope.product.defaultMargin = (1 / (1 - (Math.abs($scope.product.defaultMargin)/100))).toFixed(3);
        		}
            	else{
            		$scope.product.defaultMargin = 1 - (Math.abs($scope.product.defaultMargin)/100);            		
            	}
    	    };

            $scope.submitForm = function (addProduct) {
                if (addProduct.$valid) {
                	angular.element(document.querySelector('.loader')).addClass('show'); 
        		    response = addProductsService.save($scope.product, function(){/* Success Callback */
        		    	$timeout(function () {                            
        		    		$scope.reset();
                		    $scope.showSuccessBox = true;
                            $scope.showErrorBox = false;
                            smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
    					    angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);        		    	
        		    }, function(){/* Error Callback */        		    	
        		    	$timeout(function () {
        		    		$scope.showErrorBox = true; 
    				    	$scope.showSuccessBox = false;
        		    		smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);        		    	
        		    });  
                }               
            };

            $scope.reset = function () {
            	$scope.product = { /* Reset the Product Object */                	
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
                $scope.product.isValid="false";
                $scope.addProduct.$setPristine();
                $scope.addProduct.productCode.$touched = false;
                $scope.addProduct.cartoonQuantity.$touched = false;
                $scope.addProduct.cbm.$touched = false;
                $scope.addProduct.weight.$touched = false;
                $scope.addProduct.description.$touched = false;
                $scope.addProduct.moq.$touched = false;
                $scope.addProduct.defaultMarginPercentage.$touched = false;
                $scope.addProduct.supplierInitials1.$touched = false;
                $scope.addProduct.supplierProductCode1.$touched = false;
                $scope.addProduct.supplierPrice1.$touched = false;
                $scope.addProduct.supplierInitials2.$touched = false;
                $scope.addProduct.supplierProductCode2.$touched = false;
                $scope.addProduct.supplierPrice2.$touched = false;
                $scope.addProduct.supplierInitials3.$touched = false;
                $scope.addProduct.supplierProductCode3.$touched = false;
                $scope.addProduct.supplierPrice3.$touched = false;
                $scope.addProduct.isValid.$touched = false;
                $scope.showSuccessBox = false;
                $scope.showErrorBox = false;
                
            };
        });
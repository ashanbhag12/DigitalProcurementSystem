angular.module('viewSupplierOrderApp', ['smoothScroll', 'angularUtils.directives.dirPagination'])
        .controller('viewSupplierOrderController', function ($scope, $rootScope, $timeout, smoothScroll, 
        		getSupplierOrderService, getSuppliersService, exportToExcelService) {
        	$scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.maskColumns = true; /* Hide the columns */ 
            $scope.isAccordionOpen = false; /* Collapse all the accordions */
            $scope.suppliers = []; /* Array of all Suppliers */ 
            $scope.supplierOrders = []; /* Object for all supplier orders */
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {  
            	/* WS call to fetch all suppliers */
            	getSuppliersService.query().$promise.then(function(data) {
            		$scope.suppliers = data;
            	});
            });
            
            /* Function to set search date to past 3/6 months */
            $scope.setSearchDate = function(monthsToAdd){
            	$scope.orderEndDate = new Date();
            	if(monthsToAdd === 3){
            		$scope.orderStartDate = new Date(new Date().setMonth(new Date().getMonth() - 3));
            	}
            	else{            		
            		$scope.orderStartDate = new Date(new Date().setMonth(new Date().getMonth() - 6));
            	}
            };
            
            /* Function to search for Products */
            $scope.getSupplierOrders = function () {
                if ($scope.supplierInitials !== undefined) {
                	angular.element(document.querySelector('.loader')).addClass('show');
                    /* Service Call to retrieve all products */
                	$scope.supplierOrders = getSupplierOrderService.query({supplierInitials:$scope.supplierInitials, startDate:Date.parse($scope.orderStartDate), endDate:Date.parse($scope.orderEndDate)}, function(){/* Success Callback */
                    	$timeout(function () {
                    		$scope.searchedResults = true;      
                    		$scope.showErrorBox = false;                   
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
                    }, function(error){/* Error Callback */
                    	$timeout(function () {
                    		$scope.showErrorBox = true;
	                		$scope.errorMessage = "Supplier order could not be retrieved. Please try after some time";
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
                    });
                }
            };
            
            /* Function to export Supplier Order to Excel */
            $scope.exportToExcel = function(selectedOrder, selectedOrderIndex){
            	angular.forEach(selectedOrder.details, function (product) {
            		delete product.isChecked;
	            });
            	angular.element(document.querySelector('.loader')).addClass('show'); 
    		    response = exportToExcelService.query({id:selectedOrder.id}, function(){/* Success Callback */
    		    	$timeout(function () {
                        $scope.showSuccessBox = true;
                        $scope.successMessage = "Supplier order exported to excel successfully"
    				    $scope.showErrorBox = false;                        
    				    angular.element(document.querySelector('.loader')).removeClass('show');
    				    smoothScroll(document.getElementById("viewSupplierOrderPage")); /* Scroll to top of the page */
                    }, 500);
    		    }, function(error){/* Error Callback */    		    	
    		    	$timeout(function () {
    		    		$scope.showErrorBox = true; 
        		    	$scope.errorMessage = "Supplier order could not be exported to excel. Please try again after some time."
        		    	$scope.showSuccessBox = false;
                        angular.element(document.querySelector('.loader')).removeClass('show');
                        smoothScroll(document.getElementById("viewSupplierOrderPage")); /* Scroll to top of the page */
                    }, 500);
    		    });    		  
            };
        }); 
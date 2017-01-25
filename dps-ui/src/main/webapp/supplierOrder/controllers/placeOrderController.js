angular.module('placeOrderApp', [])
        .controller('placeOrderController', function ($rootScope, $http, $scope, $timeout, getPlaceOrderService, savePlaceOrderService) {
            $scope.showSuccessBox = false; /* Hide the Success box */
            $scope.showErrorBox = false; /* Hide the Error box */
            $scope.showInfoBox = false; /* Hide the Info box */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.modalSortOrder = false; /* Set the default sort order in the popup*/
            $scope.modalSortType = 'productCode'; /* Set the default sort type in the popup */
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.products = []; /* Object containing all products data */
            $scope.selectAll = false; /* Set toggle all to false */
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
                /* WS call to fetch the data */
            	getPlaceOrderService.query().$promise.then(function(data) {            		
                	$scope.products = data;
                	
                	/* To select the rows which fulfills MOQ */
	                angular.forEach($scope.products, function (product) {
	                    if (product.toOrder) {
	                        $scope.selectedRows.push(1);
	                    }
	                });
	                
	                /* Set selectAll true if all products are selected */
	                if ($scope.products.length === $scope.selectedRows.length && $scope.products.length !== 0) {
	    	            $scope.selectAll = true;	    	            
	    	        }
                });
            });

            /* Function to select/unselect all the Suppliers */
            $scope.toggleAll = function () {
                if ($scope.selectAll) {
                    $scope.selectAll = true;
                    $scope.selectedRows = [];
                    angular.forEach($scope.products, function (product) {
                        product.toOrder = $scope.selectAll;                        
                        $scope.selectedRows.push(1);
                    });
                }
                else {
                    $scope.selectAll = false;
                    angular.forEach($scope.products, function (product) {
                        product.toOrder = $scope.selectAll;
                        $scope.selectedRows.pop();
                    });
                    $scope.selectedRows = [];
                }
            };

            /* Function to select/unselect the Supplier */
            $scope.toggle = function (element) {
                if (element.toOrder) {
                    $scope.selectedRows.push(1);
                }
                else {
                    $scope.selectedRows.pop();
                }
                $scope.selectAll = ($scope.selectedRows.length === $scope.products.length);
            };

            $scope.placeOrder = function () {
            	angular.element(document.querySelector('.modal')).css('display', "none");
			    response = savePlaceOrderService.save($scope.products, function(){/* Success Callback */
			    	$timeout(function () {
	                    $scope.showSuccessBox = true;
					    $scope.showErrorBox = false;
					    /* To reload data in to the table by removing placed order items. */ 
	                    getPlaceOrderService.query().$promise.then(function(data) {
	                    	$scope.products = data;	             
	                    	$scope.selectedRows = []; /* Reset the object */
	                		/* To select the rows which fulfills MOQ */
	    	                angular.forEach($scope.products, function (product) {
	    	                    if (product.toOrder) {
	    	                        $scope.selectedRows.push(1);
	    	                    }
	    	                });	        
	    	                /* Set selectAll true if all products are selected */
	    	                if ($scope.products.length === $scope.selectedRows.length && $scope.products.length !== 0) {
	    	    	            $scope.selectAll = true;	    	            
	    	    	        }
	    	                else{
	    	                	$scope.selectAll = false;
	    	                }
	                    });					    
					    angular.element(document.querySelector('.loader')).removeClass('show');
	                }, 500);
			    }, function(error){/* Error Callback */
			    	$scope.showErrorBox = true; 
			    	$scope.showSuccessBox = false;
			    	$timeout(function () {
	                    angular.element(document.querySelector('.loader')).removeClass('show');
	                }, 500);
			    });
            };
        });
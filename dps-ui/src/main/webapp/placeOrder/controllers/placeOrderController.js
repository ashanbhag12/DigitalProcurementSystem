angular.module('placeOrderApp', [])
        .controller('placeOrderController', function ($http, $scope, $timeout, getPlaceOrderService) {
            $scope.showSuccessBox = false; /* Hide the Success box */
            $scope.showInfoBox = false; /* Hide the Info box */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.products = []; /* Object containing all products data */
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
                /* WS call to fetch the data */
            	getPlaceOrderService.query().$promise.then(function(data) {
            		console.log(data)
                	$scope.products = data;
                });

                /* To select the rows which fulfills MOQ */
                angular.forEach($scope.products, function (product) {
                    if (product.isChecked) {
                        $scope.selectedRows.push(1);
                    }
                });
            });

            /* Function to select/unselect all the Suppliers */
            $scope.toggleAll = function () {
                if ($scope.selectAll) {
                    $scope.selectAll = true;
                    $scope.selectedRows = [];
                    angular.forEach($scope.products, function (product) {
                        product.isChecked = $scope.selectAll;                        
                        $scope.selectedRows.push(1);
                    });
                }
                else {
                    $scope.selectAll = false;
                    angular.forEach($scope.products, function (product) {
                        product.isChecked = $scope.selectAll;
                        $scope.selectedRows.pop();
                    });
                }
            };

            /* Function to select/unselect the Supplier */
            $scope.toggle = function (element) {
                if (element.isChecked) {
                    $scope.selectedRows.push(1);
                }
                else {
                    $scope.selectedRows.pop();
                }
                $scope.selectAll = ($scope.selectedRows.length === $scope.products.length);
            };

            $scope.placeOrder = function () {
            	angular.element(document.querySelector('.loader')).addClass('show');
            	$scope.showInfoBox = true;
                /* WS Call to fetch excel from Products selected */
            	$http({
            	    url: 'http://localhost:8080/dps-web-service-0.0.1/rest/placeorder/save',
            	    method: 'POST',
            	    responseType: 'arraybuffer',
            	    data: $scope.products, //this is your json data string
            	    headers: {
            	        'Content-type': 'application/json'
            	    }
            	}).success(function(data){
            		$timeout(function(){ 
	            	    var blob = new Blob([data], {
	            	        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
	            	    });
	            	    saveAs(blob, 'File_Name_With_Some_Unique_Id_Time' + '.xls');
	            	    
	            	    $scope.showSuccessBox = true;
	                    $scope.showInfoBox = false;
	                    angular.element(document.querySelector('.loader')).removeClass('show');
	                    
	                    /* To reload data in to the table by removing placed order items. */ 
                        getPlaceOrderService.query().$promise.then(function(data) {
                    		console.log(data)
                        	$scope.products = data;
                        });
                        
	                    console.log(data);
            		}, 500)
            	}).error(function(error){/* Error Callback */
            		$timeout(function(){ 
                        console.log(error)
                        angular.element(document.querySelector('.loader')).removeClass('show');
            		}, 500)
            	});
            };
        });
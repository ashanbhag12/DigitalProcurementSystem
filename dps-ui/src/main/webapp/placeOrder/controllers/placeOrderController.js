angular.module('placeOrderApp', [])
        .controller('placeOrderController', function ($scope, $timeout, getPlaceOrderService, savePlaceOrderService) {
            $scope.showSuccessBox = false; /* Hide the Success box */
            $scope.showInfoBox = false; /* Hide the Info box */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.products = []; /* Object containing all products data */
            
            getPlaceOrderService.query().$promise.then(function(data) {
            	$scope.products = data;
            }); 
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
                /* WS call to fetch the data */

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
                /* WS Call to fetch excel from Products selected */
            	savePlaceOrderService.save($scope.products);
            	
                $scope.showInfoBox = true;
                angular.element(document.querySelector('.loader')).addClass('show');
                $timeout(function () {
                    $scope.showSuccessBox = true;
                    $scope.showInfoBox = false;
                    angular.element(document.querySelector('.loader')).removeClass('show');
                }, 3000);
            };
        });
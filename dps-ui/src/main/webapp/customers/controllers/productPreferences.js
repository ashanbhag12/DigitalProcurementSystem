angular.module('productPreferencesApp', ['angularUtils.directives.dirPagination'])
        .controller('productPreferencesController', function ($scope, $rootScope, $timeout, getCustomersProductPreferencesService,
        		getProductPreferencesService, modifyProductPreferencesService ) {
            /* Initialize the page variables */
            $scope.showSuccessBox = false; /* Hide the error messages */
            $scope.showErrorBox = false; /* Hide the success messages */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.products = [];
            
            getCustomersProductPreferencesService.query().$promise.then(function(data) {
            	$scope.customers = data;
            });

            $scope.editProductDetailsRow = {}; /* Object for inline editing in Order Summary table */

            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
                /* WS call to fetch all customers*/
            });

            /* Function to search for Products */
            $scope.getProductDetails = function () {
                if ($scope.customerShipmark !== undefined) {
                    $scope.searchedResults = true;
                    /* Service Call to retrieve all products */
                    $scope.products = getProductPreferencesService.get({shipmark : $scope.customerShipmark})
                }
            };

            /* Set variable for inline editing in order summary table */
            for (var i = 0; i < $scope.products.length; i++) {
                $scope.editProductDetailsRow[i] = false;
            }

            $scope.editProductDetails = function (index, product) {
                $scope.editProductDetailsRow[index] = true;
            };

            $scope.updateProductDetails = function (index, product) {
                product.cost = (product.productPrice * product.productMargin * product.customerMargin * product.customerProductMargin).toFixed(2);
                $scope.editProductDetailsRow[index] = false;
            };

            $scope.saveProductDetails = function () {
                /* WS call to save the changes and update the table */
                $scope.showSuccessBox = true;
                $timeout(function () {
                    $scope.showSuccessBox = false;
                }, 5000);
                
                modifyProductPreferencesService.save($scope.products,
                	function(successResult) {
                		$scope.products = getProductPreferencesService.get({shipmark : $scope.customerShipmark})
                	});
            };

            $scope.cancelChanges = function () {/* Cancel the changes and hide the table */
                $scope.showSuccessBox = false;
                $scope.searchedResults = false;
                $scope.customerShipmark = "";
            };
        });
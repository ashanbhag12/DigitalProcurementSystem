angular.module('productPreferencesApp', ['angularUtils.directives.dirPagination'])
        .controller('productPreferencesController', function ($scope, $rootScope, $timeout) {
            /* Initialize the page variables */
            $scope.showSuccessBox = false; /* Hide the error messages */
            $scope.showErrorBox = false; /* Hide the success messages */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.customers = ["Customer1", "Customer2", "Customer3"]; /* List of all Customers */
            $scope.products = [{/* List of all Products received from WS call */
                "id": "P101",
                "productCode": "P101",
                "pricePerItem": "100",
                "productMargin": "1.00",
                "customerMargin": "1.00",
                "customerProductMargin":"1.00",
                "cost":"1"
            },            
            {
                "id": "P102",
                "productCode": "P102",
                "pricePerItem": "200",
                "productMargin": "1.00",
                "customerMargin": "1.00",
                "customerProductMargin":"1.00",
                "cost":"2"
            },
            {
                "id": "P103",
                "productCode": "P103",
                "pricePerItem": "300",
                "productMargin": "1.00",
                "customerMargin": "1.00",
                "customerProductMargin":"1.00",
                "cost":"3"
            }];
            $scope.customerProductPreferences = { /* List of Products to be sent via WS after preferences are added */
                "customerId": "",
                "products": []
            };

            $scope.editProductDetailsRow = {}; /* Object for inline editing in Order Summary table */

            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
                /* WS call to fetch all customers*/
            });

            /* Function to search for Products */
            $scope.getProductDetails = function () {
                if ($scope.customerName !== undefined) {
                    $scope.searchedResults = true;
                    /* Service Call to retrieve all products */
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
                product.cost = product.pricePerItem * product.productMargin * product.customerMargin * product.customerProductMargin;
                $scope.editProductDetailsRow[index] = false;
            };

            $scope.saveProductDetails = function () {
                /* WS call to save the changes and update the table */
                $scope.showSuccessBox = true;
                $timeout(function () {
                    $scope.showSuccessBox = false;
                }, 5000);
                $scope.customerProductPreferences.customerId = $scope.customerName;
                $scope.customerProductPreferences.products = angular.copy($scope.products);
                console.log($scope.customerProductPreferences);
            };

            $scope.cancelChanges = function () {/* Cancel the changes and hide the table */
                $scope.showSuccessBox = false;
                $scope.searchedResults = false;
                $scope.customerName = "";
            };
        });
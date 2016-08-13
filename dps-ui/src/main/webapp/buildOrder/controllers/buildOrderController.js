angular.module('buildOrderApp', ['angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('buildOrderController', function ($scope, $rootScope, $timeout, $q, smoothScroll) {

            $scope.showSuccessBox = false; /* Hide the success box */
            $scope.successMessage = ""; /* Set success message to blank */
            $scope.searchProductSection = false; /* Hide the search product section */
            $scope.productDetailsSection = false; /* Hide the Products details section */
            $scope.addedProductsSection = false; /* Hide the products' added table */
            $scope.orderSummarySection = false; /* Hide Customer order summary section */
            $scope.productsList = []; /* List of Products Added in the table */
            $scope.editDisabled = false; /* Enable the product edit button in added products table */
            $scope.showAddBtn = true; /* Show Add Button & hide Update Button */
            $scope.sortOrder = false; /* set the default sort order */
            $scope.sortType = 'productCode'; /* set the default sort type */
            $scope.productDetails = {/* Object to show product details based on autocomplete result */
                "productCode": "P101",
                "supplierCode": "Supplier1",
                "pricePerPiece": "24",
                "productMOQ": 3,
                "remarks": "",
                "productQuantity": ""
            };
            $scope.customers = ["Customer1", "Customer2", "Customer3"]; /* List of all Customers */
            $scope.suppliers = ["Supplier1", "Supplier2", "Supplier3"]; /* List of all Suppliers */
            $scope.orderDate = new Date(); /* By default set todays date as order date */
            $scope.querySearch = querySearch; /* Call the querySearch function for autocomplete */
            $scope.searchText = null; /* Set search text as null */
            $scope.searchedProduct = null; /* Set searched product as null */
            $scope.allProducts = [{/* Get all the products for autocomplete */
                    "id": "ABC",
                    "text": "Product1",
                    "value": "product1"
                },
                {
                    "id": "BCD",
                    "text": "Product2",
                    "value": "product2"
                },
                {
                    "id": "DEF",
                    "text": "Product3",
                    "value": "product3"
                }
            ];
            $scope.orderSummary = [/* Object containing all the products added to cart */
                {
                    "productCode": "P101",
                    "supplierCode": "S101",
                    "productMOQ": 3,
                    "productQuantity": 4,
                    "productMargin": 1.00
                },
                {
                    "productCode": "P102",
                    "supplierCode": "S102",
                    "productMOQ": 2,
                    "productQuantity": 3,
                    "productMargin": .98
                }
            ];
            $scope.editOrderSummmaryRow = {}; /* Object for inline editing in Order Summary table */

            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
                console.log("page loaded");
            });

            $scope.showSearchSection = function () {
                if ($scope.customerName !== undefined) {
                    $scope.searchProductSection = true;
                }
            };

            /* Set variable for inline editing in order summary table */
            for (var i = 0; i < $scope.orderSummary.length; i++) {
                $scope.editOrderSummmaryRow[i] = false;
            }

            $scope.editCartRow = function (index) {
                $scope.editOrderSummmaryRow[index] = true;
            };

            $scope.updateCartRow = function (index) {
                $scope.editOrderSummmaryRow[index] = false;
            };

            /**
             * Search for products... use $timeout to simulate
             * remote dataservice call.
             */
            function querySearch(query) {
                var results = query ? $scope.allProducts.filter(createFilterFor(query)) : $scope.allProducts;
                var deferred = $q.defer();
                $timeout(function () {
                    deferred.resolve(results);
                }, Math.random() * 1000, false);
                return deferred.promise;
            }

            /**
             * Create filter function for a query string
             */
            function createFilterFor(query) {
                var lowercaseQuery = angular.lowercase(query);
                return function filterFn(product) {
                    return (product.value.indexOf(lowercaseQuery) === 0);
                };
            }

            $scope.getProductDetails = function () {
                if ($scope.searchedProduct !== null) {
                    angular.element(document.querySelector('.loader')).addClass('show');
                    $timeout(function () {
                        $scope.productDetailsSection = true;
                        angular.element(document.querySelector('.loader')).removeClass('show');
                    }, 1000);
                }
            };

            $scope.addProduct = function () { /* Function to add Product in the table */
                $scope.productsList.push({
                    "productCode": $scope.productDetails.productCode,
                    "supplierCode": $scope.productDetails.supplierCode,
                    "pricePerPiece": $scope.productDetails.pricePerPiece,
                    "productMOQ": $scope.productDetails.productMOQ,
                    "remarks": $scope.productDetails.remarks,
                    "productQuantity": $scope.productDetails.productQuantity,
                    "isSelected": false
                });
                $scope.searchText = null;
                $scope.productDetails.productCode = null;
                $scope.productDetails.supplierCode = "";
                $scope.productDetails.pricePerPiece = "";
                $scope.productDetails.productMOQ = "";
                $scope.productDetails.remarks = "";
                $scope.productDetails.productQuantity = "";
                $scope.addedProductsSection = true;
                smoothScroll(document.getElementById('addedProductsSection')); /* Scroll to the added products section */
            };

            $scope.editProduct = function (product) {/* Function to edit the Product from added products table */
                $scope.productDetails.productCode = product.productCode;
                $scope.productDetails.supplierCode = product.supplierCode;
                $scope.productDetails.pricePerPiece = product.pricePerPiece;
                $scope.productDetails.productMOQ = product.productMOQ;
                $scope.productDetails.remarks = product.remarks;
                $scope.productDetails.productQuantity = product.productQuantity;
                $scope.showAddBtn = false; /* Show Update Button & hide Add Button */
                $scope.editDisabled = true;
                smoothScroll(document.getElementById('productDetailsSection')); /* Scroll to the product details section */
            };

            $scope.updateProduct = function () {/* Function to update the Product in products added table */
                angular.forEach($scope.productsList, function (product) {
                    if (product.isChecked) {
                        product.productCode = $scope.productDetails.productCode;
                        product.supplierCode = $scope.productDetails.supplierCode;
                        product.pricePerPiece = $scope.productDetails.pricePerPiece;
                        product.productMOQ = $scope.productDetails.productMOQ;
                        product.remarks = $scope.productDetails.remarks;
                        product.productQuantity = $scope.productDetails.productQuantity;
                        product.isChecked = false;
                    }
                });
                $scope.showAddBtn = true; /* Show Add Button & hide Update Button */
                $scope.editDisabled = false;
            };

            $scope.submitCart = function () {
                $scope.orderSummarySection = true;
                smoothScroll(document.getElementById('orderSummarySection')); /* Scroll to the order summary section */
            };

            $scope.cancelCart = function () {
            };

            $scope.saveOrder = function () {
                angular.element(document.querySelector('.loader')).addClass('show');
                $scope.successMessage = "Order saved successfully!";
                $scope.showSuccessBox = true;
                $timeout(function () {
                    smoothScroll(document.getElementById('buildOrderPage')); /* Scroll to the top of the page */
                    angular.element(document.querySelector('.loader')).removeClass('show');
                }, 500);
            };

            $scope.cancelOrder = function () {
                $rootScope.hideModal();
                $scope.customerName = "";
                $scope.productsList = [];
                $scope.orderSummary = [];
                $scope.productDetails = {};
                $scope.searchProductSection = false; /* Hide the search product section */
                $scope.productDetailsSection = false; /* Hide the Products details section */
                $scope.addedProductsSection = false; /* Hide the products' added table */
                $scope.orderSummarySection = false; /* Hide Customer order summary section */
                $timeout(function () {
                    smoothScroll(document.getElementById('buildOrderPage')); /* Scroll to the top of the page */
                }, 500);
            };
        });
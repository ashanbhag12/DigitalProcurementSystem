angular.module('editSupplierApp', ['ngMessages', 'angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('editSupplierController', function ($rootScope, $scope, $timeout, getSuppliersService, modifySuppliersService, deleteSuppliersService, smoothScroll) {
            /* Initialize the page variables */
            $scope.showSuccessBox = false; /* Hide the error messages */
            $scope.showErrorBox = false; /* Hide the success messages */
            $scope.editDisabled = false; /* Enable the Edit button */
            $scope.deleteDisabled = true; /* Disable the Delete button */
            $scope.editSupplierForm = false; /* Hide the edit Supplier Form */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* set the default sort order */
            $scope.sortBy = 'name'; /* set the default sort type */
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.suppliers = []; /* Array of all Suppliers */
            
            
            $scope.searchSupplierName = ''; /* Name for supplier search */
            $scope.searchSupplierInitials = ''; /* Initials for supplier search */
            
            $scope.showSuccessBox = false;
            $scope.showErrorBox = false;
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {   
                console.log("page loaded")
            });

            /* Supplier object to be edited */
            /* changed name from editSupplier with supplier bcos it was conflicting with form name*/
            $scope.supplier = {
                "id": "",
                "name": "",
                "initials": "",
                "phoneNumber": "",
                "emailId": ""
            };

            /* Function to select/unselect all the Suppliers */
            $scope.toggleAll = function () {
                if ($scope.selectAll) {
                    $scope.selectAll = true;
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = false;
                    angular.forEach($scope.suppliers, function (supplier) {
                        supplier.isChecked = $scope.selectAll;
                        $scope.selectedRows.push(1);
                    });
                }
                else {
                    $scope.selectAll = false;
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = true;
                    angular.forEach($scope.suppliers, function (supplier) {
                        supplier.isChecked = $scope.selectAll;
                        $scope.selectedRows = [];
                    });
                }
            };

            /* Function to select/unselect the Supplier */
            $scope.toggle = function (element) {
                if (element.isChecked) {
                    $scope.selectedRows.push(1);
                    $scope.editDisabled = false;
                    $scope.deleteDisabled = false;
                }
                else {
                    $scope.selectedRows.pop();
                }
                if ($scope.suppliers.length === $scope.selectedRows.length) {
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

            /* Function to edit the selected Supplier */
            $scope.edit = function () {
            	var element = document.getElementById("editSupplierForm");
                angular.forEach($scope.suppliers, function (supplier) {
                    if (supplier.isChecked) {
                        smoothScroll(element); /* Scroll to the form */
                        $scope.editSupplierForm = true;
                        $scope.supplier.name = supplier.name;
                        $scope.supplier.initials = supplier.initials;
                        $scope.supplier.phoneNumber = supplier.phoneNumber;
                        $scope.supplier.emailId = supplier.emailId;
                    }
                });
            };

            /* Function to delete the selected Suppliers */
            $scope.deleteSupplier = function () {
                angular.element(document.querySelector('.loader')).addClass('show');
                angular.element(document.querySelector('.modal')).css('display', "block");

                angular.forEach($scope.suppliers, function (supplier) {
                    if (supplier.isChecked) {
                    	response = deleteSuppliersService.remove({supplierId : supplier.id});                   
                    }
                });

                angular.element(document.querySelector('.modal')).css('display', "none");
                $timeout(function () {
                    angular.element(document.querySelector('.loader')).removeClass('show');
                    $scope.selectAll = false;
                    // WS call to get all suppliers.
                    $scope.suppliers = getSuppliersService.query({name:$scope.searchSupplierName,initials:$scope.searchSupplierInitials});
                    $scope.selectedRows = [];
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = true;
                }, 1000);
            };

            /* Function to update the selected Supplier */
            /* added keepgoing check for performance. Bcos for very first supplier if isChecked condition is true, that supplier will be updated and for loop breaks
             * Note: no break; concept for angularJs forEach */
            
            $scope.updateSupplierJson = {};
            
            $scope.update = function () {
            	var keepGoing = true;
                angular.forEach($scope.suppliers, function (supplier) {
                	if(keepGoing) {
                		if (supplier.isChecked) {
                            supplier.name = $scope.supplier.name;
                            supplier.initials = $scope.supplier.initials;
                            supplier.phoneNumber = $scope.supplier.phoneNumber;
                            supplier.emailId = $scope.supplier.emailId;
                            delete supplier.isChecked;
                            
                            $scope.updateSupplierJson = angular.toJson(supplier);
                            
                            keepGoing = false;
                        }                    
                	}
                });
                
                /* Service call to update supplier */
    		    response = modifySuppliersService.save($scope.updateSupplierJson);

                // WS call to get all suppliers.
                $scope.suppliers = getSuppliersService.query({name:$scope.searchSupplierName,initials:$scope.searchSupplierInitials});
                
                $scope.editSupplierForm = false;
                $scope.showSuccessBox = true;
                $scope.showErrorBox = false;
            };

            /* Function to search for Suppliers */
            $scope.searchSupplier = function () {
                $scope.selectAll = false;
                $scope.searchedResults = true;
                $scope.editDisabled = true;
                $scope.deleteDisabled = true;
                
                /* Service Call to retrieve searched supplier */
                /*Asign below value to $scope.suppliers later on*/
                
                $scope.suppliers = getSuppliersService.query({name:$scope.searchSupplierName,initials:$scope.searchSupplierInitials});
            };

            $scope.reset = function () {
                $scope.supplier = {};
                $scope.editSupplier.$setPristine();
                $scope.editSupplier.name.$touched = false;
                $scope.editSupplier.initials.$touched = false;
                $scope.editSupplier.phoneNumber.$touched = false;
                $scope.editSupplier.emailId.$touched = false;
                $scope.showSuccessBox = false;
            };
        });
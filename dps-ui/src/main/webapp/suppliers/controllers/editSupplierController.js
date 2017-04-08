angular.module('editSupplierApp', ['ngMessages', 'angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('editSupplierController', function ($rootScope, $scope, $timeout, getSuppliersService, modifySuppliersService, deleteSuppliersService, smoothScroll) {
            /* Initialize the page variables */
            $scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.editDisabled = true; /* Disable the Edit button */
            $scope.deleteDisabled = true; /* Disable the Delete button */
            $scope.editSupplierForm = false; /* Hide the edit Supplier Form */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* set the default sort order */
            $scope.sortBy = 'name'; /* set the default sort type */
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.suppliers = []; /* Array of all Suppliers */ 
            $scope.searchSupplierName = ''; /* Name for supplier search */
            $scope.searchSupplierInitials = ''; /* Initials for supplier search */
            $scope.selectAll = false; /* Set toggle all to false */
            $scope.supplier = {/* Supplier Object */
                "id": "",
                "name": "",
                "initials": "",
                "phoneNumber": "",
                "emailId": ""
            };
            var scrollOptions = { /* Set offset to scroll to search table */
            	    offset: -200,
            	    callbackAfter: function(element) {
            	    	angular.element(document.getElementsByName("searchTableText")).focus();
            		}
            	};
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {});

            /* Function to select/unselect all the Suppliers */
            $scope.toggleAll = function () {
                if ($scope.selectAll) {
                    $scope.selectAll = true;
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = false;
                    $scope.selectedRows = [];
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
                angular.forEach($scope.suppliers, function (supplier) {
                    if (supplier.isChecked) {
                        smoothScroll(document.getElementById("editSupplierForm")); /* Scroll to the form */
                        $scope.editSupplierForm = true;
                        $scope.supplier.name = supplier.name;
                        $scope.supplier.initials = supplier.initials;
                        $scope.supplier.phoneNumber = supplier.phoneNumber;
                        $scope.supplier.emailId = supplier.emailId;
                        $timeout(function () {
                        	angular.element(document.getElementsByName("name")).focus();
                        }, 500);                        
                    }
                });
            };

            /* Function to delete the selected Suppliers */
            $scope.deleteSupplier = function () {
                angular.forEach($scope.suppliers, function (supplier) {
                    if (supplier.isChecked) {
                    	angular.element(document.querySelector('.loader')).addClass('show');
                    	response = deleteSuppliersService.remove({supplierId : supplier.id}, function(){/* Success Callback */
                    		angular.element(document.querySelector('.modal')).css('display', "none");                            
	                    	$timeout(function () {
	                    		/* WS call to get all suppliers */
	                            $scope.suppliers = getSuppliersService.query({name:$scope.searchSupplierName,initials:$scope.searchSupplierInitials});
		        	            $scope.selectAll = false;
		        	            $scope.selectedRows = [];
		        	            $scope.editDisabled = true;
		        	            $scope.deleteDisabled = true;
		        	            $scope.showSuccessBox = true;
		    			        $scope.successMessage = "Suppliers deleted successfully";
		    		            $scope.showErrorBox = false;
		    		            smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
		    		            angular.element(document.querySelector('.loader')).removeClass('show');
		    		            angular.element(document.getElementsByName("searchTableText")).focus();
		        	        }, 500);
	                	}, function(){/* Error Callback */
	                		angular.element(document.querySelector('.modal')).css('display', "none");     
	                    	$timeout(function () {	
	                    		$scope.selectedRows = [];
		        	            $scope.editDisabled = true;
		        	            $scope.deleteDisabled = true;
						    	$scope.showSuccessBox = false;
					            $scope.showErrorBox = true;
					            $scope.errorMessage = "Suppliers could not be deleted. Please try again after some time";
					            smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
					            angular.element(document.querySelector('.loader')).removeClass('show');
						    }, 500);
	                    }); 
                    }
                });                      
            };

            /* Function to update the selected Supplier */
            /* added keepgoing check for performance. Because for very first supplier if isChecked condition is true, that supplier will be updated and for loop breaks
             * Note: no break; concept for angularJs forEach */
            
            $scope.update = function () {
            	var keepGoing = true;
                angular.forEach($scope.suppliers, function (supplier) {
                	if(keepGoing) {
                		if (supplier.isChecked) {
                			angular.element(document.querySelector('.loader')).addClass('show');
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
    		    response = modifySuppliersService.save($scope.updateSupplierJson, function(){ /* Success Callback */    		    	
                    $timeout(function () {		 
                    	// WS call to get all suppliers.
                        $scope.suppliers = getSuppliersService.query({name:$scope.searchSupplierName,initials:$scope.searchSupplierInitials});                        
                        $scope.editSupplierForm = false;
    			        $scope.showSuccessBox = true;
    			        $scope.showErrorBox = false;
    			        $scope.selectAll = false;
        	            $scope.selectedRows = [];
        	            $scope.editDisabled = true;
        	            $scope.deleteDisabled = true;
    			        $scope.successMessage = "Supplier details updated successfully";		            
    		            smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
    		    		angular.element(document.querySelector('.loader')).removeClass('show');
    		    		angular.element(document.getElementsByName("searchTableText")).focus();
    		    	}, 500);
    		    }, function(){/* Error Callback */
    		    	$timeout(function () {		   
    			    	$scope.showSuccessBox = false;
    			    	$scope.showErrorBox = true;
    			    	$scope.selectAll = false;
        	            $scope.selectedRows = [];
        	            $scope.editDisabled = true;
        	            $scope.deleteDisabled = true;
    			        $scope.errorMessage = "Supplier details could not be updated. Please try again after some time";	            
    		            smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
    		    		angular.element(document.querySelector('.loader')).removeClass('show');
    		    	}, 500);
    		    });
            };

            /* Function to search for Suppliers */
            $scope.searchSupplier = function () {    
            	angular.element(document.querySelector('.loader')).addClass('show'); 
                /* Service Call to retrieve searched supplier */                
                $scope.suppliers = getSuppliersService.query({name:$scope.searchSupplierName,initials:$scope.searchSupplierInitials}, function(){/* Success Callback */
    	        	$timeout(function(){
    	        		$scope.searchedResults = true;
    	        		$scope.showSuccessBox = false;
    		            $scope.showErrorBox = false;
    		            $scope.selectedRows = [];
    		            $scope.editDisabled = true;
        	            $scope.deleteDisabled = true;
    	        		angular.element(document.querySelector('.loader')).removeClass('show');
    	        		smoothScroll(document.getElementsByClassName("searchedResults"), scrollOptions); /* Scroll to the table */
    	        	}, 500);
    	        }, function(){ /* Error Callback */
    	        	$timeout(function(){
    	        		$scope.errorMessage = "Supplier not found. Please try again after some time";
    	        		$scope.showSuccessBox = false;
    		            $scope.showErrorBox = true;
    	        		angular.element(document.querySelector('.loader')).removeClass('show');
    	        	}, 500);
    	        });
            };
            
            $scope.cancel = function () {
            	$scope.editSupplierForm = false;
            	$scope.selectAll = false;
            	angular.forEach($scope.suppliers, function (supplier) {
            		supplier.isChecked = $scope.selectAll;
                    $scope.selectedRows = [];
                });
            	$scope.editDisabled = true;
                $scope.deleteDisabled = true;
            	smoothScroll(document.getElementsByTagName('body')); /* Scroll to top of the page */
            	angular.element(document.getElementsByName("searchTableText")).focus();
            };
        });
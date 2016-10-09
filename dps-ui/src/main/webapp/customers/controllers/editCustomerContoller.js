angular.module('editCustomerApp', ['ngMessages', 'angularUtils.directives.dirPagination', 'smoothScroll'])
	.controller('editCustomerController', function ($rootScope, $scope, $timeout, getCustomersService, modifyCustomersService, deleteCustomersService, smoothScroll) {
	    /* Initialize the page variables */
	    $scope.showSuccessBox = false; /* Hide the error messages */
	    $scope.showErrorBox = false; /* Hide the success messages */
	    $scope.successMessage = "";
        $scope.errorMessage = "";
	    $scope.editDisabled = true; /* Disable the Edit button */
	    $scope.deleteDisabled = true; /* Disable the Delete button */
	    $scope.editCustomerForm = false; /* Hide the edit Customer Form */
	    $scope.searchedResults = false; /* Hide the search results container */
	    $scope.sortOrder = false; /* set the default sort order */
	    $scope.sortBy = 'name'; /* set the default sort type */
	    $scope.selectedRows = []; /* Array for toggleAll function */
	    $scope.customers = []; /* Array of all Customers */	    	    
	    $scope.searchCustomerName = ''; /* Name for customer search */
	    $scope.searchCustomerShipmark = ''; /* Initials for customer search */
	    $scope.selectAll = false; /* Set toggle all to false */
	    $scope.maskColumns = true; /* Hide the columns */
	    
	    /* Function will be executed after the page is loaded */
	    $scope.$on('$viewContentLoaded', function () {});
	
	    /* Customer object to be edited */
	    /* changed name from editCustomer with customer bcos it was conflicting with form name*/
	    $scope.customer = {
	    	name: "",
	    	phoneNumber: "",
	    	emailId: "",
	    	shipmark: "",
	    	additionalMargin: "",
	    	additionalMarginPercentage: "",
	    	flatNo: "",
		    building: "",
		    street: "",
		    locality: "",
		    city: "",
		    state: "",
		    zip: ""
	    };
	
	    /* Function to select/unselect all the Customers */
	    $scope.toggleAll = function () {
	        if ($scope.selectAll) {
	            $scope.selectAll = true;
	            $scope.editDisabled = true;
	            $scope.deleteDisabled = false;
	            $scope.selectedRows = [];
	            angular.forEach($scope.customers, function (customer) {
	                customer.isChecked = $scope.selectAll;
	                $scope.selectedRows.push(1);
	            });
	        }
	        else {
	            $scope.selectAll = false;
	            $scope.editDisabled = true;
	            $scope.deleteDisabled = true;
	            angular.forEach($scope.customers, function (customer) {
	                customer.isChecked = $scope.selectAll;
	                $scope.selectedRows = [];
	            });
	        }
	    };
	
	    /* Function to select/unselect the Customer */
	    $scope.toggle = function (element) {
	        if (element.isChecked) {
	            $scope.selectedRows.push(1);
	            $scope.editDisabled = false;
	            $scope.deleteDisabled = false;
	        }
	        else {
	            $scope.selectedRows.pop();
	        }
	        if ($scope.customers.length === $scope.selectedRows.length) {
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
	    
	    /* Function to update Additional Customer Margin */
	    $scope.updateCustomerMargin = function(){
	    	$scope.customer.additionalMargin = parseFloat($scope.customer.additionalMarginPercentage);
        	if($scope.customer.additionalMargin >= 0){
        		$scope.customer.additionalMargin = (1 / (1 - (Math.abs($scope.customer.additionalMargin)/100))).toFixed(6);
    		}
        	else{
        		$scope.customer.additionalMargin = (1 - (Math.abs($scope.customer.additionalMargin)/100)).toFixed(6);            		
        	}
	    };
	
	    /* Function to edit the selected Customer */
	    $scope.edit = function () {
	        angular.forEach($scope.customers, function (customer) {
	            if (customer.isChecked) {
	            	smoothScroll(document.getElementById("editCustomerForm")); /* Scroll to the form */
	                $scope.editCustomerForm = true;
	                $scope.customer.name = customer.name;
	                $scope.customer.phoneNumber = customer.phoneNumber;
	                $scope.customer.emailId = customer.emailId;
	                $scope.customer.shipmark = customer.shipmark;
	                $scope.customer.additionalMarginPercentage = customer.additionalMarginPercentage;
	                $scope.customer.flatNo = customer.flatNo;
	                $scope.customer.building = customer.building;
	                $scope.customer.street = customer.street;
	                $scope.customer.locality = customer.locality;
	                $scope.customer.city = customer.city;
	                $scope.customer.state = customer.state;
	                $scope.customer.zip = customer.zip;
	            }
	        });
	    };
	
	    /* Function to delete the selected Customers */
	    $scope.deleteCustomer = function () {
	        angular.forEach($scope.customers, function (customer) {
	            if (customer.isChecked) { 
	            	angular.element(document.querySelector('.loader')).addClass('show');
	            	response = deleteCustomersService.remove({customerId : customer.id}, function(){/* Success Callback */
	            		angular.element(document.querySelector('.modal')).css('display', "none");
	        	        $timeout(function () {	        	            
	        	            $scope.selectAll = false;
	        	            // WS call to get all Customers
	        	            $scope.customers = getCustomersService.query({name:$scope.searchCustomerName,shipmark:$scope.searchCustomerShipmark});
	        	            $scope.selectedRows = [];
	        	            $scope.editDisabled = true;
	        	            $scope.deleteDisabled = true;
	        	            $scope.showSuccessBox = true;
	    			        $scope.successMessage = "Customers deleted successfully";
	    		            $scope.showErrorBox = false;
	    		            angular.element(document.querySelector('.loader')).removeClass('show');
	        	        }, 500);
	            	}, function(error){/* Error Callback */
	            		angular.element(document.querySelector('.modal')).css('display', "none");     
	            		$timeout(function () {	
	            			$scope.selectAll = false;
	        	            $scope.selectedRows = [];
	        	            $scope.editDisabled = true;
	        	            $scope.deleteDisabled = true;
					    	$scope.showSuccessBox = false;
				            $scope.showErrorBox = true;
				            $scope.errorMessage = "Customers could not be deleted. Please try again after some time";
				            angular.element(document.querySelector('.loader')).removeClass('show');
					    }, 500);
	            	});
	            }
	        });        
	    };
	
	    /* Function to update the selected Customer */
	    /* added keepgoing check for performance. Because for very first customer if isChecked condition is true, that customer will be updated and for loop breaks
	     * Note: no break; concept for angularJs forEach */
	   
	    $scope.update = function () {
	    	var keepGoing = true;
	        angular.forEach($scope.customers, function (customer) {
	        	if(keepGoing) {
	        		if (customer.isChecked) {
	        			$scope.updateCustomerMargin();
	        			angular.element(document.querySelector('.loader')).addClass('show');
	                    customer.name = $scope.customer.name;
	                    customer.phoneNumber = $scope.customer.phoneNumber;
	                    customer.emailId = $scope.customer.emailId;
	                    customer.shipmark = $scope.customer.shipmark;
	                    customer.additionalMarginPercentage = $scope.customer.additionalMarginPercentage;
	                    customer.additionalMargin = $scope.customer.additionalMargin;
	                    customer.flatNo = $scope.customer.flatNo;
	                    customer.building = $scope.customer.building;
	                    customer.street = $scope.customer.street;
	                    customer.locality = $scope.customer.locality;
	                    customer.city = $scope.customer.city;
	                    customer.state = $scope.customer.state;
	                    customer.zip = $scope.customer.zip;
	                    delete customer.isChecked;	                    
	                    keepGoing = false;
	                    $scope.selectedRows = [];
	                    $scope.updateCustomerJson = angular.toJson(customer);	
	                }  
	        	}
	        });
	        
	        /* Service call to update customer */
		    response = modifyCustomersService.save($scope.updateCustomerJson, function(){/* Success Callback */		    	
		    	$timeout(function () {		   
		    		/* WS call to get all Customers */
			    	$scope.customers = getCustomersService.query({name:$scope.searchCustomerName,shipmark:$scope.searchCustomerShipmark});
			        $scope.editCustomerForm = false;
			        $scope.showSuccessBox = true;
			        $scope.showErrorBox = false;
			        $scope.selectAll = false;
    	            $scope.selectedRows = [];
    	            $scope.editDisabled = true;
    	            $scope.deleteDisabled = true;
			        $scope.successMessage = "Customer details updated successfully";		            
		            smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
		    		angular.element(document.querySelector('.loader')).removeClass('show');
		    	}, 500);
		    }, function(error){/* Error Callback */
		    	$timeout(function () {		   
			    	$scope.showSuccessBox = false;
			    	$scope.showErrorBox = true;
			    	$scope.selectAll = false;
    	            $scope.selectedRows = [];
    	            $scope.editDisabled = true;
    	            $scope.deleteDisabled = true;
			        $scope.errorMessage = "Customer details could not be updated. Please try again after some time";	            
		            smoothScroll(document.getElementsByTagName('body')); /* Scroll to the top of the page */
		    		angular.element(document.querySelector('.loader')).removeClass('show');
		    	}, 500);
		    });
	    };
	
	    /* Function to search for Customers */
	    $scope.searchCustomer = function () {
	    	angular.element(document.querySelector('.loader')).addClass('show'); 
	        /* Service Call to retrieve searched customer */
	        $scope.customers = getCustomersService.query({name:$scope.searchCustomerName,shipmark:$scope.searchCustomerShipmark}, function(){/* Success Callback */
	        	$timeout(function(){
	        		$scope.searchedResults = true;
	        		angular.element(document.querySelector('.loader')).removeClass('show');
	        	}, 500);
	        }, function(){ /* Error Callback */
	        	$timeout(function(){
	        		$scope.errorMessage = "Customer not found. Please try again";
	        		angular.element(document.querySelector('.loader')).removeClass('show');
	        	}, 500);
	        });
	    };
	    
	    $scope.cancel = function () {
        	$scope.editCustomerForm = false;
        	$scope.selectAll = false;
        	angular.forEach($scope.customers, function (customer) {
                customer.isChecked = $scope.selectAll;
                $scope.selectedRows = [];
            });
        	smoothScroll(document.getElementsByTagName('body')); /* Scroll to top of the page */
        };
});
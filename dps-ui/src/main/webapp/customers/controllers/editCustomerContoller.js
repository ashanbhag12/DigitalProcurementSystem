angular.module('editCustomerApp', ['ngMessages', 'angularUtils.directives.dirPagination'])
	.controller('editCustomerController', function ($rootScope, $scope, $timeout, getCustomersService, modifyCustomersService, deleteCustomersService) {
	    /* Initialize the page variables */
	    $scope.showSuccessBox = false; /* Hide the error messages */
	    $scope.showErrorBox = false; /* Hide the success messages */
	    $scope.editDisabled = false; /* Enable the Edit button */
	    $scope.deleteDisabled = true; /* Disable the Delete button */
	    $scope.editCustomerForm = false; /* Hide the edit Customer Form */
	    $scope.searchedResults = false; /* Hide the search results container */
	    $scope.sortOrder = false; /* set the default sort order */
	    $scope.sortBy = 'name'; /* set the default sort type */
	    $scope.selectedRows = []; /* Array for toggleAll function */
	    $scope.customers = []; /* Array of all Customers */
	    
	    
	    $scope.searchCustomerName = ''; /* Name for customer search */
	    $scope.searchCustomerShipmark = ''; /* Initials for customer search */
	    
	    /* Customers Object */
	    $scope.customers = [
	        {
	            id: "S1",
	            name: "Customer 1",
		    	phoneNumber: "1234567890",
		    	emailId: "customer1@gmail.com",
		    	shipmark: "1Ship",
		    	additionalMargin: "1.1",
		    	flatNo: "1",
			    building: "xyz",
			    street: "road",
			    locality: "abc",
			    city: "Mu",
			    state: "MH",
			    zip: "567765",
	            isChecked: false
	        },
	        {
	            id: "S2",
	            name: "Customer 2",
		    	phoneNumber: "0987654321",
		    	emailId: "customer2@gmail.com",
		    	shipmark: "2Ship",
		    	additionalMargin: "2.2",
		    	flatNo: "2",
			    building: "aaa",
			    street: "",
			    locality: "",
			    city: "Pu",
			    state: "MH",
			    zip: "123456",
	            isChecked: false
	        }
	    ];
	    
	    /* Function will be executed after the page is loaded */
	    $scope.$on('$viewContentLoaded', function () {   
	        console.log("page loaded")
	    });
	
	    /* Customer object to be edited */
	    /* changed name from editCustomer with customer bcos it was conflicting with form name*/
	    $scope.customer = {
	    	name: "",
	    	phoneNumber: "",
	    	emailId: "",
	    	shipmark: "",
	    	additionalMargin: "",
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
	
	    /* Function to edit the selected Customer */
	    $scope.edit = function () {
	        angular.forEach($scope.customers, function (customer) {
	            if (customer.isChecked) {
	                $scope.editCustomerForm = true;
	                $scope.customer.name = customer.name;
	                $scope.customer.phoneNumber = customer.phoneNumber;
	                $scope.customer.emailId = customer.emailId;
	                $scope.customer.shipmark = customer.shipmark;
	                $scope.customer.additionalMargin = customer.additionalMargin;
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
	        var newCustomers = [];
	        var deleteCustomers = []; /* To be sent to server for Delete operation */
	        angular.element(document.querySelector('.loader')).addClass('show');
	        angular.element(document.querySelector('.modal')).css('display', "block");
	
	        angular.forEach($scope.customers, function (customer) {
	            if (!customer.isChecked) {
	                newCustomers.push(customer);
	            }
	            else{
	            	response = deleteCustomersService.remove({customerId : customer.id});
	                deleteCustomers.push(customer);                       
	            }
	        });
	
	        angular.element(document.querySelector('.modal')).css('display', "none");
	        $timeout(function () {
	            angular.element(document.querySelector('.loader')).removeClass('show');
	            $scope.selectAll = false;
	            $scope.customers = newCustomers;
	            $scope.selectedRows = [];
	            $scope.editDisabled = true;
	            $scope.deleteDisabled = true;
	        }, 1000);
	    };
	
	    /* Function to update the selected Customer */
	    /* added keepgoing check for performance. Bcos for very first customer if isChecked condition is true, that customer will be updated and for loop breaks
	     * Note: no break; concept for angularJs forEach */
	    
	    $scope.updateCustomerJson = {};
	   
	    $scope.update = function () {
	    	var keepGoing = true;
	        angular.forEach($scope.customers, function (customer) {
	        	if(keepGoing) {
	        		if (customer.isChecked) {
	                    customer.name = $scope.customer.name;
	                    customer.phoneNumber = $scope.customer.phoneNumber;
	                    customer.emailId = $scope.customer.emailId;
	                    customer.shipmark = $scope.customer.shipmark;
	                    customer.additionalMargin = $scope.customer.additionalMargin;
	                    customer.flatNo = $scope.customer.flatNo;
	                    customer.building = $scope.customer.building;
	                    customer.street = $scope.customer.street;
	                    customer.locality = $scope.customer.locality;
	                    customer.city = $scope.customer.city;
	                    customer.state = $scope.customer.state;
	                    customer.zip = $scope.customer.zip;
	                    
	                    $scope.updateCustomerJson = angular.toJson(customer);
	                    
	                    keepGoing = false;
	                }                    
	        	}
	        });
	        
	        /* Service call to update customer */
	        // alert($scope.updateCustomerJson);
		    response = modifyCustomersService.save($scope.updateCustomerJson);
	        
	        $scope.editCustomerForm = false;
	    };
	
	    /* Function to search for Customers */
	    $scope.searchCustomer = function () {
	        $scope.selectAll = false;
	        $scope.searchedResults = true;
	        $scope.editDisabled = true;
	        $scope.deleteDisabled = true;
	        
	        /* Service Call to retrieve searched customer */
	        /*Asign below value to $scope.customers later on*/
	        
	        //$scope.customers = getCustomersService.get({customerName:$scope.searchCustomerName,customerInitial:$scope.searchCustomerShipmark});
	        
	        var response = getCustomersService.get({name:$scope.searchCustomerName,shipmark:$scope.searchCustomerShipmark});
	        
	        //alert(response);
	    };
	
	    /* Global function to show Modal Window */
	    $rootScope.showModal = function () {
	        angular.element(document.querySelector('.loader')).addClass('show');
	        angular.element(document.querySelector('.modal')).css('display', "block");
	    };
	
	    /* Global function to hide Modal Window */
	    $rootScope.hideModal = function () {
	        angular.element(document.querySelector('.loader')).removeClass('show');
	        angular.element(document.querySelector('.modal')).css('display', "none");
	    };
	    
	    $scope.reset = function () {
	        $scope.customer = {};
	        $scope.editCustomer.$setPristine();
	        $scope.editCustomer.name.$touched = false;
	        $scope.editCustomer.phoneNumber.$touched = false;
	        $scope.editCustomer.emailId.$touched = false;
	        $scope.editCustomer.shipmark.$touched = false;
	        $scope.editCustomer.additionalMargin.$touched = false;
	        $scope.editCustomer.flatNo.$touched = false;
	        $scope.editCustomer.building.$touched = false;
	        $scope.editCustomer.street.$touched = false;
	        $scope.editCustomer.locality.$touched = false;
	        $scope.editCustomer.city.$touched = false;
	        $scope.editCustomer.state.$touched = false;
	        $scope.editCustomer.zip.$touched = false;
	        
	        $scope.showSuccessBox = false;
	    };
});
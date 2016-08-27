angular.module('addCustomerApp', ['ngMessages'])
		.controller('addCustomerController', function ($scope, $timeout, addCustomersService) {
		    $scope.showSuccessBox = false; /* Hide the success box */
		    $scope.showErrorBox = false; /* Hide the error box */
		
		    $scope.customer = {/* Customer Object */
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
		
		    $scope.submitForm = function (addCustomer) {
		        if (addCustomer.$valid) {
		        	angular.element(document.querySelector('.loader')).addClass('show'); 
				    response = addCustomersService.save($scope.customer, function(){				    
					    $timeout(function () {                            
                            $scope.reset();
                            $scope.showSuccessBox = true;
    					    $scope.showErrorBox = false;
    					    angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
				    }, function(error){
				    	$scope.showErrorBox = true; 
				    	$scope.showSuccessBox = false;
        		    	$timeout(function () {
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
				    });				    
		        }               
		    };
		
		    $scope.reset = function () {
		        $scope.customer = {};
		        $scope.addCustomer.$setPristine();
		        $scope.addCustomer.name.$touched = false;
		        $scope.addCustomer.phoneNumber.$touched = false;		        
		        $scope.addCustomer.emailId.$touched = false;
		        $scope.addCustomer.shipmark.$touched = false;
		        $scope.addCustomer.additionalMargin.$touched = false;	        
		        $scope.addCustomer.flatNo.$touched = false;
		        $scope.addCustomer.building.$touched = false;
		        $scope.addCustomer.street.$touched = false;
		        $scope.addCustomer.locality.$touched = false;
		        $scope.addCustomer.city.$touched = false;
		        $scope.addCustomer.state.$touched = false;
		        $scope.addCustomer.zip.$touched = false;
		        $scope.showSuccessBox = false;
		        $scope.showErrorBox = false;
		    };
});
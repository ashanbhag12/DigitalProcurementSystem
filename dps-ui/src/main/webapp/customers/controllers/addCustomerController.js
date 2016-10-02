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
		        additionalMarginPercentage: "",
		        flatNo: "",
			    building: "",
			    street: "",
			    locality: "",
			    city: "",
			    state: "",
			    zip: ""		        	
		    };
		    
		    /* Function to calculate Additional Customer Margin */
		    $scope.calculateCustomerMargin = function(){
		    	$scope.customer.additionalMargin = parseInt($scope.customer.additionalMarginPercentage);
            	if($scope.customer.additionalMargin >= 0){
            		$scope.customer.additionalMargin = 1 / (1 - (Math.abs($scope.customer.additionalMargin)/100));
        		}
            	else{
            		$scope.customer.additionalMargin = 1 - (Math.abs($scope.customer.additionalMargin)/100);            		
            	}
		    };
		
		    $scope.submitForm = function (addCustomer) {
		        if (addCustomer.$valid) {
		        	angular.element(document.querySelector('.loader')).addClass('show'); 
				    response = addCustomersService.save($scope.customer, function(){/* Success Callback */				    
					    $timeout(function () {                            
                            $scope.reset();
                            $scope.showSuccessBox = true;
    					    $scope.showErrorBox = false;
    					    angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
				    }, function(error){/* Error Callback */		    	
        		    	$timeout(function () {
        		    		$scope.showErrorBox = true; 
    				    	$scope.showSuccessBox = false;
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
		        $scope.addCustomer.additionalMarginPercentage.$touched = false;	        
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
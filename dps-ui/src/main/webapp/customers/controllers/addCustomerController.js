angular.module('addCustomerApp', ['ngMessages','customerApp'])
		.controller('addCustomerController', function ($scope, addCustomersService) {
		    $scope.showSuccessBox = true;
		    $scope.showErrorBox = true;
		
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
		
		    $scope.submitForm = function (addCustomer) {
		        if (addCustomer.$valid) {
		        	$scope.customerJson = angular.toJson($scope.customer);
				    //alert($scope.customerJson);
				    response = addCustomersService.save($scope.customerJson);
				    //alert(response);
				    $scope.reset();
		        }
		        else{                    
		            console.log("form invalid");
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
		    };
});
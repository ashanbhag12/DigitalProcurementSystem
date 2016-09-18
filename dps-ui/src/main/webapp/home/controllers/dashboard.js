angular.module('dashboardApp', ["ngMessages"])
        .controller("dashboardController", function ($scope, $timeout, getDashboardCardService, getDashboardConfigService, modifyDashboardConfigService) {
            $scope.showSuccessBox = false; /* Hide the error messages */
            $scope.showErrorBox = false; /* Hide the success messages */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.showEditBtn = true; /* Show Edit Button */
            $scope.showSaveBtn = false; /* Hide Save Button */
            $scope.showClearBtn = false; /* Hide Clear Button */
            $scope.isReadonly = true; /* Set the fields as Readonly */
            $scope.dashboard; /* Create dashboard object for Cards */
            $scope.global = {}; /* Create Globals object */
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
            	$scope.dashboard = getDashboardCardService.get(); /* Get data for Cards */
            	$scope.global = getDashboardConfigService.get(); /* Get Global Parameters */
            });

            $scope.edit = function () {
    		    $scope.showSaveBtn = true;
                $scope.showClearBtn = true;
                $scope.showEditBtn = false;
                $scope.isReadonly = false;    
                $scope.showSuccessBox = false; 
                $scope.showErrorBox = false;
            };

            $scope.submitForm = function (globalParameters) {
            	if (globalParameters.$valid) {
            		angular.element(document.querySelector('.loader')).addClass('show');                    
        		    response = modifyDashboardConfigService.save($scope.global, function(){/* Success Callback */        		    	
                        $timeout(function () {
                        	$scope.showSuccessBox = true;
                		    $scope.successMessage = "Global Parameters saved successfully";          		   
                		    $scope.showEditBtn = true;
                            $scope.showSaveBtn = false;
                            $scope.showClearBtn = false;
                            $scope.isReadonly = true; /* Should be the last line */                            
                            $scope.dashboard.exchangeRate = response.exchangeRate; 
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
        		    }, function(error){/* Error Callback */        		    	    
        		    	$timeout(function () {
                            $scope.showErrorBox = true;
                		    $scope.errorMessage = "Could not save global parameters. Please try again after some time";
                		    angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
        		    });        		    
                }                   
            };

            $scope.clear = function () {
            	$scope.global = {};
                $scope.globalParameters.$setPristine();
                $scope.globalParameters.exchangeRate.$touched = false;
                $scope.globalParameters.costPerGrossWeight.$touched = false;
                $scope.globalParameters.costPerCbm.$touched = false;
                $scope.globalParameters.basePath.$touched = false;
            };
        });
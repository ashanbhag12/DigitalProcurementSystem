angular.module('dashboardApp', [])
        .controller("dashboardController", function ($scope, $timeout) {
            $scope.showSuccessBox = false; /* Hide the error messages */
            $scope.showErrorBox = false; /* Hide the success messages */
            $scope.showEditBtn = true; /* Show Edit Button */
            $scope.showSaveBtn = false; /* Hide Save Button */
            $scope.showClearBtn = false; /* Hide Clear Button */
            $scope.isReadonly = true; /* Set the fields as Readonly */
            $scope.globals = { /* Create Globals object */
                "exchangeRate": "1",
                "CBMCost": "2",
                "grossWeightCost": "3"
            };
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
                console.log($scope.globals);
            });

            $scope.edit = function () {
                $scope.showSaveBtn = true;
                $scope.showClearBtn = true;
                $scope.showEditBtn = false;
                $scope.isReadonly = false;
            };

            $scope.save = function () {
                $scope.showEditBtn = true;
                $scope.showSaveBtn = false;
                $scope.showClearBtn = false;
                console.log($scope.globals);
                $scope.isReadonly = true; /* Should be the last line */
            };

            $scope.clear = function () {

            };
        });
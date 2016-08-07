angular.module('customersApp', [])
	.controller("customersCtrl", function($scope){
		$scope.message = "I am a customer";
		$scope.pageName = "home";
		$scope.reverse = false;
		$scope.employees = [
			{
				name:"Z",
				id:"111"
			},
			{
				name:"A",
				id:"222"
			},
			{
				name:"F",
				id:"333"
			},
			{
				name:"O",
				id:"444"
			}
		]
	})
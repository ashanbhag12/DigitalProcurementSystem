var customerServiceApp = angular.module('addCustomerApp');

customerServiceApp.factory('addCustomersService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customer/add');
});
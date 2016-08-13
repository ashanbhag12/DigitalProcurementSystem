var customerServiceApp = angular.module('editCustomerApp');

customerServiceApp.factory('modifyCustomersService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customer/modify');
});

customerServiceApp.factory('getCustomersService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customer/find');
});

customerServiceApp.factory('deleteCustomersService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customer/delete/:customerId', {customerId:'@customerId'});
});
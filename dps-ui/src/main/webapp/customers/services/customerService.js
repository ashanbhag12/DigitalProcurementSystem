var customerServiceApp = angular.module('customerApp', []);

customerServiceApp.factory('addCustomersService', function ($resource) {
    return $resource('/customer/add');
});

customerServiceApp.factory('modifyCustomersService', function ($resource) {
    return $resource('/customer/modify');
});

customerServiceApp.factory('getCustomersService', function ($resource) {
    return $resource('/customer/find');
});

customerServiceApp.factory('deleteCustomersService', function ($resource) {
    return $resource('/customer/delete/:customerId', {customerId:'@customerId'});
});
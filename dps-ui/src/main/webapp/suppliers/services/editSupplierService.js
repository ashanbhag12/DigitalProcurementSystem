var supplierServiceApp = angular.module('editSupplierApp');


supplierServiceApp.factory('modifySuppliersService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/supplier/modify');
});

supplierServiceApp.factory('getSuppliersService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/supplier/find');
});

supplierServiceApp.factory('deleteSuppliersService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/supplier/delete/:supplierId', {supplierId:'@supplierId'}, {
    	remove:{
            method:'POST'
            }
    });
});
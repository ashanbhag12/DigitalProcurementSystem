var supplierServiceApp = angular.module('addSupplierApp');

supplierServiceApp.factory('addSuppliersService', function ($resource) {
            return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/supplier/add');
        });
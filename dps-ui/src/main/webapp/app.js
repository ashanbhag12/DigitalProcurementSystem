'use strict';
angular.module('mainApp', ['ngMaterial', 'ngMessages', 'ngResource', 'ui.router', 'dashboardApp',
    'customersApp', 'addCustomerApp', 'editCustomerApp', 'deleteCustomerApp',
    'addSupplierApp', 'editSupplierApp', 'deleteSupplierApp',
    'addProductApp', 'editProductApp', 'deleteProductApp',
    'buildOrderApp', 'placeOrderApp'])
        .config(function ($stateProvider, $urlRouterProvider) {

            $urlRouterProvider.otherwise("/dashboard");

            $stateProvider
                    .state('dashboard', {
                        url: "/dashboard",
                        templateUrl: "home/views/dashboard.html",
                        controller: "dashboardController"
                    })

                    .state('addProduct', {
                        url: "/addProduct",
                        templateUrl: "products/views/addProduct.html",
                        controller: "addProductController"
                    })

                    .state('editProduct', {
                        url: "/editProduct",
                        templateUrl: "products/views/editProduct.html",
                        controller: "editProductController"
                    })

                    .state('deleteProduct', {
                        url: "/deleteProduct",
                        templateUrl: "products/views/deleteProduct.html",
                        controller: "deleteProductController"
                    })

                    .state('customers', {
                        url: "/customers",
                        templateUrl: "customers/views/customers.html",
                        controller: "customersCtrl"
                    })
                    
                    .state('addCustomer', {
                        url: "/addCustomer",
                        templateUrl: "customers/views/addCustomer.html",
                        controller: "addCustomerController"
                    })
                    
                    .state('editCustomer', {
                        url: "/editCustomer",
                        templateUrl: "customers/views/editCustomer.html",
                        controller: "editCustomerController"
                    })
                    
                    .state('deleteCustomer', {
                        url: "/deleteCustomer",
                        templateUrl: "customers/views/deleteCustomer.html",
                        controller: "deleteCustomerController"
                    })

                    .state('addSupplier', {
                        url: "/addSupplier",
                        templateUrl: "suppliers/views/addSupplier.html",
                        controller: "addSupplierController"
                    })

                    .state('editSupplier', {
                        url: "/editSupplier",
                        templateUrl: "suppliers/views/editSupplier.html",
                        controller: "editSupplierController"
                    })

                    .state('deleteSupplier', {
                        url: "/deleteSupplier",
                        templateUrl: "suppliers/views/deleteSupplier.html",
                        controller: "deleteSupplierController"
                    })
                    
                    .state('buildOrder', {
                        url: "/buildOrder",
                        templateUrl: "buildOrder/views/buildOrder.html",
                        controller: "buildOrderController"
                    })
                    
                    .state('placeOrder', {
                        url: "/placeOrder",
                        templateUrl: "placeOrder/views/placeOrder.html",
                        controller: "placeOrderController"
                    });
        });
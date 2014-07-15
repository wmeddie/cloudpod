/// <reference path="./typings/tsd.d.ts" />

var app = angular.module("app", ['ngRoute']);

app.config(['$routeProvider', function ($routeProvider: ng.route.IRouteProvider) {
    $routeProvider
        .when('/', {
                controller: 'LoginController',
                controllerAs: 'login',
                templateUrl: '/assets/partials/login.html'
            })
        .otherwise('/');
}]);

class LoginController {
    loginEmail: string;
    loginPassword: string;
    loginRemember: boolean;
    signUpEmail: string;
    signUpPassword: string;
    signUpConfirmation: string;
    signUpRemember: string;

    constructor() {

    }

    doLogin() {
        window.alert("login");
    }

    doSignUp() {
        window.alert("sign up");
    }
}
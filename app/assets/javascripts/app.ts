/// <reference path="./typings/tsd.d.ts" />

/**
 * Main Application module for Cloudpod.
 * @type {IModule}
 */
var app = angular.module("app", ['ngRoute']);

app.config(['$routeProvider', function ($routeProvider: ng.route.IRouteProvider) {
    $routeProvider
        .when('/', {
                controller: 'LoginController',
                controllerAs: 'login',
                templateUrl: '/assets/partials/login.html'
        })
        .when('/jams', {
                controller: 'JamsController',
                controllerAs: 'jams',
                templateUrl: '/assets/partials/jams.html'
        })
        .otherwise('/');
}]);

/**
 * Error Handler for AJAX requests.
 *
 * 500 errors are treated as an HTML page.
 * @param data response.
 * @param status HTTP status.
 */
function globalErrorHandler(data: any, status: number) {
    // TODO: Make more user friendly.
    if (status == 500) {
        var method = 'write';
        document[method](data); // document.write blocked by jslint.
        return;
    }

    alert(status);
    console.log(data);
}

/**
 * Service for persisting data client-side.
 */
class CloudpodSessionService {

    /**
     * Constructor for service.
     *
     * Should only be called by $factory.
     *
     * @param session Place where data will be put.
     */
    constructor(private session: Storage) {
    }

    /** userId from DB */
    get userId(): number {
        return parseInt(this.session.getItem('userId'), 10);
    }

    set userId(value: number) {
        this.session.setItem('userId', value.toString())
    }

    /** email used to log in. */
    get email(): string {
        return this.session.getItem('email');
    }

    set email(value: string) {
        this.session.setItem('email', value);
    }
}

app.factory('session', function () {
    return new CloudpodSessionService(window.localStorage);
});

/**
 * Controller for login.html
 */
class LoginController {
    /** Login side e-mail binding */
    loginEmail: string;
    /** Login side password binding */
    loginPassword: string;
     /** Login side form error message */
    loginError: string;
    /** Login side form element */
    loginForm: ng.IFormController;


    /** Sign up side e-mail binding. */
    signUpEmail: string;
    /** Sign up side password binding. */
    signUpPassword: string;
    /** Sign up side password confirmation binding. */
    signUpConfirmation: string;
    /** Sign up side form error message */
    signUpError: string;
    /** Sign up side form element */
    signUpForm: ng.IFormController;


    constructor(private $http: ng.IHttpService,
                private $location: ng.ILocationService,
                private session: CloudpodSessionService) {
    }

    /**
     * Perform validation checks and post to /login
     */
    doLogin() {
        if (this.loginForm.$invalid) {
            return;
        } else {
            this.loginError = '';
        }

        this.$http({
            method: 'POST',
            url: '/login',
            data: { email: this.loginEmail, password: this.loginPassword }
        }).success((data: any, status: number) => {
            this.session.userId = data.userId;
            this.session.email = this.loginEmail;

            this.$location.path('/jams');
        }).error((data: any, status: number) => {
            if (status < 500) {
                this.loginError = 'Invalid email or password combination.';
                this.loginPassword = '';
            } else {
                globalErrorHandler(data, status);
            }
        });
    }

    /**
     * Perform validation checks and post to /signUp.
     */
    doSignUp() {
        if (this.signUpForm.$invalid) {
            return;
        } else if (this.signUpPassword != this.signUpConfirmation) {
            this.signUpError = 'Passwords do not match';
            return;
        } else {
            this.signUpError = '';
        }

        this.$http({
            method: 'POST',
            url: '/signup',
            data: { email: this.signUpEmail, password: this.signUpPassword }
        }).success((data, status: number) => {
            this.session.userId = data.userId;
            this.session.email = this.signUpEmail;

            this.$location.path('/jams');
        }).error((data: any, status: number) => {
            if (status < 500) {
                this.signUpError = 'A user with that e-mail exists.';
            } else {
                globalErrorHandler(data, status);
            }
        });
    }
}

/**
 * Controller for the user's Jam list page.
 */
class JamController {
    constructor(private $http: ng.IHttpService,
                private session: CloudpodSessionService) {
    }
}
/// <reference path="./typings/tsd.d.ts" />
/**
* Main Application module for Cloudpod.
* @type {IModule}
*/
var app = angular.module("app", ['ngRoute', 'angularFileUpload']);

app.config([
    '$routeProvider', function ($routeProvider) {
        $routeProvider.when('/', {
            controller: 'LoginController',
            controllerAs: 'login',
            templateUrl: '/assets/partials/login.html'
        }).when('/player', {
            controller: 'PlayerController',
            controllerAs: 'player',
            templateUrl: '/assets/partials/player.html'
        }).otherwise('/');
    }]);

/**
* Error Handler for AJAX requests.
*
* 500 errors are treated as an HTML page.
* @param data response.
* @param status HTTP status.
*/
function globalErrorHandler(data, status) {
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
var CloudpodSessionService = (function () {
    /**
    * Constructor for service.
    *
    * Should only be called by $factory.
    *
    * @param session Place where data will be put.
    */
    function CloudpodSessionService(session) {
        this.session = session;
    }
    Object.defineProperty(CloudpodSessionService.prototype, "userId", {
        /** userId from DB */
        get: function () {
            return parseInt(this.session.getItem('userId'), 10);
        },
        set: function (value) {
            this.session.setItem('userId', value.toString());
        },
        enumerable: true,
        configurable: true
    });


    Object.defineProperty(CloudpodSessionService.prototype, "email", {
        /** email used to log in. */
        get: function () {
            return this.session.getItem('email');
        },
        set: function (value) {
            this.session.setItem('email', value);
        },
        enumerable: true,
        configurable: true
    });

    return CloudpodSessionService;
})();

app.factory('session', function () {
    return new CloudpodSessionService(window.localStorage);
});

/**
* Controller for login.html
*/
var LoginController = (function () {
    function LoginController($http, $location, session) {
        this.$http = $http;
        this.$location = $location;
        this.session = session;
    }
    /**
    * Perform validation checks and post to /login
    */
    LoginController.prototype.doLogin = function () {
        var _this = this;
        if (this.loginForm.$invalid) {
            this.loginError = 'All fields are required.';
            return;
        } else {
            this.loginError = '';
        }

        this.$http({
            method: 'POST',
            url: '/login',
            data: { email: this.loginEmail, password: this.loginPassword }
        }).success(function (data, status) {
            _this.session.userId = data.userId;
            _this.session.email = _this.loginEmail;

            _this.$location.path('/player');
        }).error(function (data, status) {
            if (status < 500) {
                _this.loginError = 'Invalid email or password combination.';
                _this.loginPassword = '';
            } else {
                globalErrorHandler(data, status);
            }
        });
    };

    /**
    * Perform validation checks and post to /signUp.
    */
    LoginController.prototype.doSignUp = function () {
        var _this = this;
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
        }).success(function (data, status) {
            _this.session.userId = data.userId;
            _this.session.email = _this.signUpEmail;

            _this.$location.path('/player');
        }).error(function (data, status) {
            if (status < 500) {
                _this.signUpError = 'A user with that e-mail exists.';
            } else {
                globalErrorHandler(data, status);
            }
        });
    };
    return LoginController;
})();

/**
* Data for enqueued tracks.
*/
var Song = (function () {
    function Song(title, artist, album, length, url) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.length = length;
        this.url = url;
    }
    return Song;
})();

/**
* The 'not playing' song.
* @type {Song}
*/
var nilSong = new Song("Not Playing", "Pick a jam below", "", 0, "");

/**
* Data for the Jam session list.
*/
var JamSession = (function () {
    function JamSession(name, spectators) {
        this.name = name;
        this.spectators = spectators;
    }
    return JamSession;
})();

/**
* Controller for the player page.
*/
var PlayerController = (function () {
    function PlayerController($http, session, $upload) {
        this.$http = $http;
        this.session = session;
        this.$upload = $upload;
        this.playing = false;
        this.songs = [];
        this.jams = [];
        this.activeJam = null;
        this.currentSong = nilSong;
        this.currentPosition = 0;
    }
    /**
    * Log user out and redirect to login page.
    */
    PlayerController.prototype.doLogout = function () {
        alert("TODO Logout");
    };

    /**
    * Upload the dropped file to the Library and Enqueue if successful.
    */
    PlayerController.prototype.fileDropped = function (files) {
        var first = files[0];
        this.$upload.upload({
            url: '/upload',
            method: 'POST',
            file: first,
            fileFormDataName: 'song'
        }).success(function (data, status, headers) {
            console.log(headers);
            alert("Uploaded!");
        });
    };

    /**
    * Send a start player event to the server.
    */
    PlayerController.prototype.play = function () {
        alert("TODO Play");
    };

    /**
    * Send a skip vote to the server.
    */
    PlayerController.prototype.skip = function () {
        alert("TODO Skip");
    };

    /**
    * Display a Jam creation form.
    */
    PlayerController.prototype.createJam = function () {
        alert("TODO Create Jam");
    };
    return PlayerController;
})();
//# sourceMappingURL=app.js.map

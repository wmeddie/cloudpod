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
        .when('/player', {
                controller: 'PlayerController',
                controllerAs: 'player',
                templateUrl: '/assets/partials/player.html'
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
            this.loginError = 'All fields are required.';
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

            this.$location.path('/player');
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

            this.$location.path('/player');
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
 * Data for enqueued tracks.
 */
class Song {
    constructor(public title: string,
                public artist: string,
                public album: string,
                public length: number,
                public src: string) {
    }
}

/**
 * The 'not playing' song.
 * @type {Song}
 */
var nilSong = new Song("Not Playing", "Pick a jam below", "", 0, "");

/**
 * Data for the Jam session list.
 */
class JamSession {
    constructor(public name: string,
                public spectators: number) {
    }
}

/**
 * Controller for the player page.
 */
class PlayerController {
    private playerEl: HTMLAudioElement;

    /** playing/stopped state (should change play button to pause.) */
    playing: boolean;
    /** Current playlist */
    songs: Array<Song>;
    /** List of Jams the user is invited to */
    jams: Array<JamSession>;
    /** Selected Jam */
    activeJam: JamSession;
    /** Currently playing song. */
    currentSong: Song;
    /** Scrubber position (0 - 100) */
    currentPosition: number;

    constructor(private $http: ng.IHttpService,
                private session: CloudpodSessionService) {
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
    doLogout() {
        alert("TODO Logout");
    }

    /**
     * Upload the dropped file to the Library and Enqueue if successful.
     */
    fileDropped(file: File) {
        alert("TODO File dropped");
    }

    /**
     * Send a start player event to the server.
     */
    play() {
        alert("TODO Play");
    }

    /**
     * Send a skip vote to the server.
     */
    skip() {
        alert("TODO Skip");
    }

    /**
     * Display a Jam creation form.
     */
    createJam() {
        alert("TODO Create Jam");
    }
}


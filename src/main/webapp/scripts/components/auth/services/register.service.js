'use strict';

angular.module('appreservationApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



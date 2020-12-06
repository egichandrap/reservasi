 'use strict';

angular.module('appreservationApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-appreservationApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-appreservationApp-params')});
                }
                return response;
            }
        };
    });

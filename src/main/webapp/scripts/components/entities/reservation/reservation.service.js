'use strict';

angular.module('appreservationApp')
    .factory('Reservation', function ($resource, DateUtils) {
        return $resource('api/reservations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                    data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
    .factory('ReservationService', function ($http, $resource) {
        var onGetSuccess = function (resp) {
            return resp.data;
        };

        var onGetError = function (resp) {
            return resp.data;
        };

        return {
            //PAGE
            getPageByKeyword: function (kolomPencarian, kataPencarian) {
                return $resource('api/reservations/getPageByKeyword', {
                    kolomPencarian: kolomPencarian,
                    kataPencarian: kataPencarian
                }, {});
            }
        }
    });

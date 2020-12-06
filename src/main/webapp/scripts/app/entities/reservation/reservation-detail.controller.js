'use strict';

angular.module('appreservationApp')
    .controller('ReservationDetailController', function ($scope, $rootScope, $stateParams, entity, Reservation, Costumer, Room) {
        $scope.reservation = entity;
        $scope.load = function (id) {
            Reservation.get({id: id}, function(result) {
                $scope.reservation = result;
            });
        };
        var unsubscribe = $rootScope.$on('appreservationApp:reservationUpdate', function(event, result) {
            $scope.reservation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('appreservationApp')
    .controller('RoomDetailController', function ($scope, $rootScope, $stateParams, entity, Room) {
        $scope.room = entity;
        $scope.load = function (id) {
            Room.get({id: id}, function(result) {
                $scope.room = result;
            });
        };
        var unsubscribe = $rootScope.$on('appreservationApp:roomUpdate', function(event, result) {
            $scope.room = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

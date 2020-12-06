'use strict';

angular.module('appreservationApp').controller('ReservationDialogController', ['$scope', '$rootScope', '$stateParams', '$uibModalInstance', 'entity', 'Reservation', 'Costumer', 'Room',
    function($scope, $stateParams,$rootScope, $uibModalInstance, entity, Reservation, Costumer, Room) {

        $scope.reservation = entity;
        $scope.costumers = Costumer.query();
        $scope.rooms = Room.query();
        $scope.load = function(id) {
            Reservation.get({ id: id }, function(result) {
                $scope.reservation = result;
            });
        };

        var onSaveSuccess = function(result) {
            $scope.$emit('appreservationApp:reservationUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function(result) {
            $scope.isSaving = false;
        };

        $scope.save = function() {
            $scope.isSaving = true;
            if ($scope.reservation.id != null) {
                Reservation.update($scope.reservation, onSaveSuccess, onSaveError);
            } else {
                Reservation.save($scope.reservation, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForStartDate = {};

        $scope.datePickerForStartDate.status = {
            opened: false
        };

        $scope.datePickerForStartDateOpen = function($event) {
            $scope.datePickerForStartDate.status.opened = true;
        };
        $scope.datePickerForEndDate = {};

        $scope.datePickerForEndDate.status = {
            opened: false
        };

        $scope.datePickerForEndDateOpen = function($event) {
            $scope.datePickerForEndDate.status.opened = true;
        };
        $scope.datePickerForCreateDate = {};

        $scope.datePickerForCreateDate.status = {
            opened: false
        };

        $scope.datePickerForCreateDateOpen = function($event) {
            $scope.datePickerForCreateDate.status.opened = true;
        };
    }
]);

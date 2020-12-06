'use strict';

angular.module('appreservationApp').controller('CostumerDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Costumer',
        function($scope, $stateParams, $uibModalInstance, entity, Costumer) {

        $scope.costumer = entity;
        $scope.load = function(id) {
            Costumer.get({id : id}, function(result) {
                $scope.costumer = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('appreservationApp:costumerUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.costumer.id != null) {
                Costumer.update($scope.costumer, onSaveSuccess, onSaveError);
            } else {
                Costumer.save($scope.costumer, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreateDate = {};

        $scope.datePickerForCreateDate.status = {
            opened: false
        };

        $scope.datePickerForCreateDateOpen = function($event) {
            $scope.datePickerForCreateDate.status.opened = true;
        };
}]);

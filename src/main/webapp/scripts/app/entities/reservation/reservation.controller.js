'use strict';

angular.module('appreservationApp')
    .controller('ReservationController', function (
    $scope,
    $state,
    Reservation,
    ParseLinks,
    ReservationService,
    Costumer,
    Room) {

        $scope.kolomPencarianList = [
            "Costumer",
            "Room"
        ];

        $scope.cari = {
            kolom: "Customer",
            kata: ""
        };

        $scope.reservations = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            ReservationService.getPageByKeyword(
            $scope.cari.kolom,
            $scope.cari.kata
            ).query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.reservations = result;
            });
        };

        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.reservation = {
                startDate: null,
                endDate: null,
                keterangan: null,
                createBy: null,
                createDate: null,
                id: null
            };
        };

//        $scope.approveReservation = function(reservation){
//            $http.get("/reservations/approve/"+ reservation.id).success(function(data){
//                alert("Approved!" + data);
//            });
//        }
//
//        $scope.rejectReservation = function(reservation){
//            $http.get("/reservations/reject/"+ reservation.id).success(function(data){
//                alert("Rejected!" + data);
//            });
//        }
    });

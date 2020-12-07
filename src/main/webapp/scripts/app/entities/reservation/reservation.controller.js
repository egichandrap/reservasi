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

//        $scope.approve = function(request){
//        var currentUserComment = request.comments.filter(function(comm)
//        return comm.userId == user.id && comm.status == "Pending";
//        })[0];
//        currentUserComment.status = State.APPROVED; // change comments Status
//        Comments.update(currentUserComment, function() { // send PUT request to API
//        // $rootScope.$broadcast('daysUpdated');
//        });
//        }

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
    });

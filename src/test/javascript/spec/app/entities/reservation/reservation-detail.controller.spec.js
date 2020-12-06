'use strict';

describe('Controller Tests', function() {

    describe('Reservation Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockReservation, MockCostumer, MockRoom;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockReservation = jasmine.createSpy('MockReservation');
            MockCostumer = jasmine.createSpy('MockCostumer');
            MockRoom = jasmine.createSpy('MockRoom');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Reservation': MockReservation,
                'Costumer': MockCostumer,
                'Room': MockRoom
            };
            createController = function() {
                $injector.get('$controller')("ReservationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'appreservationApp:reservationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

(function() {
    'use strict';

    angular
        .module('toDoJhipsterApp')
        .controller('CitizenDetailController', CitizenDetailController);

    CitizenDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Citizen', 'Company'];

    function CitizenDetailController($scope, $rootScope, $stateParams, previousState, entity, Citizen, Company) {
        var vm = this;

        vm.citizen = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('toDoJhipsterApp:citizenUpdate', function(event, result) {
            vm.citizen = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

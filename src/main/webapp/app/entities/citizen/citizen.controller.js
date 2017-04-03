(function() {
    'use strict';

    angular
        .module('toDoJhipsterApp')
        .controller('CitizenController', CitizenController);

    CitizenController.$inject = ['Citizen'];

    function CitizenController(Citizen) {

        var vm = this;

        vm.citizens = [];

        loadAll();

        function loadAll() {
            Citizen.query(function(result) {
                vm.citizens = result;
                vm.searchQuery = null;
            });
        }
    }
})();

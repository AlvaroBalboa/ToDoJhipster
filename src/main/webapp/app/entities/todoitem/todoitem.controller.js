(function() {
    'use strict';

    angular
        .module('toDoJhipsterApp')
        .controller('TodoitemController', TodoitemController);

    TodoitemController.$inject = ['Todoitem'];

    function TodoitemController(Todoitem) {

        var vm = this;

        vm.todoitems = [];

        loadAll();

        function loadAll() {
            Todoitem.query(function(result) {
                vm.todoitems = result;
                vm.searchQuery = null;
            });
        }
    }
})();

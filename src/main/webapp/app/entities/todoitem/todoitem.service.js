(function() {
    'use strict';
    angular
        .module('toDoJhipsterApp')
        .factory('Todoitem', Todoitem);

    Todoitem.$inject = ['$resource', 'DateUtils'];

    function Todoitem ($resource, DateUtils) {
        var resourceUrl =  'api/todoitems/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateCreated = DateUtils.convertLocalDateFromServer(data.dateCreated);
                        data.dateDue = DateUtils.convertLocalDateFromServer(data.dateDue);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateCreated = DateUtils.convertLocalDateToServer(copy.dateCreated);
                    copy.dateDue = DateUtils.convertLocalDateToServer(copy.dateDue);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateCreated = DateUtils.convertLocalDateToServer(copy.dateCreated);
                    copy.dateDue = DateUtils.convertLocalDateToServer(copy.dateDue);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();

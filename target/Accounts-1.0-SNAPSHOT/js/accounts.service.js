app.factory('accountsService', Service);
Service.$inject = ['$http', '$q'];
function Service($http, $q) {
    var url = './user';
    return {
        getPdfReport: getPdfReport,
        getUser: getUser
    };

    function getUser(userId) {
        return fetch(url + '?actionName=getUser' + '&userId=' + userId, {
            method: 'GET',
            headers: {
                "Content-type": "application/json; charset=UTF-8"
            }
        })
            .then((response) => response.json())
            .catch(function (error) {
                alert("Exception: " + error);
            })
    }

    function getPdfReport(filter) {
        let deferred = $q.defer();
        $.fileDownload('./report',
            {
                httpMethod: 'POST',
                data: {data: JSON.stringify(filter)},
                successCallback: function (response) {
                    deferred.resolve(response.data);
                },
                failCallback: function (error) {
                    let errorMessage = JSON.parse((new DOMParser().parseFromString(error,"text/xml").getElementsByTagName("pre"))[0].childNodes[0].nodeValue).error;
                    deferred.reject(errorMessage);
                }
            });
        return deferred.promise;
    }
}

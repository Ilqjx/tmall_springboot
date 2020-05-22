$(function () {
    var data = {
        order: {},
        total: 0
    };

    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            this.load();
        },
        methods: {
            load: function () {
                var total = getUrlParams("total");
                var oid = getUrlParams("oid");
                var url = "foregetorder/" + oid;
                this.total = total;
                axios.get(url).then(function (response) {
                    if (response.data.code == 1) {
                        vm.order = response.data.data;
                    }
                });
            }
        }
    });
});
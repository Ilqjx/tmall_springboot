$(function () {
    var data = {
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
                this.total = total;
            },
            confirmPay: function () {
                var oid = getUrlParams("oid");
                var url = "forepayed/" + oid;
                axios.get(url).then(function (response) {
                    if (response.data.code == 1) {
                        location.href = "payed?total=" + vm.total + "&oid=" + oid;
                    }
                });
            }
        }
    });
});
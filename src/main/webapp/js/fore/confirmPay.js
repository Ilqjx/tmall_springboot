$(function () {
    var data = {
        uri: "foreconfirmPay",
        order: {}
    };
    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            this.load();
        },
        methods: {
            load: function () {
                var oid = getUrlParams("oid");
                var url = this.uri + "/" + oid;
                axios.get(url).then(function (response) {
                    if (response.data.code == 1) {
                        vm.order = response.data.data;
                    }
                });
            },
            orderConfirmed: function () {
                var oid = getUrlParams("oid");
                var url = "foreorderConfirmed/" + oid;
                axios.put(url).then(function (response) {
                    if (response.data.code == 1) {
                        location.href = "orderConfirmed";
                    }
                });
            }
        }
    });
});
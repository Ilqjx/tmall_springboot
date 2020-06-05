$(function () {
    var data = {
        orderItems: [],
        total: 0,
        order: {
            address: "",
            post: "",
            receiver: "",
            mobile: "",
            userMessage: "",
            total: 0
        }
    };

    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            this.load();
            linkDefaultActions();
        },
        methods: {
            load: function () {
                var url = "forebuy";
                url = connectUrl(url);
                axios.get(url).then(function (response) {
                    vm.orderItems = response.data.data.orderItems;
                    vm.total = response.data.data.total;
                });
            },
            submitOrder: function () {
                if (checkEmpty("address")) {
                    alert("详细地址不能为空");
                    return;
                }
                if (checkEmpty("receiver")) {
                    alert("收货人姓名不能为空");
                    return;
                }
                if (checkEmpty("mobile")) {
                    alert("手机号码不能为空");
                    return;
                }
                var url = "forecreateOrder";
                url = connectUrl(url);
                vm.order.total = vm.total;
                axios.post(url, vm.order).then(function (response) {
                    if (response.data.code == 1) {
                        location.href = "alipay?oid=" + response.data.data.id + "&total=" + response.data.data.total;
                    }
                });
            }
        }
    });
});

function connectUrl(url) {
    var oiids = getUrlParams("oiid");
    if (oiids instanceof Array) {
        for (var index in oiids) {
            if (index == 0) {
                url = url + "?oiid=" + oiids[index];
            } else {
                url = url + "&oiid=" + oiids[index];
            }
        }
    } else {
        url = url + "?oiid=" + oiids;
    }
    return url;
}
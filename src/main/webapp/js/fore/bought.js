$(function () {
    var data = {
        orders: []
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
                var url = "forelistOrder";
                axios.get(url).then(function (response) {
                    if (response.data.code == 1) {
                        vm.orders = response.data.data;
                        vm.$nextTick(function () {
                            orderListeners();
                        });
                    }
                });
            }
        }
    });
});

function orderListeners() {
    // 筛选不同的订单状态
    $("a[orderstatus]").on("click", function () {
        var orderstatus = $(this).attr("orderstatus");
        if (orderstatus == "all") {
            $("table[orderstatus]").show();
        } else {
            $("table[orderstatus]").hide();
            $("table[orderstatus="+orderstatus+"]").show();
        }
        $("div.boughtDiv div.orderType div").removeClass("selectedOrderType");
        $(this).parent("div").addClass("selectedOrderType");
    })

    // 删除
    var deleteOrderid = 0;
    var deleteOrder = false;
    $("a.deleteOrderLink").click(function() {
        deleteOrderid = $(this).attr("oid");
        deleteOrder = false;
        $("#deleteConfirmModal").modal("show");
    });

    $("button.deleteConfirmButton").click(function() {
        deleteOrder = true;
        $("#deleteConfirmModal").modal('hide');
    });

    $("div#deleteConfirmModal").on("hidden.bs.modal", function (e) {
        if (deleteOrder) {
            var url = "foredeleteOrder/" + deleteOrderid;
            axios.delete(url).then(function (response) {
                if (response.data.code == 1) {
                    location.reload();
                }
            });
        }
    });
}
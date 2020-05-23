$(function () {
    var data = {
        orderItems: []
    };

    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            this.load();
        },
        methods: {
            load: function () {
                var url = "forelistOrderItem";
                axios.get(url).then(function (response) {
                    if (response.data.code == 1) {
                        vm.orderItems = response.data.data;
                        vm.$nextTick(function () {
                            cartAction();
                        });
                    }
                });
            }
        }
    });
});

// 更改结算按钮状态
function syncCreateOrderButton() {
    var selectAny = false;
    $("img.cartProductItemIfSelected").each(function () {
        if ("selectit" == $(this).attr("selectit")) {
            selectAny = true;
        }
    })
    if (selectAny) {
        // disabled 按钮不可点击
        $("button.createOrderButton").css("background-color", "#C40000");
        $("button.createOrderButton").removeAttr("disabled");
    } else {
        $("button.createOrderButton").css("background-color", "#AAA");
        $("button.createOrderButton").attr("disabled", "disabled");
    }
}

// 是否全选
function syncSelect() {
    var selectAll = true;
    $("img.cartProductItemIfSelected").each(function () {
        if ($(this).attr("selectit") == "false") {
            selectAll = false;
        }
    })
    if (selectAll) {
        $("img.selectAllItem").attr("selectit", "selectit");
        $("img.selectAllItem").attr("src", "img/site/cartSelected.png");
    } else {
        $("img.selectAllItem").attr("selectit", "false");
        $("img.selectAllItem").attr("src", "img/site/cartNotSelected.png");
    }
}

// 设置总金额和总数量
function calcCartSumPriceAndNumber() {
    var sum = 0;
    var totalNumber = 0;
    $("img.cartProductItemIfSelected[selectit='selectit']").each(function () {
        var oiid = $(this).attr("oiid");
        var price = $("span.cartProductItemSmallSumPrice[oiid="+oiid+"]").text();
        price = price.replace(/,/g, "");
        price = price.replace(/￥/g, "");
        sum += new Number(price);
        var num = $("input.orderItemNumberSetting[oiid="+oiid+"]").val();
        totalNumber += new Number(num);
    })
    $("span.cartSumPrice").html("￥" + formatMoney(sum));
    $("span.cartTitlePrice").html("￥" + formatMoney(sum));
    $("span.cartSumNumber").html(totalNumber);
}

// 修改购物车的数量
function cartNumber() {
    var totalNumber = 0;
    $("input.orderItemNumberSetting").each(function () {
        var hide = $(this).attr("hide");
        if (hide == "false") {
            var num = $(this).val();
            totalNumber += new Number(num);
        }
    })
    $("span#cartProductNumber").html(totalNumber);
}

// 用于对表单元素的处理
function syncPrice(pid, num, price) {
    $("input.orderItemNumberSetting[pid="+pid+"]").val(num);
    var cartProductItemSmallSumPrice = formatMoney(num * price);
    $("span.cartProductItemSmallSumPrice[pid="+pid+"]").html("￥" + cartProductItemSmallSumPrice);
    calcCartSumPriceAndNumber();
    cartNumber();

    // 更新数据库中订单项中产品的数量
    var url = "forechangeOrderItem?pid=" + pid + "&num=" + num;
    axios.put(url);
}

function cartAction() {
    var deleteOrderItemId = 0;
    var deleteOrderItem = false;
    // 删除
    $("a.deleteOrderItem").click(function () {
        deleteOrderItem = false;
        var oiid = $(this).attr("oiid");
        deleteOrderItemId = oiid;
        $("div#deleteConfirmModal").modal('show');
    })

    // 确认删除
    $("button.deleteConfirmButton").click(function () {
        deleteOrderItem = true;
        $("div#deleteConfirmModal").modal('hide');
    })

    // 在数据库中进行删除
    $("div#deleteConfirmModal").on("hide.bs.modal", function () {
        if (deleteOrderItem) {
            var url = "foredeleteOrderItem/" + deleteOrderItemId;
            axios.delete(url).then(function (response) {
                location.reload();
            });
        }
    })

    // 点击结算按钮跳转到结算页面
    $("button.createOrderButton").click(function () {
        var params = "";
        $("img.cartProductItemIfSelected").each(function () {
            if ("selectit" == $(this).attr("selectit")) {
                var oiid = $(this).attr("oiid");
                params += "&oiid=" + oiid;
            }
        })
        params = params.substring(1);
        location.href = "buy?" + params;
    })

    // 选中一种商品
    $("img.cartProductItemIfSelected").on("click", function () {
        var selectit = $(this).attr("selectit");
        if (selectit == "selectit") {
            $(this).attr("selectit", "false");
            $(this).attr("src", "img/site/cartNotSelected.png");
            $(this).parents("tr.cartProductItemTR").css("background-color", "#FFF");
        } else {
            $(this).attr("selectit", "selectit");
            $(this).attr("src", "img/site/cartSelected.png");
            $(this).parents("tr.cartProductItemTR").css("background-color", "#FFF8E1");
        }
        syncSelect();
        syncCreateOrderButton();
        calcCartSumPriceAndNumber();
    })

    // 全选
    $("img.selectAllItem").on("click", function () {
        var selectit = $(this).attr("selectit");
        if (selectit == "selectit") {
            $("img.selectAllItem").attr("selectit", "false");
            $("img.selectAllItem").attr("src", "img/site/cartNotSelected.png");
            $("img.cartProductItemIfSelected").attr("selectit", "false");
            $("img.cartProductItemIfSelected").attr("src", "img/site/cartNotSelected.png");
            $("tr.cartProductItemTR").css("background-color", "#FFF");
        } else {
            $("img.selectAllItem").attr("selectit", "selectit");
            $("img.selectAllItem").attr("src", "img/site/cartSelected.png");
            $("img.cartProductItemIfSelected").attr("selectit", "selectit");
            $("img.cartProductItemIfSelected").attr("src", "img/site/cartSelected.png");
            $("tr.cartProductItemTR").css("background-color", "#FFF8E1");
        }
        syncCreateOrderButton();
        calcCartSumPriceAndNumber();
    })

    // 增加数量
    $("a.numberPlus").on("click", function () {
        var pid = $(this).attr("pid");
        var stock = $("span.orderItemStock[pid="+pid+"]").text();
        var price = $("span.orderItemPromotePrice[pid="+pid+"]").text();
        var num = $("input.orderItemNumberSetting[pid="+pid+"]").val();
        num++;
        if (num > stock) {
            num = stock;
        }
        syncPrice(pid, num, price);
    })

    // 减少数量
    $("a.numberMinus").on("click", function () {
        var pid = $(this).attr("pid");
        var price = $("span.orderItemPromotePrice[pid="+pid+"]").text();
        var num = $("input.orderItemNumberSetting[pid="+pid+"]").val();
        num--;
        if (num <= 0) {
            num = 1;
        }
        syncPrice(pid, num, price);
    })

    // 直接修改数量
    $("input.orderItemNumberSetting").keyup(function () {
        var pid = $(this).attr("pid");
        var stock = $("span.orderItemStock[pid="+pid+"]").text();
        var num = $("input.orderItemNumberSetting[pid="+pid+"]").val();
        var price = $("span.orderItemPromotePrice[pid="+pid+"]").text();
        num = parseInt(num);
        if (isNaN(num)) {
            num = 1;
        }
        if (num <= 0) {
            num = 1;
        }
        if (num > stock) {
            num = stock;
        }
        syncPrice(pid, num, price);
    })
}
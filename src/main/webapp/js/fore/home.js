$(function () {
    var data = {
        uri: "forehome",
        categorys: []
    };

    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            this.listCategory();
        },
        methods: {
            listCategory: function () {
                var url = this.uri;
                axios.get(url).then(function (response) {
                    vm.categorys = response.data;
                    vm.$nextTick(function () {
                        homePageRegisterListeners();
                    });
                });
            }
        },
        filters: {
            subTitleFilter: function (value) {
                if (!value) {
                    return "";
                }
                var title = value.split(" ");
                return title[0];
            },
            subStringFilter: function (value) {
                if (!value) {
                    return "";
                }
                return value.substr(0, 20);
            },
            formatMoneyFilter: function (num) {
                num = num.toString().replace(/\$|\,/g, '');
                if (isNaN(num)) {
                    num = "0";
                }
                sign = (num == (num = Math.abs(num)));
                num = Math.floor(num * 100 + 0.50000000001);
                cents = num % 100;
                num = Math.floor(num / 100).toString();
                if (cents < 10) {
                    cents = "0" + cents;
                }
                for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++) {
                    num = num.substring(0, num.length-(4*i+3)) + ',' + num.substring(num.length-(4*i+3));
                }
                return (((sign)?'':'-') + num + '.' + cents);
            }
        }
    });
});

function homePageRegisterListeners() {
    $("div.eachCategory").mouseenter(function() {
        var cid = $(this).attr("cid");
        showProductsAsideCategory(cid);
    });

    $("div.eachCategory").mouseleave(function() {
        var cid = $(this).attr("cid");
        hideProductsAsideCategory(cid);
    });

    $("div.productsAsideCategorys").mouseenter(function() {
        var cid = $(this).attr("cid");
        showProductsAsideCategory(cid);
    });

    $("div.productsAsideCategorys").mouseleave(function() {
        var cid = $(this).attr("cid");
        hideProductsAsideCategory(cid);
    });

    $("div.productsAsideCategorys div.row a").each(function () {
        // Math.round() 四舍五入
        // 1/5 概率变色
        var randomDigit = Math.round(Math.random() * 4);
        if (randomDigit == 2) {
            $(this).css("color", "#87CEFA");
        }
    })
}

function showProductsAsideCategory(cid) {
    $("div.eachCategory[cid = "+cid+"]").css("background-color", "#fff");
    $("div.eachCategory[cid = "+cid+"] a").css("color", "#87cefa");
    $("div.productsAsideCategorys[cid = "+cid+"]").show();
}

function hideProductsAsideCategory(cid) {
    $("div.eachCategory[cid = "+cid+"]").css("background-color", "#e2e2e3");
    $("div.eachCategory[cid = "+cid+"] a").css("color", "#000");
    $("div.productsAsideCategorys[cid = "+cid+"]").hide();
}
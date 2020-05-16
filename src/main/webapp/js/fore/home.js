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
            linkDefaultActions();
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

    $("div.rightMenu span").mouseenter(function () {
        var left = $(this).position().left;
        var top = $(this).position().top;
        var width = $(this).css("width");
        var imageWidth = $("img#catear").css("width");
        $("img#catear").css("left", parseInt(left) + parseInt(width) / 2 - parseInt(imageWidth) / 2);
        $("img#catear").css("top", top - 20);
        $("img#catear").fadeIn(500);
    });

    $("div.rightMenu span").mouseleave(function () {
        $("img#catear").hide();
    });

    var left = $("div#carousel-of-product").offset().left;
    $("div.categoryMenu").css("left", left - 20);
    $("div.categoryWithCarousel div.head").css("margin-left", left);
    $("div.productsAsideCategorys").css("left", left - 20);

    $("div.productsAsideCategorys div.row a").each(function () {
        // 1/5 概率变色
        var randomDigit = Math.round(Math.random() * 6);
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
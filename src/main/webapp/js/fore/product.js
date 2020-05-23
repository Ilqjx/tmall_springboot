$(function () {
    var data = {
        uri: "foreproduct",
        product: {id: 0, category: {}, firstProductImage: {}},
        singleProductImages: [],
        detailProductImages: [],
        propertyValues: [],
        reviews: [],
        user: {name: "", password: ""},
        orderItem: {product: {}, number: 1}
    };

    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            var pid = getUrlParams("pid");
            this.getProduct(pid);
            linkDefaultActions();
            loginAction();
        },
        methods: {
            getProduct: function (id) {
                var url = this.uri + "/" + id;
                axios.get(url).then(function (response) {
                    vm.product = response.data.data.product;
                    vm.singleProductImages = response.data.data.singleProductImages;
                    vm.detailProductImages = response.data.data.detailProductImages;
                    vm.propertyValues = response.data.data.propertyValues;
                    vm.reviews = response.data.data.reviews;

                    vm.$nextTick(function () {
                        productAction(vm);
                    });
                });
            },
            login: function () {
                if (checkEmpty("modalName")) {
                    $("div.loginErrorMessageDiv").show();
                    $("span.errorMessage").html("用户名不能为空");
                    return;
                }
                if (checkEmpty("modalPassword")) {
                    $("div.loginErrorMessageDiv").show();
                    $("span.errorMessage").html("密码不能为空");
                    return;
                }
                var url = "forelogin";
                axios.post(url, this.user).then(function (response) {
                    if (response.data.code == 1) {
                        location.reload();
                    } else {
                        $("div.loginErrorMessageDiv").show();
                        $("span.errorMessage").html(response.data.message);
                    }
                });
            },
            subTitleSplit: function (value) {
                if (!value) {
                    return "";
                }
                var subTitles = value.split(" ");
                return subTitles;
            }
        }
    });
});

function productAction(vm) {
    var initBigImg = false;
    $("img.smallImage").mouseenter(function () {
        var bigImageURL = $(this).attr("bigImageURL");
        $("img.bigImg").attr("src", bigImageURL);
    });

    // 预加载 大图片加载后调用这个函数
    $("img.bigImg").on(function () {
        if (initBigImg) {
            return;
        }
        $("img.smallImage").each(function () {
            var bigImageURL = $(this).attr("bigImageURL");
            var img = new Image();
            img.src = bigImageURL;
            // 图像加载完成后
            img.onload(function () {
                $("div.img4load").append($(img));
            })
        })
        initBigImg = true;
    });

    var stock = vm.product.stock;
    $("input.productNumberSetting").keyup(function () {
        var num = $(this).val();
        num = parseInt(num);
        if (isNaN(num)) {
            $(this).val("");
            return;
        }
        if (num < 1) {
            num = 1;
        }
        if (num > stock) {
            num = stock;
        }
        $(this).val(num);
    });

    $("a.increaseNumber").on("click", function () {
        var num = $("input.productNumberSetting").val();
        num = parseInt(num);
        if (isNaN(num)) {
            num = 1;
        } else {
            num++;
        }
        if (num > stock) {
            num = stock;
        }
        $("input.productNumberSetting").val(num);
    });

    $("a.decreaseNumber").on("click", function () {
        var num = $("input.productNumberSetting").val();
        num = parseInt(num);
        if (isNaN(num)) {
            num = 1;
        } else {
            num--;
        }
        if (num < 1) {
            num = 1;
        }
        $("input.productNumberSetting").val(num);
    });

    $("div.productReviewDiv").hide();

    $("a.productDetailTopReviewLink").click(function() {
        $("div.productDetailDiv").hide();
        $("div.productReviewDiv").show();
    });

    $("a.productReviewTopPartSelectedLink").click(function() {
        $("div.productDetailDiv").show();
        $("div.productReviewDiv").hide();
    });

    $("button.buyButton").click(function () {
        var url = "forecheckLogin";
        axios.get(url).then(function (response) {
            if (response.data.code == 0) {
                $("div#loginModal").modal('show');
            } else {
                var number = $("input#productNumber").val();
                vm.orderItem.product = vm.product;
                vm.orderItem.number = number;
                var url = "forebuyone";
                axios.post(url, vm.orderItem).then(function (response) {
                    location.href = "buy?oiid=" + response.data.data.id;
                });
            }
        });
    });

    $("button.addCartButton").click(function () {
        var url = "forecheckLogin";
        axios.get(url).then(function (response) {
            if (response.data.code == 0) {
                $("div#loginModal").modal('show');
            } else {
                var number = $("input#productNumber").val();
                vm.orderItem.product = vm.product;
                vm.orderItem.number = number;
                var url = "foreaddCart";
                axios.post(url, vm.orderItem).then(function (response) {
                    $("button.addCartButton").html("已加入购物车");
                    $("button.addCartButton").attr("disabled", "disabled");
                    $("button.addCartButton").css("background-color", "#d3d3d3");
                    $("button.addCartButton").css("border", "1px solid #d3d3d3");
                    $("button.addCartButton").css("color", "#000");
                    // var cartNumber = parseInt(${cartTotalItemNumber}) + parseInt(num);
                    // $("strong").html(cartNumber);
                });
            }
        });
    });
}

function loginAction() {
    $("div.loginInput input").keyup(function () {
        $("div.loginErrorMessageDiv").hide();
    })
}
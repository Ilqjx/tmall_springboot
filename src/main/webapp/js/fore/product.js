$(function () {
    $("div.productReviewDiv").hide();

    // $("a.productDetailTopReviewLink").click(function() {
    //     console.log("------------------------");
    //     $("div.productDetailDiv").hide();
    //     $("div.productReviewDiv").show();
    // })
    //
    // $("a.productReviewTopPartSelectedLink").click(function() {
    //     console.log("*******************************");
    //     $("div.productDetailDiv").show();
    //     $("div.productReviewDiv").hide();
    // })

    var data = {
        uri: "foreproduct",
        product: {},
        singleProductImages: []
    };

    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            var pid = getUrlParams("pid");
            this.getProduct(pid);
            this.listProductSingleImage(pid);
        },
        methods: {
            getProduct: function (id) {
                var url = this.uri + "/" + id;
                axios.get(url).then(function (response) {
                    vm.product = response.data;
                    vm.$nextTick(function () {
                        productAction(vm);
                    });
                });
            },
            listProductSingleImage: function (pid) {
                var url = "foreproductsingleimage/" + pid;
                axios.get(url).then(function (response) {
                    vm.singleProductImages = response.data;
                    vm.$nextTick(function () {
                        productImageAction();
                    })
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

function productImageAction() {
    var initBigImg = false;
    $("img.smallImage").mouseenter(function () {
        var bigImageURL = $(this).attr("bigImageURL");
        $("img.bigImg").attr("src", bigImageURL);
    })

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
    })
}

function productAction(vm) {
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
    })

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
    })

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
    })
}
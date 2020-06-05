$(function () {
    var data = {
        uri: "forereview",
        product: {firstProductImage: {}},
        order: {},
        review: {id: 0, content: "", product: {}},
        reviews: []
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
                        vm.product = response.data.data.product;
                        vm.order = response.data.data.order;
                        vm.$nextTick(function () {
                            vm.listReview();
                        });
                    }
                });
            },
            doReview: function () {
                var oid = getUrlParams("oid");
                var url = "foredoReview/" + oid;
                this.review.product = this.product;
                axios.post(url, this.review).then(function (response) {
                    if (response.data.code == 1) {
                        location.reload();
                    }
                });
            },
            listReview: function () {
                var pid = this.product.id;
                var url = "forelistReview/" + pid;
                axios.get(url).then(function (response) {
                    if (response.data.code == 1) {
                        vm.reviews = response.data.data;
                    }
                });
            }
        }
    });
});
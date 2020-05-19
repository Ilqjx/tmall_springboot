$(function () {
    var data = {
        uri: "foresearch",
        products: [],
    };

    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            var keyword = getUrlParams("keyword");
            $("input#keyword").val(decodeURI(keyword));
            this.listProductByKeyword(keyword);
            linkDefaultActions();
        },
        methods: {
            listProductByKeyword: function (keyword) {
                var url = this.uri + "?keyword=" + keyword;
                axios.post(url).then(function (response) {
                    console.log(response.data.data);
                    if (response.data.code == 1) {
                        vm.products = response.data.data;
                    }
                });
            }
        }
    });
});
$(function () {
    var data = {
        oiids: []
    };

    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            this.load();
        },
        methods: {
            load: function () {
                var oiids = getUrlParams("oiid");
                var url = "foreorderitem";
                axios.get(url, oiids).then(function (response) {

                });
            }
        }
    });
});
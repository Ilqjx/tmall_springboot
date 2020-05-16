$(function () {
    var data = {
        uri: "foreregister",
        user: {id: 0, name: "", password: ""}
    };

    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            linkDefaultActions();
            registerAction();
        },
        methods: {
            register: function () {
                if (checkEmpty("name")) {
                    $("div.registerErrorMessageDiv").show();
                    $("span.errorMessage").html("请输入用户名");
                    return;
                }
                if (checkEmpty("password")) {
                    $("div.registerErrorMessageDiv").show();
                    $("span.errorMessage").html("请输入密码");
                    return;
                }
                if (checkEmpty("repeatpassword")) {
                    $("div.registerErrorMessageDiv").show();
                    $("span.errorMessage").html("请输入重复密码");
                    return;
                }
                var password = $("#password").val();
                var repeatpassword = $("#repeatpassword").val();
                if (password != repeatpassword) {
                    $("div.registerErrorMessageDiv").show();
                    $("span.errorMessage").html("密码不一致，请重新输入");
                    return;
                }
                var url = this.uri;
                axios.post(url, this.user).then(function (response) {
                    if (response.data.code == 1) {
                        location.href = "registerSuccess";
                    } else {
                        $("div.registerErrorMessageDiv").show();
                        $("span.errorMessage").html(response.data.message);
                    }
                });
            }
        }
    });
});

function registerAction() {
    $("div.registerDiv input").keyup(function () {
        $("div.registerErrorMessageDiv").hide();
    });
}
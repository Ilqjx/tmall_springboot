$(function () {
    var data = {
        uri: "forelogin",
        user: {id: 0, name: "", password: ""}
    };
    var vm = new Vue({
        el: "#workingArea",
        data: data,
        mounted: function () {
            linkDefaultActions();
            loginActoin();
        },
        methods: {
            login: function () {
                if (checkEmpty("name")) {
                    $("div.loginErrorMsgDiv").show();
                    $("span#errorMessage").html("用户名不能为空");
                    return;
                }
                if (checkEmpty("password")) {
                    $("div.loginErrorMsgDiv").show();
                    $("span#errorMessage").html("密码不能为空");
                    return;
                }
                var url = this.uri;
                axios.post(url, this.user).then(function (response) {
                    if (response.data.code == 1) {
                        location.href = "home";
                    } else {
                        $("div.loginErrorMsgDiv").show();
                        $("span#errorMessage").html(response.data.message);
                    }
                });
            }
        }
    });
});

function loginActoin() {
    $("div#loginSmallDiv input").keyup(function () {
        $("div.loginErrorMsgDiv").hide();
    });
}
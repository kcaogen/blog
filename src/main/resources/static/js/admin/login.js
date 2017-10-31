var login = {

    init : function (params) {
        $("button").click(function(){
            login.loginForm(params);
        });
    },

    loginForm : function (params) {
        var headers = {};
        headers['X-CSRF-TOKEN'] = params.token;
        $("#login").submit(function(e){
            $.ajax({
                type: "POST",
                url: "/login",
                headers: headers,
                data: $("#login").serialize(),
                success:function (data) {
                    alert(data)
                    //window.location.href = "/blog";
                }
            })
        });
    }

}
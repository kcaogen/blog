var blogTypeList = {

    init : function (params) {
        blogTypeList.addClick(params);
    },

    URL : {
        addBlogType : function() {
            return "/admin/addBlogType";
        }
    },

    addClick : function (params) {
        $("#addBlogType").click(function () {
            layer.prompt({title: '输入类型名称', formType: 2}, function(val, index){
                if(val.trim() != ""){
                    params.typeName = val.trim();
                    blogTypeList.addBlogType(params);
                }
            });
        });
    },

    addBlogType : function (params) {
        var headers = {};
        headers['X-CSRF-TOKEN'] = params.token;
        $.ajax({
            type: "POST",
            url: blogTypeList.URL.addBlogType(),
            headers: headers,
            data: {"typeName": params.typeName},
            success:function (data) {
                if (data.result == true) {
                    layer.msg("success", function(){
                        location.reload();
                    });
                }
            }
        })
    }

}
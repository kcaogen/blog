var blogTagList = {

    init : function (params) {
        blogTagList.addClick(params);
    },

    URL : {
        addBlogTag : function() {
            return "/admin/addBlogTag";
        }
    },

    addClick : function (params) {
        $("#addBlogTag").click(function () {
            layer.prompt({title: '输入标签名称', formType: 2}, function(val, index){
                if(val.trim() != ""){
                    params.tagName = val.trim();
                    blogTagList.addBlogTag(params);
                }
            });
        });
    },

    addBlogTag : function (params) {
        var headers = {};
        headers['X-CSRF-TOKEN'] = params.token;
        $.ajax({
            type: "POST",
            url: blogTagList.URL.addBlogTag(),
            headers: headers,
            data: {"tagName": params.tagName},
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
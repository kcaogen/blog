var blogList = {

    init : function (params) {
        blogList.ajaxBlogList(params);
        $(".load-more").click(function(){
            blogList.loadMore(params);
        });
    },

    URL : {
        blogList : function () {
            return "/blogList";
        }
    },

    ajaxBlogList : function (params) {
        $.ajax({
            type: "POST",
            url: blogList.URL.blogList(),
            data: {"page": params.page, "blogType": params.blogType},
            success:function(data){
                if(data.result==true){
                    blogList.loadBlogInfo(data.data);
                    if(params.page >= params.maxPage){
                        $(".pager").hide();
                    }else{
                        $(".pager").show();
                    }
                }else{
                    layer.msg(data.error, function(){});
                }
            }
        });
    },

    loadBlogInfo : function (json) {
        var blogList = $.templates("#blogListJsrender");
        var temp = blogList.render(json);
        $("#blogList").append(temp);
    },
    
    loadMore : function (params) {
        params.page += 1;
        if(params.page > params.maxPage){
            return false;
        }
        blogList.ajaxBlogList(params);
    }

}
var layout = {

    init : function (params) {
        var blogType = params.blogType;
        layout.changeType(blogType)
    },

    changeType : function (blogType) {
        if(blogType != undefined && blogType != ""){
            $("#blogTypeList").find("li").each(function(){
                if($(this).find("a").text() == blogType){
                    $(this).css("background-color","#428bca");
                    $(this).find("a").css("color","white");
                }
            });
        }
    }

}
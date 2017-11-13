var layout = {

    init : function (params) {
        var blogType = params.blogType;
        var keywords = params.keywords;
        layout.changeType(blogType);
        layout.searchClick(keywords);
    },

    URL : {
        SearChBlogUrl : function () {
            return "/blog/solr/";
        }
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
    },

    searchClick : function (keywords) {
        $("#keywords").val(keywords);
        $("#search").click(function () {
            layout.search();
        });
        $("#keywords").keydown(function(event){
            if(event.which == "13") {
                layout.search();
                return false;
            }
        });
    },

    search : function () {
        var keywords = $("#keywords").val();
        window.location.href = layout.URL.SearChBlogUrl() + keywords;
    }

}
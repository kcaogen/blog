var blogList = {

    init : function (params) {
        blogList.click();
        blogList.searchValue(params);
    },

    URL : {
        blogList : function() {
            return '/admin/blogList';
        }
    },

    /**
     * 点击事件
     * @constructor
     */
    click : function () {
        $("#blogSearch").click(function(){
            blogList.search();
        });
        $(document).keydown(function(e) {
            if (e.keyCode == 13) {
                blogList.search();
            }
        });
    },

    /**
     * 搜索事件
     * @constructor
     */
    search : function () {
        var blogCondition = $("#blogCondition").val();
        location.href = blogList.URL.blogList() + "?blogCondition=" + blogCondition;
    },

    /**
     * 搜索表单赋值
     * @param params
     * @constructor
     */
    searchValue : function (params) {
        $("#blogCondition").val(params['blogCondition']);
    }

}
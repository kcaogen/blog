var blogList = {

    init : function (params) {
        if (params.keywords == null) {
            blogList.ajaxBlogList(params);
        }else {
            blogList.ajaxBlogListBySolr(params);
        }
        $(".load-more").click(function(){
            blogList.loadMore(params);
        });
    },

    URL : {
        blogList : function () {
            return "/blog/list";
        },
        blogListBySolr : function () {
            return "/blog/listBySolr";
        }
    },

    ajaxBlogList : function (params) {
        var headers = {};
        headers['X-CSRF-TOKEN'] = params.token;
        $.ajax({
            type: "POST",
            url: blogList.URL.blogList(),
            headers: headers,
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

    ajaxBlogListBySolr : function (params) {
        var headers = {};
        headers['X-CSRF-TOKEN'] = params.token;
        $.ajax({
            type: "POST",
            url: blogList.URL.blogListBySolr(),
            headers: headers,
            data: {"page": params.page, "keywords": params.keywords},
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
        $.views.converters({
            times: function(val){
                var dateSeparator = "-";
                var timeSeparator = ":";
                var date = new Date(val),
                    year = date.getFullYear(),// 获取完整的年份(4位,1970)
                    month = date.getMonth(),// 获取月份(0-11,0代表1月,用的时候记得加上1)
                    day = date.getDate(),// 获取日(1-31)
                    hour = date.getHours(),// 获取小时数(0-23)
                    minute = date.getMinutes(),// 获取分钟数(0-59)
                    seconds = date.getSeconds(),// 获取秒数(0-59)
                    Y = year + dateSeparator,
                    M = ((month + 1) > 9 ? (month + 1) : ('0' + (month + 1))) + dateSeparator,
                    D = (day > 9 ? day : ('0' + day)) + ' ',
                    h = (hour > 9 ? hour : ('0' + hour)) + timeSeparator,
                    m = (minute > 9 ? minute : ('0' + minute)) + timeSeparator,
                    s = (seconds > 9 ? seconds : ('0' + seconds)),
                    formatDate = Y + M + D + h + m + s;
                return formatDate;
            }
        })
        var blogList = $.templates("#blogListJsrender");
        var temp = blogList.render(json);
        $("#blogList").append(temp);
    },
    
    loadMore : function (params) {
        params.page += 1;
        if(params.page > params.maxPage){
            return false;
        }
        if (params.keywords == null) {
            blogList.ajaxBlogList(params);
        }else {
            blogList.ajaxBlogListBySolr(params);
        }
    }


}
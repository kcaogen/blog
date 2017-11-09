var pagination = {

    init : function (params) {
        //获取当前页
        var pageNum = params['pageNum'];
        //获取总页数
        var totalPages = params['totalPages'];
        //获取总数量
        var totalCount = params['totalCount'];

        $("#paginationPage").empty();
        if (totalPages <= 1) {
            $("#paginationPrevious").addClass("disabled");
            $("#paginationPage").html("<li class=\"active\"><a href=\"javascript:void(0);\">1</a></li>")
            $("#paginationNext").addClass("disabled");
        }else {
            pagination.loadPage(pageNum, totalPages);
        }

        $("#paginationTotalCount").text("共" + totalCount + "条记录 " + totalPages + "页");
    },

    funcUrlDel : function (name) {
        var loca = window.location;
        var baseUrl = loca.protocol + "//" + loca.host + loca.pathname + "?";
        var query = loca.search.substr(1);
        if (query.indexOf(name)>-1) {
            var obj = {}
            var arr = query.split("&");
            for (var i = 0; i < arr.length; i++) {
                arr[i] = arr[i].split("=");
                obj[arr[i][0]] = arr[i][1];
            }
            delete obj[name];
            var url = baseUrl + JSON.stringify(obj).replace(/["\{\}]/g,"").replace(/\:/g,"=").replace(/\,/g,"&");
            if (arr.length >= 2) url = url + "&";
            return url
        } else {
            if (query == undefined || query == "") return baseUrl + query;
            return baseUrl + query + "&";
        }
    },

    loadPage : function (pageNum, totalPages) {
        var showPage = 5; //显示的页数
        var startPage = 1;

        if(pageNum >= 4){
            startPage = pageNum - 2;
        }

        var endPage = startPage + showPage;
        if (endPage > totalPages)endPage =  totalPages + 1;

        var url = pagination.funcUrlDel("pageNum");
        for (var i = startPage; i < endPage; i++) {
            if (pageNum == i) {
                $("#paginationPage").append("<li class=\"active\"><a href=\"javascript:void(0);\">"+i+"</a></li>")
            }else {
                $("#paginationPage").append("<li><a href=\""+url+"pageNum="+i+"\">"+i+"</a></li>")
            }
        }

        $("#paginationPrevious").find("a").attr("href",""+url+"pageNum=" + (pageNum-1));
        $("#paginationNext").find("a").attr("href",""+url+"pageNum=" + (pageNum+1));

        if (pageNum == 1) {
            $("#paginationPrevious").addClass("disabled");
            $("#paginationPrevious").find("a").attr("href","javascript:void(0);");
        }
        if (pageNum == totalPages) {
            $("#paginationNext").addClass("disabled");
            $("#paginationNext").find("a").attr("href","javascript:void(0);");
        }
    }

}
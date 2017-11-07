var adminLayout  = {
    
    init : function () {
        adminLayout.menuClick();
    },
    
    menuClick : function () {
        var path = window.location.pathname;
        if (path == "/admin"){
            $("#index").addClass("active");
            return;
        }

        if (path.indexOf("blogType") != -1) {
            $("#blogType").addClass("active");
            return;
        }

        if (path.indexOf("blogTag") != -1) {
            $("#blogTag").addClass("active");
            return;
        }

        if (path.indexOf("blog") != -1) {
            $("#blogList").addClass("active");
            return;
        }

    }
    
}
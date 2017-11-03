var addBlog = {

    init : function () {
        addBlog.editormd();
        addBlog.showDiv();
        addBlog.hideDiv();
        addBlog.tagListClick();
        addBlog.addBlog();
    },

    URL : {
        addBlog : function () {
            return '/admin/addBlog';
        },
        blog : function () {
            return '/blog/info/';
        },
        upload : function () {
            return '/upload?_csrf='+$("#token").val();
        }
    },

    editormd : function () {
        var testEditor = editormd("test-editormd", {
            width   : "90%",
            height  : 850,
            syncScrolling : "single",
            path    : "/editormd/lib/",
            /* previewTheme : "dark",
            theme : "dark",
            editorTheme : "pastel-on-dark", */
            codeFold : true,
            imageUpload : true,
            imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
            imageUploadURL : addBlog.URL.upload(),
            htmlDecode : "style,script,iframe|on*",            // 开启 HTML 标签解析，为了安全性，默认不开启
            tocm : true,         		  // Using [TOCM]
            taskList : true,
            tex : true,                   // 开启科学公式TeX语言支持，默认关闭
            flowChart : true,             // 开启流程图支持，默认关闭
            sequenceDiagram : true        // 开启时序/序列图支持，默认关闭,
            //saveHTMLToTextarea : true		//开启之后有些特殊的内容不能被转换成html
        });
    },

    showDiv : function () {
        $("#showDiv").click(function(){
            document.getElementById("bg").style.display ="block";
            document.getElementById("show").style.display ="block";
        });
    },

    hideDiv : function () {
        $("#hideDiv").click(function(){
            document.getElementById("bg").style.display ='none';
            document.getElementById("show").style.display ='none';
        });
    },

    tagListClick : function () {
        $(".tagList.show").click(function(){
            $("#tagList").append("<span class='tagList add' value='"+$(this).attr("value")+"'>"+$(this).text()+"</span>");
            $(".tagList.add").click(function(){
                $(this).remove();
            });
        });
    },

    addBlog : function () {
        $("#addBlogButton").click(function(){
            if($("#blogName").val() == "" || $("#introduction").val() == "" || $("#blogType").val() == "0"){
                return;
            }

            var blogTag = "";
            $(".tagList.add").each(function(){
                blogTag += $(this).attr("value") + ",";
            });

            if(blogTag != ""){
                $("#blogTag").val(blogTag);
            }

            $.ajax({
                type: "POST",
                url: addBlog.URL.addBlog(),
                data: $("#addBlog").serialize(),
                success:function(data){
                    if(data.result==true){
                        layer.msg("success", function(){
                            window.location.href=addBlog.URL.blog() + data.data;
                        });
                    }else{
                        layer.msg(data.error, function(){});
                    }
                }
            });
        });
    }

}
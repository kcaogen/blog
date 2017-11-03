package com.caogen.blog.controller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;

@Controller
public class UploadController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${web.upload-path}")
    private String path;

    @PostMapping(value = "/upload", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public HashMap<String,Object> uploadfile(@RequestParam(value = "editormd-image-file", required = false) MultipartFile attach,
                                 HttpServletResponse response){
        HashMap <String,Object> map = new HashMap <>();
        try {

            String blogImgPath = "/blogImg/";
            path = path + blogImgPath;

            /**
             * 文件路径不存在则需要创建文件路径
             */
            File filePath=new File(path);
            if(!filePath.exists()){
                filePath.mkdirs();
            }

            //最终文件名
            String fileName = System.currentTimeMillis()+"_"+attach.getOriginalFilename();
            File realFile=new File(path+File.separator+fileName);
            FileUtils.copyInputStreamToFile(attach.getInputStream(), realFile);

            //解决IFrame拒绝的问题
            response.setHeader("X-Frame-Options", "SAMEORIGIN");

            map.put("success", 1);
            map.put("message", "上传成功");
            map.put("url", blogImgPath+fileName);
        } catch (Exception e) {
            map.put("success", 0);
            map.put("message", "上传失败");
            logger.error("uploadfile:" + e);
            e.printStackTrace();
        }finally {
            return map;
        }
    }

}

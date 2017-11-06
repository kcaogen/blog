package com.caogen.blog.controller;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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

    @Value("${web.blogImg-path}")
    private String blogImgPath;

    @Value("${qiniuyun.AK}")
    private String accessKey;

    @Value("${qiniuyun.SK}")
    private String secretKey;

    @Value("${qiniuyun.bucket}")
    private String bucket;

    @Value("${qiniuyun.url}")
    private String url;

    @PostMapping(value = "/upload", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public HashMap<String,Object> uploadfile(@RequestParam(value = "editormd-image-file", required = false) MultipartFile attach,
                                 HttpServletResponse response){
        HashMap <String,Object> map = new HashMap <>();
        try {

            /**
             * 文件路径不存在则需要创建文件路径
             */
            File filePath=new File(path);
            if(!filePath.exists()){
                filePath.mkdirs();
            }

            //最终文件名
            String originalFilename = attach.getOriginalFilename();
            String fileNameSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = "kcaogen_" + System.currentTimeMillis() + fileNameSuffix.toLowerCase();
            File realFile=new File(path+File.separator+fileName);
            FileUtils.copyInputStreamToFile(attach.getInputStream(), realFile);

            //上传到本地之后再上传到七牛云加速
            String url = uploadByQiNiu(path, fileName);

            //解决IFrame拒绝的问题
            response.setHeader("X-Frame-Options", "SAMEORIGIN");

            map.put("success", 1);
            map.put("message", "上传成功");
            //map.put("url", blogImgPath+fileName);
            map.put("url", url);
        } catch (Exception e) {
            map.put("success", 0);
            map.put("message", "上传失败");
            logger.error("uploadfile:" + e);
            e.printStackTrace();
        }finally {
            return map;
        }
    }

    public String uploadByQiNiu(String path, String fileName) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = path+fileName;
        String systemName = System.getProperty("os.name");
        //判断当前环境，如果是Windows
        if(!StringUtils.isEmpty(systemName) && systemName.indexOf("Windows") !=-1){
            localFilePath = "D:" + localFilePath;
        }
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }finally {
            return url + key;
        }
    }

}

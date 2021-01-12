package com.love.outofmemory.controller.common;


import com.love.outofmemory.Utills.CodeUtil;
import com.sun.prism.Image;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Objects;
import java.util.UUID;


/**
 * @author huang
 */
@RequestMapping("/common")
@Controller
public class CommonController {

//    本机测试
    private String imgPathP = "/home/huang/outofmemory/src/main/resources/static/common/profile_image/";
    private String imgPathS ="/home/huang/outofmemory/src/main/resources/static/common/blog_image/";
    private String imgPathC ="/home/huang/outofmemory/src/main/resources/static/common/image_cache/";

    /*private String imgPathP = "/home/admin/outofmemory/resources/static/common/profile_image/";
    private String imgPathS ="/home/admin/outofmemory/resources/static/common/blog_image/";
    private String imgPathC ="/home/admin/outofmemory/resources/static/common/image_cache/";*/

    //参数：图片名称 响应图片给客户端
    @RequestMapping("/getImage")
    @ResponseBody
    public void getImage(String image, HttpServletResponse response) throws Exception {
        //1.判断图片是否存在
        File file = new File(imgPathP + image);
        if (file.exists()) {
            //如果图片存在的，构建一个字节输入流，读取图片的数据
            FileInputStream is = new FileInputStream(file);
            //构建字节响应输出流
            ServletOutputStream os = response.getOutputStream();
            //对接两个流，响应图片数据
            IOUtils.copy(is, os);
        } else {
            //图片不存在
            response.setStatus(404);
        }
    }

    //上传图片到目录实际上文章中存的是图片的url地址，图片与文章就是这样关联的
    @RequestMapping(value = "/article/upload",method = RequestMethod.POST,produces = {"text/plain;charset=utf-8"})
    @ResponseBody
    public String blogImage(@RequestParam(value = "editormd-image-file") MultipartFile file,
                            String guid,
                            HttpServletResponse response,
                            HttpServletRequest request) throws JSONException {

        //这里要返回一个json对象否则前台会出错
        JSONObject re=new JSONObject();
        //若文件不为空
         String unique_Profile_name=null;
        if(!file.isEmpty()){

            try {

                //获取图像后缀名
                String[] houzui= Objects.requireNonNull(file.getOriginalFilename()).split("[.]");
                //生成图像唯一名
                unique_Profile_name= UUID.randomUUID().toString().replace("-","")+"."+houzui[1];
                //转移图片位置
               file.transferTo(new File(imgPathS+unique_Profile_name ));

               //设置jsons数据返回成功
                re.put("success",1);
                re.put("message","上传成功");
                //作为img的src可以为资源路径访问
                re.put("url","/common/blog_image/"+unique_Profile_name);
            } catch (IOException e) {
                re.put("success",0);
                re.put("message","上传异常");
                e.printStackTrace();

            }

        }
        else{

            re.put("success",0);
            re.put("message","上传失败");

        }
        return re.toString();
    }

    @RequestMapping("/viewImage")
    @ResponseBody
    public void viewBlogImage(String image,HttpServletResponse response) throws Exception {
        //1.判断图片是否存在
        File file = new File(imgPathP + image);
        if (file.exists()) {
            //如果图片存在的，构建一个字节输入流，读取图片的数据
            FileInputStream is = new FileInputStream(file);
            //构建字节响应输出流
            ServletOutputStream os = response.getOutputStream();
            //对接两个流，响应图片数据
            IOUtils.copy(is, os);
        } else {
            //图片不存在
            response.setStatus(404);
        }

    }

    @RequestMapping("/code")
    @ResponseBody
    public void getCodeImage(HttpServletResponse response, HttpSession session) throws Exception {
        String text = CodeUtil.generateCodeAndPic(response.getOutputStream());
        session.setAttribute("code", text);

    }


}

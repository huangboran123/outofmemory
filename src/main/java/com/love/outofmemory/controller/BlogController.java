package com.love.outofmemory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.love.outofmemory.Utills.DateUtil;
import com.love.outofmemory.Utills.MarkDownUtil;
import com.love.outofmemory.Utills.RedisUtil;
import com.love.outofmemory.annotation.LogInterceptor;
import com.love.outofmemory.controller.commonbean.AjaxResults;
import com.love.outofmemory.domain.*;
import com.love.outofmemory.domain.view.BlogPageUser;
import com.love.outofmemory.domain.view.Classify;
import com.love.outofmemory.service.IBlogService;
import com.love.outofmemory.service.IBlogTagService;
import com.love.outofmemory.service.IClassificationService;
import com.love.outofmemory.service.ICommentService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * @author huang
 */
@Controller
public class BlogController {

    @Autowired
    private IBlogService iBlogService;
    @Autowired
    private IClassificationService iClassificationService;
    @Autowired
    private IBlogTagService iBlogTagService;
    @Autowired
    private ICommentService iCommentService;
    @Autowired
    private RedisUtil redisUtil;

    //新建博客页面
    @RequestMapping(value = "/newBlogPage", method = RequestMethod.GET)
    public String newBlogPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        //防止非登录状态请求
        if (user != null) {
            //查询用户博客分类信息，以及相关的博客标签信息
            List<Classification> classificationList = iClassificationService.getClassificationByUserID(user.getId());
            model.addAttribute("Classifications", classificationList);

            List<BlogTag> blogTagsList = iBlogTagService.getBlogTags();
            model.addAttribute("blogTagsList", blogTagsList);

            return "front/blog/new_blog";
        } else {
            return "redirect:/";
        }

    }

    //新建博客提交
    @RequestMapping(value = "/newBlog", method = RequestMethod.POST, produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    @LogInterceptor
    public String newBlog(String title, String content, String tag, String classify, Integer isoriginal, HttpSession session) {
        //获取当前登录用户信息
        User user = (User) session.getAttribute("user");
        Integer userId = user.getId();

        //新建一个blog实体
        Blog blog = new Blog();
        blog.setClassification(iClassificationService.getIdByname(userId, classify));
        blog.setContent(content);
        blog.setTitle(title);
        blog.setGood_count(0);
        blog.setPublish_time(DateUtil.getNowDateLong());
        blog.setViews(0);
        blog.setTag(iBlogTagService.getTagByName(tag));
        blog.setIsoriginal(isoriginal);
        blog.setComments(0);
        blog.setUser(user);
        int i = iBlogService.addBlog(blog);
        if (i == 1) {

            return "发布成功";
        } else {
            return "发布失败";
        }

    }

    /* 我的博客页面*/
    @GetMapping(value = "/myblog", produces = {"text/plain;charset=UTF-8"})
    public String myblogPage(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        //防止非登录状态请求
        if (user != null) {
            Integer pagesize=6;
            Integer page=1;
            //个人博客分类信息
            List<Classify> classifyList = iBlogService.getClassifyBlogCount(user.getId());
            model.addAttribute("classifyList", classifyList);
            //查询所有博客取前n条按发布时间
            List<Blog> allblogList = iBlogService.getAllmyPagingblogs(null,page,pagesize, user.getId(),0,null);

            Integer totalcount=iBlogService.getTotalcountbyclassanduser(null, user.getId(),null);
            //将查询到的Stringmarkdown格式转换为html格式
            for (Blog b : allblogList) {
                String content = MarkDownUtil.markdownToHtml(b.getContent());
                b.setContent(content);
            }
            model.addAttribute("allblogList", allblogList);
            model.addAttribute("totalcount",totalcount%pagesize>0 ? (totalcount/pagesize)+1:totalcount/pagesize);
            return "front/blog/my_blog";
        }
        return "redirect:front/loginPage";
    }

    /* 查询博客分类*/
    @PostMapping(value = "/myblog/classify")
    @ResponseBody
    @LogInterceptor
    public AjaxResults queryClassify(HttpSession session, Integer classificationId,Integer page,Integer pageSize,Integer sortmethod) {

        User user = (User) session.getAttribute("user");
        List<Blog> blogList = iBlogService.getAllmyPagingblogs(classificationId,page,pageSize, user.getId(), sortmethod,null);
        Integer totalcount=iBlogService.getTotalcountbyclassanduser(classificationId,user.getId(),null);
        AjaxResults results=new AjaxResults();
        results.setBlogresults(blogList);
        results.setTotalcount(totalcount%pageSize>0 ? (totalcount/pageSize)+1:totalcount/pageSize);
        return results;

    }

    /* 博客删除确认*/
    @RequestMapping(value = "/myblog/del", produces = {"text/plain;charset=UTF-8"}, method = RequestMethod.POST)
    @ResponseBody
    @LogInterceptor
    public String deleteBlog(Integer delblogId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (delblogId != null) {
            int i = iBlogService.removeblogByIid(delblogId);
            if (i == 0) {
                return "删除失败";
            } else {
                return "删除成功";
            }
        } else {

            return "删除失败";
        }


    }

    /*修改博客页面*/
    @GetMapping(value = "/myblog/modPage")
    public String blogModify(@RequestParam("blogId") Integer blogId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        //防止非登录状态请求
        if (user != null) {
            Blog blog = iBlogService.getblogById(blogId);
            if (blog == null) {
                return "redirect:/myblog";
            }
            /*防止个人用户修改其它用户数据，理论上不可能发生*/
            if (!user.getId().equals(blog.getUser().getId())) {
                return "redirect:/";
            } else {
                //查询用户博客分类信息，以及相关的博客标签信息
                List<Classification> classificationList = iClassificationService.getClassificationByUserID(user.getId());
                model.addAttribute("Classifications", classificationList);

                List<BlogTag> blogTagsList = iBlogTagService.getBlogTags();
                model.addAttribute("blogTagsList", blogTagsList);

                model.addAttribute("blog", blog);
                return "front/blog/modify_blog";
            }
        }
        return "redirect:/myblog";


    }

    /*修改博客提交*/
    @PostMapping(value = "/myblog/modblog", produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    @LogInterceptor
    public String modBlog(Integer userId, Integer blogId, String title, String content, String tag, String classify, Integer isoriginal) {

        int i = iBlogService.updateBlogByid(userId, blogId, title, content, tag, classify, isoriginal);

        if (i == 1) {
            return "博客修改成功";
        } else {
            return "博客修改失败";
        }
    }

    /*浏览个人博客*/
    @GetMapping(value = "/myblog/viewblog")
    public String viewblogPage(Model model,
                               @RequestParam("blogId") Integer blogId,
                               @RequestParam("userId") Integer userId,
                               HttpSession session) {


            Integer id=null;
            /*当前浏览博客信息*/
            Blog blog = iBlogService.getblogById(blogId);
            /*   博客作者统计信息*/
            BlogPageUser blogPageUser = iBlogService.getUserMoreById(userId, id);
            /*   作者博客分类信息*/
            List<Classify> classlistcount = iBlogService.getClassifyBlogCount(userId);
            /*推荐博客*/
            List<Blog> recomandblogs = iBlogService.getSideRecommandblogs();

            List<Comment> comments = iCommentService.getAllCommentsPageByBlogId(blogId, 0, 5);

            /*博客，用户信息查询*/
            model.addAttribute("blog", blog);
            model.addAttribute("blogPageuser", blogPageUser);
            model.addAttribute("classlistcount", classlistcount);
            model.addAttribute("recomandblogs", recomandblogs);
            /*当前博客评论查询*/
            model.addAttribute("comments", comments);

            return "front/blog/show_blog";


    }

    /*浏览他人博客,不进行登录判断*/
    @GetMapping(value = "/blog/viewblog")
    public String viewotherblogPage(Model model,
                                    @RequestParam("blogId") Integer blogId,
                                    @RequestParam("userId") Integer userId,
                                    HttpSession session) {
        String blogID = blogId.toString();
        Integer id=null;
        Blog blog = new Blog();
        /*   判断redis中是否已经存在博客id的view*/
        if (redisUtil.HashGet("Views", blogID) != null) {
            blog = iBlogService.getblogByIdNoviews(blogId);
            blog.setViews((Integer) redisUtil.HashGet("Views", blogID));

        } else {
            /*当前浏览博客信息*/
            blog = iBlogService.getblogById(blogId);

        }

        User user=(User)session.getAttribute("user");
        if(!Objects.isNull(user)){
            id=user.getId();
        }


        /*   博客作者统计信息*/
        BlogPageUser blogPageUser = iBlogService.getUserMoreById(userId,id);
        /*   作者博客分类信息*/
        List<Classify> classlistcount = iBlogService.getClassifyBlogCount(userId);
        /*推荐博客*/
        List<Blog> recomandblogs = iBlogService.getSideRecommandblogs();

        /*评论*/
        List<Comment> comments = iCommentService.getAllCommentsPageByBlogId(blogId, 0, 5);



        /*博客，用户信息查询*/
        model.addAttribute("blog", blog);
        model.addAttribute("blogPageuser", blogPageUser);
        model.addAttribute("classlistcount", classlistcount);
        model.addAttribute("recomandblogs", recomandblogs);

        /*当前博客评论查询*/
        model.addAttribute("comments", comments);

        /*查询浏览量存入redis数据库,若redis中存在则加一*/
        /*评论量存blogid和views*/

        if (redisUtil.HashGet("Views", blogID) == null) {
            redisUtil.HashPut("Views", blogID, blog.getViews());
        } else {
            redisUtil.HashPut("Views", blogID, blog.getViews() + 1);
        }


        /*与浏览个人博客返回一样的视图，使用thymeleaf进行条件判断*/
        return "front/blog/show_blog";
    }

    /*点赞*/
    @PostMapping(value = "/likes", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @LogInterceptor
    public Boolean likes(Integer blogId, HttpSession session) {


            int i = iBlogService.likesblogbyid(blogId);
            return i == 1;

    }

   /* 首页博客默认查询分页*/
    @PostMapping(value = "/index/changeblogpage")
    @ResponseBody
    public List<Blog> changeblogpage(Integer page,Integer pageSize,String tag) throws JSONException {
        Integer ptag;
        if(!Objects.isNull(page)&&!Objects.isNull(pageSize)){
            if("all".equals(tag)){
                ptag=null;
            }else
            {
                ptag=Integer.valueOf(tag);
            }
            return iBlogService.getIndexRecommandblogs(page,pageSize,ptag);
        }
        else{
            return null;
        }
    }
    /* 首页博客分类查询分页*/
    @PostMapping(value = "/index/tagblogquery")
    @ResponseBody
    public AjaxResults tagblogquery(Integer page,Integer pageSize,Integer tag) throws JSONException {
            List<Blog> blogresults=iBlogService.getIndexRecommandblogs(page,pageSize,tag);
            Integer totalcount=iBlogService.getTotalcountbyclassanduser(null,null,tag);
            AjaxResults results=new AjaxResults();
            results.setBlogresults(blogresults);
            results.setTotalcount(totalcount%pageSize>0 ? (totalcount/pageSize)+1:totalcount/pageSize);
            return results;
    }

   /* 我的博客页面分页展示*/
   @PostMapping(value = "/myblog/changeblogpage")
   @ResponseBody
   @LogInterceptor
   public List<Blog> changemyblogpage(HttpSession session,Integer page,Integer pageSize,Integer sortmethod,String classification) throws JSONException {
       if(!Objects.isNull(page)&&!Objects.isNull(pageSize)){
           User user = (User) session.getAttribute("user");
           if("all".equals(classification)){
               return iBlogService.getAllmyPagingblogs(null,page,pageSize,user.getId(), sortmethod,null);
           }
          else{
               return iBlogService.getAllmyPagingblogs(Integer.valueOf(classification),page,pageSize,user.getId(), sortmethod,null);
           }
       }
       else{

           return null;

       }

   }


}

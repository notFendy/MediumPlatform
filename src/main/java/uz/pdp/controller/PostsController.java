package uz.pdp.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.dao.ArticleDao;
import uz.pdp.dao.CommentDao;
import uz.pdp.dao.UserDao;
import uz.pdp.dto.CommentDto;
import uz.pdp.dto.PostDto;
import uz.pdp.model.Article;
import uz.pdp.model.Comment;
import uz.pdp.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Controller
@Transactional
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {
    private final ArticleDao articleDao;
    private final CommentDao commentDao;
    private final UserDao userDao;

    @GetMapping
    public ModelAndView showMedia(ModelAndView modelAndView) {
        modelAndView.addObject("posts", articleDao.getAllPosts());
        modelAndView.setViewName("view-posts");
        return modelAndView;
    }


    @GetMapping("/{id}")
    public ModelAndView get(ModelAndView modelAndView,
                            @PathVariable("id") Long id) {

        Article article = articleDao.getById(id);

        List<Comment> commentaries = commentDao.getCommentsByPostId(id);

        modelAndView.addObject("get", article);
        modelAndView.addObject("commentaries", commentaries);
        modelAndView.setViewName("showpostinfo");
        return modelAndView;

    }

    @PostMapping("/{id}")
    public String comment(@PathVariable("id") Long postId,
                                @ModelAttribute CommentDto commentDto
//                                ModelAndView modelAndView
    ) {
        Article post = articleDao.getById(postId);

        User user = userDao.getUserByUsername(commentDto.authName());

        Comment comment = Comment.builder()
                .commentText(commentDto.commentaries())
                .commentedTime(LocalDateTime.now())
                .post(post)
                .user(user)
                .build();

        commentDao.save(comment);

//        session.getCurrentSession().persist(comment);
//        modelAndView.addObject("post", post);
//        modelAndView.addObject("comment", comment);
//        modelAndView.setViewName("showpostinfo");
        return "showpostinfo";
    }


//    @GetMapping("/{id}")
//    public ModelAndView comment(@PathVariable("id") Long postId,
//                                 @RequestParam("authName") String username,
//                                 @ModelAttribute CommentDto commentDto,
//                                 ModelAndView modelAndView){
//
//        Article post = articleDao.getById(postId);
//
//        Comment comment = Comment.builder()
//                .commentText(commentDto.commentaries())
//                .commentedTime(LocalDateTime.now())
//                .user(userDao.getUserByUsername(commentDto.authName()))
//                .post(articleDao.getById(postId))
//                .build();
//
//        commentDao.save(comment);
//
//        List<Comment> commentaries = commentDao.getCommentsByPostId(postId);
//
//        modelAndView.addObject("posts", post);
//        modelAndView.addObject("comment", comment);
//        modelAndView.setViewName("showpostinfo");
//        return modelAndView;
//    }
//

    @GetMapping("/createarticle")
    public ModelAndView createArticle(
            @ModelAttribute PostDto postDto,
            ModelAndView modelAndView
    ) {


        Article post = Article.builder()
                .title(postDto.title())
                .content(postDto.content())
                .user(userDao.getUserByUsername(postDto.auth_name()))
                .build();

        articleDao.save(post);

        modelAndView.addObject("posts", post);
        modelAndView.setViewName("post");


        return modelAndView;
    }


    @GetMapping("/myposts")
    public ModelAndView myPosts(@AuthenticationPrincipal User currentUser,
                                ModelAndView modelAndView) {
        if (currentUser != null) {
            Article currentUserPosts = articleDao.findByUser(currentUser);
            modelAndView.addObject("posts", currentUserPosts);
        } else {

            modelAndView.addObject("posts", new ArrayList<Article>());
        }
        modelAndView.setViewName("editpost");

        return modelAndView;
    }

}
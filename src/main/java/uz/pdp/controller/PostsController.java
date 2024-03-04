package uz.pdp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.dao.ArticleDao;
import uz.pdp.dto.PostDto;
import uz.pdp.model.Article;
import uz.pdp.model.User;
import java.util.ArrayList;


@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {

    private final ArticleDao articleDao;

    @GetMapping
    public ModelAndView showMedia(ModelAndView modelAndView){
        modelAndView.addObject("posts", articleDao.getAllPosts());
        modelAndView.setViewName("view-posts");
        return modelAndView;
    }

    @GetMapping("/{postId}")
    public ModelAndView showPost(@PathVariable Long postId, ModelAndView modelAndView){
        Article post = articleDao.getById(postId);
        modelAndView.addObject("post", post);
        modelAndView.setViewName("view-posts");
        return modelAndView;
    }
    @GetMapping("/createarticle")
    public ModelAndView createArticle(
            @ModelAttribute PostDto postDto,
            ModelAndView modelAndView
            ){


            Article post = Article.builder()
                    .title(postDto.title())
                    .content(postDto.content())
                    .build();

            articleDao.save(post, postDto.auth_name());

            modelAndView.addObject("posts", post);
            modelAndView.setViewName("post");


        return modelAndView;
    }


    @GetMapping("/myposts")
    public ModelAndView myPosts(@AuthenticationPrincipal User currentUser,
                                ModelAndView modelAndView){
        if (currentUser != null) {
            // If currentUser is not null, proceed with retrieving the posts
            Article currentUserPosts = articleDao.findByUser(currentUser);

            // Add currentUserPosts to the modelAndView
            modelAndView.addObject("posts", currentUserPosts);
        } else {
            // Handle the case when currentUser is null
            modelAndView.addObject("posts", new ArrayList<Article>());
        }

        // Set the view name
        modelAndView.setViewName("editpost");

        // Return the populated modelAndView
        return modelAndView;
    }

}
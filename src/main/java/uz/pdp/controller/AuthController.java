package uz.pdp.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.extras.springsecurity6.util.SpringSecurityContextUtils;
import uz.pdp.dao.UserDao;
import uz.pdp.model.User;
import uz.pdp.dto.UserLoginDto;
import uz.pdp.dto.UserSignUpDto;
import uz.pdp.service.UserService;


@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserDao userDao;
    @GetMapping("/login")
    public String auth(){
        return "login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/logout")
    public String logout(){
        return "logout";
    }

    @PostMapping("/signup")
    public ModelAndView SignUp(
             @ModelAttribute UserSignUpDto signupDto,
            ModelAndView modelAndView,
            BindingResult bindingResult
    ){

        if (isEmailExists(signupDto.email())){
            modelAndView.setViewName("error");
            return modelAndView;
        }

        if (bindingResult.hasErrors()){
            modelAndView.setViewName("signup");
            return modelAndView;
        }
        userService.saveUser(signupDto);


        modelAndView.addObject("user", userDao.getUserByUsername(signupDto.username()));
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PostMapping ("/login")
    public ModelAndView login(@ModelAttribute UserLoginDto userLoginDto,
                              ModelAndView modelAndView
    )  {

        User user = getUserByUsernameAndPassword(userLoginDto.username(), userLoginDto.password());

        if (user != null) {
            modelAndView.setViewName("view-posts");
        } else {
            modelAndView.setViewName("login-error");
        }

        return modelAndView;
    }

    

    private boolean isUsernameExists(String username){
        try {
            User existingUser = userDao.getUserByUsername(username);
            return existingUser != null;
        } catch (Exception e) {
            return false;
        }
    }

    private User getUserByUsernameAndPassword(String username, String password) {
        try {
            User user = userDao.getUserByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            } else {
                return null;
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private boolean isEmailExists(String email) {
        try {
            User existingUser = userDao.getByEmail(email);
            return existingUser != null;
        } catch (Exception e) {
            return false;
        }
    }
}

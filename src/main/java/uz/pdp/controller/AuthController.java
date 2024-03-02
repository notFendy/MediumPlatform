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
import uz.pdp.dao.UserDao;
import uz.pdp.domain.User;
import uz.pdp.dto.UserLoginDto;
import uz.pdp.dto.UserSignUpDto;


@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDao userDao;
    @GetMapping("/login")
    public String auth(){
        return "login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
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
            modelAndView.setViewName("auth");
            return modelAndView;
        }

        final var user = User.builder()
                .username(signupDto.username())
                .email(signupDto.email())
                .password(signupDto.password())
                .gender(signupDto.gender())
                .build();

        userDao.save(user);

        modelAndView.addObject("user", user);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PostMapping ("/login")
    public ModelAndView login(@ModelAttribute UserLoginDto userLoginDto,
                              ModelAndView modelAndView
    )  {

        User user = getUserByEmailAndPassword(userLoginDto.email(), userLoginDto.password());

        if (user != null) {
            modelAndView.setViewName("media");
        } else {
            modelAndView.setViewName("login-error");
        }

        return modelAndView;
    }


    private User getUserByEmailAndPassword(String email, String password) {
        try {
            User user = userDao.getByEmail(email);
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

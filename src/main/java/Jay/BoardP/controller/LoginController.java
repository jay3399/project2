package Jay.BoardP.controller;

import Jay.BoardP.controller.form.LoginForm;
import Jay.BoardP.service.LoginService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {


    private final LoginService loginService;


    @GetMapping("/login")
    public String signIn(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }



    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";

    }



    //    @PostMapping("/login")
//    public String signIn(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult,
//        HttpServletRequest request) {
//
//        if (bindingResult.hasErrors()) {
//            return "login/loginForm";
//        }
//
//        MemberDto login = loginService.login(loginForm);
//
//        if (login == null) {
//            bindingResult.reject("loginFail" , "아이디 또는 비밀번호가 일치하지않습니다");
//            return "login/loginFail";
//        }
//
//        HttpSession session = request.getSession();
//        //세션이있을시 , 해당세션을 반환 // 세션이 없을시 신규세션 생성
//
//        session.setAttribute(SessionConst.LOGIN_MEMBER, login);
//
//        return "redirect:/";
//
//    }





}

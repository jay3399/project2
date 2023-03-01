package Jay.BoardP.controller;


import Jay.BoardP.controller.dto.MyBoardListDto;
import Jay.BoardP.controller.dto.MyCommentDto;
import Jay.BoardP.controller.dto.User;
import Jay.BoardP.controller.form.PasswordForm;
import Jay.BoardP.domain.Member;
import Jay.BoardP.service.BoardService;
import Jay.BoardP.service.CommentService;
import Jay.BoardP.service.memberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController {

    private final memberService memberservice;
    private final BoardService boardService;

    private final CommentService commentService;


    @GetMapping
    public String home() {
        return "mypage/myHome";
    }

    @GetMapping("/info")
    public String info(@AuthenticationPrincipal User user, Model model) {
        Long id = user.getId();
        Member member = memberservice.findOne(id);
        model.addAttribute("member", member);

        return "mypage/info";
    }


    @GetMapping("/myBoardList")
    public String myBoardList(@AuthenticationPrincipal User user, Model model) {
        List<MyBoardListDto> myBoardList = boardService.getMyBoardList(user.getId());

        model.addAttribute("myBoardList", myBoardList);

        return "mypage/myBoardList";
    }


    @GetMapping("/myComments")
    private String myComments(@AuthenticationPrincipal User user, Model model) {

        List<MyCommentDto> boardComments = commentService.getBoardComments(user.getId());

        model.addAttribute("boardComments", boardComments);

        return "mypage/myCommentList";

    }


    @GetMapping("/editPassword")
    private String editPasswordForm(@ModelAttribute PasswordForm passwordForm) {
        return "mypage/editPassword";
    }

    @PostMapping("/editPassword")
    private String editPassword(@Validated @ModelAttribute PasswordForm passwordForm,
        BindingResult bindingResult, @AuthenticationPrincipal User user
    ) {

        if (!passwordForm.getPassword().equals(user.getPassword())) {
            bindingResult.reject("checkPassword", "기존비밀번호가 일치하지않습니다");
        }

        if (passwordForm.getEditPassword().equals(user.getPassword())) {
            bindingResult.reject("previousPassword", "이전 비밀번호와 같을수 없습니다..");
        }

        if (!passwordForm.getEditPassword().equals(passwordForm.getCheckEditPassword())) {
            bindingResult.reject("checkEditPassword", "변경된 비밀번호가 일치하지않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/editPassword";
        }

        memberservice.editPassword(user.getId(), passwordForm.getEditPassword());
        return "redirect:/mypage/myHome";
    }

    @GetMapping("/deleteForm")
    private String deleteMemberForm() {
        return "/deleteForm";
    }

    @PostMapping("/deleteForm")
    private String deleteMember(String password, @AuthenticationPrincipal User user , RedirectAttributes redirectAttributes
    ) {

        if (!password.equals(user.getPassword())) {
            redirectAttributes.addAttribute("incorrectPassword", "비밀번호가 일치하지 않습니디");
            return "redirect:/mypage/deleteForm";
        }

        memberservice.deleteMember(user.getMember());

        return "redirect:/";
    }


}

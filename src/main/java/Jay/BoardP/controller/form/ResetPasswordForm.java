package Jay.BoardP.controller.form;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordForm {

    @NotBlank(message = "패스워드를 입력해주세요")
    private String editPassword;

    @NotBlank(message = "패스워드를 입력해주세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String checkEditPassword;

    @NotBlank(message = "인증번호를 입력해주세요.")
    private String code;

}

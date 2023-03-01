package Jay.BoardP.controller.form;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordForm {


    @NotBlank(message = "패스워드를 입력해주세요")
    private String editPassword;

    @NotBlank(message = "패스워드를 입력해주세요")
    private String checkEditPassword;

    @NotBlank(message = "인증번호를 입력해주세요.")
    private String code;






}

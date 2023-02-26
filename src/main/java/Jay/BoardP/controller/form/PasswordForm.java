package Jay.BoardP.controller.form;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordForm {

    @NotBlank(message = "패스워드를 입력해주세요")
    private String password;

    @NotBlank(message = "패스워드를 입력해주세요")
    private String editPassword;

    @NotBlank(message = "패스워드를 입력해주세요")
    private String checkEditPassword;

}

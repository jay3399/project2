package Jay.BoardP.controller.dto;


import Jay.BoardP.domain.Address;
import Jay.BoardP.domain.Member;
import java.sql.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberFormDto {

    private Long id;

    @NotBlank(message = "아이디를 입력해주세요")
    @Pattern(regexp = "^(?!\\d+$)\\w{4,8}$", message = "아이디는 특수문자를제외한 영문,또는 영문+숫자 4~8자리여야 합니다.")
    //특수문자 , 전부 숫자는 불가능 // 알파벳 + 숫자 , 전부알파벳 가능.
//    @Size(min = 2, max = 10, message = "아이디는 2자이상 8자이하여야 합니다.")
    private String userId;

//    @NotBlank(message = "성함을 입력해주세요")
//    @Pattern(regexp = "^[가-힣]{2,4}$", message = "올바른 형식을 입력해주세요")
//    private String name;
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~6자리여야 합니다.")
    private String nickname;
    @NotBlank(message = "비밀번호를 입력해주세요")
//    @Size(min = 8, max = 15, message = "비밀번호는 8자이상 15자이하만 가능합니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String checkPassword;

    @Email(message = "올바른 이메일형식을 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "인증번호를 입력해주세요")
    private String code;

    @NotBlank
//    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String phone;

    private Date birthDate;

//    private Address address;

//    private String city;
//    private String street;
//    private String zipCode;


    public void setMemberForm(Member member) {
        this.userId = member.getUserId();
        this.nickname = member.getNickname();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.phone = member.getPhoneNumber();
        this.birthDate = member.getBirthDate();
//        this.address = member.getAddress();
    }


    public static MemberFormDto createMemberFromEntity(Member member) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setMemberForm(member);
        return memberFormDto;
    }

    public Member createMember(String password) {
        return Member.builder().phoneNumber(phone).userId(userId).email(email)
            .birthDate(birthDate).nickname(nickname).password(password).build();
    }

//    public static Member createMember(MemberFormDto formDto) {
//        return Member.builder().userId(formDto.getUserId())
//            .nickname(formDto.getNickname())
//            .password(formDto.password)
//            .email(formDto.getEmail())
//            .phoneNumber(formDto.getPhone())
//            .birthDate(formDto.getBirthDate())
//            .address(formDto.getAddress()).build();
//    }
//

}

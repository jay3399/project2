package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.Member;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MemberListDto {

    private Long id;
    private String userId;
    private String nickname;
    private LocalDateTime createDate;
    private Role role;

    private String phoneNumber;
    private String email;

    private Long countOfBoards;

    private Long countOfComments;


    public void setMemberListDto(Member member) {
        this.id = member.getId();
        this.userId = member.getUserId();
        this.nickname = member.getNickname();
        this.createDate = member.getCreatedDate();
        this.role = member.getRole();
        this.phoneNumber = member.getPhoneNumber();
        this.email = member.getEmail();
        this.countOfBoards = member.getCountOfBoards();
        this.countOfComments = member.getCountOfComments();
    }


    public static MemberListDto from(Member member) {

        MemberListDto dto = new MemberListDto();
        dto.setMemberListDto(member);
        return dto;

    }








}

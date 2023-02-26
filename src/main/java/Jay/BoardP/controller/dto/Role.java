package Jay.BoardP.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    USER("ROLE_USER") , ADMIN("ROLE_ADMIN") , PENALTY("ROLE_PENALTY") , HUMAN("ROLE_HUMAN"), DELETED("ROLE_DELETED");



    private final String value;


}

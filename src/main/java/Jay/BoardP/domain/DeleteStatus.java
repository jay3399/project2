package Jay.BoardP.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeleteStatus {

    Y("DELETE_Y") , N("DELETE_N");

    private String value;


}

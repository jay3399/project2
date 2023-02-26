package Jay.BoardP.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardSearch {

    private String title;
    private String content;
    private String complex;
    private String nickname;


}

package Jay.BoardP.domain;


import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CountPerDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long visitPerDay;

    private Long signInPerDay;

    private Long signUpPerDay;

    private Long boardPerDay;

    private Long boardCntPerDay;

    private LocalDateTime createdDate;


    protected CountPerDay(Long visitPerDay, Long signInPerDay,
        Long boardPerDay, Long boardCntPerDay, Long signUpPerDay) {
        this.visitPerDay = visitPerDay;
        this.signInPerDay = signInPerDay;
        this.boardPerDay = boardPerDay;
        this.boardCntPerDay = boardCntPerDay;
        this.signUpPerDay = signUpPerDay;
        this.createdDate = LocalDateTime.now();
    }



    public static CountPerDay createCountPerDay(Long visitPerDay, Long signInPerDay,
        Long boardPerDay, Long boardCntPerDay, Long signUpPerDay) {

        return new CountPerDay(visitPerDay, signInPerDay, boardPerDay, boardCntPerDay, signUpPerDay);
    }

}

package Jay.BoardP.domain;


import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Setter;

@Entity
@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CountPerMonth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long visitPerMonth = 0L;

    private Long signInPerMonth = 0L;

    private Long signUpPerMonth = 0L;

    private Long boardPerMonth = 0L;

    private Long boardCntPerMonth = 0L;

    private LocalDateTime createdDate;


    public CountPerMonth() {
        this.createdDate = LocalDateTime.now();
    }


//    protected CountPerMonth(Long visitPerDay, Long signInPerDay,
//        Long boardPerDay, Long boardCntPerDay, Long signUpPerDay) {
//        this.visitPerMonth = visitPerDay;
//        this.signInPerMonth = signInPerDay;
//        this.signUpPerMonth = boardPerDay;
//        this.boardPerMonth = boardCntPerDay;
//        this.boardCntPerMonth = signUpPerDay;
//    }

//    public static CountPerMonth createCountPerMonth(Long id, Long visitPerDay, Long signInPerDay,
//        Long boardPerDay, Long boardCntPerDay, Long signUpPerDay) {
//
//        return new CountPerMonth(Long id, visitPerDay, signInPerDay, boardPerDay, boardCntPerDay, signUpPerDay);
//    }


    public void addVisitPerMonth(CountPerDay
        countPerDay) {

        this.visitPerMonth += countPerDay.getVisitPerDay();
        this.signUpPerMonth += countPerDay.getSignUpPerDay();
        this.signInPerMonth += countPerDay.getSignInPerDay();
        this.boardPerMonth += countPerDay.getBoardPerDay();
        this.boardCntPerMonth += countPerDay.getBoardCntPerDay();

    }



}

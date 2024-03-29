package Jay.BoardP;

import Jay.BoardP.controller.dto.Role;
import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.CountPerDay;
import Jay.BoardP.domain.CountPerMonth;
import Jay.BoardP.domain.Member;
import Jay.BoardP.domain.TotalVisit;
import Jay.BoardP.repository.CountPerDayRepository;
import Jay.BoardP.repository.CountPerMonthRepository;
import Jay.BoardP.repository.SpringDataCountRepository;
import Jay.BoardP.repository.SpringDataTotalVisitRepository;
import java.security.SecurityPermission;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final RedisTemplate redisTemplate;
    private final SpringDataCountRepository repository;
    private final SpringDataTotalVisitRepository totalVisitRepository;
    private final CountPerDayRepository countPerDayRepository;
    private final CountPerMonthRepository countPerMonthRepository;


    @Bean
    public Job jobPerDay() {
        return jobBuilderFactory.get("jobPerDay").preventRestart().start(stepForPenalty())
            .next(stepForTotalVisit())
            .next(stepForCountPerDay()).build();
    }

    @Bean
    public Job jobPerMonth() {
        return jobBuilderFactory.get("jobPerMonth").preventRestart().start(stepForHumanOnMember())
            .next(stepForHumanOnBoard()).next(
                stepForCountPerMonth()).build();
    }

    //    @Bean
    public Step stepForHumanOnMember() {
        return stepBuilderFactory.get("stepForHumanOnMember").<Member, Member>chunk(10)
            .reader(jpaCursorItemReader()).processor(chunkProcessor()).writer(jpaCursorItemWriter())
            .build();
    }

    //    @Bean
    public Step stepForHumanOnBoard() {
        return stepBuilderFactory.get("stepForHumanOnBoard").<Board, Board>chunk(10)
            .reader(jpaCursorItemReader3()).processor(chunkProcessor2())
            .writer(jpaCursorItemWriter2())
            .build();
    }


    //    @Bean
    public Step stepForPenalty() {
        return stepBuilderFactory.get("stepForPenalty").<Board, Board>chunk(10)
            .reader(jpaCursorItemReader2()).processor(chunkProcessor2())
            .writer(jpaCursorItemWriter2()).build();
    }


    public Step stepForTotalCount() {
        return stepBuilderFactory.get("stepForTotalCount").<TotalVisit, TotalVisit>chunk(1)
            .reader(jpaCursorItemReader5()).processor(chunkProcessor5())
            .writer(jpaCursorItemWriter4()).build();
    }


    // 휴먼회원 , 로그인 일자로부터 60일 지난게시글 read
//    @Bean
    public JpaCursorItemReader<Member> jpaCursorItemReader() {

        Map<String, Object> param = new HashMap<>();

        LocalDateTime dateTime = LocalDateTime.now().minusDays(60);

        param.put("date", dateTime);

        return new JpaCursorItemReaderBuilder<Member>()
            .name("jpaCursorMemberReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT m FROM Member m where m.loginDate < :date")
            .parameterValues(param)
            .build();
    }

    //마지막 업데이트일로부터 60일 지난 게시글 read
//    @Bean
    public JpaCursorItemReader<Board> jpaCursorItemReader3() {

        Map<String, Object> param = new HashMap<>();

        LocalDateTime dateTime = LocalDateTime.now().minusDays(60);

        param.put("date", dateTime);

        return new JpaCursorItemReaderBuilder<Board>()
            .name("jpaCursorItemReader3")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT b FROM Board b where b.modifiedDate < :date")
            .parameterValues(param)
            .build();
    }

    //신고누적 게시판
//    @Bean
    public JpaCursorItemReader<Board> jpaCursorItemReader2() {

        return new JpaCursorItemReaderBuilder<Board>()
            .name("jpaCursorBoardReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT b FROM Board b where 9 < b.countOfPenalties")
            .build();
    }

    public JpaCursorItemReader<TotalVisit> jpaCursorItemReader5() {

        return new JpaCursorItemReaderBuilder<TotalVisit>()
            .name("jpaCursorItemReader4")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT t FROM TotalVisit t")
            .build();
    }

    //월별 데이터 집계 , 일별데이터 모두 select

    //    @Bean
    public JpaCursorItemReader<CountPerDay> jpaCursorItemReader4() {

        //달의 첫날부터  시작되는시점인 마지막날 11시59분까지 의 데이터를 셀렉트

        return new JpaCursorItemReaderBuilder<CountPerDay>()
            .name("jpaCursorItemReader4")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT c FROM CountPerDay c where b.modifiedDate < :date")
            .build();
    }


    // 휴먼회원 전환
//    @Bean
    public ItemProcessor<Member, Member> chunkProcessor() {
        return m -> {
            m.setRole(Role.HUMAN);
            return m;
        };
    }


    // 신고누적 및 오래된 게시판 정리
//    @Bean
    public ItemProcessor<Board, Board> chunkProcessor2() {
        return b -> {
            b.setIsDeleted(true);
            return b;
        };
    }

    public ItemProcessor<TotalVisit, TotalVisit> chunkProcessor5() {

        Long visitCountPerDay = Long.parseLong(
            String.valueOf(redisTemplate.opsForValue().get("VisitCountPerDay")));

        return b -> {
            Long count = b.getCount();
            b.setId(count + visitCountPerDay);
            return b;
        };
    }


    //    @Bean
    public ItemWriter<Member> jpaCursorItemWriter() {
        return new JpaItemWriterBuilder<Member>().entityManagerFactory(entityManagerFactory)
            .build();
    }

    //    @Bean
    public ItemWriter<Board> jpaCursorItemWriter2() {
        return new JpaItemWriterBuilder<Board>().entityManagerFactory(entityManagerFactory)
            .build();
    }

    //    @Bean
    public ItemWriter<CountPerMonth> jpaCursorItemWriter3() {
        return new JpaItemWriterBuilder<CountPerMonth>().entityManagerFactory(entityManagerFactory)
            .build();
    }

    public ItemWriter<TotalVisit> jpaCursorItemWriter4() {
        return new JpaItemWriterBuilder<TotalVisit>().entityManagerFactory(entityManagerFactory)
            .build();
    }


    //일일데이터
//    @Bean
    public Step stepForCountPerDay() {
        return stepBuilderFactory.get("stepForCountPerDay")
            .tasklet(
                ((contribution, chunkContext) -> {

                    Long boardPerDay = 0L;
                    Long boardCountPerDay = 0L;
                    Long signUpPerDay = 0L;
                    Long VisitCountPerDay = 0L;
                    Long signInPerDay = 0L;

                    if (redisTemplate.hasKey("boardPerDay")) {
                        boardPerDay = Long.parseLong(
                            String.valueOf(
                                redisTemplate.opsForValue().getAndDelete("boardPerDay")));
                    }
                    if (redisTemplate.hasKey("boardCountPerDay")) {
                        boardCountPerDay = Long.parseLong(
                            String.valueOf(
                                redisTemplate.opsForValue().getAndDelete("boardCountPerDay")));
                    }
                    if (redisTemplate.hasKey("signUpPerDay")) {
                        signUpPerDay = Long.parseLong(
                            String.valueOf(
                                redisTemplate.opsForValue().getAndDelete("signUpPerDay")));
                    }
                    if (redisTemplate.hasKey("VisitCountPerDay")) {
                        VisitCountPerDay = Long.parseLong(
                            String.valueOf(
                                redisTemplate.opsForValue().getAndDelete("VisitCountPerDay")));
                    }
                    if (redisTemplate.hasKey("signInPerDay")) {
                        signInPerDay = Long.parseLong(
                            String.valueOf(
                                redisTemplate.opsForValue().getAndDelete("signInPerDay")));
                    }

                    CountPerDay countPerDay = CountPerDay.createCountPerDay(VisitCountPerDay,
                        signInPerDay, boardPerDay,
                        boardCountPerDay, signUpPerDay);

                    repository.save(countPerDay);

                    return RepeatStatus.FINISHED;

                })
            ).build();
    }


    public Step stepForTotalVisit() {
        return stepBuilderFactory.get("stepForTotalVisit")
            .tasklet(
                ((contribution, chunkContext) -> {

                    if (!redisTemplate.hasKey("VisitCountPerDay")) {
                        return RepeatStatus.FINISHED;
                    }

                    Long visitCountPerDay = Long.parseLong(
                        String.valueOf(redisTemplate.opsForValue().get("VisitCountPerDay")));

                    totalVisitRepository.findById(1L).ifPresentOrElse(
                        totalVisit -> {
                            Long count = totalVisit.getCount();
                            totalVisit.setCount(count + visitCountPerDay);
                        },
                        () -> totalVisitRepository.save(new TotalVisit(1L, visitCountPerDay))
                    );

                    return RepeatStatus.FINISHED;
                })
            ).build();
    }


    //    @Bean
    public Step stepForCountPerMonth() {
        return stepBuilderFactory.get("stepForCountPerMonth")
            .tasklet(
                ((contribution, chunkContext) -> {

                    LocalDateTime dateTime = LocalDate.now()
                        .with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();

                    LocalDateTime dateTime1 = LocalDate.now()
                        .with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);

                    CountPerMonth countPerMonth = new CountPerMonth();
                    countPerDayRepository.findByCreatedDateBetween(dateTime, dateTime1).stream()
                        .forEach(
                            countPerDay -> {
                                System.out.println("countPerDay = " + countPerDay.getVisitPerDay());
                                countPerMonth.addVisitPerMonth(countPerDay);
                            }
                        );

                    countPerMonthRepository.save(countPerMonth);
                    return RepeatStatus.FINISHED;

                })
            ).build();
    }

}

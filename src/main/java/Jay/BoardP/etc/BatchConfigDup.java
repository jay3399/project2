package Jay.BoardP.etc;

import Jay.BoardP.controller.dto.Role;
import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.CountPerDay;
import Jay.BoardP.domain.Member;
import Jay.BoardP.repository.SpringDataCountRepository;
import java.time.LocalDateTime;
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

//1주일마다 멤버를 휴먼회원으로  , 마지막 접속일이 1달이상이면 휴면으로


@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfigDup {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final RedisTemplate redisTemplate;

    private final SpringDataCountRepository repository;



    @Bean
    public Job job() {
        return jobBuilderFactory.get("job").start(step()).next(step2()).build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step").<Member, Member>chunk(10)
            .reader(jpaCursorItemReader()).processor(chunkProcessor()).writer(jpaCursorItemWriter())
            .build();

    }

    // 휴먼회원
    @Bean
    public JpaCursorItemReader<Member> jpaCursorItemReader() {

        Map<String, Object> param = new HashMap<>();

        LocalDateTime dateTime = LocalDateTime.now().minusDays(30);

        param.put("date", dateTime);

        return new JpaCursorItemReaderBuilder<Member>()
            .name("jpaCursorMemberReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT m FROM Member m where m.loginDate < :date")
            .parameterValues(param)
            .build();
    }

    //신고누적 게시판
    @Bean
    public JpaCursorItemReader<Board> jpaCursorItemReader2() {

        return new JpaCursorItemReaderBuilder<Board>()
            .name("jpaCursorBoardReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT b FROM Board b where b.countOfPenalties > 9")
            .build();
    }





    // 휴먼회원 전환
    @Bean
    public ItemProcessor<Member , Member> chunkProcessor() {
        return m -> {
            m.setRole(Role.HUMAN);
            return m;
        };
    }

    // 신고누적 게시판 정리
    @Bean
    public ItemProcessor<Board, Board> chunkProcessor2() {
        return b -> {
            b.setIsDeleted(true);
            return b;
        };
    }


    @Bean
    public ItemWriter<Member> jpaCursorItemWriter() {
        return new JpaItemWriterBuilder<Member>().entityManagerFactory(entityManagerFactory)
            .build();
    }


    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
            .tasklet(
                ((contribution, chunkContext) -> {

                    Long boardPerDay = null;
                    Long boardCountPerDay = null;
                    Long signUpPerDay = null;
                    Long VisitCountPerDay = null;
                    Long signInPerDay = null;

                    Object visitCountPerDay = redisTemplate.opsForValue().get("VisitCountPerDay");

                    System.out.println("visitCountPerDay = " + visitCountPerDay);

                    String s = String.valueOf(visitCountPerDay);

                    System.out.println("s = " + s);


                    if (redisTemplate.hasKey("boardPerDay")) {
                        boardPerDay = Long.parseLong(
                            String.valueOf(redisTemplate.opsForValue().getAndDelete("boardPerDay")));
                    }
                    if (redisTemplate.hasKey("boardCountPerDay")) {
                        boardCountPerDay = Long.parseLong(
                            String.valueOf(redisTemplate.opsForValue().getAndDelete("boardCountPerDay")));
                    }
                    if (redisTemplate.hasKey("signUpPerDay")) {
                        signUpPerDay = Long.parseLong(
                            String.valueOf( redisTemplate.opsForValue().getAndDelete("signUpPerDay")));
                    }
                    if (redisTemplate.hasKey("VisitCountPerDay")) {
                        VisitCountPerDay = Long.parseLong(
                            String.valueOf(redisTemplate.opsForValue().getAndDelete("VisitCountPerDay")));
                    }
                    if (redisTemplate.hasKey("signInPerDay")) {
                        signInPerDay = Long.parseLong(
                            String.valueOf(redisTemplate.opsForValue().getAndDelete("signInPerDay")));
                    }

                    CountPerDay countPerDay = CountPerDay.createCountPerDay(VisitCountPerDay,
                        signInPerDay, boardPerDay,
                        boardCountPerDay, signUpPerDay);

                    repository.save(countPerDay);

                    return RepeatStatus.FINISHED;

                })
            ).build();
    }


    //    @Bean
//    public Step step2() {
//        return stepBuilderFactory.get("step2").chunk(10).reader(
//                new ItemReader<String>() {
//                    @Override
//                    public String read()
//                        throws Exception {
//                        return "3";
//                    }
//                }
//            )
//            .build();
//    }

}

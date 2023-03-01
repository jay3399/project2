package Jay.BoardP;


import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchScheduler {


    private final JobLauncher jobLauncher;
    private final BatchConfig batchConfig;

    @Scheduled(cron = "0 */10 * * * *")
    private void runJobPerDay() {

        Map<String, JobParameter> parameterMap = new HashMap<>();

        parameterMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(parameterMap);

        try {
            jobLauncher.run(batchConfig.jobPerDay(), jobParameters);
        } catch(JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e){

            log.error(e.getMessage());
        }
    }


    @Scheduled(cron = "0 */59 * * * *")
    private void runJobPerMonth() {

        Map<String, JobParameter> parameterMap = new HashMap<>();

        parameterMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(parameterMap);

        try {
            jobLauncher.run(batchConfig.jobPerMonth(), jobParameters);
        } catch(JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e){

            log.error(e.getMessage());
        }

    }


}

package capital.blockpower.tendermonitor.job.config;

import capital.blockpower.tendermonitor.job.TenderMonitorJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tiger
 */

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail tenderMonitorJobDetail(){
        return JobBuilder.newJob(TenderMonitorJob.class)
                .withIdentity("TenderMonitorJobDetail","MOINTOR_JOBGROUP")
                .storeDurably(true)
                .build();
    }

    @Bean
    public Trigger tenderMonitorJobTrigger(){
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("5 * * * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(tenderMonitorJobDetail())
                .withIdentity("TenderMonitorJobTrigger","MOINTOR_TRIGGERGROUP")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

}

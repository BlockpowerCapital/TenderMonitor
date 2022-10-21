package capital.blockpower.tendermonitor.job;

import capital.blockpower.tendermonitor.bean.ServerItem;
import capital.blockpower.tendermonitor.services.StatusService;
import capital.blockpower.tendermonitor.utils.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;

/**
 * @author Tiger
 */

@Slf4j
public class TenderMonitorJob extends QuartzJobBean {
    @Autowired
    private StatusService statusService;

    @Value("${data.path.server}")
    private String serverPath;

    private static final String FILE_SUFFIX = ".json";

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext){


        File[] files = FileUtil.listAll(serverPath, FILE_SUFFIX);
        for (File file : files) {
            try {
                ServerItem serverItem = JSONObject.parseObject(FileUtil.readFileAll(file.getAbsolutePath()), ServerItem.class);
                ThreadUtil.execAsync(() -> statusService.checkStatus(serverItem));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

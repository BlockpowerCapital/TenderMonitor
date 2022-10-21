package capital.blockpower.tendermonitor.utils;

import capital.blockpower.tendermonitor.bean.MessageLog;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Tiger
 */

@Slf4j
public class DataUtil {


    private DataUtil() {
    }

    public static boolean getLogByAddress(String path, String address) {
        boolean flag = false;

        String fileContent = FileUtil.readFileAll(path + address + ".json");
        if (!"".equals(fileContent)) {
            MessageLog messageLog = JSONObject.parseObject(fileContent, MessageLog.class);
            if (messageLog.getFlags() == 0 && Duration.between(messageLog.getMessageTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), LocalDateTime.now()).toMinutes() < 60L) {
                flag = true;
            }
        }

        log.error("log-check:{}", flag);
        return flag;
    }


}

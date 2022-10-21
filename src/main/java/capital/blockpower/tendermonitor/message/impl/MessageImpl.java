package capital.blockpower.tendermonitor.message.impl;

import capital.blockpower.tendermonitor.bean.Message;
import capital.blockpower.tendermonitor.bean.MessageLog;
import capital.blockpower.tendermonitor.message.BaseMessage;
import capital.blockpower.tendermonitor.utils.DataUtil;
import capital.blockpower.tendermonitor.utils.FileUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author： Tiger
 * @Date: 2021/9/29 6:22:41
 */
@Slf4j
@Component
public class MessageImpl extends BaseMessage {
    @Override
    public boolean send(Message messageData) {
        return false;
    }

    @Override
    public void saveLog(MessageLog messageLog, Message message, TYPE type) {
        log.info("{} saveLog",type);
        String jsonStr = JSONObject.toJSONString(messageLog);
        FileUtil.saveFileAs(jsonStr, message.getLogDataPath()+type.toString().toLowerCase()+"/"+ messageLog.getAddress()+".json");
    }

    @Override
    public boolean checkLog(Message message, TYPE type) {
        log.info("{} checkLog:{}",type, message.getLogDataPath()+type.toString().toLowerCase()+"/");
        return DataUtil.getLogByAddress(message.getLogDataPath()+type.toString().toLowerCase()+"/", message.getAddress());
    }
}

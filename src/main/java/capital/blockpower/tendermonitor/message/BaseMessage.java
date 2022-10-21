package capital.blockpower.tendermonitor.message;


import capital.blockpower.tendermonitor.bean.Message;
import capital.blockpower.tendermonitor.bean.MessageLog;

/**
 * @author Tiger
 */
public abstract class BaseMessage {
    public enum TYPE{
        /**
         * Telegram message
         */
        TELEGRAM,

    }
    public abstract boolean send(Message messageData);
    public abstract void saveLog(MessageLog messageLog, Message message, TYPE type);
    public abstract boolean checkLog(Message messaageData, TYPE type);
}

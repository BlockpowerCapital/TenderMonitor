package capital.blockpower.tendermonitor.message.impl;

import capital.blockpower.tendermonitor.bean.TelegramAccount;
import capital.blockpower.tendermonitor.bot.TenderMonitorBotHandlers;
import capital.blockpower.tendermonitor.bean.Message;
import capital.blockpower.tendermonitor.bean.MessageLog;
import capital.blockpower.tendermonitor.services.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author Tiger
 */

@Slf4j
@Component
public class TelegramPush extends MessageImpl {

    TYPE type = TYPE.TELEGRAM;

    @Autowired
    private TenderMonitorBotHandlers urMonitorBotHandlers;

    @Autowired
    private DataService dataService;

    private static TelegramPush telegramPush;

    @PostConstruct
    public void init() {
        telegramPush = this;
    }


    @Override
    public boolean send(Message messageData) {

        if (super.checkLog(messageData, type)) {
            log.error("Repeat push");
            return false;
        }

        MessageLog messageLog;

        messageLog = MessageLog.builder()
                .flags(0)
                .messageType(messageData.getMessageType())
                .address(messageData.getAddress())
                .messageTime(Date.from(LocalDateTime.parse(messageData.getTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        log.info("{} send", type);

        List<TelegramAccount> accounts = telegramPush.dataService.getTelegramAccounts();
        for (TelegramAccount account : accounts) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableHtml(true);

            sendMessage.setText("<b>\uD83D\uDD14TenderMonitor</b>\n" + messageData.getFirst() + "\n\n" + messageData.getPerformance() + "\nip:" + messageData.getAddress() + "\n<i>Time：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</i>");

            sendMessage.setChatId(account.getChatId());
            try {
                telegramPush.urMonitorBotHandlers.execute(sendMessage);
            } catch (Exception e) {
                log.error("send error:{}", account.getChatId());
                e.printStackTrace();
            }

        }

        super.saveLog(messageLog, messageData, type);
        return true;
    }
}

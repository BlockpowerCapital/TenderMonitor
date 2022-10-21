package capital.blockpower.tendermonitor.bot;

import capital.blockpower.tendermonitor.bean.TelegramAccount;
import capital.blockpower.tendermonitor.services.DataService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : Tiger
 * @create 2022/9/19 09:56
 */
@Slf4j
@Component
@Setter
public class TenderMonitorBotHandlers extends TelegramLongPollingBot {

    private static final String LOGTAG = "TENDERMONITORBOT";


    @Value("${data.bot.token}")
    private String token;

    @Value("${data.bot.username}")
    private String userName;

    @Value("${data.bot.regtoken}")
    private String regToken;

    @Value("${data.bot.regcommand}")
    private String regCommand;

    @Autowired
    private DataService dataService;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            handleDirections(update);

        } catch (Exception e) {
            log.error(LOGTAG, e);
        }
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    private void handleDirections(Update update) {

        log.info("{}",update.getMessage());
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            if (messageText.startsWith(regCommand)){
                onRegReceived(update.getMessage());
            }else{
                //do nothing
            }

        }
    }

    private void onRegReceived(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        if(message.getText().endsWith(regToken)){
            TelegramAccount account = dataService.getTelegramAccountSetting(message.getChatId().toString());
            if (account != null) {
                sendMessage.setText("The user has registered. Do not register again");
            }else{
                account = TelegramAccount.builder()
                        .chatId(message.getChatId().toString())
                        .messageFlag(1)
                        .build();

                account = dataService.saveTelegramUserSetting(account);

                if(account.getChatId() == null || "".equals(account.getChatId())){
                    sendMessage.setText("Registration failed");
                }else{
                    sendMessage.setText("Registration succeeded. All notifications have been enabled");
                }
            }

        }else{
            sendMessage.setText("Registration failed. The registration code is invalid");
        }

        try {
            execute(sendMessage);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}

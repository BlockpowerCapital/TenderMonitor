package capital.blockpower.tendermonitor.services;

import capital.blockpower.tendermonitor.bean.TelegramAccount;
import capital.blockpower.tendermonitor.utils.FileUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : Tiger
 * @create 2022/9/19 09:58
 */
@Repository
@Service
@Slf4j
public class DataService {

    @Value("${data.path.user.telegram}")
    private String telegramUserDataPath;

    private static final String  FILE_SUFFIX = ".json";

    public TelegramAccount saveTelegramUserSetting(TelegramAccount account){
        if(!FileUtil.saveFileAs(JSONObject.toJSONString(account),telegramUserDataPath+account.getChatId()+FILE_SUFFIX)){
            account.setChatId("");
            return account;
        }
        return account;
    }


    public TelegramAccount getTelegramAccountSetting(String chatId){
        TelegramAccount account = null;
        File[] files = FileUtil.listAll(new File(telegramUserDataPath), new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(FILE_SUFFIX);
            }

            @Override
            public String getDescription() {
                return null;
            }
        });

        for (File file : files) {
            if(file.getName().contains(chatId)){
                account = JSONObject.parseObject(FileUtil.readFileAll(file.getAbsolutePath()),TelegramAccount.class);
                break;
            }
        }
        return account;
    }

    public List<TelegramAccount> getTelegramAccounts(){

        List<TelegramAccount> accounts = new ArrayList<>();
        File[] files = FileUtil.listAll(new File(telegramUserDataPath), new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(FILE_SUFFIX);
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        for (File file : files) {
            TelegramAccount account = JSONObject.parseObject(FileUtil.readFileAll(file.getAbsolutePath()),TelegramAccount.class);
            if(account.getMessageFlag() == 1){
                accounts.add(account);
            }

        }
        return accounts;
    }
}

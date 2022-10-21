package capital.blockpower.tendermonitor.services;

import capital.blockpower.tendermonitor.bean.Message;
import capital.blockpower.tendermonitor.bean.ServerItem;
import capital.blockpower.tendermonitor.message.BaseMessage;
import capital.blockpower.tendermonitor.message.impl.TelegramPush;
import capital.blockpower.tendermonitor.utils.FileUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : Tiger
 * @create 2022/9/22 11:22
 */
@Repository
@Service
@Slf4j
public class StatusService {

    @Value("${data.path.log}")
    private String logDataPath;

    public void checkStatus(ServerItem serverItem) {

        try {
            log.info("{}: ", serverItem);
            String firstStr = "";
            String performanceStr = "";
            if (!isOnline(serverItem.getIp(), serverItem.getPort())) {
                //server is offline
                firstStr = "Server offline";
                performanceStr = "Server " + serverItem.getName() + " is offline, please check it";

            } else if (!isOnTime(serverItem.getApiUrl())) {
                //server block time is not correct
                firstStr = "Server anomaly";
                performanceStr = "Server " + serverItem.getName() + ": latest block time anomaly, please check it";
            } else {
                //server is ok
                log.info("Server is ok");
            }
            if(!"".equals(firstStr)){
                log.info("firstStr: {}", firstStr);
                Message messageData = Message.builder()
                        .messageType(serverItem.getIp())
                        .first(firstStr)
                        .performance(performanceStr)
                        .logDataPath(logDataPath)
                        .address(serverItem.getIp())
                        .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .build();

                BaseMessage telegramMessage = new TelegramPush();
                telegramMessage.send(messageData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isOnTime(String apiUrl) {
        if (apiUrl == null || "".equals(apiUrl)) {
            return true;
        }
        try {

            Document doc = Jsoup.connect(apiUrl + "blocks/latest")
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-US;q=0.8,en;q=0.5,zh-CN,zh;q=0.3")
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                    .timeout(100000).ignoreContentType(true).get();
            JSONObject jsonObject = JSONObject.parseObject(doc.body().text());

            String blockTimeStr = jsonObject.getJSONObject("block").getJSONObject("header").getString("time");
            blockTimeStr = blockTimeStr.substring(0, blockTimeStr.indexOf("."));
            DateTime blockDate = DateUtil.parse(blockTimeStr);
            DateTime now = DateUtil.date();

            return blockDate.between(now, DateUnit.SECOND) <= 60;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isOnline(String hostname, int port) {
        if (hostname == null || "".equals(hostname)) {
            return true;
        }
        Socket server;
        try {
            server = new Socket();
            InetSocketAddress address = new InetSocketAddress(hostname, port);
            server.connect(address, 3000);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

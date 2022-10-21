package capital.blockpower.tendermonitor.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : Tiger
 * @create 2022/9/22 13:31
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerItem {
    private String name;
    private String apiUrl;
    private String ip;
    private int port;
}

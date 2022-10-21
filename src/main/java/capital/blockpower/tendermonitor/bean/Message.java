package capital.blockpower.tendermonitor.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * @author Tiger
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String messageType;
    private String templateId;
    private String url;
    private String toUser;
    private String first;
    private String performance;
    private String time;
    private String remark;
    private String logDataPath;

    private String title;
    private String text;

    private String address;
}

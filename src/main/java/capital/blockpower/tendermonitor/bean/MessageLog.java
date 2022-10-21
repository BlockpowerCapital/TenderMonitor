package capital.blockpower.tendermonitor.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Tiger
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageLog {
    private String messageType;
    private Date messageTime;
    private String address;
    private int flags;
}

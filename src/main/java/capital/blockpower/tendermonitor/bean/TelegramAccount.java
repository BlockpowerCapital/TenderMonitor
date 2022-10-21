package capital.blockpower.tendermonitor.bean;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Tiger
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramAccount {

    private String chatId;
    private int messageFlag;

}

package DTO;

import java.util.ArrayList;
import java.util.List;

public class NotificationsDTO {

    public class NotificationDTO{
        public String yaz;
        public String loanName;
        public String amount;
        public String details;

        public NotificationDTO(String yaz, String loanName, String amount, String details){
            this.loanName = loanName;
            this.yaz = yaz;
            this.amount = amount;
            this.details = details;
        }
    }

    public NotificationsDTO(){
        notifications = new ArrayList<NotificationDTO>();
    }
    public List<NotificationDTO> notifications;
}

package DTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationsDTO {

    public class NotificationDTO{
        public String yaz;
        public String loanName;
        public String amount;
        public String dateTime;

        public NotificationDTO(String yaz, String loanName, String amount, String dateTime){
            this.loanName = loanName;
            this.yaz = yaz;
            this.amount = amount;
            this.dateTime = dateTime;
        }
    }

    public NotificationsDTO(){
        notifications = new ArrayList<NotificationDTO>();
    }
    public List<NotificationDTO> notifications;
}

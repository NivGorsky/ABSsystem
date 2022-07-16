package jsonDeserializer;
import DTO.LoanDTO;
import DTO.NotificationsDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javax.security.auth.login.Configuration;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

public class NotificationsDTODeserializer implements JsonDeserializer<NotificationsDTO> {

    private Gson gson = new Gson();

    @Override
    public NotificationsDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //extract raw data
        JsonObject jsonObject = json.getAsJsonObject();

        //build the actual object
        Type notificationsDTOType = new TypeToken<List<NotificationsDTO.NotificationDTO>>(){}.getType();
        JsonArray notificationsAsJsonObject = jsonObject.getAsJsonArray("notifications");
        List<NotificationsDTO.NotificationDTO> notificationDTOSFromJson = gson.fromJson(notificationsAsJsonObject, notificationsDTOType);
        NotificationsDTO newNotificationsDTO = new NotificationsDTO();
        newNotificationsDTO.notifications = notificationDTOSFromJson;

        return newNotificationsDTO;
    }
}

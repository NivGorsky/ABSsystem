package main;
import DTO.LoanDTO;
import customerScene.CustomerSceneController;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;

public class CustomerComponentsRefresher extends TimerTask {

    private CustomerSceneController customerSceneToUpdate;
    private int version;
    private final Object lock = new Object();

    public CustomerComponentsRefresher(CustomerSceneController customerSceneController) {
        this.customerSceneToUpdate = customerSceneController;
        this.version = 0;
    }

    @Override
    public void run() {

        synchronized (lock) {
            if(shouldUpdate(version)) {
                customerSceneToUpdate.onShow();
            }
        }

    }

    private boolean shouldUpdate(int version) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/version").newBuilder();
        urlBuilder.addQueryParameter("consumer", "CUSTOMER");
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder().url(finalUrl).build();
        Call call = Configurations.HTTP_CLIENT.newCall(request);

        Callback versionCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                customerSceneToUpdate.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            customerSceneToUpdate.createExceptionDialog(new Exception(responseBody)));
                }
                else {
                    Platform.runLater(() -> {
                       
                    });
                }

                response.close();
            }
        };

        call.enqueue(versionCallBack);
    }

}

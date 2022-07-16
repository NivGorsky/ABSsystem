package main;
import customerScene.CustomerSceneController;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomerComponentsRefresher extends TimerTask {

    private CustomerSceneController customerSceneToUpdate;
    private int version;
    private boolean update;
    private final Object lock = new Object();

    public CustomerComponentsRefresher(CustomerSceneController customerSceneController) {
        this.customerSceneToUpdate = customerSceneController;
        this.version = 1;
    }

    @Override
    public void run() {

        if(customerSceneToUpdate.getIsFileSelected()) {
            synchronized (lock) {
                shouldUpdate();
                if(update) {
                    customerSceneToUpdate.onShow();
                }
            }
        }
    }

    private void shouldUpdate() {
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
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            customerSceneToUpdate.createExceptionDialog(new Exception(responseBody)));
                }
                else {
                    Platform.runLater(() -> {
                       int versionResponse = Configurations.GSON.fromJson(responseBody, int.class);
                       if(version != versionResponse) {
                           update = true;
                           version++;
                       }
                       else {
                           update = false;
                       }
                    });
                }

                response.close();
            }
        };

        call.enqueue(versionCallBack);
    }

}

package DTO;

import javafx.concurrent.Task;

public interface UIController {
    public void bindTaskToUIComponents(Task<Boolean> task);
}

package mutualInterfaces;

import Engine.MainSystem;

public interface ParentController {
    public MainSystem getModel();
    public void createExceptionDialog(Exception ex);
}

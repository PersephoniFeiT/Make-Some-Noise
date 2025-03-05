package frontend;

import backend.Accounts.CurrentSession;
import frontend.ProjectEditorGUI;

public class App {
    public static void main(String[] args) throws Exception {
        
        CurrentSession currentSession = new CurrentSession();

        new ProjectEditorGUI(currentSession);
    }
}

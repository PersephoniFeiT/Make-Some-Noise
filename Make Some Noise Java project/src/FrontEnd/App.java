package FrontEnd;

import BackEnd.Accounts.CurrentSession;
import FrontEnd.MakeSomeNoiseWindow;

public class App {
    public static void main(String[] args) throws Exception {

        CurrentSession currentSession = new CurrentSession();

        new MakeSomeNoiseWindow(currentSession);
    }
}

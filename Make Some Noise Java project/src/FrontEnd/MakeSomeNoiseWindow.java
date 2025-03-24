package FrontEnd;

import javax.swing.*;
import BackEnd.Accounts.CurrentSession;
import java.awt.BorderLayout;

public class MakeSomeNoiseWindow extends JFrame {

    private CurrentSession currentSession;

    private EditorPanel editorPanel = null;
    private AccountPanel accountPanel = null;
    private SearchPanel searchPanel = null;

    private JPanel currentPanel = null;

    public MakeSomeNoiseWindow(CurrentSession currentSession) {
        setLayout(new BorderLayout());                            // using BorderLayout layout managers
        setSize(1000, 600);                                       // width and height
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.currentSession = currentSession;

        goToEditorPanel();

        setVisible(true);
    }

    public boolean hasEditorPanel() {
        return editorPanel != null;
    }

    public void addEditorPanel() {
        editorPanel = new EditorPanel(this);
    }

    public void goToEditorPanel() {
        if (!this.hasEditorPanel()) {
            addEditorPanel();
        }

        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = editorPanel;
        add(currentPanel);
    }

    public boolean hasAccountPanel() {
        return accountPanel != null;
    }

    public void addAccountPanel() {
        accountPanel = new AccountPanel();
    }

    public void goToAccountPanel() {
        if (!this.hasAccountPanel()) {
            addAccountPanel();
        }

        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = accountPanel;
        add(currentPanel);
    }

    public boolean hasSearchPanel() {
        return searchPanel != null;
    }

    public void addSearchPanel() {
        searchPanel = new SearchPanel();
    }

    public void goToSearchPanel() {
        if (!this.hasSearchPanel()) {
            addSearchPanel();
        }

        if (currentPanel != null) {
           remove(currentPanel);
        }
        currentPanel = searchPanel;
        add(currentPanel);
    }

    public void createSignInWindow() {
        new SignInWindow(currentSession);
    }
}

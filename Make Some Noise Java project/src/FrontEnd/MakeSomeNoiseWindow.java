package FrontEnd;

import javax.swing.*;
import BackEnd.Accounts.CurrentSession;
import BackEnd.Accounts.Project;
import Exceptions.Accounts.NotSignedInException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MakeSomeNoiseWindow extends JFrame {

    private CurrentSession currentSession;

    private EditorPanel editorPanel = null;
    private AccountPanel accountPanel = null;
    private SearchPanel searchPanel = null;

    private JPanel currentPanel = null;

    private JMenuBar menuBar;

    public MakeSomeNoiseWindow(CurrentSession currentSession) {
        setLayout(new BorderLayout());                            // using BorderLayout layout managers
        setSize(1300, 800);                                       // width and height
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.currentSession = currentSession;

        goToEditorPanel();

        // Initialize menu bar
        menuBar = new JMenuBar();

        JMenu accountsMenu = new JMenu("Account");

        JMenuItem menuItem = new JMenuItem("Sign In");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSignInWindow();
            }
        });
        accountsMenu.add(menuItem);

        menuItem = new JMenuItem("View Account");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToAccountPanel();
            }
        });
        accountsMenu.add(menuItem);

        menuBar.add(accountsMenu);

        JMenu fileMenu = new JMenu("File");
        menuItem = new JMenuItem("Go to open file");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToEditorPanel();
            }
        });
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Save");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // currentSession.save(new Project());
            }
        });
        fileMenu.add(menuItem);

        menuBar.add(fileMenu);

        JMenu searchMenu = new JMenu("Find"); 

        menuItem = new JMenuItem("Pattern Search");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToSearchPanel();
            }
        });
        searchMenu.add(menuItem);

        menuBar.add(searchMenu);

        setJMenuBar(menuBar);

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
        revalidate();
        repaint();
    }

    public boolean hasAccountPanel() {
        return accountPanel != null;
    }

    public void addAccountPanel() {
        try {
            currentSession.getSignedIn();
            accountPanel = new AccountPanel(currentSession.GetAccountInfo());
        } catch (NotSignedInException e) {
            accountPanel = new AccountPanel();
        }
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
        revalidate();
        repaint();
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
        revalidate();
        repaint();
    }

    public void createSignInWindow() {
        new SignInWindow(currentSession);
    }


    public static void main(String[] args) throws Exception {
        CurrentSession cs = new CurrentSession();
        new MakeSomeNoiseWindow(cs);

    }
}

package FrontEnd;

import javax.swing.*;
// import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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

        // Create and populate drop-down menu for account functions
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

        // Create and populate drop-down menu for file/project manipulation functions
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
                currentSession.SaveProject(editorPanel.getProject());
            }
        });
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("New");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEditorPanel();
                goToEditorPanel();
            }
        });
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Save As Locally");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {

                saveProject();
                
            }
        });
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Open from Disk");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                openFile();

            }
        });
        fileMenu.add(menuItem);

        menuBar.add(fileMenu);

        // Create and populate drop-down menu for searching project functions
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

    public void saveProject() {
        Project p = editorPanel.getProject();
                
        String fileContent = p.toJSONString();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    try { 
                        FileWriter file = new FileWriter(fileChooser.getSelectedFile());
                        p.title = fileChooser.getSelectedFile().getName();
                        file.write(fileContent);
                        file.close();
                    } catch (IOException ex) {
                        System.out.println("ERROR: Failed to write to file");
                    }
                }
            }
        });
        fileChooser.showSaveDialog(this);
    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    try { 
                        Scanner reader = new Scanner(fileChooser.getSelectedFile());
                        StringBuffer buff = new StringBuffer();
                        while (reader.hasNextLine()) {
                            buff.append(reader.nextLine());
                        }
                        reader.close();
                        addEditorPanel(Project.fromJSONtoProject(buff.toString()));
                        goToEditorPanel();
                    } catch (IOException ex) {
                        System.out.println("ERROR: Failed to read from file");
                    }
                }

            }
        });
        fileChooser.showOpenDialog(this);
    }

    public boolean hasEditorPanel() {
        return editorPanel != null;
    }

    public void addEditorPanel() {
        editorPanel = new EditorPanel(this, currentSession.CreateNewProject());
    }

    public void addEditorPanel(Project p) {
        editorPanel = new EditorPanel(this, p);
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
        accountPanel = new AccountPanel(currentSession.GetAccountInfo(), currentSession);
    }

    public void goToAccountPanel() {
        if (!this.hasAccountPanel()) {
            addAccountPanel();
        }

        // Check if the user has changed accounts; if they have then update the AccountPanel
        try {
            if (!currentSession.getSignedIn().equals(accountPanel.getAccountId())) {
                addAccountPanel();
            }
        } catch (NotSignedInException e) {
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

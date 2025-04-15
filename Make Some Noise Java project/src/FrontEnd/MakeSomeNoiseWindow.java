package FrontEnd;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

// import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import BackEnd.Accounts.CurrentSession;
import BackEnd.Accounts.Project;
import Exceptions.IncorrectPasswordException;
import Exceptions.InvalidInputException;
import Exceptions.NoSuchAccountException;
import Exceptions.NotSignedInException;

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
                try {
                    currentSession.getSignedIn();
                    signOut();
                } catch (NotSignedInException ex) {
                    createSignInWindow();
                }
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
                try {
                    currentSession.SaveProject(editorPanel.getProject());
                } catch (NotSignedInException x){
                    System.out.println("not signed in.");
                }
                //new PostProjectWindow(currentSession, editorPanel.getProject());
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
                saveProjectLocal(editorPanel.getProject());;
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

        menuItem = new JMenuItem("Save Image");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                saveImage();

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

    public static void reloadPanel(JPanel panel){
        panel.revalidate();
        panel.repaint();
    }

    public void saveProjectLocal(Project p) {
        String fileContent = p.toJSONString();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));
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

    public void saveImage() {
        if (editorPanel != null) {
            editorPanel.writeImage();
        }
    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));
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

    public void signInErrorMessage() {
        JOptionPane.showMessageDialog(this, "Sign in to use this feature", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean hasEditorPanel() {
        return editorPanel != null;
    }

    public void addEditorPanel() {
        editorPanel = new EditorPanel(this, currentSession.CreateNewProject(), currentSession);
    }

    public void addEditorPanel(Project p) {
        editorPanel = new EditorPanel(this, p, currentSession);
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

    public void createAccountPanel() {
        accountPanel = new AccountPanel(this, currentSession.GetAccountInfo(), currentSession);
    }

    public void goToAccountPanel() {
        createAccountPanel();

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
        searchPanel = new SearchPanel(this);
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
        new SignInWindow(this);
    }

    public void signIn(String username, String password) throws IncorrectPasswordException, NoSuchAccountException, InvalidInputException {
        currentSession.SignIn(username, password);

        menuBar.getMenu(0).getItem(0).setText("Sign Out");
    }

    public void signOut() {
        currentSession.SignOut();

        menuBar.getMenu(0).getItem(0).setText("Sign In");
    }

    public void createAccount(String username, String password, String email) {
        currentSession.CreateNewAccount(username, password, email);
    }


    public static void main(String[] args) throws Exception {
        CurrentSession cs = new CurrentSession();
        new MakeSomeNoiseWindow(cs);
    }
}

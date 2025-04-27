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

/**
 * MakeSomeNoiseWindow is the main window for the application. It can have be set to show an editor, account information, or a search feature
 * @author Ryan Shipp
 */
public class MakeSomeNoiseWindow extends JFrame {

    private CurrentSession currentSession;

    private EditorPanel editorPanel = null;
    private AccountPanel accountPanel = null;
    private SearchPanel searchPanel = null;
    private boolean adminOn = false;
    private JPanel currentPanel = null;

    private JMenuBar menuBar;

    /**
     * Creates a new window with all its elements. 
     * @param currentSession the user's app session that this window is tied to and can interact with
     */
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

        // Create and populate drop-down menu for project-searching functions
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

    /**
     * Checks if the user is currently signed into an administrator account
     * @return true if user is signed in as admin, false if not
     */
    public boolean isAdmin(){
        return this.adminOn;
    }

    /**
     * Return the signed-in user's user ID number
     * @return the user ID integer if the user is signed in, or null if the user is not signed in
     */
    public Integer getSignedIn(){
        try {
            return this.currentSession.getSignedIn();
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Save a project as a json file to the local disk
     * This launches a file navigator for the user to select the location and name of their saved file
     * @param p the project to be saved
     */
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

    /**
     * Delete a project in the database
     * This will prompt the user with a dialog box to confirm they want to delete the project
     * @param accID the account ID number of the user requesting the deletion
     * @param projID the project ID number of the project to be deleted
     */
    public void deleteProject(Integer accID, int projID) {
        int userChoice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this project? This will clear the current canvas and layers, and delete the remote save");

        if (userChoice == JOptionPane.YES_OPTION) {
            CurrentSession.DeleteProject(accID, projID);
            addEditorPanel();
        }
    }

    /**
     * Request the current editor panel to save the project as a png image
     * If there is no current editor panel, does nothing
     */
    public void saveImage() {
        if (editorPanel != null) {
            editorPanel.writeImage();
        }
    }

    /**
     * Opens a json file and parses it to restore a saved project, then opens the project in a new editor panel
     */
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

    /**
     * Tell the user that they need to sign in the access whichever feature they tried to use
     */
    public void signInErrorMessage() {
        JOptionPane.showMessageDialog(this, "Sign in to use this feature", "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Check if this window currently has an {@link EditorPanel}
     * @return true if the window does have an editor panel and false if it does not
     */
    public boolean hasEditorPanel() {
        return editorPanel != null;
    }

    /**
     * Create a new {@link EditorPanel} with an empty {@link Project} and assign it to this window
     */
    public void addEditorPanel() {
        editorPanel = new EditorPanel(this, currentSession.CreateNewProject(), currentSession);
    }

    /**
     * Create a new {@link EditorPanel} that is already populated with a {@link Project} and assign it to this window
     * @param p the {@link Project} to create the editor panel from
     */
    public void addEditorPanel(Project p) {
        editorPanel = new EditorPanel(this, p, currentSession);
    }

    /**
     * Go to the {@link EditorPanel} for this window. If the window does not have an editor, create a new one with an empty project
     */
    public void goToEditorPanel() {
        if (!this.hasEditorPanel()) {
            addEditorPanel();
        }

        if (currentPanel != null) {
            remove(currentPanel);
        }

        editorPanel.renderNoise();
        currentPanel = editorPanel;
        add(currentPanel);
        revalidate();
        repaint();
    }

    /**
     * Check if this window currently has an {@link AccountPanel}
     * @return true if the window does have an account panel and false if it does not
     */
    public boolean hasAccountPanel() {
        return accountPanel != null;
    }

    /**
     * Create a new {@link AccountPanel} for the currently signed-in user
     */
    public void createAccountPanel() {
        accountPanel = new AccountPanel(this, currentSession.GetAccountInfo(), currentSession);
    }

    /**
     * Create a new {@link AccountPanel} for the current session and go to it
     */
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

    /**
     * Check if this window currently has a {@link SearchPanel}
     * @return true if the window does have a search panel and false if it does not
     */
    public boolean hasSearchPanel() {
        return searchPanel != null;
    }

     /**
     * Create a new {@link SearchPanel} 
     */
    public void addSearchPanel() {
        searchPanel = new SearchPanel(this);
    }

     /**
     * Go to the {@link SearchPanel} for this window. If the window does not have an search panel, create a new one
     */
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

    /**
     * Create a {@link SignInWindow} so the user can sign in to the current session
     */
    public void createSignInWindow() {
        new SignInWindow(this);
    }

    /**
     * Attempt to sign the user into an account in this window's {@link CurrentSession}
     * @param username the username of the account to sign into
     * @param password the password of the account to sign into
     * @throws IncorrectPasswordException if the given password does not match the password saved to the given username
     * @throws NoSuchAccountException if the given username does not exist in the databse
     * @throws InvalidInputException if the given username or password are null, empty, or whitespace
     */
    public void signIn(String username, String password) throws IncorrectPasswordException, NoSuchAccountException, InvalidInputException {
        currentSession.SignIn(username, password);
        this.adminOn = currentSession.isAdmin();
        menuBar.getMenu(0).getItem(0).setText("Sign Out");
        if (currentPanel == accountPanel) goToAccountPanel();
    }

    /**
     * Sign the user out of their current account and reload this window's {@link AccountPanel}
     */
    public void signOut() {
        currentSession.SignOut();
        menuBar.getMenu(0).getItem(0).setText("Sign In");
        if (currentPanel == accountPanel) goToAccountPanel();
    }

    /**
     * Create a new account in the database
     * @param username the username for the new account
     * @param password the password for the new account
     * @param email the email for the new account
     */
    public void createAccount(String username, String password, String email) {
        currentSession.CreateNewAccount(username, password, email);
    }

    /** 
     * main method to create a new {@link CurrentSession} and launch a new window
     */
    public static void main(String[] args) throws Exception {
        CurrentSession cs = new CurrentSession();
        new MakeSomeNoiseWindow(cs);
    }
}

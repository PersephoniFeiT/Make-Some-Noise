package FrontEnd;

import BackEnd.Accounts.CurrentSession;
import BackEnd.Accounts.Project;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

public class DeleteButton extends JButton {
    CurrentSession cs;
    Integer projectID;
    public DeleteButton(CurrentSession cs, Integer projectID){
        super();
        this.cs = cs;
        this.projectID = projectID;

        this.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                cs.DeleteProject(projectID);
            }
        });
    }
}

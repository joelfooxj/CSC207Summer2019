package View;

import Control.HrCommandHandler;
import Model.JobApplication;
import Model.JobPosting;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;

public class HRJobOptionsForm extends JDialog {
    private JPanel contentPane;
    private JList jobsList;
    private JList associatedApplicationsList;
    private JButton creatJobButton;
    private JButton hireButton;
    private JButton rejectButton;
    private JButton exitButton;

    private HrCommandHandler hrCH;
    private HashMap<String, JobPosting> jobListLink = new HashMap<>();
    private HashMap<String, JobApplication> jobApplicationListLink = new HashMap<>();

    public HRJobOptionsForm(HrCommandHandler inHRCH, String inTitle) {
        setContentPane(contentPane);
        setModal(true);

        this.jobsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.jobsList.setLayoutOrientation(JList.VERTICAL);
        this.associatedApplicationsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.associatedApplicationsList.setLayoutOrientation(JList.VERTICAL);

        this.hrCH = inHRCH;
        this.contentPane.setBorder(BorderFactory.createTitledBorder(inTitle + " Job Options"));


//        buttonOK.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//
//        buttonCancel.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

    }

    private void updateForm(){
        //get jobs, list out jobs for each application
        // List<JobPosting> inJobs = this.hrCH.getOpenJobs();
//        for (JobPosting job: inJobs){
//            jobListLink.put(job.toString(), job);
//        }
//        this.jobsList.setListData(jobListLink.keySet().toArray());
//        this.jobsList.setSelectedIndex(0);
//        JobPosting selectedJob = jobListLink.get(this.jobsList.getSelectedValue());


    }

}

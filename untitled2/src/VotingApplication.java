import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VotingApplication extends JFrame {

    private JFrame frame;
    private JPanel homePanel;
    private JPanel votingPanel;
    private JPanel resultsPanel;

    private JTextField nameField;
    private JTextField aadharField;
    private JTextField otpField;
    private JTextField passwordField;
    private JButton startButton;
    private JButton submitButton;
    private JButton checkResultsButton;
    private JLabel resultLabel;
    private Map<String, Integer> votes = new HashMap<>();
    private int otp;

    public VotingApplication() {
        initComponents();
    }

    private void initComponents() {
        frame = new JFrame("Online Voting System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Home Panel
        homePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    URL url = new URL("http://www.happywalagift.com/wp-content/uploads/2015/08/indian-flag-photos-hd-wallpapers-download-free-15-Aug-2015.jpg"); // Replace with your image URL
                    Image image = ImageIO.read(url);
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        homePanel.setLayout(new BorderLayout());

        // Title Label with Bold Font
        JLabel titleLabel = new JLabel("<html><b>Online Voting Application</b></html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Adjust font size and style as needed

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startButton = new JButton("Start Now");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateOTP();
                showVotingPanel();
            }
        });
        buttonPanel.add(startButton);
        homePanel.add(titleLabel, BorderLayout.NORTH);
        homePanel.add(buttonPanel, BorderLayout.SOUTH);
        //


        // Voting Panel
        votingPanel = new JPanel();
        votingPanel.setLayout(new GridLayout(0, 1));

        JPanel inputPanel= new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("Enter Name: "));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Enter Aadhar No.: "));
        aadharField = new JTextField();
        inputPanel.add(aadharField);
        inputPanel.add(new JLabel("Enter OTP: "));
        otpField = new JTextField();
        inputPanel.add(otpField);

        JPanel partyPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton party1RadioButton = new JRadioButton("BJP", createImageIcon("https://tse3.mm.bing.net/th?id=OIP.2Ly_8s9lMiQbBCTmL5mRNAHaHa&pid=Api&P=0&h=180"));
        partyPanel.add(party1RadioButton);
        buttonGroup.add(party1RadioButton);
        JRadioButton party2RadioButton = new JRadioButton("CONGRESS", createImageIcon("https://tse1.mm.bing.net/th?id=OIP.CUwTHhdJGUE1AtxVsxNcigHaHa&pid=Api&P=0&h=180"));
        partyPanel.add(party2RadioButton);
        buttonGroup.add(party2RadioButton);
        JRadioButton party3RadioButton = new JRadioButton("CPI(M)", createImageIcon("https://tse4.mm.bing.net/th?id=OIP.Q8x6czZtEh11nMh2NuP-xwHaHc&pid=Api&P=0&h=180"));
        partyPanel.add(party3RadioButton);
        buttonGroup.add(party3RadioButton);

        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitButton = new JButton("Submit Your Vote");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyOTP();
            }
        });
        buttonPanel2.add(submitButton);

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        checkResultsButton = new JButton("Check Results");
        checkResultsButton.setVisible(false); // Initially not visible
        checkResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
        resultPanel.add(checkResultsButton);

        votingPanel.add(inputPanel);
        votingPanel.add(partyPanel);
        votingPanel.add(buttonPanel2);
        votingPanel.add(resultPanel);

        frame.add(homePanel, BorderLayout.CENTER);
    }

    private void showVotingPanel() {
        frame.remove(homePanel);
        frame.add(votingPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void generateOTP() {
        otp = new Random().nextInt(9000) + 1000;
        JOptionPane.showMessageDialog(null, "Your OTP is: " + otp);
    }

    private void verifyOTP() {
        try {
            int userOTP = Integer.parseInt(otpField.getText());
            if (userOTP == otp) {
                submitVote();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect OTP. Please try again.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid OTP.");
        }
    }

    private void submitVote() {
        String name = nameField.getText();
        String aadhar = aadharField.getText();
        if (name.isEmpty() || aadhar.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter Name and Aadhar No.");
        } else if (!aadhar.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(null, "Invalid Aadhar No. Please enter a 12-digit number.");
        } else {
            JRadioButton selectedParty = getSelectedParty();
            if (selectedParty != null) {
                castVote(selectedParty.getText());
                clearFields();
                checkResultsButton.setVisible(true); // Make checkResultsButton visible after vote submission
            } else {
                JOptionPane.showMessageDialog(null, "Please select a party to vote.");
            }
        }
    }

    private JRadioButton getSelectedParty() {
        for (Component comp : votingPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component innerComp : panel.getComponents()) {
                    if (innerComp instanceof JRadioButton) {
                        JRadioButton radioButton = (JRadioButton) innerComp;
                        if (radioButton.isSelected()) {
                            return radioButton;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void castVote(String candidate) {
        votes.put(candidate, votes.getOrDefault(candidate, 0) + 1);
        JOptionPane.showMessageDialog(null, "Vote submitted successfully for " + candidate);
    }

    private void authenticate() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter password:");
        JPasswordField passField = new JPasswordField(10);
        panel.add(label);
        panel.add(passField);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Authentication",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        if (option == 0) // pressing OK button
        {
            char[] password = passField.getPassword();
            if (String.valueOf(password).equals("9870")) {
                displayResults();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect password. Please try again.");
            }
        }
    }

    private void displayResults() {
        String winner = "";
        int maxVotes = 0;
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                winner = entry.getKey();
            }
        }
        if (!winner.isEmpty()) {
            try {
                URL url = new URL("https://tse1.mm.bing.net/th?id=OIP.XxP1fAuuU1zl_dWorWzG4gHaHa&pid=Api&P=0&h=180"); // Placeholder image URL
                Image image = ImageIO.read(url);
                ImageIcon partyImage = new ImageIcon(image);
                JPanel resultsPanel = new JPanel(new BorderLayout());
                JLabel winnerLabel = new JLabel(winner, partyImage, JLabel.CENTER);
                resultsPanel.add(winnerLabel, BorderLayout.CENTER);
                JOptionPane.showMessageDialog(null, resultsPanel, "Election Results", JOptionPane.PLAIN_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "No votes cast yet.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        aadharField.setText("");
        otpField.setText("");
    }

    private ImageIcon createImageIcon(String url) {
        try {
            URL imgUrl = new URL(url);
            Image image = ImageIO.read(imgUrl);
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VotingApplication().frame.setVisible(true);
            }
        });
    }
}

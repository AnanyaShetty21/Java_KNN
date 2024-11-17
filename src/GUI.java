import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;


public class GUI extends JFrame {
    String csvFilePath;
    int size = 590541;
    double[][] array;
    knn Knn;
    double[] values;
    int predDataFlag = 0;

    public GUI() {
        setTitle("KNN Machine Learning Algorithm in JAVA");
        setSize(450, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        // title
        JLabel titleLabel = new JLabel("KNN Machine Learning Algorithm in JAVA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        //input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));
        inputPanel.setBackground(Color.WHITE);


        JTextField pathField = new JTextField();
        JTextField predictionDataField = new JTextField();
        JTextField kValueField = new JTextField();
        setPlaceholder(pathField, "Enter file path");
        setPlaceholder(predictionDataField, "Enter prediction data");
        setPlaceholder(kValueField, "Enter K value");
        JButton pathButton = setButton("Update");
        JButton predictionDataButton = setButton("Update");
        JButton kValueButton = setButton("Update");



        inputPanel.add(pathField);
        inputPanel.add(pathButton);
        inputPanel.add(predictionDataField);
        inputPanel.add(predictionDataButton);
        inputPanel.add(kValueField);
        inputPanel.add(kValueButton);

        //selected panel
        JPanel selectedPanel = new JPanel();
        selectedPanel.setLayout(new GridLayout(3, 1, 5, 5));
        selectedPanel.setBackground(new Color(250, 250, 250));


        JLabel pathLabel = new JLabel("Selected Path: Path value here");
        JLabel predictionDataLabel = new JLabel("Prediction Data: Data value here");
        JLabel kValueLabel = new JLabel("K Value: K value here");
        selectedPanel.add(pathLabel);
        selectedPanel.add(predictionDataLabel);
        selectedPanel.add(kValueLabel);

        //result panel
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(3, 1, 5, 5));
        resultPanel.setBackground(new Color(250, 250, 250));
        resultPanel.setBorder(new LineBorder(new Color(255, 182, 193), 2, true));



        pathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String destinationText[];
                destinationText = pathLabel.getText().split(": ");

                try{
                    File file = new File(pathField.getText());
                    if(file.exists()){
                        pathLabel.setText(destinationText[0]+": "+pathField.getText());
                        csvFilePath = pathField.getText();

                    }else{
                        throw new FileNotFoundException();
                    }
                }catch(FileNotFoundException fe){
                    JOptionPane.showMessageDialog(GUI.this, "File not found");
                }

            }
        });

        predictionDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] destinationText;

                try {

                    String inputText = predictionDataField.getText();
                    String[] inputValues = inputText.split(",");

                    // Regex to validate each number (allowing decimals)
                    String regex = "^[+-]?\\d*\\.?\\d+$";

                    for (String value : inputValues) {
                        if (!value.trim().matches(regex)) {
                            throw new NumberFormatException(); // Trigger exception if invalid
                        }
                    }

                    // If all values are valid, parse them into double array
                    values = Arrays.stream(inputValues).mapToDouble(Double::parseDouble).toArray();

                    destinationText = predictionDataLabel.getText().split(": ");
                    predictionDataLabel.setText(destinationText[0] + ": " + inputText);
                    predDataFlag = 1;

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GUI.this, "Invalid prediction data. Enter numeric values separated by commas.");
                }
            }
        });




        kValueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String destinationText[];

                try{
                    int k = Integer.parseInt(kValueField.getText());
                    if(k>0)
                        Knn = new knn(k);
                    else
                        throw new NumberFormatException();
                    destinationText = kValueLabel.getText().split(": ");
                    kValueLabel.setText(destinationText[0]+": "+kValueField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GUI.this, "Invalid K value. Enter an integer greater than 0.");
                }


            }
        });



        JLabel resultLabel = new JLabel("Prediction Result: Result here");
        JLabel javaLabel = new JLabel("Java Runtime: Java runtime here");
        resultPanel.add(resultLabel);
        resultPanel.add(javaLabel);

        //run button
        JButton runButton = setButton("Run and Predict");
        runButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        runButton.setPreferredSize(new Dimension(200, 40));

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (csvFilePath == null || csvFilePath.isEmpty()) {
                    JOptionPane.showMessageDialog(GUI.this, "Please provide a valid CSV file path.");
                    return;
                }
                if (predDataFlag == 0) {
                    JOptionPane.showMessageDialog(GUI.this, "Please provide valid prediction data.");
                    return;
                }
                if (Knn == null) {
                    JOptionPane.showMessageDialog(GUI.this, "Please set a valid K value.");
                    return;
                }

                long startTime = System.currentTimeMillis();
                array = Utils.convertCSVtoArray(csvFilePath, size);
                Knn.fit(array);
                double result = Knn.predict(values);
                double runtime = (System.currentTimeMillis() - startTime)/1000.0;
                resultLabel.setText("Prediction Result: "+result);
                javaLabel.setText("Java runtime: "+runtime);

            }
        });


        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(selectedPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(resultPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(runButton);

        add(mainPanel);
        setVisible(true);
    }

    // placeholders for the text fields
    private void setPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }




    private JButton setButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        return button;
    }








    public static void main(String[] args) {
        new GUI();
    }
}


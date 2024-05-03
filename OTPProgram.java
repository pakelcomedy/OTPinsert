import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OTPProgram extends JFrame {
    private JTextField[] otpFields;
    private final int boxSize = 50; // Define the size of the OTP box

    public OTPProgram() {
        setTitle("OTP Input");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel otpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        otpPanel.setBackground(Color.WHITE);
        otpPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        otpFields = new JTextField[6];
        for (int i = 0; i < 6; i++) {
            final int index = i; // Create a final variable for the lambda
            otpFields[i] = new JTextField(1);
            otpFields[i].setFont(new Font("Arial", Font.BOLD, 24));
            otpFields[i].setHorizontalAlignment(JTextField.CENTER);
            otpFields[i].setMargin(new Insets(5, 5, 5, 5));
            otpFields[i].setPreferredSize(new Dimension(boxSize, boxSize)); // Set the size of the OTP box

            // Add a document filter to restrict each text field to contain only one digit
            ((AbstractDocument) otpFields[i].getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    String newText = otpFields[index].getText().substring(0, offset) + text + otpFields[index].getText().substring(offset + length);
                    if (newText.length() <= 1 && newText.matches("[0-9]*")) {
                        super.replace(fb, offset, length, text, attrs);
                        focusNextTextField(index);
                    }
                }
            });

            otpFields[i].addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && otpFields[index].getText().isEmpty() && index > 0) {
                        otpFields[index - 1].requestFocus();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });

            otpPanel.add(otpFields[i]);
        }

        mainPanel.add(otpPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        pack(); // Pack the frame to match the size of the OTP boxes
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    private void focusNextTextField(int currentIndex) {
        if (currentIndex < 5 && otpFields[currentIndex].getText().length() == 1) {
            otpFields[currentIndex + 1].requestFocus();
        } else if (currentIndex == 5 && otpFields[currentIndex].getText().length() == 1) {
            submitOTP();
        }
    }

    private void submitOTP() {
        StringBuilder otp = new StringBuilder();
        for (JTextField field : otpFields) {
            otp.append(field.getText());
        }
        JOptionPane.showMessageDialog(this, "OTP submitted: " + otp.toString());
        System.out.println("OTP submitted: " + otp.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OTPProgram::new);
    }
}
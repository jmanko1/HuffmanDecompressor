package graphicalUserInterface;

import javax.swing.*;

public class MyButton extends JButton {

    public MyButton(String text) {
        super(text);

        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
    }
}

import javax.swing.*;
import java.awt.*;

/*
Created by Ivan Savenko. Most functionality implemented 11.06
*/
public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PasswordGeneratorUI().setVisible(true);
            }
        });
    }
}

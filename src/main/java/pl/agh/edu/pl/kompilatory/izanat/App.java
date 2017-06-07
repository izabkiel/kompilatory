package pl.agh.edu.pl.kompilatory.izanat;


import javax.swing.*;

public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new GUI().displayGUI();
            }
        });

    }
}

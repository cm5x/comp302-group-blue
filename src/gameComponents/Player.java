package gameComponents;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Player {
    private int playerChance = 3;
    public String username;
    private String password;


    public Player(String username, String password){
        this.username = username;
        this.password = password;
    }

public void setChances(int set){
    this.playerChance = set;
}

public int getChances(){
    return this.playerChance;
}

public void setName(String newname){
    this.username = newname;
}

public String getName(){
    return this.username;
}

public void setpass(String newpass){
    this.password = newpass;
}

public String getPass(){
    return this.password;
}

public void decChance(JPanel chancePanel, ArrayList<JLabel> labels){
    this.playerChance = playerChance - 1;
    if (!labels.isEmpty()) {
            JLabel label = labels.remove(labels.size() - 1);
            chancePanel.remove(label);
            chancePanel.revalidate();
            chancePanel.repaint();

            }

    if(this.getChances() == 0){
                    JOptionPane.showMessageDialog(null, "Game Over!", "Message", JOptionPane.PLAIN_MESSAGE);

                }
    
}   
}


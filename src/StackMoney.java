import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StackMoney {

    public static void main(String[] args) {

        JFrame janela = new JFrame("Assistente de Pote");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(400, 300);
        janela.setLayout(new BorderLayout(10, 10));

        JPanel stackHeroi;
        stackHeroi = new JPanel(new GridLayout(4, 2, 5, 5));

        JLabel poteHeroi = new JLabel("Pote do Herói: ");
        JTextField campoPoteHeroi = new JTextField(5);

        JLabel poteVilao = new JLabel("Pote do Vilão: ");
        JTextField campoPoteVilao = new JTextField(5);

        JLabel poteAtualMesa = new JLabel("Valor do pote mesa: ");
        JTextField campoPoteMesa = new JTextField(5);

        JButton botaoAnalisePoteTotal = new JButton("Calcular Pote:");

        stackHeroi.add(poteHeroi);
        stackHeroi.add(campoPoteHeroi);

        stackHeroi.add(poteVilao);
        stackHeroi.add(campoPoteVilao);

        stackHeroi.add(poteAtualMesa);
        stackHeroi.add(campoPoteMesa);

        stackHeroi.add(botaoAnalisePoteTotal);
        stackHeroi.add(new JPanel());


        botaoAnalisePoteTotal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double stackHeroi = Double.parseDouble(campoPoteHeroi.getText());
                    double stackVilao = Double.parseDouble(campoPoteVilao.getText());
                    double poteMesa = Double.parseDouble(campoPoteMesa.getText());


                    double min = Math.min(stackHeroi, stackVilao);

                    try {
                        double spr = min / poteMesa;


                        resultadoFinal += String.format(" ")

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog();
                    }

                }

            }
        }

    }
}
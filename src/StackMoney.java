import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class StackMoney {

    public static void main(String[] args) {

        JFrame janela = new JFrame("Assistente de Pote");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(400, 300);
        janela.setLayout(new BorderLayout(10, 10));

        JPanel valorStackHeroi;
        valorStackHeroi = new JPanel(new GridLayout(4, 2, 5, 5));

        JLabel poteHeroi = new JLabel("Pote do Herói: ");
        JTextField campoPoteHeroi = new JTextField(5);

        JLabel poteVilao = new JLabel("Pote do Vilão: ");
        JTextField campoPoteVilao = new JTextField(5);

        JLabel poteAtualMesa = new JLabel("Valor do pote mesa: ");
        JTextField campoPoteMesa = new JTextField(5);

        JButton botaoAnalisePoteTotal = new JButton("Calcular Pote:");

        valorStackHeroi.add(poteHeroi);
        valorStackHeroi.add(campoPoteHeroi);

        valorStackHeroi.add(poteVilao);
        valorStackHeroi.add(campoPoteVilao);

        valorStackHeroi.add(poteAtualMesa);
        valorStackHeroi.add(campoPoteMesa);

        valorStackHeroi.add(botaoAnalisePoteTotal);
        valorStackHeroi.add(new JPanel());

        janela.add(valorStackHeroi, BorderLayout.CENTER);


        botaoAnalisePoteTotal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double valorStackHeroi = Double.parseDouble(campoPoteHeroi.getText());
                    double stackVilao = Double.parseDouble(campoPoteVilao.getText());
                    double poteMesa = Double.parseDouble(campoPoteMesa.getText());

                    double min = Math.min(valorStackHeroi, stackVilao);
                    double spr = min / poteMesa;


                    String resultado = String.format("Stack: $%.2f", spr);
                    JOptionPane.showMessageDialog(janela, resultado, "Resultado:", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(janela, "Digite somente números", "ERRO" ,JOptionPane.ERROR_MESSAGE);
                }

            }

        });
        janela.setVisible(true);

    }
}
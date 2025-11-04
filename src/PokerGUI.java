import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PokerGUI {

        public static void main(String[] args) {

        //JFrame é uma classe da biblioteca Swing em Java usada para criar janelas de aplicações desktop
        JFrame janela = new JFrame("Menu Assistente de Poker");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(600,400);

        JPanel painel = new JPanel();

        painel.setLayout(new FlowLayout());

        JLabel etiquetaPosicao = new JLabel("Sua posição:");

        JTextField campoPosicao = new JTextField(5);

        JLabel etiquetaMao  = new JLabel("Sua mão (ex: AKs, 77, T9o...):");

        JTextField campoMao = new JTextField(5);

        JButton botaoAnalisar = new JButton("Analisar Pré-Flop!");

        painel.add(etiquetaPosicao);
        painel.add(campoPosicao);
        painel.add(etiquetaMao);
        painel.add(campoMao);
        painel.add(botaoAnalisar);

        janela.add(painel);

        janela.setVisible(true);

        System.out.println("GUI com componentes iniciada");


        botaoAnalisar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String textoPosicao = campoPosicao.getText();
            }
        });
    }



}
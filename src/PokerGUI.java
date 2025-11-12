import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class PokerGUI {

    private static GtoAdvisor advisorGTO = new GtoAdvisor();

    public static void main(String[] args) {

        JFrame janela = new JFrame("Meu Assistente de Poker");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(800, 600);
        janela.setLayout(new BorderLayout(10,10));

        JPanel painelPreFlop;
        painelPreFlop = new JPanel(new GridLayout(3, 2, 5, 5));

        JLabel etiquetaPosicao = new JLabel("Sua posição: ");
        JTextField campoPosicao = new JTextField(5);
        JLabel etiquetaMao = new JLabel("Sua mão: ");
        JTextField campoMao = new JTextField(5);
        JButton botaoAnalisarPreFlop = new JButton("Analisar Pré-Flop:");

        painelPreFlop.add(etiquetaPosicao);
        painelPreFlop.add(campoPosicao);
        painelPreFlop.add(etiquetaMao);
        painelPreFlop.add(campoMao);
        painelPreFlop.add(botaoAnalisarPreFlop);
        painelPreFlop.add(new JPanel());

        //JLabel mostra coisas para o usuario. resumindo é o que você VÊ.
        //JPanel Agrupar, organizar e conter componentes como JLabel, JButton e JTextField. resumindo é onde você PÕE

        JPanel painelMesa = new JPanel(new GridLayout(3, 4, 5, 5));

        painelMesa.add(new JLabel("Flop 1: "));
        painelMesa.add(new JLabel("Flop 2: "));
        painelMesa.add(new JLabel("Flop 3: "));
        painelMesa.add(new JLabel("Turn: "));

        JTextField campoFlop1 = new JTextField(3);
        JTextField campoFlop2 = new JTextField(3);
        JTextField campoFlop3 = new JTextField(3);
        JTextField campoTurn = new JTextField(3);

        painelMesa.add(new JLabel("River: "));

        JTextField campoRiver = new JTextField(3);

        JButton botaoAnalisarPostFlop = new JButton("Analisar Pós-Flop");

        painelMesa.add(campoFlop1);
        painelMesa.add(campoFlop2);
        painelMesa.add(campoFlop3);
        painelMesa.add(campoTurn);
        painelMesa.add(campoRiver);
        painelMesa.add(botaoAnalisarPostFlop);
        painelMesa.add(new JPanel());
        painelMesa.add(new JPanel());

        JPanel painelResultados = new JPanel(new GridLayout(2, 1));

        final JLabel labelResultadoGTO = new JLabel("Aguardando análise GTO...", SwingConstants.CENTER);
        labelResultadoGTO.setFont(new Font("Arial", Font.BOLD, 16));

        final JLabel labelResultadoPosFlop = new JLabel("Aguardando análise Pós-Flop", SwingConstants.CENTER);
        labelResultadoPosFlop.setFont(new Font("Arial", Font.PLAIN, 14));

        painelResultados.add(labelResultadoGTO);
        painelResultados.add(labelResultadoPosFlop);

        botaoAnalisarPreFlop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String textoPosicao = campoPosicao.getText().trim().toUpperCase();
                    String textoMao = campoMao.getText().trim().toUpperCase();
                    Posicao posicao = Posicao.valueOf(textoPosicao);
                    AcaoGTO acaoGTO = advisorGTO.getAcao(posicao, textoMao);

                    labelResultadoGTO.setText("Ação GTO: " + acaoGTO);
                }  catch (Exception ex) {
                    labelResultadoGTO.setText("ERRO: Posição ou mão inválida.");
                }
            }
        });

        botaoAnalisarPostFlop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String f1 = campoFlop1.getText();
                String f2 = campoFlop2.getText();
                String f3 = campoFlop3.getText();
                String t = campoTurn.getText();
                String r = campoRiver.getText();

                labelResultadoPosFlop.setText("Analisando Mesa: " + f1 + "," + f2 + "," + f3 + "," + t + "," + r);
            }
        });

        janela.add(painelPreFlop, BorderLayout.NORTH);
        janela.add(painelMesa, BorderLayout.CENTER);
        janela.add(painelResultados, BorderLayout.SOUTH);

        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }
}
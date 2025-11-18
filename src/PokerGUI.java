import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class PokerGUI {

    private static GtoAdvisor advisorGTO = new GtoAdvisor();
    private static HandEvaluator avaliador = new HandEvaluator();
    private static OutsCalculator calculadoraDeOuts = new OutsCalculator(avaliador);
    private static EquityCalculator calculadoraEquidade = new EquityCalculator(avaliador);

    public static void main(String[] args) {

        JFrame janela = new JFrame("Meu Assistente de Poker");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(800, 600);
        janela.setLayout(new BorderLayout(10,10));

        JPanel painelPreFlop;
        painelPreFlop = new JPanel(new GridLayout(4, 2, 5, 5));

        JLabel etiquetaPosicao = new JLabel("Sua posição (UTG, MP...): ");
        JTextField campoPosicao = new JTextField(5);

        JLabel etiquetaHeroi1 = new JLabel("Herói - Carta 1 (ex:AC): ");
        JTextField campoHeroi1 = new JTextField(5); //AC = AS DE COPAS

        JLabel etiquetaHeroi2 = new JLabel("Herói - Carta 2 (ex: KH): ");
        JTextField campoHeroi2 = new JTextField(5); // KH = REI DE COPAS ("H" é o HEART)

        //(Nota: Vamos usar 'H' (Hearts) e 'D' (Diamonds) para Copas e Ouros para ficar no padrão GTO)

        JButton botaoAnalisarPreFlop = new JButton("Analisar Pré-Flop:");

        painelPreFlop.add(etiquetaPosicao);
        painelPreFlop.add(campoPosicao);

        painelPreFlop.add(etiquetaHeroi1);
        painelPreFlop.add(campoHeroi1);

        painelPreFlop.add(etiquetaHeroi2);
        painelPreFlop.add(campoHeroi2);

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
                    String textoC1 = campoHeroi1.getText().trim();
                    String textoC2 = campoHeroi2.getText().trim();

                    Carta c1 = CardParser.parse(textoC1);
                    Carta c2 = CardParser.parse(textoC2);

                    Posicao posicao = Posicao.valueOf(textoPosicao);

                    if (c1 == null || c2 == null) {
                        throw new IllegalArgumentException("Formato de carta inválida.");
                        // throw é usada para lançar explicitamente uma exceção a partir de um metodo ou bloco de código.
                        //  Ela é usada quando você quer interromper o fluxo normal do programa e notificar sobre uma condição de erro,
                        //  permitindo que o controle seja transferido para o bloco try-catch mais próximo ou para o chamador do metodo
                    }

                    PreflopHand maoPreFlop = new PreflopHand(c1, c2);

                    AcaoGTO acao = advisorGTO.getAcao(posicao, maoPreFlop.getNotacao());

                    labelResultadoGTO.setText("Mão " + maoPreFlop.getNotacao() + " | Ação GTO: " + acao);

                    // catch é usado para tratar exceções que ocorrem dentro de um bloco try. Ele define o que fazer quando uma exceção específica acontece,
                    // permitindo que o programa não termine abruptamente e continue sua execução.
                } catch (Exception ex) {
                    labelResultadoGTO.setText("ERRO: Verifique a posição ou formato das cartas");
                }

            }
        });

        botaoAnalisarPostFlop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Carta c1 = CardParser.parse(campoHeroi1.getText());
                    Carta c2 = CardParser.parse(campoHeroi2.getText());

                    if (c1 == null || c2 == null) {
                        throw new IllegalArgumentException("Preencha as 2 cartas do Herói e pelo menos o flop.");
                    }

                    List<Carta> maoHeroi = List.of(c1, c2);

                    Carta f1 = CardParser.parse(campoFlop1.getText());
                    Carta f2 = CardParser.parse(campoFlop2.getText());
                    Carta f3 = CardParser.parse(campoFlop3.getText());


                    List<Carta> cartasDaMesa = new ArrayList<>();
                    if (f1 != null) cartasDaMesa.add(f1);
                    if (f2 != null) cartasDaMesa.add(f2);
                    if (f3 != null) cartasDaMesa.add(f3);

                    if(cartasDaMesa.size() < 3) {
                        throw new IllegalArgumentException("Preencha pelo menos as 3 cartas do flop");
                    }

                    Carta t = CardParser.parse(campoTurn.getText());
                    Carta r = CardParser.parse(campoRiver.getText());
                    if (t != null) cartasDaMesa.add(t);
                    if (r != null) cartasDaMesa.add(r);



                    List<Carta> cartasCompletas = new ArrayList<>(maoHeroi);
                    cartasCompletas.addAll(cartasDaMesa);
                    ResultadoMao maoAtual = avaliador.avaliarMao(cartasCompletas);

                    int NUMERO_SIMULACOES = 30000;
                    double equidade = calculadoraEquidade.calcularEquidade(maoHeroi, cartasDaMesa,NUMERO_SIMULACOES);

                    String resultadoFinal = String.format(
                            "Mão atual: %s | Equidade: %.2f%%",
                            maoAtual.getTipoMao(), (equidade * 100)
                    );

                    if (cartasDaMesa.size() < 5) {
                        DrawAnalysis analiseOuts = calculadoraDeOuts.calcularDraws(maoHeroi, cartasDaMesa);
                        String resumoOuts = analiseOuts.getResumo();
                        int totalUnicoOuts = analiseOuts.getTotalUnicoOuts();

                        int chance = (cartasDaMesa.size() == 3) ? (totalUnicoOuts * 4) : (totalUnicoOuts * 2);

                        resultadoFinal += String.format("| Outs (%d): %s (aprox. %d%%)", totalUnicoOuts, resumoOuts, chance);
                    }

                    labelResultadoPosFlop.setText(resultadoFinal);

                } catch (Exception ex) {
                    labelResultadoPosFlop.setText("ERRO: " + ex.getMessage());
                }
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
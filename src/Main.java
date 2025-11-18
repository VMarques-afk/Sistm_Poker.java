import java.nio.file.attribute.PosixFileAttributeView;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        HandEvaluator avaliador = new HandEvaluator();
        OutsCalculator calculadoraDeOuts = new OutsCalculator(avaliador);
        EquityCalculator calculadoraEquidade = new EquityCalculator(avaliador);
        GtoAdvisor advisorGTO = new GtoAdvisor();

        Scanner scanner = new Scanner(System.in);

        System.out.println("ASSISTENTE DE POKER (modulo 4)");
        System.out.println("Vamos analisar sua mão");

        System.out.println("Mão do Herói");
        Carta heroiC1 = pedirCarta(scanner, "heroi - Carta 1");
        Carta heroiC2 = pedirCarta(scanner, "heroi - Carta 2");

        List<Carta> maoHeroi = List.of(heroiC1, heroiC2);
        System.out.println("Mão do heroi registrada: " + maoHeroi);


        System.out.println("MODULO GTO (PRÉ FLOP");

        Posicao posicao  = pedirPosicao(scanner, "Digite sua posição");

        PreflopHand maoPreFlop = new PreflopHand(heroiC1, heroiC2);

        AcaoGTO acaoRecomendada = advisorGTO.getAcao(posicao, maoPreFlop.getNotacao());

        System.out.println("=================================");
        System.out.println("Posição " + posicao);
        System.out.println("Mão " + maoPreFlop.getNotacao());
        System.out.println("AÇÃO GTO RECOMENDADA: " + acaoRecomendada);
        System.out.println("==================================");


        System.out.println("Mesa (FLOP)");
        List<Carta> cartasDaMesa = new ArrayList<>();
        cartasDaMesa.add(pedirCarta(scanner, "Flop - Carta 1"));
        cartasDaMesa.add(pedirCarta(scanner, "Flop - Carta 2"));
        cartasDaMesa.add(pedirCarta(scanner, "Flop - Carta 3"));
        System.out.println("Flop registrado: " + cartasDaMesa);

        System.out.println("===Calculando Ánalise Pós-Flop===");
        mostrarAnalise(avaliador, calculadoraDeOuts, calculadoraEquidade, maoHeroi, cartasDaMesa);

        if (perguntasSimNao(scanner, "Deseja adicionar a carta Turn?")) {
            System.out.println("===MESA (TURN)===");
            Carta turn = pedirCarta(scanner, "Turn - Carta 1");
            cartasDaMesa.add(turn);
            System.out.println("Mesa atualizada: " + cartasDaMesa);

            System.out.println("Calculando Análise de Pós-Turn");
            mostrarAnalise(avaliador, calculadoraDeOuts, calculadoraEquidade, maoHeroi,cartasDaMesa);

            if (perguntasSimNao(scanner, "Deseja adicionar a carta do River?")) {
                System.out.println("===Mesa (RIVER)===");
                Carta river = pedirCarta(scanner ,"River - Carta 1");
                cartasDaMesa.add(river);
                System.out.println("Mesa final" + cartasDaMesa);

                System.out.println("Calculando Análise Pós-River");
                mostrarAnalise(avaliador, calculadoraDeOuts, calculadoraEquidade, maoHeroi, cartasDaMesa);

            }

        }
                System.out.println("Análise concluida.");
                scanner.close();

                System.out.println("===Teste modulo GTO===");
                System.out.println("Testando a nova classe PrefloHand...");


                Carta c1_par = new Carta(Naipe.COPAS, Valor.SETE);
                Carta c2_par = new Carta(Naipe.PAUS, Valor.SETE);
                PreflopHand maoPar = new PreflopHand(c1_par, c2_par);
                System.out.println("Mão: 7c, 7p. Resultado esperado: 77. -> Resultado Real: " + maoPar.getNotacao());


                Carta c1_suited = new Carta(Naipe.ESPADAS, Valor.AS);
                Carta c2_suited = new Carta(Naipe.ESPADAS, Valor.REI);
                PreflopHand maoSuited = new PreflopHand(c1_suited, c2_suited);
                System.out.println("Mão: As, Ks. Resultado esperado: AKs. -> Resultado real: " + maoSuited.getNotacao());


                Carta c1_offsuit = new Carta(Naipe.COPAS, Valor.VALETE);
                Carta c2_offsuit = new Carta(Naipe.PAUS, Valor.DAMA);
                PreflopHand maoOffsuit = new PreflopHand(c1_offsuit, c2_offsuit);
                System.out.println("Mão: Jc, Qp. Resultado esperado: QJo. -> Resultado real: " + maoOffsuit.getNotacao());

                System.out.println("=== FIM DO TESTE GTO ====");

    }

    private static Valor pedirValor (Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().toUpperCase();

            try {
                return Valor.valueOf(input);

            } catch (IllegalArgumentException e) {
                System.out.println ("'" + input + ("' não é um valor válido. Tente (DOIS, TRES, .... DAMA, REI, AS"));
            }
        }
    }

    private static Naipe pedirNaipe(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().toUpperCase();
            try {
                return Naipe.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("'" + input + "' não é um naipe válido. Tente ( COPAS, PAUS, OUROS, ESPADAS).");
            }
        }
    }

    private static Carta pedirCarta(Scanner scanner, String nomeDaCarta) {
        System.out.println("Digite a " + nomeDaCarta + ":");
        Valor valor = pedirValor(scanner, " Valor(AS, REI, DAMA, VALETE, 2, 3, 4....): ");
        Naipe naipe = pedirNaipe(scanner, "Naipe (COPAS, PAUS, OUROS, ESPADAS): ");
        return new Carta(naipe, valor);
    }

    private static  Posicao pedirPosicao(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt + "(UTG, MP, CO, BTN, SB, BB):");
            String input = scanner.nextLine().toUpperCase();
            try {
                return Posicao.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("'" + input + "não é uma posição válida");
            }
        }

    }

    private static boolean perguntasSimNao (Scanner scanner, String pergunta) {
        while (true) {
            System.out.println(pergunta + "(S/N): ");
            String input = scanner.nextLine().toUpperCase();
            if (input.equals("S")) {
                return true;
            } else if (input.equals("N")) {
                return false;
            } else {
                System.out.println("Resposta inválida. Por favor, digite 'S' ou 'N' ");
            }
        }

    }

    private static void mostrarAnalise (HandEvaluator avaliador, OutsCalculator calcOuts, EquityCalculator calcEq,List<Carta> maoHeroi, List<Carta> cartasDaMesa) {

        List<Carta> cartasCompletas = new ArrayList<>(maoHeroi);
        cartasCompletas.addAll(cartasDaMesa);

        ResultadoMao maoAtual = avaliador.avaliarMao(cartasCompletas);

        System.out.println("======RESULTADO DA ANALISE (" + cartasCompletas.size() + "cartas totais)======");
        System.out.println("=================================================");

        if (cartasDaMesa.size() < 5) {

            DrawAnalysis analiseOuts = calcOuts.calcularDraws(maoHeroi, cartasDaMesa);
            String resumoOuts = analiseOuts.getResumo();

            int totalUnicoOuts = analiseOuts.getTotalUnicoOuts();


            int chanceProximaCarta = totalUnicoOuts * 2;
            int chanceAteRiver = (cartasDaMesa.size() == 3) ? (totalUnicoOuts * 4) : chanceProximaCarta;

            System.out.println("MODULO 2 (Potencial de melhora):");
            System.out.println(" Outs (cartas que melhoram o TIPO da mão):" + totalUnicoOuts);
            if (cartasDaMesa.size() == 3) {
                System.out.println("Chance de melhorar no turn: aprox." + chanceProximaCarta + "%");
                System.out.println("Chance de melhorar até o River: aprox. " + chanceAteRiver + "%");
            } else {
                System.out.println("Chance de melhorar no River: aprox. " + chanceProximaCarta + "+");
            }
            System.out.println("=============================");
        } else {
            System.out.println("MODULO 2: não há mais cartas para virar");
            System.out.println("==================================");
        }

        System.out.println("MODULO 3 (Chance de vitória total vs Mão Aleatoria): ");
        if (cartasDaMesa.size() < 5) {
            System.out.println(" Calculando equidade...(aguarde)");
        }
        int NUMERO_SIMULACOES = 50000;
        double equidade = calcEq.calcularEquidade(maoHeroi, cartasDaMesa, NUMERO_SIMULACOES);
        System.out.println(String.format("Equidade: %.2f%%",(equidade * 100)));
        System.out.println("==========================================================================");

    }

}

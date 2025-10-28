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

        Scanner scanner = new Scanner(System.in);


        System.out.println("ASSISTENTE DE POKER (modulo 4)");
        System.out.println("Vamos analisar sua mão");

        System.out.println("Mão do Herói");

        Carta heroiC1 = pedirCarta(scanner, "heroi - Carta 1");
        Carta heroiC2 = pedirCarta(scanner, "heroi - Carta 2");

        List<Carta> maoHeroi = List.of(heroiC1, heroiC2);
        System.out.println("Mão do heroi registrada: " + maoHeroi);

        System.out.println("Mesa (FLOP)");
        List<Carta> cartasDaMesa = new ArrayList<>();
        cartasDaMesa.add(pedirCarta(scanner, "Flop - Carta 1"));
        cartasDaMesa.add(pedirCarta(scanner, "Flop - Carta 2"));
        cartasDaMesa.add(pedirCarta(scanner, "Flop - Carta 3"));
        System.out.println("Flop registrado: " + cartasDaMesa);

        /*List<Carta> maoHeroi = new ArrayList<>();

        List<Carta> cartasDaMesa = new ArrayList<>();*/

        System.out.println("calculando");

        List<Carta> cartasCompletas = new ArrayList<>();
        cartasCompletas.addAll(cartasDaMesa);

        ResultadoMao maoAtual = avaliador.avaliarMao(cartasCompletas);

        int outs = calculadoraDeOuts.calcularOuts(maoHeroi, cartasDaMesa);
        int chanceTurn = outs * 2;
        int chanceRiver = outs * 4;

        int NUMERO_SIMULACOES = 100000;
        double equidade = calculadoraEquidade.calcularEquidade(maoHeroi, cartasDaMesa, NUMERO_SIMULACOES);

        System.out.println("=======RESULTADO DA ANALISE==========");
        System.out.println("sua mão atual:" + maoAtual.getTipoMao() + "com valores" + maoAtual.getValoresDaMao());
        System.out.println("----------------------------------------");
        System.out.println("MODULO 2 (potencial de melhora");
        System.out.println("Outs (Cartas que melhoram sua mão): " + outs);
        System.out.println("Chance de melhorar no Turn: aprox. " + chanceTurn + "%");
        System.out.println("Chance de melhorar ate o River: aprox. " + chanceRiver + "%");
        System.out.println("---------------------------------------");
        System.out.println("MODULO 3 (Chance de vitoria total): ");
        System.out.println(String.format("Equidade (Chance de ganhar a mão): %.2f%%", (equidade * 100)));
        System.out.println("============================================");


        System.out.println("Analise Concluida");

        scanner.close();

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
        Valor valor = pedirValor(scanner, " Valor(AS, REI, DAMA, ....): ");
        Naipe naipe = pedirNaipe(scanner, "Naipe (COPAS, PAUS, OUROS, ESPADAS): ");
        return new Carta(naipe, valor);
    }
}

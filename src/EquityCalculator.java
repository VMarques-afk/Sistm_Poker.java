import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;

public class EquityCalculator {

    private final HandEvaluator avaliador;
    private final Random random;

    public EquityCalculator(HandEvaluator avaliador) {
        this.avaliador = avaliador;
        this.random = new Random(); //Random é mais rapido que Shuffle em listas
    }

    public double calcularEquidade(List<Carta> maoHeroi, List<Carta> cartasDaMesa, int numeroDeSimulacoes) {

        List<Carta> cartasConhecidas = new ArrayList<>(maoHeroi);
        cartasConhecidas.addAll(cartasDaMesa);

        int vitoriasHeroi = 0;
        int empates = 0;

        for (int i = 0; i < numeroDeSimulacoes; i++) {

            Baralho baralhoSimulacao = new Baralho();
            List<Carta> baralhoRestante = baralhoSimulacao.getCartas();

            baralhoRestante.removeAll(cartasConhecidas);

            Collections.shuffle(baralhoRestante);

            List<Carta> maoVilao = new ArrayList<>();
            maoVilao.add(baralhoRestante.remove(0));
            maoVilao.add(baralhoRestante.remove(0));

            List<Carta> mesaCompleta = new ArrayList<>(cartasDaMesa);
            int cartasQueFaltamNaMesa = 5 - cartasDaMesa.size();


            for (int j = 0; j < cartasQueFaltamNaMesa; j++) {
                mesaCompleta.add(baralhoRestante.remove(0));
            }

            List<Carta> maoFinalHeroi = new ArrayList<>(maoHeroi);
            maoFinalHeroi.addAll(mesaCompleta);

            List<Carta> maoFinalVilao = new ArrayList<>(maoVilao);
            maoFinalVilao.addAll(mesaCompleta);

            ResultadoMao resultadoHeroi = avaliador.avaliarMao(maoFinalHeroi);
            ResultadoMao resultadoVilao = avaliador.avaliarMao(maoFinalVilao);

            int comparacao = resultadoHeroi.compareTo(resultadoVilao);

            if (comparacao > 0) {
                vitoriasHeroi++;
            } else if (comparacao == 0) {
                empates++;
            }
        }

        double equidade = (vitoriasHeroi + (empates / 2.0)) / numeroDeSimulacoes;
        return equidade;

    }

    public double calcularEquidadeComRange(List<Carta> maoHeroi, List<Carta> cartasDaMesa, int numeroDeSimulacoes, List<List<Carta>> rangeVilao) {

        List<Carta> cartasConhecidas = new ArrayList<>(maoHeroi);
        cartasConhecidas.addAll(cartasDaMesa);

        List<List<Carta>> rangeValido = new ArrayList<>();
        for (List<Carta> maoPossivel : rangeVilao) {
            Carta carta1Vilao = maoPossivel.get(0);
            Carta carta2Vilao = maoPossivel.get(1);
//retira todas combinações de cartas que ja estão na mão ou mesa
            if (!cartasConhecidas.contains(carta1Vilao) && !cartasConhecidas.contains(carta2Vilao)) {
                rangeValido.add(maoPossivel);
            }
        }

        if (rangeValido.isEmpty()) {
            return 1.0;
        }
        int vitoriasHeroi = 0;
        int empates = 0;

        for (int i = 0; i < numeroDeSimulacoes; i++) {

            Baralho baralhoSimulacao = new Baralho();
            List<Carta> baralhoRestante = baralhoSimulacao.getCartas();

            int indexSorteio = random.nextInt(rangeValido.size());
            List<Carta> maoVilao = new ArrayList<>(rangeValido.get(indexSorteio));

            baralhoRestante.removeAll(cartasConhecidas);
            baralhoRestante.removeAll(maoVilao);

            Collections.shuffle(baralhoRestante);

            List<Carta> mesaCompleta = new ArrayList<>(cartasDaMesa);
            int cartasQueFaltamNaMesa = 5 - cartasDaMesa.size();

            for (int j = 0; j < cartasQueFaltamNaMesa; j++) {
                mesaCompleta.add(baralhoRestante.remove(0));
            }

            List<Carta> maoFinalHeroi = new ArrayList<>(maoHeroi);
            maoFinalHeroi.addAll(mesaCompleta);

            List<Carta> maoFinalVilao = new ArrayList<>(maoVilao);
            maoFinalVilao.addAll(mesaCompleta);

            ResultadoMao resultadoHeroi = avaliador.avaliarMao(maoFinalHeroi);
            ResultadoMao resultadoVilao = avaliador.avaliarMao(maoFinalVilao);

            int comparacao = resultadoHeroi.compareTo(resultadoVilao);

            if (comparacao > 0) {
                vitoriasHeroi++;
            } else if (comparacao == 0) {
                empates++;
            }
        }
        return (vitoriasHeroi + (empates / 2.00)) / numeroDeSimulacoes;
    }
}
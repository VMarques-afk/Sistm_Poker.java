import java.util.ArrayList;
import java.util.List;
import java.util.Collections;



public class EquityCalculator {

    private final HandEvaluator avaliador;

    public EquityCalculator(HandEvaluator avaliador)  {

        this.avaliador = avaliador;
    }

    public double calcularEquidade(List<Carta> maoHeroi, List<Carta> cartasDaMesa, int numeroDeSimulacoes) {

        List<Carta> cartasConhecidas = new ArrayList<>();
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


            //faltava um for para retornar as cartas na mesa
            //precisava de um loop

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
           /* int forcaHeroi222222 = avaliador.avaliarMao(maoFinalHeroi).getForca();
            int forcaVilao = avaliador.avaliarMao(maoFinalVilao).getForca();

            if (forcaHeroi > forcaVilao) {
                vitoriasHeroi++;

            } else if (forcaHeroi == forcaVilao) {
                empates++;
            }

        }
        double equidade = (vitoriasHeroi + (empates / 2.0)) / numeroDeSimulacoes;
        return equidade;
         correção
         */


}
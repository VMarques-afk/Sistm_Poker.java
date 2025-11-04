import java.util.ArrayList;
import java.util.List;

public class OutsCalculator {

    private final HandEvaluator avaliador;

    public OutsCalculator (HandEvaluator avaliador) {
        this.avaliador = avaliador;

    }

    public int calcularOuts (List<Carta> maoDoJogador, List<Carta> cartasDaMesa) {

        List<Carta> cartasConhecidas = new ArrayList<>(maoDoJogador);
        cartasConhecidas.addAll(cartasDaMesa);

        /*int forcaAtual = avaliador.avaliarMao(cartasConhecidas).getForca();
        * esta gerando conflito
        * */
        ResultadoMao resultadoAtual = avaliador.avaliarMao(cartasConhecidas);

        Baralho baralho = new Baralho();
        List<Carta> baralhoRestante = baralho.getCartas();

        baralhoRestante.removeAll(cartasConhecidas);

        int contagemDeOuts = 0;

        for (Carta proximaCarta : baralhoRestante) {

            List<Carta> maoFutura = new ArrayList<>(cartasConhecidas);
            maoFutura.add(proximaCarta);

            ResultadoMao resultadoFuturo = avaliador.avaliarMao(maoFutura);

            if(resultadoFuturo.getTipoMao().getForca() > resultadoAtual.getTipoMao().getForca()) {
                contagemDeOuts++;
            }

            /*if (resultadoFuturo.compareTo(resultadoAtual) > 0) {
            contagemDeOuts++;*/
        }

        return contagemDeOuts;

    }
}
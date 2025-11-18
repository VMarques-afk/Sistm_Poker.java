import java.util.ArrayList;
import java.util.List;

public class OutsCalculator {

    private final HandEvaluator avaliador;

    public OutsCalculator(HandEvaluator avaliador) {
        this.avaliador = avaliador;
    }

    public DrawAnalysis calcularDraws(List<Carta> maoDoJogador, List<Carta> cartasDaMesa) {

        List<Carta> cartasConhecidas = new ArrayList<>(maoDoJogador);
        cartasConhecidas.addAll(cartasDaMesa);

        ResultadoMao resultadoAtual = avaliador.avaliarMao(cartasConhecidas);
        TipoMao tipoMao = resultadoAtual.getTipoMao();

        Baralho baralho = new Baralho();
        List<Carta> baralhoRestante = baralho.getCartas();
        baralhoRestante.removeAll(cartasConhecidas);

        DrawAnalysis analise = new DrawAnalysis();

        for (Carta proximaCarta : baralhoRestante) {
            List<Carta> maoFutura = new ArrayList<>(cartasConhecidas);
            maoFutura.add(proximaCarta);

            ResultadoMao resultadoFuturo = avaliador.avaliarMao(maoFutura);
            TipoMao tipoFuturo = resultadoFuturo.getTipoMao();

            if (resultadoFuturo.compareTo(resultadoAtual) > 0) {

                if(tipoFuturo == TipoMao.FLUSH && tipoMao.getForca() < TipoMao.FLUSH.getForca()) {
                    analise.addOutsFlush(proximaCarta);

                } else if (tipoFuturo == TipoMao.STRAIGHT && tipoMao.getForca() < TipoMao.STRAIGHT.getForca()) {
                    analise.addOutStraight(proximaCarta);

                }else if (tipoFuturo == TipoMao.FULL_HOUSE && tipoMao.getForca() < TipoMao.FULL_HOUSE.getForca()) {
                    analise.addOutFullHouse(proximaCarta);

                } else if (tipoFuturo.getForca() >= TipoMao.UM_PAR.getForca() && tipoFuturo.getForca() <= TipoMao.QUADRA.getForca()) {
                    analise.addOutParaParTrincaQuadra(proximaCarta);
                }
            }
        }
        return analise;
    }
}
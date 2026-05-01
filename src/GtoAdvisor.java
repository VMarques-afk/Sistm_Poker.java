import java.util.HashMap;
import java.util.Map;

public class GtoAdvisor {
    // Usamos um "Mapa de Mapas" para guardar a tabela.
    private Map<Posicao, Map<String, AcaoGTO>> tabelasGTO;

    public GtoAdvisor() {
        this.tabelasGTO = new HashMap<>();
        carregarTabelas(); // Chama o método que preenche os dados
    }
    //Metodo principal: O usuario pergunta o que fazer.

    public AcaoGTO getAcao(Posicao posicao, String notacaoMao) {
        Map<String, AcaoGTO> tabelaDaPosicao = tabelasGTO.get(posicao);

        if (tabelaDaPosicao == null) {
            return AcaoGTO.FOLD;
        }
        return tabelaDaPosicao.getOrDefault(notacaoMao, AcaoGTO.FOLD);
    }

    private void addRange(Posicao pos, AcaoGTO acao, String... maos) {
        //pega a gaveta e cria uma nova
        tabelasGTO.putIfAbsent(pos, new HashMap<>());
        Map<String, AcaoGTO> tabela = tabelasGTO.get(pos);
        for (String mao : maos) {
            tabela.put(mao.toUpperCase(), acao);
        }
    }

    private void carregarTabelas() {
//UTG(UNDER THE GUN) RANGE BEM APERTAOD
        addRange(Posicao.UTG, AcaoGTO.RAISE,
                "AA", "KK", "QQ", "JJ", "TT",
                "99", "88", "77", "AKS",
                "AQS", "AJS", "ATS", "KQS",
                "AKO", "AQO"
        );

        //MP(MIDDLE) ABRE UM POUCO MAIS
        addRange(Posicao.MP, AcaoGTO.RAISE,
                "AA", "KK", "QQ", "JJ", "TT", "99", "88", "77", "66", "55",
                "AKS", "AQS", "AJS", "ATS", "A9S", "KQS", "KJS", "QJS", "JTS",
                "AKO", "AQO", "AJO"
        );

//CO(cutoff) range mais agressivo para roubar blinds
        addRange(Posicao.CO, AcaoGTO.RAISE,
                "AA", "KK", "QQ", "JJ", "TT", "99", "88", "77", "66", "55", "44", "33", "22", "AKS", "AQS", "AJS",
                "ATS", "A9S", "A8S", "A7S", "A6S", "A5S", "A4S", "A3S", "A2S", "KQS", "KJS", "KTS", "K9S", "QJS", "QTS",
                "JTS", "T9S", "98S", "87S", "AKO", "AQO", "AJO", "ATO", "KQO", "KJO"
        );

//BTN(button) A melhor posição, range mais amplo
        addRange(Posicao.BTN, AcaoGTO.RAISE,
                "AA", "KK", "QQ", "JJ", "TT", "99", "88", "77", "66", "55", "44", "33", "22", "AKS", "AQS", "AJS", "ATS", "A9S", "A8S", "A7S",
                "A6S", "A5S", "A4S", "A3S", "A2S", "KQS", "KJS", "KTS", "K9S", "K8S", "K7S", "K6S", "K5S", "QJS", "QTS", "Q9S", "JTS", "J9S",
                "T9S", "T8S", "98S", "87S", "76S", "65S", "AKO", "AQO", "AJO", "ATO", "A9O", "A8O", "KQO", "KJO", "KTO", "QJO", "QTO", "JTO"
        );

//SB(Small Blind) joga fora de posição, range mesclado
        addRange(Posicao.SB, AcaoGTO.RAISE,
                "AA", "KK", "QQ", "JJ", "TT", "99", "88", "77", "66", "AKS", "AQS", "AJS", "ATS", "A9S", "A8S", "A7S", "A6S", "A5S",
                "A4S", "KQS", "KJS", "KTS", "QJS", "QTS", "JTS", "T9S", "98S", "AKO", "AQO", "AJO", "ATO", "KQO"
        );
        addRange(Posicao.SB, AcaoGTO.CALL,//calls de defesa
                "55", "44", "33", "22", "A3S", "A2S", "K9S", "K8S", "KJO", "QJO"
        );

//BB(BigBlind)(fora de posição) uma das piores posições, pois age por último pré-flop e primeiro pós-flop
//No entanto, oferece ótimas pot odds para defender com mãos amplas
        addRange(Posicao.BB, AcaoGTO.RAISE,
                "AA", "KK", "QQ", "JJ", "TT", "AKS", "AQS", "AKO"
        );

        // RANGE DE DEFESA DO BB (CALL) — adicionado para revisão GTO.
        // BB já tem 1BB investido e recebe pot odds melhores que qualquer outra posição,
        // por isso defende um range bem mais amplo do que as outras posições.
        // Revisar: frequência de defesa ideal vs. range de abertura do oponente.
        addRange(Posicao.BB, AcaoGTO.CALL,
                "99", "88", "77", "66", "55", "44", "33", "22",
                "AJS", "ATS", "A9S", "A8S", "A7S", "A6S", "A5S", "A4S", "A3S", "A2S",
                "KQS", "KJS", "KTS", "K9S", "K8S", "QJS", "QTS", "Q9S", "JTS", "J9S",
                "T9S", "T8S", "98S", "87S", "76S", "65S",
                "AQO", "AJO", "ATO", "A9O", "KQO", "KJO", "QJO", "JTO"
        );
    }


}
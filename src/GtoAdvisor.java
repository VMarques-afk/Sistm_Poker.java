import java.util.HashMap;
import java.util.Map;

    public class GtoAdvisor {
        // Usamos um "Mapa de Mapas" para guardar a tabela.
        private Map<Posicao, Map<String, AcaoGTO>> tabelasGTO;

        public GtoAdvisor() {
            this.tabelasGTO = new HashMap<>();
            carregarTabelas(); // Chama o método que preenche os dados
        }
        //Método principal: O usuário pergunta o que fazer.

        public AcaoGTO getAcao(Posicao posicao, String notacaoMao) {

            Map<String, AcaoGTO> tabelaDaPosicao = tabelasGTO.get(posicao);

            if (tabelaDaPosicao == null) {
                return AcaoGTO.FOLD;
            }
                return tabelaDaPosicao.getOrDefault(notacaoMao, AcaoGTO.FOLD);
        }

        private void carregarTabelas() {
            // --- Tabela para Posição: UTG (Under the Gun)
            Map<String, AcaoGTO> tabelaUTG = new HashMap<>();
            // Pares que damos RAISE
            tabelaUTG.put("AA", AcaoGTO.RAISE);
            tabelaUTG.put("KK", AcaoGTO.RAISE);
            tabelaUTG.put("QQ", AcaoGTO.RAISE);
            tabelaUTG.put("JJ", AcaoGTO.RAISE);
            tabelaUTG.put("TT", AcaoGTO.RAISE); // Par de Dez
            tabelaUTG.put("99", AcaoGTO.RAISE);
            tabelaUTG.put("88", AcaoGTO.RAISE);
            tabelaUTG.put("77", AcaoGTO.RAISE);

            // Mãos "Suited" que damos RAISE
            tabelaUTG.put("AKS", AcaoGTO.RAISE);
            tabelaUTG.put("AQS", AcaoGTO.RAISE);
            tabelaUTG.put("AJS", AcaoGTO.RAISE);
            tabelaUTG.put("ATS", AcaoGTO.RAISE);
            tabelaUTG.put("KQS", AcaoGTO.RAISE);

            // Mãos "Offsuit" que damos RAISE
            tabelaUTG.put("AKO", AcaoGTO.RAISE);
            tabelaUTG.put("AQO", AcaoGTO.RAISE);

            this.tabelasGTO.put(Posicao.UTG, tabelaUTG);

            // Adiciona as tabelas para as outras posições (MP, CO, BTN, SB, BB)

        }
    }



   /* Pense no GtoAdvisor como um novo "especialista" na nossa equipe: o Estrategista Chefe.

    Enquanto os outros cérebros (HandEvaluator, EquityCalculator) são "Matemáticos" que calculam probabilidades,
    o GtoAdvisor é um "General" que te dá a ordem de batalha com base em um livro de regras pré-definido.

    Vamos ver as duas partes que você copiou:

            1. O "Menu de Comandos" (AcaoGTO.java)
    Isso é um Enum, que já vimos antes.

    O que é? É simplesmente uma lista de comandos oficiais que nosso Estrategista pode dar.

    Analogia: Pense nisso como os únicos 4 botões que o General pode apertar: RAISE, CALL, FOLD, CHECK.

    Por que usar um Enum? Para evitar erros de digitação. Se usássemos Strings, poderíamos acidentalmente
    dizer AUMENTAR em um lugar e AUMENTE em outro, e o programa não entenderia que são a mesma coisa. Com o Enum, AcaoGTO.RAISE
    é sempre o mesmo comando.

            2. O "Cérebro Estratégico" (GtoAdvisor.java)
    Esta classe é o "escritório" do Estrategista Chefe. É aqui que ele guarda e consulta seu livro de regras.

    A Parte Mais Importante: O "Armário de Arquivos"
    Java

    private Map<Posicao, Map<String, AcaoGTO>> tabelasGTO;
    Esta linha parece assustadora, mas é a parte mais genial. É um "Mapa dentro de um Mapa". Vamos usar uma analogia:

    Analogia: tabelasGTO é um Armário de Arquivos (Map).

    A primeira chave, Posicao, é a Gaveta. Cada gaveta é etiquetada com uma Posicao (ex: "UTG", "MP", "BTN").

            O valor, Map<String, AcaoGTO>, é o Conjunto de Pastas dentro de cada gaveta.

    Dentro desse conjunto de pastas:

    A String é a Etiqueta da Pasta (ex: "AA", "AKs", "T9o").

    O AcaoGTO é a Instrução em Papel dentro da pasta (ex: "RAISE").

    Resumindo: Para saber o que fazer com "AKs" na posição "UTG", o programa faz o seguinte:

    Vai até o Armário (tabelasGTO).

    Abre a Gaveta "UTG".

    Procura pela Pasta "AKs".

    Lê a Instrução dentro dela, que diz "RAISE".

    Como a Classe Funciona (Método por Método)
1. O Construtor: public GtoAdvisor()

    O que faz: É o "montador". Quando você cria um new GtoAdvisor(), ele:

            this.tabelasGTO = new HashMap<>();: Compra um armário de arquivos vazio.

    carregarTabelas();: Chama o "estagiário" (o método abaixo) para encher o armário com os dados.

2. O "Trabalho Braçal": private void carregarTabelas()

    O que faz: Este é o "estagiário" que faz o trabalho de inserir os dados.

    Map<String, AcaoGTO> tabelaUTG = new HashMap<>();: Pega um novo conjunto de pastas vazio para a gaveta "UTG".

            tabelaUTG.put("AA", AcaoGTO.RAISE);: Cria uma pasta "AA", coloca a instrução "RAISE" dentro.

            tabelaUTG.put("KK", AcaoGTO.RAISE);: Cria uma pasta "KK", coloca "RAISE" dentro.

... (ele faz isso para todas as mãos que não são FOLD) ...

            this.tabelasGTO.put(Posicao.UTG, tabelaUTG);: Pega o conjunto de pastas "UTG" pronto e o coloca dentro
    da gaveta "UTG" do armário principal.

// TODO:: Isso é um lembrete de que nós, por enquanto, só preenchemos a gaveta "UTG". As gavetas "MP", "CO", etc., ainda estão vazias.

            3. O Método Principal: public AcaoGTO getAcao(...)

    O que faz: Este é o "Consultor" público. É o único método que o Main.java vai chamar.

            Map<String, AcaoGTO> tabelaDaPosicao = tabelasGTO.get(posicao);:
            "Vá até o armário e pegue o conjunto de pastas da gaveta posicao."

    String notacaoMao = mao.getNotacao();: "Pergunte ao PreflopHand qual é a nossa mão (ex: '72o')."

            return tabelaDaPosicao.getOrDefault(notacaoMao, AcaoGTO.FOLD);

    Esta última linha é crucial e muito inteligente. Ela significa:
            "Procure neste conjunto de pastas pela etiqueta da nossa mão (notacaoMao, ex: '72o').

    Se você ACHAR (ex: procurou 'AKs' e achou), me retorne a instrução que está dentro (ex: RAISE).

    Se você NÃO ACHAR (ex: procurou '72o' e não tem pasta), não quebre o programa. Em vez disso, me retorne a ação
            padrão: AcaoGTO.FOLD."

    Por que isso é genial? Porque no carregarTabelas(), nós não precisamos digitar as 169 mãos. Nós só precisamos
    digitar as mãos que são RAISE ou CALL. Qualquer mão que não estiver na lista (como "72o") será automaticamente
            um FOLD, graças ao getOrDefault. */

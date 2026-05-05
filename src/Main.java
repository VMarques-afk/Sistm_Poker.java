import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static GtoAdvisor advisorGTO = new GtoAdvisor();
    private static HandEvaluator avaliador = new HandEvaluator();
    private static EquityCalculator calculadoraEquidade = new EquityCalculator(avaliador);
    private static OutsCalculator calculadoraOuts = new OutsCalculator(avaliador);

    private static String traduzirTipoMao(TipoMao tipo) {
        switch (tipo) {
            case CARTA_ALTA:    return "Carta Alta";
            case UM_PAR:        return "Um Par";
            case DOIS_PARES:    return "Dois Pares";
            case TRINCA:        return "Trinca";
            case STRAIGHT:      return "Sequência";
            case FLUSH:         return "Flush";
            case FULL_HOUSE:    return "Full House";
            case QUADRA:        return "Quadra";
            case STRAIGHT_FLUSH: return "Sequência de Flush";
            case ROYAL_FLUSH:   return "Royal Flush";
            default:            return tipo.name();
        }
    }

    private static Carta traduzirCarta(String notacaoWeb) {
        char valChar = notacaoWeb.charAt(0);
        char naipeChar = notacaoWeb.charAt(1);

        Valor valor = null;
        switch (valChar) {
            case 'A': valor = Valor.AS; break;
            case 'K': valor = Valor.REI; break;
            case 'Q': valor = Valor.DAMA; break;
            case 'J': valor = Valor.VALETE; break;
            case 'T': valor = Valor.DEZ; break;
            case '9': valor = Valor.NOVE; break;
            case '8': valor = Valor.OITO; break;
            case '7': valor = Valor.SETE; break;
            case '6': valor = Valor.SEIS; break;
            case '5': valor = Valor.CINCO; break;
            case '4': valor = Valor.QUATRO; break;
            case '3': valor = Valor.TRES; break;
            case '2': valor = Valor.DOIS; break;
        }

        Naipe naipe = null;
        switch (naipeChar) {
            case '♠': naipe = Naipe.ESPADAS; break;
            case '♥': naipe = Naipe.COPAS; break;
            case '♦': naipe = Naipe.OUROS; break;
            case '♣': naipe = Naipe.PAUS; break;
        }
        return new Carta(naipe, valor);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando...");

        DatabaseManager.inicializarBanco();

        int porta = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] response = null;
                // Tenta carregar do JAR (produção), depois do sistema de arquivos (desenvolvimento)
                InputStream is = Main.class.getResourceAsStream("/poker-advisor-ui.html");
                if (is != null) {
                    response = is.readAllBytes();
                } else {
                    File htmlFile = new File("poker-advisor-ui.html");
                    if (htmlFile.exists()) response = Files.readAllBytes(htmlFile.toPath());
                }
                if (response == null) {
                    String erro = "<h1>Erro: poker-advisor-ui.html não encontrado.</h1>";
                    exchange.sendResponseHeaders(404, erro.getBytes("UTF-8").length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(erro.getBytes("UTF-8"));
                    os.close();
                    return;
                }
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            }
        });

        server.createContext("/api/gto", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                String posStr = "UTG";
                String mao = "AA";

                if (query != null) {
                    for (String param : query.split("&")) {
                        String[] par = param.split("=");
                        if (par.length > 1) {
                            if (par[0].equals("pos")) posStr = par[1].toUpperCase();
                            if (par[0].equals("mao")) mao = par[1].toUpperCase();
                        }
                    }
                }
                try {
                    Posicao posicao = Posicao.valueOf(posStr);
                    AcaoGTO acao = advisorGTO.getAcao(posicao, mao);
                    String descricao;
                    switch (acao) {
                        case RAISE: descricao = "Aumentar (Raise)"; break;
                        case CALL:  descricao = "Pagar (Call)";     break;
                        case CHECK: descricao = "Passar (Check)";   break;
                        default:    descricao = "Desistir (Fold)";  break;
                    }
                    String respostaJson = "{\"acao\": \"" + acao.name() + "\", \"descricao\": \"" + descricao + "\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                    exchange.sendResponseHeaders(200, respostaJson.getBytes("UTF-8").length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(respostaJson.getBytes("UTF-8"));
                    os.close();
                } catch (Exception e) {
                    String erroJson = "{\"erro\" : \"Dados invalidos\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(400, erroJson.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(erroJson.getBytes());
                    os.close();
                }
            }
        });

        server.createContext("/api/equidade", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                String heroParam = "";
                String boardParam = "";
                String vilaoParam = "tight";

                if (query != null) {
                    for (String param : query.split("&")) {
                        String[] par = param.split("=");
                        if (par.length > 1) {
                            if (par[0].equals("hero")) heroParam = java.net.URLDecoder.decode(par[1], "UTF-8");
                            if (par[0].equals("board")) boardParam = java.net.URLDecoder.decode(par[1], "UTF-8");
                            if (par[0].equals("vilao")) vilaoParam = par[1];
                        }
                    }
                }
                try {
                    System.out.println("--- Nova Simulação Nível Deus ---");
                    System.out.println("Heroi: " + heroParam + " | Board: " + boardParam + " | Vilão: " + vilaoParam);

                    String[] cartasHeroiStr = heroParam.split(",");
                    String[] cartasBoardStr = boardParam.split(",");

                    Carta c1 = traduzirCarta(cartasHeroiStr[0]);
                    Carta c2 = traduzirCarta(cartasHeroiStr[1]);

                    List<Carta> board = new ArrayList<>();
                    if (!boardParam.isEmpty()) {
                        for (String cStr : cartasBoardStr) {
                            board.add(traduzirCarta(cStr));
                        }
                    }

                    List<Carta> maoHeroi = new ArrayList<>();
                    maoHeroi.add(c1);
                    maoHeroi.add(c2);

                    String rangeAdversarioStr = "";
                    if (vilaoParam.equals("tight")) {
                        rangeAdversarioStr = "TT, JJ, QQ, KK, AA, AJS, AQS, AKS, AQO, AKO";
                    } else if (vilaoParam.equals("loose")) {
                        rangeAdversarioStr = "55, 66, 77, 88, 99, TT, JJ, QQ, KK, AA, A8S, A9S, ATS, AJS, AQS, AKS, K9S, KTS, KJS, KQS, Q9S, QTS, QJS, J9S, JTS, T9S, 98S, 87S, ATO, AJO, AQO, AKO, KJO, KQO";
                    } else {
                        rangeAdversarioStr = "22, 33, 44, 55, 66, 77, 88, 99, TT, JJ, QQ, KK, AA, A2S, A3S, A4S, A5S, A6S, A7S, A8S, A9S, ATS, AJS, AQS, AKS, K2S, K3S, K4S, K5S, K6S, K7S, K8S, K9S, KTS, KJS, KQS, Q7S, Q8S, Q9S, QTS, QJS, J7S, J8S, J9S, JTS, T7S, T8S, T9S, 97S, 98S, 86S, 87S, 75S, 76S, 65S, 54S, A2O, A3O, A4O, A5O, A6O, A7O, A8O, A9O, ATO, AJO, AQO, AKO, K9O, KTO, KJO, KQO, Q9O, QTO, QJO, J9O, JTO, T9O";
                    }

                    List<List<Carta>> rangeVilao = RangeBuilder.construirRange(rangeAdversarioStr);

                    double equidadeFinal = calculadoraEquidade.calcularEquidadeComRange(maoHeroi, board, 10000, rangeVilao);

                    DatabaseManager.salvarJogada(heroParam, boardParam, vilaoParam, equidadeFinal);

                    String maoAtualStr = "";
                    String outsResumo = "";
                    int totalOuts = 0;
                    if (!board.isEmpty()) {
                        List<Carta> cartasParaAvaliar = new ArrayList<>(maoHeroi);
                        cartasParaAvaliar.addAll(board);
                        ResultadoMao maoAtual = avaliador.avaliarMao(cartasParaAvaliar);
                        maoAtualStr = traduzirTipoMao(maoAtual.getTipoMao());
                        if (board.size() < 5) {
                            DrawAnalysis analise = calculadoraOuts.calcularDraws(maoHeroi, board);
                            outsResumo = analise.getResumo();
                            totalOuts = analise.getTotalUnicoOuts();
                        }
                    }

                    String respostaJson = "{" +
                        "\"equidade\": " + equidadeFinal + "," +
                        "\"mao_atual\": \"" + maoAtualStr + "\"," +
                        "\"outs_resumo\": \"" + outsResumo.replace("\"", "\\\"") + "\"," +
                        "\"total_outs\": " + totalOuts +
                        "}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                    exchange.sendResponseHeaders(200, respostaJson.getBytes("UTF-8").length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(respostaJson.getBytes("UTF-8"));
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    String erroJson = "{\"erro\": \"erro interno\"}";
                    exchange.getResponseHeaders().set("Content-type", "application/json");
                    exchange.sendResponseHeaders(500, erroJson.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(erroJson.getBytes());
                    os.close();
                }
            }
        });

        server.createContext("/api/outs", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                String heroParam = "";
                String boardParam = "";
                if (query != null) {
                    for (String param : query.split("&")) {
                        String[] par = param.split("=");
                        if (par.length > 1) {
                            if (par[0].equals("hero")) heroParam = java.net.URLDecoder.decode(par[1], "UTF-8");
                            if (par[0].equals("board")) boardParam = java.net.URLDecoder.decode(par[1], "UTF-8");
                        }
                    }
                }
                try {
                    String[] cartasHeroiStr = heroParam.split(",");
                    List<Carta> maoHeroi = new ArrayList<>();
                    maoHeroi.add(traduzirCarta(cartasHeroiStr[0]));
                    maoHeroi.add(traduzirCarta(cartasHeroiStr[1]));

                    List<Carta> board = new ArrayList<>();
                    if (!boardParam.isEmpty()) {
                        for (String cStr : boardParam.split(",")) board.add(traduzirCarta(cStr));
                    }

                    DrawAnalysis analise = calculadoraOuts.calcularDraws(maoHeroi, board);
                    String respostaJson = "{" +
                        "\"total_outs\": " + analise.getTotalUnicoOuts() + "," +
                        "\"resumo\": \"" + analise.getResumo().replace("\"", "\\\"") + "\"" +
                        "}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                    exchange.sendResponseHeaders(200, respostaJson.getBytes("UTF-8").length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(respostaJson.getBytes("UTF-8"));
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    String erroJson = "{\"erro\": \"erro interno\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(500, erroJson.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(erroJson.getBytes());
                    os.close();
                }
            }
        });

        server.createContext("/api/historico", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    String json = DatabaseManager.buscarHistoricoJson();
                    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                    exchange.sendResponseHeaders(200, json.getBytes("UTF-8").length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(json.getBytes("UTF-8"));
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor online na porta " + porta);
    }
}
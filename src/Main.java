import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;

public class Main {

    private static GtoAdvisor advisorGTO = new GtoAdvisor();
    private static HandEvaluator avaliador = new HandEvaluator();
    private static EquityCalculator calculadoraEquidade = new EquityCalculator(avaliador);

    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando...");

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                File htmlFile = new File("poker-advisor-ui.html");

                if (!htmlFile.exists()) {
                    String erro = "<h1>Erro 404</h1> <p>Arquivo poker-advisor-ui.html não encontrado.</p>";
                    exchange.sendResponseHeaders(404, erro.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(erro.getBytes());
                    os.close();
                    return;
                }

                byte[] response = Files.readAllBytes(htmlFile.toPath());
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
                    for (String param : query.split("&")) {  // erro 1: = → :
                        String[] par = param.split("=");
                        if (par.length > 1) {
                            if (par[0].equals("pos")) posStr = par[1].toUpperCase();
                            if (par[0].equals("mao")) mao = par[1].toUpperCase();  // erro 2: posicao → mao
                        }
                    }
                }

                try {
                    Posicao posicao = Posicao.valueOf(posStr);
                    AcaoGTO acao = advisorGTO.getAcao(posicao, mao);

                    String respostaJson = "{\"acao\" : \"" + acao.name() + "\"}";

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, respostaJson.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(respostaJson.getBytes());
                    os.close();
                } catch (Exception e) {
                    String erroJson = "{\"erro\" : \"Dados invalidos\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(400, erroJson.length());  // erro 3: getResponseHeaders → sendResponseHeaders
                    OutputStream os = exchange.getResponseBody();
                    os.write(erroJson.getBytes());
                    os.close();
                }
            }
        });

        server.createContext("/api/equidade", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                //pega a query da url(ex: heroi=A.S,K.S&board= 7.D,8.D,9.D)
                String query = exchange.getRequestURI().getQuery().toUpperCase();
                String heroParam = "";
                String boardParam = "";

                if (query != null) {
                    //query.split ("&")separa um bloco do outro para não ter confusão das cartas
                    for (String param : query.split("&")) {
                        String[] par = param.split("=");
                        if (par.length > 1) {
                            if (par[0].equals("hero")) heroParam = par[1];
                            if (par[0].equals("board")) boardParam = par[1];
                        }
                    }
                }
                try {
                    heroParam = java.net.URLDecoder.decode(heroParam, "UTF-8");
                    boardParam = java.net.URLDecoder.decode(boardParam, "UTF-8");

                    System.out.println("Nova simulação");
                    System.out.println("Heroi: " + heroParam);
                    System.out.println("Board: " + boardParam);

                    double equidadeSimulada = 68.5;

                    String respostaJson = "{\"equidade\": " + equidadeSimulada + "}";

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, respostaJson.getBytes("UTF-8").length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(respostaJson.getBytes("UTF-8"));
                    os.close();
                } catch (Exception e) {
                    String erroJson = "{\"erro\" : \"Erro ao rodar o MC\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(500, erroJson.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(erroJson.getBytes());
                    os.close();
                }
            }
        });

server.setExecutor(null);
        server.start();
        System.out.println("Servidor online. Port 8080");

        if (Desktop.isDesktopSupported() &&
                Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI("http://localhost:8080"));
        }
    }
}

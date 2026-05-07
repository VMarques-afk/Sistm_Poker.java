import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:historico_poker.db";

    public static void inicializarBanco() {
        String sql = "CREATE TABLE IF NOT EXISTS historico (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "data_hora DATETIME DEFAULT (datetime('now', 'localtime'))," +
                "mao_heroi TEXT NOT NULL," +
                "board TEXT NOT NULL," +
                "perfil_vilao TEXT NOT NULL," +
                "equidade REAL NOT NULL" +
                ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            try {
                stmt.execute("ALTER TABLE historico ADD COLUMN session_id TEXT NOT NULL DEFAULT ''");
            } catch (SQLException e) {
                // Coluna já existe
            }
            System.out.println("✅ Banco de Dados SQLite inicializado e pronto para salvar mãos!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao criar banco de dados: " + e.getMessage());
        }
    }

    public static void salvarJogada(String maoHeroi, String board, String perfilVilao, double equidade, String sessionId) {
        String sql = "INSERT INTO historico(mao_heroi, board, perfil_vilao, equidade, session_id) VALUES(?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maoHeroi);
            pstmt.setString(2, board);
            pstmt.setString(3, perfilVilao);
            pstmt.setDouble(4, equidade);
            pstmt.setString(5, sessionId);
            pstmt.executeUpdate();

            System.out.println("💾 Simulação salva no histórico com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar jogada: " + e.getMessage());
        }
    }

    public static String buscarHistoricoJson(String sessionId) {
        String sql = "SELECT * FROM historico WHERE session_id = ? ORDER BY id DESC LIMIT 20";
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sessionId);
            ResultSet rs = pstmt.executeQuery();

            boolean isFirst = true;
            while (rs.next()) {
                if (!isFirst) {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append("{");
                jsonBuilder.append("\"id\":").append(rs.getInt("id")).append(",");
                jsonBuilder.append("\"data_hora\":\"").append(rs.getString("data_hora")).append("\",");
                jsonBuilder.append("\"mao_heroi\":\"").append(rs.getString("mao_heroi")).append("\",");
                jsonBuilder.append("\"board\":\"").append(rs.getString("board")).append("\",");
                jsonBuilder.append("\"perfil_vilao\":\"").append(rs.getString("perfil_vilao")).append("\",");
                jsonBuilder.append("\"equidade\":").append(rs.getDouble("equidade"));
                jsonBuilder.append("}");
                isFirst = false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar histórico: " + e.getMessage());
        }

        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}
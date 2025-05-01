package ufrn.br.projeto_web_1u;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectaBanco {


    public static Connection getConnection() throws SQLException {
        // Configurações fixas de conexão
        String dbHost = "localhost"; // ou o IP do servidor onde está o PostgreSQL
        String dbPort = "5433";
        String dbName = "bdpw";
        String username = "postgres";
        String password = "porta";

        // URL de conexão
        String dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

        // Estabelecendo a conexão
        return DriverManager.getConnection(dbUrl, username, password);
    }
}

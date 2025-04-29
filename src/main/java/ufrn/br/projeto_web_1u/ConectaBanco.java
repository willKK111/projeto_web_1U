package ufrn.br.projeto_web_1u;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectaBanco {

    /*
    Conexão fixa para o banco PostgreSQL usado no PgAdmin
    */
    public static Connection getConnection() throws SQLException {
        // Configurações fixas de conexão
        String dbHost = "localhost"; // ou o IP do servidor onde está o PostgreSQL
        String dbPort = "5433";
        String dbName = "bdpw"; // troque pelo nome do seu banco
        String username = "postgres"; // seu usuário do banco
        String password = "porta"; // sua senha do banco

        // URL de conexão
        String dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

        // Estabelecendo a conexão
        return DriverManager.getConnection(dbUrl, username, password);
    }
}

package ufrn.br.projeto_web_1u;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Controller
public class ConfigTabelas {

    @RequestMapping("/config")
    public void doConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = ConectaBanco.getConnection();
            stmt = con.
                    prepareStatement(
                            "CREATE TABLE IF NOT EXISTS " +
                                    "usuario (id SERIAL PRIMARY KEY, " +
                                    "nome VARCHAR(55), " +
                                    "email VARCHAR(55), " +
                                    "senha VARCHAR(55))");
            stmt.execute();
            stmt = con
                    .prepareStatement(
                            "CREATE TABLE IF NOT EXISTS " +
                                    "produto (id SERIAL PRIMARY KEY, " +
                                    "preco FLOAT, " +
                                    "descricao VARCHAR(55), " +
                                    "nome VARCHAR(55), " +
                                    "estoque INTEGER)");
            stmt.execute();
            con.close();

            response.getWriter().println("ok");

        } catch (SQLException ex) {
            response.getWriter().println(ex);
        }
    }

}


package ufrn.br.projeto_web_1u;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoBanco {

    public void cadastrarProduto(Produto produto) {
        String sql = "INSERT INTO produto (preco,descricao, nome , estoque) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConectaBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setFloat(1, produto.getPreco());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, produto.getNome());
            stmt.setInt(4, produto.getEstoque());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";

        try (Connection conn = ConectaBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getFloat("preco"),
                        rs.getString("descricao"),
                        rs.getString("nome"),
                        rs.getInt("estoque")
                );
                produtos.add(produto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return produtos;
    }
}

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

    public Produto getProdutoPorId(int id) {
        for (Produto p : listarProdutos()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void atualizarProduto(int produtoId) {
        String sql = "UPDATE produto SET estoque = estoque - 1 WHERE id = ? AND estoque > 0";

        try (Connection conn = ConectaBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);
            int rowsUpdated = stmt.executeUpdate();  // Aqui a quantidade de linhas afetadas pela query

            if (rowsUpdated == 0) {
                // Isso significa que nenhum produto foi atualizado, ou seja, o estoque estava 0 ou não encontrado.
                System.out.println("Produto com ID " + produtoId + " não foi atualizado, talvez estoque já seja 0.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Ou log com SLF4J
        }
    }



}

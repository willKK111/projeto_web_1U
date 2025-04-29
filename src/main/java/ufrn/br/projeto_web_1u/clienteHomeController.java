package ufrn.br.projeto_web_1u;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;

@Controller
public class clienteHomeController {

    private ProdutoBanco produtoBanco = new ProdutoBanco();

    @RequestMapping(value = "/clienteHome", method = RequestMethod.GET)
    public void listarProdutos(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        var writer = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioAutenticado") == null) {
            response.sendRedirect("/login");
            return;
        }

        writer.println("<html><head><title>Lista de Produtos</title></head><body>");
        writer.println("<h1>Produtos Disponíveis</h1>");

        List<Produto> produtos = produtoBanco.listarProdutos();

        if (produtos.isEmpty()) {
            writer.println("<p>Não há produtos cadastrados ainda.</p>");
        } else {
            writer.println("<table border='1'>");
            writer.println("<tr><th>Nome</th><th>Descrição</th><th>Preço</th><th>Estoque</th><th>Ações</th></tr>");
            for (Produto produto : produtos) {
                writer.println("<tr>");
                writer.println("<td>" + produto.getNome() + "</td>");
                writer.println("<td>" + produto.getDescricao() + "</td>");
                writer.println("<td>R$ " + produto.getPreco() + "</td>");
                writer.println("<td>" + produto.getEstoque() + "</td>");
                writer.println("<td>");
                if (produto.getEstoque() > 0) {
                    writer.println("<form action='/adicionarAoCarrinho' method='post' style='display:inline;'>");
                    writer.println("<input type='hidden' name='produtoId' value='" + produto.getId() + "'/>");
                    writer.println("<input type='submit' value='Adicionar ao Carrinho'/>");
                    writer.println("</form>");
                } else {
                    writer.println("<span>Sem estoque</span>");
                }
                writer.println("</td>");
                writer.println("</tr>");
            }
            writer.println("</table>");
        }

        // Botão para ir para o carrinho
        writer.println("<br/><form action='/verCarrinho' method='get'>");
        writer.println("<input type='submit' value='Ver Carrinho'/>");
        writer.println("</form>");

        writer.println("</body></html>");
        writer.close();
    }
}


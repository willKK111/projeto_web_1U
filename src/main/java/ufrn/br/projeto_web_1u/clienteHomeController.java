package ufrn.br.projeto_web_1u;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.*;

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
                    // Redireciona para o CarrinhoController para adicionar produto
                    writer.println("<a href='/modificarCarrinho?produtoId=" + produto.getId() + "&comando=add'>Adicionar Item no Carrinho</a>");
                } else {
                    writer.println("<span>Sem estoque</span>");
                }
                writer.println("</td>");
                writer.println("</tr>");
            }
            writer.println("</table>");
        }

        writer.println("<br/><form action='/verCarrinho' method='get'>");
        writer.println("<input type='submit' value='Ver Carrinho'/>");
        writer.println("</form>");

        writer.println("<br/><form action='/logout' method='get'>");
        writer.println("<input type='submit' value='Sair'/>");
        writer.println("</form>");

        writer.println("</body></html>");
        writer.close();
    }

    @RequestMapping(value = "/verCarrinho", method = RequestMethod.GET)
    public void verCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        var writer = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioAutenticado") == null) {
            response.sendRedirect("/login");
            return;
        }

        Object carrinhoObj = session.getAttribute("carrinho");
        Map<Integer, Integer> carrinho;

        if (carrinhoObj instanceof Map) {
            carrinho = (Map<Integer, Integer>) carrinhoObj;
        } else {
            session.removeAttribute("carrinho");
            response.sendRedirect("/clienteHome");
            return;
        }

        if (carrinho.isEmpty()) {
            response.sendRedirect("/clienteHome");
            return;
        }

        writer.println("<html><head><title>Carrinho de Compras</title></head><body>");
        writer.println("<h1>Seu Carrinho de Compras</h1>");
        writer.println("<table border='1'><tr><th>Produto</th><th>Preço Unitário</th><th>Quantidade</th><th>Total</th><th>Ações</th></tr>");

        double totalCarrinho = 0.0;

        for (Map.Entry<Integer, Integer> entry : carrinho.entrySet()) {
            int produtoId = entry.getKey();
            int quantidade = entry.getValue();
            Produto produto = produtoBanco.getProdutoPorId(produtoId);

            if (produto != null) {
                double subtotal = produto.getPreco() * quantidade;
                totalCarrinho += subtotal;

                writer.println("<tr>");
                writer.println("<td>" + produto.getNome() + "</td>");
                writer.println("<td>R$ " + produto.getPreco() + "</td>");
                writer.println("<td>" + quantidade + "</td>");
                writer.println("<td>R$ " + subtotal + "</td>");
                writer.println("<td>");
                // Redireciona para o CarrinhoController para remover produto
                writer.println("<a href='/modificarCarrinho?produtoId=" + produtoId + "&comando=remove'>Remover</a>");
                writer.println("</td>");
                writer.println("</tr>");
            }
        }

        writer.println("</table>");
        writer.println("<h3>Total da Compra: R$ " + totalCarrinho + "</h3>");

        writer.println("<form action='/finalizarCompra' method='post'>");
        writer.println("<input type='submit' value='Finalizar Compra'/>");
        writer.println("</form>");

        writer.println("<br/><a href='/clienteHome'>Voltar para Produtos</a>");
        writer.println("</body></html>");
        writer.close();
    }

    @RequestMapping(value = "/finalizarCompra", method = RequestMethod.POST)
    public void finalizarCompra(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioAutenticado") == null) {
            response.sendRedirect("/login");
            return;
        }

        Object carrinhoObj = session.getAttribute("carrinho");
        Map<Integer, Integer> carrinho;

        if (carrinhoObj instanceof Map) {
            carrinho = (Map<Integer, Integer>) carrinhoObj;
        } else {
            response.sendRedirect("/clienteHome");
            return;
        }

        if (carrinho.isEmpty()) {
            response.sendRedirect("/clienteHome");
            return;
        }

        for (Map.Entry<Integer, Integer> entry : carrinho.entrySet()) {
            int produtoId = entry.getKey();
            int quantidade = entry.getValue();

            for (int i = 0; i < quantidade; i++) {
                produtoBanco.atualizarProduto(produtoId); // Diminui estoque 1 a 1
            }
        }

        session.removeAttribute("carrinho");

        response.setContentType("text/html");
        var writer = response.getWriter();
        writer.println("<html><head><title>Compra Finalizada</title></head><body>");
        writer.println("<h1>Compra realizada com sucesso!</h1>");
        writer.println("<a href='/clienteHome'>Voltar para Produtos</a>");
        writer.println("</body></html>");
        writer.close();
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("/login");
    }
}

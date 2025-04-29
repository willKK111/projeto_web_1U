package ufrn.br.projeto_web_1u;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class CadastroProdutoController {

    private ProdutoBanco produtoBanco = new ProdutoBanco();

    @RequestMapping(value = "/cadastrarProduto", method = RequestMethod.GET)
    public void getCadastrarProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        var writer = response.getWriter();

        writer.println("<html><head><title>Cadastrar Produto</title></head><body>");
        writer.println("<h1>Cadastro de Produto</h1>");

        writer.println("<form action='/cadastrarProduto' method='post'>");
        writer.println("Nome: <input type='text' name='nome'/><br/>");
        writer.println("Descrição: <input type='text' name='descricao'/><br/>");
        writer.println("Preço: <input type='number' step='0.01' name='preco'/><br/>");
        writer.println("Estoque: <input type='number' name='estoque'/><br/>");
        writer.println("<input type='submit' value='Cadastrar'/>");
        writer.println("</form>");

        writer.println("<br/><form action='/lojistaHome' method='get'>");
        writer.println("<input type='submit' value='Voltar para Home do Lojista'/>");
        writer.println("</form>");

        writer.println("</body></html>");
        writer.close();
    }

    @RequestMapping(value = "/cadastrarProduto", method = RequestMethod.POST)
    public void postCadastrarProduto(@RequestParam String nome,
                                     @RequestParam String descricao,
                                     @RequestParam float preco,
                                     @RequestParam int estoque,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws IOException {

        Produto novoProduto = new Produto(0, preco, descricao, nome, estoque);

        try {
            produtoBanco.cadastrarProduto(novoProduto);
            response.sendRedirect("/listarProdutos");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao cadastrar produto." + e.getMessage());
        }
    }
}

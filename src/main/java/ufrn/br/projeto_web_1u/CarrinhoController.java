package ufrn.br.projeto_web_1u;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Map;

@Controller
public class CarrinhoController {

    @RequestMapping(value = "/modificarCarrinho", method = RequestMethod.GET)
    public void modificarCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioAutenticado") == null) {
            response.sendRedirect("/login");
            return;
        }

        String produtoIdStr = request.getParameter("produtoId");
        String comando = request.getParameter("comando");  // Pode ser "add" ou "remove"

        if (produtoIdStr != null && comando != null) {
            int produtoId = Integer.parseInt(produtoIdStr);
            Map<Integer, Integer> carrinho = (Map<Integer, Integer>) session.getAttribute("carrinho");

            if (carrinho == null) {
                carrinho = new java.util.HashMap<>();
            }

            if (comando.equals("add")) {
                // Adiciona produto ao carrinho
                carrinho.put(produtoId, carrinho.getOrDefault(produtoId, 0) + 1);
            } else if (comando.equals("remove")) {
                // Remove produto do carrinho
                if (carrinho.containsKey(produtoId)) {
                    int quantidade = carrinho.get(produtoId);
                    if (quantidade <= 1) {
                        carrinho.remove(produtoId);
                    } else {
                        carrinho.put(produtoId, quantidade - 1);
                    }
                }
            }

            session.setAttribute("carrinho", carrinho);
        }

        // Após adicionar/remover, redireciona para a página do clienteHome
        response.sendRedirect("/clienteHome");
    }
}

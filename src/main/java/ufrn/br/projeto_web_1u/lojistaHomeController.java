package ufrn.br.projeto_web_1u;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class lojistaHomeController {

    @RequestMapping(value = "/lojistaHome", method = RequestMethod.GET)
    public void lojistaHome(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        var writer = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioAutenticado") == null) {
            response.sendRedirect("/login");
            return;
        }

        writer.println("<html><head><title>Home do Lojista</title></head><body>");
        writer.println("<h1>Bem-vindo Lojista!</h1>");

        // Botão para deslogar
        writer.println("<form action='/logout' method='post'>");
        writer.println("<input type='submit' value='Deslogar'/>");
        writer.println("</form>");

        // Botão para cadastrar produtos
        writer.println("<form action='/cadastrarProduto' method='get'>");
        writer.println("<input type='submit' value='Cadastrar Produto'/>");
        writer.println("</form>");

        // Botão para listar produtos
        writer.println("<form action='/listarProdutos' method='get'>");
        writer.println("<input type='submit' value='Listar Produtos'/>");
        writer.println("</form>");

        writer.println("</body></html>");
        writer.close();
    }

    // Controlador para logout
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destrói a sessão
        }
        response.sendRedirect("/login"); // Volta para login
    }





    
}

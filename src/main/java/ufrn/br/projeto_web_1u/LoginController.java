package ufrn.br.projeto_web_1u;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private UsuarioBanco usuarioBanco = new UsuarioBanco(); // Instancia o DAO

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void getLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        var writer = response.getWriter();

        String erro = request.getParameter("erro");
        String sucessoCadastro = request.getParameter("sucessoCadastro");

        writer.println("<html><head><title>Login</title></head><body>");

        if ("credenciais_invalidas".equals(erro)) {
            writer.println("<p style='color:red;'>Erro: Email ou senha inválidos!</p>");
        }
        if ("true".equals(sucessoCadastro)) {
            writer.println("<p style='color:green;'>Cadastro realizado com sucesso. Faça o login!</p>");
        }

        writer.println("<h2>Login</h2>");
        writer.println("<form action='/login' method='post'>");
        writer.println("Email: <input type='text' name='email'/><br/>");
        writer.println("Senha: <input type='password' name='senha'/><br/>");
        writer.println("<input type='submit' value='Entrar'/>");
        writer.println("</form>");

        writer.println("<form action='/cadastro' method='get'>");
        writer.println("<input type='submit' value='Cadastrar novo usuário'/>");
        writer.println("</form>");

        writer.println("</body></html>");
        writer.close();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void postLogin(@RequestParam String email, @RequestParam String senha,
                          HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            Usuario usuario = usuarioBanco.buscarPorEmailSenha(email, senha);

            if (usuario != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioAutenticado", usuario);
                session.setMaxInactiveInterval(1200); // Expira em 20 minutos

                if (senha.equals("lojista123") || usuario.getEmail().equalsIgnoreCase("tanirocr@gmail.com") || usuario.getEmail().equalsIgnoreCase("lore_sil@yahoo.com.br")) {
                    response.sendRedirect("/lojistaHome");
                } else {
                    response.sendRedirect("/clienteHome");
                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no login: " + e.getMessage());
            return;
        }

        response.sendRedirect("/login?erro=credenciais_invalidas");
    }
}

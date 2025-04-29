package ufrn.br.projeto_web_1u;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CadastroController {

    private UsuarioBanco usuarioBanco = new UsuarioBanco(); // instancia o "DAO"

    @RequestMapping(value = "/cadastro", method = RequestMethod.GET)
    public void getCadastro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        var writer = response.getWriter();

        String erro = request.getParameter("erro");
        String sucesso = request.getParameter("sucesso");

        writer.println("<html><head><title>Cadastro</title></head><body>");

        if ("email_duplicado".equals(erro)) {
            writer.println("<p style='color:red;'>Erro: Email já cadastrado!</p>");
        }
        if ("true".equals(sucesso)) {
            writer.println("<p style='color:green;'>Cadastro realizado com sucesso!</p>");
        }

        writer.println("<h2>Cadastro de Usuário</h2>");
        writer.println("<form action='/cadastro' method='post'>");
        writer.println("Nome: <input type='text' name='nome'/><br/>");
        writer.println("Email: <input type='text' name='email'/><br/>");
        writer.println("Senha: <input type='password' name='senha'/><br/>");
        writer.println("<input type='submit' value='Cadastrar'/>");
        writer.println("</form>");

        // Botão para voltar para login
        writer.println("<br/><form action='/login' method='get'>");
        writer.println("<input type='submit' value='Voltar para Login'/>");
        writer.println("</form>");

        writer.println("</body></html>");
        writer.close();
    }

    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    public void postCadastro(@RequestParam String nome, @RequestParam String email, @RequestParam String senha,
                             HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Usuario usuarioExistente = usuarioBanco.buscarPorEmailSenha(email, senha);

            if (usuarioExistente != null) {
                response.sendRedirect("/cadastro?erro=email_duplicado");
                return;
            }

            Usuario novoUsuario = new Usuario(nome, email, senha);
            usuarioBanco.cadastrarUsuario(novoUsuario);

            response.sendRedirect("/cadastro?sucesso=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no cadastro: " + e.getMessage());
        }
    }
}

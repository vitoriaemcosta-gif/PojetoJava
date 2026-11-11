package controller;

import dao.Conexao;
import dao.InvestidorDAO;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Investidor;
import view.Cadastro;
import view.Login;

public class ControleCadastro {
    // Referência para a View (Interface Gráfica) de Cadastro
    private Cadastro tela;

    // Construtor que recebe a instância da tela de cadastro
    public ControleCadastro(Cadastro tela) {
        this.tela = tela;
    }

    // Método responsável por validar os dados e realizar o cadastro do investidor
    public void salvarInvestidor() {
        // Recupera os valores digitados nos campos da tela e limpa o CPF 
        //(mantendo apenas números)
        String cpf = tela.getTxt_cadcpf().getText().replaceAll("[^0-9]", "");
        String email = tela.getTxt_cademail().getText();
        String senha = tela.getTxt_cadsenha().getText();
        String nome = tela.getTxt_cadnome().getText();
        
        // Validação: Verifica se algum dos campos principais está vazio
        if (cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "Preencha todos os campos!", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return; // Interrompe a execução
        }
        
        // Validação: O CPF deve conter exatamente 11 dígitos numéricos
        if (cpf.length() != 11) {
            JOptionPane.showMessageDialog(tela, "O CPF deve conter exatamente "
                    + "11 dígitos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return; // Interrompe a execução
        }

        // Validação: A senha deve ser estritamente numérica e conter exatamente 6 dígitos
        if (!senha.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(tela, "A senha deve conter exatamente "
                    + "6 dígitos numéricos!", "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
            return; // Interrompe a execução
        }

        // Cria e preenche o objeto Model com os dados validados
        Investidor investidor = new Investidor();
        investidor.setCpf(cpf);
        investidor.setEmail(email);
        investidor.setNome(nome);
        investidor.setSenha(senha);

        // Instancia a classe de conexão com o banco de dados
        Conexao conexao = new Conexao();

        try {
            // Estabelece a ligação com o PostgreSQL e chama o 
            //DAO para inserir o registo
            Connection conn = conexao.getConnection();
            InvestidorDAO dao = new InvestidorDAO(conn);
            dao.inserir(investidor);
            
            // Informa ao utilizador que o registo foi bem-sucedido
            JOptionPane.showMessageDialog(tela, "Cadastro realizado com "
                    + "sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Abre a tela de Login e fecha a janela de cadastro atual
            Login login = new Login();
            login.setVisible(true);
            tela.dispose(); 
            
        } catch (SQLException ex) {
            // Trata eventuais erros de banco de dados (ex: CPF duplicado)
            JOptionPane.showMessageDialog(tela, "Erro ao cadastrar "
                    + "investidor!", "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
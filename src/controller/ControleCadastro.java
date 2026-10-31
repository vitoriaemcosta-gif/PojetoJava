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
    private Cadastro tela;

    public ControleCadastro(Cadastro tela) {
        this.tela = tela;
    }

    public void salvarInvestidor() {
        String cpf = tela.getTxt_cadcpf().getText().replaceAll("[^0-9]", "");
        String email = tela.getTxt_cademail().getText();
        String senha = tela.getTxt_cadsenha().getText();
        String nome = tela.getTxt_cadnome().getText();
        
        if (cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validação: CPF deve ter exatamente 11 dígitos numéricos
        if (cpf.length() != 11) {
            JOptionPane.showMessageDialog(tela, "O CPF deve conter exatamente 11 dígitos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validação: Senha numérica com exatamente 6 dígitos
        if (!senha.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(tela, "A senha deve conter exatamente 6 dígitos numéricos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Investidor investidor = new Investidor();
        investidor.setCpf(cpf);
        investidor.setEmail(email);
        investidor.setNome(nome);
        investidor.setSenha(senha);

        Conexao conexao = new Conexao();

        try {
            Connection conn = conexao.getConnection();
            InvestidorDAO dao = new InvestidorDAO(conn);
            dao.inserir(investidor);
            
            JOptionPane.showMessageDialog(tela, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            // Abre a tela de Login
            Login login = new Login();
            login.setVisible(true);
            // Fecha a tela de cadastro
            tela.dispose(); 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tela, "Erro ao cadastrar investidor!", "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.Conexao;
import dao.InvestidorDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Investidor;
import view.Login;
import view.MenuPrincipal;

/**
 *
 * @author vitor
 */
public class ControleLogin {
    private Login tela;

    public ControleLogin(Login tela) {
        this.tela = tela;
    }
    
    public void loginInvestidor() {
        // Pega o CPF e a Senha digitados na View
        String cpf = tela.getTxt_cpf().getText().replaceAll("[^0-9]", "");
        String senha = tela.getTxt_senha().getText();
        
        if (cpf.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "Preencha o CPF e a senha!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!cpf.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(tela, "O CPF deve conter exatamente 11 dígitos numéricos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!senha.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(tela, "A senha deve conter exatamente 6 dígitos numéricos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Investidor investidor = new Investidor();
        investidor.setCpf(cpf);
        investidor.setSenha(senha);

        Conexao conexao = new Conexao();

        try {
            Connection conn = conexao.getConnection();
            InvestidorDAO dao = new InvestidorDAO(conn);
            ResultSet res = dao.consultar(investidor);

            if (res.next()) {
                // Preenche o objeto investidor com os dados vindos do PostgreSQL
                investidor.setNome(res.getString("nome"));
                investidor.setSaldoReais(res.getDouble("saldo_reais"));
                investidor.setSaldoBtc(res.getDouble("saldo_btc"));
                investidor.setSaldoEth(res.getDouble("saldo_eth"));
                investidor.setSaldoXrp(res.getDouble("saldo_xrp"));
                
                JOptionPane.showMessageDialog(tela, 
                        "Login efetuado com sucesso!", "Sucesso", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Abre a tela principal passando o investidor logado e fecha o login
                MenuPrincipal menuPrincipal = new MenuPrincipal(investidor);
                menuPrincipal.setVisible(true);
                tela.dispose();
                
                conn.close();
            } else {
                JOptionPane.showMessageDialog(tela, "CPF ou senha incorretos!", 
                        "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela, 
                    "Erro de conexão com o banco de dados!", "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
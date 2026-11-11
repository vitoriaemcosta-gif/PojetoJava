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
    // Referência para a View (Interface Gráfica) de Login
    private Login tela;

    // Construtor que inicializa o controlador recebendo a instância da tela de login
    public ControleLogin(Login tela) {
        this.tela = tela;
    }
    
    // Método principal responsável por autenticar o investidor no sistema
    public void loginInvestidor() {
        // Recolhe o CPF inserido na tela (removendo caracteres especiais e mantendo apenas números) e a senha
        String cpf = tela.getTxt_cpf().getText().replaceAll("[^0-9]", "");
        String senha = tela.getTxt_senha().getText();
        
        // Validação: Verifica se os campos de CPF ou senha estão vazios
        if (cpf.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "Preencha o CPF e a senha!", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return; // Interrompe a execução
        }

        // Validação: Garante que o CPF contém exatamente 11 dígitos numéricos
        if (!cpf.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(tela, "O CPF deve conter exatamente 11 "
                    + "dígitos numéricos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return; // Interrompe a execução
        }

        // Validação: Garante que a senha contém exatamente 6 dígitos numéricos
        if (!senha.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(tela, "A senha deve conter exatamente "
                    + "6 dígitos numéricos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return; // Interrompe a execução
        }

        // Cria uma instância do Model `Investidor` e define o CPF e a senha 
        //fornecidos
        Investidor investidor = new Investidor();
        investidor.setCpf(cpf);
        investidor.setSenha(senha);

        // Inicializa a classe de conexão com a base de dados
        Conexao conexao = new Conexao();

        try {
            // Estabelece a ligação com o PostgreSQL, instancia o DAO e efetua a 
            //consulta das credenciais
            Connection conn = conexao.getConnection();
            InvestidorDAO dao = new InvestidorDAO(conn);
            ResultSet res = dao.consultar(investidor);

            // Verifica se foi encontrado um registo correspondente na base de dados
            if (res.next()) {
                // Preenche o objeto investidor com os dados adicionais vindos 
                //do PostgreSQL (nome e saldos)
                investidor.setNome(res.getString("nome"));
                investidor.setSaldoReais(res.getDouble("saldo_reais"));
                investidor.setSaldoBtc(res.getDouble("saldo_btc"));
                investidor.setSaldoEth(res.getDouble("saldo_eth"));
                investidor.setSaldoXrp(res.getDouble("saldo_xrp"));
                
                // Exibe uma mensagem de sucesso ao utilizador
                JOptionPane.showMessageDialog(tela, 
                        "Login efetuado com sucesso!", "Sucesso", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Abre a tela principal (MenuPrincipal) passando o investidor 
                //logado e fecha a tela de login
                MenuPrincipal menuPrincipal = new MenuPrincipal(investidor);
                menuPrincipal.setVisible(true);
                tela.dispose();
                
                // Fecha a conexão com a base de dados
                conn.close();
            } else {
                // Exibe aviso caso o CPF ou a senha estejam incorretos
                JOptionPane.showMessageDialog(tela, "CPF ou senha incorretos!", 
                        "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            // Trata eventuais erros de conexão ou falhas de SQL
            JOptionPane.showMessageDialog(tela, 
                    "Erro de conexão com o banco de dados!", "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
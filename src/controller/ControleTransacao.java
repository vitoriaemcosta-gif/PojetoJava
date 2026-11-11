/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author vitor
 */
public class ControleTransacao {

    // Método responsável por registar uma nova transação na base de dados PostgreSQL
    public boolean registrarTransacao(String cpf, String dataHora, String tipo, 
                                      String moeda, double valor, double taxa, 
                                      double saldoReaisApos, double saldoBtcApos, 
                                      double saldoEthApos, double saldoXrpApos) {
        
        // Instancia a classe de conexão para obter o acesso à base de dados
        Conexao conexao = new Conexao();
        
        // Instrução SQL INSERT parametrizada para inserir os dados da operação 
        //na tabela `transacao`
        String sql = "INSERT INTO transacao (cpf, data_hora, tipo, moeda, valor, "
                + "taxa, saldo_reais_apos, saldo_btc_apos, saldo_eth_apos, "
                + "saldo_xrp_apos) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Utiliza try-with-resources para garantir o fecho seguro da conexão 
        //e do PreparedStatement
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Atribui os respetivos valores aos parâmetros da query 
            //SQL de forma segura (prevenindo SQL Injection)
            stmt.setString(1, cpf);
            stmt.setString(2, dataHora);
            stmt.setString(3, tipo);
            stmt.setString(4, moeda);
            stmt.setDouble(5, valor);
            stmt.setDouble(6, taxa);
            stmt.setDouble(7, saldoReaisApos);
            stmt.setDouble(8, saldoBtcApos);
            stmt.setDouble(9, saldoEthApos);
            stmt.setDouble(10, saldoXrpApos);
            
            // Executa a instrução de inserção na base de dados
            stmt.executeUpdate();
            return true; // Retorna verdadeiro se o registo for bem-sucedido
            
        } catch (SQLException e) {
            e.printStackTrace(); // Regista o erro na consola caso ocorra falha de SQL
            return false; // Retorna falso se a operação falhar
        }
    }
}
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

    public boolean registrarTransacao(String cpf, String dataHora, String tipo, String moeda, double valor, double taxa, 
                                      double saldoReaisApos, double saldoBtcApos, double saldoEthApos, double saldoXrpApos) {
        Conexao conexao = new Conexao();
        String sql = "INSERT INTO transacao (cpf, data_hora, tipo, moeda, valor, taxa, saldo_reais_apos, saldo_btc_apos, saldo_eth_apos, saldo_xrp_apos) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
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
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
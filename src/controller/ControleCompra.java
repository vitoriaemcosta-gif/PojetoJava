/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Bitcoin;
import model.Ethereum;
import model.Investidor;
import model.Ripple;
/**
 *
 * @author vitor
 */

public class ControleCompra {

    private Investidor investidor;

    public ControleCompra(Investidor investidor) {
        this.investidor = investidor;
    }

    // Método para realizar a compra recebendo a sigla ("BTC", "ETH", "XRP"), o valor em reais investido e a cotação atual
    public boolean realizarCompra(String tipoMoeda, double valorReaisGasto, double cotacaoAtual) {
        if (valorReaisGasto <= 0) {
            return false; // Valor inválido
        }

        double taxa = 0.0;
        double quantidadeMoedaAdquirida = 0.0;

        // Aplicação do polimorfismo com a interface Tarifacao de acordo com a moeda escolhida
        switch (tipoMoeda.toUpperCase()) {
            case "BTC":
                Bitcoin btc = new Bitcoin(cotacaoAtual);
                taxa = btc.calculaTaxaCompra(valorReaisGasto); // 2% de taxa para Bitcoin[cite: 9]
                break;
            case "ETH":
                Ethereum eth = new Ethereum(cotacaoAtual);
                taxa = eth.calculaTaxaCompra(valorReaisGasto); // 1% de taxa para Ethereum[cite: 9]
                break;
            case "XRP":
                Ripple xrp = new Ripple(cotacaoAtual);
                taxa = xrp.calculaTaxaCompra(valorReaisGasto); // 1% de taxa para Ripple[cite: 9]
                break;
            default:
                return false;
        }

        // O valor total necessário em reais é o valor gasto + a taxa cobrada
        double custoTotalReais = valorReaisGasto + taxa;

        // Verifica se o investidor tem saldo suficiente em Reais (o saldo não pode ficar negativo)
        if (investidor.getSaldoReais() < custoTotalReais) {
            return false; 
        }

        // Calcula a quantidade líquida de criptomoeda comprada (descontando o valor gasto pela cotação)
        quantidadeMoedaAdquirida = valorReaisGasto / cotacaoAtual;

        // Atualiza os saldos na memória do objeto Investidor
        investidor.setSaldoReais(investidor.getSaldoReais() - custoTotalReais);

        switch (tipoMoeda.toUpperCase()) {
            case "BTC":
                investidor.setSaldoBtc(investidor.getSaldoBtc() + quantidadeMoedaAdquirida);
                break;
            case "ETH":
                investidor.setSaldoEth(investidor.getSaldoEth() + quantidadeMoedaAdquirida);
                break;
            case "XRP":
                investidor.setSaldoXrp(investidor.getSaldoXrp() + quantidadeMoedaAdquirida);
                break;
        }

        // Persiste as alterações no banco de dados PostgreSQL
        return atualizarSaldosNoBanco();
    }

    private boolean atualizarSaldosNoBanco() {
        Conexao conexao = new Conexao();
        String sql = "UPDATE investidor SET saldo_reais = ?, saldo_btc = ?, saldo_eth = ?, saldo_xrp = ? WHERE cpf = ?";
        
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, investidor.getSaldoReais());
            stmt.setDouble(2, investidor.getSaldoBtc());
            stmt.setDouble(3, investidor.getSaldoEth());
            stmt.setDouble(4, investidor.getSaldoXrp());
            stmt.setString(5, investidor.getCpf());
            
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
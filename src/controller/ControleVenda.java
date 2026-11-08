/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import dao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Investidor;
import model.*;

/**
 *
 * @author vitor
 */

public class ControleVenda {
    private Investidor investidor;

    public ControleVenda(Investidor investidor) {
        this.investidor = investidor;
    }

    public boolean realizarVenda(String siglaMoeda, double qtdVendida, double cotacaoAtual) {
        if (qtdVendida <= 0) {
            return false;
        }

        Moeda moedaObj = null;
        double saldoDisponivel = 0.0;

        // Identifica a moeda e o saldo respetivo do investidor
       switch (siglaMoeda.toUpperCase()) {
            case "BTC":
                moedaObj = new Bitcoin(cotacaoAtual);
                saldoDisponivel = investidor.getSaldoBtc();
                break;
            case "ETH":
                moedaObj = new Ethereum(cotacaoAtual);
                saldoDisponivel = investidor.getSaldoEth();
                break;
            case "XRP":
                moedaObj = new Ripple(cotacaoAtual);
                saldoDisponivel = investidor.getSaldoXrp();
                break;
            default:
                return false;
        }

        // Verifica se o investidor tem saldo suficiente da criptomoeda
        if (saldoDisponivel < qtdVendida) {
            return false;
        }

        // Calcula o valor bruto e a taxa de venda usando a interface de Tarifação
        double valorBruto = qtdVendida * cotacaoAtual;
        double valorTaxa = ((model.Tarifacao) moedaObj).calculaTaxaVenda(valorBruto);        
        double valorLiquidoReais = valorBruto - valorTaxa;

        // Atualiza os saldos no objeto Investidor
        investidor.setSaldoReais(investidor.getSaldoReais() + valorLiquidoReais);

        switch (siglaMoeda.toUpperCase()) {
            case "BTC":
                investidor.setSaldoBtc(investidor.getSaldoBtc() - qtdVendida);
                break;
            case "ETH":
                investidor.setSaldoEth(investidor.getSaldoEth() - qtdVendida);
                break;
            case "XRP":
                investidor.setSaldoXrp(investidor.getSaldoXrp() - qtdVendida);
                break;
        }

        // Persiste as alterações na base de dados PostgreSQL
        boolean atualizado = atualizarSaldosNoBanco();
        
        if (atualizado) {
            // Regista a transação na tabela transacao após o sucesso da venda
            String dataHoraAtual = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            ControleTransacao ctrlTransacao = new ControleTransacao();
            ctrlTransacao.registrarTransacao(
                investidor.getCpf(),
                dataHoraAtual,
                "-", // ou "VENDA"
                siglaMoeda.toUpperCase(),
                qtdVendida,
                valorTaxa,
                investidor.getSaldoReais(),
                investidor.getSaldoBtc(),
                investidor.getSaldoEth(),
                investidor.getSaldoXrp()
            );
        }

        return atualizado;
    }

    private boolean atualizarSaldosNoBanco() {
        Conexao conexao = new Conexao();
        try (Connection conn = conexao.getConnection()) {
            String sql = "UPDATE investidor SET saldo_reais = ?, saldo_btc = ?, saldo_eth = ?, saldo_xrp = ? WHERE cpf = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, investidor.getSaldoReais());
                stmt.setDouble(2, investidor.getSaldoBtc());
                stmt.setDouble(3, investidor.getSaldoEth());
                stmt.setDouble(4, investidor.getSaldoXrp());
                stmt.setString(5, investidor.getCpf());
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
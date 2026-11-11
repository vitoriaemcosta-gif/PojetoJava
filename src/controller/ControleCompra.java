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

    // Instância do investidor que está a realizar a operação de compra
    private Investidor investidor;

    // Construtor que inicializa o controlador recebendo o investidor logado
    public ControleCompra(Investidor investidor) {
        this.investidor = investidor;
    }

    // Método para realizar a compra recebendo a sigla ("BTC", "ETH", "XRP"), 
    //o valor em reais investido e a cotação atual
    public boolean realizarCompra(String tipoMoeda, double valorReaisGasto, 
            double cotacaoAtual) {
        // Validação: o valor gasto não pode ser menor ou igual a zero
        if (valorReaisGasto <= 0) {
            return false; // Valor inválido
        }

        double taxa = 0.0;
        double quantidadeMoedaAdquirida = 0.0;

        // Aplicação do polimorfismo com a interface Tarifacao de acordo com a 
        //moeda escolhida
        switch (tipoMoeda.toUpperCase()) {
            case "BTC":
                Bitcoin btc = new Bitcoin(cotacaoAtual);
                taxa = btc.calculaTaxaCompra(valorReaisGasto);
                break;
            case "ETH":
                Ethereum eth = new Ethereum(cotacaoAtual);
                taxa = eth.calculaTaxaCompra(valorReaisGasto);
                break;
            case "XRP":
                Ripple xrp = new Ripple(cotacaoAtual);
                taxa = xrp.calculaTaxaCompra(valorReaisGasto);
                break;
            default:
                return false; // Moeda não reconhecida
        }

        // O valor total necessário em reais é o valor gasto + a taxa cobrada 
        //pela corretora
        double custoTotalReais = valorReaisGasto + taxa;

        // Verifica se o investidor tem saldo suficiente em Reais (o saldo não 
        //pode ficar negativo)
        if (investidor.getSaldoReais() < custoTotalReais) {
            return false; 
        }

        // Calcula a quantidade líquida de criptomoeda comprada (dividindo o 
        //valor gasto em reais pela cotação atual)
        quantidadeMoedaAdquirida = valorReaisGasto / cotacaoAtual;

        // Atualiza o saldo de Reais na memória do objeto Investidor (debitando 
        //o custo total com taxas)
        investidor.setSaldoReais(investidor.getSaldoReais() - custoTotalReais);

        // Atualiza o saldo da criptomoeda correspondente na memória do objeto 
        //Investidor
        switch (tipoMoeda.toUpperCase()) {
            case "BTC":
                investidor.setSaldoBtc(investidor.getSaldoBtc() + 
                        quantidadeMoedaAdquirida);
                break;
            case "ETH":
                investidor.setSaldoEth(investidor.getSaldoEth() + 
                        quantidadeMoedaAdquirida);
                break;
            case "XRP":
                investidor.setSaldoXrp(investidor.getSaldoXrp() + 
                        quantidadeMoedaAdquirida);
                break;
        }

        // Persiste as alterações de saldo atualizadas no banco de dados PostgreSQL
        boolean atualizado = atualizarSaldosNoBanco();

        // Se a atualização na base de dados for bem-sucedida, regista a 
        //transação no extrato
        if (atualizado) {
            // Obtém a data e hora atual formatada para o registo
            String dataHoraAtual = java.time.LocalDateTime.now().format
        (java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            ControleTransacao ctrlTransacao = new ControleTransacao();
            
            // Grava os dados detalhados da operação na tabela `transacao`
            ctrlTransacao.registrarTransacao(
                investidor.getCpf(),
                dataHoraAtual,
                "+", // Indica entrada/compra de criptomoeda
                tipoMoeda.toUpperCase(),
                valorReaisGasto,
                taxa,
                investidor.getSaldoReais(),
                investidor.getSaldoBtc(),
                investidor.getSaldoEth(),
                investidor.getSaldoXrp()
            );
        }

        return atualizado;
    }

    // Método auxiliar privado responsável por comunicar com a base de dados e 
    //atualizar os saldos do investidor
    private boolean atualizarSaldosNoBanco() {
        Conexao conexao = new Conexao();
        String sql = "UPDATE investidor SET saldo_reais = ?, saldo_btc = ?, "
                + "saldo_eth = ?, saldo_xrp = ? WHERE cpf = ?";
        
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Define os parâmetros da instrução SQL com os novos valores da memória
            stmt.setDouble(1, investidor.getSaldoReais());
            stmt.setDouble(2, investidor.getSaldoBtc());
            stmt.setDouble(3, investidor.getSaldoEth());
            stmt.setDouble(4, investidor.getSaldoXrp());
            stmt.setString(5, investidor.getCpf());
            
            // Executa a atualização na tabela `investidor`
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna falso caso ocorra algum erro na base de dados
        }
    }
}
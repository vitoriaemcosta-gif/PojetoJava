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
    // Instância do investidor que está a realizar a operação de venda
    private Investidor investidor;

    // Construtor que inicializa o controlador com o investidor autenticado
    public ControleVenda(Investidor investidor) {
        this.investidor = investidor;
    }

    // Método principal para processar a venda de criptomoedas
    public boolean realizarVenda(String siglaMoeda, double qtdVendida, 
                                 double cotacaoAtual) {
        // Validação: a quantidade a vender não pode ser menor ou igual a zero
        if (qtdVendida <= 0) {
            return false;
        }

        Moeda moedaObj = null;
        double saldoDisponivel = 0.0;

        // Identifica a criptomoeda escolhida (BTC, ETH ou XRP) utilizando 
        //polimorfismo e recolhe o saldo respetivo
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
                return false; // Moeda inválida ou não reconhecida
        }

        // Validação: verifica se o investidor possui saldo suficiente da 
        //criptomoeda que deseja vender
        if (saldoDisponivel < qtdVendida) {
            return false;
        }

        // Calcula o valor bruto da venda e aplica a taxa correspondente 
        //utilizando a interface de Tarifação
        double valorBruto = qtdVendida * cotacaoAtual;
        double valorTaxa = ((model.Tarifacao) moedaObj).calculaTaxaVenda(valorBruto);        
        double valorLiquidoReais = valorBruto - valorTaxa;

        // Atualiza o saldo em Reais na memória do objeto Investidor 
        //(adicionando o valor líquido da venda)
        investidor.setSaldoReais(investidor.getSaldoReais() + valorLiquidoReais);

        // Desconta a quantidade vendida do saldo da respetiva criptomoeda na memória
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

        // Persiste as alterações de saldo atualizadas na base de dados PostgreSQL
        boolean atualizado = atualizarSaldosNoBanco();
        
        // Se a atualização na base de dados for bem-sucedida, regista a 
        //transação na tabela de extrato
        if (atualizado) {
            // Obtém a data e hora atual formatada
            String dataHoraAtual = java.time.LocalDateTime.now().format
            (java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            ControleTransacao ctrlTransacao = new ControleTransacao();
            
            // Regista os detalhes da venda na tabela `transacao`
            ctrlTransacao.registrarTransacao(
                investidor.getCpf(),
                dataHoraAtual,
                "-", // Indica saída/venda de criptomoeda
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

    // Método auxiliar privado responsável por comunicar com o PostgreSQL e 
    //atualizar os saldos do investidor
    private boolean atualizarSaldosNoBanco() {
        Conexao conexao = new Conexao();
        try (Connection conn = conexao.getConnection()) {
            String sql = "UPDATE investidor SET saldo_reais = ?, saldo_btc = ?, "
                    + "saldo_eth = ?, saldo_xrp = ? WHERE cpf = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Atribui os novos valores correntes aos parâmetros do SQL
                stmt.setDouble(1, investidor.getSaldoReais());
                stmt.setDouble(2, investidor.getSaldoBtc());
                stmt.setDouble(3, investidor.getSaldoEth());
                stmt.setDouble(4, investidor.getSaldoXrp());
                stmt.setString(5, investidor.getCpf());
                
                // Executa a atualização na tabela `investidor`
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna falso se ocorrer erro de SQL
        }
    }
}
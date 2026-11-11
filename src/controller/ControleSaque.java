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

public class ControleSaque {

    // Instância do investidor que está a efetuar a operação de saque
    private Investidor investidor;

    // Construtor que inicializa o controlador recebendo o investidor logado
    public ControleSaque(Investidor investidor) {
        this.investidor = investidor;
    }

    // Método que executa a regra de negócio do saque em Reais
    public boolean realizarSaque(double valor) {
        // Validação: o valor do saque não pode ser menor ou igual a zero, nem 
        //exceder o saldo atual em Reais
        if (valor <= 0 || investidor.getSaldoReais() < valor) {
            return false; // Valor inválido ou saldo insuficiente
        }

        // Atualiza o saldo em memória na classe Investidor (decrementa o valor 
        //do saque ao saldo atual de reais)
        double novoSaldo = investidor.getSaldoReais() - valor;
        investidor.setSaldoReais(novoSaldo);

        // Atualiza e persiste os dados alterados na base de dados PostgreSQL
        boolean atualizado = atualizarSaldoNoBanco();

        // Se a base de dados for atualizada com sucesso, regista a transação no 
        //extrato
        if (atualizado) {
            // Obtém a data e hora atual formatada para o registo da transação
            String dataHoraAtual = java.time.LocalDateTime.now().
                    format(java.time.format.DateTimeFormatter.ofPattern
                    ("dd-MM-yyyy HH:mm"));
            
            ControleTransacao ctrlTransacao = new ControleTransacao();
            
            // Regista a transação na tabela `transacao` após o sucesso do saque
            ctrlTransacao.registrarTransacao(
                investidor.getCpf(),
                dataHoraAtual,
                "-", // Indica saída/saque de valores
                "REAL",
                valor,
                0.0, // Sem taxa aplicada para operações de saque
                investidor.getSaldoReais(),
                investidor.getSaldoBtc(),
                investidor.getSaldoEth(),
                investidor.getSaldoXrp()
            );
        }

        return atualizado;
    }

    // Método auxiliar privado responsável pela comunicação direta com a base 
    //de dados (PostgreSQL)
    private boolean atualizarSaldoNoBanco() {
        Conexao conexao = new Conexao();
        String sql = "UPDATE investidor SET saldo_reais = ?, saldo_btc = ?, "
                + "saldo_eth = ?, saldo_xrp = ? WHERE cpf = ?";
        
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Define os parâmetros da instrução SQL com os novos valores 
            //correntes da memória
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
            return false; // Retorna falso caso ocorra alguma exceção de SQL
        }
    }
}
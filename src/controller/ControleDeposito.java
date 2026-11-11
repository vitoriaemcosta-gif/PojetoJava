package controller;

import dao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Investidor;

public class ControleDeposito {

    // Instância do investidor que está a efetuar o depósito
    private Investidor investidor;

    // Construtor que recebe o investidor logado
    public ControleDeposito(Investidor investidor) {
        this.investidor = investidor;
    }

    // Método que executa a regra de negócio do depósito em Reais
    public boolean realizarDeposito(double valor) {
        // Validação: o valor do depósito não pode ser menor ou igual a zero
        if (valor <= 0) {
            return false; // Valor inválido
        }

        // Atualiza o saldo em memória na classe Investidor 
        //(soma o valor depositado ao saldo atual de reais)
        double novoSaldo = investidor.getSaldoReais() + valor;
        investidor.setSaldoReais(novoSaldo);

        // Atualiza e persiste os dados na base de dados PostgreSQL
        boolean atualizado = atualizarSaldoNoBanco();

        // Se a base de dados for atualizada com sucesso, regista a operação no extrato
        if (atualizado) {
            // Obtém a data e hora atual formatada para o registo da transação
            String dataHoraAtual = java.time.LocalDateTime.now().
                    format(java.time.format.DateTimeFormatter.ofPattern
                    ("dd-MM-yyyy HH:mm"));
            
            ControleTransacao ctrlTransacao = new ControleTransacao();
            
            // Regista a transação na tabela `transacao` após o sucesso do depósito
            ctrlTransacao.registrarTransacao(
                investidor.getCpf(),
                dataHoraAtual,
                "+", // Indica entrada/depósito de valores
                "REAL",
                valor,
                0.0, // Sem taxa aplicada para operações de depósito
                investidor.getSaldoReais(),
                investidor.getSaldoBtc(),
                investidor.getSaldoEth(),
                investidor.getSaldoXrp()
            );
        }

        return atualizado;
    }

    // Método auxiliar privado responsável pela comunicação com o banco de 
    //dados (PostgreSQL)
    private boolean atualizarSaldoNoBanco() {
        Conexao conexao = new Conexao();
        String sql = "UPDATE investidor SET saldo_reais = ?, saldo_btc = ?, "
                + "saldo_eth = ?, saldo_xrp = ? WHERE cpf = ?";
        
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Define os parâmetros com os saldos atualizados na memória
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
            return false; // Retorna falso caso ocorra algum erro de SQL
        }
    }
}
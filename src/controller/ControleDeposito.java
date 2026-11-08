package controller;

import dao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Investidor;

public class ControleDeposito {

    private Investidor investidor;

    public ControleDeposito(Investidor investidor) {
        this.investidor = investidor;
    }

    // Método que executa a regra de negócio do depósito
    public boolean realizarDeposito(double valor) {
        if (valor <= 0) {
            return false; // Valor inválido
        }

        // Atualiza o saldo em memória na classe Investidor
        double novoSaldo = investidor.getSaldoReais() + valor;
        investidor.setSaldoReais(novoSaldo);

        // Atualiza os dados na base de dados PostgreSQL
        boolean atualizado = atualizarSaldoNoBanco();

        if (atualizado) {
            // Regista a transação na tabela transacao após o sucesso do depósito
            String dataHoraAtual = java.time.LocalDateTime.now().
                    format(java.time.format.DateTimeFormatter.ofPattern
                    ("dd-MM-yyyy HH:mm"));
            ControleTransacao ctrlTransacao = new ControleTransacao();
            ctrlTransacao.registrarTransacao(
                investidor.getCpf(),
                dataHoraAtual,
                "+", // Indica entrada/depósito
                "REAL",
                valor,
                0.0, // Sem taxa para depósito
                investidor.getSaldoReais(),
                investidor.getSaldoBtc(),
                investidor.getSaldoEth(),
                investidor.getSaldoXrp()
            );
        }

        return atualizado;
    }

    // Comunicação com o banco de dados
    private boolean atualizarSaldoNoBanco() {
        Conexao conexao = new Conexao();
        String sql = "UPDATE investidor SET saldo_reais = ?, saldo_btc = ?, "
                + "saldo_eth = ?, saldo_xrp = ? WHERE cpf = ?";
        
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
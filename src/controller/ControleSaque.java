package controller;

import dao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Investidor;

public class ControleSaque {

    private Investidor investidor;

    public ControleSaque(Investidor investidor) {
        this.investidor = investidor;
    }

    // Método que executa a regra de negócio do saque
    public boolean realizarSaque(double valor) {
        if (valor <= 0 || investidor.getSaldoReais() < valor) {
            return false; // Valor inválido ou saldo insuficiente
        }

        // Atualiza o saldo em memória na classe Investidor (decrementa)
        double novoSaldo = investidor.getSaldoReais() - valor;
        investidor.setSaldoReais(novoSaldo);

        // Atualiza os dados na base de dados PostgreSQL
        return atualizarSaldoNoBanco();
    }

    // Comunicação com o banco de dados
    private boolean atualizarSaldoNoBanco() {
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
package controller;

import dao.Conexao;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Investidor;

public class ControleExtrato {
    private Investidor investidor;

    public ControleExtrato(Investidor investidor) {
        this.investidor = investidor;
    }

    // Retorna uma lista com as linhas formatadas do extrato (ou as últimas 5 para a tela)
    public ArrayList<String> obterTodasTransacoes() {
        ArrayList<String> transacoes = new ArrayList<>();
        Conexao conexao = new Conexao();
        
        // Exemplo de consulta à tabela de transações ordenada por data decrescente
        String sql = "SELECT data_hora, tipo, moeda, valor, taxa, saldo_reais_apos, saldo_btc_apos, saldo_eth_apos, saldo_xrp_apos " +
                     "FROM transacao WHERE cpf = ? ORDER BY data_hora DESC";
        
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, investidor.getCpf());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String linha = String.format("%s %s R$ %.2f %s CT: %.2f TX: %.2f REAL: %.2f BTC: %.4f ETH: %.4f XRP: %.4f",
                        rs.getString("data_hora"),
                        rs.getString("tipo"),
                        rs.getDouble("valor"),
                        rs.getString("moeda"),
                        0.0, // Cotação se aplicável
                        rs.getDouble("taxa"),
                        rs.getDouble("saldo_reais_apos"),
                        rs.getDouble("saldo_btc_apos"),
                        rs.getDouble("saldo_eth_apos"),
                        rs.getDouble("saldo_xrp_apos")
                    );
                    transacoes.add(linha);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Se ainda não houver tabela de transações no banco, retorna uma lista simulada/vazia para teste
        if (transacoes.isEmpty()) {
            transacoes.add("Nenhuma transação registada ainda.");
        }
        
        return transacoes;
    }

    public String gerarTextoExtratoCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(investidor.getNome()).append("\n");
        sb.append("CPF: ").append(investidor.getCpf()).append("\n\n");
        sb.append("--- Histórico Completo de Operações ---\n");
        
        ArrayList<String> todas = obterTodasTransacoes();
        for (String t : todas) {
            sb.append(t).append("\n");
        }
        
        return sb.toString();
    }

    // Grava o extrato completo em arquivo texto usando FileWriter e PrintWriter
    public boolean salvarExtratoEmArquivo(String nomeArquivo) {
        try (FileWriter arquivo = new FileWriter(nomeArquivo);
             PrintWriter escritaArquivo = new PrintWriter(arquivo)) {
            
            escritaArquivo.print(gerarTextoExtratoCompleto());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
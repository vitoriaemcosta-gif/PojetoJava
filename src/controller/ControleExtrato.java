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
    // Instância do investidor logado para filtrar o extrato pelo CPF
    private Investidor investidor;

    // Construtor que inicializa o controlador com o investidor atual
    public ControleExtrato(Investidor investidor) {
        this.investidor = investidor;
    }

    // Retorna uma lista contendo todas as linhas formatadas do extrato obtidas 
    //da base de dados
    public ArrayList<String> obterTodasTransacoes() {
        ArrayList<String> transacoes = new ArrayList<>();
        Conexao conexao = new Conexao();
        
        // Consulta SQL para selecionar o histórico de transações do investidor 
        //ordenado da mais recente para a mais antiga
        String sql = "SELECT data_hora, tipo, moeda, valor, taxa, "
                + "saldo_reais_apos, saldo_btc_apos, saldo_eth_apos, "
                + "saldo_xrp_apos " + "FROM transacao WHERE cpf = ? "
                + "ORDER BY data_hora DESC";
        
        // Estabelece a conexão e prepara a consulta protegida contra SQL Injection
        try (Connection conn = conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Define o CPF do investidor no parâmetro da query
            stmt.setString(1, investidor.getCpf());
            
            // Executa a consulta e percorre os resultados retornados
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Formata cada registo da base de dados numa linha de texto legível
                    String linha = String.format("%s %s R$ %.2f %s CT: %.2f "
                            + "TX: %.2f REAL: %.2f BTC: %.4f ETH: %.4f XRP: %.4f",
                        rs.getString("data_hora"),
                        rs.getString("tipo"),
                        rs.getDouble("valor"),
                        rs.getString("moeda"),
                        0.0, // Cotação placeholder se aplicável
                        rs.getDouble("taxa"),
                        rs.getDouble("saldo_reais_apos"),
                        rs.getDouble("saldo_btc_apos"),
                        rs.getDouble("saldo_eth_apos"),
                        rs.getDouble("saldo_xrp_apos")
                    );
                    // Adiciona a linha formatada à lista de transações
                    transacoes.add(linha);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Regista o erro na consola caso ocorra falha de SQL
        }
        
        // Se o investidor ainda não tiver transações registadas, adiciona uma 
        //mensagem informativa
        if (transacoes.isEmpty()) {
            transacoes.add("Nenhuma transação registada ainda.");
        }
        
        return transacoes;
    }

    // Gera o texto completo do extrato estruturado (com cabeçalho, dados do 
    //investidor e histórico)
    public String gerarTextoExtratoCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(investidor.getNome()).append("\n");
        sb.append("CPF: ").append(investidor.getCpf()).append("\n\n");
        sb.append("--- Histórico Completo de Operações ---\n");
        
        // Obtém todas as transações e concatena ao texto
        ArrayList<String> todas = obterTodasTransacoes();
        for (String t : todas) {
            sb.append(t).append("\n");
        }
        
        return sb.toString();
    }

    // Grava o extrato completo num ficheiro de texto utilizando FileWriter e PrintWriter
    public boolean salvarExtratoEmArquivo(String nomeArquivo) {
        try (FileWriter arquivo = new FileWriter(nomeArquivo);
             PrintWriter escritaArquivo = new PrintWriter(arquivo)) {
            
            // Escreve todo o conteúdo gerado no ficheiro de texto
            escritaArquivo.print(gerarTextoExtratoCompleto());
            return true; // Retorna verdadeiro se a gravação for bem-sucedida
            
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Retorna falso se ocorrer erro de E/S (Input/Output)
        }
    }
}
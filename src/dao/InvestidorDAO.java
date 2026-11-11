package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Investidor;

public class InvestidorDAO {
    // Variável de instância para armazenar a conexão ativa com a base de dados
    private Connection conn;

    // Construtor que recebe a conexão ativa por injeção de dependência ao 
    //instanciar o DAO
    public InvestidorDAO(Connection conn) {
        this.conn = conn;
    }

    // Método responsável por consultar um investidor na base de dados 
    public ResultSet consultar(Investidor investidor) throws SQLException {
        // Instrução SQL parametrizada para buscar o registo correspondente 
        String sql = "SELECT * FROM investidor WHERE cpf = ? AND senha = ?";
        
        // Prepara a instrução SQL utilizando a conexão recebida
        PreparedStatement statement = conn.prepareStatement(sql);
        
        // Atribui o CPF e a senha do objeto investidor aos parâmetros da query
        statement.setString(1, investidor.getCpf());
        statement.setString(2, investidor.getSenha());
        
        // Executa a consulta na base de dados
        statement.execute();
        
        // Obtém e armazena o resultado da consulta num ResultSet para 
        //posterior leitura
        ResultSet resultado = statement.getResultSet();
        return resultado; // Retorna o conjunto de resultados encontrado
    }

    // Método responsável por inserir um novo registo de investidor 
    //na tabela `investidor` do PostgreSQL
    public void inserir(Investidor investidor) throws SQLException {
        // Instrução SQL INSERT parametrizada para inserir os dados 
        //principais do novo utilizador
        String sql = "INSERT INTO investidor (cpf, nome, senha, email) "
                + "VALUES (?, ?, ?, ?)";
        
        // Prepara a instrução SQL
        PreparedStatement statement = conn.prepareStatement(sql);
        
        // Atribui os respetivos valores do objeto investidor aos parâmetros da query
        statement.setString(1, investidor.getCpf());
        statement.setString(2, investidor.getNome());
        statement.setString(3, investidor.getSenha());
        statement.setString(4, investidor.getEmail());
        
        // Executa a operação de inserção na base de dados
        statement.execute();
        
        // Fecha a ligação com a base de dados após concluir a inserção
        conn.close();
    }
}
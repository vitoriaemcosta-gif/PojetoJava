package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Investidor;

public class InvestidorDAO {
    private Connection conn;

    public InvestidorDAO(Connection conn) {
        this.conn = conn;
    }

    public ResultSet consultar(Investidor investidor) throws SQLException {
        // Ajustado para o nome da sua tabela: investidor
        String sql = "SELECT * FROM investidor WHERE cpf = ? AND senha = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, investidor.getCpf());
        statement.setString(2, investidor.getSenha());
        statement.execute();
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }

    public void inserir(Investidor investidor) throws SQLException {
        String sql = "INSERT INTO investidor (cpf, nome, senha, email) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, investidor.getCpf());
        statement.setString(2, investidor.getNome());
        statement.setString(3, investidor.getSenha());
        statement.setString(4, investidor.getEmail());
        statement.execute();
        conn.close();
    }
}
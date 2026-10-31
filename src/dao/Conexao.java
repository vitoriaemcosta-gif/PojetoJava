package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    public Connection getConnection() throws SQLException {
        Connection conexao = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/exchange_db",
            "postgres",
            "vitoria" // <--- Sua senha do PostgreSQL
        );
        return conexao;
    }
}
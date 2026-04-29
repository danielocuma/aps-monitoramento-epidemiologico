package model.dao;

import model.exceptions.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_doencas";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    static {
        if (PASSWORD == null) {
            throw new IllegalStateException("DB_PASSWORD não definida no ambiente");
        }
    }

    // ✔ construtor privado (resolve o aviso)
    private ConnectionFactory() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DaoException("Erro ao conectar ao banco de dados", e);
        }
    }
}
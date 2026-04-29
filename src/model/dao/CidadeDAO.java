package model.dao;

import model.entities.Cidade;
import model.exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CidadeDAO {

    public List<Cidade> listar() {

        List<Cidade> lista = new ArrayList<>();

        String sql = "SELECT id, nome, populacao FROM cidade";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Integer id = rs.getInt("id");
                String nome = rs.getString("nome");
                int populacao = rs.getInt("populacao");

                Cidade cidade = new Cidade(id, nome, populacao);
                lista.add(cidade);
            }

        } catch (SQLException e) {
            throw new DaoException("Erro ao listar cidades", e);
        }

        return lista;
    }
}
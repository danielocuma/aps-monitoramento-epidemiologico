package model.dao;

import model.entities.Cidade;
import model.entities.Coleta;
import model.exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ColetaDAO {

    // INSERT
    public void inserir(Coleta coleta) {

        String sql = "INSERT INTO coleta (cidade_id, data_coleta, casos, obitos) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, coleta.getCidade().getId());
            stmt.setDate(2, Date.valueOf(coleta.getDataColeta()));
            stmt.setInt(3, coleta.getCasos());
            stmt.setInt(4, coleta.getObitos());

            stmt.execute();

        } catch (SQLException e) {
            throw new DaoException(
                    "Erro ao inserir coleta para cidade ID: " + coleta.getCidade().getId(),
                    e
            );
        }
    }

    // SELECT
    public List<Coleta> listarPorCidade(int cidadeId) {

        List<Coleta> lista = new ArrayList<>();

        String sql =
                "SELECT c.id, c.data_coleta, c.casos, c.obitos, " +
                        "ci.nome, ci.populacao " +
                        "FROM coleta c " +
                        "JOIN cidade ci ON c.cidade_id = ci.id " +
                        "WHERE ci.id = ? " +
                        "ORDER BY c.data_coleta DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cidadeId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Cidade cidade = new Cidade(
                        cidadeId,
                        rs.getString("nome"),
                        rs.getInt("populacao")
                );

                Coleta coleta = new Coleta(
                        rs.getInt("id"),
                        rs.getDate("data_coleta").toLocalDate(),
                        rs.getInt("casos"),
                        rs.getInt("obitos"),
                        cidade
                );

                lista.add(coleta);
            }

        } catch (SQLException e) {
            throw new DaoException(
                    "Erro ao listar coletas da cidade (ID: " + cidadeId + ")",
                    e
            );
        }

        return lista;
    }

    // UPDATE
    public void atualizar(Coleta coleta) {

        String sql = "UPDATE coleta SET casos = ?, obitos = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, coleta.getCasos());
            stmt.setInt(2, coleta.getObitos());
            stmt.setInt(3, coleta.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(
                    "Erro ao atualizar coleta (ID: " + coleta.getId() + ")",
                    e
            );
        }
    }
}
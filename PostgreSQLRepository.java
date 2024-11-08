package org.investment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLRepository implements ISecurityRepository {
    private final PostgreSQLConnection connection;

    public PostgreSQLRepository() {
        this.connection = PostgreSQLConnection.getInstance();
    }

    @Override
    public Security getById(int id) {
        String sql = "SELECT * FROM securities WHERE security_id = ?";
        try (PreparedStatement stmt = connection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractSecurityFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BriefSecurity> get_k_n_short_list(int k, int n, String sortField) {
        List<BriefSecurity> result = new ArrayList<>();
        String sql = "SELECT * FROM securities ORDER BY " + sortField + " LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, n);
            stmt.setInt(2, (k - 1) * n);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(extractBriefSecurityFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void addSecurity(Security security) {
        String sql = "INSERT INTO securities (name, type, current_price, expected_return) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, security.getName());
            stmt.setString(2, security.getType());
            stmt.setBigDecimal(3, security.getCurrentPrice());
            stmt.setBigDecimal(4, security.getExpectedReturn());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void replaceSecurity(int id, Security newSecurity) {
        String sql = "UPDATE securities SET name = ?, type = ?, current_price = ?, expected_return = ? WHERE security_id = ?";
        try (PreparedStatement stmt = connection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, newSecurity.getName());
            stmt.setString(2, newSecurity.getType());
            stmt.setBigDecimal(3, newSecurity.getCurrentPrice());
            stmt.setBigDecimal(4, newSecurity.getExpectedReturn());
            stmt.setInt(5, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSecurity(int id) {
        String sql = "DELETE FROM securities WHERE security_id = ?";
        try (PreparedStatement stmt = connection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int get_count() {
        String sql = "SELECT COUNT(*) FROM securities";
        try (Statement stmt = connection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void sort_by_field(String field) {
        // Сортировка происходит на уровне SQL запросов
    }

    private Security extractSecurityFromResultSet(ResultSet rs) throws SQLException {
        return Security.updateExistingSecurity(
                rs.getInt("security_id"),
                rs.getString("name"),
                rs.getString("type"),
                rs.getBigDecimal("current_price"),
                rs.getBigDecimal("expected_return")
        );
    }

    private BriefSecurity extractBriefSecurityFromResultSet(ResultSet rs) throws SQLException {
        return new BriefSecurity(
                rs.getInt("security_id"),
                rs.getString("name"),
                rs.getString("type"),
                rs.getBigDecimal("current_price")
        );
    }
}

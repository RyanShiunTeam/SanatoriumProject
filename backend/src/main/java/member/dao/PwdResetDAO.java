package member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import member.bean.PwdReset;
import utils.HikariCputil;

public class PwdResetDAO {

	// 注入密碼
	public void insert(PwdReset reset) throws SQLException {
		final String sql = "INSERT INTO PwdReset(user_ID, code, expiresAt, used) VALUES (?, ?, ?, ?)";
		try(Connection conn = HikariCputil.getDataSource().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reset.getUserID());
            ps.setString(2, reset.getCode());
            ps.setTimestamp(3, reset.getExpiresAt());
            ps.setBoolean(4, reset.isUsed());
            ps.executeUpdate();
		}
	}
	
    public PwdReset getValidCode(String userID, String code) throws Exception {
        try (Connection conn = HikariCputil.getDataSource().getConnection()) {
            String sql = "SELECT TOP 1 * FROM PwdReset WHERE user_ID = ? AND code = ? AND used = 0 AND expiresAt > GETDATE() ORDER BY expiresAt DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userID);
            ps.setString(2, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PwdReset pr = new PwdReset();
                pr.setId(rs.getInt("id"));
                pr.setUserID(rs.getInt("user_ID"));
                pr.setCode(rs.getString("code"));
                pr.setExpiresAt(rs.getTimestamp("expiresAt"));
                pr.setUsed(rs.getBoolean("used"));
                return pr;
            }
        }
        return null;
    }

    public void markUsed(int id) throws Exception {
        try (Connection conn = HikariCputil.getDataSource().getConnection()) {
            String sql = "UPDATE PwdReset SET used = 1 WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
} 

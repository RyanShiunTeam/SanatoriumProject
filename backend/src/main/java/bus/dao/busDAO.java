package bus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import bus.bean.RehaBus;
import bus.util.HikariUtil;

public class busDAO {

	private final DataSource ds;

	public busDAO() {
		this.ds = HikariUtil.getDataSource();
	}

	// 新增復康巴士資料
	public void insertBus(RehaBus rehabus) {

		String sql = "INSERT INTO rehabus(bus_id, car_dealership, bus_brand," + "bus_model, seat_capacity, "
				+ "wheelchair_capacity, license_plate) VALUES (?,?,?,?,?,?,?)";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(1, rehabus.getBusId());
			preparedStatement.setString(2, rehabus.getCarDealership());
			preparedStatement.setString(3, rehabus.getBusBrand());
			preparedStatement.setString(4, rehabus.getBusModel());
			preparedStatement.setInt(5, rehabus.getSeatCapacity());
			preparedStatement.setInt(6, rehabus.getWheelchairCapacity());
			preparedStatement.setString(7, rehabus.getLicensePlate());

			// 執行 SQL，真正把資料寫入資料庫
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("新增巴士完成");
	}

	// 刪除復康巴士資料
	public void deleteBus(int busId) {

		String sql = "DELETE FROM rehabus WHERE bus_id = ? ";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(1, busId);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("刪除巴士完成");
	}
 
	//修改巴士資料
	public void updateBus(RehaBus rehabus) {

		String sql = "UPDATE rehabus"
				+ " SET seat_capacity =?,"
				+ " wheelchair_capacity =?"
				+ " WHERE bus_id=? ";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(1, rehabus.getSeatCapacity());
			preparedStatement.setInt(2, rehabus.getWheelchairCapacity());
			preparedStatement.setInt(3, rehabus.getBusId());
			
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("修改巴士完成");
	}
	
	//查詢巴士資料(模糊查詢)
	public List<RehaBus> findByCapacityAndId(
		Integer minSeats, 
		Integer maxSeats,
        Integer minWheelchairs, 
        Integer maxWheelchairs,
        Integer BusId
        ) {
		
		List<RehaBus> list = new ArrayList<>();
		String sql = "SELECT * FROM rehabus WHERE (? IS NULL OR seat_capacity       BETWEEN ? AND ?)\r\n"
				+ "                           AND (? IS NULL OR wheelchair_capacity BETWEEN ? AND ?)\r\n"
				+ "                           AND (? IS NULL OR bus_id ) "
				+ "ORDER BY bus_id ";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setObject(1, minSeats);
			preparedStatement.setObject(2, minSeats);
			preparedStatement.setObject(3, maxSeats);
	        // wheelchair_capacity 範圍
			preparedStatement.setObject(4, minWheelchairs);
			preparedStatement.setObject(5, minWheelchairs);
			preparedStatement.setObject(6, maxWheelchairs);
	        // bus_id 範圍
			preparedStatement.setObject(7, BusId);
			preparedStatement.setObject(8, BusId);
			
			
			ResultSet rs = preparedStatement.executeQuery();
			
			 while (rs.next()) {
				 	RehaBus b = new RehaBus();
		            b.setBusId(rs.getInt("bus_id"));
		            b.setCarDealership(rs.getString("car_dealership"));
		            b.setBusBrand(rs.getString("bus_brand"));
		            b.setBusModel(rs.getString("bus_model"));
		            b.setSeatCapacity(rs.getInt("seat_capacity"));
		            b.setWheelchairCapacity(rs.getInt("wheelchair_capacity"));
		            b.setLicensePlate(rs.getString("license_plate"));
		            list.add(b);
		        }
			

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("查詢巴士完成");
		return list;
	}
	

}

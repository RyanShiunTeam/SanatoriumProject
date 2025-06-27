package bus.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import bus.bean.RehaBus;
import bus.util.HikariUtil;

public class BusDAO {

	private final DataSource datasource;

	public BusDAO() throws SQLException {
		this.datasource = HikariUtil.getDataSource();
	}

	// 新增復康巴士資料
	public boolean insertBus(RehaBus rehabus) throws SQLException {

		String sql = "INSERT INTO rehabus(car_dealership, bus_brand, bus_model, seat_capacity, "
				+ "wheelchair_capacity, license_plate) VALUES (?,?,?,?,?,?)";
		try (Connection connection = datasource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, rehabus.getCarDealership());
			preparedStatement.setString(2, rehabus.getBusBrand());
			preparedStatement.setString(3, rehabus.getBusModel());
			preparedStatement.setInt(4, rehabus.getSeatCapacity());
			preparedStatement.setInt(5, rehabus.getWheelchairCapacity());
			preparedStatement.setString(6, rehabus.getLicensePlate());
			return preparedStatement.executeUpdate() > 0;
		}
	}

	// 刪除復康巴士資料
	public boolean deleteBus(int busId) throws SQLException {

		String sql = "DELETE FROM rehabus WHERE bus_id = ? ";
		try (Connection connection = datasource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, busId);
			return preparedStatement.executeUpdate() > 0;
		}
	}

	// 更新巴士資料
	public boolean updateBus(RehaBus rehabus) throws SQLException {

		String sql = "UPDATE rehabus SET seat_capacity =?, wheelchair_capacity =? WHERE bus_id=? ";
		try (Connection connection = datasource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, rehabus.getSeatCapacity());
			preparedStatement.setInt(2, rehabus.getWheelchairCapacity());
			preparedStatement.setInt(3, rehabus.getBusId());
			return preparedStatement.executeUpdate() > 0;
		}
	}

	// 查詢巴士資料(模糊查詢)
	public List<RehaBus> findByFilter(Integer minSeats, Integer maxSeats, Integer minWheels, Integer maxWheels,
			Integer busId) throws SQLException {

		String sql = "SELECT * FROM rehabus WHERE (? IS NULL OR seat_capacity       BETWEEN ? AND ?) "
				+ "                           AND (? IS NULL OR wheelchair_capacity BETWEEN ? AND ?) "
				+ "                           AND (? IS NULL OR bus_id = ? ) " + "ORDER BY bus_id ";
		List<RehaBus> list = new ArrayList<>();
		try (Connection connection = datasource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setObject(1, minSeats);
			preparedStatement.setObject(2, minSeats);
			preparedStatement.setObject(3, maxSeats);

			preparedStatement.setObject(4, minWheels);
			preparedStatement.setObject(5, minWheels);
			preparedStatement.setObject(6, maxWheels);

			preparedStatement.setObject(7, busId);
			preparedStatement.setObject(8, busId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					RehaBus bus = new RehaBus();
					bus.setBusId(resultSet.getInt("bus_id"));
					bus.setCarDealership(resultSet.getString("car_dealership"));
					bus.setBusBrand(resultSet.getString("bus_brand"));
					bus.setBusModel(resultSet.getString("bus_model"));
					bus.setSeatCapacity(resultSet.getInt("seat_capacity"));
					bus.setWheelchairCapacity(resultSet.getInt("wheelchair_capacity"));
					bus.setLicensePlate(resultSet.getString("license_plate"));
					list.add(bus);
				}
			}
		}
		return list;
	}

	// 查詢巴士資料(根據巴士編號查詢單筆)
	public RehaBus findById(int busId) throws SQLException {

		String sql = "SELECT * FROM rehabus WHERE bus_id=?";
		try (Connection connection = datasource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, busId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				if (resultSet.next()) {
					RehaBus RehaBus = new RehaBus();
					RehaBus.setBusId(busId);
					RehaBus.setCarDealership(resultSet.getString("car_dealership"));
					RehaBus.setBusBrand(resultSet.getString("bus_brand"));
					RehaBus.setBusModel(resultSet.getString("bus_model"));
					RehaBus.setSeatCapacity(resultSet.getInt("seat_capacity"));
					RehaBus.setWheelchairCapacity(resultSet.getInt("wheelchair_capacity"));
					RehaBus.setLicensePlate(resultSet.getString("license_plate"));
					return RehaBus;
				}
			}
		}
		return null;
	}

	// 查詢巴士資料(查詢全部)
	public List<RehaBus> findAllBus() throws SQLException {

		String sql = "SELECT bus_id, car_dealership, bus_brand, bus_model, seat_capacity,"
				+ "wheelchair_capacity, license_plate FROM rehabus";
		List<RehaBus> buslist = new ArrayList<>();
		try (Connection connection = datasource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				RehaBus RehaBus = new RehaBus();
				RehaBus.setBusId(resultSet.getInt("bus_id"));
				RehaBus.setCarDealership(resultSet.getString("car_dealership"));
				RehaBus.setBusBrand(resultSet.getString("bus_brand"));
				RehaBus.setBusModel(resultSet.getString("bus_model"));
				RehaBus.setSeatCapacity(resultSet.getInt("seat_capacity"));
				RehaBus.setWheelchairCapacity(resultSet.getInt("wheelchair_capacity"));
				RehaBus.setLicensePlate(resultSet.getString("license_plate"));
				buslist.add(RehaBus);
			}
		}
		return buslist;
	}

	// 匯入csv檔
	public int importFromCsv(String csvFilePath) throws SQLException, IOException {
		String sql = "INSERT INTO rehabus(car_dealership, bus_brand, bus_model, seat_capacity,"
				+ "wheelchair_capacity, license_plate) VALUES (?,?,?,?,?,?)";

		try (Connection connection = datasource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFilePath))) {

			connection.setAutoCommit(false);
			String line;
			int count = 0, batch = 0;
			while ((line = bufferedReader.readLine()) != null) {
				String[] token = line.split(",");
				preparedStatement.setString(1, token[1]);
				preparedStatement.setString(2, token[2]);
				preparedStatement.setString(3, token[3]);
				preparedStatement.setInt(4, Integer.parseInt(token[4]));
				preparedStatement.setInt(5, Integer.parseInt(token[5]));
				preparedStatement.setString(6, token[6]);
				preparedStatement.addBatch();

				if (++batch % 50 == 0) {
					preparedStatement.executeBatch();
					batch = 0;
				}
				count++;
				// 統計總共讀了幾筆
			}
			if (batch > 0)
				preparedStatement.executeBatch();
			connection.commit();
			return count;

		} catch (SQLException | IOException e) {
			throw e;
		}

	}

}

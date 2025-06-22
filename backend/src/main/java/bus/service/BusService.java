package bus.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import bus.bean.RehaBus;
import bus.dao.busDAO;
import bus.util.HikariUtil;

public class BusService {

	private busDAO busDao;
	private DataSource ds;

	public BusService() {
		this.ds = HikariUtil.getDataSource();
		this.busDao = new busDAO();

	}

	public void importFromCsv(String csvFilePath) {
		String sql = "INSERT INTO rehabus"
				+ " (car_dealership, bus_brand, bus_model, seat_capacity, wheelchair_capacity, license_plate)"
				+ " VALUES (?,?,?,?,?,?)";

		try (Connection conn = ds.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			conn.setAutoCommit(false); // 關掉自動提交
			String line = br.readLine(); // 讀 header
			int batchSize = 0;

			while ((line = br.readLine()) != null) {

				String[] cols = line.split(",");

				ps.setString(1, cols[1]); // car_dealership
				ps.setString(2, cols[2]); // bus_brand
				ps.setString(3, cols[3]); // bus_model
				ps.setInt(4, Integer.parseInt(cols[4])); // seat_capacity
				ps.setInt(5, Integer.parseInt(cols[5])); // wheelchair_capacity
				ps.setString(6, cols[6]); // license_plate

				ps.addBatch();
				if (++batchSize % 100 == 0) {
					ps.executeBatch();
					batchSize = 0;
				}
			}
			if (batchSize > 0)
				ps.executeBatch();
			conn.commit();
			System.out.println("CSV 匯入完成，共 " + batchSize + " 筆。");
		} catch (SQLException sqle) {
          
            try {
                ds.getConnection().rollback();
            } catch (SQLException ignore) {}
            sqle.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

	// 新增復康巴士
	public void createBus(RehaBus bus) {
		if (bus.getSeatCapacity() <= 0 || bus.getWheelchairCapacity() <= 0) {
			throw new IllegalArgumentException("座位數量必須大於0");
		}
		busDao.insertBus(bus);
	}

	
	// 刪除復康巴士
	public void removeBus(int busId) {
		if (busId <= 0) {
			throw new IllegalArgumentException("查無此號碼，請輸入正確的數字");
		}
		busDao.deleteBus(busId);
	}

	// 修改指定巴士的座位與輪椅容量
	public void updateBusCapacity(int busId, int seatCapacity, int wheelchairCapacity) {
		if (busId <= 0) {
			throw new IllegalArgumentException("車輛號碼無法使用，請再輸入一次");
		}
		if (seatCapacity <= 0 || wheelchairCapacity <= 0) {
			throw new IllegalArgumentException("座位數量必須大於0");
		}

		RehaBus bus = new RehaBus();
		bus.setBusId(busId);
		bus.setSeatCapacity(seatCapacity);
		bus.setWheelchairCapacity(wheelchairCapacity);
		busDao.updateBus(bus);
	}

	// 根據座位與輪椅容量範圍，以及巴士 ID 範圍查詢復康巴士
	public List<RehaBus> searchBuses(Integer minSeats, Integer maxSeats, Integer minWheelchairs, Integer maxWheelchairs,
			Integer BusId) {

		if (minSeats != null && maxSeats != null && minSeats > maxSeats) {
			throw new IllegalArgumentException("最少一般座位的數量不可以大於最多一般座位的數量");
		}
		if (minWheelchairs != null && maxWheelchairs != null && minWheelchairs > maxWheelchairs) {
			throw new IllegalArgumentException("最少輪椅座位的數量不可以大於最多輪椅座位的數量");
		}

		return busDao.findByCapacityAndId(minSeats, maxSeats, minWheelchairs, maxWheelchairs, BusId);
	}
}
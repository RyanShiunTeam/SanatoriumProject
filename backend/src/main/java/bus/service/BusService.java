package bus.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import bus.bean.RehaBus;
import bus.dao.BusDAO;
import utils.HikariCputil;

public class BusService {

	//商業邏輯
	private BusDAO busDao = new BusDAO();


	// 批次匯入csv，回傳實際匯入筆數
	public int importFromCsv(String csvFilePath) {
		try {
			int count = busDao.importFromCsv(csvFilePath);
			return count;
		} catch (SQLException e) {
			throw new RuntimeException("匯入失敗，請檢察資料庫連線", e);

		} catch (IOException ioe) {
			throw new RuntimeException("讀取csv檔案失敗", ioe);
		}
	}

	// 新增復康巴士
	public boolean createBus(RehaBus bus) {
		try {
			return busDao.insertBus(bus);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
	}

	// 刪除復康巴士
	public boolean deleteBus(int busId) {
		try {
			return busDao.deleteBus(busId);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 修改指定巴士的座位與輪椅容量
	public boolean updateBus(int busId, int seatCapacity, int wheelchairCapacity) {
		try {
			return busDao.updateBus(busId, seatCapacity, wheelchairCapacity);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 根據座位與輪椅容量範圍，以及巴士 ID 範圍查詢復康巴士
	public List<RehaBus> findByFilter(Integer minSeats, Integer maxSeats, Integer minWheelchairs,
			Integer maxWheelchairs) {

		try {
			return busDao.findByFilter(minSeats, maxSeats, minWheelchairs, maxWheelchairs);
		} catch (SQLException e) {
			throw new RuntimeException("依條件查詢復康巴士失敗！", e);
		}
	}

	// 全部查詢
	public List<RehaBus> findAllBus() {

		try {
			return busDao.findAllBus();
		} catch (SQLException e) {
			throw new RuntimeException("查詢所有復康巴士失敗！", e);

		}
	}

	// 根據id查詢單筆復康巴士
	public RehaBus findById(int busId) {
		if (busId <= 0) {
			throw new IllegalArgumentException("編號錯誤，請輸入正確的數字");
		}
		try {
			RehaBus bus = busDao.findById(busId);
			if (bus == null) {
				throw new RuntimeException("找不到編號為" + busId + "的車輛");
			}
			return bus;

		} catch (SQLException e) {
			throw new RuntimeException("查詢復康巴士失敗！", e);
		}
	}


}
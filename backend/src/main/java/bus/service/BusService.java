package bus.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import bus.bean.RehaBus;
import bus.dao.BusDAO;
import utils.HikariCputil;

public class BusService {

	//商業邏輯
	private BusDAO busDao;

	public BusService() throws SQLException {
		HikariCputil.getDataSource();
		this.busDao = new BusDAO();

	}

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
	public void createBus(RehaBus bus) {
		if (bus.getSeatCapacity() <= 0 || bus.getWheelchairCapacity() <= 0)
			throw new IllegalArgumentException("座位與輪椅數量都必須大於0");

		try {
			if (!busDao.insertBus(bus))
				throw new RuntimeException("新增失敗，請稍後再試！");

		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤，新增復康巴士失敗！", e);
		}
	}

	// 刪除復康巴士
	public void deleteBus(int busId) {
		if (busId <= 0)
			throw new IllegalArgumentException("編號錯誤，請輸入正確的數字");

		try {
			if (!busDao.deleteBus(busId))
				throw new RuntimeException("查無此筆，刪除失敗！");

		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤，新增復康巴士失敗！", e);
		}
	}

	// 修改指定巴士的座位與輪椅容量
	public  void updateBus(int busId, int seatCapacity, int wheelchairCapacity) {
		if (busId <= 0)
			throw new IllegalArgumentException("車輛號碼錯誤，請再輸入一次");

		if (seatCapacity <= 0 || wheelchairCapacity <= 0)
			throw new IllegalArgumentException("座位與輪椅數量都必須大於0");

		RehaBus bus = new RehaBus();
		bus.setBusId(busId);
		bus.setSeatCapacity(seatCapacity);
		bus.setWheelchairCapacity(wheelchairCapacity);

		try {
			if (!busDao.updateBus(bus))
				throw new RuntimeException("更新失敗，請再檢查一次！");

		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤，更改復康巴士失敗！", e);
		}
	}

	// 根據座位與輪椅容量範圍，以及巴士 ID 範圍查詢復康巴士
	public List<RehaBus> findByFilter(Integer minSeats, Integer maxSeats, Integer minWheelchairs,
			Integer maxWheelchairs, Integer BusId) {

		try {
			return busDao.findByFilter(minSeats, maxSeats, minWheelchairs, maxWheelchairs, BusId);
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
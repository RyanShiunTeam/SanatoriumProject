package activity.service;

import java.util.List;

import activity.bean.Activity;
import activity.dao.ActivityDao;

public class ActivityService {
	private ActivityDao activityDao;
	
	// 新增活動
	public int addActivity(Activity act) {
		if (activityDao == null) {
			activityDao = new ActivityDao();
		}
		return activityDao.insert(act);
	}
	
	// 查詢單一活動
	public Activity getActivityById(int id) {
		if (activityDao == null) {
			activityDao = new ActivityDao();
		}
		return activityDao.findById(id);
	}
	
	// 查詢所有活動
	public List<Activity> getAllActivities() {
		if (activityDao == null) {
			activityDao = new ActivityDao();
		}
		return activityDao.findAll();
	}
	
	// 更新活動
	public boolean updateActivity(Activity a) {
		if (activityDao == null) {
			activityDao = new ActivityDao();
		}
		return activityDao.update(a);
	}
	
	// 刪除活動
	public boolean deleteActivity(int id) {
		if (activityDao == null) {
			activityDao = new ActivityDao();
		}
		return activityDao.delete(id);
	}
	
}

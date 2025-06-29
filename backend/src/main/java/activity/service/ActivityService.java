package activity.service;

import java.util.List;

import activity.bean.Activity;
import activity.dao.ActivityDao;

public class ActivityService {
	private ActivityDao activityDao = new ActivityDao();
	
	// 新增活動
	public Boolean addActivity(Activity act) {
		return activityDao.insert(act);
	}
	
	// 查詢單一活動
	public List<Activity> getActivityByName(String activityName) {
		return activityDao.findActivityByName(activityName);
	}
	
	// 查詢所有活動
	public List<Activity> getAllActivities() {
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
		return activityDao.delete(id);
	}
	
}

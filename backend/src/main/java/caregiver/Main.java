package caregiver;


import caregiver.bean.Caregiver;
import caregiver.service.CaregiverService;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        CaregiverService service = new CaregiverService();

        // ➤ 1. 新增照服員
        Caregiver newCaregiver = new Caregiver(0, "陳美華", true, "0912344678", "mei@example.com", 5, "mei.jpg");
        boolean addSuccess = service.addCaregiver(newCaregiver);
        System.out.println("新增成功: " + addSuccess);

        // ➤ 2. 查詢全部照服員
        List<Caregiver> caregivers = service.getAllCaregivers();
        System.out.println("目前照服員清單：");
        for (Caregiver c : caregivers) {
            System.out.println(c);
        }
        // ➤ 2.1 模糊查詢
        List<Caregiver> caregiver = service.getCaregiversByName("陳");
        System.out.println("模糊查詢名單:");
        for(Caregiver c : caregiver) {
        	System.out.println(c);
        }
        // ➤ 3. 查詢指定 ID (請根據資料庫現有 ID 設定)
        int testId = 1;
        Caregiver queried = service.getCaregiverById(testId);
        System.out.println("查詢 ID = " + testId + " 的結果：");
        System.out.println(queried);

        // ➤ 4. 修改資料（假設 ID = 1 存在）
        if (queried != null) {
            queried.setPhone("0999888777");
            queried.setExperienceYears(10);
            boolean updateSuccess = service.updateCaregiver(queried);
            System.out.println("更新成功: " + updateSuccess);
        }

        // ➤ 5. 刪除照服員（可自行決定是否刪除）
        int deleteId = 3;
        boolean deleteSuccess = service.deleteCaregiverById(deleteId);
        System.out.println("刪除 ID = " + deleteId + " 成功: " + deleteSuccess);
    }
}

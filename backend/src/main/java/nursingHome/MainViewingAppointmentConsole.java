package nursingHome;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

import nursingHome.dao.model.RoomType;
import nursingHome.dao.model.ViewingAppointment;
import nursingHome.service.RoomTypeService;
import nursingHome.service.ViewingAppointmentService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import nursingHome.dao.model.RoomType;
import nursingHome.service.RoomTypeService;

public class MainViewingAppointmentConsole {
    public static void main(String[] args) {
        try (Connection conn = JDBCutil.getConnection(); Scanner scanner = new Scanner(System.in)) {
            if (conn == null) {
                System.out.println("\u274c 資料庫連線失敗，請檢查設定！");
                return;
            }

            ViewingAppointmentService service = new ViewingAppointmentService(conn);

            while (true) {
                System.out.println("\n=== 預約看房管理系統 ===");
                System.out.println("1. 新增預約");
                System.out.println("2. 查詢全部預約");
                System.out.println("3. 查詢指定ID預約");
                System.out.println("4. 匯入預約 CSV");
                System.out.println("0. 離開");
                System.out.print("請輸入選項：");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        ViewingAppointment appt = new ViewingAppointment();
                        System.out.print("會員ID：");
                        appt.setMemberId(scanner.nextInt());
                        System.out.print("房型ID：");
                        appt.setRoomId(scanner.nextInt());
                        scanner.nextLine();
                        System.out.print("預約時間（格式: yyyy-MM-dd HH:mm:ss）：");
                        String timeStr = scanner.nextLine();
                        appt.setAppointmentDate(Timestamp.valueOf(timeStr));
                        System.out.print("備註：");
                        appt.setNotes(scanner.nextLine());
                        service.addAppointment(appt);
                        System.out.println("預約新增成功！");
                        break;

                    case 2:
                        List<ViewingAppointment> allAppts = service.getAllAppointments();
                        for (ViewingAppointment a : allAppts) {
                            printAppointment(a);
                        }
                        break;

                    case 3:
                        System.out.print("輸入預約ID：");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        ViewingAppointment found = service.getAppointmentById(id);
                        if (found != null) {
                            printAppointment(found);
                        } else {
                            System.out.println("查無此預約");
                        }
                        break;

                    case 4:
                        System.out.print("請輸入 CSV 檔案路徑（預設 src/main/resources/data/appointments.csv）：");
                        String path = scanner.nextLine();
                        if (path.trim().isEmpty()) {
                            path = "src/main/resources/data/appointments.csv";
                        }
                        service.importAppointmentsFromCSV(path);
                        break;

                    case 0:
                        System.out.println("程式結束");
                        return;

                    default:
                        System.out.println("無效選項");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printAppointment(ViewingAppointment a) {
        System.out.println("===========");
        System.out.println("ID: " + a.getId());
        System.out.println("會員ID: " + a.getMemberId());
        System.out.println("房型ID: " + a.getRoomId());
        System.out.println("預約時間: " + a.getAppointmentDate());
        System.out.println("備註: " + a.getNotes());
    }
}

package nursingHome;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import nursingHome.dao.model.RoomType;
import nursingHome.service.RoomTypeService;



import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import nursingHome.dao.model.RoomType;
import nursingHome.service.RoomTypeService;

public class MainRoomTypeConsole {
    public static void main(String[] args) {
        try (Connection conn = JDBCutil.getConnection(); Scanner scanner = new Scanner(System.in)) {
            if (conn == null) {
                System.out.println("資料庫連線失敗，請檢查設定！");
                return;
            }

            RoomTypeService service = new RoomTypeService(conn);

            while (true) {
                System.out.println("\n=== 養老院房型管理系統 ===");
                System.out.println("1. 新增房型");
                System.out.println("2. 查詢全部房型");
                System.out.println("3. 查詢指定ID房型");
                System.out.println("4. 查詢價格區間房型");
                System.out.println("5. 模糊查詢描述");
                System.out.println("6. 匯入房型 CSV");
                System.out.println("0. 離開");
                System.out.print("請輸入選項：");

                int choice = scanner.nextInt();
                scanner.nextLine(); // 清掉換行

                switch (choice) {
                    case 1:
                        RoomType room = new RoomType();
                        System.out.print("房型名稱：");
                        room.setName(scanner.nextLine());
                        System.out.print("費用（月）：");
                        room.setPrice(scanner.nextInt());
                        System.out.print("坪數：");
                        room.setArea(scanner.nextDouble());
                        scanner.nextLine();
                        System.out.print("描述：");
                        room.setDescription(scanner.nextLine());
                        System.out.print("特殊設施：");
                        room.setSpecialFeatures(scanner.nextLine());
                        System.out.print("圖片網址：");
                        room.setImageUrl(scanner.nextLine());
                        service.addRoomType(room);
                        System.out.println("房型新增成功！");
                        break;

                    case 2:
                        List<RoomType> allRooms = service.getAllRoomTypes();
                        for (RoomType r : allRooms) {
                            printRoom(r);
                        }
                        break;

                    case 3:
                        System.out.print("輸入房型 ID：");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        RoomType found = service.getRoomTypeById(id);
                        if (found != null) {
                            printRoom(found);
                        } else {
                            System.out.println("查無此房型");
                        }
                        break;

                    case 4:
                        System.out.print("最低價格：");
                        int min = scanner.nextInt();
                        System.out.print("最高價格：");
                        int max = scanner.nextInt();
                        scanner.nextLine();
                        List<RoomType> byPrice = service.getRoomTypesByPriceRange(min, max);
                        for (RoomType r : byPrice) {
                            printRoom(r);
                        }
                        break;

                    case 5:
                        System.out.print("輸入描述關鍵字：");
                        String keyword = scanner.nextLine();
                        List<RoomType> byDesc = service.getRoomTypesByDescriptionKeyword(keyword);
                        for (RoomType r : byDesc) {
                            printRoom(r);
                        }
                        break;

                    case 6:
                        System.out.print("請輸入 CSV 檔案路徑（預設 src/main/resources/data/room_types.csv）：");
                        String path = scanner.nextLine();
                        if (path.trim().isEmpty()) {
                            path = "src/main/resources/data/room_types.csv";
                        }
                        service.importRoomTypesFromCSV(path);
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

    private static void printRoom(RoomType r) {
        System.out.println("===========");
        System.out.println("ID: " + r.getId());
        System.out.println("名稱: " + r.getName());
        System.out.println("費用: " + r.getPrice());
        System.out.println("坪數: " + r.getArea());
        System.out.println("描述: " + r.getDescription());
        System.out.println("設施: " + r.getSpecialFeatures());
        System.out.println("圖片: " + r.getImageUrl());
    }
}
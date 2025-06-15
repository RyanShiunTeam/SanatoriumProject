package member;

public class BackendUserMain {
	public static void main(String[] args) {

		 BackendUserService service = new BackendUserService();
		 
		 BackendUser newUser = new BackendUser(0, "Luna", "055", "test3@mail.com", "Editor", true, null, null);
		 
		 // 增加後臺使用者
//		 service.addBackendUser(newUser);
		 
		 // 停權後臺使用者
// 		 service.deleteBackendUser("Luna");
 		 
 		 // 更新後臺使用者
 		 service.updateBackendUser("Luna", "9499");
		 
		 // 依照名稱查詢後台使用者
//		 BackendUser user = service.getBackendUserByName("Ryan");
		 
		 // 印出查詢結果
//		 if (user != null) {
//			 System.out.println("User found: " + user.userName() + ", Email: " + user.email() + ", Role: " + user.role());
//		 } else {
//			 System.out.println("User not found.");
//		 }
	} 
}

package member;

import java.time.LocalDate;

public record BackendUser(	
	int userId,
	String userName,
	String passWord,
	String email,
	String role,
	boolean isActive,
	LocalDate createdAt,
	LocalDate updatedAt
	) {}

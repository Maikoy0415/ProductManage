package model.entity;

public enum UserRole {
    ADMIN, // 管理者
    USER;  // 一般ユーザー

    // 文字列から UserRole を取得するメソッド（DB とのマッピング用）
    public static UserRole fromString(String role) {
        for (UserRole r : UserRole.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + role);
    }
}

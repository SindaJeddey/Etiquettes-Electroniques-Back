package project.EE.security;

public enum ApplicationRoles {
    ADMIN("ROLE_ADMIN"),
    SUPER_OPERATOR("ROLE_SUPER_OPERATOR"),
    OPERATOR("ROLE_OPERATOR");

    private final String role;

    ApplicationRoles(String role) {
        this.role = role;
    }
}

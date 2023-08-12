package com.vulcanth.commons.player.role;

public enum RoleEnum {

    MASTER("Master", "§6[Master] ", "role.master", 5.0, true, true, 0),
    GERENTE("Gerente", "§4[Gerente] ", "role.gerente", 3.0, true, true, 1),
    ADMIN("Admin", "§c[Admin] ", "role.admin", 3.0, true, true, 2),
    MODERADOR("Moderador", "§2[Moderador] ", "role.mod", 3.0, true, true, 3),
    AJUDANTE("Ajudante", "§e[Ajudante] ", "role.ajudante", 3.0, true, true, 4),
    STREAMER("Streamer", "§5[Streamer] ", "role.streamer", 3.0, true, true, 5),
    YOUTUBER("YouTuber", "§c[YouTuber] ", "role.yt", 3.0, true, true, 6),
    MVPPLUS("MVP+", "§b[MVP§6+§b] ", "role.mvpplus", 3.5, true, true, 7),
    MVP("MVP", "§6[MVP] ", "role.mvp", 3.0, true, true, 8),
    VIP("VIP", "§a[VIP] ", "role.vip", 2.0, true, true, 9),
    MEMBRO("Membro", "§7[Membro] ", null, 1.0, true, true, 10);

    private final int id;
    private final String name;
    private final String prefix;
    private final String permission;
    private final double naturalBooster;
    private final boolean canFly;
    private final boolean alwaysVisible;

    RoleEnum(String name, String prefix, String permission, double naturalBooster, boolean canFly, boolean alwaysVisible, int id) {
        this.name = name;
        this.prefix = prefix;
        this.permission = permission;
        this.naturalBooster = naturalBooster;
        this.canFly = canFly;
        this.alwaysVisible = alwaysVisible;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAlwaysVisible() {
        return this.alwaysVisible;
    }

    public boolean isCanFly() {
        return this.canFly;
    }

    public double getNaturalBooster() {
        return this.naturalBooster;
    }

    public int getId() {
        return this.id;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getPrefix() {
        return this.prefix;
    }
}

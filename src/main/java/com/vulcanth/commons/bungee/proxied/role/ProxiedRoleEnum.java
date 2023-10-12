package com.vulcanth.commons.bungee.proxied.role;

public enum ProxiedRoleEnum {

    MASTER("master", "§6Master", "§6[Master] ", "role.master", "§6", 5.0, true, true, 0),
    GERENTE("gerente", "§4Gerente", "§4[Gerente] ", "role.gerente", "§4", 3.0, true, true, 1),
    ADMIN("admin", "§cAdmin", "§c[Admin] ", "role.admin", "§c", 3.0, true, true, 2),
    MODERADOR("mod", "§2Moderador", "§2[Moderador] ", "role.mod", "§2", 3.0, true, true, 3),
    AJUDANTE("ajudante", "§eAjudante", "§e[Ajudante] ", "role.ajudante", "§e", 3.0, true, true, 4),
    CONSTRUTOR("construtor", "§aConstrutor", "§a[Construtor] ", "role.construtor", "§a", 3.0, true, true, 5),
    STREAMER("streamer", "§9Streamer", "§9[Streamer] ", "role.streamer", "§9", 3.0, true, true, 6),
    YOUTUBER("yt", "§cYouTuber", "§c[YouTuber] ", "role.yt", "§c", 3.0, true, true, 7),
    MVPPLUS("mvpplus", "§bMVP§6+§b", "§b[MVP§6+§b] ", "role.mvpplus", "§b", 3.5, true, false, 8),
    MVP("mvp", "§6MVP", "§6[MVP] ", "role.mvp", "§6", 3.0, true, false, 9),
    VIP("vip", "§aVIP", "§a[VIP] ", "role.vip", "§a", 2.0, true, false, 10),
    MEMBRO("default", "§7Membro", "§7", null, "§7", 1.0, false, false, 11);

    private final String groupName;
    private final int id;
    private final String name;
    private final String prefix;
    private final String permission;
    private final String color;
    private final double naturalBooster;
    private final boolean canFly;
    private final boolean alwaysVisible;

    ProxiedRoleEnum(String groupName, String name, String prefix, String permission, String color, double naturalBooster, boolean canFly, boolean alwaysVisible, int id) {
        this.groupName = groupName;
        this.name = name;
        this.prefix = prefix;
        this.permission = permission;
        this.color = color;
        this.naturalBooster = naturalBooster;
        this.canFly = canFly;
        this.alwaysVisible = alwaysVisible;
        this.id = id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAlwaysVisible() {
        return this.alwaysVisible;
    }

    public boolean canFly() {
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

    public String getColor() {
        return this.color;
    }

    public String getPrefix() {
        return this.prefix;
    }
}

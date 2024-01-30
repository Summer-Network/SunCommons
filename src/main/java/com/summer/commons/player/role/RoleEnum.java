package com.summer.commons.player.role;

public enum RoleEnum {

    ADMIN("admin", "§eAdministrador", "§e[ADM]$f ", "role.admin", "§e", 5.0, true, true, 0, 20),
    GER("gerente", "§4Gerente", "§4[GER]$f ", "role.gerente", "§4", 3.0, true, true, 1, 20),
    COORD("coordenador", "§9Coordenador", "§9[COD]$f ", "role.coordenador", "§9", 3.0, true, true, 1, 20),
    STUDIO("estudio", "§dEstudio", "§d[STD]$f ", "role.studio", "§d", 3.0, true, true, 3, 20),
    MODERADOR("mod", "§aModerador", "§a[MOD]$f ", "role.mod", "§2", 3.0, true, true, 3, 20),
    AJUDANTE("ajudante", "§eAjudante", "§e[AJD]$f ", "role.ajudante", "§e", 3.0, true, true, 4, 20),
    CONSTRUTOR("construtor", "§aConstrutor", "§a[CNT]$f ", "role.construtor", "§a", 3.0, true, true, 5, 20),
    STREAMER("streamer", "§9Streamer", "§9[STR]$f ", "role.streamer", "§9", 3.0, true, true, 6, 20),
    YOUTUBER("yt", "§cYouTuber", "§c[YT]$f ", "role.yt", "§c", 3.0, true, true, 7, 20),
    MVPPLUS("mvpplus", "§bMVP§6+§b", "§b[MVP§6+§b] ", "role.mvpplus", "§b", 3.5, true, false, 8, 20),
    MVP("mvp", "§6MVP", "§6[MVP] ", "role.mvp", "§6", 3.0, true, false, 9, 15),
    VIP("vip", "§aVIP", "§a[VIP] ", "role.vip", "§a", 2.0, true, false, 10, 10),
    MEMBRO("default", "§7Membro", "§7", null, "§7", 1.0, false, false, 11, 5);

    private final String groupName;
    private final int id;
    private final String name;
    private final String prefix;
    private final String permission;
    private final String color;
    private final double naturalBooster;
    private final boolean canFly;
    private final boolean alwaysVisible;
    private final int maxSkinUse;

    RoleEnum(String groupName, String name, String prefix, String permission, String color, double naturalBooster, boolean canFly, boolean alwaysVisible, int id, int maxSkinUse) {
        this.groupName = groupName;
        this.name = name;
        this.prefix = prefix;
        this.permission = permission;
        this.color = color;
        this.naturalBooster = naturalBooster;
        this.canFly = canFly;
        this.alwaysVisible = alwaysVisible;
        this.id = id;
        this.maxSkinUse = maxSkinUse;
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

    public int getMaxSkinUse() {
        return this.maxSkinUse;
    }
}

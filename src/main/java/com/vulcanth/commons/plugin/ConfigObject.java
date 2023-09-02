package com.vulcanth.commons.plugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigObject {

    private final String fileLink;
    private final File file;
    private YamlConfiguration yamlConfig;

    public ConfigObject(String fileLink, File file, VulcanthConfig vulcanthConfig) {
        this.yamlConfig = YamlConfiguration.loadConfiguration(file);
        this.file = file;
        this.fileLink = fileLink;
        this.checkIfHasNewPatch(vulcanthConfig);
        System.out.println("a");
    }

    @Deprecated
    private void checkIfHasNewPatch(VulcanthConfig vulcanthConfig) {
        Set<String> patch = this.yamlConfig.getKeys(false);
        YamlConfiguration tempConfig = YamlConfiguration.loadConfiguration(Objects.requireNonNull(vulcanthConfig.getClass().getResourceAsStream("/" + file.getName())));
        for (String patchAdd : tempConfig.getKeys(false).stream().filter(s -> !patch.contains(s)).collect(Collectors.toList())) {
            Object value = tempConfig.get(patchAdd);
            this.yamlConfig.set(patchAdd, value);
        }

        try {
            this.yamlConfig.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getFile() {
        return this.file;
    }

    public String getFileLink() {
        return this.fileLink;
    }

    public YamlConfiguration getYamlConfig() {
        return this.yamlConfig;
    }

    public String getStringWithColor(String localPatch) {
        return ChatColor.translateAlternateColorCodes('&', this.yamlConfig.getString(localPatch));
    }

    public List<String> getListStringWithColor(String localPatch) {
        List<String> newListString = new ArrayList<>();
        for (String str : this.yamlConfig.getStringList(localPatch)) {
            newListString.add(ChatColor.translateAlternateColorCodes('&', str));
        }

        return newListString;
    }

    protected void reload() {
        this.yamlConfig = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            this.yamlConfig.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

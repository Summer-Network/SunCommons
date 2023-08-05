package com.vulcanth.commons.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VulcanthConfig {

    private final List<ConfigObject> configObjectList;
    private final VulcanthPlugins plugin;

    public VulcanthConfig(VulcanthPlugins plugin) {
        this.configObjectList = new ArrayList<>();
        this.plugin = plugin;
    }

    public void setupConfigs(String... filesLink) {
        for (String fileLink : filesLink) {
            File file = new File("plugins/" + this.plugin.getDescription().getName() + "/" + fileLink);
            if (!file.exists()) {
                File folder = file.getParentFile();
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                try {
                    Files.copy(Objects.requireNonNull(this.plugin.getClass().getResourceAsStream("/" + fileLink.split("/")[fileLink.split("/").length - 1])), file.toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            this.configObjectList.add(new ConfigObject(fileLink, file, this));
        }

        this.plugin.sendMessage("Todos os arquivos de configuração foi carregado com sucesso!", 'e');
    }

    public ConfigObject findYamlByFileLink(String fileLink) {
        return this.configObjectList.stream().filter(configObject -> configObject.getFileLink().equals(fileLink)).findFirst().orElse(null);
    }

    public void reload() {
        this.configObjectList.forEach(ConfigObject::reload);
    }

    public void reload(String fileLink) {
        this.findYamlByFileLink(fileLink).reload();
    }
}

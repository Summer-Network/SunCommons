package com.vulcanth.commons.player.cache.collections;

import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.CacheAbstract;
import com.vulcanth.commons.player.preferences.PreferencesEnum;
import org.bukkit.Bukkit;
import simple.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class PlayerPreferencesCache extends CacheAbstract {

    public PlayerPreferencesCache(Profile profile) {
        super("VulcanthProfiles", "PREFERENCES", "{}", profile);
        if (this.getAsString().equals("{}")) {
            buildDefaultJSON();
        } else {
            checkIfHasNew();
        }
    }

    public boolean getPreference(PreferencesEnum preference) {
        return (Boolean) getAsJSONObject().get(preference.getId());
    }

    public void changePreference(PreferencesEnum preference) {
        JSONObject newJson = this.getAsJSONObject();
        Bukkit.broadcastMessage(String.valueOf(getPreference(preference)));
        if (getPreference(preference)) {
            newJson.clear();
            newJson.put(preference.getId(), false);
            this.setValueCache(newJson.toJSONString());
            return;
        }

        newJson.clear();
        newJson.put(preference.getId(), true);
        this.setValueCache(newJson.toJSONString());
    }

    public String getGlassColor(PreferencesEnum preference) {
        return getPreference(preference) ? "5" : "14";
    }

    public String getDyeColor(PreferencesEnum preference) {
        return getPreference(preference) ? "10" : "8";
    }

    public String getState(PreferencesEnum preference) {
        return getPreference(preference) ? "Ativado" : "Desativado";
    }

    public String getStateWithColor(PreferencesEnum preference) {
        return getPreference(preference) ? "§aAtivado" : "§cDesativado";
    }

    //Aqui ele constroi um JSON que armazena informações básicas do jogador
    private void buildDefaultJSON() {
        this.setValueCache(getDefaultJSON().toJSONString()); //Caso utilize JSON, sempre o salve como JSON String
    }

    private void checkIfHasNew() {
        JSONObject json = getDefaultJSON();
        JSONObject newJson = this.getAsJSONObject();
        List<String> keys = (List<String>) json.keySet().stream().filter(key -> !newJson.containsKey(key)).collect(Collectors.toList());
        keys.forEach(key -> newJson.put(key, true));
        this.setValueCache(newJson.toJSONString());
    }

    private JSONObject getDefaultJSON() {
        JSONObject json = new JSONObject();
        for (PreferencesEnum preference : PreferencesEnum.values()) {
            json.put(preference.getId(), true);
        }

        return json;
    }
}

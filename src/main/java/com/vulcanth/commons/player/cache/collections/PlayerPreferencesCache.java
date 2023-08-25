package com.vulcanth.commons.player.cache.collections;

import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.CacheAbstract;
import com.vulcanth.commons.player.preferences.PreferencesEnum;
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

    public String getInformation(String key) {
        return (String) this.getAsJSONObject().get(key);
    }

    public void updateInformation(String key, String information) {
        JSONObject json = this.getAsJSONObject();
        json.replace(key, information);

        this.setValueCache(json.toJSONString());
    }

    //Aqui ele constroi um JSON que armazena informações básicas do jogador
    private void buildDefaultJSON() {
        this.setValueCache(getDefaultJSON().toJSONString()); //Caso utilize JSON, sempre o salve como JSON String
    }

    private void checkIfHasNew() {
        JSONObject json = getDefaultJSON();
        JSONObject newJson = this.getAsJSONObject();
        List<String> keys = (List<String>) json.keySet().stream().filter(newJson::containsKey).collect(Collectors.toList());
        keys.forEach(key -> newJson.put(key, ""));
        this.setValueCache(newJson.toJSONString());
    }

    private JSONObject getDefaultJSON() {
        JSONObject json = new JSONObject();
        for (PreferencesEnum preference : PreferencesEnum.values()) {
            json.put(String.valueOf(preference.getId()), Boolean.TRUE.toString());
        }

        return json;
    }
}

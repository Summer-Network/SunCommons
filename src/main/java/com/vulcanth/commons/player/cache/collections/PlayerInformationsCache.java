package com.vulcanth.commons.player.cache.collections;

import com.vulcanth.commons.player.cache.CacheAbstract;
import simple.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class PlayerInformationsCache extends CacheAbstract {

    public PlayerInformationsCache() {
        super("", "", "{}");
        if (this.getAsString().equals("{}")) {
            buildDefaultJSON();
        } else {

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
        keys.forEach(key -> {

        });
    }

    private JSONObject getDefaultJSON() {
        JSONObject json = new JSONObject();
        json.put("firstLogin", "");
        json.put("lastLogin", "");
        json.put("email", "");
        json.put("discord", "");
        json.put("role", "");

        return json;
    }
}

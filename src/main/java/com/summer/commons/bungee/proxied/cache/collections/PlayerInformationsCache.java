package com.summer.commons.bungee.proxied.cache.collections;

import com.summer.commons.storage.redisupdater.collections.ProfileInformationsUpdater;
import com.summer.commons.bungee.proxied.ProxiedProfile;
import com.summer.commons.bungee.proxied.cache.CacheAbstract;
import com.summer.commons.bungee.proxied.role.ProxiedRoleEnum;
import simple.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class PlayerInformationsCache extends CacheAbstract {

    public PlayerInformationsCache(ProxiedProfile profile) {
        super("INFORMATIONS", "{}", new ProfileInformationsUpdater("INFORMATIONS"), profile);
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
        List<String> keys = (List<String>) json.keySet().stream().filter(key -> !newJson.containsKey(key)).collect(Collectors.toList());
        keys.forEach(key -> newJson.put(key, ""));
        this.setValueCache(newJson.toJSONString());
    }

    private JSONObject getDefaultJSON() {
        JSONObject json = new JSONObject();
        json.put("firstLogin", "");
        json.put("lastLogin", "");
        json.put("email", "");
        json.put("discord", "");
        json.put("role", String.valueOf(ProxiedRoleEnum.MEMBRO.getId()));
        json.put("cash", "0");

        return json;
    }
}

package com.summer.commons.player.cache.collections;

import com.summer.commons.storage.redisupdater.collections.ProfileInformationsUpdater;
import com.summer.commons.player.Profile;
import com.summer.commons.player.cache.CacheAbstract;
import com.summer.commons.player.role.RoleEnum;
import simple.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class PlayerInformationsCache extends CacheAbstract {

    public PlayerInformationsCache(Profile profile) {
        super("VulcanthProfiles", "INFORMATIONS", "{}", new ProfileInformationsUpdater("INFORMATIONS"), profile);
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

        this.setValueCache(json.toJSONString(), true);
    }

    //Aqui ele constroi um JSON que armazena informações básicas do jogador
    private void buildDefaultJSON() {
        this.setValueCache(getDefaultJSON().toJSONString(), false); //Caso utilize JSON, sempre o salve como JSON String
    }

    private void checkIfHasNew() {
        JSONObject json = getDefaultJSON();
        JSONObject newJson = this.getAsJSONObject();
        List<String> keys = (List<String>) json.keySet().stream().filter(key -> !newJson.containsKey(key)).collect(Collectors.toList());
        keys.forEach(key -> newJson.put(key, ""));
        this.setValueCache(newJson.toJSONString(), false);
    }

    private JSONObject getDefaultJSON() {
        JSONObject json = new JSONObject();
        json.put("firstLogin", "");
        json.put("lastLogin", "");
        json.put("email", "");
        json.put("discord", "");
        json.put("role", String.valueOf(RoleEnum.MEMBRO.getId()));
        json.put("cash", "0");

        return json;
    }
}

package com.summer.commons.player.cache.collections;

import com.summer.commons.player.Profile;
import com.summer.commons.player.cache.CacheAbstract;
import simple.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class RankUPInformationCache extends CacheAbstract {

    public RankUPInformationCache(Profile profile) {
        super("SunRank", "INFORMATIONS", "{}", null, profile);
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

    private void buildDefaultJSON() {
        this.setValueCache(getDefaultJSON().toJSONString(), false);
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
        json.put("yen", "0");
        json.put("rank", "0");
        json.put("hierarchy", "3");

        return json;
    }
}

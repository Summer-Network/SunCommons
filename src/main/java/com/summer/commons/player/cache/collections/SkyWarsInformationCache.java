package com.summer.commons.player.cache.collections;

import com.summer.commons.player.Profile;
import com.summer.commons.player.cache.CacheAbstract;
import simple.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SkyWarsInformationCache extends CacheAbstract {

    public SkyWarsInformationCache(Profile profile) {
        super("VulcanthSkyWars", "STASTISTICS", "{}", null, profile);
        if (this.getAsString().equals("{}")) {
            buildDefaultJSON();
        } else {
            checkIfHasNew();
        }

        checkIfRestatedSeason();
    }


    public Long getStastistic(String key, String stastistic) {
        return (Long) ((JSONObject) this.getAsJSONObject().get(key)).get(stastistic);
    }

    public void addStastisc(String key, String stastistic, Long value) {
        JSONObject newObject = this.getAsJSONObject();
        JSONObject objectStastistic = ((JSONObject) newObject.get(key));
        ((JSONObject) objectStastistic.get(stastistic)).put(key, getStastistic(key, stastistic) + value);
        this.setValueCache(newObject.toJSONString(), false);
    }

    public void removeStastisc(String key, String stastistic, Long value) {
        JSONObject newObject = this.getAsJSONObject();
        JSONObject objectStastistic = ((JSONObject) newObject.get(key));
        ((JSONObject) objectStastistic.get(stastistic)).put(key, getStastistic(key, stastistic) - value);
        this.setValueCache(newObject.toJSONString(), false);
    }

    public void setStastisc(String key, String stastistic, Long value) {
        JSONObject newObject = this.getAsJSONObject();
        JSONObject objectStastistic = ((JSONObject) newObject.get(key));
        ((JSONObject) objectStastistic.get(stastistic)).put(key, value);
        this.setValueCache(newObject.toJSONString(), false);
    }

    private void buildDefaultJSON() {
        this.setValueCache(getDefaultJSON().toJSONString(), false); //Caso utilize JSON, sempre o salve como JSON String
    }

    private void checkIfHasNew() {
        JSONObject json = getDefaultJSON();
        JSONObject newJson = this.getAsJSONObject();
        List<String> keys = (List<String>) json.keySet().stream().filter(key -> !newJson.containsKey(key)).collect(Collectors.toList());
        keys.forEach(key -> newJson.put(key, json.get(key)));

        for (Object keysChecked : json.keySet()) {
            List<String> informationsKeys = (List<String>) ((JSONObject) json.get(keysChecked)).keySet().stream().filter(key -> !((JSONObject) newJson.get(keysChecked)).containsKey(key)).collect(Collectors.toList());
            JSONObject informationsNow = (JSONObject) newJson.get(keysChecked);
            informationsKeys.forEach(s -> informationsNow.put(s, ((JSONObject) json.get(keysChecked)).get(s)));
            newJson.put(keysChecked, informationsNow);
        }

        this.setValueCache(newJson.toJSONString(), false);
    }

    private void checkIfRestatedSeason() {
        if (((double) (((Long) ((JSONObject) this.getAsJSONObject().get("monthly")).get("SeasonStated")) - System.currentTimeMillis()) / 1000) <= 0.1) {
        }
    }

    private JSONObject getDefaultJSON() {
        JSONObject json = new JSONObject();
        JSONObject jsonGeral = new JSONObject();
        JSONObject jsonMonthly = new JSONObject();

        jsonGeral.put("SoloKills", 0L);
        jsonGeral.put("DoubleKills", 0L);
        jsonGeral.put("SoloDeaths", 0L);
        jsonGeral.put("DoubleDeaths", 0L);
        jsonGeral.put("SoloWins", 0L);
        jsonGeral.put("DoubleWins", 0L);
        jsonGeral.put("SoloPlays", 0L);
        jsonGeral.put("DoublePlays", 0L);
        jsonGeral.put("SoloAssists", 0L);
        jsonGeral.put("DoubleAssists", 0L);
        json.put("geral", jsonGeral);

        jsonMonthly.put("SeasonStated", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30));
        jsonMonthly.put("SoloKills", 0L);
        jsonMonthly.put("DoubleKills", 0L);
        jsonMonthly.put("SoloDeaths", 0L);
        jsonMonthly.put("DoubleDeaths", 0L);
        jsonMonthly.put("SoloWins", 0L);
        jsonMonthly.put("DoubleWins", 0L);
        jsonMonthly.put("SoloPlays", 0L);
        jsonMonthly.put("DoublePlays", 0L);
        jsonMonthly.put("SoloAssists", 0L);
        jsonMonthly.put("DoubleAssists", 0L);
        json.put("monthly", jsonMonthly);

        return json;
    }
}

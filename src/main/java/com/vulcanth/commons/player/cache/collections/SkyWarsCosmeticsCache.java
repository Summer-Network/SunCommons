package com.vulcanth.commons.player.cache.collections;

import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.CacheAbstract;
import simple.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class SkyWarsCosmeticsCache extends CacheAbstract {

    public SkyWarsCosmeticsCache(Profile profile) {
        super("VulcanthSkyWars", "COSMETICS", "{}", null, profile);
        if (this.getAsString().equals("{}")) {
            buildDefaultJSON();
        } else {
            checkIfHasNew();
        }
    }

    public String getCosmeticIDSelected(String cosmeticTypeID) {
        return ((JSONObject) this.getAsJSONObject().get("selected")).get(cosmeticTypeID).toString();
    }

    public void setCosmeticIDSelected(String cosmeticTypeID, String cosmeticID) {
        JSONObject object = this.getAsJSONObject();
        JSONObject newSelectedObject = (JSONObject) object.get("selected");
        newSelectedObject.replace(cosmeticTypeID, cosmeticID);
        object.replace(object.get("selected"), newSelectedObject);
        this.setValueCache(object, false);
    }

    public void registerCosmetics(JSONObject cosmeticsObject, String cosmeticTypeID) {
        JSONObject object = this.getAsJSONObject();
        JSONObject newHasObject = (JSONObject) object.get("has");
        JSONObject hasObjectList = (JSONObject) newHasObject.get(cosmeticTypeID);
        List<Object> keys = (List<Object>) cosmeticsObject.keySet().stream().filter(o -> !hasObjectList.containsKey(o)).collect(Collectors.toList());
        for (Object key : keys) {
            hasObjectList.put(key, false);
        }

        object.replace(object.get("has"), newHasObject);
        this.setValueCache(object, false);
    }

    public boolean hasCosmetic(String cosmeticTypeID, String cosmeticID) {
        JSONObject object = this.getAsJSONObject();
        JSONObject newHasObject = (JSONObject) object.get("has");
        JSONObject hasObjectList = (JSONObject) newHasObject.get(cosmeticTypeID);
        return (boolean) hasObjectList.get(cosmeticID);
    }

    public void addCosmetic(String cosmeticTypeID, String cosmeticID) {
        JSONObject object = this.getAsJSONObject();
        JSONObject newHasObject = (JSONObject) object.get("has");
        JSONObject hasObjectList = (JSONObject) newHasObject.get(cosmeticTypeID);
        hasObjectList.replace(cosmeticID, true);
        object.replace(object.get("has"), newHasObject);
        this.setValueCache(object, false);
    }

    public void removeCosmetic(String cosmeticTypeID, String cosmeticID) {
        JSONObject object = this.getAsJSONObject();
        JSONObject newHasObject = (JSONObject) object.get("has");
        JSONObject hasObjectList = (JSONObject) newHasObject.get(cosmeticTypeID);
        hasObjectList.replace(cosmeticID, false);
        object.replace(object.get("has"), newHasObject);
        this.setValueCache(object, false);
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

    private JSONObject getDefaultJSON() {
        JSONObject json = new JSONObject();
        JSONObject selected = new JSONObject();
        JSONObject has = new JSONObject();

        selected.put("0", "0");
        selected.put("1", "0");
        selected.put("2", "0");
        selected.put("3", "0");
        selected.put("4", "0");
        selected.put("5", "0");
        selected.put("6", "0");
        selected.put("7", "0");
        selected.put("8", "0");
        selected.put("9", "0");
        selected.put("10", "0");
        selected.put("11", "0");
        json.put("selected", selected);

        has.put("0", new JSONObject());
        has.put("1", new JSONObject());
        has.put("2", new JSONObject());
        has.put("3", new JSONObject());
        has.put("4", new JSONObject());
        has.put("5", new JSONObject());
        has.put("6", new JSONObject());
        has.put("7", new JSONObject());
        has.put("8", new JSONObject());
        has.put("9", new JSONObject());
        has.put("10", new JSONObject());
        has.put("11", new JSONObject());
        json.put("has", has);

        return json;
    }

}

package com.vulcanth.commons.player.cache.collections;

import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.CacheAbstract;
import com.vulcanth.commons.storage.redisupdater.collections.ProfileSkinUpdater;
import simple.JSONArray;
import simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PlayerSkinCache extends CacheAbstract {

    public PlayerSkinCache(Profile profile) {
        super("VulcanthSkins", "SKININFO", "{}", new ProfileSkinUpdater("SKININFO"), profile);
        if (this.getAsString().equals("{}")) {
            buildDefaultJSON();
        } else {
            checkIfHasNew();
        }
    }

    public String getSkinSelected() {
        return (String) this.getAsJSONObject().get("skin_selected");
    }

    public String getSkinSelectedName() {
        String info = (String) this.getAsJSONObject().get("skin_selected");
        if (info.equals("")) {
            return "";
        }

        return info.split(" ; ")[0];
    }

    public List<String> listSkinsUsed() {
        JSONArray array = (JSONArray) this.getAsJSONObject().get("skins_used");
        List<String> skinsInfo = new ArrayList<>();
        for (Object obj : array) {
            JSONObject skinInfoObj = (JSONObject) obj;
            String skinName = (String) skinInfoObj.keySet().stream().findFirst().get();
            JSONObject infos = (JSONObject) skinInfoObj.get(skinName);
            skinsInfo.add(skinName + " ; " + infos.get("lastUse"));
        }

        return skinsInfo;
    }

    public void updateSkinSelected(String skin, String texture) {
        JSONObject object = this.getAsJSONObject();
        if (skin.equals("") || texture.equals("")) {
            object.replace("skin_selected", "");
        } else {
            object.replace("skin_selected", skin + " ; " + texture);
        }

        this.setValueCache(object.toJSONString(), true);
    }

    public void putSkinUse(String skinUse) {
        JSONArray array = (JSONArray) this.getAsJSONObject().get("skins_used");
        if (findSkinInfo(skinUse) != null) {
            updateInfoSkin(skinUse, "lastUse", String.valueOf(System.currentTimeMillis()));
            return;
        }

        JSONObject object = new JSONObject();
        JSONObject objectInfo = new JSONObject();
        objectInfo.put("lastUse", String.valueOf(System.currentTimeMillis()));
        object.put(skinUse, objectInfo);
        array.add(object);
        JSONObject jsonObject = this.getAsJSONObject();
        jsonObject.replace("skins_used", array);
        this.setValueCache(jsonObject.toJSONString(), false);
    }

    public JSONObject findSkinInfo(String key) {
        JSONArray array = (JSONArray) this.getAsJSONObject().get("skins_used");
        AtomicReference<JSONObject> object = new AtomicReference<>(null);
        array.stream().filter(o -> ((JSONObject) o).containsKey(key)).findFirst().ifPresent(o -> object.set((JSONObject) o));

        return object.get();
    }

    public void updateInfoSkin(String key, String keyValue, String value) {
        JSONArray array = (JSONArray) this.getAsJSONObject().get("skins_used");
        JSONObject object = findSkinInfo(key);
        JSONObject skinInfo = (JSONObject) object.get(key);
        skinInfo.replace(keyValue, value);
        array.remove(findSkinInfo(key));
        array.add(object);

        JSONObject jsonObject = this.getAsJSONObject();
        jsonObject.replace("skins_used", array);
        this.setValueCache(jsonObject.toJSONString(), false);
    }

    private void buildDefaultJSON() {
        this.setValueCache(getDefaultJSON().toJSONString(), false); //Caso utilize JSON, sempre o salve como JSON String
    }

    private void checkIfHasNew() {
        JSONObject json = getDefaultJSON();
        JSONObject newJson = this.getAsJSONObject();
        List<String> keys = (List<String>) json.keySet().stream().filter(key -> !newJson.containsKey(key)).collect(Collectors.toList());
        keys.forEach(key -> {
            if (key.equals("skin_selected")) {
                newJson.put(key, "");
            } else {
                newJson.put(key, new JSONArray());
            }
        });
        this.setValueCache(newJson.toJSONString(), false);
    }

    private JSONObject getDefaultJSON() {
        JSONObject json = new JSONObject();
        json.put("skin_selected", "");
        json.put("skins_used", new JSONArray());
        return json;
    }
}

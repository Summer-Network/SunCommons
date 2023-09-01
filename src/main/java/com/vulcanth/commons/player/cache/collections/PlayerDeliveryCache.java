package com.vulcanth.commons.player.cache.collections;

import com.vulcanth.commons.player.Profile;
import com.vulcanth.commons.player.cache.CacheAbstract;
import simple.JSONObject;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class PlayerDeliveryCache extends CacheAbstract {

    public PlayerDeliveryCache(Profile profile) {
        super("VulcanthProfiles", "REWARDS", "{}", profile);
        if (this.getAsString().equals("{}")) {
            buildDefaultJSON();
        }
    }

    public void setDeliveryClaim(String deliveryClaim, Long systemClaimStart, int totalCooldown) {
        JSONObject deliveryObject = this.getAsJSONObject();
        deliveryObject.put(deliveryClaim, systemClaimStart + TimeUnit.SECONDS.toMillis(86400L * totalCooldown));
        this.setValueCache(deliveryObject.toJSONString());
    }

    public boolean hasColletedDelivery(String deliveryID) {
        return this.getAsJSONObject().containsKey(deliveryID);
    }

    public double getInSecounds(String deliveryID) {
        return Double.parseDouble(String.valueOf(((Long) this.getAsJSONObject().get(deliveryID) - System.currentTimeMillis()) / 1000));
    }

    //Aqui ele constroi um JSON que armazena informações básicas do jogador
    private void buildDefaultJSON() {
        this.setValueCache(new JSONObject().toJSONString()); //Caso utilize JSON, sempre o salve como JSON String
    }

    public JSONObject getObject() {
        return this.getAsJSONObject();
    }

}

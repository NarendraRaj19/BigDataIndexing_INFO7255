package com.bigdataindexing.project.service;

import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;


public class HelperMethods {

    private JedisPool jedisPool;

    private JedisPool getJedisPool() {
        if (this.jedisPool == null) {
            this.jedisPool = new JedisPool();
        }
        return this.jedisPool;
    }

    public String savePlan(JSONObject jsonObject) {

        String objectKey = (String) jsonObject.get("objectId");
        Jedis jedis = this.getJedisPool().getResource();
        jedis.set(objectKey, jsonObject.toString());
        jedis.close();

        return objectKey;

    }


    public JSONObject fetchPlan(String key) {

        Jedis jedis = this.getJedisPool().getResource();
        String jsonString = jedis.get(key);
        jedis.close();

        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        JSONObject jsonObject = new JSONObject(jsonString);

        return  jsonObject;
    }

    public void removePlan(String key) {

        Jedis jedis = this.getJedisPool().getResource();
        jedis.del(key);
        jedis.close();

    }

    public boolean checkIfKeyExists(String key) {

        Jedis jedis = this.getJedisPool().getResource();
        String jsonString = jedis.get(key);
        jedis.close();
        if (jsonString == null || jsonString.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }


    //ETag Methods
    public String getETag(JSONObject json) {

        String encoded=null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(json.toString().getBytes(StandardCharsets.UTF_8));
            encoded = Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "\""+encoded+"\"";
    }

    public boolean verifyETag(JSONObject json, List<String> etags) {
        if(etags.isEmpty())
            return false;
        String encoded=getETag(json);
        return etags.contains(encoded);

    }

}

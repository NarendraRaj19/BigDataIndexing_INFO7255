package com.bigdataindexing.project.service;

import com.bigdataindexing.project.controller.ApiController;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonValidator {
    public void validateJSON(JSONObject jsonObject) throws ValidationException {

        JSONObject jsonSchema = new JSONObject(
                new JSONTokener(ApiController.class.getResourceAsStream("/planSchemaTemplate.json")));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonObject);

    }
}

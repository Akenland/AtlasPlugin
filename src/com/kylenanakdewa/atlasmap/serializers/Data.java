package com.kylenanakdewa.atlasmap.serializers;

import com.google.gson.Gson;

/**
 * Holds data, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public abstract class Data {

    /** The type of data. */
    protected String type;

    public String toJson() {
        return new Gson().toJson(this);
    }

}
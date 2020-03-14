package com.kylenanakdewa.atlasmap.serializers;

import com.google.gson.Gson;

/**
 * Holds data for events, for JSON serialization.
 *
 * @author Kyle Nanakdewa
 */
public abstract class EventData {

    /** The type of event. */
    protected String type;
    /** A user-friendly message for this event. */
    protected String event_message;

    public String toJson() {
        return new Gson().toJson(this);
    }

}
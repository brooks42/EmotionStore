package com.archrival.emotionstore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The EmotionStore object stores a map of "emotions", unique integers for now,
 * tied to a float value where (0<=x<=1) that represents the emotion's
 * "strength".
 *
 * Note that in this store there's not yet a concept of "tied" or "mutual"
 * emotions, since the term "emotion" is only a flavor word at the moment and
 * this is basically a simple data structure for storing a map of integers and
 * floats.
 *
 * @author Chris
 */
public class EmotionStore {

    // the default value for this emotion store. Newly added emotions will have
    // this default value unless it is set in the call. 
    private float defaultValue = 0.5f;

    private HashMap<Integer, Float> store;

    /**
     * Creates a new, empty EmotionStore.
     */
    public EmotionStore() {
        store = new HashMap();
    }

    /**
     * Creates a new, empty EmotionStore with the passed default value.
     *
     * @param defaultValue
     */
    public EmotionStore(float defaultValue) {
        this();
        this.defaultValue = defaultValue;
    }

    /**
     * Creates a new, empty EmotionStore with the passed default value and the
     * passed set of emotions built in. This is equivalent to calling
     * <code>EmotionStore(value)</code> to create your store with a default
     * value, then adding the emotions one at a time.
     *
     * @param emotions
     * @param defaultValue
     */
    public EmotionStore(ArrayList<Integer> emotions, float defaultValue) {
        this(defaultValue);
        for (Integer emotion : emotions) {
            this.registerEmotion(emotion, defaultValue);
        }
    }

    /**
     * Registers an emotion to this EmotionStore, storing it with the current
     * default value.
     *
     * @param emotion
     */
    public void registerEmotion(int emotion) {
        store.put(emotion, defaultValue);
    }

    /**
     * Stores the passed emotion into this EmotionStore with the passed value.
     *
     * @param emotion
     * @param value
     */
    public void registerEmotion(int emotion, float value) {
        store.put(emotion, value);
    }

    /**
     * Sets the default value for newly-added emotions to the passed value.
     *
     * @discussion this doesn't set any of the previously added emotions to this
     * value, of course.
     *
     * @param value
     */
    public void setDefaultValue(float value) {
        this.defaultValue = value;
    }
}

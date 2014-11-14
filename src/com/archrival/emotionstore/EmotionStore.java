package com.archrival.emotionstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * The EmotionStore object stores a map of "emotions", unique integers for now,
 * tied to a float value that represents the emotion's "strength".
 *
 * Note that in this store there's not yet a concept of "tied" or "mutual"
 * emotions, since the term "emotion" is only a flavor word at the moment and
 * this is basically a simple data structure for storing a map of integers and
 * floats.
 *
 * Developer's note: this was developed with the purpose that the values of the
 * emotions be 0<=x<=1. This hasn't been implemented yet, so you'll have to do
 * this part yourself if you want it. There may be a toggle for a min/max value
 * in the future if there's a demand.
 *
 * @author Chris
 */
public class EmotionStore {

    // the default value for this emotion store. Newly added emotions will have
    // this default value unless it is set in the call. 
    private float defaultValue = 0.5f;

    private HashMap<Integer, Emotion> store;

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
     * @discussion If an emotion currently exists with this ID, this will
     * destroy that one, including any pairings it contains. Old pairings to the
     * newly registered ones will still exist.
     *
     * @param emotion
     */
    public void registerEmotion(int emotion) {
        store.put(emotion, new Emotion(emotion, defaultValue));
    }

    /**
     * Stores the passed emotion into this EmotionStore with the passed value.
     *
     * @discussion If an emotion currently exists with this ID, this will
     * destroy that one. Old pairings to the newly registered ones will still
     * exist.
     *
     * @param emotion
     * @param value
     */
    public void registerEmotion(int emotion, float value) {
        store.put(emotion, new Emotion(emotion, value));
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

    /**
     * Returns the value of the passed emotion as stored in this EmotionStore.
     *
     * @param emotion the emotion to get from this Store
     * @return the value of the emotion if it exists, or 0 if it doesn't (note
     * that it can also return 0 if the emotion exists and its value is 0).
     */
    public float valueForEmotion(int emotion) {
        if (store.containsKey(emotion)) {
            return store.get(emotion).value;
        }
        return 0;
    }

    /**
     * Sets the value of the passed emotion to the passed value.
     *
     * This method will simply set the value, but will not update the paired
     * emotions if any. To update pairs, use the <code>updateValue</code>
     * method.
     *
     * @param emotion the emotion to update. This emotion must exist.
     * @param value the value to set
     */
    public void setValue(int emotion, float value) {
        if (!store.containsKey(emotion)) {
            throw new IllegalArgumentException("The emotion must exist to set its value. Use registerEmotion() to register one, first.");
        }
        store.get(emotion).value = value;
    }

    /**
     * Adds the passed value to this emotion's current value. This method will
     * also update each of the emotion's pairs - for more information, see
     * <code>pair</code>.
     *
     * @param emotion the emotion to add the value to
     * @param value the value to add to this emotion. Negative values will, of
     * course, result in a decreased value.
     */
    public void updateValue(int emotion, float value) {
        if (!store.containsKey(emotion)) {
            throw new IllegalArgumentException("The emotion must exist to update its value. Use registerEmotion() to register one, first.");
        }
        store.get(emotion).updateValue(value);
    }

    /**
     * Pairs the passed emotion with the other passed emotion, with the passed
     * ratio.
     *
     * For an example: let's say you have 2 emotions, HAPPINESS and HUNGER. You
     * pair HUNGER and HAPPINESS with a -.75 ratio. Now, each increase in HUNGER
     * will decrease HAPPINESS by (amount)*-.75.
     *
     * This pairing is not reciprocal, so in the above example if you would like
     * each increase in HAPPINESS to affect HUNGER, you would have to perform
     * the additional pairing.
     *
     * The two passed emotions must exist in the tree - if not, this method
     * throws an <code>IllegalArgumentException</code>.
     *
     * @discussion note that, right now, updates to paired emotions are only
     * done on a shallow pair basis - that is, if you have three emotions (say
     * HUNGER, HAPPINESS And SATIETY), each paired to the others, updating
     * HAPPINESS will only update HUNGER and SATIETY - the updates themselves
     * will not trigger recursive updates, otherwise there would be an infinite
     * recursion. This will be updated shortly.
     *
     * @param emotion1
     * @param emotion2
     * @param ratio
     */
    public void pair(int emotion1, int emotion2, float ratio) {
        if (!store.containsKey(emotion1) || !store.containsKey(emotion2)) {
            throw new IllegalArgumentException("Cannot pair emotions that don't exist.");
        }

        // here we pair the emotions. This will overwrite any existent relation with this emotion pair.
        store.get(emotion1).appendRelation(store.get(emotion2), ratio);
    }
}

/**
 * The Emotion class acts as the store between the Integer key in this
 * EmotionStore and its float value. The goal is to have the user only interact
 * with the Int/float pair, and to obfuscate away this Emotion class for
 * internal purposes.
 *
 * @author Chris
 */
class Emotion {

    // the id for this emotion
    public int id;

    // the value for this emotion
    public float value;

    // a pairing between this emotion and another. This is not a mutual pairing - that is,
    // a pairing here between HAPPINESS and SADNESS at 0.75 would not reciprocate.
    public HashMap<Emotion, Float> pairs;

    /**
     * Creates a new Emotion with the passed ID.
     *
     * @param id the integer ID for this emotion, linking it in the EmotionStore
     * hashmap.
     */
    public Emotion(int id) {
        this.id = id;
        this.pairs = new HashMap<>();
    }

    /**
     * Creates a new Emotion with the passed ID and the passed default value.
     *
     * @param id the integer ID for this emotion, linking it in the EmotionStore
     * hashmap.
     * @param value the value for this emotion
     */
    public Emotion(int id, float value) {
        this(id);
        this.value = value;
    }

    /**
     * Appends the relation between this emotion and the passed one. See the
     * EmotionStore's <code>putRelation</code>.
     *
     * @param pair
     * @param ratio
     */
    public void appendRelation(Emotion pair, float ratio) {
        pairs.put(pair, ratio);
    }

    /**
     * Updates this emotion with the passed value.
     *
     * @param value
     */
    public void updateValue(float value) {
        this.value += value;

        // TODO: for now, this only does a shallow update - we'll need some sort of 
        // algorithm to not do an infinite loop if more than 2 emotions are linked together. 
        // for example, if you have HUNGER, HAPPINESS and SATIETY, HUNGER could affect
        // both HAPPINESS and SATIETY, and SATIETY could affect HAPPINESS. If we did an 
        // updateValue() on each of those pairs, there would be an infinite loop.
        // This can be worked around with a well-designed set of pairs, but that's not worth 
        // the trouble just yet. Something even as simple as recursion with an
        // array of already-updated emotions might work?
        for (Entry<Emotion, Float> pair : pairs.entrySet()) {
            pair.getKey().value += (value * pair.getValue());
        }
    }

    /**
     * Returns whether the passed object is an Emotion object with the same ID
     * as this one.
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Emotion) {
            return (((Emotion) o).hashCode() == hashCode());
        }
        return false;
    }

    /**
     * Overridden HashCode.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return this.id;
    }
}

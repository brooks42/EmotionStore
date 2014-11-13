/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.archrival.emotionstore;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Chris
 */
public class EmotionStoreTest {
    
    EmotionStore store;
    public static final int TEST_HAPPINESS = 1;
    public static final int TEST_HUNGER = 2;
    public static final int TEST_UNREST = 3;
    
    public EmotionStoreTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        store = new EmotionStore();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of registerEmotion method, of class EmotionStore.
     */
    @Test
    public void testRegisterEmotion_int() {
        store.registerEmotion(TEST_HAPPINESS);
    }

    /**
     * Test of registerEmotion method, of class EmotionStore.
     */
    @Test
    public void testRegisterEmotion_int_float() {
    }

    /**
     * Test of setDefaultValue method, of class EmotionStore.
     */
    @Test
    public void testSetDefaultValue() {
    }
    
}

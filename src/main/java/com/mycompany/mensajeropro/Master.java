/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mensajeropro;

import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andres
 */
public class Master {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    
    public static void main(String []args) throws InterruptedException{
        Httper ht = new Httper();
        LogIn l = new LogIn(ht, countDownLatch);
        l.setVisible(true);
        countDownLatch.await();
        l.setVisible(false);
        Window w = new Window(ht);
        w.setVisible(true);

    }
    
}

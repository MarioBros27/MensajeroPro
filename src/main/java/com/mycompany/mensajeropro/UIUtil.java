/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mensajeropro;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author andres
 */
public class UIUtil {
    

    public static void appendS(JTextPane textPane, String s, Color color, boolean isBold) {
        try {
            SimpleAttributeSet keyWord = new SimpleAttributeSet();
            StyleConstants.setForeground(keyWord, color);
            StyleConstants.setBold(keyWord, isBold);
            textPane.getDocument().insertString(textPane.getDocument().getLength(), s + "\n", keyWord);
        } catch (BadLocationException exc) {
            exc.printStackTrace();
        }
    }
}

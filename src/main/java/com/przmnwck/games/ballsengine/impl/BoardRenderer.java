/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.przmnwck.games.ballsengine.impl;

import com.przmnwck.games.ballsengine.Board;
import com.przmnwck.games.ballsengine.interfaces.IRenderer;

/**
 *
 * @author Przemo
 */
public class BoardRenderer implements IRenderer {

    @Override
    public void renderResult(Board b) {
        String bstr="+";
        for (int rows = 0; rows < b.getSize(); rows++) {
            for (int i = 0; i < b.getSize(); i++) {
                bstr += "-+";
            }
            bstr += "\n|";
            for (int i = 0; i < b.getSize(); i++) {
                bstr += b.getFields()[rows][i] + "|";
            }
            bstr+="\n+";
        }
        System.out.println();
        System.out.println(bstr);
        System.out.println();
    }
}

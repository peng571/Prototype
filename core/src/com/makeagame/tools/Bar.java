package com.makeagame.tools;

import com.makeagame.tools.KeyTable.Frame;
import com.makeagame.tools.KeyTable.Key;

public class Bar {
    public float percent = 0; // 0~1
    KeyTable ktProgress;
    
    public enum Direction {
        ROW,
        ROW_RESVERSE,
        COLUMN,
        COLUMN_REVERSE,
    };
    
    public void setBar(Direction dir, int length) {
        String sDstMain = new String();
        String sDstLength = new String();
        String sSrcMain = new String();
        String sSrcLength = new String();
        
        if (dir == Direction.ROW || dir == Direction.ROW_RESVERSE) {
            sDstMain = "x";
            sDstLength = "dstW";
            sSrcMain = "srcX";
            sSrcLength = "srcW";
        } else if (dir == Direction.COLUMN || dir == Direction.COLUMN_REVERSE) {
            sDstMain = "y";
            sDstLength = "dstH";
            sSrcMain = "srcY";
            sSrcLength = "srcH";
        }
        
        if (dir == Direction.ROW || dir == Direction.COLUMN) {
            ktProgress = new KeyTable(new Frame[]{
                    new Frame(0.0, new Key[]{
                            new Key(sSrcLength, new Double(0), KeyTable.INT_LINEAR),
                            new Key(sDstLength, new Double(0), KeyTable.INT_LINEAR)}),
                    new Frame(1.0, new Key[]{
                            new Key(sSrcLength, new Double(length), KeyTable.INT_LINEAR),
                            new Key(sDstLength, new Double(length), KeyTable.INT_LINEAR)}),
            });
            
        }  else if (dir == Direction.ROW_RESVERSE || dir == Direction.COLUMN_REVERSE) {
            ktProgress = new KeyTable(new Frame[]{
                    new Frame(0.0, new Key[]{
                            new Key(sDstMain, new Double(length), KeyTable.INT_LINEAR),
                            new Key(sSrcMain, new Double(length), KeyTable.INT_LINEAR),
                            new Key(sDstLength, new Double(0), KeyTable.INT_LINEAR),
                            new Key(sSrcLength, new Double(0), KeyTable.INT_LINEAR)}),
                    new Frame(1.0, new Key[]{
                            new Key(sDstMain, new Double(0), KeyTable.INT_LINEAR),
                            new Key(sSrcMain, new Double(0), KeyTable.INT_LINEAR),
                            new Key(sDstLength, new Double(length), KeyTable.INT_LINEAR),
                            new Key(sSrcLength, new Double(length), KeyTable.INT_LINEAR)}),
            });
        }
        
        
    }
    
    public void apply(Sprite sprite) {
        sprite.apply(ktProgress.get(percent));
    }
    
}

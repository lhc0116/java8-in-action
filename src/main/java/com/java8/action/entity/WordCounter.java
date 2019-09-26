package com.java8.action.entity;

/**
 * @author li huaichuan
 * @date 19-9-26
 */
public class WordCounter {

    private final int count;
    private final boolean lastSpace;

    public WordCounter(int count, boolean lastSpace) {
        this.count = count;
        this.lastSpace = lastSpace;
    }

   public WordCounter accumulate(Character c) {
        if(Character.isWhitespace(c)) {
            return lastSpace ? this : new WordCounter(count, true);
        } else {
            return lastSpace ? new WordCounter(count + 1, false) : this;
        }
   }

    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(this.count + wordCounter.getCount(), true);
    }

    public int getCount() {
        return this.count;
    }
}

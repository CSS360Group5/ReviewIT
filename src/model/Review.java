package model;

import java.io.File;

public class Review {
    public final int score;
    public final File review;
    
    
    public Review(File review, int score) {
        this.score = score;
        this.review = review;
    }
}

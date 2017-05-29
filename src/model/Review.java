package model;

import java.io.File;
import java.io.Serializable;

public class Review implements Serializable {
    /** SVUID */
    private static final long serialVersionUID = -192022211461846483L;
    
    public final int score;
    public final File review;
    
    
    public Review(File review, int score) {
        this.score = score;
        this.review = review;
    }
}

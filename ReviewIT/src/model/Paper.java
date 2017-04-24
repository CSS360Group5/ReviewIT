package model;

import java.io.File;
import java.util.Objects;

public class Paper {
	private final File myPaperFile;
	
	private Paper(final File thePaper){
		myPaperFile = Objects.requireNonNull(thePaper);
	}
	
	public static Paper createPaper(final File thePaperFile){
		return new Paper(thePaperFile);
	}
	
	public File getPaperFile(){
		return myPaperFile;
	}
}


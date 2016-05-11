package com.quodatum.file;
/*
 * file walker
 * @see https://docs.oracle.com/javase/7/docs/api/java/nio/file/FileVisitResult.html
 */
import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.node.FElem;

public class Walker extends SimpleFileVisitor<Path> {
	ValueBuilder vb = new ValueBuilder();
	void FileWork(ValueBuilder avb ){
		vb=avb;
	};
	
	public Value result(){
		return vb.value();
	}
	
    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file,
                                   BasicFileAttributes attr) {
    	FElem elem = new FElem("file").add("name", file.toString());
    	vb.add(elem);
        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir,
                                          IOException exc) {
    	FElem elem = new FElem("dir").add("name", dir.toString());
    	vb.add(elem);
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException 
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file,
                                       IOException exc) {
    	FElem elem = new FElem("err").add("name", file.toString());
    	vb.add(elem);
        return CONTINUE;
    }
}
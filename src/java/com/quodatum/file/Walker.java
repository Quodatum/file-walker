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
import java.util.ArrayDeque;
import java.util.Deque;

import org.basex.query.value.Value;
import org.basex.query.value.ValueBuilder;
import org.basex.query.value.node.FElem;

public class Walker extends SimpleFileVisitor<Path> {
	final String cns = "http://www.w3.org/ns/xproc-step";
	
	Deque<FElem> stack = new ArrayDeque<FElem>();
	FElem site;
	
	public Walker(Path startingDir) {
		site= new FElem("directory", cns)
		.add("name",startingDir.getFileName().toString())
		.add("xml:base",startingDir.toUri().toString());
	}

	public Value result() {
		ValueBuilder vb = new ValueBuilder();
		vb.add(site);
		return vb.value();
	}

	// Print information about
	// each type of file.
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
		FElem elem = new FElem("file", cns).add("name", file.getFileName()
				.toString());
		site.add(elem);
		return CONTINUE;
	}

	// Print each directory visited.
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
		stack.push(site);
		site = new FElem("directory", cns)
		.add("name",dir.getFileName().toString())
		.add("xml:base",dir.toUri().toString());
		return CONTINUE;
	}

	// Print each directory visited.
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {
		site = stack.pop().add(site);
		return CONTINUE;
	}

	// If there is some error accessing
	// the file, let the user know.
	// If you don't override this method
	// and an error occurs, an IOException
	// is thrown.
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		FElem elem = new FElem("error", cns).add("name", file.toString());
		site.add(elem);
		return CONTINUE;
	}
}
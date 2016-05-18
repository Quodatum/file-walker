package com.quodatum.file;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

import com.quodatum.file.Walker;

import org.basex.query.value.Value;
import org.basex.query.value.map.Map;

public class Runner {
	public static Value filewalk(final String path,final Map options ) throws IOException {
		Path startingDir = Paths.get(path);	
		System.out.print(options);
		EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		Walker walk = new Walker(startingDir);
		Files.walkFileTree(startingDir,opts, Integer.MAX_VALUE, walk);	
		return walk.result();
	}
}

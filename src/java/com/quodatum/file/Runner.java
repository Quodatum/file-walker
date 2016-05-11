package com.quodatum.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.quodatum.file.Walker;
import org.basex.query.value.Value;

public class Runner {
	public static Value filewalk(final String path) throws IOException {
		Path startingDir = Paths.get(path);
		Walker pf = new Walker();
		Files.walkFileTree(startingDir, pf);
		return pf.result();
	}
}

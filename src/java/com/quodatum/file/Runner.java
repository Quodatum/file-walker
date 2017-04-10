package com.quodatum.file;

// 
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

import org.basex.query.QueryException;
import org.basex.query.QueryModule;
import org.basex.query.value.Value;
import org.basex.query.value.map.Map;
import org.basex.util.options.BooleanOption;
import org.basex.util.options.NumberOption;
import org.basex.util.options.Options;
import org.basex.util.options.StringOption;
public class Runner extends QueryModule {
    //@TODO use this
	public static class RunnerOptions extends Options {
		/** Max depth of directories to scan. */
		public static final NumberOption MAXDEPTH = new NumberOption("maxDepth", Integer.MAX_VALUE);
		public static final NumberOption MAXFILES = new NumberOption("maxFiles", Integer.MAX_VALUE);
		/** add attrs eg size. */
		public static final BooleanOption SHOWFILEINFO = new BooleanOption("showFileInfo", false);
		/** Query base-uri. */
		public static final StringOption INCLUDE_FILTER = new StringOption("include-filter");
		public static final StringOption EXCLUDE_FILTER = new StringOption("exclude-filter");
	}

	public Value filewalk(final String path, final Map options) throws IOException, QueryException {
		//FnTrace.trace("BY".getBytes(), "TEST".getBytes(), queryContext);
		Path startingDir = Paths.get(path);
		EnumSet<FileVisitOption> walkopts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		
		int maxDepth=SimpleOptions.mapOption(options,"maxDepth",Integer.MAX_VALUE);
		
		// String s=options.dbl(ii)
		Walker walk = new Walker(options, queryContext);
		Files.walkFileTree(startingDir, walkopts, maxDepth, walk);
		return walk.result();
	}
	
	
}

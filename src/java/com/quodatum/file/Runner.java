package com.quodatum.file;

/*
 * @author andy bunce
 * @copyright Quodatum Ltd
 * @date 2015-2017
 * @licence Apache 2
 */
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

import org.basex.query.QueryException;
import org.basex.query.QueryModule;
import org.basex.query.value.Value;
import org.basex.query.value.map.XQMap;
import org.basex.util.options.BooleanOption;
import org.basex.util.options.NumberOption;
import org.basex.util.options.Options;
import org.basex.util.options.StringOption;

public class Runner extends QueryModule {
    // @TODO use this
public static class RunnerOptions extends Options {
    /** Max depth of directories to scan. */
    public static final NumberOption MAX_DEPTH = new NumberOption("max-depth", Integer.MAX_VALUE);
    public static final NumberOption MAX_FILES = new NumberOption("max-files", Integer.MAX_VALUE);
    /** add attrs eg size. */
    public static final BooleanOption INCLUDE_INFO = new BooleanOption("include-info", false);
    public static final BooleanOption FOLLOW_LINKS = new BooleanOption("follow-links", false);
    /** Query base-uri. */
    public static final StringOption INCLUDE_FILTER = new StringOption("include-filter");
    public static final StringOption EXCLUDE_FILTER = new StringOption("exclude-filter");
    public static final StringOption SKIP_FILTER = new StringOption("skip-filter");
}

public Value filewalk(final String path, final XQMap options) throws IOException, QueryException {
    // FnTrace.trace("BY".getBytes(), "TEST".getBytes(), queryContext);
    Path startingDir = Paths.get(path).normalize();

    boolean followLinks = SimpleOptions.mapOption(options, "follow-links", false);
    EnumSet<FileVisitOption> walkopts = followLinks ? EnumSet.of(FileVisitOption.FOLLOW_LINKS)
            : EnumSet.noneOf(FileVisitOption.class);

    int maxDepth = SimpleOptions.mapOption(options, "max-depth", Integer.MAX_VALUE);
    if (-1 == maxDepth)
        maxDepth = Integer.MAX_VALUE;

    // String s=options.dbl(ii)
        Walker walk = new Walker(options, queryContext);
        Files.walkFileTree(startingDir, walkopts, maxDepth, walk);
        return walk.result();
    }

}

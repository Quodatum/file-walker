package com.quodatum.file;

/*
 * file walker
 * @author andy bunce
 * @copyright Quodatum Ltd
 * @date 2015-2017
 * @licence Apache 2
 * @see https://docs.oracle.com/javase/7/docs/api/java/nio/file/FileVisitResult.html
 */
import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

import org.basex.build.MemBuilder;
import org.basex.build.SingleParser;
import org.basex.core.MainOptions;
import org.basex.io.IOContent;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.fn.FnTrace;
import org.basex.query.value.map.XQMap;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.server.Log.LogType;
import org.basex.util.Atts;
import org.basex.util.Token;

@SuppressWarnings("unused")
public class Walker extends SimpleFileVisitor<Path> {

    final byte[] C_DIR = Token.token("c:directory");
    final byte[] C_FILE = Token.token("c:file");
    final byte[] C_ERR = Token.token("c:error");
    final byte[] NAME = Token.token("name");
    final byte[] BASE = Token.token("xml:base");
    final byte[] LAST_MODIFIED = Token.token("last-modified");
    final byte[] SIZE = Token.token("size");
    final byte[] COUNT = Token.token("count");
   

    SingleParser singleParser = new SingleParser(new IOContent(""), MainOptions.get()) {
        @Override
        protected void parse() throws IOException {
        }
    };
    MemBuilder memBuilder = new MemBuilder("", singleParser);
    private boolean showFileInfo = false;
    private int maxFiles;
    private int filesFound = 0;
    private String includeFilter;
    private String excludeFilter;
    private String skipFilter;
    private QueryContext queryContext;
    private Atts NSP =new Atts();
    public Walker(final XQMap options, QueryContext queryContext)
            throws IOException, QueryException {
        // options.get("showInfo", null);
        this.queryContext = queryContext;
        log("walk create");
        showFileInfo = SimpleOptions.mapOption(options, "include-info", false);
        maxFiles = SimpleOptions.mapOption(options, "max-files", Integer.MAX_VALUE);
        includeFilter = SimpleOptions.mapOption(options, "include-filter", null);
        excludeFilter = SimpleOptions.mapOption(options, "exclude-filter", null);
        skipFilter = SimpleOptions.mapOption(options, "skip-filter", null);
        NSP=NSP.add(Token.token("c"), Token.token("http://www.w3.org/ns/xproc-step"));
        memBuilder.init();
    }

    public ANode result() {
        return new DBNode(memBuilder.data());
    }

    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attr)
            throws IOException {
        String name = Objects.toString(file.getFileName(),"");
        log("visit-file " + name);
        boolean use = true;
        if (includeFilter != null) {
            use = name.matches(includeFilter);
        }
        if (excludeFilter != null) {
            use = use && !name.matches(excludeFilter);
        }
        if (!use)
            return CONTINUE;
        log("pre-atts " + name);
        Atts atts = new Atts();
        atts.add(NAME, Token.token(name));
      
        if (showFileInfo) {
            atts.add(LAST_MODIFIED, Token.token(attr.lastModifiedTime().toString()));
            atts.add(SIZE, Token.token(Long.toString(attr.size())));
        }

        if (attr.isDirectory()) {
            atts.add(BASE, Token.token(file.toUri().toString()));
            memBuilder.emptyElem(C_DIR, atts, NSP);
        } else {
            filesFound++;
            memBuilder.emptyElem(C_FILE, atts, NSP);

        }
        return (filesFound < maxFiles) ? CONTINUE : SKIP_SIBLINGS;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
            throws IOException {
        String name = Objects.toString(dir.getFileName(), "");
        log("pre-folder " + name);
        boolean skip = false;
        if (skipFilter != null) {
            skip = name.matches(skipFilter);
        }
        if (skip)
            return SKIP_SUBTREE;
        if (filesFound < maxFiles) {
            log("pre-atts " + name);
            Atts atts = new Atts();
            atts.add(NAME, Token.token(name));
            atts.add(BASE, Token.token(dir.toUri().toString()));
            log( "post-atts " + name);
            if (showFileInfo) {
                atts.add(LAST_MODIFIED, Token.token(attrs.lastModifiedTime().toString()));
                atts.add(SIZE, Token.token(Long.toString(attrs.size())));
            }
            log( "build " + name);
            memBuilder.openElem(C_DIR, atts, NSP);
            return CONTINUE;
        } else {
            return SKIP_SUBTREE;
        }
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc)
            throws IOException {
        memBuilder.closeElem();
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc)
            throws IOException {
        String name = file.getFileName().toString();
        Atts atts=new Atts();
        atts.add(NAME, Token.token(name));
        memBuilder.emptyElem(C_ERR, atts, NSP);
        return CONTINUE;
    }
    
    private void log(final String msg) {
    	//queryContext.context.log.write(LogType.INFO, msg, null, queryContext.context);
        }
}
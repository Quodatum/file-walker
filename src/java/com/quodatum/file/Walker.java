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

import org.basex.build.MemBuilder;
import org.basex.build.SingleParser;
import org.basex.core.MainOptions;
import org.basex.io.IOContent;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.value.map.Map;
import org.basex.query.value.node.ANode;
import org.basex.query.value.node.DBNode;
import org.basex.util.Atts;
import org.basex.util.Token;

public class Walker extends SimpleFileVisitor<Path> {
    
    final byte[] C_DIR = Token.token("c:directory");
    final byte[] C_FILE = Token.token("c:file");
    final byte[] C_ERR = Token.token("c:error");
    final byte[] NAME = Token.token("name");
    final byte[] BASE = Token.token("xml:base");
    final byte[] LAST_MODIFIED = Token.token("last-modified");
    final byte[] SIZE = Token.token("size");
    final Atts NSP = new Atts(Token.token("c"), Token.token("http://www.w3.org/ns/xproc-step"));

    SingleParser singleParser=new SingleParser(new IOContent(""),MainOptions.get()){@Override protected void parse()throws IOException{}};
    MemBuilder memBuilder = new MemBuilder("", singleParser);
    private boolean showFileInfo=false;
    private int maxFiles;
    private int filesFound = 0;
    
    public Walker(Map options, QueryContext queryContext) throws IOException, QueryException {
        // options.get("showInfo", null);
        showFileInfo=SimpleOptions.mapOption(options,"showFileInfo",false);
        maxFiles=SimpleOptions.mapOption(options,"maxFiles",Integer.MAX_VALUE);
        memBuilder.init();
    }

    public ANode result() {
        return new DBNode(memBuilder.data());
    }

    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
        Atts atts = new Atts(NAME, Token.token(file.getFileName().toString()));
        if (showFileInfo) {
            atts.add(LAST_MODIFIED, Token.token(attr.lastModifiedTime().toString()));
            atts.add(SIZE, Token.token(Long.toString(attr.size())));
        }

        if (attr.isDirectory()) {
            atts.add(BASE, Token.token(file.toUri().toString()));
            memBuilder.emptyElem(C_DIR, atts, NSP);
        }else{
            memBuilder.emptyElem(C_FILE, atts, NSP);
            filesFound++;
        }
        return (filesFound<maxFiles)?CONTINUE:TERMINATE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Atts atts = new Atts();
        atts.add(NAME, Token.token(dir.getFileName().toString()));
        atts.add(BASE, Token.token(dir.toUri().toString()));
        if (showFileInfo) {
            atts.add(LAST_MODIFIED, Token.token(attrs.lastModifiedTime().toString()));
            atts.add(SIZE, Token.token(Long.toString(attrs.size())));
        }
        ;
        memBuilder.openElem(C_DIR, atts, NSP);
        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        memBuilder.closeElem();
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        Atts atts = new Atts(NAME, Token.token(file.getFileName().toString()));
        memBuilder.emptyElem(C_ERR, atts, NSP);
        return CONTINUE;
    }
}
package com.quodatum.file;

import org.basex.query.QueryException;
import org.basex.query.value.Value;
import org.basex.query.value.item.ANum;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Item;
import org.basex.query.value.item.Str;
import org.basex.query.value.map.XQMap;
import org.basex.util.InputInfo;
import org.basex.util.Token;

public class SimpleOptions {

    // get int key from map or default def if missing/invalid
    static int mapOption(final XQMap m, final String skey, final int def)
            throws QueryException {
        Item key = Str.get(skey);
        InputInfo ii = new InputInfo("XQueryXQMaps.java", 0, 0);
        Value v = m.get(key, ii);
        if (v.isEmpty()) {
            return def;
        }
        Item item = v.itemAt(0);
        if (item instanceof ANum) {
            return (int) ((ANum) item).itr(null);
        } else {
            return def;
        }
    }

    // get int key from XQMap or default def if missing/invalid
    static boolean mapOption(final XQMap m, final String skey, final boolean def)
            throws QueryException {
        Item key = Str.get(skey);
        InputInfo ii = new InputInfo("XQueryXQMaps.java", 0, 0);
        Value v = m.get(key, ii);
        if (v.isEmpty()) {
            return def;
        }
        Item item = v.itemAt(0);
        if (item instanceof Bln) {
            return ((Bln) item).bool(null);
        } else {
            return def;
        }
    }

    // get string key from XQMap or default def if missing/invalid
    static String mapOption(final XQMap m, final String skey, final String def)
            throws QueryException {
        Item key = Str.get(skey);
        InputInfo ii = new InputInfo("XQueryXQMaps.java", 0, 0);
        Value v = m.get(key, ii);
        if (v.isEmpty()) {
            return def;
        }
        Item item = v.itemAt(0);
        if (item.type.isStringOrUntyped()) {
            return Token.string(item.string(null));
        } else {
            return def;
        }
    }
}

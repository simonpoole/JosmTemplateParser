package ch.poole.osm.josmtemplateparser;

import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.poole.osm.josmfilterparser.Meta;
import ch.poole.osm.josmfilterparser.Type;

/**
 * Replace {key} with the special values
 * 
 * @author simon
 *
 */
public class Special implements Formatter {
    private static final String EVERYTHING = "special:everything";
    private static final String ID         = "special:id";
    private static final String LOCAL_NAME = "special:localName";

    private static final String NAME_KEY = "name";

    private final String key;

    public Special(@NotNull String specialKey) {
        this.key = specialKey;

    }

    @Override
    @NotNull
    public String format(@NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags) {
        if (meta == null) {
            return "";
        }
        switch (key) {
        case EVERYTHING:
            Map<String, String> t = meta.getTags();
            if (t != null) {
                return buildStringFromTags(t);
            }
            break;
        case ID:
            return Long.toString(meta.getId());
        case LOCAL_NAME:
            t = meta.getTags();
            if (t != null) {
                String name = t.get(NAME_KEY);
                if (name != null) {
                    return name;
                }
            }
        default:
        }
        return "";
    }

    /**
     * Build a string by concatenating the tags
     * 
     * @param tags
     * @return a String
     */
    @NotNull
    private static String buildStringFromTags(@NotNull Map<String, String> tags) {
        StringBuilder builder = new StringBuilder();
        for (Entry<String, String> entry : tags.entrySet()) {
            if (builder.length() != 0) {
                builder.append("\n");
            }
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return "{special:" + key + "}";
    }
}

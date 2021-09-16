package ch.poole.osm.josmtemplateparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.poole.osm.josmfilterparser.Meta;
import ch.poole.osm.josmfilterparser.Type;

/**
 * Replace {key} with the value
 * 
 * @author simon
 *
 */
public class Tag implements Formatter {

    private final String key;
    
    public Tag(@NotNull String key) {
        this.key = key;
    }
    
    @Override
    @NotNull
    public String format(@NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags) {
        if (tags == null) {
            return "";
        }
        String value = tags.get(key);
        return value != null ? value : "";
    }
    
    @Override
    public String toString() {
        return "{" + key + "}";
    }
}

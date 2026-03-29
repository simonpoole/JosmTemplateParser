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

    private static final String USE_DISPLAY_VALUE_PREFIX = "%";
    
    private final String  key;
    private final boolean useDisplayValue;

    public Tag(@NotNull String key, boolean useDisplayValue) {
        this.key = key;
        this.useDisplayValue = useDisplayValue;
    }

    @Override
    @NotNull
    public String format(@NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags) {
        if (tags == null) {
            return "";
        }
        String value = tags.get(key);
        return value != null ? displayValue(meta, key, value) : "";
    }

    /**
     * Return a suitable value for display
     * 
     * @param meta the Meta object or null
     * @param key the key
     * @param value the original value
     * @return a value suitable for display
     */
    private @NotNull String displayValue(@Nullable Meta meta, @NotNull String key, @NotNull String value) {
        return meta != null && useDisplayValue ? meta.displayValue(key, value) : value;
    }

    @Override
    public String toString() {
        return "{" + (useDisplayValue ? USE_DISPLAY_VALUE_PREFIX : "") + key + "}";
    }
}

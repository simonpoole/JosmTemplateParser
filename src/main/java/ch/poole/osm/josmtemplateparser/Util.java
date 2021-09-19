package ch.poole.osm.josmtemplateparser;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.poole.osm.josmfilterparser.Meta;
import ch.poole.osm.josmfilterparser.Type;

public final class Util {

    /**
     * Private constructor
     */
    private Util() {
        // nothing
    }

    /**
     * Execute a list of formatters
     * 
     * @param formatters the formatters
     * @param type object type
     * @param meta object meta
     * @param tags object tags
     * @return a formated string
     */
    static String listFormat(@NotNull List<Formatter> formatters, @NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags) {
        StringBuilder builder = new StringBuilder();
        for (Formatter f : formatters) {
            builder.append(f.format(type, meta, tags));
        }
        return builder.toString();
    }
}

package ch.poole.osm.josmtemplateparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.poole.osm.josmfilterparser.Meta;
import ch.poole.osm.josmfilterparser.Type;

/**
 * Return a literal string
 * 
 * @author simon
 *
 */
public class Literal implements Formatter {

    private final String value;
    
    public Literal(@NotNull String value) {
        this.value = value;
    }
    
    @Override
    @NotNull
    public String format(@NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags) {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}

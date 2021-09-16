package ch.poole.osm.josmtemplateparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.poole.osm.josmfilterparser.Meta;
import ch.poole.osm.josmfilterparser.Type;

public interface Formatter {
   
        /**
         * Format a concrete OSM element
         * 
         * @param type the Type of the OSM element
         * @param meta meta information for the OSM element or null
         * @param tags tags of the OSM element or null
         * @return the formated "name"
         */
        @NotNull
        public String format(@NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags);

}

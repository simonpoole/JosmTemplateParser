package ch.poole.osm.josmtemplateparser;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.poole.osm.josmfilterparser.Condition;
import ch.poole.osm.josmfilterparser.JosmFilterParser;
import ch.poole.osm.josmfilterparser.Meta;
import ch.poole.osm.josmfilterparser.Type;

/**
 * Find object with a search expression then format it
 * 
 * @author simon
 *
 */
public class Search implements Formatter {

    private final Condition       condition;
    private final List<Formatter> formatters;

    public Search(@NotNull String searchExpression, @NotNull String template) throws ParseException {
        try {
            condition = (new JosmFilterParser(new ByteArrayInputStream(searchExpression.trim().getBytes()))).condition();
            formatters = (new JosmTemplateParser(new ByteArrayInputStream(template.trim().getBytes()))).formatters();
        } catch (ch.poole.osm.josmfilterparser.ParseException pex) {
            throw new ParseException("Template " + template + " " + pex.getMessage());
        }
    }

    @Override
    @NotNull
    public String format(@NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags) {
        if (meta != null) {
            List<Object> matches = meta.getMatchingElements(condition);
            if (!matches.isEmpty()) {
                Meta match = meta.wrap(matches.get(0));
                return Util.listFormat(formatters, match.getType(), match, match.getTags());
            }
        }
        return "";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("!{");
        builder.append(condition.toString());
        builder.append(" '");
        boolean first = true;
        for (Formatter f : formatters) {
            if (first) {
                first = false;
            } else {
                builder.append(" ");
            }
            builder.append(f.toString());
        }
        builder.append("'}");
        return builder.toString();
    }
}

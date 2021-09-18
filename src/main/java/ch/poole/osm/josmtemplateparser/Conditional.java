package ch.poole.osm.josmtemplateparser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.poole.osm.josmfilterparser.Condition;
import ch.poole.osm.josmfilterparser.JosmFilterParser;
import ch.poole.osm.josmfilterparser.Meta;
import ch.poole.osm.josmfilterparser.Type;

/**
 * Replace {key} with the value
 * 
 * @author simon
 *
 */
public class Conditional implements Formatter {

    class ConditionalValue {
        final Condition       condition;
        final List<Formatter> values;

        ConditionalValue(@Nullable final Condition condition, @NotNull final List<Formatter> value) {
            this.condition = condition;
            this.values = value;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (condition != null) {
                builder.append(condition);
            }
            for (Formatter f : values) {
                builder.append(f.toString());
            }
            return builder.toString();
        }
    }

    private final List<ConditionalValue> conditions = new ArrayList<>();;

    public Conditional(@NotNull List<String> conditionStrings, @NotNull List<String> values) throws ParseException {

        for (int i = 0; i < conditionStrings.size(); i++) {
            String c = conditionStrings.get(i);
            try {
                ConditionalValue cv = new ConditionalValue(
                        c != null && !"".equals(c.trim())
                                ? (new JosmFilterParser(new ByteArrayInputStream(c.trim().getBytes()))).condition()
                                : null,
                        (new JosmTemplateParser(new ByteArrayInputStream(values.get(i).getBytes()))).formatters());
                conditions.add(cv);

            } catch (ch.poole.osm.josmfilterparser.ParseException pex) {
                throw new ParseException("Search expression " + c + " " + pex.getMessage());
            }
        }
    }

    @Override
    @NotNull
    public String format(@NotNull Type type, @Nullable Meta meta, @Nullable Map<String, String> tags) {
        if (tags == null || conditions.isEmpty()) {
            return "";
        }
        int lastIndex = conditions.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            ConditionalValue cv = conditions.get(0);
            if (cv != null && cv.condition != null) {
                if (cv.condition.eval(type, meta, tags)) {
                    return listFormat(cv.values, type, meta, tags);
                }
            } else {
                StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (Formatter f : cv.values) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(" ");
                    }
                    String t = f.format(type, meta, tags);
                    if ("".equals(t)) {
                        builder.setLength(0);
                        continue;
                    }
                    builder.append(t);
                }
                if (builder.length() > 0) {
                    return builder.toString();
                }
            }
        }
        return listFormat(conditions.get(lastIndex).values, type, meta, tags);
    }

    private String listFormat(List<Formatter> formatters, @NotNull Type type, @Nullable Meta meta,
            @Nullable Map<String, String> tags) {
        StringBuilder builder = new StringBuilder();
        for (Formatter f : formatters) {
            builder.append(f.format(type, meta, tags));
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("?{");
        boolean first = true;
        for (ConditionalValue cv : conditions) {
            if (first) {
                first = false;
            } else {
                builder.append(" | ");
            }
            if (cv.condition != null) {
                builder.append(cv.toString());
                builder.append(" ");
            }
            builder.append("'");
            builder.append(cv.values);
            builder.append("'");
        }
        builder.append("}");
        return builder.toString();
    }
}

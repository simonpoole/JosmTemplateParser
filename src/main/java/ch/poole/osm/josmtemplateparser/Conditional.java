package ch.poole.osm.josmtemplateparser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.poole.osm.josmfilterparser.Condition;
import ch.poole.osm.josmfilterparser.JosmFilterParser;
import ch.poole.osm.josmfilterparser.Meta;
import ch.poole.osm.josmfilterparser.Type;

/**
 * If a condition is true use the corresponding value
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

    private final List<ConditionalValue> conditions = new ArrayList<>();

    public Conditional(@NotNull List<String> conditionStrings, @NotNull List<String> values) throws ParseException {
        for (int i = 0; i < conditionStrings.size(); i++) {
            final String c = conditionStrings.get(i);
            final String v = values.get(i);
            try {
                ConditionalValue cv = new ConditionalValue(
                        c != null && !"".equals(c.trim()) ? (new JosmFilterParser(new ByteArrayInputStream(c.trim().getBytes()))).condition() : null,
                        !"".equals(v.trim()) ? (new JosmTemplateParser(new ByteArrayInputStream(v.getBytes()))).formatters() : Arrays.asList(new Literal("")));
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
            ConditionalValue cv = conditions.get(i);
            if (cv != null) {
                if (cv.condition != null) {
                    if (cv.condition.eval(type, meta, tags)) {
                        return Util.listFormat(cv.values, type, meta, tags);
                    }
                } else {
                    StringBuilder builder = new StringBuilder();
                    for (Formatter f : cv.values) {
                        String t = f.format(type, meta, tags);
                        if ("".equals(t)) {
                            builder.setLength(0);
                            break;
                        }
                        builder.append(t);
                    }
                    if (builder.length() > 0) {
                        return builder.toString();
                    }
                }
            }
        }
        return Util.listFormat(conditions.get(lastIndex).values, type, meta, tags);
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

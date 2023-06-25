
package ch.poole.osm.josmtemplateparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import ch.poole.osm.josmfilterparser.Condition;
import ch.poole.osm.josmfilterparser.Meta;
import ch.poole.osm.josmfilterparser.Type;

/**
 * Tests for the OpeningHoursParser
 * 
 * @author Simon Poole
 *
 */
public class JosmTemplateIntegrationTest {

    /**
     * Test tag replacement
     */
    @Test
    public void literalAndTagTest() {
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr2");

        List<Formatter> f = parse("test1");
        assertEquals(1, f.size());
        assertEquals("test1", f.get(0).format(Type.NODE, null, tags));

        f = parse("{test1}");
        assertEquals(1, f.size());
        assertEquals("grrr", f.get(0).format(Type.NODE, null, tags));

        f = parse("test1{test1}");
        assertEquals(2, f.size());
        assertEquals("test1", f.get(0).format(Type.NODE, null, tags));
        assertEquals("grrr", f.get(1).format(Type.NODE, null, tags));

        f = parse("{test1}test1");
        assertEquals(2, f.size());
        assertEquals("grrr", f.get(0).format(Type.NODE, null, tags));
        assertEquals("test1", f.get(1).format(Type.NODE, null, tags));

        f = parse("test1{test1}test2");
        assertEquals(3, f.size());
        assertEquals("test1", f.get(0).format(Type.NODE, null, tags));
        assertEquals("grrr", f.get(1).format(Type.NODE, null, tags));
        assertEquals("test2", f.get(2).format(Type.NODE, null, tags));

        f = parse("test1{test1}test2{test}");
        assertEquals(4, f.size());
        assertEquals("test1", f.get(0).format(Type.NODE, null, tags));
        assertEquals("grrr", f.get(1).format(Type.NODE, null, tags));
        assertEquals("test2", f.get(2).format(Type.NODE, null, tags));
        assertEquals("grrr2", f.get(3).format(Type.NODE, null, tags));
    }

    @Test
    public void conditionalsTest() {
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr2");

        List<Formatter> f = parse("?{'{test1}' | 'fail'}");
        assertEquals(1, f.size());
        assertEquals("grrr", f.get(0).format(Type.NODE, null, tags));

        f = parse("?{'{test2}' | 'fail'}");
        assertEquals(1, f.size());
        assertEquals("fail", f.get(0).format(Type.NODE, null, tags));
    }

    @Test
    public void searchTest() {

        Meta meta = new TestMeta() {
            @Override
            public Type getType() {
                return Type.NODE;
            }

            @Override
            public Map<String, String> getTags() {
                Map<String, String> tags = new HashMap<>();
                tags.put("test1", "grrr");
                tags.put("test", "grrr2");
                return tags;
            }

            @Override
            public List<Object> getMatchingElements(@NotNull Condition c) {
                return Arrays.asList(this);
            }

            @Override
            public @NotNull Meta wrap(Object o) {
                return this;
            }
        };

        List<Formatter> f = parse("!{test1 '{test1}'}");
        assertEquals(1, f.size());
        assertEquals("grrr", f.get(0).format(Type.NODE, meta, null));
    }

    @Test
    public void specialIdTest() {
        TestMeta meta = new TestMeta();
        meta.id = 123456789L;

        List<Formatter> f = parse("{special:id}");
        assertEquals(1, f.size());
        assertEquals("123456789", f.get(0).format(Type.NODE, meta, null));
    }

    @Test
    public void specialEverythingTest() {
        TestMeta meta = new TestMeta() {
            @Override
            public Map<String, String> getTags() {
                Map<String, String> tags = new HashMap<>();
                tags.put("test1", "grrr");
                tags.put("test", "grrr2");
                return tags;
            }
        };

        List<Formatter> f = parse("{special:everything}");
        assertEquals(1, f.size());
        assertEquals("test=grrr2\ntest1=grrr", f.get(0).format(Type.NODE, meta, null));
    }

    @Test
    public void specialLocalNameTest() {
        TestMeta meta = new TestMeta() {
            @Override
            public Map<String, String> getTags() {
                Map<String, String> tags = new HashMap<>();
                tags.put("test1", "grrr");
                tags.put("name", "grrr2");
                return tags;
            }
        };

        List<Formatter> f = parse("{special:localName}");
        assertEquals(1, f.size());
        assertEquals("grrr2", f.get(0).format(Type.NODE, meta, null));
    }

    /**
     * Parse a filter string and return the Condition object
     * 
     * @param filterString the filter string
     * @return a Condition object
     */
    private List<Formatter> parse(@NotNull String filterString) {

        try {
            JosmTemplateParser parser = new JosmTemplateParser(new ByteArrayInputStream(filterString.getBytes()));
            return parser.formatters();
        } catch (ParseException pex) {
            fail(pex.toString());
        } catch (Error err) {
            fail(err.toString());
        }
        return null;
    }
}


package ch.poole.osm.josmtemplateparser;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ch.poole.osm.josmfilterparser.Type;

/**
 * 
 * @author Simon Poole
 *
 */
public class JosmTemplateParserTest {

    /**
     * Check a set of test data against known good results
     */
    @Test
    public void regressionTest() {
        parseData("test-data/template.txt", "test-data/template.txt-result");
    }

    /**
     * This completes successfully if parsing gives the same success result and for successful parses the same
     * regenerated OH string
     */
    private void parseData(String inputFile, String resultsFile) {
        int successful = 0;
        int errors = 0;
        int lexical = 0;
        BufferedReader inputRules = null;
        BufferedReader inputExpected = null;
        BufferedWriter outputExpected = null;
        String line = null;
        try {

            inputRules = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF8"));
            try {
                inputExpected = new BufferedReader(new InputStreamReader(new FileInputStream(resultsFile), "UTF8"));
            } catch (FileNotFoundException fnfex) {
                System.out.println("File not found " + fnfex.toString());
            }
            outputExpected = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputFile + "-result-temp"), "UTF8"));

            String expectedResultCode = null;
            String expectedResult = null;
            Map<String, String> tags = new HashMap<>();
            ;
            int lineCount = 0;
            while ((line = inputRules.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                String[] b = line.split("\t");
                tags.clear();
                if (b.length == 2) {
                    String[] tempTags = b[1].split("/");
                    for (String tag : tempTags) {
                        String[] kv = tag.split("=");
                        if (kv.length == 2) {
                            tags.put(kv[0], kv[1]);
                        }
                    }
                }
                if (inputExpected != null) {
                    String[] expected = inputExpected.readLine().split("\t");
                    expectedResultCode = expected[0];
                    if (expected.length == 2) {
                        expectedResult = expected[1];
                    } else {
                        expectedResult = null;
                    }
                }

                try {
                    JosmTemplateParser parser = new JosmTemplateParser(new ByteArrayInputStream(b[0].getBytes()));

                    List<Formatter> rs = parser.formatters();

                    successful++;
                    outputExpected.write("0\t" + rs.toString());
                    outputExpected.write("\t");
                    for (Formatter r : rs) {
                        outputExpected.write(r.format(Type.NODE, null, tags));
                    }
                    outputExpected.write("\n");
                    if (expectedResultCode != null) {
                        assertEquals(expectedResultCode, "0");
                        if (expectedResult != null) {
                            assertEquals(rs.toString(), expectedResult);
                        }
                    }
                } catch (ParseException pex) {
                    if (pex.toString().contains("Lexical")) {
                        lexical++;
                    } else {
                        System.err.println("Parser exception on line  " + lineCount + " for " + line + " " + pex.toString());
                    }
                    pex.printStackTrace();
                    errors++;
                    outputExpected.write("1\n");
                    if (expectedResultCode != null) {
                        assertEquals(expectedResultCode, "1");
                    }
                } catch (NumberFormatException nfx) {
                    System.err.println("Parser exception for " + line + " " + nfx.toString());
                    // pex.printStackTrace();
                    lexical++;
                    errors++;
                    outputExpected.write("2\n");
                    if (expectedResultCode != null) {
                        assertEquals(expectedResultCode, "2");
                    }
                } catch (Exception ex) {
                    System.err.println("Parser exception for " + line + " " + ex.toString());
                    ex.printStackTrace();
                    outputExpected.write("4\n");
                } catch (Error err) {
                    if (err.toString().contains("Lexical")) {
                        lexical++;
                    } else {
                        System.err.println("Parser err for " + line + " " + err.toString());
                        // err.printStackTrace();
                    }
                    errors++;
                    outputExpected.write("3\n");
                    if (expectedResultCode != null) {
                        assertEquals(expectedResultCode, "3");
                    }
                }
                lineCount++;
            }
        } catch (FileNotFoundException fnfex) {
            System.err.println("File not found " + fnfex.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AssertionError ae) {
            System.err.println("Assertion failed for " + line);
            throw ae;
        } finally {
            if (inputRules != null) {
                try {
                    inputRules.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (outputExpected != null) {
                try {
                    outputExpected.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Successful " + successful + " errors " + errors + " of which " + lexical + " are lexical errors");
    }
}

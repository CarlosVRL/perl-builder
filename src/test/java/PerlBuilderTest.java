import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;

/**
 * PerlBuilder tests.
 */
public class PerlBuilderTest
{
    @Test
    public void can_instantiateObject()
    {
        PerlBuilder perlBuilder = new PerlBuilder();
        Assert.assertNotNull(perlBuilder);
    }

    @Test
    public void can_echoVersion()
    {
        String PERL_VERSION_FLAG = "-v";
        String EXPECTED_PERL_VERSION_SNIPPET = "v"; // e.g., v5.8.8

        PerlBuilder perlBuilder = new PerlBuilder();
        perlBuilder.executeCommand(PERL_VERSION_FLAG);

        String versionInfo = perlBuilder.getOutput();
        Assert.assertThat(
                versionInfo,
                StringContains.containsString(EXPECTED_PERL_VERSION_SNIPPET)
        );
    }
}

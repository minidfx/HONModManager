package com.honmodmanager.services;

import com.github.jlinqer.collections.List;
import com.github.jlinqer.linq.IEnumerable;
import com.honmodmanager.models.ConditionElement;
import com.honmodmanager.models.ModImpl;
import com.honmodmanager.models.VersionImpl;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Version;
import junit.framework.TestCase;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Burgy Benjamin
 */
public final class ConditionEvaluatorImplTest extends TestCase
{
    private ConditionEvaluatorImpl instance;

    public ConditionEvaluatorImplTest(String testName)
    {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        this.instance = new ConditionEvaluatorImpl();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void test_evaluate_with_empty_condition()
    {
        Version versionStub = new VersionImpl();
        Mod modStub1 = new ModImpl("stub1", versionStub, true);
        ConditionElement conditionMock = mock(ConditionElement.class);
        when(conditionMock.getCondition()).thenReturn("");

        IEnumerable<Mod> mods = new List<>(modStub1);

        boolean result = this.instance.evaluate(conditionMock, mods);

        Assert.assertTrue(result);
    }

    public void test_evaluate_with_simple_condition()
    {
        Version versionStub = new VersionImpl();
        Mod modStub1 = new ModImpl("stub1", versionStub, true);
        ConditionElement conditionMock = mock(ConditionElement.class);
        when(conditionMock.getCondition()).thenReturn("mod1");

        IEnumerable<Mod> mods = new List<>(modStub1);

        boolean result = this.instance.evaluate(conditionMock, mods);

        Assert.assertTrue(result);
    }
}

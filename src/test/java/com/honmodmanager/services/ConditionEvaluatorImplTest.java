package com.honmodmanager.services;

import junit.framework.TestCase;
import org.junit.Assert;

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
        boolean result = this.instance.evaluate("", null);

        Assert.assertTrue(result);
    }

    public void test_evaluate_with_simple_condition()
    {
        boolean result = this.instance.evaluate("mod1", null);

        Assert.assertTrue(result);
    }
}

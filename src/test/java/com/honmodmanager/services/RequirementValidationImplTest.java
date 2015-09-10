package com.honmodmanager.services;

import com.honmodmanager.models.RequirementImpl;
import com.honmodmanager.models.VersionImpl;
import com.honmodmanager.models.contracts.Version;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 *
 * @author Burgy Benjamin
 */
public final class RequirementValidationImplTest extends TestCase
{
    private RequirementValidationImpl instance;
    private RequirementImpl fakeRequirement;

    public RequirementValidationImplTest(String testName)
    {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        this.instance = new RequirementValidationImpl();
        this.fakeRequirement = new RequirementImpl(new VersionImpl(1, 0, 0, 0), new VersionImpl(2, 0, 0, 0));
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testIsValid()
    {
        Version[] validVersions = new Version[]
        {
            new VersionImpl(1, 0, 0, 0),
            new VersionImpl(1, 0, 0, 1),
            new VersionImpl(1, 9, 9, 9),
            new VersionImpl(2, 0, 0, 0)
        };

        for (Version valiVersion : validVersions)
        {
            Assert.assertTrue(this.instance.isValid(valiVersion, this.fakeRequirement));
        }
    }

    public void testIsInvalid()
    {
        Version[] invalidVersions = new Version[]
        {
            new VersionImpl(0, 0, 0, 9),
            new VersionImpl(2, 0, 0, 1)
        };

        for (Version invalidVersion : invalidVersions)
        {
            Assert.assertFalse(this.instance.isValid(invalidVersion, this.fakeRequirement));
        }
    }
}

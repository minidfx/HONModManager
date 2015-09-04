package com.honmodmanager.services;

import com.honmodmanager.models.RequierementImpl;
import com.honmodmanager.models.VersionImpl;
import com.honmodmanager.models.contracts.Version;
import junit.framework.TestCase;

/**
 *
 * @author Burgy Benjamin
 */
public class RequierementValidationImplTest extends TestCase
{
    private RequierementValidationImpl instance;
    private RequierementImpl fakeRequierement;

    public RequierementValidationImplTest(String testName)
    {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        this.instance = new RequierementValidationImpl();
        this.fakeRequierement = new RequierementImpl(new VersionImpl(1, 0, 0, 0), new VersionImpl(2, 0, 0, 0));
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
            assertTrue(this.instance.isValid(valiVersion, this.fakeRequierement));
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
            assertFalse(this.instance.isValid(invalidVersion, this.fakeRequierement));
        }
    }
}

package com.honmodmanager.models;

import com.honmodmanager.models.contracts.Version;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 *
 * @author Burgy Benjamin
 */
public final class VersionImplTest extends TestCase
{
    private final VersionImpl instance;

    public VersionImplTest(String testName)
    {
        super(testName);

        this.instance = new VersionImpl(1, 2, 3, 4);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testGetMajor()
    {
        Assert.assertEquals(1, this.instance.getMajor());
    }

    public void testGetMinor()
    {
        Assert.assertEquals(2, this.instance.getMinor());
    }

    public void testGetFix()
    {
        Assert.assertEquals(3, this.instance.getFix());
    }

    public void testGetBuild()
    {
        Assert.assertEquals(4, this.instance.getBuild());
    }

    public void testToString()
    {
        String expectedValue = "1.2.3.4";
        Assert.assertEquals(expectedValue, this.instance.toString());
    }

    public void testGreaterThan_with_smaller_version_returing_true()
    {
        Version smallerVersion = new VersionImpl(1, 0, 0, 0);
        Assert.assertTrue(this.instance.greaterThan(smallerVersion));
    }

    public void testGreaterThan_with_same_version_returing_false()
    {
        Version smallerVersion = new VersionImpl(1, 2, 3, 4);
        Assert.assertFalse(this.instance.greaterThan(smallerVersion));
    }

    public void testGreaterThan_with_higher_version_returing_false()
    {
        Version[] smallerVersions = new VersionImpl[]
        {
            new VersionImpl(2, 2, 3, 4),
            new VersionImpl(1, 3, 3, 4),
            new VersionImpl(1, 2, 4, 4),
            new VersionImpl(1, 2, 3, 5),
            new VersionImpl(2, 0, 0, 0)
        };

        for (Version version : smallerVersions)
        {
            Assert.assertFalse(String.format("%s > %s", this.instance, version), this.instance.greaterThan(version));
        }
    }

    public void testLowerThan_with_higher_version_returning_true()
    {
        Version higherVersion = new VersionImpl(2, 0, 0, 0);
        Assert.assertTrue(this.instance.lowerThan(higherVersion));
    }

    public void testLowerThan_with_same_version_returning_false()
    {
        Version smallerVersion = new VersionImpl(1, 2, 3, 4);
        Assert.assertFalse(this.instance.lowerThan(smallerVersion));
    }

    public void testLowerThan_with_smaller_version_returning_false()
    {
        Version[] smallerVersions = new Version[]
        {
            new VersionImpl(0, 1, 0, 0),
            new VersionImpl(0, 0, 1, 0),
            new VersionImpl(0, 0, 0, 1)
        };

        for (Version version : smallerVersions)
        {
            Assert.assertFalse(String.format("%s > %s", this.instance, version), this.instance.lowerThan(version));
        }
    }

    public void testIsSame()
    {
        Version aVersion = new VersionImpl(1, 2, 3, 4);
        Assert.assertTrue(this.instance.isSame(aVersion));
    }

    public void testIsSame_with_instance_version_and_version_passed_as_null()
    {
        Version nullVersion = new VersionImpl(0, 0, 0, 0);
        Assert.assertTrue(this.instance.isSame(nullVersion));
    }

    public void testIsSame_with_tested_version_as_null()
    {
        Version primaryVersion = new VersionImpl(0, 0, 0, 0);
        Assert.assertTrue(primaryVersion.isSame(this.instance));
    }
}

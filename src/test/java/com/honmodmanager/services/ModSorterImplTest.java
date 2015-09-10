package com.honmodmanager.services;

import com.github.jlinqer.collections.List;
import com.honmodmanager.exceptions.ModSortException;
import com.honmodmanager.models.ModImpl;
import com.honmodmanager.models.VersionImpl;
import com.honmodmanager.models.contracts.Mod;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 *
 * @author Burgy Benjamin
 */
public final class ModSorterImplTest extends TestCase
{
    private ModSorterImpl instance;
    public ModSorterImplTest(String testName)
    {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        
        this.instance = new ModSorterImpl();
    }
    
    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testSort() throws ModSortException
    {
        Mod mod1 = new ModImpl("mod1", null, false);
        Mod mod2 = new ModImpl("mod2", null, false);
        Mod mod3 = new ModImpl("mod3", null, false);
        Mod mod4 = new ModImpl("mod4", null, false);
        Mod mod5 = new ModImpl("mod5", null, false);
        
        mod1.setId("mod1");
        mod2.setId("mod2");
        mod3.setId("mod3");
        mod4.setId("mod4");
        mod5.setId("mod5");
       
        // Currently, don't use the version for sorting mods.
        mod1.addApplyAfter("mod3", new VersionImpl());
        mod2.addApplyAfter("mod4", new VersionImpl());
        mod5.addApplyBefore("mod3", new VersionImpl());

        List<Mod> mods = new List<>();
        mods.add(mod1);
        mods.add(mod2);
        mods.add(mod3);
        mods.add(mod4);
        mods.add(mod5);

        List<Mod> expectedMods = new List<>();
        expectedMods.add(mod5);
        expectedMods.add(mod3);
        expectedMods.add(mod4);
        expectedMods.add(mod1);
        expectedMods.add(mod2);
        
        List<Mod> sortedMods = this.instance.sort(mods);
       
        Assert.assertArrayEquals(expectedMods.toArray(), sortedMods.toArray());
    }
}

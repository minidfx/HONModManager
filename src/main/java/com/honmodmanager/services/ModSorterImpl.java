package com.honmodmanager.services;

import com.github.jlinqer.collections.Dictionary;
import com.github.jlinqer.collections.List;
import com.honmodmanager.exceptions.ModSortException;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.models.contracts.Version;
import com.honmodmanager.services.contracts.ModSorter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * A basic first implementation but It works!
 * 
 * @author Burgy Benjamin
 */
@Service
@Scope("singleton")
public final class ModSorterImpl implements ModSorter
{   
    @Override
    @SuppressWarnings("empty-statement")
    public List<Mod> sort(List<Mod> mods) throws ModSortException
    {
        List<Mod> sorted = mods.toList();
        
        // List to identify which mods aren't sorted.
        List<Mod> modNotSorted = mods.toList();
        
        // First sort by after operation
        while(modNotSorted.any())
        {
            Mod currentMod = modNotSorted.first();
            Dictionary<String, Version> applyAfter = currentMod.getApplyAfter();
            
            List<Mod> tempSortedList = null;
            for(Mod mod : sorted)
            {
                if(applyAfter.any(x -> x.getKey().equals(mod.getId()))) 
                {
                     tempSortedList = this.insertAfter(sorted, mod, currentMod);
                }
            }
            
            if(tempSortedList != null)
            {
                sorted = null;
                sorted = tempSortedList;
            }
            
            modNotSorted.remove(currentMod);
        }
        
        // Reset the list
        modNotSorted = mods.toList();
        
        // Second sort by before operation
        while(modNotSorted.any())
        {
            Mod currentMod = modNotSorted.first();
            Dictionary<String, Version> applyBefore = currentMod.getApplyBefore();
            
            List<Mod> tempSortedList = null;
            for(Mod mod : sorted)
            {
                if(applyBefore.any(x -> x.getKey().equals(mod.getId()))) 
                {
                     tempSortedList = this.insertBefore(sorted, mod, currentMod);
                }
            }
            
            if(tempSortedList != null)
            {
                sorted = null;
                sorted = tempSortedList;
            }
            
            modNotSorted.remove(currentMod);
        }
        
        return sorted;
    }
    
    private List<Mod> insertBefore(List<Mod> mods, Mod before, Mod insert) throws ModSortException
    {
        for(int i=0; i<mods.size(); i++)
        {
            Mod currentMod = mods.get(i);
            if(currentMod.getId().equals(before.getId()))
            {
                return this.insert(mods, insert, i);
            }
        }
        
        throw new ModSortException("Cannot found the mod 'after'.");
    }
    
    private List<Mod> insertAfter(List<Mod> mods, Mod after, Mod insert) throws ModSortException
    {
        for(int i=0; i<mods.size(); i++)
        {
            Mod currentMod = mods.get(i);
            if(currentMod.getId().equals(after.getId()))
            {
                return this.insert(mods, insert, i+1);
            }
        }
        
        throw new ModSortException("Cannot found the mod 'after'.");
    }
    
    private List<Mod> insert(List<Mod> mods, Mod mod, int index)
    {
        List<Mod> out = mods.toList();
        
        out.remove(mod);
        out.add(index, mod);
        
        return out;
    }
}


package com.honmodmanager.services;

import com.honmodmanager.services.contracts.ModIdBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class ModIdBuilderImpl implements ModIdBuilder
{
    @Override
    public String build(String modName)
    {
        char[] tempModName = modName.toCharArray();
        String modFixName = new String();

        for (int i = 0;
             i < tempModName.length;
             modFixName += Character.isLetter(tempModName[i++]) || Character.isDigit(tempModName[i - 1]) ? tempModName[i - 1] : "");

        return modFixName.toLowerCase();
    }
}

package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public interface ZipCommentsBuilder
{
    void init();

    void addMod(Mod mod);

    String build();
}

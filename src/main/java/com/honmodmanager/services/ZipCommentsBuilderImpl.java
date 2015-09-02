package com.honmodmanager.services;

import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.GameInformation;
import com.honmodmanager.services.contracts.ZipCommentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class ZipCommentsBuilderImpl implements ZipCommentsBuilder
{
    private final GameInformation gameInformation;
    private StringBuilder commentsBuilder;

    /**
     *
     * @param gameInformation
     */
    @Autowired
    public ZipCommentsBuilderImpl(GameInformation gameInformation)
    {
        this.gameInformation = gameInformation;
    }

    @Override
    public void init()
    {
        StringBuilder comments = new StringBuilder();

        comments.append(String.format("HoN Mod Manager v%s Output\n\n",
                                      this.gameInformation.getModMangerVersion()));
        comments.append(String.format("Game Version: %s\n", this.gameInformation.getVersion()));
        comments.append("Applieds Mods: ");

        this.commentsBuilder = comments;
    }

    @Override
    public void addMod(Mod mod)
    {
        this.commentsBuilder.append(String.format("%s (v%s)\n", mod.getId(), mod.getVersion()));
    }

    @Override
    public String build()
    {
        return this.commentsBuilder.toString();
    }
}

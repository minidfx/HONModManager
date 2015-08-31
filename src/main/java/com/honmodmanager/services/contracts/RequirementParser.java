package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Requierement;
import java.text.ParseException;

/**
 *
 * @author Burgy Benjamin
 */
public interface RequirementParser
{
    Requierement parse(String versions) throws ParseException;
}

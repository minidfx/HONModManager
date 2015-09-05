package com.honmodmanager.services.contracts;

import com.honmodmanager.models.contracts.Requirement;
import java.text.ParseException;

/**
 *
 * @author Burgy Benjamin
 */
public interface RequirementParser
{
    Requirement parse(String versions) throws ParseException;
}

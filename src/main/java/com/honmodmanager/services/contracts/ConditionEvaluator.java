package com.honmodmanager.services.contracts;

import com.github.jlinqer.linq.IEnumerable;
import com.honmodmanager.models.ConditionElement;
import com.honmodmanager.models.contracts.Mod;

/**
 *
 * @author Burgy Benjamin
 */
public interface ConditionEvaluator
{
    boolean evaluate(ConditionElement editFileElement, IEnumerable<Mod> mods);
}

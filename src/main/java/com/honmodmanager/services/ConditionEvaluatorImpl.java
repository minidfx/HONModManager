package com.honmodmanager.services;

import com.github.jlinqer.linq.IEnumerable;
import com.honmodmanager.models.ConditionElement;
import com.honmodmanager.models.contracts.CopyFileElement;
import com.honmodmanager.models.contracts.EditFileElement;
import com.honmodmanager.models.contracts.Mod;
import com.honmodmanager.services.contracts.ConditionEvaluator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class ConditionEvaluatorImpl implements ConditionEvaluator
{
    @Override
    public boolean evaluate(ConditionElement conditionElement, IEnumerable<Mod> enabledMods)
    {
        String conditionTrimmed = conditionElement.getCondition();
        Mod[] mods = enabledMods.toArray(Mod.class);

        if (conditionTrimmed.isEmpty())
            return true;

        if (conditionTrimmed.startsWith("not"))
        {
            return this.evaluateNegativeCondition(conditionTrimmed);
        }

        return this.evaluatePositiveCondition(conditionTrimmed);
    }

    private boolean evaluateNegativeCondition(String condition)
    {
        return true;
    }

    private boolean evaluatePositiveCondition(String condition)
    {
        return true;
    }
}

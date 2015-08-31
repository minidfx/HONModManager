package com.honmodmanager.services;

import com.honmodmanager.services.contracts.ConditionEvaluator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public final class ConditionEvaluatorImpl implements ConditionEvaluator
{
    @Override
    public boolean evaluate(String condition)
    {
        String conditionTrimmed = condition.trim();

        if (conditionTrimmed.isEmpty())
            return true;

        if (conditionTrimmed.startsWith("not"))
        {
            return this.evaluateNegativeCondition(condition);
        }

        return this.evaluatePositiveCondition(condition);
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

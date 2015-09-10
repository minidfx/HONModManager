package com.honmodmanager.models.contracts;

import com.github.jlinqer.collections.List;
import javafx.util.Pair;

/**
 *
 * @author Burgy Benjamin
 */
public interface EditOperation
{
    EditOperationType getOperationType();

    List<Pair<String, String>> getAttributes();

    String getText();
}

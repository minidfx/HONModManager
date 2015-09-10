package com.honmodmanager.services.contracts;

import com.github.jlinqer.collections.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Benjamin Burgy
 */
public interface DocumentNavigator
{
    Element getElement(List<Node> nodes, int index);

    Element getFirstElement(Element element);
}

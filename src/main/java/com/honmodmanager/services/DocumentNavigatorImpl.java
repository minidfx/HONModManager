package com.honmodmanager.services;

import com.github.jlinqer.collections.List;
import com.honmodmanager.services.contracts.DocumentNavigator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
@Scope("singleton")
public final class DocumentNavigatorImpl implements DocumentNavigator
{
    @Override
    public Element getFirstElement(Element element)
    {
        NodeList nodes = element.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            if (node instanceof Element)
            {
                return (Element) node;
            }
        }

        throw new UnsupportedOperationException("Cannot found the first element of the specified element.");
    }

    @Override
    public Element getElement(List<Node> nodes, int index)
    {
        return nodes.ofType(Element.class).skip(index - 1).single();
    }
}

/*
// $Id$
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 1998-2002 Kana Software, Inc.
// Copyright (C) 2001-2012 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
// jhyde, 21 January, 1999
*/
package mondrian.olap;

import org.olap4j.impl.Named;

import java.util.Locale;

/**
 * An <code>OlapElement</code> is a catalog object (dimension, hierarchy,
 * level, member).
 *
 * @version $Id$
 */
public interface OlapElement extends Named {
    String getUniqueName();

    String getDescription();

    /**
     * Looks up a child element, returning null if it does not exist.
     */
    OlapElement lookupChild(
        SchemaReader schemaReader,
        Id.Segment s,
        MatchType matchType);

    /**
     * Returns the name of this element qualified by its class, for example
     * "hierarchy 'Customers'".
     */
    String getQualifiedName();

    String getCaption();

    /**
     * Returns the value of a property (caption or description) of
     * this element in the given locale.
     *
     * @param locale Locale
     * @return Localized caption or description
     */
    String getLocalized(LocalizedProperty prop, Locale locale);

    /**
     * Returns the hierarchy of an expression.
     */
    Hierarchy getHierarchy();

    /**
     * Returns the dimension of a this expression, or null if no dimension is
     * defined. Applicable only to set expressions.
     *
     * <p>Example 1:
     * <blockquote><pre>
     * [Sales].children
     * </pre></blockquote>
     * has dimension <code>[Sales]</code>.</p>
     *
     * <p>Example 2:
     * <blockquote><pre>
     * order(except([Promotion].[Media Type].members,
     *              {[Promotion].[Media Type].[No Media]}),
     *       [Measures].[Unit Sales], DESC)
     * </pre></blockquote>
     * has dimension [Promotion].</p>
     *
     * <p>Example 3:
     * <blockquote><pre>
     * CrossJoin([Product].[Product Department].members,
     *           [Gender].members)
     * </pre></blockquote>
     * has no dimension (well, actually it is [Product] x [Gender], but we
     * can't represent that, so we return null);</p>
     */
    Dimension getDimension();

    /**
     * Returns whether this element is visible to end-users.
     *
     * <p>Visibility is a hint for client applications. An element's visibility
     * does not affect how it is treated when MDX queries are evaluated.
     *
     * @return Whether this element is visible
     */
    boolean isVisible();

    enum LocalizedProperty {
        CAPTION,
        DESCRIPTION
    }
}

// End OlapElement.java
/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2004-2005 TONBELLER AG
// Copyright (C) 2006-2009 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
*/
package mondrian.rolap;

import mondrian.olap.Evaluator;
import mondrian.rolap.aggmatcher.AggStar;
import mondrian.rolap.sql.*;

import java.util.*;

/**
 * TupleConstaint which restricts the result of a tuple sqlQuery to a
 * set of parents.  All parents must belong to the same level.
 *
 * @author av
 * @since Nov 10, 2005
 * @version $Id$
 */
class DescendantsConstraint implements TupleConstraint {
    List<RolapMember> parentMembers;
    MemberChildrenConstraint mcc;

    /**
     * Creates a DescendantsConstraint.
     *
     * @param parentMembers list of parents all from the same level
     *
     * @param mcc the constraint that would return the children for each single
     * parent
     */
    public DescendantsConstraint(
        List<RolapMember> parentMembers,
        MemberChildrenConstraint mcc)
    {
        this.parentMembers = parentMembers;
        this.mcc = mcc;
    }

    public void addConstraint(
        SqlQuery sqlQuery,
        RolapStarSet starSet,
        AggStar aggStar)
    {
        mcc.addMemberConstraint(sqlQuery, starSet, aggStar, parentMembers);
    }

    public void addLevelConstraint(
        SqlQuery sqlQuery,
        RolapStarSet starSet,
        AggStar aggStar,
        RolapLevel level)
    {
        mcc.addLevelConstraint(sqlQuery, starSet, aggStar, level);
    }

    public MemberChildrenConstraint getMemberChildrenConstraint(
        RolapMember parent)
    {
        return mcc;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation returns null, because descendants is not cached.
     */
    public Object getCacheKey() {
        return null;
    }

    public Evaluator getEvaluator() {
        return null;
    }

    public List<RolapMeasureGroup> getMeasureGroupList() {
        return mcc instanceof TupleConstraint
            ? ((TupleConstraint) mcc).getMeasureGroupList()
            : Collections.<RolapMeasureGroup>emptyList();
    }

    public boolean isJoinRequired() {
        return mcc instanceof TupleConstraint
            && ((TupleConstraint) mcc).isJoinRequired();
    }
}

// End DescendantsConstraint.java
/*
// $Id$
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2002-2002 Kana Software, Inc.
// Copyright (C) 2002-2009 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
// jhyde, 21 March, 2002
*/
package mondrian.rolap;

import mondrian.olap.Util;

import java.util.Arrays;
import java.util.List;

/**
 * <code>MemberKey</code> todo:
 *
 * @see mondrian.olap.Util#deprecated(Object, boolean) Review to ensure that
 * this data structure is as memory-efficient as possible. Compare with
 * RolapMemberBase.key.
 *
 * @author jhyde
 * @since 21 March, 2002
 * @version $Id$
 */
class MemberKey {
    private final RolapMember parent;
    private final Object[] value;

    MemberKey(RolapMember parent, Object[] value) {
        this.parent = parent;
        this.value = value;
    }

    MemberKey(RolapMember parent, List<Object> value) {
        this.parent = parent;
        this.value = value.toArray(new Object[value.size()]);
    }

    // override Object
    public boolean equals(Object o) {
        if (!(o instanceof MemberKey)) {
            return false;
        }
        MemberKey other = (MemberKey) o;
        return Util.equals(this.parent, other.parent)
            && Arrays.equals(other.value, this.value);
    }

    // override Object
    public int hashCode() {
        if (parent == null && value == null) {
            return 0;
        }
        if (parent == null && value != null) {
            return (value.hashCode() << 16);
        }
        if (parent != null && value == null) {
            return (parent.hashCode() << 16);
        }
        return (parent.hashCode() << 16) ^ value.hashCode();
    }
}

// End MemberKey.java
/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.jet.datamodel;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * An immutable 4-tuple of statically typed fields.
 *
 * @param <E0> the type of the field 0
 * @param <E1> the type of the field 1
 * @param <E2> the type of the field 2
 * @param <E3> the type of the field 3
 *
 * @since 3.0
 */
public final class Tuple4<E0, E1, E2, E3> {
    private final E0 f0;
    private final E1 f1;
    private final E2 f2;
    private final E3 f3;

    /**
     * Constructs a new 3-tuple with the supplied values.
     */
    private Tuple4(E0 f0, E1 f1, E2 f2, E3 f3) {
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
    }

    /**
     * Returns a new 5-tuple with the supplied values.
     */
    public static <E0, E1, E2, E3> Tuple4<E0, E1, E2, E3> tuple4(
            @Nullable E0 f0, @Nullable E1 f1, @Nullable E2 f2, @Nullable E3 f3
    ) {
        return new Tuple4<>(f0, f1, f2, f3);
    }

    /**
     * Returns the value of the field 0.
     */
    @Nullable
    public E0 f0() {
        return f0;
    }

    /**
     * Returns the value of the field 1.
     */
    @Nullable
    public E1 f1() {
        return f1;
    }

    /**
     * Returns the value of the field 2.
     */
    @Nullable
    public E2 f2() {
        return f2;
    }

    /**
     * Returns the value of the field 3.
     */
    @Nullable
    public E3 f3() {
        return f3;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tuple4)) {
            return false;
        }
        Tuple4 that = (Tuple4) o;
        return Objects.equals(this.f0, that.f0)
                && Objects.equals(this.f1, that.f1)
                && Objects.equals(this.f2, that.f2)
                && Objects.equals(this.f3, that.f3);
    }

    @Override
    public int hashCode() {
        int hc = 17;
        hc = 73 * hc + Objects.hashCode(f0);
        hc = 73 * hc + Objects.hashCode(f1);
        hc = 73 * hc + Objects.hashCode(f2);
        hc = 73 * hc + Objects.hashCode(f3);
        return hc;
    }

    @Override
    public String toString() {
        return "(" + f0 + ", " + f1 + ", " + f2 + ", " + f3 + ')';
    }
}

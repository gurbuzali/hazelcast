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

package com.hazelcast.jet.sql.impl.aggregate.function;

import com.hazelcast.sql.impl.calcite.validate.HazelcastCallBinding;
import com.hazelcast.sql.impl.calcite.validate.operators.HazelcastReturnTypeInference;
import com.hazelcast.sql.impl.calcite.validate.operators.common.HazelcastAggFunction;
import com.hazelcast.sql.impl.calcite.validate.param.NoOpParameterConverter;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlDynamicParam;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSyntax;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorScope;
import org.apache.calcite.util.Optionality;

public class HazelcastCountAggFunction extends HazelcastAggFunction {

    public HazelcastCountAggFunction() {
        super(
                "COUNT",
                SqlKind.COUNT,
                // TODO [viliam] How to use BIGINT(64)? Currently, BIGINT(63) is used
                HazelcastReturnTypeInference.wrap(ReturnTypes.BIGINT),
                null,
                null,
                SqlFunctionCategory.NUMERIC,
                false,
                false,
                Optionality.FORBIDDEN);
    }

    @Override
    public SqlSyntax getSyntax() {
        return SqlSyntax.FUNCTION_STAR;
    }

    @Override
    public RelDataType deriveType(
            SqlValidator validator,
            SqlValidatorScope scope,
            SqlCall call
    ) {
//         Check for COUNT(*) function.  If it is, we don't want to try and derive the "*"
        if (call.isCountStar()) {
            return validator.getTypeFactory().createSqlType(
                    SqlTypeName.BIGINT);
        }
        return super.deriveType(validator, scope, call);
    }

    protected boolean checkOperandTypes(HazelcastCallBinding binding, boolean throwOnFailure) {
        SqlNode node = binding.operand(0);
        if (node.getKind() == SqlKind.DYNAMIC_PARAM) {
            int parameterIndex = ((SqlDynamicParam) node).getIndex();
            binding.getValidator().setParameterConverter(parameterIndex, NoOpParameterConverter.INSTANCE);
        }

        // COUNT accepts any operand type
        return true;
    }
}

package com.cleanonfire.api.core;

/**
 * Created by heitor-12 on 08/08/17.
 */

public interface Mapper<Input,Output> {
    Output map(Input input);
}

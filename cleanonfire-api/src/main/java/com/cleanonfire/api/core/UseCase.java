package com.cleanonfire.api.core;

import com.cleanonfire.api.interaction.OnErrorListener;
import com.cleanonfire.api.interaction.OnResultListener;

/**
 * Created by heitor-12 on 08/08/17.
 */

public interface UseCase<Params,Return> {
    void execute(Params params, OnResultListener<Return> resultListener, OnErrorListener errorListener);
}

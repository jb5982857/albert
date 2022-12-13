package com.albert.application.lifecycle.apt;

import com.albert.application.lifecycle.api.IAApplicationLifecycleTemp;

import java.util.ArrayList;
import java.util.List;

public class AApplicationApts {
    public List<IAApplicationLifecycleTemp> getPlugins() {
        return new ArrayList<>();
    }
}

package com.example.myboardgames.adapters;

import java.util.Map;
import java.util.Set;

public interface ChangeStateEvent {
    void callback(Map<String, Set<String>> checkedItems);
}
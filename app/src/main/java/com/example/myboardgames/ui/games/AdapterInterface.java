package com.example.myboardgames.ui.games;

public interface AdapterInterface {
    void notifyGameChanged(int position);
    void notifyGameRemoved(int position);
}
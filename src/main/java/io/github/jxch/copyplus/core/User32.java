package io.github.jxch.copyplus.core;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface User32 extends StdCallLibrary {

    User32 INSTANCE = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

    void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);

}

package com.bsu.stoninois.misc;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface Prefs {
    @DefaultString("")
    String token();

    @DefaultString("admin")
    String userName();

    @DefaultString("127.0.0.1")
    String ipAddress();

    @DefaultString("80")
    String port();
}

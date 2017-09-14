package com.pahomov.exifeditor.lite.Dagger;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by grok on 9/11/17.
 */

@Qualifier
@Retention(RUNTIME)
public @interface ForDaggerApp {
}
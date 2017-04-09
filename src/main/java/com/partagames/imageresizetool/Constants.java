/*
* Copyright 2015 Parta Games Oy
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
package com.partagames.imageresizetool;

import com.google.common.collect.ImmutableList;

/**
 * Created by Antti on 21.9.2015.
 */
public class Constants {
    public static final String VERSION = "0.0.2";
    public static final ImmutableList<String> OUTPUT_IMAGE_FORMATS = ImmutableList.of("png", "jpg", "gif");
    public static final ImmutableList<String> SUPPORTED_SCALING_HINTS = ImmutableList.of("n", "b");

    // list the command line arguments
    public static final String ARG_DIMENSIONS_SHORT = "d";
    public static final String ARG_OUTPUT_SHORT = "o";
    public static final String ARG_FORMAT_SHORT = "f";
    public static final String ARG_HINT_SHORT = "s";
    public static final String ARG_HELP_SHORT = "h";

    public static final String ARG_DIMENSIONS = "dimensions";
    public static final String ARG_OUTPUT = "output";
    public static final String ARG_FORMAT = "format";
    public static final String ARG_HINT = "scalinghint";
    public static final String ARG_HELP = "help";
}

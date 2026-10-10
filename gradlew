#!/bin/sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a symlink
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls -ld "$PRG"
    LINK=`expr "$PRG" : '.*-> \(.*\)$'`
    if expr "$LINK" : '/.*' > /dev/null; then
        PRG="$LINK"
    else
        PRG=`dirname "$PRG"`/"$LINK"
    fi
done
SAVEPWD=`pwd`
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME=`pwd -P`
cd "$SAVEPWD" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != maximum.
MAX_FD="maximum"

warn ( ) {
    echo "$*"
} >&2

die ( ) {
    echo
    echo "$*"
    echo
    exit 1
} >&2

# OS specific support (must be 'true' or 'false').
darwin=false
msys=false
cygwin=false
nativeFor=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MSYS* | MINGW* )
    msys=true
    ;;
  NATIVEIMAGE )
    nativeFor=true
    ;;
esac

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Increase the maximum file descriptors if we can.
if [ "$cygwin" = "false" -a "$darwin" = "false" -a "$nativeFor" = "false" ] ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -e 0 ] ; then
        if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
            MAX_FD="$MAX_FD_LIMIT"
        fi
        ulimit -n $MAX_FD
        if [ $? -ne 0 ] ; then
            warn "Could not set maximum file descriptor limit: $MAX_FD"
        fi
    else
        warn "Could not query maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
fi

# For Darwin, add options to specify how the application appears in the dock
if [ "$darwin" = "true" ] ; then
    GRADLE_OPTS="$GRADLE_OPTS \"-Xdock:name=$APP_NAME\" \"-Xdock:icon=$APP_HOME/media/gradle.icns\""
fi

# For Cygwin or MSYS, switch paths to Windows format before running java
if [ "$cygwin" = "true" -o "$msys" = "true" ] ; then
    APP_HOME=`cygpath --path --mixed "$APP_HOME"`
    CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`

    JAVACMD=`cygpath --unix "$JAVACMD"`

    # We build the pattern for arguments to be converted via cygpath
    ROOTDIRSRAW=`find -L / -maxdepth 3 -type d -name java >/dev/null 2>&1 && echo "ROOTDIR_FOUND=true" || echo "ROOTDIR_FOUND=false"`
    eval "$ROOTDIRSRAW"
    if [ "$ROOTDIR_FOUND" = true ] ; then
      JAVA_HOME=`cygpath --path --mixed "$JAVA_HOME"`
    fi
    for arg in "$@" ; do
        if expr "$arg" : '-' > /dev/null ; then
            false
        else
            arg=`cygpath --path --windows "$arg"`
        fi
        CLASSPATH="$CLASSPATH:$arg"
    done
fi

# Escape application args
save ( ) {
    for arg do
        printf '%s\n' "$arg" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/'/ " >> "$APP_ARGS_FILE"
    done
}
APP_ARGS_FILE="$APP_HOME/gradle.properties"
if [ -n "${ZSH_VERSION}" ] ; then
    APP_ARGS_FILE="$( cd \"$APP_HOME\" && pwd )/gradle.properties"
fi
touch "$APP_ARGS_FILE"
save "$@"

set -- $(
        printf '%s\n' "$DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS" |
        xargs -n1 |
        sed 's/^-D/-D/g' |
        tr '\n' ' '
    ) "${@}"

# Stop when "xargs" by itself outputs nothing at all.
CMD="$( ( printf '%s ' "$JAVACMD" "$@" ) | head -c 32768 )"
( eval "$CMD" )

EXIT_CODE=$?
if [ -n "$APP_ARGS_FILE" ] ; then
    rm -f "$APP_ARGS_FILE"
fi
exit $EXIT_CODE

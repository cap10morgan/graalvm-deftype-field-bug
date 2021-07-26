# graalvm-deftype-field-bug

## Building

Install latest version of clojure CLI (as of 2021-07-26 it needs the latest
prerelease, not the latest stable release) for tools.build support.

## Running

Then run: `clj -T:build native-image`

Then you can run `java -jar target/gdfb.jar` to see the regular JVM behavior
and `./gdfb` for graalvm native-image behavior.

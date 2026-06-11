xquery version "3.1" encoding "UTF-8";
(:~
 : Build script: downloads dependencies, compiles Java, packages XAR.
 : Requires network access for Maven Central downloads.
 :)
declare namespace pkg="http://expath.org/ns/pkg";
import module namespace build = "urn:quodatum.service.build" at "lib/buildx.xqm";
declare variable $BASE:= file:resolve-path("../",static-base-uri());
declare variable $BUILDER:=doc($BASE || "build.xml");
declare function local:resolve($path){
  file:resolve-path($path,static-base-uri()=>file:parent()=>file:parent())
(:   =>trace($path || "->") :)
};


declare variable $build:=local:resolve("build/");

(: Step 1: Download  from Maven Central :)
let $_:= $BUILDER/build/artifact/jar
         !``[`{@name}`:`{@version}`]``
         =>build:maven-download($BASE || "build/lib/")

(: Step 2: Compile Java sources :)


let $_:=build:javac($BUILDER/build/javac,map{"dir":$BASE=>trace("BASE ")})
(: create jar :)
let $_:=build:jar($BUILDER/build/jar,map{"dir":$BASE})


return "Done"
 
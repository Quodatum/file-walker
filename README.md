# file-walker
File list  and search in the XProc style. 
````
import module namespace fw="quodatum.file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
fw:directory-list($test:dir,map{"depth":-1})
````
see https://www.w3.org/TR/xproc/#c.directory-list

Also testing the performance of the built-in file module against 
a Java SimpleFileVisitor implementation.
https://docs.oracle.com/javase/7/docs/api/java/nio/file/FileVisitResult.html 

## Performance

````
<testsuites time="PT9M22.656S">
  <testsuite name="file:///C:/Users/andy/git/file-walker/src/test/test.xqm" time="PT9M22.654S" tests="2" failures="0" errors="0" skipped="0">
    <testcase name="directory-list" time="PT5M36.935S"/>
    <testcase name="directory-list-xq" time="PT3M45.697S"/>
  </testsuite>
</testsuites>

````

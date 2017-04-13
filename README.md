# file-walker
Directory file listing. The output uses the format from the XProc `directory-list` step. 
See https://www.w3.org/TR/xproc/#c.directory-list and
https://github.com/transpect/xproc-util/blob/master/recursive-directory-list/xpl/recursive-directory-list.xpl

```xquery
import module namespace fw="quodatum:file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
let $options:=map{}
return fw:directory-list("Z:/recordings/",$options)
```
## Options

|name|type|default|description
|----|----|-------|-----------
|max-depth|integer|-1|directory depth to scan, -1 = all
|include-info|boolean|false|add @size and @last-modified attributes to o/p file and directory nodes
|max-files|integer||stop scanning after finding this number of files
|follow-links|boolean|false|follow links
|include-filter|string|| A regex include only where name matches, applied only to file names
|exclude-filter|string|| A regex omit where name matches, applied only to file names
|skip-filter|string|| A regex omit this and children where name matches, applied only to directory names

## Sample output
````
<directory xmlns="http://www.w3.org/ns/xproc-step" name="radio" xml:base="file:///Z:/recordings/radio/">
  <directory name="radio" xml:base="file:///Z:/recordings/radio/">
    <directory name="Book of the Week" xml:base="file:///Z:/recordings/radio/Book%20of%20the%20Week/">
      <file name="Book of the Week.mka"/>
    </directory>
    <directory name="Book-at-Bedtime" xml:base="file:///Z:/recordings/radio/Book-at-Bedtime/"/>
    <directory name="Book-of-the-Week" xml:base="file:///Z:/recordings/radio/Book-of-the-Week/">
      <file name="Book-of-the-Week.Memories---From-Moscow-to-the-Black-Sea,-by-Teffi.-1-5.mp3"/>
      <file name="Book-of-the-Week.Memories---From-Moscow-to-the-Black-Sea,-by-Teffi.-2-5.mp3"/>
    </directory>
    <directory name="From-Our-Own-Correspondent" xml:base="file:///Z:/recordings/radio/From-Our-Own-Correspondent/">
      <file name="Can-the-leaders-of-South-Sudan's-civil-war-now-lead-the-country-to-peace_.mka.mp3"/>
      <file name="Stories-from-North-Korea,-Hungary,-Mexico,-the-United-States-and-Turkey..mka.mp3"/>
      <file name="the-Philippines,-Greece,-Venezuela,-Albania-and-St-Helena..mka.mp3"/>
    </directory>
    <directory name="Just-a-Minute" xml:base="file:///Z:/recordings/radio/Just-a-Minute/">
      <file name="Just-a-Minute.Nicholas-Parsons-hosts-the-popular-panel-game.-1-6.mp3"/>
    </directory>
    <directory name="The-News-Quiz" xml:base="file:///Z:/recordings/radio/The-News-Quiz/">
      <file name="The-News-Quiz.A-satirical-review-of-the-week's-news,-chaired-by-Miles-Jupp.-4-9.mp3"/>
      <file name="The-News-Quiz.A-satirical-review-of-the-week's-news,-chaired-by-Miles-Jupp.-5-9.mp3"/>
    </directory>
  </directory>
</directory>
````

## include-info vs file info
The `include-info` option currently provides the first 2 attributes from the proposed XProc
step https://www.w3.org/XML/XProc/docs/fileos/#pf-info
* last-modified   xs:dateTime     The last modification time of the object expressed in UTC.
* size    xs:integer  The size of the object in bytes.
* readable    xs:boolean  “true” if the object is readable.
* writable    xs:boolean  “true” if the object file is writable.
* hidden  xs:boolean  “true” if the object is hidden.


## Performance notes
Also testing the performance of the built-in file module against 
a Java `SimpleFileVisitor` implementation.
https://docs.oracle.com/javase/7/docs/api/java/nio/file/SimpleFileVisitor.html 
The result depends a lot on the environment. Results listing a folder tree containing 25,000 files across a LAN. With version 0.2.8.
Apart from the ,strange but repeatable, ReadyNAS xq result there is no significant performance gain from the Java implementation. 

### Velvet - ReadyNAS server

````
velvet
<testsuites time="PT10M46.481S">
  <testsuite name="file:///C:/Users/andy/git/file-walker/src/test/test.xqm" time="PT10M46.479S" tests="2" failures="0" errors="0" skipped="0">
    <testcase name="directory-list" time="PT1M40.283S"/>
    <testcase name="directory-list-xq" time="PT9M6.185S"/>
  </testsuite>
</testsuites>

<testsuites time="PT10M24.341S">
  <testsuite name="file:///C:/Users/andy/git/file-walker/src/test/test.xqm" time="PT10M24.341S" tests="2" failures="0" errors="0" skipped="0">
    <testcase name="directory-list" time="PT1M35.96S"/>
    <testcase name="directory-list-xq" time="PT8M48.37S"/>
  </testsuite>
</testsuites>
````

### Odroid xu4
cpuctrl -s -g "powersave" -m 300M -M 1G

````
<testsuites time="PT8M33.356S">
  <testsuite name="file:///C:/Users/andy/git/file-walker/src/test/test.xqm" time="PT8M33.352S" tests="2" failures="0" errors="0" skipped="0">
    <testcase name="directory-list" time="PT4M28.039S"/>
    <testcase name="directory-list-xq" time="PT4M5.293S"/>
  </testsuite>
</testsuites>
````
cpuctrl -s  -g performance  -M 1.4G 
````
<testsuites time="PT3M31.073S">
  <testsuite name="file:///C:/Users/andy/git/file-walker/src/test/test.xqm" time="PT3M31.073S" tests="2" failures="0" errors="0" skipped="0">
    <testcase name="directory-list" time="PT1M45.852S"/>
    <testcase name="directory-list-xq" time="PT1M45.213S"/>
  </testsuite>
</testsuites>

````
Running directly on the server takes about 44sec.

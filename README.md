# file-walker
File list  and search in the XProc style. 

````
import module namespace fw="quodatum:file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
fw:directory-list($test:dir,$options)
````
##Options
maxDepth: integer
showFileInfo: boolean
maxFiles: integer
include-filter:string @TODO
exclude-filter:string @TODO

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
see https://www.w3.org/TR/xproc/#c.directory-list
## file info
readable    xs:boolean  “true” if the object is readable.
writable    xs:boolean  “true” if the object file is writable.
hidden  xs:boolean  “true” if the object is hidden.
last-modified   xs:dateTime     The last modification time of the object expressed in UTC.
size    xs:integer  The size of the object in bytes.
https://www.w3.org/XML/XProc/docs/fileos/#pf-info

## Performance
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

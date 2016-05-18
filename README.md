# file-walker
File list  and search in the XProc style. 
````
import module namespace fw="quodatum.file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
fw:directory-list($test:dir,map{"depth":-1})
````
see https://www.w3.org/TR/xproc/#c.directory-list
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
Also testing the performance of the built-in file module against 
a Java `SimpleFileVisitor` implementation.
https://docs.oracle.com/javase/7/docs/api/java/nio/file/SimpleFileVisitor.html 

## Performance
Currently the file module is winning:
````
<testsuites time="PT9M22.656S">
  <testsuite name="file:///C:/Users/andy/git/file-walker/src/test/test.xqm" time="PT9M22.654S" tests="2" failures="0" errors="0" skipped="0">
    <testcase name="directory-list" time="PT5M36.935S"/>
    <testcase name="directory-list-xq" time="PT3M45.697S"/>
  </testsuite>
</testsuites>

````

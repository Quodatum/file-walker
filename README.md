# file-walker
`file-walker` generates file system directory listings for `BaseX`. 
It produces more structured and detailed results than 
[`file:list`](http://docs.basex.org/wiki/File#file:list). It also performs better, often significantly so. 
The output is XML using the format from the XProc `directory-list` step. 
See [c.directory-list](https://www.w3.org/TR/xproc/#c.directory-list) and
[recursive-directory-list.xpl](https://github.com/transpect/xproc-util/blob/master/recursive-directory-list/xpl/recursive-directory-list.xpl)

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
|include-filter|string|| A Java regex include only where name matches, applied only to file names
|exclude-filter|string|| A Java regex omit where name matches, applied only to file names
|skip-filter|string|| A Java regex omit this and children where name matches, applied only to directory names

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
* readable    xs:boolean  �true� if the object is readable.
* writable    xs:boolean  �true� if the object file is writable.
* hidden  xs:boolean  �true� if the object is hidden.

## History
* v0.7 for use with basex9.1 https://github.com/BaseXdb/basex/issues/2027
* v0.6 for use with basex9.1
## Road map
* Use XQuery regex
* allow use of XQuery functions in options to determine inclusion/exclusion
* return files found within given time 

## Performance notes
The following shows times for file-walker (with include-info) vs file:list. The first pair of result is for a local tree of 400,000 files. The other pairs are for LAN accessed folders of around 50,000 files.
```xml
<testsuites time="PT23M18.847S">
  <testsuite name="file:///C:/Users/andy/git/file-walker/src/test/performance.xqm" time="PT23M18.846S" tests="6" failures="0" errors="0" skipped="0">
    <testcase name="directory-list" time="PT12.817S"/>
    <testcase name="directory-list-xq" time="PT1M1.311S"/>
    <testcase name="remote-directory-list" time="PT3M7.53S"/>
    <testcase name="remote-directory-list-xq" time="PT5M17.128S"/>
    <testcase name="remote-directory-list2" time="PT38.957S"/>
    <testcase name="remote-directory-list-xq2" time="PT13M1.094S"/>
  </testsuite>
</testsuites>
```

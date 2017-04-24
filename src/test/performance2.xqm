module namespace test = 'http://basex.org/modules/xqunit-tests';
(:~
 : performance tests for filewalker module
 : effects of options on performance
 :)
import module namespace fw="quodatum:file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
declare variable $test:odroid:="\\ODROID-JESSIE\sda1\pictures\Pictures";
declare variable $test:velvet:="P:\pictures\Pictures";
declare variable $test:home:="C:\Users\Andy";
declare variable $test:dir:=$test:odroid;
declare variable $test:opts0:=map{"max-depth":-1};
declare variable $test:opts1:=map{"max-depth":-1,"include-info":true()};
declare variable $test:opts2:=map{"max-depth":-1,"exclude-filter":"zzzz"};
declare variable $test:opts3:=map{"max-depth":-1,"exclude-filter":"zzzz","include-filter":".*"};
(:~ directory-list :)
declare
  %unit:test
  function test:directory-list0() {
  let $r:=fw:directory-list($test:home,$test:opts0)
  return unit:assert($r)  
};
 
(:~ directory-list :)
declare
  %unit:test
  function test:directory-list1() {
  let $r:=fw:directory-list($test:home,$test:opts1)
  return unit:assert($r)  
};

(:~ directory-list :)
declare
  %unit:test
  function test:directory-list2() {
  let $r:=fw:directory-list($test:home,$test:opts2)
  return unit:assert($r)  
};
(:~ directory-list :)
declare
  %unit:test
  function test:directory-list3() {
  let $r:=fw:directory-list($test:home,$test:opts3)
  return unit:assert($r)  
};  
module namespace test = 'http://basex.org/modules/xqunit-tests';
(:~
 : unit tests for filewalker module
 :)
import module namespace fw="quodatum.file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
declare variable $test:dir:="\\ODROID-JESSIE\sda1\pictures\Pictures";

 
(:~ directory-list :)
declare
  %unit:test
  function test:directory-list() {
  let $r:=fw:directory-list($test:dir,map{"depth":-1})
  let $_:=trace($r//c:file=>count(),"files: ")
  return unit:assert($r)  
};
 
declare
  %unit:test
  function test:directory-list-xq() {
  let $r:=fw:directory-list-xq($test:dir,map{"depth":-1})
  let $_:=trace($r//c:file=>count(),"files: ")
  return unit:assert($r)  
};
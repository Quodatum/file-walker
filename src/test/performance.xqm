module namespace test = 'http://basex.org/modules/xqunit-tests';
(:~
 : performance  tests for filewalker module
 : compare against file:list for local and 2 network servers
 :)
import module namespace fw="quodatum:file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
declare variable $test:odroid:="\\ODROID-JESSIE\sda1\pictures\Pictures";
declare variable $test:velvet:="P:\pictures\Pictures";
declare variable $test:home:="C:\Users\Andy";
declare variable $test:dir:=$test:odroid;
declare variable $test:opts:=map{"max-depth":-1,"include-info":true()};

(:~ directory-list :)
declare
  %unit:test
  function test:directory-list() {
  let $r:=fw:directory-list($test:home,$test:opts)
  return unit:assert($r)  
};
 
declare
  %unit:test
  function test:directory-list-xq() {
  let $r:=file:list($test:home,true())
  return unit:assert(count($r))  
};

declare
  %unit:test
  function test:remote-directory-list() {
  let $r:=fw:directory-list($test:odroid,$test:opts)
  return unit:assert($r)  
};
 
declare
  %unit:test
  function test:remote-directory-list-xq() {
  let $r:=file:list($test:odroid,true())
  return unit:assert(count($r))  
};
declare
  %unit:test
  function test:remote-directory-list2() {
  let $r:=fw:directory-list($test:velvet,$test:opts)
  return unit:assert($r)  
};
 
declare
  %unit:test
  function test:remote-directory-list-xq2() {
  let $r:=file:list($test:velvet,true())
  return unit:assert(count($r))  
};
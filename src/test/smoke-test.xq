import module namespace fw="quodatum.file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
declare variable $large:="Z:\pictures\Pictures";
declare variable $small:="Z:\recordings\radio";
declare variable $local:="C:\Users\andy\Desktop\radio";

let $r:= fw:directory-list-xq($small,map{"depth":-1})
return $r
 

 
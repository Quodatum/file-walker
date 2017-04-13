import module namespace fw="quodatum:file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
declare variable $large:="Z:\pictures\Pictures";
declare variable $small:="Z:\recordings\radio";
declare variable $local:="C:\Users\andy\Desktop\radio";
let $opts:=map{
    "include-info":false(),
    "max-files":2,
    "include-filter":"(?).*\.svg" 
}
let $r:= fw:directory-list("C:\Users",$opts)
return $r 
 

 
import module namespace fw="quodatum:file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
declare variable $large:="Z:\pictures\Pictures";
declare variable $small:="Z:\recordings\radio";
declare variable $local:="C:\Users\andy\Desktop\radio";
let $opts:=map{
    "include-info":true(),
    "max-files":5
   
}
let $r:= fw:directory-list("C:\Users\Andy",$opts)
let $a:=copy $c:=$r 
        modify delete node $c//c:directory[not(descendant::c:file)]
        return $c
return $a 
 

 
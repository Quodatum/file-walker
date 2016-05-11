import module namespace fw="quodatum.file.walker";
declare namespace c="http://www.w3.org/ns/xproc-step";
(: fw:web-resolve("")=> fw:directory-list-xq() :)
let $r:=fw:web-resolve("")=> fw:directory-list(map{"depth":-1})
return $r


 
(:~ 
 : XProc influenced file system tools
 :@author Andy Bunce
 :@version 0.2
 :)
module namespace xpf = 'quodatum.file.walker';
declare namespace c="http://www.w3.org/ns/xproc-step";

(:~
 : path to webapps with trailing slash 
 :)
declare variable $xpf:base:= db:system()/globaloptions/webpath/fn:string()
                             || file:dir-separator();
 
 (:~
  : list contents
  :)                            
declare function xpf:directory-list($path) as element(c:directory)
{
let $resolved:=file:resolve-path($path,$xpf:base)
let $files:=file:list($resolved)
return 
    <c:directory name="{file:name($path)}" xml:base="{file:path-to-uri($resolved)}">
        {for $f in $files
        let $full:=file:resolve-path($f,$resolved)
        let $name:=file:name($full)
        return if(file:is-dir($full)) then <c:directory name="{$name}"/> else <c:file name="{$name}"/>
        }
    </c:directory>
};

declare function xpf:resolve-uri($c as element(*)) as xs:string
{
    resolve-uri($c/@name,base-uri($c))
};
declare function xpf:resolve-path($c as element(*)) as xs:string
{
    file:resolve-path($c/@name,base-uri($c))
};
declare function xpf:name($c as element(*)) as xs:string
{
    $c/@name/string()
};
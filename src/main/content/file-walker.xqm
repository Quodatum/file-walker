(:~ 
 : XProc influenced file system tools
 :@author Andy Bunce
 :@version 0.2
 :)
module namespace xpf = 'quodatum.file.walker';
declare namespace c="http://www.w3.org/ns/xproc-step";

 
 (:~
  : list contents
  :)                            
declare function xpf:directory-list($path as xs:string,$options as map(*)) as element(c:directory)
{
let $files:=file:list($path)
return 
    <c:directory name="{file:name($path)}" xml:base="{file:path-to-uri($path)}">
        {for $f in $files
        let $full:=file:resolve-path($f,$path)
        let $name:=file:name($full)
        return if(file:is-dir($full)) then <c:directory name="{$name}"/> else <c:file name="{$name}"/>
        }
    </c:directory>
};


declare function xpf:directory-list($path as xs:string) as element(c:directory)
{
    xpf:directory-list($path,map{})
};

(:~
 : full path to file
 :)
declare function xpf:resolve-uri($c as element(*)) as xs:string
{
    resolve-uri($c/@name,base-uri($c))
};

(:~
 : full path to file
 :)
declare function xpf:resolve-path($c as element(*)) as xs:string
{
    file:resolve-path($c/@name,base-uri($c))
};

(:~
 : file name
 :)
declare function xpf:name($c as element(*)) as xs:string
{
    $c/@name/string()
};

(:~
 : convenience function for web app use
 :)
declare function xpf:web-resolve($path as xs:string) as xs:string
{
    let $b:=db:system()/globaloptions/webpath/fn:string()
                             || file:dir-separator()
    return file:resolve-path($path,$b)
};
(:~ 
 : XProc influenced file system tools
 : @see l:recursive-directory-list http://xproc.org/library/
 :@author Andy Bunce
 :@version 0.2
 :)
module namespace xpf = 'quodatum.file.walker';
declare namespace c="http://www.w3.org/ns/xproc-step";

declare variable $xpf:default-options:=map{
    "depth":0
    };

(:~
  : list contents using java
  :)                            
declare function xpf:directory-list($path as xs:string,$options as map(*)) as element(c:directory)
{
let $options:=xpf:defaults($options)
return Q{java:com.quodatum.file.Runner}filewalk($path,$options)

};


declare function xpf:directory-list($path as xs:string) as element(c:directory)
{
    xpf:directory-list($path,map{})
};

(:~
  : list contents using xquery
  :)                            
declare function xpf:directory-list-xq($path as xs:string,$options as map(*)) as element(c:directory)
{
let $options:=xpf:defaults($options)
return xpf:directory-list-xq2($path ,$options,$options?depth)
};

(:~
  : list contents using xquery
  :)                            
declare function xpf:directory-list-xq2($path as xs:string,$options as map(*),$depth as xs:integer) as element(c:directory)
{
let $files:=file:children($path)
return 
    <c:directory name="{file:name($path)}" xml:base="{file:path-to-uri($path)}">
        {for $f in $files
        return if(file:is-file($f))           
               then <c:file name="{file:name($f)}"/>
               else if($depth eq 0) then ()
               else xpf:directory-list-xq2($f,$options, $depth -1)
        }
    </c:directory>
};

declare function xpf:directory-list-xq($path as xs:string) as element(c:directory)
{
    xpf:directory-list-xq($path,map{})
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
 : apply default options
 :)
declare function xpf:defaults($options as map(*)) as map(*)
{
map:merge(($xpf:default-options,$options))
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
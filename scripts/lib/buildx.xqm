(:~
 build utils: 
   java compile, jar creation, maven download
 @author Andy Bunce
 @copyright Quodatum Ltd
 @license Apache 2
 @since may-2015
:)
module namespace build = 'urn:quodatum.service.build';

declare namespace pkg="http://expath.org/ns/pkg";


 

declare function build:javac($javac as element(javac),$opts as map(*)) 
{
  let $_:=trace($javac,"FFF") 
  let $javaSrc:=$javac/@srcdir
  let $javaFiles :=file:list($javaSrc,true(),"*.java")
                !concat($javaSrc,.)
                =>trace("javac files: ")
  let $classpath:=$javac/@classpath
  let $args:=("-cp",$classpath,
              "-sourcepath",$javaSrc,
              "-d",$javac/@destdir,
              "-source",$javac/@source,
              "-target",$javac/@target,
              $javaFiles)
  let $_:=trace(string-join($args," "),"javac ")
  return proc:system("javac",$args,$opts)
};



(:~ 
 : write xqdoc for $src/$path to $dest
 :)
declare %updating  function build:write-xqdoc($path,$src,$dest){
  let $url:=fn:resolve-uri( $path,$src)=>fn:trace()
  let $type:=fetch:content-type($url)
 
  return  switch($type)
    case "application/xquery"
      return file:write(
          fn:resolve-uri($path || ".xml",$dest),
           inspect:xqdoc($url)
         )
    default 
      return ()

};
(:~ 
: name of dist xar file eg "fred-0.1.0.xar"
:)
declare function build:xar-name($package as element(pkg:package)) as xs:string
{
fn:concat($package/@abbrev , "-" ,$package/@version, ".xar")
};

(: build xar:)
declare function build:xar($xar as element(xar),$BASE as xs:string) as empty-sequence()
{
let $version := $xar/@version/string()
let $dir:=file:resolve-path($xar/@srcdir,$BASE )

let $files := file:list($dir,true())[not(ends-with(.,file:dir-separator() ))]
let $zip := archive:create($files, $files ! file:read-binary($dir || .))

let $entries:=$xar/item/@entry/string()
let $content:=$xar/item/@src! file:read-binary($BASE || .)
let $basex:=build:basex-xml($entries,$xar/@class)
let $zip:=archive:update($zip
                       ,($entries,"basex.xml","expath-pkg.xml")
                       ,($content,$basex,build:expath-pkg($dir || "expath-pkg.xml",$version))
                       )

let $dest:=string:format($xar/@dest,$version)=>file:resolve-path($BASE )
return build:write-binary($zip,$dest)
};

(:~ construct basex.xml
@see https://docs.basex.org/main/Repository#java 
:)
declare function build:basex-xml($jars as xs:string*,$class as xs:string)
as xs:string
{
  <pkg:package >
    {$jars!<pkg:jar>{ file:name(.) }</pkg:jar>}
    <pkg:class>{ $class }</pkg:class>
  </pkg:package>
  =>serialize()
};

declare function build:expath-pkg($src as xs:string,$version as xs:string)
as xs:string 
{
  let $u:=doc($src)  update {
  replace value of node /pkg:package/@version with $version
  }
  return serialize($u)
};

(:~ 
: update package.xml located at $cxan to ensure has entry for package $pkg
:)
declare %updating function build:publish($pkg as element(pkg:package),$cxan)
{
let $doc:=copy  $c:=fn:doc($cxan)
          modify(
          let $pack:=$c/repo/pkg[name=$pkg/@name]
          let $hit:= $pack/version[@num=$pkg/@version]
          let $new:=<version num="{$pkg/@version}">
                    <!-- generated: {fn:current-dateTime()} -->
                    </version>
          return if($hit)then () 
                 else insert node $new into $pack
               )
          return $c
return fn:put($doc,$cxan)
};

(:================ jar ==========================:)
declare function build:jar($jar as element(jar),$opts as map(*))
as xs:string
{
let $class:=file:resolve-path($jar/@class,base-uri($jar))=>trace("BBBase: ")
let $dest:=file:resolve-path($jar/@dest,base-uri($jar))=>trace("dest: ")
let $files := file:list($class, true(), '*.class')
 
return( 
  archive:create($files, $files ! file:read-binary($class || .))
  =>build:update-manifest($jar/@main-class!map{"Main-Class":.})
  =>build:write-binary($dest)
  , "jar saved"
)
};

(:~ update/add manifest.mf in $jar :)
declare function build:update-manifest($jar  as xs:base64Binary,$entries as map(*)?)
as xs:base64Binary{
let $mf2:=concat("Manifest-Version: 1.0&#xA;",
                 $entries!map:for-each(.,function($k,$v){$k || ": " || $v || "&#xA;"}),
                 "&#xA;")
return archive:update($jar,"META-INF/MANIFEST.MF",$mf2)
};

(:================= maven ======================= :)
declare variable $build:REPO as xs:string external :="https://repo1.maven.org/maven2/";

(:~ download $files from $urls to  $destdir:)
declare function build:maven-download($artifacts as xs:string*,$destdir as xs:string)
as empty-sequence(){
    file:create-dir($destdir),    
    for $id in $artifacts
    let $slug:=build:maven-slug($id)
    let $dest:=$destdir || file:name($slug) 
    where not(file:exists($dest))
    return build:write-binary(fetch:binary(resolve-uri($slug,$build:REPO)
           =>trace("Download: ")),$dest)
};

(:~ non-rooted url for maven $artifact 
groupId:artifactId:version [:classifier]
:)
declare function build:maven-slug($artifact as xs:string)
as xs:string{
   
   let $parts:=if(matches($artifact,'[^:]+:[^:]+:[^:]+'))
               then tokenize($artifact,":")
               else error(xs:QName('build:maven-slug'),"invalid format required 'groupId:id:version' " || $artifact)
  
    return (
            translate($parts[1],".","/"),
            $parts[2],
            $parts[3],
            string-join(($parts[2] , "-" , $parts[3] ,
            if(3<count($parts)) then "-" || $parts[4] else (), (: classifier :)
             ".jar"),"")
    )=>string-join("/")
};

(:~ write-binary, creating dir if required :)
declare  function build:write-binary($contents as xs:base64Binary?
                                          ,$dest as xs:string)
as empty-sequence(){
file:create-dir(file:parent($dest)),
file:write-binary($dest,$contents)
};

(:~
 : file paths below $src
 : $src typically from resolve-uri
 : @return sequences of relative file paths "content/ebnf/CR-xquery-31-20141218.ebnf" "..."
 :)
 declare function build:files($src as xs:string) as xs:string*
 {
   fn:filter(file:list($src,fn:true()),
          function ($f){($src || $f)=>fn:translate("\","/")=>file:is-file()}
        )
          !fn:translate(.,"\","/") 
 };
/**
 *  jQuery Plug-in for KISSY that developed by jimmy.song at 2013/05/21
 *  Free Under MIT License
 *  @version 1.0
 */
KISSY.add("jquery/1.0/index",function(d){KISSY.$config=KISSY.$config||{};var a={};a.extend=function(g,h){if(typeof g!=="object"||typeof h!=="object"){return g}for(var f in h){if(typeof g[f]!=="undefined"&&typeof h[f]===typeof g[f]){if(typeof g[f]==="object"&&!(g[f] instanceof Array)){a.extend(g[f],h[f])}else{g[f]=h[f]}}else{g[f]=h[f]}}return g};a.extend(KISSY.$config,{base:"",group:{},loaded:[]});KISSY.$Group=(function(f){return function(g){f.extend(KISSY.$config.group,g)}})(a);var b=KISSY.Config;var e=b.packages;if(!e.jquery){e.jquery={base:b.base}}KISSY.$config.base=e.jquery.base+"jquery/plugins/";a.contains=function(g){var f=KISSY.$config.loaded;for(var h=0;h<f.length;h++){if(g.toLowerCase()===f[h]){return true}}return false};a.url=function(f){var g=f.lastIndexOf("/");var k="";var h=f;var i=f;if(g>0){h=f.substring(0,g);k=f.substring(g+1);var j=h.lastIndexOf("/");if(j==-1){i=h}else{i=h.substring(j+1)}}return KISSY.$config.base+h+"/jquery."+(i==""?"":i+".")+(k==""?"":k+".")+"min.js"};a.argument=function(f,h,j){for(var g=0;g<f.length;g++){if(typeof f[g]===h){return f[g]}}return j};a._push=function(j){var h=[];for(var g=0;g<j.length;g++){var f=j[g];if(f==""){continue}f=a.url(j[g]);if(!f||a.contains(f)){continue}h.push(f)}return h};a.loadScripts=(function(h,i){var f=function(){this.scripts={len:0,list:[],loaded:0,done:false};this.head=document.getElementsByTagName("head")[0]};KISSY.$config.loaded=d.unique(KISSY.$config.loaded);a.extend(f.prototype,{installLoadEvent:function(j,g,k){var l=this;if(document.all){j.attachEvent("onreadystatechange",function(){if(j.readyState=="loaded"||j.readyState=="complete"){g.loaded++;KISSY.$config.loaded.push(j.src);if(g.loaded>=g.len&&!g.done){k(l)}j.detachEvent("onreadystatechange",arguments.callee)}})}else{j.addEventListener("load",function(){g.loaded++;KISSY.$config.loaded.push(j.src);if(g.loaded>=g.len&&!g.done){g.done=true;k(l)}j.removeEventListener("load",arguments.call,false)},false)}},loadScripts:function(k,o){if(o.length<=0){this.scripts.done=true;k(this);return}this.scripts.len=o.length;for(var n=0;n<o.length;n++){var l=o[n];if(a.contains(l)){this.scripts.len--;continue}var p=document.createElement("script");p.type="text/javascript";p.src=encodeURI(l);KISSY.$config.loaded.push(p.src);this.head.appendChild(p);this.scripts.list.push(p);this.installLoadEvent(p,this.scripts,k)}if(this.scripts.len<=0){this.scripts.done=true;k(this);return}}});return function(j,n){var k=new f();k.loadScripts(j,n)}})();var c=e.jquery.group;c=!c?e.jquery.base+"jquery/jquery.group.js":c;a.use=function(i){var g={name:"",group:"",fn:function(){}};a.extend(g,i);var j=d.trim(g.name).split(",");var h=[];h=h.concat(a._push(j));var f=d.trim(g.group).split(",");a.loadScripts(function(){for(var n=0;n<f.length;n++){var k=KISSY.$config.group[f[n]];if(k&&k instanceof Array){h=h.concat(a._push(k))}}h=d.unique(h);var l=0;var m=KISSY.$config.base+"jquery.min.js";a.loadScripts(function(){if(h.length===0){g.fn();return}a.loadScripts(function(){g.fn()},h)},[m])},[c])};return a});
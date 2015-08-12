/**
 *  jQuery Plug-in for KISSY that developed by jimmy.song at 2013/05/21
 *  Free Under MIT License
 *  @version 1.0
 */
KISSY.add('jquery/1.0/index',function(S) {
	KISSY.$config = KISSY.$config || {};
	var jquery = {};
	jquery.extend = function(o,n){
		if (typeof o !== 'object' || typeof n !== 'object') {
			return o;
		}
		for ( var k in n) {
			if (typeof o[k] !== 'undefined' && typeof n[k] === typeof o[k]) {
				if (typeof o[k] === 'object' && !(o[k] instanceof Array)) {
					jquery.extend(o[k], n[k]);
				} else {
					o[k] = n[k];
				}
			} else {
				o[k] = n[k];
			}
		}
		return o;
	};
	jquery.extend(KISSY.$config,{
		base:'',
		group:{},
		loaded:[]
	});
	
	KISSY.$Group = (function($){
		return function(config){
			$.extend(KISSY.$config.group,config);
		};
	})(jquery);	
	
	var config = KISSY.Config;
	var packages = config.packages;
	if(!packages.jquery) {
		packages.jquery = {
			base:config.base
		};
	}
	KISSY.$config.base = packages.jquery.base+"jquery/plugins/";
	jquery.contains = function(url){
		var arr = KISSY.$config.loaded;
		for(var i = 0;i<arr.length;i++){
			if(url.toLowerCase() === arr[i]) {
				return true;
			}
		}
		return false;
	};
	
	/**
	 * build real path for url 
	 * @param m the name of component
	 */
	jquery.url = function(m) {
		var l = m.lastIndexOf('/');
		var n = '';
		var u = m;
		var px = m;
		if(l > 0){
			u = m.substring(0,l);
			n = m.substring(l+1);
			var pxi = u.lastIndexOf('/');
			if(pxi == -1) px = u;
			else px = u.substring(pxi+1);
		}
		return KISSY.$config.base+u+'/jquery.'+(px == ''?'':px+'.')+(n == '' ? '':n+'.')+'min.js';
	};
	
	/**
	 * dynamic assign arguments of function according to the type.
	 * @param args arguments of function
	 * @param the type that picked from arguments
	 * @param def the default value while can not matched the item from arguments.
	 */
	jquery.argument = function(args,type,def){
		for(var i = 0;i<args.length;i++){
			if(typeof args[i] === type){
				return args[i];
			}
		}
		return def;
	};
	
	/**
	 * make it sure that correctly resolve javascript library, and do guarantee that the library has been loaded uniquely. 
	 * @params urls array of loading library
	 */
	jquery._push= function(urls){
		var _urls = [];
		for(var i = 0;i<urls.length;i++) {
			var u = urls[i];
			if(u == '') {continue;}
			u = jquery.url(urls[i]);
			if(!u || jquery.contains(u)) {continue;}
			_urls.push(u);
		}
		return _urls;
	};
	
	/**
	 * rebuild dynamic loading javascript library
	 * @param m callback function for success loaded
	 * @param g array of loading library
	 */
	jquery.loadScripts = (function(m,g) {
		var loader = function(){
			this.scripts = {
				len : 0,
				list : [],
				loaded : 0,
				done : false
			};
			this.head = document.getElementsByTagName('head')[0];
		};
		KISSY.$config.loaded = S.unique(KISSY.$config.loaded);
		jquery.extend(loader.prototype,{
			installLoadEvent : function(obj, data, success) {
				var _this = this;
				if (document.all) {
					obj.attachEvent("onreadystatechange", function() {
						if (obj.readyState == "loaded" || obj.readyState == "complete") {
							data.loaded++;
							KISSY.$config.loaded.push(obj.src);
							if (data.loaded >= data.len && !data.done) {
								success(_this)
							}
							obj.detachEvent("onreadystatechange", arguments.callee)
						}
					})
				} else {
					obj.addEventListener("load", function() {
						data.loaded++;
						KISSY.$config.loaded.push(obj.src);
						if (data.loaded >= data.len && !data.done) {
							data.done = true;
							success(_this)
						}
						obj.removeEventListener("load", arguments.call, false)
					}, false)
				}
			},
			loadScripts:function(m,g){
				if (g.length <= 0) {
					this.scripts.done = true;
					m(this);
					return
				}
				this.scripts.len = g.length;
				for (var h = 0; h < g.length; h++) {
					var j = g[h];
					if(jquery.contains(j)){
						this.scripts.len--;
						continue;
					}
					var f = document.createElement("script");
					f.type = "text/javascript";
					f.src = encodeURI(j);
					KISSY.$config.loaded.push(f.src);
					this.head.appendChild(f);
					this.scripts.list.push(f);
					this.installLoadEvent(f, this.scripts, m);
				}
				if (this.scripts.len <= 0) {
					this.scripts.done = true;
					m(this);
					return
				}
			}
		});
		return function(m,g) {
			var l = new loader();
			l.loadScripts(m,g);
		};
	})();
	/**
	 * build jquery group of component
	 */
	var groupPath = packages.jquery.group;
	groupPath = !groupPath?packages.jquery.base+'jquery/jquery.group.js':groupPath;
	
	/**
	 *  Loading jQuery library according to the parameter
 	 * @param {Object} opts there is an object parameter, which builded as name , group and fn. 
	 */
	jquery.use = function(opts) {
		var _opts = {name:'',group:'',fn:function(){}};
		jquery.extend(_opts,opts);
		var urls = S.trim(_opts.name).split(',');
		var _urls = [];
		_urls = _urls.concat(jquery._push(urls));
		var gs = S.trim(_opts.group).split(',');
		jquery.loadScripts(function(){
			for(var i = 0;i<gs.length;i++){
				var v = KISSY.$config.group[gs[i]];
				if(v && v instanceof Array){
					_urls = _urls.concat(jquery._push(v));
				}
			}
			_urls = S.unique(_urls);
			var loaded = 0;
			var $path = KISSY.$config.base+'jquery.min.js';
			jquery.loadScripts(function(){
				if(_urls.length === 0){
					_opts.fn();
					return ;
				}
				jquery.loadScripts(function(){
					_opts.fn();
				},_urls);
			},[$path]);
		},[groupPath]);
	};
	return jquery;
});

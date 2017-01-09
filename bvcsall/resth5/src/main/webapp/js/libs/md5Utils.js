/**
	 * jQuery MD5 hash algorithm function
	 * 
	 * 	<code>
	 * 		Calculate the md5 hash of a String 
	 * 		String $.md5 ( String str )
	 * 	</code>
	 * 
	 * Calculates the MD5 hash of str using the » RSA Data Security, Inc. MD5 Message-Digest Algorithm, and returns that hash. 
	 * MD5 (Message-Digest algorithm 5) is a widely-used cryptographic hash function with a 128-bit hash value. MD5 has been employed in a wide variety of security applications, and is also commonly used to check the integrity of data. The generated hash is also non-reversable. Data cannot be retrieved from the message digest, the digest uniquely identifies the data.
	 * MD5 was developed by Professor Ronald L. Rivest in 1994. Its 128 bit (16 byte) message digest makes it a faster implementation than SHA-1.
	 * This script is used to process a variable length message into a fixed-length output of 128 bits using the MD5 algorithm. It is fully compatible with UTF-8 encoding. It is very useful when u want to transfer encrypted passwords over the internet. If you plan using UTF-8 encoding in your project don't forget to set the page encoding to UTF-8 (Content-Type meta tag). 
	 * This function orginally get from the WebToolkit and rewrite for using as the jQuery plugin.
	 * 
	 * Example
	 * 	Code
	 * 		<code>
	 * 			$.md5("I'm Persian."); 
	 * 		</code>
	 * 	Result
	 * 		<code>
	 * 			"b8c901d0f02223f9761016cfff9d68df"
	 * 		</code>
	 * 
	 * @alias Muhammad Hussein Fattahizadeh < muhammad [AT] semnanweb [DOT] com >
	 * @link http://www.semnanweb.com/jquery-plugin/md5.html
	 * @see http://www.webtoolkit.info/
	 * @license http://www.gnu.org/licenses/gpl.html [GNU General Public License]
	 * @param {jQuery} {md5:function(string))
	 * @return string
	 */

//	(function($){
//		var rotateLeft = function(lValue, iShiftBits) {
//			return (lValue << iShiftBits) | (lValue >>> (32 - iShiftBits));
//		}
//		var addUnsigned = function(lX, lY) {
//			var lX4, lY4, lX8, lY8, lResult;
//			lX8 = (lX & 0x80000000);
//			lY8 = (lY & 0x80000000);
//			lX4 = (lX & 0x40000000);
//			lY4 = (lY & 0x40000000);
//			lResult = (lX & 0x3FFFFFFF) + (lY & 0x3FFFFFFF);
//			if (lX4 & lY4) return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
//			if (lX4 | lY4) {
//				if (lResult & 0x40000000) return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
//				else return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
//			} else {
//				return (lResult ^ lX8 ^ lY8);
//			}
//		}
//		var F = function(x, y, z) {
//			return (x & y) | ((~ x) & z);
//		}
//		var G = function(x, y, z) {
//			return (x & z) | (y & (~ z));
//		}
//		var H = function(x, y, z) {
//			return (x ^ y ^ z);
//		}
//		var I = function(x, y, z) {
//			return (y ^ (x | (~ z)));
//		}
//		var FF = function(a, b, c, d, x, s, ac) {
//			a = addUnsigned(a, addUnsigned(addUnsigned(F(b, c, d), x), ac));
//			return addUnsigned(rotateLeft(a, s), b);
//		};
//		var GG = function(a, b, c, d, x, s, ac) {
//			a = addUnsigned(a, addUnsigned(addUnsigned(G(b, c, d), x), ac));
//			return addUnsigned(rotateLeft(a, s), b);
//		};
//		var HH = function(a, b, c, d, x, s, ac) {
//			a = addUnsigned(a, addUnsigned(addUnsigned(H(b, c, d), x), ac));
//			return addUnsigned(rotateLeft(a, s), b);
//		};
//		var II = function(a, b, c, d, x, s, ac) {
//			a = addUnsigned(a, addUnsigned(addUnsigned(I(b, c, d), x), ac));
//			return addUnsigned(rotateLeft(a, s), b);
//		};
//		var convertToWordArray = function(string) {
//			var lWordCount;
//			var lMessageLength = string.length;
//			var lNumberOfWordsTempOne = lMessageLength + 8;
//			var lNumberOfWordsTempTwo = (lNumberOfWordsTempOne - (lNumberOfWordsTempOne % 64)) / 64;
//			var lNumberOfWords = (lNumberOfWordsTempTwo + 1) * 16;
//			var lWordArray = Array(lNumberOfWords - 1);
//			var lBytePosition = 0;
//			var lByteCount = 0;
//			while (lByteCount < lMessageLength) {
//				lWordCount = (lByteCount - (lByteCount % 4)) / 4;
//				lBytePosition = (lByteCount % 4) * 8;
//				lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount) << lBytePosition));
//				lByteCount++;
//			}
//			lWordCount = (lByteCount - (lByteCount % 4)) / 4;
//			lBytePosition = (lByteCount % 4) * 8;
//			lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80 << lBytePosition);
//			lWordArray[lNumberOfWords - 2] = lMessageLength << 3;
//			lWordArray[lNumberOfWords - 1] = lMessageLength >>> 29;
//			return lWordArray;
//		};
//		var wordToHex = function(lValue) {
//			var WordToHexValue = "", WordToHexValueTemp = "", lByte, lCount;
//			for (lCount = 0; lCount <= 3; lCount++) {
//				lByte = (lValue >>> (lCount * 8)) & 255;
//				WordToHexValueTemp = "0" + lByte.toString(16);
//				WordToHexValue = WordToHexValue + WordToHexValueTemp.substr(WordToHexValueTemp.length - 2, 2);
//			}
//			return WordToHexValue;
//		};
//		var uTF8Encode = function(string) {
//			string = string.replace(/\x0d\x0a/g, "\x0a");
//			var output = "";
//			for (var n = 0; n < string.length; n++) {
//				var c = string.charCodeAt(n);
//				if (c < 128) {
//					output += String.fromCharCode(c);
//				} else if ((c > 127) && (c < 2048)) {
//					output += String.fromCharCode((c >> 6) | 192);
//					output += String.fromCharCode((c & 63) | 128);
//				} else {
//					output += String.fromCharCode((c >> 12) | 224);
//					output += String.fromCharCode(((c >> 6) & 63) | 128);
//					output += String.fromCharCode((c & 63) | 128);
//				}
//			}
//			return output;
//		};
//		$.extend({
//			md5: function(string) {
//				var x = Array();
//				var k, AA, BB, CC, DD, a, b, c, d;
//				var S11=7, S12=12, S13=17, S14=22;
//				var S21=5, S22=9 , S23=14, S24=20;
//				var S31=4, S32=11, S33=16, S34=23;
//				var S41=6, S42=10, S43=15, S44=21;
//				string = uTF8Encode(string);
//				x = convertToWordArray(string);
//				a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476;
//				for (k = 0; k < x.length; k += 16) {
//					AA = a; BB = b; CC = c; DD = d;
//					a = FF(a, b, c, d, x[k+0],  S11, 0xD76AA478);
//					d = FF(d, a, b, c, x[k+1],  S12, 0xE8C7B756);
//					c = FF(c, d, a, b, x[k+2],  S13, 0x242070DB);
//					b = FF(b, c, d, a, x[k+3],  S14, 0xC1BDCEEE);
//					a = FF(a, b, c, d, x[k+4],  S11, 0xF57C0FAF);
//					d = FF(d, a, b, c, x[k+5],  S12, 0x4787C62A);
//					c = FF(c, d, a, b, x[k+6],  S13, 0xA8304613);
//					b = FF(b, c, d, a, x[k+7],  S14, 0xFD469501);
//					a = FF(a, b, c, d, x[k+8],  S11, 0x698098D8);
//					d = FF(d, a, b, c, x[k+9],  S12, 0x8B44F7AF);
//					c = FF(c, d, a, b, x[k+10], S13, 0xFFFF5BB1);
//					b = FF(b, c, d, a, x[k+11], S14, 0x895CD7BE);
//					a = FF(a, b, c, d, x[k+12], S11, 0x6B901122);
//					d = FF(d, a, b, c, x[k+13], S12, 0xFD987193);
//					c = FF(c, d, a, b, x[k+14], S13, 0xA679438E);
//					b = FF(b, c, d, a, x[k+15], S14, 0x49B40821);
//					a = GG(a, b, c, d, x[k+1],  S21, 0xF61E2562);
//					d = GG(d, a, b, c, x[k+6],  S22, 0xC040B340);
//					c = GG(c, d, a, b, x[k+11], S23, 0x265E5A51);
//					b = GG(b, c, d, a, x[k+0],  S24, 0xE9B6C7AA);
//					a = GG(a, b, c, d, x[k+5],  S21, 0xD62F105D);
//					d = GG(d, a, b, c, x[k+10], S22, 0x2441453);
//					c = GG(c, d, a, b, x[k+15], S23, 0xD8A1E681);
//					b = GG(b, c, d, a, x[k+4],  S24, 0xE7D3FBC8);
//					a = GG(a, b, c, d, x[k+9],  S21, 0x21E1CDE6);
//					d = GG(d, a, b, c, x[k+14], S22, 0xC33707D6);
//					c = GG(c, d, a, b, x[k+3],  S23, 0xF4D50D87);
//					b = GG(b, c, d, a, x[k+8],  S24, 0x455A14ED);
//					a = GG(a, b, c, d, x[k+13], S21, 0xA9E3E905);
//					d = GG(d, a, b, c, x[k+2],  S22, 0xFCEFA3F8);
//					c = GG(c, d, a, b, x[k+7],  S23, 0x676F02D9);
//					b = GG(b, c, d, a, x[k+12], S24, 0x8D2A4C8A);
//					a = HH(a, b, c, d, x[k+5],  S31, 0xFFFA3942);
//					d = HH(d, a, b, c, x[k+8],  S32, 0x8771F681);
//					c = HH(c, d, a, b, x[k+11], S33, 0x6D9D6122);
//					b = HH(b, c, d, a, x[k+14], S34, 0xFDE5380C);
//					a = HH(a, b, c, d, x[k+1],  S31, 0xA4BEEA44);
//					d = HH(d, a, b, c, x[k+4],  S32, 0x4BDECFA9);
//					c = HH(c, d, a, b, x[k+7],  S33, 0xF6BB4B60);
//					b = HH(b, c, d, a, x[k+10], S34, 0xBEBFBC70);
//					a = HH(a, b, c, d, x[k+13], S31, 0x289B7EC6);
//					d = HH(d, a, b, c, x[k+0],  S32, 0xEAA127FA);
//					c = HH(c, d, a, b, x[k+3],  S33, 0xD4EF3085);
//					b = HH(b, c, d, a, x[k+6],  S34, 0x4881D05);
//					a = HH(a, b, c, d, x[k+9],  S31, 0xD9D4D039);
//					d = HH(d, a, b, c, x[k+12], S32, 0xE6DB99E5);
//					c = HH(c, d, a, b, x[k+15], S33, 0x1FA27CF8);
//					b = HH(b, c, d, a, x[k+2],  S34, 0xC4AC5665);
//					a = II(a, b, c, d, x[k+0],  S41, 0xF4292244);
//					d = II(d, a, b, c, x[k+7],  S42, 0x432AFF97);
//					c = II(c, d, a, b, x[k+14], S43, 0xAB9423A7);
//					b = II(b, c, d, a, x[k+5],  S44, 0xFC93A039);
//					a = II(a, b, c, d, x[k+12], S41, 0x655B59C3);
//					d = II(d, a, b, c, x[k+3],  S42, 0x8F0CCC92);
//					c = II(c, d, a, b, x[k+10], S43, 0xFFEFF47D);
//					b = II(b, c, d, a, x[k+1],  S44, 0x85845DD1);
//					a = II(a, b, c, d, x[k+8],  S41, 0x6FA87E4F);
//					d = II(d, a, b, c, x[k+15], S42, 0xFE2CE6E0);
//					c = II(c, d, a, b, x[k+6],  S43, 0xA3014314);
//					b = II(b, c, d, a, x[k+13], S44, 0x4E0811A1);
//					a = II(a, b, c, d, x[k+4],  S41, 0xF7537E82);
//					d = II(d, a, b, c, x[k+11], S42, 0xBD3AF235);
//					c = II(c, d, a, b, x[k+2],  S43, 0x2AD7D2BB);
//					b = II(b, c, d, a, x[k+9],  S44, 0xEB86D391);
//					a = addUnsigned(a, AA);
//					b = addUnsigned(b, BB);
//					c = addUnsigned(c, CC);
//					d = addUnsigned(d, DD);
//				}
//				var tempValue = wordToHex(a) + wordToHex(b) + wordToHex(c) + wordToHex(d);
//				return tempValue.toLowerCase();
//			}
//		});
//	})(jQuery);

define([],function(){var r=function(r,n){return r<<n|r>>>32-n},n=function(r,n){var t,o,e,u,f;return e=2147483648&r,u=2147483648&n,t=1073741824&r,o=1073741824&n,f=(1073741823&r)+(1073741823&n),t&o?2147483648^f^e^u:t|o?1073741824&f?3221225472^f^e^u:1073741824^f^e^u:f^e^u},t=function(r,n,t){return r&n|~r&t},o=function(r,n,t){return r&t|n&~t},e=function(r,n,t){return r^n^t},u=function(r,n,t){return n^(r|~t)},f=function(o,e,u,f,i,a,c){return o=n(o,n(n(t(e,u,f),i),c)),n(r(o,a),e)},i=function(t,e,u,f,i,a,c){return t=n(t,n(n(o(e,u,f),i),c)),n(r(t,a),e)},a=function(t,o,u,f,i,a,c){return t=n(t,n(n(e(o,u,f),i),c)),n(r(t,a),o)},c=function(t,o,e,f,i,a,c){return t=n(t,n(n(u(o,e,f),i),c)),n(r(t,a),o)},C=function(r){for(var n,t=r.length,o=t+8,e=(o-o%64)/64,u=16*(e+1),f=Array(u-1),i=0,a=0;t>a;)n=(a-a%4)/4,i=a%4*8,f[n]=f[n]|r.charCodeAt(a)<<i,a++;return n=(a-a%4)/4,i=a%4*8,f[n]=f[n]|128<<i,f[u-2]=t<<3,f[u-1]=t>>>29,f},g=function(r){var n,t,o="",e="";for(t=0;3>=t;t++)n=r>>>8*t&255,e="0"+n.toString(16),o+=e.substr(e.length-2,2);return o},h=function(r){r=r.replace(/\x0d\x0a/g,"\n");for(var n="",t=0;t<r.length;t++){var o=r.charCodeAt(t);128>o?n+=String.fromCharCode(o):o>127&&2048>o?(n+=String.fromCharCode(o>>6|192),n+=String.fromCharCode(63&o|128)):(n+=String.fromCharCode(o>>12|224),n+=String.fromCharCode(o>>6&63|128),n+=String.fromCharCode(63&o|128))}return n};return{md5:function(r){var t,o,e,u,d,v,m,S,l,A=Array(),s=7,x=12,y=17,b=22,p=5,w=9,L=14,j=20,k=4,q=11,z=16,B=23,D=6,E=10,F=15,G=21;for(r=h(r),A=C(r),v=1732584193,m=4023233417,S=2562383102,l=271733878,t=0;t<A.length;t+=16)o=v,e=m,u=S,d=l,v=f(v,m,S,l,A[t+0],s,3614090360),l=f(l,v,m,S,A[t+1],x,3905402710),S=f(S,l,v,m,A[t+2],y,606105819),m=f(m,S,l,v,A[t+3],b,3250441966),v=f(v,m,S,l,A[t+4],s,4118548399),l=f(l,v,m,S,A[t+5],x,1200080426),S=f(S,l,v,m,A[t+6],y,2821735955),m=f(m,S,l,v,A[t+7],b,4249261313),v=f(v,m,S,l,A[t+8],s,1770035416),l=f(l,v,m,S,A[t+9],x,2336552879),S=f(S,l,v,m,A[t+10],y,4294925233),m=f(m,S,l,v,A[t+11],b,2304563134),v=f(v,m,S,l,A[t+12],s,1804603682),l=f(l,v,m,S,A[t+13],x,4254626195),S=f(S,l,v,m,A[t+14],y,2792965006),m=f(m,S,l,v,A[t+15],b,1236535329),v=i(v,m,S,l,A[t+1],p,4129170786),l=i(l,v,m,S,A[t+6],w,3225465664),S=i(S,l,v,m,A[t+11],L,643717713),m=i(m,S,l,v,A[t+0],j,3921069994),v=i(v,m,S,l,A[t+5],p,3593408605),l=i(l,v,m,S,A[t+10],w,38016083),S=i(S,l,v,m,A[t+15],L,3634488961),m=i(m,S,l,v,A[t+4],j,3889429448),v=i(v,m,S,l,A[t+9],p,568446438),l=i(l,v,m,S,A[t+14],w,3275163606),S=i(S,l,v,m,A[t+3],L,4107603335),m=i(m,S,l,v,A[t+8],j,1163531501),v=i(v,m,S,l,A[t+13],p,2850285829),l=i(l,v,m,S,A[t+2],w,4243563512),S=i(S,l,v,m,A[t+7],L,1735328473),m=i(m,S,l,v,A[t+12],j,2368359562),v=a(v,m,S,l,A[t+5],k,4294588738),l=a(l,v,m,S,A[t+8],q,2272392833),S=a(S,l,v,m,A[t+11],z,1839030562),m=a(m,S,l,v,A[t+14],B,4259657740),v=a(v,m,S,l,A[t+1],k,2763975236),l=a(l,v,m,S,A[t+4],q,1272893353),S=a(S,l,v,m,A[t+7],z,4139469664),m=a(m,S,l,v,A[t+10],B,3200236656),v=a(v,m,S,l,A[t+13],k,681279174),l=a(l,v,m,S,A[t+0],q,3936430074),S=a(S,l,v,m,A[t+3],z,3572445317),m=a(m,S,l,v,A[t+6],B,76029189),v=a(v,m,S,l,A[t+9],k,3654602809),l=a(l,v,m,S,A[t+12],q,3873151461),S=a(S,l,v,m,A[t+15],z,530742520),m=a(m,S,l,v,A[t+2],B,3299628645),v=c(v,m,S,l,A[t+0],D,4096336452),l=c(l,v,m,S,A[t+7],E,1126891415),S=c(S,l,v,m,A[t+14],F,2878612391),m=c(m,S,l,v,A[t+5],G,4237533241),v=c(v,m,S,l,A[t+12],D,1700485571),l=c(l,v,m,S,A[t+3],E,2399980690),S=c(S,l,v,m,A[t+10],F,4293915773),m=c(m,S,l,v,A[t+1],G,2240044497),v=c(v,m,S,l,A[t+8],D,1873313359),l=c(l,v,m,S,A[t+15],E,4264355552),S=c(S,l,v,m,A[t+6],F,2734768916),m=c(m,S,l,v,A[t+13],G,1309151649),v=c(v,m,S,l,A[t+4],D,4149444226),l=c(l,v,m,S,A[t+11],E,3174756917),S=c(S,l,v,m,A[t+2],F,718787259),m=c(m,S,l,v,A[t+9],G,3951481745),v=n(v,o),m=n(m,e),S=n(S,u),l=n(l,d);var H=g(v)+g(m)+g(S)+g(l);return H.toLowerCase()}}});
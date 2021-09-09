var faceapi = (() => {
  var __defineProperty = Object.defineProperty;
  var __hasOwnProperty = Object.prototype.hasOwnProperty;
  var __assign = Object.assign;
  var __commonJS = (callback, module) => () => {
    if (!module) {
      module = {exports: {}};
      callback(module.exports, module);
    }
    return module.exports;
  };
  var __markAsModule = (target) => {
    return __defineProperty(target, "__esModule", {value: true});
  };
  var __export = (target, all3) => {
    __markAsModule(target);
    for (var name in all3)
      __defineProperty(target, name, {get: all3[name], enumerable: true});
  };
  var __exportStar = (target, module) => {
    __markAsModule(target);
    if (typeof module === "object" || typeof module === "function") {
      for (let key in module)
        if (!__hasOwnProperty.call(target, key) && key !== "default")
          __defineProperty(target, key, {get: () => module[key], enumerable: true});
    }
    return target;
  };
  var __toModule = (module) => {
    if (module && module.__esModule)
      return module;
    return __exportStar(__defineProperty({}, "default", {value: module, enumerable: true}), module);
  };

  // node_modules/seedrandom/lib/alea.js
  var require_alea = __commonJS((exports, module) => {
    (function(global2, module2, define2) {
      function Alea(seed) {
        var me = this, mash = Mash();
        me.next = function() {
          var t = 2091639 * me.s0 + me.c * 23283064365386963e-26;
          me.s0 = me.s1;
          me.s1 = me.s2;
          return me.s2 = t - (me.c = t | 0);
        };
        me.c = 1;
        me.s0 = mash(" ");
        me.s1 = mash(" ");
        me.s2 = mash(" ");
        me.s0 -= mash(seed);
        if (me.s0 < 0) {
          me.s0 += 1;
        }
        me.s1 -= mash(seed);
        if (me.s1 < 0) {
          me.s1 += 1;
        }
        me.s2 -= mash(seed);
        if (me.s2 < 0) {
          me.s2 += 1;
        }
        mash = null;
      }
      function copy(f, t) {
        t.c = f.c;
        t.s0 = f.s0;
        t.s1 = f.s1;
        t.s2 = f.s2;
        return t;
      }
      function impl(seed, opts) {
        var xg = new Alea(seed), state = opts && opts.state, prng = xg.next;
        prng.int32 = function() {
          return xg.next() * 4294967296 | 0;
        };
        prng.double = function() {
          return prng() + (prng() * 2097152 | 0) * 11102230246251565e-32;
        };
        prng.quick = prng;
        if (state) {
          if (typeof state == "object")
            copy(state, xg);
          prng.state = function() {
            return copy(xg, {});
          };
        }
        return prng;
      }
      function Mash() {
        var n = 4022871197;
        var mash = function(data) {
          data = data.toString();
          for (var i = 0; i < data.length; i++) {
            n += data.charCodeAt(i);
            var h = 0.02519603282416938 * n;
            n = h >>> 0;
            h -= n;
            h *= n;
            n = h >>> 0;
            h -= n;
            n += h * 4294967296;
          }
          return (n >>> 0) * 23283064365386963e-26;
        };
        return mash;
      }
      if (module2 && module2.exports) {
        module2.exports = impl;
      } else if (define2 && define2.amd) {
        define2(function() {
          return impl;
        });
      } else {
        this.alea = impl;
      }
    })(exports, typeof module == "object" && module, typeof define == "function" && define);
  });

  // node_modules/seedrandom/lib/xor128.js
  var require_xor128 = __commonJS((exports, module) => {
    (function(global2, module2, define2) {
      function XorGen(seed) {
        var me = this, strseed = "";
        me.x = 0;
        me.y = 0;
        me.z = 0;
        me.w = 0;
        me.next = function() {
          var t = me.x ^ me.x << 11;
          me.x = me.y;
          me.y = me.z;
          me.z = me.w;
          return me.w ^= me.w >>> 19 ^ t ^ t >>> 8;
        };
        if (seed === (seed | 0)) {
          me.x = seed;
        } else {
          strseed += seed;
        }
        for (var k = 0; k < strseed.length + 64; k++) {
          me.x ^= strseed.charCodeAt(k) | 0;
          me.next();
        }
      }
      function copy(f, t) {
        t.x = f.x;
        t.y = f.y;
        t.z = f.z;
        t.w = f.w;
        return t;
      }
      function impl(seed, opts) {
        var xg = new XorGen(seed), state = opts && opts.state, prng = function() {
          return (xg.next() >>> 0) / 4294967296;
        };
        prng.double = function() {
          do {
            var top = xg.next() >>> 11, bot = (xg.next() >>> 0) / 4294967296, result = (top + bot) / (1 << 21);
          } while (result === 0);
          return result;
        };
        prng.int32 = xg.next;
        prng.quick = prng;
        if (state) {
          if (typeof state == "object")
            copy(state, xg);
          prng.state = function() {
            return copy(xg, {});
          };
        }
        return prng;
      }
      if (module2 && module2.exports) {
        module2.exports = impl;
      } else if (define2 && define2.amd) {
        define2(function() {
          return impl;
        });
      } else {
        this.xor128 = impl;
      }
    })(exports, typeof module == "object" && module, typeof define == "function" && define);
  });

  // node_modules/seedrandom/lib/xorwow.js
  var require_xorwow = __commonJS((exports, module) => {
    (function(global2, module2, define2) {
      function XorGen(seed) {
        var me = this, strseed = "";
        me.next = function() {
          var t = me.x ^ me.x >>> 2;
          me.x = me.y;
          me.y = me.z;
          me.z = me.w;
          me.w = me.v;
          return (me.d = me.d + 362437 | 0) + (me.v = me.v ^ me.v << 4 ^ (t ^ t << 1)) | 0;
        };
        me.x = 0;
        me.y = 0;
        me.z = 0;
        me.w = 0;
        me.v = 0;
        if (seed === (seed | 0)) {
          me.x = seed;
        } else {
          strseed += seed;
        }
        for (var k = 0; k < strseed.length + 64; k++) {
          me.x ^= strseed.charCodeAt(k) | 0;
          if (k == strseed.length) {
            me.d = me.x << 10 ^ me.x >>> 4;
          }
          me.next();
        }
      }
      function copy(f, t) {
        t.x = f.x;
        t.y = f.y;
        t.z = f.z;
        t.w = f.w;
        t.v = f.v;
        t.d = f.d;
        return t;
      }
      function impl(seed, opts) {
        var xg = new XorGen(seed), state = opts && opts.state, prng = function() {
          return (xg.next() >>> 0) / 4294967296;
        };
        prng.double = function() {
          do {
            var top = xg.next() >>> 11, bot = (xg.next() >>> 0) / 4294967296, result = (top + bot) / (1 << 21);
          } while (result === 0);
          return result;
        };
        prng.int32 = xg.next;
        prng.quick = prng;
        if (state) {
          if (typeof state == "object")
            copy(state, xg);
          prng.state = function() {
            return copy(xg, {});
          };
        }
        return prng;
      }
      if (module2 && module2.exports) {
        module2.exports = impl;
      } else if (define2 && define2.amd) {
        define2(function() {
          return impl;
        });
      } else {
        this.xorwow = impl;
      }
    })(exports, typeof module == "object" && module, typeof define == "function" && define);
  });

  // node_modules/seedrandom/lib/xorshift7.js
  var require_xorshift7 = __commonJS((exports, module) => {
    (function(global2, module2, define2) {
      function XorGen(seed) {
        var me = this;
        me.next = function() {
          var X = me.x, i = me.i, t, v, w;
          t = X[i];
          t ^= t >>> 7;
          v = t ^ t << 24;
          t = X[i + 1 & 7];
          v ^= t ^ t >>> 10;
          t = X[i + 3 & 7];
          v ^= t ^ t >>> 3;
          t = X[i + 4 & 7];
          v ^= t ^ t << 7;
          t = X[i + 7 & 7];
          t = t ^ t << 13;
          v ^= t ^ t << 9;
          X[i] = v;
          me.i = i + 1 & 7;
          return v;
        };
        function init(me2, seed2) {
          var j, w, X = [];
          if (seed2 === (seed2 | 0)) {
            w = X[0] = seed2;
          } else {
            seed2 = "" + seed2;
            for (j = 0; j < seed2.length; ++j) {
              X[j & 7] = X[j & 7] << 15 ^ seed2.charCodeAt(j) + X[j + 1 & 7] << 13;
            }
          }
          while (X.length < 8)
            X.push(0);
          for (j = 0; j < 8 && X[j] === 0; ++j)
            ;
          if (j == 8)
            w = X[7] = -1;
          else
            w = X[j];
          me2.x = X;
          me2.i = 0;
          for (j = 256; j > 0; --j) {
            me2.next();
          }
        }
        init(me, seed);
      }
      function copy(f, t) {
        t.x = f.x.slice();
        t.i = f.i;
        return t;
      }
      function impl(seed, opts) {
        if (seed == null)
          seed = +new Date();
        var xg = new XorGen(seed), state = opts && opts.state, prng = function() {
          return (xg.next() >>> 0) / 4294967296;
        };
        prng.double = function() {
          do {
            var top = xg.next() >>> 11, bot = (xg.next() >>> 0) / 4294967296, result = (top + bot) / (1 << 21);
          } while (result === 0);
          return result;
        };
        prng.int32 = xg.next;
        prng.quick = prng;
        if (state) {
          if (state.x)
            copy(state, xg);
          prng.state = function() {
            return copy(xg, {});
          };
        }
        return prng;
      }
      if (module2 && module2.exports) {
        module2.exports = impl;
      } else if (define2 && define2.amd) {
        define2(function() {
          return impl;
        });
      } else {
        this.xorshift7 = impl;
      }
    })(exports, typeof module == "object" && module, typeof define == "function" && define);
  });

  // node_modules/seedrandom/lib/xor4096.js
  var require_xor4096 = __commonJS((exports, module) => {
    (function(global2, module2, define2) {
      function XorGen(seed) {
        var me = this;
        me.next = function() {
          var w = me.w, X = me.X, i = me.i, t, v;
          me.w = w = w + 1640531527 | 0;
          v = X[i + 34 & 127];
          t = X[i = i + 1 & 127];
          v ^= v << 13;
          t ^= t << 17;
          v ^= v >>> 15;
          t ^= t >>> 12;
          v = X[i] = v ^ t;
          me.i = i;
          return v + (w ^ w >>> 16) | 0;
        };
        function init(me2, seed2) {
          var t, v, i, j, w, X = [], limit = 128;
          if (seed2 === (seed2 | 0)) {
            v = seed2;
            seed2 = null;
          } else {
            seed2 = seed2 + "\0";
            v = 0;
            limit = Math.max(limit, seed2.length);
          }
          for (i = 0, j = -32; j < limit; ++j) {
            if (seed2)
              v ^= seed2.charCodeAt((j + 32) % seed2.length);
            if (j === 0)
              w = v;
            v ^= v << 10;
            v ^= v >>> 15;
            v ^= v << 4;
            v ^= v >>> 13;
            if (j >= 0) {
              w = w + 1640531527 | 0;
              t = X[j & 127] ^= v + w;
              i = t == 0 ? i + 1 : 0;
            }
          }
          if (i >= 128) {
            X[(seed2 && seed2.length || 0) & 127] = -1;
          }
          i = 127;
          for (j = 4 * 128; j > 0; --j) {
            v = X[i + 34 & 127];
            t = X[i = i + 1 & 127];
            v ^= v << 13;
            t ^= t << 17;
            v ^= v >>> 15;
            t ^= t >>> 12;
            X[i] = v ^ t;
          }
          me2.w = w;
          me2.X = X;
          me2.i = i;
        }
        init(me, seed);
      }
      function copy(f, t) {
        t.i = f.i;
        t.w = f.w;
        t.X = f.X.slice();
        return t;
      }
      ;
      function impl(seed, opts) {
        if (seed == null)
          seed = +new Date();
        var xg = new XorGen(seed), state = opts && opts.state, prng = function() {
          return (xg.next() >>> 0) / 4294967296;
        };
        prng.double = function() {
          do {
            var top = xg.next() >>> 11, bot = (xg.next() >>> 0) / 4294967296, result = (top + bot) / (1 << 21);
          } while (result === 0);
          return result;
        };
        prng.int32 = xg.next;
        prng.quick = prng;
        if (state) {
          if (state.X)
            copy(state, xg);
          prng.state = function() {
            return copy(xg, {});
          };
        }
        return prng;
      }
      if (module2 && module2.exports) {
        module2.exports = impl;
      } else if (define2 && define2.amd) {
        define2(function() {
          return impl;
        });
      } else {
        this.xor4096 = impl;
      }
    })(exports, typeof module == "object" && module, typeof define == "function" && define);
  });

  // node_modules/seedrandom/lib/tychei.js
  var require_tychei = __commonJS((exports, module) => {
    (function(global2, module2, define2) {
      function XorGen(seed) {
        var me = this, strseed = "";
        me.next = function() {
          var b = me.b, c = me.c, d = me.d, a = me.a;
          b = b << 25 ^ b >>> 7 ^ c;
          c = c - d | 0;
          d = d << 24 ^ d >>> 8 ^ a;
          a = a - b | 0;
          me.b = b = b << 20 ^ b >>> 12 ^ c;
          me.c = c = c - d | 0;
          me.d = d << 16 ^ c >>> 16 ^ a;
          return me.a = a - b | 0;
        };
        me.a = 0;
        me.b = 0;
        me.c = 2654435769 | 0;
        me.d = 1367130551;
        if (seed === Math.floor(seed)) {
          me.a = seed / 4294967296 | 0;
          me.b = seed | 0;
        } else {
          strseed += seed;
        }
        for (var k = 0; k < strseed.length + 20; k++) {
          me.b ^= strseed.charCodeAt(k) | 0;
          me.next();
        }
      }
      function copy(f, t) {
        t.a = f.a;
        t.b = f.b;
        t.c = f.c;
        t.d = f.d;
        return t;
      }
      ;
      function impl(seed, opts) {
        var xg = new XorGen(seed), state = opts && opts.state, prng = function() {
          return (xg.next() >>> 0) / 4294967296;
        };
        prng.double = function() {
          do {
            var top = xg.next() >>> 11, bot = (xg.next() >>> 0) / 4294967296, result = (top + bot) / (1 << 21);
          } while (result === 0);
          return result;
        };
        prng.int32 = xg.next;
        prng.quick = prng;
        if (state) {
          if (typeof state == "object")
            copy(state, xg);
          prng.state = function() {
            return copy(xg, {});
          };
        }
        return prng;
      }
      if (module2 && module2.exports) {
        module2.exports = impl;
      } else if (define2 && define2.amd) {
        define2(function() {
          return impl;
        });
      } else {
        this.tychei = impl;
      }
    })(exports, typeof module == "object" && module, typeof define == "function" && define);
  });

  // disabled:crypto
  var require_disabled_crypto = __commonJS(() => {
  });

  // node_modules/seedrandom/seedrandom.js
  var require_seedrandom = __commonJS((exports, module) => {
    (function(pool3, math) {
      var global2 = this, width = 256, chunks = 6, digits = 52, rngname = "random", startdenom = math.pow(width, chunks), significance = math.pow(2, digits), overflow = significance * 2, mask = width - 1, nodecrypto;
      function seedrandom2(seed, options, callback) {
        var key = [];
        options = options == true ? {entropy: true} : options || {};
        var shortseed = mixkey(flatten2(options.entropy ? [seed, tostring(pool3)] : seed == null ? autoseed() : seed, 3), key);
        var arc4 = new ARC4(key);
        var prng = function() {
          var n = arc4.g(chunks), d = startdenom, x = 0;
          while (n < significance) {
            n = (n + x) * width;
            d *= width;
            x = arc4.g(1);
          }
          while (n >= overflow) {
            n /= 2;
            d /= 2;
            x >>>= 1;
          }
          return (n + x) / d;
        };
        prng.int32 = function() {
          return arc4.g(4) | 0;
        };
        prng.quick = function() {
          return arc4.g(4) / 4294967296;
        };
        prng.double = prng;
        mixkey(tostring(arc4.S), pool3);
        return (options.pass || callback || function(prng2, seed2, is_math_call, state) {
          if (state) {
            if (state.S) {
              copy(state, arc4);
            }
            prng2.state = function() {
              return copy(arc4, {});
            };
          }
          if (is_math_call) {
            math[rngname] = prng2;
            return seed2;
          } else
            return prng2;
        })(prng, shortseed, "global" in options ? options.global : this == math, options.state);
      }
      math["seed" + rngname] = seedrandom2;
      function ARC4(key) {
        var t, keylen = key.length, me = this, i = 0, j = me.i = me.j = 0, s = me.S = [];
        if (!keylen) {
          key = [keylen++];
        }
        while (i < width) {
          s[i] = i++;
        }
        for (i = 0; i < width; i++) {
          s[i] = s[j = mask & j + key[i % keylen] + (t = s[i])];
          s[j] = t;
        }
        (me.g = function(count) {
          var t2, r = 0, i2 = me.i, j2 = me.j, s2 = me.S;
          while (count--) {
            t2 = s2[i2 = mask & i2 + 1];
            r = r * width + s2[mask & (s2[i2] = s2[j2 = mask & j2 + t2]) + (s2[j2] = t2)];
          }
          me.i = i2;
          me.j = j2;
          return r;
        })(width);
      }
      function copy(f, t) {
        t.i = f.i;
        t.j = f.j;
        t.S = f.S.slice();
        return t;
      }
      ;
      function flatten2(obj, depth) {
        var result = [], typ = typeof obj, prop;
        if (depth && typ == "object") {
          for (prop in obj) {
            try {
              result.push(flatten2(obj[prop], depth - 1));
            } catch (e) {
            }
          }
        }
        return result.length ? result : typ == "string" ? obj : obj + "\0";
      }
      function mixkey(seed, key) {
        var stringseed = seed + "", smear, j = 0;
        while (j < stringseed.length) {
          key[mask & j] = mask & (smear ^= key[mask & j] * 19) + stringseed.charCodeAt(j++);
        }
        return tostring(key);
      }
      function autoseed() {
        try {
          var out;
          if (nodecrypto && (out = nodecrypto.randomBytes)) {
            out = out(width);
          } else {
            out = new Uint8Array(width);
            (global2.crypto || global2.msCrypto).getRandomValues(out);
          }
          return tostring(out);
        } catch (e) {
          var browser = global2.navigator, plugins = browser && browser.plugins;
          return [+new Date(), global2, plugins, global2.screen, tostring(pool3)];
        }
      }
      function tostring(a) {
        return String.fromCharCode.apply(0, a);
      }
      mixkey(math.random(), pool3);
      if (typeof module == "object" && module.exports) {
        module.exports = seedrandom2;
        try {
          nodecrypto = require_disabled_crypto();
        } catch (ex) {
        }
      } else if (typeof define == "function" && define.amd) {
        define(function() {
          return seedrandom2;
        });
      }
    })([], Math);
  });

  // node_modules/seedrandom/index.js
  var require_seedrandom2 = __commonJS((exports, module) => {
    var alea2 = require_alea();
    var xor128 = require_xor128();
    var xorwow = require_xorwow();
    var xorshift7 = require_xorshift7();
    var xor4096 = require_xor4096();
    var tychei = require_tychei();
    var sr = require_seedrandom();
    sr.alea = alea2;
    sr.xor128 = xor128;
    sr.xorwow = xorwow;
    sr.xorshift7 = xorshift7;
    sr.xor4096 = xor4096;
    sr.tychei = tychei;
    module.exports = sr;
  });

  // build/env/isNodejs.js
  var require_isNodejs = __commonJS((exports, module) => {
    __export(exports, {
      isNodejs: () => isNodejs3
    });
    function isNodejs3() {
      return typeof global === "object" && true && typeof module !== "undefined" && typeof process !== "undefined" && !!process.version;
    }
  });

  // build/index.js
  var require_build = __commonJS((exports) => {
    __export(exports, {
      AgeGenderNet: () => AgeGenderNet,
      BoundingBox: () => BoundingBox,
      Box: () => Box,
      ComposableTask: () => ComposableTask,
      ComputeAllFaceDescriptorsTask: () => ComputeAllFaceDescriptorsTask,
      ComputeFaceDescriptorsTaskBase: () => ComputeFaceDescriptorsTaskBase,
      ComputeSingleFaceDescriptorTask: () => ComputeSingleFaceDescriptorTask,
      DetectAllFaceLandmarksTask: () => DetectAllFaceLandmarksTask,
      DetectAllFacesTask: () => DetectAllFacesTask,
      DetectFaceLandmarksTaskBase: () => DetectFaceLandmarksTaskBase,
      DetectFacesTaskBase: () => DetectFacesTaskBase,
      DetectSingleFaceLandmarksTask: () => DetectSingleFaceLandmarksTask,
      DetectSingleFaceTask: () => DetectSingleFaceTask,
      Dimensions: () => Dimensions,
      FACE_EXPRESSION_LABELS: () => FACE_EXPRESSION_LABELS,
      FaceDetection: () => FaceDetection,
      FaceDetectionNet: () => FaceDetectionNet,
      FaceExpressionNet: () => FaceExpressionNet,
      FaceExpressions: () => FaceExpressions,
      FaceLandmark68Net: () => FaceLandmark68Net,
      FaceLandmark68TinyNet: () => FaceLandmark68TinyNet,
      FaceLandmarkNet: () => FaceLandmarkNet,
      FaceLandmarks: () => FaceLandmarks,
      FaceLandmarks5: () => FaceLandmarks5,
      FaceLandmarks68: () => FaceLandmarks68,
      FaceMatch: () => FaceMatch,
      FaceMatcher: () => FaceMatcher,
      FaceRecognitionNet: () => FaceRecognitionNet,
      Gender: () => Gender,
      LabeledBox: () => LabeledBox,
      LabeledFaceDescriptors: () => LabeledFaceDescriptors,
      NetInput: () => NetInput,
      NeuralNetwork: () => NeuralNetwork,
      ObjectDetection: () => ObjectDetection,
      Point: () => Point,
      PredictedBox: () => PredictedBox,
      Rect: () => Rect,
      SsdMobilenetv1: () => SsdMobilenetv1,
      SsdMobilenetv1Options: () => SsdMobilenetv1Options,
      TinyFaceDetector: () => TinyFaceDetector,
      TinyFaceDetectorOptions: () => TinyFaceDetectorOptions,
      TinyYolov2: () => TinyYolov2,
      TinyYolov2Options: () => TinyYolov2Options,
      TinyYolov2SizeType: () => TinyYolov2SizeType,
      allFaces: () => allFaces,
      allFacesSsdMobilenetv1: () => allFacesSsdMobilenetv1,
      allFacesTinyYolov2: () => allFacesTinyYolov2,
      awaitMediaLoaded: () => awaitMediaLoaded,
      bufferToImage: () => bufferToImage,
      computeFaceDescriptor: () => computeFaceDescriptor,
      createCanvas: () => createCanvas,
      createCanvasFromMedia: () => createCanvasFromMedia,
      createFaceDetectionNet: () => createFaceDetectionNet,
      createFaceRecognitionNet: () => createFaceRecognitionNet,
      createSsdMobilenetv1: () => createSsdMobilenetv1,
      createTinyFaceDetector: () => createTinyFaceDetector,
      createTinyYolov2: () => createTinyYolov2,
      detectAllFaces: () => detectAllFaces,
      detectFaceLandmarks: () => detectFaceLandmarks,
      detectFaceLandmarksTiny: () => detectFaceLandmarksTiny,
      detectLandmarks: () => detectLandmarks,
      detectSingleFace: () => detectSingleFace,
      draw: () => draw_exports,
      env: () => env2,
      euclideanDistance: () => euclideanDistance,
      extendWithAge: () => extendWithAge,
      extendWithFaceDescriptor: () => extendWithFaceDescriptor,
      extendWithFaceDetection: () => extendWithFaceDetection,
      extendWithFaceExpressions: () => extendWithFaceExpressions,
      extendWithFaceLandmarks: () => extendWithFaceLandmarks,
      extendWithGender: () => extendWithGender,
      extractFaceTensors: () => extractFaceTensors,
      extractFaces: () => extractFaces,
      fetchImage: () => fetchImage,
      fetchJson: () => fetchJson,
      fetchNetWeights: () => fetchNetWeights,
      fetchOrThrow: () => fetchOrThrow,
      getContext2dOrThrow: () => getContext2dOrThrow,
      getMediaDimensions: () => getMediaDimensions,
      imageTensorToCanvas: () => imageTensorToCanvas,
      imageToSquare: () => imageToSquare,
      inverseSigmoid: () => inverseSigmoid,
      iou: () => iou,
      isMediaElement: () => isMediaElement,
      isMediaLoaded: () => isMediaLoaded,
      isWithAge: () => isWithAge,
      isWithFaceDetection: () => isWithFaceDetection,
      isWithFaceExpressions: () => isWithFaceExpressions,
      isWithFaceLandmarks: () => isWithFaceLandmarks,
      isWithGender: () => isWithGender,
      loadAgeGenderModel: () => loadAgeGenderModel,
      loadFaceDetectionModel: () => loadFaceDetectionModel,
      loadFaceExpressionModel: () => loadFaceExpressionModel,
      loadFaceLandmarkModel: () => loadFaceLandmarkModel,
      loadFaceLandmarkTinyModel: () => loadFaceLandmarkTinyModel,
      loadFaceRecognitionModel: () => loadFaceRecognitionModel,
      loadSsdMobilenetv1Model: () => loadSsdMobilenetv1Model,
      loadTinyFaceDetectorModel: () => loadTinyFaceDetectorModel,
      loadTinyYolov2Model: () => loadTinyYolov2Model,
      loadWeightMap: () => loadWeightMap,
      locateFaces: () => locateFaces,
      matchDimensions: () => matchDimensions,
      minBbox: () => minBbox,
      nets: () => nets,
      nonMaxSuppression: () => nonMaxSuppression2,
      normalize: () => normalize,
      padToSquare: () => padToSquare,
      predictAgeAndGender: () => predictAgeAndGender,
      recognizeFaceExpressions: () => recognizeFaceExpressions,
      resizeResults: () => resizeResults,
      resolveInput: () => resolveInput,
      shuffleArray: () => shuffleArray,
      sigmoid: () => sigmoid6,
      ssdMobilenetv1: () => ssdMobilenetv1,
      test: () => test,
      tf: () => dist_exports,
      tinyFaceDetector: () => tinyFaceDetector,
      tinyYolov2: () => tinyYolov23,
      toNetInput: () => toNetInput,
      utils: () => utils_exports,
      validateConfig: () => validateConfig
    });
    const test = "test";
  });

  // node_modules/@tensorflow/tfjs-core/dist/environment.js
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const TENSORFLOWJS_FLAGS_PREFIX = "tfjsflags";
  class Environment {
    constructor(global2) {
      this.global = global2;
      this.flags = {};
      this.flagRegistry = {};
      this.urlFlags = {};
      this.populateURLFlags();
    }
    setPlatform(platformName, platform) {
      if (this.platform != null) {
        console.warn(`Platform ${this.platformName} has already been set. Overwriting the platform with ${platform}.`);
      }
      this.platformName = platformName;
      this.platform = platform;
    }
    registerFlag(flagName, evaluationFn, setHook) {
      this.flagRegistry[flagName] = {evaluationFn, setHook};
      if (this.urlFlags[flagName] != null) {
        const flagValue = this.urlFlags[flagName];
        console.warn(`Setting feature override from URL ${flagName}: ${flagValue}.`);
        this.set(flagName, flagValue);
      }
    }
    async getAsync(flagName) {
      if (flagName in this.flags) {
        return this.flags[flagName];
      }
      this.flags[flagName] = await this.evaluateFlag(flagName);
      return this.flags[flagName];
    }
    get(flagName) {
      if (flagName in this.flags) {
        return this.flags[flagName];
      }
      const flagValue = this.evaluateFlag(flagName);
      if (flagValue instanceof Promise) {
        throw new Error(`Flag ${flagName} cannot be synchronously evaluated. Please use getAsync() instead.`);
      }
      this.flags[flagName] = flagValue;
      return this.flags[flagName];
    }
    getNumber(flagName) {
      return this.get(flagName);
    }
    getBool(flagName) {
      return this.get(flagName);
    }
    getFlags() {
      return this.flags;
    }
    get features() {
      return this.flags;
    }
    set(flagName, value) {
      if (this.flagRegistry[flagName] == null) {
        throw new Error(`Cannot set flag ${flagName} as it has not been registered.`);
      }
      this.flags[flagName] = value;
      if (this.flagRegistry[flagName].setHook != null) {
        this.flagRegistry[flagName].setHook(value);
      }
    }
    evaluateFlag(flagName) {
      if (this.flagRegistry[flagName] == null) {
        throw new Error(`Cannot evaluate flag '${flagName}': no evaluation function found.`);
      }
      return this.flagRegistry[flagName].evaluationFn();
    }
    setFlags(flags2) {
      this.flags = Object.assign({}, flags2);
    }
    reset() {
      this.flags = {};
      this.urlFlags = {};
      this.populateURLFlags();
    }
    populateURLFlags() {
      if (typeof this.global === "undefined" || typeof this.global.location === "undefined" || typeof this.global.location.search === "undefined") {
        return;
      }
      const urlParams = getQueryParams(this.global.location.search);
      if (TENSORFLOWJS_FLAGS_PREFIX in urlParams) {
        const keyValues = urlParams[TENSORFLOWJS_FLAGS_PREFIX].split(",");
        keyValues.forEach((keyValue) => {
          const [key, value] = keyValue.split(":");
          this.urlFlags[key] = parseValue(key, value);
        });
      }
    }
  }
  function getQueryParams(queryString) {
    const params = {};
    queryString.replace(/[?&]([^=?&]+)(?:=([^&]*))?/g, (s, ...t) => {
      decodeParam(params, t[0], t[1]);
      return t.join("=");
    });
    return params;
  }
  function decodeParam(params, name, value) {
    params[decodeURIComponent(name)] = decodeURIComponent(value || "");
  }
  function parseValue(flagName, value) {
    value = value.toLowerCase();
    if (value === "true" || value === "false") {
      return value === "true";
    } else if (`${+value}` === value) {
      return +value;
    }
    throw new Error(`Could not parse value flag value ${value} for flag ${flagName}.`);
  }
  function env() {
    return ENV;
  }
  let ENV = null;
  function setEnvironmentGlobal(environment12) {
    ENV = environment12;
  }

  // node_modules/@tensorflow/tfjs-core/dist/global_util.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  let globalNameSpace;
  function getGlobalNamespace() {
    if (globalNameSpace == null) {
      let ns;
      if (typeof window !== "undefined") {
        ns = window;
      } else if (typeof global !== "undefined") {
        ns = global;
      } else if (typeof process !== "undefined") {
        ns = process;
      } else if (typeof self !== "undefined") {
        ns = self;
      } else {
        throw new Error("Could not find a global object");
      }
      globalNameSpace = ns;
    }
    return globalNameSpace;
  }
  function getGlobalMap() {
    const ns = getGlobalNamespace();
    if (ns._tfGlobals == null) {
      ns._tfGlobals = new Map();
    }
    return ns._tfGlobals;
  }
  function getGlobal(key, init) {
    const globalMap = getGlobalMap();
    if (globalMap.has(key)) {
      return globalMap.get(key);
    } else {
      const singleton = init();
      globalMap.set(key, singleton);
      return globalMap.get(key);
    }
  }

  // node_modules/@tensorflow/tfjs-core/dist/kernel_names.js
  const Abs = "Abs";
  const Acos = "Acos";
  const Acosh = "Acosh";
  const Add = "Add";
  const AddN = "AddN";
  const All = "All";
  const Any = "Any";
  const ArgMax = "ArgMax";
  const ArgMin = "ArgMin";
  const Asin = "Asin";
  const Asinh = "Asinh";
  const Atan = "Atan";
  const Atanh = "Atanh";
  const Atan2 = "Atan2";
  const AvgPool = "AvgPool";
  const AvgPoolBackprop = "AvgPoolBackprop";
  const AvgPool3D = "AvgPool3D";
  const AvgPool3DBackprop = "AvgPool3DBackprop";
  const BatchMatMul = "BatchMatMul";
  const BatchToSpaceND = "BatchToSpaceND";
  const BroadcastTo = "BroadcastTo";
  const Cast = "Cast";
  const Ceil = "Ceil";
  const ClipByValue = "ClipByValue";
  const Complex = "Complex";
  const Concat = "Concat";
  const Conv2D = "Conv2D";
  const Conv2DBackpropFilter = "Conv2DBackpropFilter";
  const Conv2DBackpropInput = "Conv2DBackpropInput";
  const Conv3D = "Conv3D";
  const Conv3DBackpropFilterV2 = "Conv3DBackpropFilterV2";
  const Conv3DBackpropInputV2 = "Conv3DBackpropInputV2";
  const Cos = "Cos";
  const Cosh = "Cosh";
  const Cumsum = "Cumsum";
  const CropAndResize = "CropAndResize";
  const DepthToSpace = "DepthToSpace";
  const DepthwiseConv2dNative = "DepthwiseConv2dNative";
  const DepthwiseConv2dNativeBackpropFilter = "DepthwiseConv2dNativeBackpropFilter";
  const DepthwiseConv2dNativeBackpropInput = "DepthwiseConv2dNativeBackpropInput";
  const Diag = "Diag";
  const Dilation2D = "Dilation2D";
  const Dilation2DBackpropInput = "Dilation2DBackpropInput";
  const Dilation2DBackpropFilter = "Dilation2DBackpropFilter";
  const Div = "Div";
  const Elu = "Elu";
  const EluGrad = "EluGrad";
  const Erf = "Erf";
  const Equal = "Equal";
  const Exp = "Exp";
  const Expm1 = "Expm1";
  const FFT = "FFT";
  const Fill = "Fill";
  const FlipLeftRight = "FlipLeftRight";
  const Floor = "Floor";
  const FloorDiv = "FloorDiv";
  const FusedBatchNorm = "FusedBatchNorm";
  const GatherV2 = "GatherV2";
  const GatherNd = "GatherNd";
  const Greater = "Greater";
  const GreaterEqual = "GreaterEqual";
  const Identity = "Identity";
  const IFFT = "IFFT";
  const Imag = "Imag";
  const IsFinite = "IsFinite";
  const IsInf = "IsInf";
  const IsNan = "IsNan";
  const Less = "Less";
  const LessEqual = "LessEqual";
  const LinSpace = "LinSpace";
  const Log = "Log";
  const Log1p = "Log1p";
  const LogicalAnd = "LogicalAnd";
  const LogicalNot = "LogicalNot";
  const LogicalOr = "LogicalOr";
  const LogSoftmax = "LogSoftmax";
  const LRN = "LRN";
  const LRNBackprop = "LRNBackprop";
  const Max = "Max";
  const Maximum = "Maximum";
  const MaxPool = "MaxPool";
  const MaxPoolBackprop = "MaxPoolBackprop";
  const MaxPool3D = "MaxPool3D";
  const MaxPool3DBackprop = "MaxPool3DBackprop";
  const MaxPoolWithArgmax = "MaxPoolWithArgmax";
  const Mean = "Mean";
  const Min = "Min";
  const Minimum = "Minimum";
  const Mod = "Mod";
  const Multiply = "Multiply";
  const Negate = "Negate";
  const NotEqual = "NotEqual";
  const NonMaxSuppressionV3 = "NonMaxSuppressionV3";
  const NonMaxSuppressionV4 = "NonMaxSuppressionV4";
  const NonMaxSuppressionV5 = "NonMaxSuppressionV5";
  const OnesLike = "OnesLike";
  const OneHot = "OneHot";
  const PadV2 = "PadV2";
  const Pool = "Pool";
  const Pow = "Pow";
  const Prelu = "Prelu";
  const Prod = "Prod";
  const Range = "Range";
  const Real = "Real";
  const Reciprocal = "Reciprocal";
  const Relu = "Relu";
  const Reshape = "Reshape";
  const ResizeNearestNeighbor = "ResizeNearestNeighbor";
  const ResizeNearestNeighborGrad = "ResizeNearestNeighborGrad";
  const ResizeBilinear = "ResizeBilinear";
  const ResizeBilinearGrad = "ResizeBilinearGrad";
  const Relu6 = "Relu6";
  const Reverse = "Reverse";
  const Round = "Round";
  const Rsqrt = "Rsqrt";
  const ScatterNd = "ScatterNd";
  const SelectV2 = "SelectV2";
  const Selu = "Selu";
  const Slice = "Slice";
  const Sin = "Sin";
  const Sinh = "Sinh";
  const Sign = "Sign";
  const Sigmoid = "Sigmoid";
  const Softplus = "Softplus";
  const Sqrt = "Sqrt";
  const Sum = "Sum";
  const SpaceToBatchND = "SpaceToBatchND";
  const SplitV = "SplitV";
  const Softmax = "Softmax";
  const SquaredDifference = "SquaredDifference";
  const Square = "Square";
  const Sub = "Sub";
  const SparseToDense = "SparseToDense";
  const StridedSlice = "StridedSlice";
  const Tan = "Tan";
  const Tanh = "Tanh";
  const Tile = "Tile";
  const TopK = "TopK";
  const Transpose = "Transpose";
  const Unpack = "Unpack";
  const UnsortedSegmentSum = "UnsortedSegmentSum";
  const ZerosLike = "ZerosLike";
  const Step = "Step";
  const FromPixels = "FromPixels";
  const RotateWithOffset = "RotateWithOffset";
  const _FusedMatMul = "_FusedMatMul";
  const FusedConv2D = "FusedConv2D";
  const FusedDepthwiseConv2D = "FusedDepthwiseConv2D";

  // node_modules/@tensorflow/tfjs-core/dist/kernel_registry.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const kernelRegistry = getGlobal("kernelRegistry", () => new Map());
  const gradRegistry = getGlobal("gradRegistry", () => new Map());
  function getKernel(kernelName, backendName) {
    const key = makeKey(kernelName, backendName);
    return kernelRegistry.get(key);
  }
  function getGradient(kernelName) {
    return gradRegistry.get(kernelName);
  }
  function getKernelsForBackend(backendName) {
    const it = kernelRegistry.entries();
    const result = [];
    while (true) {
      const {done, value} = it.next();
      if (done) {
        break;
      }
      const [key, config2] = value;
      const [backend2] = key.split("_");
      if (backend2 === backendName) {
        result.push(config2);
      }
    }
    return result;
  }
  function registerKernel(config2) {
    const {kernelName, backendName} = config2;
    const key = makeKey(kernelName, backendName);
    if (kernelRegistry.has(key)) {
      console.warn(`The kernel '${kernelName}' for backend '${backendName}' is already registered`);
    }
    kernelRegistry.set(key, config2);
  }
  function registerGradient(config2) {
    const {kernelName} = config2;
    if (gradRegistry.has(kernelName)) {
      if (env().getBool("DEBUG")) {
        console.warn(`Overriding the gradient for '${kernelName}'`);
      }
    }
    gradRegistry.set(kernelName, config2);
  }
  function unregisterKernel(kernelName, backendName) {
    const key = makeKey(kernelName, backendName);
    if (!kernelRegistry.has(key)) {
      throw new Error(`The kernel '${kernelName}' for backend '${backendName}' is not registered`);
    }
    kernelRegistry.delete(key);
  }
  function unregisterGradient(kernelName) {
    if (!gradRegistry.has(kernelName)) {
      throw new Error(`The gradient '${kernelName}' for backend is not registered`);
    }
    gradRegistry.delete(kernelName);
  }
  function makeKey(kernelName, backendName) {
    return `${backendName}_${kernelName}`;
  }

  // node_modules/@tensorflow/tfjs-core/dist/util.js
  const util_exports = {};
  __export(util_exports, {
    arraysEqual: () => arraysEqual,
    assert: () => assert,
    assertNonNegativeIntegerDimensions: () => assertNonNegativeIntegerDimensions,
    assertNonNull: () => assertNonNull,
    assertShapesMatch: () => assertShapesMatch,
    bytesFromStringArray: () => bytesFromStringArray,
    bytesPerElement: () => bytesPerElement,
    checkConversionForErrors: () => checkConversionForErrors,
    clamp: () => clamp,
    computeStrides: () => computeStrides,
    createShuffledIndices: () => createShuffledIndices,
    decodeString: () => decodeString,
    distSquared: () => distSquared,
    encodeString: () => encodeString,
    fetch: () => fetch2,
    flatten: () => flatten,
    getArrayFromDType: () => getArrayFromDType,
    getTypedArrayFromDType: () => getTypedArrayFromDType,
    hasEncodingLoss: () => hasEncodingLoss,
    indexToLoc: () => indexToLoc,
    inferDtype: () => inferDtype,
    inferFromImplicitShape: () => inferFromImplicitShape,
    isBoolean: () => isBoolean,
    isFunction: () => isFunction,
    isInt: () => isInt,
    isNumber: () => isNumber,
    isScalarShape: () => isScalarShape,
    isString: () => isString,
    isTypedArray: () => isTypedArray,
    isValidDtype: () => isValidDtype,
    locToIndex: () => locToIndex,
    makeOnesTypedArray: () => makeOnesTypedArray,
    makeZerosNestedTypedArray: () => makeZerosNestedTypedArray,
    makeZerosTypedArray: () => makeZerosTypedArray,
    nearestDivisor: () => nearestDivisor,
    nearestLargerEven: () => nearestLargerEven,
    now: () => now,
    parseAxisParam: () => parseAxisParam,
    randUniform: () => randUniform,
    repeatedTry: () => repeatedTry,
    rightPad: () => rightPad,
    shuffle: () => shuffle,
    sizeFromShape: () => sizeFromShape,
    sizeToSquarishShape: () => sizeToSquarishShape,
    squeezeShape: () => squeezeShape,
    sum: () => sum,
    tanh: () => tanh,
    toNestedArray: () => toNestedArray,
    toTypedArray: () => toTypedArray
  });
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function shuffle(array) {
    let counter = array.length;
    let temp = 0;
    let index = 0;
    while (counter > 0) {
      index = Math.random() * counter | 0;
      counter--;
      temp = array[counter];
      array[counter] = array[index];
      array[index] = temp;
    }
  }
  function clamp(min5, x, max7) {
    return Math.max(min5, Math.min(x, max7));
  }
  function nearestLargerEven(val) {
    return val % 2 === 0 ? val : val + 1;
  }
  function sum(arr) {
    let sum26 = 0;
    for (let i = 0; i < arr.length; i++) {
      sum26 += arr[i];
    }
    return sum26;
  }
  function randUniform(a, b) {
    const r = Math.random();
    return b * r + (1 - r) * a;
  }
  function distSquared(a, b) {
    let result = 0;
    for (let i = 0; i < a.length; i++) {
      const diff = Number(a[i]) - Number(b[i]);
      result += diff * diff;
    }
    return result;
  }
  function assert(expr, msg) {
    if (!expr) {
      throw new Error(typeof msg === "string" ? msg : msg());
    }
  }
  function assertShapesMatch(shapeA, shapeB, errorMessagePrefix = "") {
    assert(arraysEqual(shapeA, shapeB), () => errorMessagePrefix + ` Shapes ${shapeA} and ${shapeB} must match`);
  }
  function assertNonNull(a) {
    assert(a != null, () => `The input to the tensor constructor must be a non-null value.`);
  }
  function flatten(arr, result = [], skipTypedArray = false) {
    if (result == null) {
      result = [];
    }
    if (Array.isArray(arr) || isTypedArray(arr) && !skipTypedArray) {
      for (let i = 0; i < arr.length; ++i) {
        flatten(arr[i], result, skipTypedArray);
      }
    } else {
      result.push(arr);
    }
    return result;
  }
  function sizeFromShape(shape) {
    if (shape.length === 0) {
      return 1;
    }
    let size = shape[0];
    for (let i = 1; i < shape.length; i++) {
      size *= shape[i];
    }
    return size;
  }
  function isScalarShape(shape) {
    return shape.length === 0;
  }
  function arraysEqual(n1, n2) {
    if (n1 === n2) {
      return true;
    }
    if (n1 == null || n2 == null) {
      return false;
    }
    if (n1.length !== n2.length) {
      return false;
    }
    for (let i = 0; i < n1.length; i++) {
      if (n1[i] !== n2[i]) {
        return false;
      }
    }
    return true;
  }
  function isInt(a) {
    return a % 1 === 0;
  }
  function tanh(x) {
    if (Math.tanh != null) {
      return Math.tanh(x);
    }
    if (x === Infinity) {
      return 1;
    } else if (x === -Infinity) {
      return -1;
    } else {
      const e2x = Math.exp(2 * x);
      return (e2x - 1) / (e2x + 1);
    }
  }
  function sizeToSquarishShape(size) {
    const width = Math.ceil(Math.sqrt(size));
    return [width, Math.ceil(size / width)];
  }
  function createShuffledIndices(n) {
    const shuffledIndices = new Uint32Array(n);
    for (let i = 0; i < n; ++i) {
      shuffledIndices[i] = i;
    }
    shuffle(shuffledIndices);
    return shuffledIndices;
  }
  function rightPad(a, size) {
    if (size <= a.length) {
      return a;
    }
    return a + " ".repeat(size - a.length);
  }
  function repeatedTry(checkFn, delayFn = (counter) => 0, maxCounter) {
    return new Promise((resolve, reject) => {
      let tryCount = 0;
      const tryFn = () => {
        if (checkFn()) {
          resolve();
          return;
        }
        tryCount++;
        const nextBackoff = delayFn(tryCount);
        if (maxCounter != null && tryCount >= maxCounter) {
          reject();
          return;
        }
        setTimeout(tryFn, nextBackoff);
      };
      tryFn();
    });
  }
  function inferFromImplicitShape(shape, size) {
    let shapeProd = 1;
    let implicitIdx = -1;
    for (let i = 0; i < shape.length; ++i) {
      if (shape[i] >= 0) {
        shapeProd *= shape[i];
      } else if (shape[i] === -1) {
        if (implicitIdx !== -1) {
          throw Error(`Shapes can only have 1 implicit size. Found -1 at dim ${implicitIdx} and dim ${i}`);
        }
        implicitIdx = i;
      } else if (shape[i] < 0) {
        throw Error(`Shapes can not be < 0. Found ${shape[i]} at dim ${i}`);
      }
    }
    if (implicitIdx === -1) {
      if (size > 0 && size !== shapeProd) {
        throw Error(`Size(${size}) must match the product of shape ${shape}`);
      }
      return shape;
    }
    if (shapeProd === 0) {
      throw Error(`Cannot infer the missing size in [${shape}] when there are 0 elements`);
    }
    if (size % shapeProd !== 0) {
      throw Error(`The implicit shape can't be a fractional number. Got ${size} / ${shapeProd}`);
    }
    const newShape = shape.slice();
    newShape[implicitIdx] = size / shapeProd;
    return newShape;
  }
  function parseAxisParam(axis, shape) {
    const rank = shape.length;
    axis = axis == null ? shape.map((s, i) => i) : [].concat(axis);
    assert(axis.every((ax) => ax >= -rank && ax < rank), () => `All values in axis param must be in range [-${rank}, ${rank}) but got axis ${axis}`);
    assert(axis.every((ax) => isInt(ax)), () => `All values in axis param must be integers but got axis ${axis}`);
    return axis.map((a) => a < 0 ? rank + a : a);
  }
  function squeezeShape(shape, axis) {
    const newShape = [];
    const keptDims = [];
    const isEmptyArray = axis != null && Array.isArray(axis) && axis.length === 0;
    const axes = axis == null || isEmptyArray ? null : parseAxisParam(axis, shape).sort();
    let j = 0;
    for (let i = 0; i < shape.length; ++i) {
      if (axes != null) {
        if (axes[j] === i && shape[i] !== 1) {
          throw new Error(`Can't squeeze axis ${i} since its dim '${shape[i]}' is not 1`);
        }
        if ((axes[j] == null || axes[j] > i) && shape[i] === 1) {
          newShape.push(shape[i]);
          keptDims.push(i);
        }
        if (axes[j] <= i) {
          j++;
        }
      }
      if (shape[i] !== 1) {
        newShape.push(shape[i]);
        keptDims.push(i);
      }
    }
    return {newShape, keptDims};
  }
  function getTypedArrayFromDType(dtype, size) {
    let values = null;
    if (dtype == null || dtype === "float32") {
      values = new Float32Array(size);
    } else if (dtype === "int32") {
      values = new Int32Array(size);
    } else if (dtype === "bool") {
      values = new Uint8Array(size);
    } else {
      throw new Error(`Unknown data type ${dtype}`);
    }
    return values;
  }
  function getArrayFromDType(dtype, size) {
    let values = null;
    if (dtype == null || dtype === "float32") {
      values = new Float32Array(size);
    } else if (dtype === "int32") {
      values = new Int32Array(size);
    } else if (dtype === "bool") {
      values = new Uint8Array(size);
    } else if (dtype === "string") {
      values = new Array(size);
    } else {
      throw new Error(`Unknown data type ${dtype}`);
    }
    return values;
  }
  function checkConversionForErrors(vals, dtype) {
    for (let i = 0; i < vals.length; i++) {
      const num = vals[i];
      if (isNaN(num) || !isFinite(num)) {
        throw Error(`A tensor of type ${dtype} being uploaded contains ${num}.`);
      }
    }
  }
  function isValidDtype(dtype) {
    return dtype === "bool" || dtype === "complex64" || dtype === "float32" || dtype === "int32" || dtype === "string";
  }
  function hasEncodingLoss(oldType, newType) {
    if (newType === "complex64") {
      return false;
    }
    if (newType === "float32" && oldType !== "complex64") {
      return false;
    }
    if (newType === "int32" && oldType !== "float32" && oldType !== "complex64") {
      return false;
    }
    if (newType === "bool" && oldType === "bool") {
      return false;
    }
    return true;
  }
  function isTypedArray(a) {
    return a instanceof Float32Array || a instanceof Int32Array || a instanceof Uint8Array;
  }
  function bytesPerElement(dtype) {
    if (dtype === "float32" || dtype === "int32") {
      return 4;
    } else if (dtype === "complex64") {
      return 8;
    } else if (dtype === "bool") {
      return 1;
    } else {
      throw new Error(`Unknown dtype ${dtype}`);
    }
  }
  function bytesFromStringArray(arr) {
    if (arr == null) {
      return 0;
    }
    let bytes = 0;
    arr.forEach((x) => bytes += x.length);
    return bytes;
  }
  function isString(value) {
    return typeof value === "string" || value instanceof String;
  }
  function isBoolean(value) {
    return typeof value === "boolean";
  }
  function isNumber(value) {
    return typeof value === "number";
  }
  function inferDtype(values) {
    if (Array.isArray(values)) {
      return inferDtype(values[0]);
    }
    if (values instanceof Float32Array) {
      return "float32";
    } else if (values instanceof Int32Array || values instanceof Uint8Array) {
      return "int32";
    } else if (isNumber(values)) {
      return "float32";
    } else if (isString(values)) {
      return "string";
    } else if (isBoolean(values)) {
      return "bool";
    }
    return "float32";
  }
  function isFunction(f) {
    return !!(f && f.constructor && f.call && f.apply);
  }
  function nearestDivisor(size, start) {
    for (let i = start; i < size; ++i) {
      if (size % i === 0) {
        return i;
      }
    }
    return size;
  }
  function computeStrides(shape) {
    const rank = shape.length;
    if (rank < 2) {
      return [];
    }
    const strides = new Array(rank - 1);
    strides[rank - 2] = shape[rank - 1];
    for (let i = rank - 3; i >= 0; --i) {
      strides[i] = strides[i + 1] * shape[i + 1];
    }
    return strides;
  }
  function toTypedArray(a, dtype) {
    if (dtype === "string") {
      throw new Error("Cannot convert a string[] to a TypedArray");
    }
    if (Array.isArray(a)) {
      a = flatten(a);
    }
    if (env().getBool("DEBUG")) {
      checkConversionForErrors(a, dtype);
    }
    if (noConversionNeeded(a, dtype)) {
      return a;
    }
    if (dtype == null || dtype === "float32" || dtype === "complex64") {
      return new Float32Array(a);
    } else if (dtype === "int32") {
      return new Int32Array(a);
    } else if (dtype === "bool") {
      const bool = new Uint8Array(a.length);
      for (let i = 0; i < bool.length; ++i) {
        if (Math.round(a[i]) !== 0) {
          bool[i] = 1;
        }
      }
      return bool;
    } else {
      throw new Error(`Unknown data type ${dtype}`);
    }
  }
  function createNestedArray(offset, shape, a) {
    const ret = new Array();
    if (shape.length === 1) {
      const d = shape[0];
      for (let i = 0; i < d; i++) {
        ret[i] = a[offset + i];
      }
    } else {
      const d = shape[0];
      const rest = shape.slice(1);
      const len = rest.reduce((acc, c) => acc * c);
      for (let i = 0; i < d; i++) {
        ret[i] = createNestedArray(offset + i * len, rest, a);
      }
    }
    return ret;
  }
  function toNestedArray(shape, a) {
    if (shape.length === 0) {
      return a[0];
    }
    const size = shape.reduce((acc, c) => acc * c);
    if (size === 0) {
      return [];
    }
    if (size !== a.length) {
      throw new Error(`[${shape}] does not match the input size ${a.length}.`);
    }
    return createNestedArray(0, shape, a);
  }
  function noConversionNeeded(a, dtype) {
    return a instanceof Float32Array && dtype === "float32" || a instanceof Int32Array && dtype === "int32" || a instanceof Uint8Array && dtype === "bool";
  }
  function makeOnesTypedArray(size, dtype) {
    const array = makeZerosTypedArray(size, dtype);
    for (let i = 0; i < array.length; i++) {
      array[i] = 1;
    }
    return array;
  }
  function makeZerosTypedArray(size, dtype) {
    if (dtype == null || dtype === "float32" || dtype === "complex64") {
      return new Float32Array(size);
    } else if (dtype === "int32") {
      return new Int32Array(size);
    } else if (dtype === "bool") {
      return new Uint8Array(size);
    } else {
      throw new Error(`Unknown data type ${dtype}`);
    }
  }
  function makeZerosNestedTypedArray(shape, dtype) {
    const size = shape.reduce((prev, curr) => prev * curr, 1);
    if (dtype == null || dtype === "float32") {
      return toNestedArray(shape, new Float32Array(size));
    } else if (dtype === "int32") {
      return toNestedArray(shape, new Int32Array(size));
    } else if (dtype === "bool") {
      return toNestedArray(shape, new Uint8Array(size));
    } else {
      throw new Error(`Unknown data type ${dtype}`);
    }
  }
  function now() {
    return env().platform.now();
  }
  function assertNonNegativeIntegerDimensions(shape) {
    shape.forEach((dimSize) => {
      assert(Number.isInteger(dimSize) && dimSize >= 0, () => `Tensor must have a shape comprised of positive integers but got shape [${shape}].`);
    });
  }
  function fetch2(path, requestInits) {
    return env().platform.fetch(path, requestInits);
  }
  function encodeString(s, encoding = "utf-8") {
    encoding = encoding || "utf-8";
    return env().platform.encode(s, encoding);
  }
  function decodeString(bytes, encoding = "utf-8") {
    encoding = encoding || "utf-8";
    return env().platform.decode(bytes, encoding);
  }
  function locToIndex(locs, rank, strides) {
    if (rank === 0) {
      return 0;
    } else if (rank === 1) {
      return locs[0];
    }
    let index = locs[locs.length - 1];
    for (let i = 0; i < locs.length - 1; ++i) {
      index += strides[i] * locs[i];
    }
    return index;
  }
  function indexToLoc(index, rank, strides) {
    if (rank === 0) {
      return [];
    } else if (rank === 1) {
      return [index];
    }
    const locs = new Array(rank);
    for (let i = 0; i < locs.length - 1; ++i) {
      locs[i] = Math.floor(index / strides[i]);
      index -= locs[i] * strides[i];
    }
    locs[locs.length - 1] = index;
    return locs;
  }

  // node_modules/@tensorflow/tfjs-core/dist/profiler.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class Profiler {
    constructor(backendTimer, logger) {
      this.backendTimer = backendTimer;
      this.logger = logger;
      if (logger == null) {
        this.logger = new Logger();
      }
    }
    profileKernel(kernelName, inputs, f) {
      let outputs;
      const holdResultWrapperFn = () => {
        outputs = f();
      };
      const timer = this.backendTimer.time(holdResultWrapperFn);
      outputs.map((r) => {
        r.data().then((tensorVals) => {
          checkComputationForErrors(tensorVals, r.dtype, kernelName);
        });
      });
      const kernelProfile = {
        kernelName,
        outputs,
        inputs,
        timeMs: timer.then((timing) => timing.kernelMs),
        extraInfo: timer.then((timing) => timing.getExtraProfileInfo != null ? timing.getExtraProfileInfo() : "")
      };
      return kernelProfile;
    }
    logKernelProfile(kernelProfile) {
      const {kernelName, outputs, timeMs, inputs, extraInfo} = kernelProfile;
      outputs.forEach((result) => {
        Promise.all([result.data(), timeMs, extraInfo]).then((valueContainer) => {
          this.logger.logKernelProfile(kernelName, result, valueContainer[0], valueContainer[1], inputs, valueContainer[2]);
        });
      });
    }
  }
  function checkComputationForErrors(vals, dtype, kernelName) {
    if (dtype !== "float32") {
      return false;
    }
    for (let i = 0; i < vals.length; i++) {
      const num = vals[i];
      if (isNaN(num) || !isFinite(num)) {
        console.warn(`Found ${num} in the result of '${kernelName}'`);
        return true;
      }
    }
    return false;
  }
  class Logger {
    logKernelProfile(name, result, vals, timeMs, inputs, extraInfo) {
      const time2 = typeof timeMs === "number" ? rightPad(`${timeMs}ms`, 9) : timeMs["error"];
      const paddedName = rightPad(name, 25);
      const rank = result.rank;
      const size = result.size;
      const shape = rightPad(result.shape.toString(), 14);
      let inputShapesDescription = "";
      for (const name2 in inputs) {
        const input = inputs[name2];
        if (input != null) {
          const inputShape = input.shape || result.shape;
          const inputRank = inputShape.length;
          inputShapesDescription += `${name2}: ${inputRank}D ${inputRank > 0 ? inputShape : ""} `;
        }
      }
      console.log(`%c${paddedName}	%c${time2}	%c${rank}D ${shape}	%c${size}	%c${inputShapesDescription}	%c${extraInfo}`, "font-weight:bold", "color:red", "color:blue", "color: orange", "color: green", "color: steelblue");
    }
  }

  // node_modules/@tensorflow/tfjs-core/dist/tape.js
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function getFilteredNodesXToY(tape2, xs, y) {
    const tensorsFromX = {};
    const nodesFromX = {};
    for (let i = 0; i < xs.length; i++) {
      tensorsFromX[xs[i].id] = true;
    }
    for (let i = 0; i < tape2.length; i++) {
      const node = tape2[i];
      const nodeInputs = node.inputs;
      for (const inputName in nodeInputs) {
        const input = nodeInputs[inputName];
        let anyInputFromX = false;
        for (let j = 0; j < xs.length; j++) {
          if (tensorsFromX[input.id]) {
            node.outputs.forEach((output) => tensorsFromX[output.id] = true);
            anyInputFromX = true;
            nodesFromX[node.id] = true;
            break;
          }
        }
        if (anyInputFromX) {
          break;
        }
      }
    }
    const tensorsLeadToY = {};
    tensorsLeadToY[y.id] = true;
    const nodesToY = {};
    for (let i = tape2.length - 1; i >= 0; i--) {
      const node = tape2[i];
      const nodeInputs = node.inputs;
      for (let j = 0; j < node.outputs.length; j++) {
        if (tensorsLeadToY[node.outputs[j].id]) {
          for (const inputName in nodeInputs) {
            tensorsLeadToY[nodeInputs[inputName].id] = true;
            nodesToY[node.id] = true;
          }
          break;
        }
      }
    }
    const filteredTape = [];
    for (let i = 0; i < tape2.length; i++) {
      const node = tape2[i];
      if (nodesFromX[node.id] && nodesToY[node.id]) {
        const prunedInputs = {};
        for (const inputName in node.inputs) {
          const nodeInput = node.inputs[inputName];
          if (tensorsFromX[nodeInput.id]) {
            prunedInputs[inputName] = nodeInput;
          }
        }
        const prunedNode = Object.assign({}, node);
        prunedNode.inputs = prunedInputs;
        prunedNode.outputs = node.outputs;
        filteredTape.push(prunedNode);
      }
    }
    return filteredTape;
  }
  function backpropagateGradients(tensorAccumulatedGradientMap, filteredTape, tidy2, add29) {
    for (let i = filteredTape.length - 1; i >= 0; i--) {
      const node = filteredTape[i];
      const dys = [];
      node.outputs.forEach((o) => {
        const gradTensor = tensorAccumulatedGradientMap[o.id];
        if (gradTensor != null) {
          dys.push(gradTensor);
        } else {
          dys.push(null);
        }
      });
      if (node.gradient == null) {
        throw new Error(`Cannot compute gradient: gradient function not found for ${node.kernelName}.`);
      }
      const inputGradients = node.gradient(dys);
      for (const inputName in node.inputs) {
        if (!(inputName in inputGradients)) {
          throw new Error(`Cannot backprop through input ${inputName}. Available gradients found: ${Object.keys(inputGradients)}.`);
        }
        const dx = tidy2(() => inputGradients[inputName]());
        if (dx.dtype !== "float32") {
          throw new Error(`Error in gradient for op ${node.kernelName}. The gradient of input ${inputName} must have 'float32' dtype, but has '${dx.dtype}'`);
        }
        const x = node.inputs[inputName];
        if (!arraysEqual(dx.shape, x.shape)) {
          throw new Error(`Error in gradient for op ${node.kernelName}. The gradient of input '${inputName}' has shape '${dx.shape}', which does not match the shape of the input '${x.shape}'`);
        }
        if (tensorAccumulatedGradientMap[x.id] == null) {
          tensorAccumulatedGradientMap[x.id] = dx;
        } else {
          const curGradient = tensorAccumulatedGradientMap[x.id];
          tensorAccumulatedGradientMap[x.id] = add29(curGradient, dx);
          curGradient.dispose();
        }
      }
    }
  }

  // node_modules/@tensorflow/tfjs-core/dist/tensor_format.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const FORMAT_LIMIT_NUM_VALS = 20;
  const FORMAT_NUM_FIRST_LAST_VALS = 3;
  const FORMAT_NUM_SIG_DIGITS = 7;
  function tensorToString(vals, shape, dtype, verbose) {
    const strides = computeStrides(shape);
    const padPerCol = computeMaxSizePerColumn(vals, shape, dtype, strides);
    const rank = shape.length;
    const valsLines = subTensorToString(vals, shape, dtype, strides, padPerCol);
    const lines = ["Tensor"];
    if (verbose) {
      lines.push(`  dtype: ${dtype}`);
      lines.push(`  rank: ${rank}`);
      lines.push(`  shape: [${shape}]`);
      lines.push(`  values:`);
    }
    lines.push(valsLines.map((l) => "    " + l).join("\n"));
    return lines.join("\n");
  }
  function computeMaxSizePerColumn(vals, shape, dtype, strides) {
    const n = sizeFromShape(shape);
    const numCols = strides[strides.length - 1];
    const padPerCol = new Array(numCols).fill(0);
    const rank = shape.length;
    const valuesOrTuples = dtype === "complex64" ? createComplexTuples(vals) : vals;
    if (rank > 1) {
      for (let row = 0; row < n / numCols; row++) {
        const offset = row * numCols;
        for (let j = 0; j < numCols; j++) {
          padPerCol[j] = Math.max(padPerCol[j], valToString(valuesOrTuples[offset + j], 0, dtype).length);
        }
      }
    }
    return padPerCol;
  }
  function valToString(val, pad8, dtype) {
    let valStr;
    if (Array.isArray(val)) {
      valStr = `${parseFloat(val[0].toFixed(FORMAT_NUM_SIG_DIGITS))} + ${parseFloat(val[1].toFixed(FORMAT_NUM_SIG_DIGITS))}j`;
    } else if (isString(val)) {
      valStr = `'${val}'`;
    } else if (dtype === "bool") {
      valStr = boolNumToString(val);
    } else {
      valStr = parseFloat(val.toFixed(FORMAT_NUM_SIG_DIGITS)).toString();
    }
    return rightPad(valStr, pad8);
  }
  function boolNumToString(v) {
    return v === 0 ? "false" : "true";
  }
  function subTensorToString(vals, shape, dtype, strides, padPerCol, isLast = true) {
    const storagePerElement = dtype === "complex64" ? 2 : 1;
    const size = shape[0];
    const rank = shape.length;
    if (rank === 0) {
      if (dtype === "complex64") {
        const complexTuple = createComplexTuples(vals);
        return [valToString(complexTuple[0], 0, dtype)];
      }
      if (dtype === "bool") {
        return [boolNumToString(vals[0])];
      }
      return [vals[0].toString()];
    }
    if (rank === 1) {
      if (size > FORMAT_LIMIT_NUM_VALS) {
        const firstValsSize = FORMAT_NUM_FIRST_LAST_VALS * storagePerElement;
        let firstVals = Array.from(vals.slice(0, firstValsSize));
        let lastVals = Array.from(vals.slice((size - FORMAT_NUM_FIRST_LAST_VALS) * storagePerElement, size * storagePerElement));
        if (dtype === "complex64") {
          firstVals = createComplexTuples(firstVals);
          lastVals = createComplexTuples(lastVals);
        }
        return [
          "[" + firstVals.map((x, i) => valToString(x, padPerCol[i], dtype)).join(", ") + ", ..., " + lastVals.map((x, i) => valToString(x, padPerCol[size - FORMAT_NUM_FIRST_LAST_VALS + i], dtype)).join(", ") + "]"
        ];
      }
      const displayVals = dtype === "complex64" ? createComplexTuples(vals) : Array.from(vals);
      return [
        "[" + displayVals.map((x, i) => valToString(x, padPerCol[i], dtype)).join(", ") + "]"
      ];
    }
    const subshape = shape.slice(1);
    const substrides = strides.slice(1);
    const stride = strides[0] * storagePerElement;
    const lines = [];
    if (size > FORMAT_LIMIT_NUM_VALS) {
      for (let i = 0; i < FORMAT_NUM_FIRST_LAST_VALS; i++) {
        const start = i * stride;
        const end = start + stride;
        lines.push(...subTensorToString(vals.slice(start, end), subshape, dtype, substrides, padPerCol, false));
      }
      lines.push("...");
      for (let i = size - FORMAT_NUM_FIRST_LAST_VALS; i < size; i++) {
        const start = i * stride;
        const end = start + stride;
        lines.push(...subTensorToString(vals.slice(start, end), subshape, dtype, substrides, padPerCol, i === size - 1));
      }
    } else {
      for (let i = 0; i < size; i++) {
        const start = i * stride;
        const end = start + stride;
        lines.push(...subTensorToString(vals.slice(start, end), subshape, dtype, substrides, padPerCol, i === size - 1));
      }
    }
    const sep = rank === 2 ? "," : "";
    lines[0] = "[" + lines[0] + sep;
    for (let i = 1; i < lines.length - 1; i++) {
      lines[i] = " " + lines[i] + sep;
    }
    let newLineSep = ",\n";
    for (let i = 2; i < rank; i++) {
      newLineSep += "\n";
    }
    lines[lines.length - 1] = " " + lines[lines.length - 1] + "]" + (isLast ? "" : newLineSep);
    return lines;
  }
  function createComplexTuples(vals) {
    const complexTuples = [];
    for (let i = 0; i < vals.length; i += 2) {
      complexTuples.push([vals[i], vals[i + 1]]);
    }
    return complexTuples;
  }

  // node_modules/@tensorflow/tfjs-core/dist/tensor.js
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class TensorBuffer {
    constructor(shape, dtype, values) {
      this.dtype = dtype;
      this.shape = shape.slice();
      this.size = sizeFromShape(shape);
      if (values != null) {
        const n = values.length;
        assert(n === this.size, () => `Length of values '${n}' does not match the size inferred by the shape '${this.size}'.`);
      }
      if (dtype === "complex64") {
        throw new Error(`complex64 dtype TensorBuffers are not supported. Please create a TensorBuffer for the real and imaginary parts separately and call tf.complex(real, imag).`);
      }
      this.values = values || getArrayFromDType(dtype, this.size);
      this.strides = computeStrides(shape);
    }
    set(value, ...locs) {
      if (locs.length === 0) {
        locs = [0];
      }
      assert(locs.length === this.rank, () => `The number of provided coordinates (${locs.length}) must match the rank (${this.rank})`);
      const index = this.locToIndex(locs);
      this.values[index] = value;
    }
    get(...locs) {
      if (locs.length === 0) {
        locs = [0];
      }
      let i = 0;
      for (const loc of locs) {
        if (loc < 0 || loc >= this.shape[i]) {
          const msg = `Requested out of range element at ${locs}.   Buffer shape=${this.shape}`;
          throw new Error(msg);
        }
        i++;
      }
      let index = locs[locs.length - 1];
      for (let i2 = 0; i2 < locs.length - 1; ++i2) {
        index += this.strides[i2] * locs[i2];
      }
      return this.values[index];
    }
    locToIndex(locs) {
      if (this.rank === 0) {
        return 0;
      } else if (this.rank === 1) {
        return locs[0];
      }
      let index = locs[locs.length - 1];
      for (let i = 0; i < locs.length - 1; ++i) {
        index += this.strides[i] * locs[i];
      }
      return index;
    }
    indexToLoc(index) {
      if (this.rank === 0) {
        return [];
      } else if (this.rank === 1) {
        return [index];
      }
      const locs = new Array(this.shape.length);
      for (let i = 0; i < locs.length - 1; ++i) {
        locs[i] = Math.floor(index / this.strides[i]);
        index -= locs[i] * this.strides[i];
      }
      locs[locs.length - 1] = index;
      return locs;
    }
    get rank() {
      return this.shape.length;
    }
    toTensor() {
      return trackerFn().makeTensor(this.values, this.shape, this.dtype);
    }
  }
  let trackerFn = null;
  let opHandler = null;
  let deprecationWarningFn = null;
  function setTensorTracker(fn) {
    trackerFn = fn;
  }
  function setOpHandler(handler) {
    opHandler = handler;
  }
  function setDeprecationWarningFn(fn) {
    deprecationWarningFn = fn;
  }
  class Tensor {
    constructor(shape, dtype, dataId, id) {
      this.kept = false;
      this.isDisposedInternal = false;
      this.shape = shape.slice();
      this.dtype = dtype || "float32";
      this.size = sizeFromShape(shape);
      this.strides = computeStrides(shape);
      this.dataId = dataId;
      this.id = id;
      this.rankType = this.rank < 5 ? this.rank.toString() : "higher";
    }
    get rank() {
      return this.shape.length;
    }
    async buffer() {
      const vals = await this.data();
      return opHandler.buffer(this.shape, this.dtype, vals);
    }
    bufferSync() {
      return opHandler.buffer(this.shape, this.dtype, this.dataSync());
    }
    async array() {
      const vals = await this.data();
      return toNestedArray(this.shape, vals);
    }
    arraySync() {
      return toNestedArray(this.shape, this.dataSync());
    }
    async data() {
      this.throwIfDisposed();
      const data = trackerFn().read(this.dataId);
      if (this.dtype === "string") {
        const bytes = await data;
        try {
          return bytes.map((b) => decodeString(b));
        } catch (_a) {
          throw new Error("Failed to decode the string bytes into utf-8. To get the original bytes, call tensor.bytes().");
        }
      }
      return data;
    }
    dataSync() {
      this.throwIfDisposed();
      const data = trackerFn().readSync(this.dataId);
      if (this.dtype === "string") {
        try {
          return data.map((b) => decodeString(b));
        } catch (_a) {
          throw new Error("Failed to decode the string bytes into utf-8. To get the original bytes, call tensor.bytes().");
        }
      }
      return data;
    }
    async bytes() {
      this.throwIfDisposed();
      const data = await trackerFn().read(this.dataId);
      if (this.dtype === "string") {
        return data;
      } else {
        return new Uint8Array(data.buffer);
      }
    }
    dispose() {
      if (this.isDisposed) {
        return;
      }
      trackerFn().disposeTensor(this);
      this.isDisposedInternal = true;
    }
    get isDisposed() {
      return this.isDisposedInternal;
    }
    throwIfDisposed() {
      if (this.isDisposed) {
        throw new Error(`Tensor is disposed.`);
      }
    }
    print(verbose = false) {
      return opHandler.print(this, verbose);
    }
    clone() {
      this.throwIfDisposed();
      return opHandler.clone(this);
    }
    toString(verbose = false) {
      const vals = this.dataSync();
      return tensorToString(vals, this.shape, this.dtype, verbose);
    }
    cast(dtype) {
      this.throwIfDisposed();
      return opHandler.cast(this, dtype);
    }
    variable(trainable = true, name, dtype) {
      this.throwIfDisposed();
      return trackerFn().makeVariable(this, trainable, name, dtype);
    }
  }
  Object.defineProperty(Tensor, Symbol.hasInstance, {
    value: (instance) => {
      return !!instance && instance.dataId != null && instance.shape != null && instance.dtype != null;
    }
  });
  class Variable extends Tensor {
    constructor(initialValue, trainable, name, tensorId) {
      super(initialValue.shape, initialValue.dtype, initialValue.dataId, tensorId);
      this.trainable = trainable;
      this.name = name;
    }
    assign(newValue) {
      if (newValue.dtype !== this.dtype) {
        throw new Error(`dtype of the new value (${newValue.dtype}) and previous value (${this.dtype}) must match`);
      }
      if (!arraysEqual(newValue.shape, this.shape)) {
        throw new Error(`shape of the new value (${newValue.shape}) and previous value (${this.shape}) must match`);
      }
      trackerFn().disposeTensor(this);
      this.dataId = newValue.dataId;
      trackerFn().incRef(this, null);
    }
    dispose() {
      trackerFn().disposeVariable(this);
      this.isDisposedInternal = true;
    }
  }
  Object.defineProperty(Variable, Symbol.hasInstance, {
    value: (instance) => {
      return instance instanceof Tensor && instance.assign != null && instance.assign instanceof Function;
    }
  });

  // node_modules/@tensorflow/tfjs-core/dist/types.js
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  var Rank;
  (function(Rank2) {
    Rank2["R0"] = "R0";
    Rank2["R1"] = "R1";
    Rank2["R2"] = "R2";
    Rank2["R3"] = "R3";
    Rank2["R4"] = "R4";
    Rank2["R5"] = "R5";
    Rank2["R6"] = "R6";
  })(Rank || (Rank = {}));
  var UpcastInt32AndMap;
  (function(UpcastInt32AndMap2) {
    UpcastInt32AndMap2["float32"] = "float32";
    UpcastInt32AndMap2["int32"] = "int32";
    UpcastInt32AndMap2["bool"] = "int32";
    UpcastInt32AndMap2["complex64"] = "complex64";
  })(UpcastInt32AndMap || (UpcastInt32AndMap = {}));
  var UpcastBoolAndMap;
  (function(UpcastBoolAndMap2) {
    UpcastBoolAndMap2["float32"] = "float32";
    UpcastBoolAndMap2["int32"] = "int32";
    UpcastBoolAndMap2["bool"] = "bool";
    UpcastBoolAndMap2["complex64"] = "complex64";
  })(UpcastBoolAndMap || (UpcastBoolAndMap = {}));
  var UpcastFloat32AndMap;
  (function(UpcastFloat32AndMap2) {
    UpcastFloat32AndMap2["float32"] = "float32";
    UpcastFloat32AndMap2["int32"] = "float32";
    UpcastFloat32AndMap2["bool"] = "float32";
    UpcastFloat32AndMap2["complex64"] = "complex64";
  })(UpcastFloat32AndMap || (UpcastFloat32AndMap = {}));
  var UpcastComplex64AndMap;
  (function(UpcastComplex64AndMap2) {
    UpcastComplex64AndMap2["float32"] = "complex64";
    UpcastComplex64AndMap2["int32"] = "complex64";
    UpcastComplex64AndMap2["bool"] = "complex64";
    UpcastComplex64AndMap2["complex64"] = "complex64";
  })(UpcastComplex64AndMap || (UpcastComplex64AndMap = {}));
  const upcastTypeMap = {
    float32: UpcastFloat32AndMap,
    int32: UpcastInt32AndMap,
    bool: UpcastBoolAndMap,
    complex64: UpcastComplex64AndMap
  };
  function upcastType(typeA, typeB) {
    if (typeA === "string" || typeB === "string") {
      if (typeA === "string" && typeB === "string") {
        return "string";
      }
      throw new Error(`Can not upcast ${typeA} with ${typeB}`);
    }
    return upcastTypeMap[typeA][typeB];
  }
  function sumOutType(type) {
    return upcastType(type, "int32");
  }

  // node_modules/@tensorflow/tfjs-core/dist/tensor_util.js
  const tensor_util_exports = {};
  __export(tensor_util_exports, {
    assertTypesMatch: () => assertTypesMatch,
    getTensorsInContainer: () => getTensorsInContainer,
    isTensorInList: () => isTensorInList,
    makeTypesMatch: () => makeTypesMatch
  });
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function makeTypesMatch(a, b) {
    if (a.dtype === b.dtype) {
      return [a, b];
    }
    const dtype = upcastType(a.dtype, b.dtype);
    return [a.cast(dtype), b.cast(dtype)];
  }
  function assertTypesMatch(a, b) {
    assert(a.dtype === b.dtype, () => `The dtypes of the first(${a.dtype}) and second(${b.dtype}) input must match`);
  }
  function isTensorInList(tensor17, tensorList) {
    return tensorList.some((x) => x.id === tensor17.id);
  }
  function getTensorsInContainer(result) {
    const list = [];
    const seen = new Set();
    walkTensorContainer(result, list, seen);
    return list;
  }
  function walkTensorContainer(container, list, seen) {
    if (container == null) {
      return;
    }
    if (container instanceof Tensor) {
      list.push(container);
      return;
    }
    if (!isIterable(container)) {
      return;
    }
    const iterable = container;
    for (const k in iterable) {
      const val = iterable[k];
      if (!seen.has(val)) {
        seen.add(val);
        walkTensorContainer(val, list, seen);
      }
    }
  }
  function isIterable(obj) {
    return Array.isArray(obj) || typeof obj === "object";
  }

  // node_modules/@tensorflow/tfjs-core/dist/engine.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class EngineState {
    constructor() {
      this.registeredVariables = {};
      this.nextTapeNodeId = 0;
      this.numBytes = 0;
      this.numTensors = 0;
      this.numStringTensors = 0;
      this.numDataBuffers = 0;
      this.gradientDepth = 0;
      this.kernelDepth = 0;
      this.scopeStack = [];
      this.numDataMovesStack = [];
      this.nextScopeId = 0;
      this.tensorInfo = new WeakMap();
      this.profiling = false;
      this.activeProfile = {newBytes: 0, newTensors: 0, peakBytes: 0, kernels: [], result: null};
    }
    dispose() {
      for (const variableName in this.registeredVariables) {
        this.registeredVariables[variableName].dispose();
      }
    }
  }
  class Engine {
    constructor(ENV3) {
      this.ENV = ENV3;
      this.registry = {};
      this.registryFactory = {};
      this.pendingBackendInitId = 0;
      this.state = new EngineState();
    }
    async ready() {
      if (this.pendingBackendInit != null) {
        return this.pendingBackendInit.then(() => {
        });
      }
      if (this.backendInstance != null) {
        return;
      }
      const sortedBackends = this.getSortedBackends();
      for (let i = 0; i < sortedBackends.length; i++) {
        const backendName = sortedBackends[i];
        const success = await this.initializeBackend(backendName).success;
        if (success) {
          await this.setBackend(backendName);
          return;
        }
      }
      throw new Error(`Could not initialize any backends, all backend initializations failed.`);
    }
    get backend() {
      if (this.pendingBackendInit != null) {
        throw new Error(`Backend '${this.backendName}' has not yet been initialized. Make sure to await tf.ready() or await tf.setBackend() before calling other methods`);
      }
      if (this.backendInstance == null) {
        const {name, asyncInit} = this.initializeBackendsAndReturnBest();
        if (asyncInit) {
          throw new Error(`The highest priority backend '${name}' has not yet been initialized. Make sure to await tf.ready() or await tf.setBackend() before calling other methods`);
        }
        this.setBackend(name);
      }
      return this.backendInstance;
    }
    backendNames() {
      return Object.keys(this.registryFactory);
    }
    findBackend(backendName) {
      if (!(backendName in this.registry)) {
        if (backendName in this.registryFactory) {
          const {asyncInit} = this.initializeBackend(backendName);
          if (asyncInit) {
            return null;
          }
        } else {
          return null;
        }
      }
      return this.registry[backendName];
    }
    findBackendFactory(backendName) {
      if (!(backendName in this.registryFactory)) {
        return null;
      }
      return this.registryFactory[backendName].factory;
    }
    registerBackend(backendName, factory, priority = 1) {
      if (backendName in this.registryFactory) {
        console.warn(`${backendName} backend was already registered. Reusing existing backend factory.`);
        return false;
      }
      this.registryFactory[backendName] = {factory, priority};
      return true;
    }
    async setBackend(backendName) {
      if (this.registryFactory[backendName] == null) {
        throw new Error(`Backend name '${backendName}' not found in registry`);
      }
      this.backendName = backendName;
      if (this.registry[backendName] == null) {
        this.backendInstance = null;
        const {success, asyncInit} = this.initializeBackend(backendName);
        const result = asyncInit ? await success : success;
        if (!result) {
          return false;
        }
      }
      this.backendInstance = this.registry[backendName];
      this.setupRegisteredKernels();
      this.profiler = new Profiler(this.backendInstance);
      return true;
    }
    setupRegisteredKernels() {
      const kernels = getKernelsForBackend(this.backendName);
      kernels.forEach((kernel) => {
        if (kernel.setupFunc != null) {
          kernel.setupFunc(this.backendInstance);
        }
      });
    }
    disposeRegisteredKernels(backendName) {
      const kernels = getKernelsForBackend(backendName);
      kernels.forEach((kernel) => {
        if (kernel.disposeFunc != null) {
          kernel.disposeFunc(this.registry[backendName]);
        }
      });
    }
    initializeBackend(backendName) {
      const registryFactoryEntry = this.registryFactory[backendName];
      if (registryFactoryEntry == null) {
        throw new Error(`Cannot initialize backend ${backendName}, no registration found.`);
      }
      try {
        const backend2 = registryFactoryEntry.factory();
        if (Promise.resolve(backend2) === backend2) {
          const promiseId = ++this.pendingBackendInitId;
          const success = backend2.then((backendInstance) => {
            if (promiseId < this.pendingBackendInitId) {
              return false;
            }
            this.registry[backendName] = backendInstance;
            this.pendingBackendInit = null;
            return true;
          }).catch((err) => {
            if (promiseId < this.pendingBackendInitId) {
              return false;
            }
            this.pendingBackendInit = null;
            console.warn(`Initialization of backend ${backendName} failed`);
            console.warn(err.stack || err.message);
            return false;
          });
          this.pendingBackendInit = success;
          return {success, asyncInit: true};
        } else {
          this.registry[backendName] = backend2;
          return {success: true, asyncInit: false};
        }
      } catch (err) {
        console.warn(`Initialization of backend ${backendName} failed`);
        console.warn(err.stack || err.message);
        return {success: false, asyncInit: false};
      }
    }
    removeBackend(backendName) {
      if (!(backendName in this.registryFactory)) {
        throw new Error(`${backendName} backend not found in registry`);
      }
      if (this.backendName === backendName && this.pendingBackendInit != null) {
        this.pendingBackendInitId++;
      }
      if (backendName in this.registry) {
        this.disposeRegisteredKernels(backendName);
        this.registry[backendName].dispose();
        delete this.registry[backendName];
      }
      delete this.registryFactory[backendName];
      if (this.backendName === backendName) {
        this.pendingBackendInit = null;
        this.backendName = null;
        this.backendInstance = null;
      }
    }
    getSortedBackends() {
      if (Object.keys(this.registryFactory).length === 0) {
        throw new Error("No backend found in registry.");
      }
      return Object.keys(this.registryFactory).sort((a, b) => {
        return this.registryFactory[b].priority - this.registryFactory[a].priority;
      });
    }
    initializeBackendsAndReturnBest() {
      const sortedBackends = this.getSortedBackends();
      for (let i = 0; i < sortedBackends.length; i++) {
        const backendName = sortedBackends[i];
        const {success, asyncInit} = this.initializeBackend(backendName);
        if (asyncInit || success) {
          return {name: backendName, asyncInit};
        }
      }
      throw new Error(`Could not initialize any backends, all backend initializations failed.`);
    }
    moveData(backend2, dataId) {
      const info = this.state.tensorInfo.get(dataId);
      const srcBackend = info.backend;
      const values = this.readSync(dataId);
      srcBackend.disposeData(dataId);
      info.backend = backend2;
      backend2.move(dataId, values, info.shape, info.dtype);
      if (this.shouldCheckForMemLeaks()) {
        this.state.numDataMovesStack[this.state.numDataMovesStack.length - 1]++;
      }
    }
    tidy(nameOrFn, fn) {
      let name = null;
      if (fn == null) {
        if (typeof nameOrFn !== "function") {
          throw new Error("Please provide a function to tidy()");
        }
        fn = nameOrFn;
      } else {
        if (typeof nameOrFn !== "string" && !(nameOrFn instanceof String)) {
          throw new Error("When calling with two arguments, the first argument to tidy() must be a string");
        }
        if (typeof fn !== "function") {
          throw new Error("When calling with two arguments, the 2nd argument to tidy() must be a function");
        }
        name = nameOrFn;
      }
      let result;
      return this.scopedRun(() => this.startScope(name), () => this.endScope(result), () => {
        result = fn();
        if (result instanceof Promise) {
          console.error("Cannot return a Promise inside of tidy.");
        }
        return result;
      });
    }
    scopedRun(start, end, f) {
      start();
      try {
        const res = f();
        end();
        return res;
      } catch (ex) {
        end();
        throw ex;
      }
    }
    nextTensorId() {
      return Engine.nextTensorId++;
    }
    nextVariableId() {
      return Engine.nextVariableId++;
    }
    clone(x) {
      const y = this.makeTensorFromDataId(x.dataId, x.shape, x.dtype);
      const inputs = {x};
      const grad2 = (dy) => ({
        x: () => {
          const dtype = "float32";
          const gradInputs = {x: dy};
          const attrs = {dtype};
          return ENGINE.runKernelFunc((backend2) => backend2.cast(dy, dtype), gradInputs, null, Cast, attrs);
        }
      });
      const saved = [];
      this.addTapeNode(this.state.activeScope.name, inputs, [y], grad2, saved, {});
      return y;
    }
    runKernel(kernelName, inputs, attrs, inputsToSave, outputsToSave) {
      const forwardFunc = null;
      const backwardsFunc = null;
      return this.runKernelFunc(forwardFunc, inputs, backwardsFunc, kernelName, attrs, inputsToSave, outputsToSave);
    }
    shouldCheckForMemLeaks() {
      return this.ENV.getBool("IS_TEST");
    }
    checkKernelForMemLeak(kernelName, numDataIdsBefore, outInfos) {
      const numDataIdsAfter = this.backend.numDataIds();
      let numOutputDataIds = 0;
      outInfos.forEach((info) => {
        numOutputDataIds += info.dtype === "complex64" ? 3 : 1;
      });
      const numMoves = this.state.numDataMovesStack[this.state.numDataMovesStack.length - 1];
      const dataIdsLeaked = numDataIdsAfter - numDataIdsBefore - numOutputDataIds - numMoves;
      if (dataIdsLeaked > 0) {
        throw new Error(`Backend '${this.backendName}' has an internal memory leak (${dataIdsLeaked} data ids) after running '${kernelName}'`);
      }
    }
    runKernelFunc(forwardFunc, inputs, backwardsFunc, kernelName, attrs, inputsToSave, outputsToSave) {
      let outputs;
      let saved = [];
      const isTapeOn = this.isTapeOn();
      if (kernelName == null) {
        kernelName = this.state.activeScope != null ? this.state.activeScope.name : "";
      }
      const startingBytecount = this.state.numBytes;
      const startingNumTensors = this.state.numTensors;
      if (this.shouldCheckForMemLeaks()) {
        this.state.numDataMovesStack.push(0);
      }
      let kernelFunc;
      const kernel = getKernel(kernelName, this.backendName);
      let out;
      if (kernel != null) {
        kernelFunc = () => {
          const numDataIdsBefore = this.backend.numDataIds();
          out = kernel.kernelFunc({inputs, attrs, backend: this.backend});
          const outInfos = Array.isArray(out) ? out : [out];
          if (this.shouldCheckForMemLeaks()) {
            this.checkKernelForMemLeak(kernelName, numDataIdsBefore, outInfos);
          }
          const outTensors = outInfos.map(({dataId, shape, dtype}) => this.makeTensorFromDataId(dataId, shape, dtype));
          if (isTapeOn) {
            let tensorsToSave = this.getTensorsForGradient(kernelName, inputs, outTensors);
            if (tensorsToSave == null) {
              if (outputsToSave == null) {
                outputsToSave = [];
              }
              const outsToSave = outTensors.filter((_, i) => outputsToSave[i]);
              tensorsToSave = (inputsToSave || []).slice().concat(outsToSave);
            }
            saved = this.saveTensorsForBackwardMode(tensorsToSave);
          }
          return outTensors;
        };
      } else {
        const saveFunc = (tensors) => {
          if (!isTapeOn) {
            return;
          }
          saved = tensors.map((tensor17) => this.keep(this.clone(tensor17)));
        };
        kernelFunc = () => {
          const numDataIdsBefore = this.backend.numDataIds();
          out = this.tidy(() => forwardFunc(this.backend, saveFunc));
          const outs = Array.isArray(out) ? out : [out];
          if (this.shouldCheckForMemLeaks()) {
            this.checkKernelForMemLeak(kernelName, numDataIdsBefore, outs);
          }
          return outs;
        };
      }
      let kernelProfile;
      this.scopedRun(() => this.state.kernelDepth++, () => this.state.kernelDepth--, () => {
        if (!this.ENV.getBool("DEBUG") && !this.state.profiling) {
          outputs = kernelFunc();
        } else {
          kernelProfile = this.profiler.profileKernel(kernelName, inputs, () => kernelFunc());
          if (this.ENV.getBool("DEBUG")) {
            this.profiler.logKernelProfile(kernelProfile);
          }
          outputs = kernelProfile.outputs;
        }
      });
      if (isTapeOn) {
        this.addTapeNode(kernelName, inputs, outputs, backwardsFunc, saved, attrs);
      }
      if (this.state.profiling) {
        this.state.activeProfile.kernels.push({
          name: kernelName,
          bytesAdded: this.state.numBytes - startingBytecount,
          totalBytesSnapshot: this.state.numBytes,
          tensorsAdded: this.state.numTensors - startingNumTensors,
          totalTensorsSnapshot: this.state.numTensors,
          inputShapes: Object.keys(inputs).map((key) => inputs[key] != null ? inputs[key].shape : null),
          outputShapes: outputs.map((item) => item.shape),
          kernelTimeMs: kernelProfile.timeMs,
          extraInfo: kernelProfile.extraInfo
        });
      }
      return Array.isArray(out) ? outputs : outputs[0];
    }
    saveTensorsForBackwardMode(tensors) {
      const saved = tensors.map((tensor17) => this.keep(this.clone(tensor17)));
      return saved;
    }
    getTensorsForGradient(kernelName, inputs, outputs) {
      const gradConfig = getGradient(kernelName);
      if (gradConfig != null) {
        const inputsToSave = gradConfig.inputsToSave || [];
        const outputsToSave = gradConfig.outputsToSave || [];
        let inputTensorsToSave;
        if (gradConfig.saveAllInputs) {
          assert(Array.isArray(inputs), () => "saveAllInputs is true, expected inputs to be an array.");
          inputTensorsToSave = Object.keys(inputs).map((key) => inputs[key]);
        } else {
          inputTensorsToSave = inputsToSave.map((inputName) => inputs[inputName]);
        }
        const outputTensorsToSave = outputs.filter((_, i) => outputsToSave[i]);
        return inputTensorsToSave.concat(outputTensorsToSave);
      }
      return null;
    }
    makeTensor(values, shape, dtype, backend2) {
      if (values == null) {
        throw new Error("Values passed to engine.makeTensor() are null");
      }
      dtype = dtype || "float32";
      backend2 = backend2 || this.backend;
      let backendVals = values;
      if (dtype === "string" && isString(values[0])) {
        backendVals = values.map((d) => encodeString(d));
      }
      const dataId = backend2.write(backendVals, shape, dtype);
      const t = new Tensor(shape, dtype, dataId, this.nextTensorId());
      this.incRef(t, backend2);
      if (dtype === "string") {
        const info = this.state.tensorInfo.get(dataId);
        const newBytes = bytesFromStringArray(backendVals);
        this.state.numBytes += newBytes - info.bytes;
        info.bytes = newBytes;
      }
      return t;
    }
    makeTensorFromDataId(dataId, shape, dtype, backend2) {
      dtype = dtype || "float32";
      const t = new Tensor(shape, dtype, dataId, this.nextTensorId());
      this.incRef(t, backend2);
      return t;
    }
    makeVariable(initialValue, trainable = true, name, dtype) {
      name = name || this.nextVariableId().toString();
      if (dtype != null && dtype !== initialValue.dtype) {
        initialValue = initialValue.cast(dtype);
      }
      const v = new Variable(initialValue, trainable, name, this.nextTensorId());
      if (this.state.registeredVariables[v.name] != null) {
        throw new Error(`Variable with name ${v.name} was already registered`);
      }
      this.state.registeredVariables[v.name] = v;
      this.incRef(v, this.backend);
      return v;
    }
    incRef(a, backend2) {
      const refCount = this.state.tensorInfo.has(a.dataId) ? this.state.tensorInfo.get(a.dataId).refCount : 0;
      this.state.numTensors++;
      if (a.dtype === "string") {
        this.state.numStringTensors++;
      }
      if (refCount === 0) {
        this.state.numDataBuffers++;
        let bytes = 0;
        if (a.dtype !== "complex64" && a.dtype !== "string") {
          bytes = a.size * bytesPerElement(a.dtype);
        }
        this.state.tensorInfo.set(a.dataId, {
          backend: backend2 || this.backend,
          dtype: a.dtype,
          shape: a.shape,
          bytes,
          refCount: 0
        });
        this.state.numBytes += bytes;
      }
      this.state.tensorInfo.get(a.dataId).refCount++;
      if (!(a instanceof Variable)) {
        this.track(a);
      }
    }
    disposeTensor(a) {
      if (!this.state.tensorInfo.has(a.dataId)) {
        return;
      }
      this.state.numTensors--;
      if (a.dtype === "string") {
        this.state.numStringTensors--;
      }
      const info = this.state.tensorInfo.get(a.dataId);
      const refCount = info.refCount;
      if (refCount <= 1) {
        if (a.dtype !== "complex64") {
          this.state.numBytes -= info.bytes;
        }
        this.state.numDataBuffers--;
        info.backend.disposeData(a.dataId);
        this.state.tensorInfo.delete(a.dataId);
      } else {
        this.state.tensorInfo.get(a.dataId).refCount--;
      }
    }
    disposeVariables() {
      for (const varName in this.state.registeredVariables) {
        const v = this.state.registeredVariables[varName];
        this.disposeVariable(v);
      }
    }
    disposeVariable(v) {
      this.disposeTensor(v);
      if (this.state.registeredVariables[v.name] != null) {
        delete this.state.registeredVariables[v.name];
      }
    }
    memory() {
      const info = this.backend.memory();
      info.numTensors = this.state.numTensors;
      info.numDataBuffers = this.state.numDataBuffers;
      info.numBytes = this.state.numBytes;
      if (this.state.numStringTensors > 0) {
        info.unreliable = true;
        if (info.reasons == null) {
          info.reasons = [];
        }
        info.reasons.push("Memory usage by string tensors is approximate (2 bytes per character)");
      }
      return info;
    }
    async profile(query) {
      this.state.profiling = true;
      const startBytes = this.state.numBytes;
      const startNumTensors = this.state.numTensors;
      this.state.activeProfile.kernels = [];
      this.state.activeProfile.result = await query();
      this.state.profiling = false;
      this.state.activeProfile.peakBytes = Math.max(...this.state.activeProfile.kernels.map((d) => d.totalBytesSnapshot));
      this.state.activeProfile.newBytes = this.state.numBytes - startBytes;
      this.state.activeProfile.newTensors = this.state.numTensors - startNumTensors;
      for (const kernel of this.state.activeProfile.kernels) {
        kernel.kernelTimeMs = await kernel.kernelTimeMs;
        kernel.extraInfo = await kernel.extraInfo;
      }
      return this.state.activeProfile;
    }
    isTapeOn() {
      return this.state.gradientDepth > 0 && this.state.kernelDepth === 0;
    }
    addTapeNode(kernelName, inputs, outputs, gradientsFunc, saved, attrs) {
      const tapeNode = {id: this.state.nextTapeNodeId++, kernelName, inputs, outputs, saved};
      const gradConfig = getGradient(kernelName);
      if (gradConfig != null) {
        gradientsFunc = gradConfig.gradFunc;
      }
      if (gradientsFunc != null) {
        tapeNode.gradient = (dys) => {
          dys = dys.map((dy, i) => {
            if (dy == null) {
              const output = outputs[i];
              const vals = makeZerosTypedArray(output.size, output.dtype);
              return this.makeTensor(vals, output.shape, output.dtype);
            }
            return dy;
          });
          return gradientsFunc(dys.length > 1 ? dys : dys[0], saved, attrs);
        };
      }
      this.state.activeTape.push(tapeNode);
    }
    keep(result) {
      result.kept = true;
      return result;
    }
    startTape() {
      if (this.state.gradientDepth === 0) {
        this.state.activeTape = [];
      }
      this.state.gradientDepth++;
    }
    endTape() {
      this.state.gradientDepth--;
    }
    startScope(name) {
      const scopeInfo = {
        track: [],
        name: "unnamed scope",
        id: this.state.nextScopeId++
      };
      if (name) {
        scopeInfo.name = name;
      }
      this.state.scopeStack.push(scopeInfo);
      this.state.activeScope = scopeInfo;
    }
    endScope(result) {
      const tensorsToTrackInParent = getTensorsInContainer(result);
      const tensorsToTrackInParentSet = new Set(tensorsToTrackInParent.map((t) => t.id));
      for (let i = 0; i < this.state.activeScope.track.length; i++) {
        const tensor17 = this.state.activeScope.track[i];
        if (!tensor17.kept && !tensorsToTrackInParentSet.has(tensor17.id)) {
          tensor17.dispose();
        }
      }
      const oldScope = this.state.scopeStack.pop();
      this.state.activeScope = this.state.scopeStack.length === 0 ? null : this.state.scopeStack[this.state.scopeStack.length - 1];
      tensorsToTrackInParent.forEach((tensor17) => {
        if (!tensor17.kept && tensor17.scopeId === oldScope.id) {
          this.track(tensor17);
        }
      });
    }
    gradients(f, xs, dy, allowNoGradients = false) {
      assert(xs.length > 0, () => "gradients() received an empty list of xs.");
      if (dy != null && dy.dtype !== "float32") {
        throw new Error(`dy must have 'float32' dtype, but has '${dy.dtype}'`);
      }
      const y = this.scopedRun(() => this.startTape(), () => this.endTape(), () => this.tidy("forward", f));
      assert(y instanceof Tensor, () => "The result y returned by f() must be a tensor.");
      const filteredTape = getFilteredNodesXToY(this.state.activeTape, xs, y);
      if (!allowNoGradients && filteredTape.length === 0 && xs.length > 0) {
        throw new Error("Cannot compute gradient of y=f(x) with respect to x. Make sure that the f you passed encloses all operations that lead from x to y.");
      }
      return this.tidy("backward", () => {
        const accumulatedGradientMap = {};
        accumulatedGradientMap[y.id] = dy == null ? ones(y.shape) : dy;
        backpropagateGradients(accumulatedGradientMap, filteredTape, (f2) => this.tidy(f2), add);
        const grads2 = xs.map((x) => accumulatedGradientMap[x.id]);
        if (this.state.gradientDepth === 0) {
          this.state.activeTape.forEach((node) => {
            for (const tensor17 of node.saved) {
              tensor17.dispose();
            }
          });
          this.state.activeTape = null;
        }
        return {value: y, grads: grads2};
      });
    }
    customGrad(f) {
      assert(isFunction(f), () => "The f passed in customGrad(f) must be a function.");
      return (...inputs) => {
        assert(inputs.every((t) => t instanceof Tensor), () => "The args passed in customGrad(f)(x1, x2,...) must all be tensors");
        let res;
        const inputMap = {};
        inputs.forEach((input, i) => {
          inputMap[i] = input;
        });
        return this.runKernelFunc((_, save) => {
          res = f(...[...inputs, save]);
          assert(res.value instanceof Tensor, () => "The function f passed in customGrad(f) must return an object where `obj.value` is a tensor");
          assert(isFunction(res.gradFunc), () => "The function f passed in customGrad(f) must return an object where `obj.gradFunc` is a function.");
          return res.value;
        }, inputMap, (dy, saved) => {
          const gradRes = res.gradFunc(dy, saved);
          const grads2 = Array.isArray(gradRes) ? gradRes : [gradRes];
          assert(grads2.length === inputs.length, () => "The function f passed in customGrad(f) must return an object where `obj.gradFunc` is a function that returns the same number of tensors as inputs passed to f(...).");
          assert(grads2.every((t) => t instanceof Tensor), () => "The function f passed in customGrad(f) must return an object where `obj.gradFunc` is a function that returns a list of only tensors.");
          const gradMap = {};
          grads2.forEach((grad2, i) => {
            gradMap[i] = () => grad2;
          });
          return gradMap;
        });
      };
    }
    readSync(dataId) {
      const info = this.state.tensorInfo.get(dataId);
      return info.backend.readSync(dataId);
    }
    read(dataId) {
      const info = this.state.tensorInfo.get(dataId);
      return info.backend.read(dataId);
    }
    async time(query) {
      const start = now();
      const timingInfo = await this.backend.time(query);
      timingInfo.wallMs = now() - start;
      return timingInfo;
    }
    track(result) {
      if (this.state.activeScope != null) {
        result.scopeId = this.state.activeScope.id;
        this.state.activeScope.track.push(result);
      }
      return result;
    }
    get registeredVariables() {
      return this.state.registeredVariables;
    }
    reset() {
      this.pendingBackendInitId++;
      this.state.dispose();
      this.ENV.reset();
      this.state = new EngineState();
      for (const backendName in this.registry) {
        this.disposeRegisteredKernels(backendName);
        this.registry[backendName].dispose();
        delete this.registry[backendName];
      }
      this.backendName = null;
      this.backendInstance = null;
      this.pendingBackendInit = null;
    }
  }
  Engine.nextTensorId = 0;
  Engine.nextVariableId = 0;
  function ones(shape) {
    const values = makeOnesTypedArray(sizeFromShape(shape), "float32");
    return ENGINE.makeTensor(values, shape, "float32");
  }
  function getOrMakeEngine() {
    const ns = getGlobalNamespace();
    if (ns._tfengine == null) {
      const environment12 = new Environment(ns);
      ns._tfengine = new Engine(environment12);
    }
    setEnvironmentGlobal(ns._tfengine.ENV);
    setTensorTracker(() => ns._tfengine);
    return ns._tfengine;
  }
  const ENGINE = getOrMakeEngine();
  function add(a, b) {
    const inputs = {a, b};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.add(a, b);
      save([a, b]);
      return res;
    }, inputs, null, Add);
  }

  // node_modules/@tensorflow/tfjs-core/dist/device_util.js
  const device_util_exports = {};
  __export(device_util_exports, {
    isBrowser: () => isBrowser,
    isMobile: () => isMobile
  });
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function _isNavigatorDefined() {
    return typeof navigator !== "undefined" && navigator != null;
  }
  function isMobile() {
    if (_isNavigatorDefined()) {
      const a = navigator.userAgent || navigator.vendor || window.opera;
      return /(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4));
    }
    return false;
  }
  function isBrowser() {
    return typeof window !== "undefined" && window.document != null || typeof WorkerGlobalScope !== "undefined";
  }

  // node_modules/@tensorflow/tfjs-core/dist/flags.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const ENV2 = env();
  ENV2.registerFlag("DEBUG", () => false, (debugValue) => {
    if (debugValue) {
      console.warn("Debugging mode is ON. The output of every math call will be downloaded to CPU and checked for NaNs. This significantly impacts performance.");
    }
  });
  ENV2.registerFlag("IS_BROWSER", () => isBrowser());
  ENV2.registerFlag("IS_NODE", () => typeof process !== "undefined" && typeof process.versions !== "undefined" && typeof process.versions.node !== "undefined");
  ENV2.registerFlag("IS_CHROME", () => typeof navigator !== "undefined" && navigator != null && navigator.userAgent != null && /Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor));
  ENV2.registerFlag("PROD", () => false);
  ENV2.registerFlag("TENSORLIKE_CHECK_SHAPE_CONSISTENCY", () => ENV2.getBool("DEBUG"));
  ENV2.registerFlag("DEPRECATION_WARNINGS_ENABLED", () => true);
  ENV2.registerFlag("IS_TEST", () => false);

  // node_modules/@tensorflow/tfjs-core/dist/tensor_util_env.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function inferShape(val, dtype) {
    let firstElem = val;
    if (isTypedArray(val)) {
      return dtype === "string" ? [] : [val.length];
    }
    if (!Array.isArray(val)) {
      return [];
    }
    const shape = [];
    while (Array.isArray(firstElem) || isTypedArray(firstElem) && dtype !== "string") {
      shape.push(firstElem.length);
      firstElem = firstElem[0];
    }
    if (Array.isArray(val) && env().getBool("TENSORLIKE_CHECK_SHAPE_CONSISTENCY")) {
      deepAssertShapeConsistency(val, shape, []);
    }
    return shape;
  }
  function deepAssertShapeConsistency(val, shape, indices) {
    indices = indices || [];
    if (!Array.isArray(val) && !isTypedArray(val)) {
      assert(shape.length === 0, () => `Element arr[${indices.join("][")}] is a primitive, but should be an array/TypedArray of ${shape[0]} elements`);
      return;
    }
    assert(shape.length > 0, () => `Element arr[${indices.join("][")}] should be a primitive, but is an array of ${val.length} elements`);
    assert(val.length === shape[0], () => `Element arr[${indices.join("][")}] should have ${shape[0]} elements, but has ${val.length} elements`);
    const subShape = shape.slice(1);
    for (let i = 0; i < val.length; ++i) {
      deepAssertShapeConsistency(val[i], subShape, indices.concat(i));
    }
  }
  function assertDtype(expectedDtype, actualDType, argName, functionName) {
    if (expectedDtype == null) {
      return;
    }
    if (expectedDtype !== "numeric" && expectedDtype !== actualDType || expectedDtype === "numeric" && actualDType === "string") {
      throw new Error(`Argument '${argName}' passed to '${functionName}' must be ${expectedDtype} tensor, but got ${actualDType} tensor`);
    }
  }
  function convertToTensor(x, argName, functionName, parseAsDtype = "numeric") {
    if (x instanceof Tensor) {
      assertDtype(parseAsDtype, x.dtype, argName, functionName);
      return x;
    }
    let inferredDtype = inferDtype(x);
    if (inferredDtype !== "string" && ["bool", "int32", "float32"].indexOf(parseAsDtype) >= 0) {
      inferredDtype = parseAsDtype;
    }
    assertDtype(parseAsDtype, inferredDtype, argName, functionName);
    if (x == null || !isTypedArray(x) && !Array.isArray(x) && typeof x !== "number" && typeof x !== "boolean" && typeof x !== "string") {
      const type = x == null ? "null" : x.constructor.name;
      throw new Error(`Argument '${argName}' passed to '${functionName}' must be a Tensor or TensorLike, but got '${type}'`);
    }
    const inferredShape = inferShape(x, inferredDtype);
    if (!isTypedArray(x) && !Array.isArray(x)) {
      x = [x];
    }
    const skipTypedArray = true;
    const values = inferredDtype !== "string" ? toTypedArray(x, inferredDtype) : flatten(x, [], skipTypedArray);
    return ENGINE.makeTensor(values, inferredShape, inferredDtype);
  }
  function convertToTensorArray(arg, argName, functionName, parseAsDtype = "numeric") {
    if (!Array.isArray(arg)) {
      throw new Error(`Argument ${argName} passed to ${functionName} must be a \`Tensor[]\` or \`TensorLike[]\``);
    }
    const tensors = arg;
    return tensors.map((t, i) => convertToTensor(t, `${argName}[${i}]`, functionName), parseAsDtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/operation.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function op(f) {
    const keys = Object.keys(f);
    if (keys.length !== 1) {
      throw new Error(`Please provide an object with a single key (operation name) mapping to a function. Got an object with ${keys.length} keys.`);
    }
    let opName = keys[0];
    const fn = f[opName];
    if (opName.endsWith("_")) {
      opName = opName.substring(0, opName.length - 1);
    }
    const f2 = (...args) => {
      ENGINE.startScope(opName);
      try {
        const result = fn(...args);
        if (result instanceof Promise) {
          console.error("Cannot return a Promise inside of tidy.");
        }
        ENGINE.endScope(result);
        return result;
      } catch (ex) {
        ENGINE.endScope(null);
        throw ex;
      }
    };
    Object.defineProperty(f2, "name", {value: opName, configurable: true});
    return f2;
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/abs.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function abs_(x) {
    const $x = convertToTensor(x, "x", "abs");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      save([$x]);
      if ($x.dtype === "complex64") {
        return backend2.complexAbs($x);
      }
      return backend2.abs($x);
    }, inputs, null, Abs);
  }
  const abs = op({abs_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/acos.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function acos_(x) {
    const $x = convertToTensor(x, "x", "acos");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.acos($x);
      save([$x]);
      return res;
    }, inputs, null, Acos);
  }
  const acos = op({acos_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/acosh.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function acosh_(x) {
    const $x = convertToTensor(x, "x", "acosh");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.acosh($x);
      save([$x]);
      return res;
    }, inputs, null, Acosh);
  }
  const acosh = op({acosh_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/add.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function add_(a, b) {
    let $a = convertToTensor(a, "a", "add");
    let $b = convertToTensor(b, "b", "add");
    [$a, $b] = makeTypesMatch($a, $b);
    const forward = (backend2, save) => {
      const res = backend2.add($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, Add);
  }
  const add2 = op({add_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/add_n.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function addN_(tensors) {
    assert(Array.isArray(tensors), () => "The argument passed to tf.addN() must be a list of tensors");
    assert(tensors.length >= 1, () => `Must pass at least one tensor to tf.addN(), but got ${tensors.length}`);
    const $tensors = tensors.map((t, i) => convertToTensor(t, `tensors${i}`, "addN"));
    const firstTensor = $tensors[0];
    $tensors.forEach((t) => {
      if (t.dtype !== firstTensor.dtype) {
        throw new Error("All tensors passed to tf.addN() must have the same dtype");
      }
    });
    $tensors.forEach((t) => {
      if (!arraysEqual(t.shape, firstTensor.shape)) {
        throw new Error("All tensors passed to tf.addN() must have the same shape");
      }
    });
    const forward = (backend2, save) => {
      const res = backend2.addN($tensors);
      save($tensors);
      return res;
    };
    const inputs = $tensors;
    return ENGINE.runKernelFunc(forward, inputs, null, AddN);
  }
  const addN = op({addN_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/axis_util.js
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function axesAreInnerMostDims(axes, rank) {
    for (let i = 0; i < axes.length; ++i) {
      if (axes[axes.length - i - 1] !== rank - 1 - i) {
        return false;
      }
    }
    return true;
  }
  function combineLocations(outputLoc, reduceLoc, axes) {
    const rank = outputLoc.length + reduceLoc.length;
    const loc = [];
    let outIdx = 0;
    let reduceIdx = 0;
    for (let dim = 0; dim < rank; dim++) {
      if (axes.indexOf(dim) === -1) {
        loc.push(outputLoc[outIdx++]);
      } else {
        loc.push(reduceLoc[reduceIdx++]);
      }
    }
    return loc;
  }
  function computeOutAndReduceShapes(aShape, axes) {
    const outShape = [];
    const rank = aShape.length;
    for (let dim = 0; dim < rank; dim++) {
      if (axes.indexOf(dim) === -1) {
        outShape.push(aShape[dim]);
      }
    }
    const reduceShape = axes.map((dim) => aShape[dim]);
    return [outShape, reduceShape];
  }
  function expandShapeToKeepDim(shape, axes) {
    const reduceSubShape = axes.map((x) => 1);
    return combineLocations(shape, reduceSubShape, axes);
  }
  function assertAxesAreInnerMostDims(msg, axes, rank) {
    assert(axesAreInnerMostDims(axes, rank), () => `${msg} supports only inner-most axes for now. Got axes ${axes} and rank-${rank} input.`);
  }
  function getAxesPermutation(axes, rank) {
    if (axesAreInnerMostDims(axes, rank)) {
      return null;
    }
    const result = [];
    for (let i = 0; i < rank; ++i) {
      if (axes.indexOf(i) === -1) {
        result.push(i);
      }
    }
    axes.forEach((axis) => result.push(axis));
    return result;
  }
  function getUndoAxesPermutation(axes) {
    return axes.map((axis, i) => [i, axis]).sort((a, b) => a[1] - b[1]).map((x) => x[0]);
  }
  function getInnerMostAxes(numAxes, rank) {
    const res = [];
    for (let i = rank - numAxes; i < rank; ++i) {
      res.push(i);
    }
    return res;
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/reshape.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function reshape_(x, shape) {
    const $x = convertToTensor(x, "x", "reshape", null);
    shape = inferFromImplicitShape(shape, $x.size);
    assert($x.size === sizeFromShape(shape), () => "new shape and old shape must have the same number of elements.");
    const inputs = {x: $x};
    const attrs = {shape};
    const forward = (backend2, save) => {
      save([$x]);
      return backend2.reshape($x, shape);
    };
    return ENGINE.runKernelFunc(forward, inputs, null, Reshape, attrs);
  }
  const reshape = op({reshape_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/transpose.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function transpose_(x, perm) {
    const $x = convertToTensor(x, "x", "transpose");
    if (perm == null) {
      perm = $x.shape.map((s, i) => i).reverse();
    }
    assert($x.rank === perm.length, () => `Error in transpose: rank of input ${$x.rank} must match length of perm ${perm}.`);
    perm.forEach((axis) => {
      assert(axis >= 0 && axis < $x.rank, () => `All entries in 'perm' must be between 0 and ${$x.rank - 1} but got ${perm}`);
    });
    if ($x.rank <= 1) {
      return $x.clone();
    }
    const inputs = {x: $x};
    const attrs = {perm};
    return ENGINE.runKernelFunc((backend2) => backend2.transpose($x, perm), inputs, null, Transpose, attrs);
  }
  const transpose = op({transpose_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/all.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function all_(x, axis = null, keepDims = false) {
    let $x = convertToTensor(x, "x", "all", "bool");
    const forward = (backend2) => {
      const origAxes = parseAxisParam(axis, $x.shape);
      let axes = origAxes;
      const permutedAxes = getAxesPermutation(axes, $x.rank);
      if (permutedAxes != null) {
        $x = transpose($x, permutedAxes);
        axes = getInnerMostAxes(axes.length, $x.rank);
      }
      const res = backend2.all($x, axes);
      if (keepDims) {
        const newShape = expandShapeToKeepDim(res.shape, origAxes);
        return reshape(res, newShape);
      }
      return res;
    };
    const inputs = {x: $x};
    const attrs = {axis, keepDims};
    return ENGINE.runKernelFunc(forward, inputs, null, All, attrs);
  }
  const all = op({all_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/any.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function any_(x, axis = null, keepDims = false) {
    let $x = convertToTensor(x, "x", "any", "bool");
    const forward = (backend2) => {
      const origAxes = parseAxisParam(axis, $x.shape);
      let axes = origAxes;
      const permutedAxes = getAxesPermutation(axes, $x.rank);
      if (permutedAxes != null) {
        $x = transpose($x, permutedAxes);
        axes = getInnerMostAxes(axes.length, $x.rank);
      }
      const res = backend2.any($x, axes);
      if (keepDims) {
        const newShape = expandShapeToKeepDim(res.shape, origAxes);
        return reshape(res, newShape);
      }
      return res;
    };
    const inputs = {x: $x};
    const attrs = {axis, keepDims};
    return ENGINE.runKernelFunc(forward, inputs, null, Any, attrs);
  }
  const any = op({any_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/arg_max.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function argMax_(x, axis = 0) {
    let $x = convertToTensor(x, "x", "argMax");
    const forward = (backend2, save) => {
      save([$x]);
      let axes = parseAxisParam(axis, $x.shape);
      const permutedAxes = getAxesPermutation(axes, $x.rank);
      if (permutedAxes != null) {
        $x = transpose($x, permutedAxes);
        axes = getInnerMostAxes(axes.length, $x.rank);
      }
      return backend2.argMax($x, axes[0]);
    };
    const inputs = {x: $x};
    const attrs = {axis};
    return ENGINE.runKernelFunc(forward, inputs, null, ArgMax, attrs);
  }
  const argMax = op({argMax_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/arg_min.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function argMin_(x, axis = 0) {
    let $x = convertToTensor(x, "x", "argMin");
    const forward = (backend2, save) => {
      save([$x]);
      if (axis == null) {
        axis = 0;
      }
      let axes = parseAxisParam(axis, $x.shape);
      const permutedAxes = getAxesPermutation(axes, $x.rank);
      if (permutedAxes != null) {
        $x = transpose($x, permutedAxes);
        axes = getInnerMostAxes(axes.length, $x.rank);
      }
      return backend2.argMin($x, axes[0]);
    };
    const inputs = {x: $x};
    const attrs = {axis};
    return ENGINE.runKernelFunc(forward, inputs, null, ArgMin, attrs);
  }
  const argMin = op({argMin_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/asin.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function asin_(x) {
    const $x = convertToTensor(x, "x", "asin");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.asin($x);
      save([$x]);
      return res;
    }, inputs, null, Asin);
  }
  const asin = op({asin_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/asinh.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function asinh_(x) {
    const $x = convertToTensor(x, "x", "asinh");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.asinh($x);
      save([$x]);
      return res;
    }, inputs, null, Asinh);
  }
  const asinh = op({asinh_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/atan.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function atan_(x) {
    const $x = convertToTensor(x, "x", "atan");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.atan($x);
      save([$x]);
      return res;
    }, inputs, null, Atan);
  }
  const atan = op({atan_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/atan2.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function atan2_(a, b) {
    let $a = convertToTensor(a, "a", "atan2");
    let $b = convertToTensor(b, "b", "atan2");
    [$a, $b] = makeTypesMatch($a, $b);
    const forward = (backend2, save) => {
      const res = backend2.atan2($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, Atan2);
  }
  const atan2 = op({atan2_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/atanh.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function atanh_(x) {
    const $x = convertToTensor(x, "x", "atanh");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.atanh($x);
      save([$x]);
      return res;
    }, inputs, null, Atanh);
  }
  const atanh = op({atanh_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/cast.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function cast_(x, dtype) {
    const $x = convertToTensor(x, "x", "cast");
    if (!isValidDtype(dtype)) {
      throw new Error(`Failed to cast to unknown dtype ${dtype}`);
    }
    if (dtype === "string" && $x.dtype !== "string" || dtype !== "string" && $x.dtype === "string") {
      throw new Error("Only strings can be casted to strings");
    }
    const inputs = {x: $x};
    const attrs = {dtype};
    return ENGINE.runKernelFunc((backend2) => backend2.cast($x, dtype), inputs, null, Cast, attrs);
  }
  const cast = op({cast_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/conv_util.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function computeDilation2DInfo(inputShape, filterShape, strides, pad8, dataFormat = "NHWC", dilations) {
    const inputChannels = inputShape[3];
    const $filterShape = [...filterShape, inputChannels];
    const $dataFormat = convertConv2DDataFormat(dataFormat);
    return computeConv2DInfo(inputShape, $filterShape, strides, dilations, pad8, null, null, $dataFormat);
  }
  function computePool2DInfo(inShape, filterSize, strides, dilations, pad8, roundingMode, dataFormat = "channelsLast") {
    const [filterHeight, filterWidth] = parseTupleParam(filterSize);
    let filterShape;
    if (dataFormat === "channelsLast") {
      filterShape = [filterHeight, filterWidth, inShape[3], inShape[3]];
    } else if (dataFormat === "channelsFirst") {
      filterShape = [filterHeight, filterWidth, inShape[1], inShape[1]];
    } else {
      throw new Error(`Unknown dataFormat ${dataFormat}`);
    }
    return computeConv2DInfo(inShape, filterShape, strides, dilations, pad8, roundingMode, false, dataFormat);
  }
  function computePool3DInfo(inShape, filterSize, strides, dilations, pad8, roundingMode, dataFormat = "NDHWC") {
    const [filterDepth, filterHeight, filterWidth] = parse3TupleParam(filterSize);
    let filterShape;
    let $dataFormat;
    if (dataFormat === "NDHWC") {
      $dataFormat = "channelsLast";
      filterShape = [filterDepth, filterHeight, filterWidth, inShape[4], inShape[4]];
    } else if (dataFormat === "NCDHW") {
      $dataFormat = "channelsFirst";
      filterShape = [filterDepth, filterHeight, filterWidth, inShape[1], inShape[1]];
    } else {
      throw new Error(`Unknown dataFormat ${dataFormat}`);
    }
    return computeConv3DInfo(inShape, filterShape, strides, dilations, pad8, false, $dataFormat, roundingMode);
  }
  function computeConv2DInfo(inShape, filterShape, strides, dilations, pad8, roundingMode, depthwise = false, dataFormat = "channelsLast") {
    let [batchSize, inHeight, inWidth, inChannels] = [-1, -1, -1, -1];
    if (dataFormat === "channelsLast") {
      [batchSize, inHeight, inWidth, inChannels] = inShape;
    } else if (dataFormat === "channelsFirst") {
      [batchSize, inChannels, inHeight, inWidth] = inShape;
    } else {
      throw new Error(`Unknown dataFormat ${dataFormat}`);
    }
    const [filterHeight, filterWidth, , filterChannels] = filterShape;
    const [strideHeight, strideWidth] = parseTupleParam(strides);
    const [dilationHeight, dilationWidth] = parseTupleParam(dilations);
    const effectiveFilterHeight = getEffectiveFilterSize(filterHeight, dilationHeight);
    const effectiveFilterWidth = getEffectiveFilterSize(filterWidth, dilationWidth);
    const {padInfo, outHeight, outWidth} = getPadAndOutInfo(pad8, inHeight, inWidth, strideHeight, strideWidth, effectiveFilterHeight, effectiveFilterWidth, roundingMode, dataFormat);
    const outChannels = depthwise ? filterChannels * inChannels : filterChannels;
    let outShape;
    if (dataFormat === "channelsFirst") {
      outShape = [batchSize, outChannels, outHeight, outWidth];
    } else if (dataFormat === "channelsLast") {
      outShape = [batchSize, outHeight, outWidth, outChannels];
    }
    return {
      batchSize,
      dataFormat,
      inHeight,
      inWidth,
      inChannels,
      outHeight,
      outWidth,
      outChannels,
      padInfo,
      strideHeight,
      strideWidth,
      filterHeight,
      filterWidth,
      effectiveFilterHeight,
      effectiveFilterWidth,
      dilationHeight,
      dilationWidth,
      inShape,
      outShape,
      filterShape
    };
  }
  function computeConv3DInfo(inShape, filterShape, strides, dilations, pad8, depthwise = false, dataFormat = "channelsLast", roundingMode) {
    let [batchSize, inDepth, inHeight, inWidth, inChannels] = [-1, -1, -1, -1, -1];
    if (dataFormat === "channelsLast") {
      [batchSize, inDepth, inHeight, inWidth, inChannels] = inShape;
    } else if (dataFormat === "channelsFirst") {
      [batchSize, inChannels, inDepth, inHeight, inWidth] = inShape;
    } else {
      throw new Error(`Unknown dataFormat ${dataFormat}`);
    }
    const [filterDepth, filterHeight, filterWidth, , filterChannels] = filterShape;
    const [strideDepth, strideHeight, strideWidth] = parse3TupleParam(strides);
    const [dilationDepth, dilationHeight, dilationWidth] = parse3TupleParam(dilations);
    const effectiveFilterDepth = getEffectiveFilterSize(filterDepth, dilationDepth);
    const effectiveFilterHeight = getEffectiveFilterSize(filterHeight, dilationHeight);
    const effectiveFilterWidth = getEffectiveFilterSize(filterWidth, dilationWidth);
    const {padInfo, outDepth, outHeight, outWidth} = get3DPadAndOutInfo(pad8, inDepth, inHeight, inWidth, strideDepth, strideHeight, strideWidth, effectiveFilterDepth, effectiveFilterHeight, effectiveFilterWidth, roundingMode);
    const outChannels = depthwise ? filterChannels * inChannels : filterChannels;
    let outShape;
    if (dataFormat === "channelsFirst") {
      outShape = [batchSize, outChannels, outDepth, outHeight, outWidth];
    } else if (dataFormat === "channelsLast") {
      outShape = [batchSize, outDepth, outHeight, outWidth, outChannels];
    }
    return {
      batchSize,
      dataFormat,
      inDepth,
      inHeight,
      inWidth,
      inChannels,
      outDepth,
      outHeight,
      outWidth,
      outChannels,
      padInfo,
      strideDepth,
      strideHeight,
      strideWidth,
      filterDepth,
      filterHeight,
      filterWidth,
      effectiveFilterDepth,
      effectiveFilterHeight,
      effectiveFilterWidth,
      dilationDepth,
      dilationHeight,
      dilationWidth,
      inShape,
      outShape,
      filterShape
    };
  }
  function computeOutputShape2D(inShape, fieldSize, stride, zeroPad, roundingMode) {
    if (zeroPad == null) {
      zeroPad = computeDefaultPad(inShape, fieldSize, stride);
    }
    const inputRows = inShape[0];
    const inputCols = inShape[1];
    const outputRows = conditionalRound((inputRows - fieldSize + 2 * zeroPad) / stride + 1, roundingMode);
    assert(isInt(outputRows), () => `The output # of rows (${outputRows}) must be an integer. Change the stride and/or zero pad parameters`);
    const outputCols = conditionalRound((inputCols - fieldSize + 2 * zeroPad) / stride + 1, roundingMode);
    assert(isInt(outputCols), () => `The output # of columns (${outputCols}) must be an integer. Change the stride and/or zero pad parameters`);
    return [outputRows, outputCols];
  }
  function computeOutputShape4D(inShape, fieldSize, outChannels, stride, zeroPad, roundingMode) {
    if (zeroPad == null) {
      zeroPad = computeDefaultPad(inShape, fieldSize, stride);
    }
    const inputDepth = inShape[0];
    const inputRows = inShape[1];
    const inputCols = inShape[2];
    const outputDepths = conditionalRound((inputDepth - fieldSize + 2 * zeroPad) / stride + 1, roundingMode);
    assert(isInt(outputDepths), () => `The output # of depths (${outputDepths}) must be an integer. Change the stride and/or zero pad parameters`);
    const outputRows = conditionalRound((inputRows - fieldSize + 2 * zeroPad) / stride + 1, roundingMode);
    assert(isInt(outputRows), () => `The output # of rows (${outputRows}) must be an integer. Change the stride and/or zero pad parameters`);
    const outputCols = conditionalRound((inputCols - fieldSize + 2 * zeroPad) / stride + 1, roundingMode);
    assert(isInt(outputCols), () => `The output # of columns (${outputCols}) must be an integer. Change the stride and/or zero pad parameters`);
    return [outputDepths, outputRows, outputCols, outChannels];
  }
  function computeDefaultPad(inputShape, fieldSize, stride, dilation = 1) {
    const effectiveFieldSize = getEffectiveFilterSize(fieldSize, dilation);
    return Math.floor((inputShape[0] * (stride - 1) - stride + effectiveFieldSize) / 2);
  }
  function parseTupleParam(param) {
    if (typeof param === "number") {
      return [param, param, param];
    }
    if (param.length === 2) {
      return [param[0], param[1], 1];
    }
    return param;
  }
  function parse3TupleParam(param) {
    return typeof param === "number" ? [param, param, param] : param;
  }
  function getEffectiveFilterSize(filterSize, dilation) {
    if (dilation <= 1) {
      return filterSize;
    }
    return filterSize + (filterSize - 1) * (dilation - 1);
  }
  function getPadAndOutInfo(pad8, inHeight, inWidth, strideHeight, strideWidth, filterHeight, filterWidth, roundingMode, dataFormat) {
    let padInfo;
    let outHeight;
    let outWidth;
    if (typeof pad8 === "number") {
      const padType = pad8 === 0 ? "VALID" : "NUMBER";
      padInfo = {top: pad8, bottom: pad8, left: pad8, right: pad8, type: padType};
      const outShape = computeOutputShape2D([inHeight, inWidth], filterHeight, strideHeight, pad8, roundingMode);
      outHeight = outShape[0];
      outWidth = outShape[1];
    } else if (pad8 === "same") {
      outHeight = Math.ceil(inHeight / strideHeight);
      outWidth = Math.ceil(inWidth / strideWidth);
      const padAlongHeight = Math.max(0, (outHeight - 1) * strideHeight + filterHeight - inHeight);
      const padAlongWidth = Math.max(0, (outWidth - 1) * strideWidth + filterWidth - inWidth);
      const top = Math.floor(padAlongHeight / 2);
      const bottom = padAlongHeight - top;
      const left = Math.floor(padAlongWidth / 2);
      const right = padAlongWidth - left;
      padInfo = {top, bottom, left, right, type: "SAME"};
    } else if (pad8 === "valid") {
      padInfo = {top: 0, bottom: 0, left: 0, right: 0, type: "VALID"};
      outHeight = Math.ceil((inHeight - filterHeight + 1) / strideHeight);
      outWidth = Math.ceil((inWidth - filterWidth + 1) / strideWidth);
    } else if (typeof pad8 === "object") {
      const top = dataFormat === "channelsLast" ? pad8[1][0] : pad8[2][0];
      const bottom = dataFormat === "channelsLast" ? pad8[1][1] : pad8[2][1];
      const left = dataFormat === "channelsLast" ? pad8[2][0] : pad8[3][0];
      const right = dataFormat === "channelsLast" ? pad8[2][1] : pad8[3][1];
      const padType = top === 0 && bottom === 0 && left === 0 && right === 0 ? "VALID" : "EXPLICIT";
      padInfo = {top, bottom, left, right, type: padType};
      outHeight = conditionalRound((inHeight - filterHeight + top + bottom) / strideHeight + 1, roundingMode);
      outWidth = conditionalRound((inWidth - filterWidth + left + right) / strideWidth + 1, roundingMode);
    } else {
      throw Error(`Unknown padding parameter: ${pad8}`);
    }
    return {padInfo, outHeight, outWidth};
  }
  function get3DPadAndOutInfo(pad8, inDepth, inHeight, inWidth, strideDepth, strideHeight, strideWidth, filterDepth, filterHeight, filterWidth, roundingMode) {
    let padInfo;
    let outDepth;
    let outHeight;
    let outWidth;
    if (typeof pad8 === "number") {
      const padType = pad8 === 0 ? "VALID" : "NUMBER";
      padInfo = {
        top: pad8,
        bottom: pad8,
        left: pad8,
        right: pad8,
        front: pad8,
        back: pad8,
        type: padType
      };
      const outShape = computeOutputShape4D([inDepth, inHeight, inWidth, 1], filterDepth, 1, strideDepth, pad8, roundingMode);
      outDepth = outShape[0];
      outHeight = outShape[1];
      outWidth = outShape[2];
    } else if (pad8 === "same") {
      outDepth = Math.ceil(inDepth / strideDepth);
      outHeight = Math.ceil(inHeight / strideHeight);
      outWidth = Math.ceil(inWidth / strideWidth);
      const padAlongDepth = (outDepth - 1) * strideDepth + filterDepth - inDepth;
      const padAlongHeight = (outHeight - 1) * strideHeight + filterHeight - inHeight;
      const padAlongWidth = (outWidth - 1) * strideWidth + filterWidth - inWidth;
      const front = Math.floor(padAlongDepth / 2);
      const back = padAlongDepth - front;
      const top = Math.floor(padAlongHeight / 2);
      const bottom = padAlongHeight - top;
      const left = Math.floor(padAlongWidth / 2);
      const right = padAlongWidth - left;
      padInfo = {top, bottom, left, right, front, back, type: "SAME"};
    } else if (pad8 === "valid") {
      padInfo = {
        top: 0,
        bottom: 0,
        left: 0,
        right: 0,
        front: 0,
        back: 0,
        type: "VALID"
      };
      outDepth = Math.ceil((inDepth - filterDepth + 1) / strideDepth);
      outHeight = Math.ceil((inHeight - filterHeight + 1) / strideHeight);
      outWidth = Math.ceil((inWidth - filterWidth + 1) / strideWidth);
    } else {
      throw Error(`Unknown padding parameter: ${pad8}`);
    }
    return {padInfo, outDepth, outHeight, outWidth};
  }
  function conditionalRound(value, roundingMode) {
    if (!roundingMode) {
      return value;
    }
    switch (roundingMode) {
      case "round":
        return Math.round(value);
      case "ceil":
        return Math.ceil(value);
      case "floor":
        return Math.floor(value);
      default:
        throw new Error(`Unknown roundingMode ${roundingMode}`);
    }
  }
  function tupleValuesAreOne(param) {
    const [dimA, dimB, dimC] = parseTupleParam(param);
    return dimA === 1 && dimB === 1 && dimC === 1;
  }
  function eitherStridesOrDilationsAreOne(strides, dilations) {
    return tupleValuesAreOne(strides) || tupleValuesAreOne(dilations);
  }
  function convertConv2DDataFormat(dataFormat) {
    if (dataFormat === "NHWC") {
      return "channelsLast";
    } else if (dataFormat === "NCHW") {
      return "channelsFirst";
    } else {
      throw new Error(`Unknown dataFormat ${dataFormat}`);
    }
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/avg_pool.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function avgPool_(x, filterSize, strides, pad8, dimRoundingMode) {
    const $x = convertToTensor(x, "x", "avgPool", "float32");
    const dilations = 1;
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in avgPool: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    let x4D = $x;
    let reshapedTo4D = false;
    if ($x.rank === 3) {
      reshapedTo4D = true;
      x4D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2]]);
    }
    assert(x4D.rank === 4, () => `Error in avgPool: x must be rank 4 but got rank ${x4D.rank}.`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in avgPool: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const forward = (backend2, save) => {
      const convInfo = computePool2DInfo(x4D.shape, filterSize, strides, 1, pad8, dimRoundingMode);
      save([x4D]);
      if (convInfo.filterWidth === 1 && convInfo.filterHeight === 1 && arraysEqual(convInfo.inShape, convInfo.outShape)) {
        return x4D.clone();
      }
      return backend2.avgPool(x4D, convInfo);
    };
    const inputs = {x: x4D};
    const attrs = {filterSize, strides, pad: pad8, dimRoundingMode};
    let res = ENGINE.runKernelFunc(forward, inputs, null, AvgPool, attrs);
    res = cast(res, $x.dtype);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const avgPool = op({avgPool_});

  // node_modules/@tensorflow/tfjs-core/dist/globals.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function enableProdMode() {
    env().set("PROD", true);
  }
  function enableDebugMode() {
    env().set("DEBUG", true);
  }
  function disableDeprecationWarnings() {
    env().set("DEPRECATION_WARNINGS_ENABLED", false);
    console.warn(`TensorFlow.js deprecation warnings have been disabled.`);
  }
  function deprecationWarn(msg) {
    if (env().getBool("DEPRECATION_WARNINGS_ENABLED")) {
      console.warn(msg + " You can disable deprecation warnings with tf.disableDeprecationWarnings().");
    }
  }
  setDeprecationWarningFn(deprecationWarn);
  function disposeVariables() {
    ENGINE.disposeVariables();
  }
  function engine22() {
    return ENGINE;
  }
  function memory() {
    return ENGINE.memory();
  }
  function profile(f) {
    return ENGINE.profile(f);
  }
  function tidy(nameOrFn, fn) {
    return ENGINE.tidy(nameOrFn, fn);
  }
  function dispose(container) {
    const tensors = getTensorsInContainer(container);
    tensors.forEach((tensor17) => tensor17.dispose());
  }
  function keep(result) {
    return ENGINE.keep(result);
  }
  function time(f) {
    return ENGINE.time(f);
  }
  function setBackend(backendName) {
    return ENGINE.setBackend(backendName);
  }
  function ready() {
    return ENGINE.ready();
  }
  function getBackend() {
    return ENGINE.backendName;
  }
  function removeBackend(name) {
    ENGINE.removeBackend(name);
  }
  function findBackend(name) {
    return ENGINE.findBackend(name);
  }
  function findBackendFactory(name) {
    return ENGINE.findBackendFactory(name);
  }
  function registerBackend(name, factory, priority = 1) {
    return ENGINE.registerBackend(name, factory, priority);
  }
  function backend() {
    return ENGINE.backend;
  }
  function setPlatform(platformName, platform) {
    env().setPlatform(platformName, platform);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/avg_pool_3d.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function avgPool3d_(x, filterSize, strides, pad8, dimRoundingMode, dataFormat = "NDHWC", dilations) {
    if (dilations == null) {
      dilations = [1, 1, 1];
    } else {
      deprecationWarn("dilations is deprecated, this field will be gone in v3.0.0.");
    }
    const $x = convertToTensor(x, "x", "avgPool3d", "float32");
    let x5D = $x;
    let reshapedTo5D = false;
    if ($x.rank === 4) {
      reshapedTo5D = true;
      x5D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2], $x.shape[3]]);
    }
    assert(x5D.rank === 5, () => `Error in avgPool3d: x must be rank 5 but got rank ${x5D.rank}.`);
    assert(dataFormat === "NDHWC", () => `Error in avgPool3d: Only NDHWC is currently supported, but got dataFormat of ${dataFormat}`);
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in avgPool3d: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in avgPool3d: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const forward = (backend2, save) => {
      if (dilations == null) {
        dilations = [1, 1, 1];
      }
      const convInfo = computePool3DInfo(x5D.shape, filterSize, strides, dilations, pad8, dimRoundingMode, dataFormat);
      save([x5D]);
      return backend2.avgPool3d(x5D, convInfo);
    };
    const inputs = {x: x5D};
    const attrs = {filterSize, strides, pad: pad8, dimRoundingMode, dataFormat, dilations};
    let res = ENGINE.runKernelFunc(forward, inputs, null, AvgPool3D, attrs);
    res = cast(res, x5D.dtype);
    if (reshapedTo5D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3], res.shape[4]]);
    }
    return res;
  }
  const avgPool3d = op({avgPool3d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/concat_util.js
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function assertParamsConsistent(shapes, axis) {
    const rank = shapes[0].length;
    shapes.forEach((shape, i) => {
      assert(shape.length === rank, () => `Error in concat${rank}D: rank of tensors[${i}] must be the same as the rank of the rest (${rank})`);
    });
    assert(axis >= 0 && axis < rank, () => `Error in concat${rank}D: axis must be between 0 and ${rank - 1}.`);
    const firstShape = shapes[0];
    shapes.forEach((shape, i) => {
      for (let r = 0; r < rank; r++) {
        assert(r === axis || shape[r] === firstShape[r], () => `Error in concat${rank}D: Shape of tensors[${i}] (${shape}) does not match the shape of the rest (${firstShape}) along the non-concatenated axis ${i}.`);
      }
    });
  }
  function computeOutShape(shapes, axis) {
    const outputShape = shapes[0].slice();
    for (let i = 1; i < shapes.length; i++) {
      outputShape[axis] += shapes[i][axis];
    }
    return outputShape;
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/tensor_ops_util.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function makeTensor(values, shape, inferredShape, dtype) {
    if (dtype == null) {
      dtype = inferDtype(values);
    }
    if (dtype === "complex64") {
      throw new Error(`Cannot construct a complex64 tensor directly. Please use tf.complex(real, imag).`);
    }
    if (!isTypedArray(values) && !Array.isArray(values) && typeof values !== "number" && typeof values !== "boolean" && typeof values !== "string") {
      throw new Error("values passed to tensor(values) must be a number/boolean/string or an array of numbers/booleans/strings, or a TypedArray");
    }
    if (shape != null) {
      assertNonNegativeIntegerDimensions(shape);
      const providedSize = sizeFromShape(shape);
      const inferredSize = sizeFromShape(inferredShape);
      assert(providedSize === inferredSize, () => `Based on the provided shape, [${shape}], the tensor should have ${providedSize} values but has ${inferredSize}`);
      for (let i = 0; i < inferredShape.length; ++i) {
        const inferred = inferredShape[i];
        const flatDimsDontMatch = i === inferredShape.length - 1 ? inferred !== sizeFromShape(shape.slice(i)) : true;
        assert(inferredShape[i] === shape[i] || !flatDimsDontMatch, () => `Error creating a new Tensor. Inferred shape (${inferredShape}) does not match the provided shape (${shape}). `);
      }
    }
    if (!isTypedArray(values) && !Array.isArray(values)) {
      values = [values];
    }
    shape = shape || inferredShape;
    values = dtype !== "string" ? toTypedArray(values, dtype) : flatten(values, [], true);
    return ENGINE.makeTensor(values, shape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/tensor.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tensor5(values, shape, dtype) {
    const inferredShape = inferShape(values, dtype);
    return makeTensor(values, shape, inferredShape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/concat.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function concat_(tensors, axis = 0) {
    assert(tensors.length >= 1, () => "Pass at least one tensor to concat");
    let $tensors = convertToTensorArray(tensors, "tensors", "concat");
    if ($tensors[0].dtype === "complex64") {
      $tensors.forEach((tensor17) => {
        if (tensor17.dtype !== "complex64") {
          throw new Error(`Cannot concatenate complex64 tensors with a tensor
          with dtype ${tensor17.dtype}. `);
        }
      });
    }
    const $axis = parseAxisParam(axis, $tensors[0].shape)[0];
    const outShape = computeOutShape($tensors.map((t) => t.shape), $axis);
    if (sizeFromShape(outShape) === 0) {
      return tensor5([], outShape);
    }
    $tensors = $tensors.filter((t) => t.size > 0);
    if ($tensors.length === 1) {
      return $tensors[0];
    }
    const shapes = $tensors.map((t) => t.shape);
    assertParamsConsistent(shapes, $axis);
    const forward = (backend2, save) => {
      const res = backend2.concat($tensors, $axis);
      save($tensors);
      return res;
    };
    const inputs = $tensors;
    const attr = {axis};
    return ENGINE.runKernelFunc(forward, inputs, null, Concat, attr);
  }
  const concat = op({concat_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/mat_mul.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function matMul_(a, b, transposeA = false, transposeB = false) {
    let $a = convertToTensor(a, "a", "matMul");
    let $b = convertToTensor(b, "b", "matMul");
    [$a, $b] = makeTypesMatch($a, $b);
    assert($a.rank >= 2 && $b.rank >= 2 && $a.rank === $b.rank, () => `Error in matMul: inputs must have the same rank of at least 2, got ranks ${$a.rank} and ${$b.rank}.`);
    const innerShapeA = transposeA ? $a.shape[$a.rank - 2] : $a.shape[$a.rank - 1];
    const innerShapeB = transposeB ? $b.shape[$b.rank - 1] : $b.shape[$b.rank - 2];
    const outerShapeA = transposeA ? $a.shape[$a.rank - 1] : $a.shape[$a.rank - 2];
    const outerShapeB = transposeB ? $b.shape[$b.rank - 2] : $b.shape[$b.rank - 1];
    const outerDimsA = $a.shape.slice(0, -2);
    const outerDimsB = $b.shape.slice(0, -2);
    const batchDimA = sizeFromShape(outerDimsA);
    const batchDimB = sizeFromShape(outerDimsB);
    assert(arraysEqual(outerDimsA, outerDimsB), () => `Error in matMul: outer dimensions (${outerDimsA}) and (${outerDimsB}) of Tensors with shapes ${$a.shape} and ${$b.shape} must match.`);
    assert(innerShapeA === innerShapeB, () => `Error in matMul: inner shapes (${innerShapeA}) and (${innerShapeB}) of Tensors with shapes ${$a.shape} and ${$b.shape} and transposeA=${transposeA} and transposeB=${transposeB} must match.`);
    const outShape = $a.shape.slice(0, -2).concat([outerShapeA, outerShapeB]);
    const a3D = transposeA ? reshape($a, [batchDimA, innerShapeA, outerShapeA]) : reshape($a, [batchDimA, outerShapeA, innerShapeA]);
    const b3D = transposeB ? reshape($b, [batchDimB, outerShapeB, innerShapeB]) : reshape($b, [batchDimB, innerShapeB, outerShapeB]);
    const forward = (backend2, save) => {
      save([a3D, b3D]);
      return backend2.batchMatMul(a3D, b3D, transposeA, transposeB);
    };
    const inputs = {a: a3D, b: b3D};
    const attrs = {transposeA, transposeB};
    const res = ENGINE.runKernelFunc(forward, inputs, null, BatchMatMul, attrs);
    return reshape(res, outShape);
  }
  const matMul = op({matMul_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/mul.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function mul_(a, b) {
    let $a = convertToTensor(a, "a", "mul");
    let $b = convertToTensor(b, "b", "mul");
    [$a, $b] = makeTypesMatch($a, $b);
    const forward = (backend2, save) => {
      const res = backend2.multiply($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, Multiply);
  }
  const mul = op({mul_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/sigmoid.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function sigmoid_(x) {
    const $x = convertToTensor(x, "x", "sigmoid");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.sigmoid($x);
      save([res]);
      return res;
    }, inputs, null, Sigmoid);
  }
  const sigmoid = op({sigmoid_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/slice_util.js
  const slice_util_exports = {};
  __export(slice_util_exports, {
    assertParamsValid: () => assertParamsValid,
    computeFlatOffset: () => computeFlatOffset,
    computeOutShape: () => computeOutShape2,
    isSliceContinous: () => isSliceContinous,
    maskToAxes: () => maskToAxes,
    parseSliceParams: () => parseSliceParams,
    startForAxis: () => startForAxis,
    startIndicesWithElidedDims: () => startIndicesWithElidedDims,
    stopForAxis: () => stopForAxis,
    stopIndicesWithElidedDims: () => stopIndicesWithElidedDims,
    stridesForAxis: () => stridesForAxis,
    stridesWithElidedDims: () => stridesWithElidedDims
  });
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function assertParamsValid(input, begin, size) {
    assert(input.rank === begin.length, () => `Error in slice${input.rank}D: Length of begin ${begin} must match the rank of the array (${input.rank}).`);
    assert(input.rank === size.length, () => `Error in slice${input.rank}D: Length of size ${size} must match the rank of the array (${input.rank}).`);
    for (let i = 0; i < input.rank; ++i) {
      assert(begin[i] + size[i] <= input.shape[i], () => `Error in slice${input.rank}D: begin[${i}] + size[${i}] (${begin[i] + size[i]}) would overflow input.shape[${i}] (${input.shape[i]})`);
    }
  }
  function maskToAxes(mask) {
    const axes = [];
    let axis = 0;
    while (mask > 0) {
      if (mask & 1) {
        axes.push(axis);
      }
      mask /= 2;
      axis++;
    }
    return axes;
  }
  function computeOutShape2(begin, end, strides) {
    const size = [];
    for (let axis = 0; axis < begin.length; axis++) {
      size[axis] = Math.ceil((end[axis] - begin[axis]) / strides[axis]);
    }
    return size;
  }
  function stridesWithElidedDims(strides, ellipsisInsertionIndex, numElidedAxes, inputShape) {
    const newStrides = [...strides];
    for (let i = newStrides.length; i < inputShape.length; i++) {
      newStrides.push(1);
    }
    for (let i = 0; i < numElidedAxes; i++) {
      if (i === 0) {
        newStrides[ellipsisInsertionIndex] = 1;
      } else {
        newStrides.splice(ellipsisInsertionIndex, 0, 1);
        newStrides.pop();
      }
    }
    return newStrides;
  }
  function unnormalizeAxis(ellipsisInsertionIndex, numElidedAxes, normalizedAxis) {
    if (normalizedAxis <= ellipsisInsertionIndex) {
      return normalizedAxis;
    }
    return normalizedAxis - (numElidedAxes - 1);
  }
  function getElidedAxes(numElidedAxes, ellipsisInsertionIndex) {
    const elidedAxes = [];
    for (let i = 0; i < numElidedAxes; i++) {
      elidedAxes.push(ellipsisInsertionIndex + i);
    }
    return elidedAxes;
  }
  function startIndicesWithElidedDims(beginMask, ellipsisInsertionIndex, numElidedAxes, originalBegin, inputShape) {
    const newIndices = [...inputShape];
    const elidedAxes = getElidedAxes(numElidedAxes, ellipsisInsertionIndex);
    for (let axis = 0; axis < newIndices.length; axis++) {
      if (elidedAxes.indexOf(axis) > -1) {
        newIndices[axis] = 0;
      } else {
        const originalAxis = unnormalizeAxis(ellipsisInsertionIndex, numElidedAxes, axis);
        let originalValue = originalBegin[originalAxis];
        if (beginMask & 1 << originalAxis) {
          originalValue = 0;
        }
        newIndices[axis] = originalValue;
      }
    }
    return newIndices;
  }
  function stopIndicesWithElidedDims(endMask, ellipsisInsertionIndex, numElidedAxes, originalEnd, inputShape) {
    const newIndices = [...inputShape];
    const elidedAxes = getElidedAxes(numElidedAxes, ellipsisInsertionIndex);
    for (let axis = 0; axis < newIndices.length; axis++) {
      if (elidedAxes.indexOf(axis) > -1) {
        newIndices[axis] = Number.MAX_SAFE_INTEGER;
      } else {
        const originalAxis = unnormalizeAxis(ellipsisInsertionIndex, numElidedAxes, axis);
        let originalValue = originalEnd[originalAxis];
        if (endMask & 1 << originalAxis) {
          originalValue = Number.MAX_SAFE_INTEGER;
        }
        newIndices[axis] = originalValue;
      }
    }
    for (let i = 0; i < newIndices.length; i++) {
      const axisSize = inputShape[i];
      if (newIndices[i] < 0) {
        newIndices[i] += axisSize;
      }
      newIndices[i] = clamp(0, newIndices[i], inputShape[i]);
    }
    return newIndices;
  }
  function stridesForAxis(strides, axis, ellipsisMask) {
    let stride = strides[axis];
    if (ellipsisMask & 1 << axis || stride == null) {
      stride = 1;
    }
    return stride;
  }
  function startForAxis(beginMask, startIndices, strides, inputShape, axis, ellipsisMask) {
    let start = startIndices[axis];
    const stride = strides[axis] || 1;
    if (beginMask & 1 << axis || ellipsisMask & 1 << axis || start == null) {
      if (stride > 0) {
        start = Number.MIN_SAFE_INTEGER;
      } else {
        start = Number.MAX_SAFE_INTEGER;
      }
    }
    const axisSize = inputShape[axis];
    if (start < 0) {
      start += axisSize;
    }
    start = clamp(0, start, axisSize - 1);
    return start;
  }
  function stopForAxis(endMask, stopIndices, strides, inputShape, axis, ellipsisMask) {
    let stop = stopIndices[axis];
    const stride = strides[axis] || 1;
    if (endMask & 1 << axis || ellipsisMask & 1 << axis || stop == null) {
      if (stride > 0) {
        stop = Number.MAX_SAFE_INTEGER;
      } else {
        stop = Number.MIN_SAFE_INTEGER;
      }
    }
    const axisSize = inputShape[axis];
    if (stop < 0) {
      stop += axisSize;
    }
    if (stride > 0) {
      stop = clamp(0, stop, axisSize);
    } else {
      stop = clamp(-1, stop, axisSize - 1);
    }
    return stop;
  }
  function isSliceContinous(shape, begin, size) {
    let firstNonOneAxis = size.length;
    for (let i = 0; i < size.length; i++) {
      if (size[i] > 1) {
        firstNonOneAxis = i;
        break;
      }
    }
    for (let i = firstNonOneAxis + 1; i < size.length; i++) {
      if (begin[i] > 0 || size[i] !== shape[i]) {
        return false;
      }
    }
    return true;
  }
  function computeFlatOffset(begin, strides) {
    let flatOffset = begin.length > 0 ? begin[begin.length - 1] : 1;
    for (let i = 0; i < begin.length - 1; i++) {
      flatOffset += begin[i] * strides[i];
    }
    return flatOffset;
  }
  function parseSliceParams(x, begin, size) {
    let begin_;
    if (typeof begin === "number") {
      begin_ = [begin, ...new Array(x.rank - 1).fill(0)];
    } else if (begin.length < x.rank) {
      begin_ = begin.concat(new Array(x.rank - begin.length).fill(0));
    } else {
      begin_ = begin.slice();
    }
    begin_.forEach((d) => {
      assert(d !== -1, () => "slice() does not support negative begin indexing.");
    });
    let size_;
    if (size == null) {
      size_ = new Array(x.rank).fill(-1);
    } else if (typeof size === "number") {
      size_ = [size, ...new Array(x.rank - 1).fill(-1)];
    } else if (size.length < x.rank) {
      size_ = size.concat(new Array(x.rank - size.length).fill(-1));
    } else {
      size_ = size;
    }
    size_ = size_.map((d, i) => {
      if (d >= 0) {
        return d;
      } else {
        assert(d === -1, () => `Negative size values should be exactly -1 but got ${d} for the slice() size at index ${i}.`);
        return x.shape[i] - begin_[i];
      }
    });
    return [begin_, size_];
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/slice.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function slice_(x, begin, size) {
    const $x = convertToTensor(x, "x", "slice");
    if ($x.rank === 0) {
      throw new Error("Slicing scalar is not possible");
    }
    const [begin_, size_] = parseSliceParams($x, begin, size);
    assertParamsValid($x, begin_, size_);
    const forward = (backend2, save) => {
      save([$x]);
      return backend2.slice($x, begin_, size_);
    };
    const inputs = {x: $x};
    const attrs = {begin, size};
    return ENGINE.runKernelFunc(forward, inputs, null, Slice, attrs);
  }
  const slice = op({slice_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/tanh.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tanh_(x) {
    const $x = convertToTensor(x, "x", "tanh");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const y = backend2.tanh($x);
      save([y]);
      return y;
    }, inputs, null, Tanh);
  }
  const tanh2 = op({tanh_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/basic_lstm_cell.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function basicLSTMCell_(forgetBias, lstmKernel, lstmBias, data, c, h) {
    const $forgetBias = convertToTensor(forgetBias, "forgetBias", "basicLSTMCell");
    const $lstmKernel = convertToTensor(lstmKernel, "lstmKernel", "basicLSTMCell");
    const $lstmBias = convertToTensor(lstmBias, "lstmBias", "basicLSTMCell");
    const $data = convertToTensor(data, "data", "basicLSTMCell");
    const $c = convertToTensor(c, "c", "basicLSTMCell");
    const $h = convertToTensor(h, "h", "basicLSTMCell");
    const combined = concat([$data, $h], 1);
    const weighted = matMul(combined, $lstmKernel);
    const res = add2(weighted, $lstmBias);
    const batchSize = res.shape[0];
    const sliceCols = res.shape[1] / 4;
    const sliceSize = [batchSize, sliceCols];
    const i = slice(res, [0, 0], sliceSize);
    const j = slice(res, [0, sliceCols], sliceSize);
    const f = slice(res, [0, sliceCols * 2], sliceSize);
    const o = slice(res, [0, sliceCols * 3], sliceSize);
    const newC = add2(mul(sigmoid(i), tanh2(j)), mul($c, sigmoid(add2($forgetBias, f))));
    const newH = mul(tanh2(newC), sigmoid(o));
    return [newC, newH];
  }
  const basicLSTMCell = op({basicLSTMCell_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/batch_to_space_nd.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function batchToSpaceND_(x, blockShape, crops) {
    const $x = convertToTensor(x, "x", "batchToSpaceND");
    const prod3 = blockShape.reduce((a, b) => a * b);
    assert($x.rank >= 1 + blockShape.length, () => `input rank is ${$x.rank} but should be > than blockShape.length ${blockShape.length}`);
    assert(crops.length === blockShape.length, () => `crops.length is ${crops.length} but should be equal to blockShape.length  ${blockShape.length}`);
    assert($x.shape[0] % prod3 === 0, () => `input tensor batch is ${$x.shape[0]} but is not divisible by the product of the elements of blockShape ${blockShape.join(" * ")} === ${prod3}`);
    const forward = (backend2) => {
      return backend2.batchToSpaceND($x, blockShape, crops);
    };
    const inputs = {x: $x};
    const attrs = {blockShape, crops};
    return ENGINE.runKernelFunc(forward, inputs, null, BatchToSpaceND, attrs);
  }
  const batchToSpaceND = op({batchToSpaceND_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/batchnorm_util.js
  function xAs4D(x) {
    let x4D;
    if (x.rank === 0 || x.rank === 1) {
      x4D = reshape(x, [1, 1, 1, x.size]);
    } else if (x.rank === 2) {
      x4D = reshape(x, [1, 1, x.shape[0], x.shape[1]]);
    } else if (x.rank === 3) {
      x4D = reshape(x, [1, x.shape[0], x.shape[1], x.shape[2]]);
    } else {
      x4D = x;
    }
    return x4D;
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/batchnorm.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function batchNorm_(x, mean5, variance, offset, scale2, varianceEpsilon) {
    if (varianceEpsilon == null) {
      varianceEpsilon = 1e-3;
    }
    const $x = convertToTensor(x, "x", "batchNorm");
    const $mean = convertToTensor(mean5, "mean", "batchNorm");
    const $variance = convertToTensor(variance, "variance", "batchNorm");
    let $scale;
    if (scale2 != null) {
      $scale = convertToTensor(scale2, "scale", "batchNorm");
    }
    let $offset;
    if (offset != null) {
      $offset = convertToTensor(offset, "offset", "batchNorm");
    }
    assert($mean.rank === $variance.rank, () => "Batch normalization gradient requires mean and variance to have equal ranks.");
    assert($offset == null || $mean.rank === $offset.rank, () => "Batch normalization gradient requires mean and offset to have equal ranks.");
    assert($scale == null || $mean.rank === $scale.rank, () => "Batch normalization gradient requires mean and scale to have equal ranks.");
    const x4D = xAs4D($x);
    const forward = (backend2, save) => {
      save([x4D, $mean, $variance, $scale]);
      return backend2.batchNorm(x4D, as1DOr4D($mean), as1DOr4D($variance), as1DOr4D($offset), as1DOr4D($scale), varianceEpsilon);
    };
    const inputs = {
      x: x4D,
      scale: $scale,
      offset: $offset,
      mean: $mean,
      variance: $variance
    };
    const attrs = {varianceEpsilon};
    const res = ENGINE.runKernelFunc(forward, inputs, null, FusedBatchNorm, attrs);
    return reshape(res, $x.shape);
  }
  function as1DOr4D(x) {
    if (x == null) {
      return null;
    }
    if (x.rank === 0) {
      return reshape(x, [x.size]);
    } else if (x.rank === 1) {
      return x;
    } else if (x.rank === 2) {
      return reshape(x, [1, 1, x.shape[0], x.shape[1]]);
    } else if (x.rank === 3) {
      return reshape(x, [1, x.shape[0], x.shape[1], x.shape[2]]);
    }
    return x;
  }
  const batchNorm = op({batchNorm_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/batchnorm2d.js
  function batchNorm2d_(x, mean5, variance, offset, scale2, varianceEpsilon) {
    const $x = convertToTensor(x, "x", "batchNorm");
    const $mean = convertToTensor(mean5, "mean", "batchNorm");
    const $variance = convertToTensor(variance, "variance", "batchNorm");
    let $scale;
    if (scale2 != null) {
      $scale = convertToTensor(scale2, "scale", "batchNorm");
    }
    let $offset;
    if (offset != null) {
      $offset = convertToTensor(offset, "offset", "batchNorm");
    }
    assert($x.rank === 2, () => `Error in batchNorm2D: x must be rank 2 but got rank ${$x.rank}.`);
    assert($mean.rank === 2 || $mean.rank === 1, () => `Error in batchNorm2D: mean must be rank 2 or rank 1 but got rank ${$mean.rank}.`);
    assert($variance.rank === 2 || $variance.rank === 1, () => `Error in batchNorm2D: variance must be rank 2 or rank 1 but got rank ${$variance.rank}.`);
    if ($scale != null) {
      assert($scale.rank === 2 || $scale.rank === 1, () => `Error in batchNorm2D: scale must be rank 2 or rank 1 but got rank ${$scale.rank}.`);
    }
    if ($offset != null) {
      assert($offset.rank === 2 || $offset.rank === 1, () => `Error in batchNorm2D: offset must be rank 2 or rank 1 but got rank ${$offset.rank}.`);
    }
    return batchNorm($x, $mean, $variance, $offset, $scale, varianceEpsilon);
  }
  const batchNorm2d = op({batchNorm2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/batchnorm3d.js
  function batchNorm3d_(x, mean5, variance, offset, scale2, varianceEpsilon) {
    const $x = convertToTensor(x, "x", "batchNorm");
    const $mean = convertToTensor(mean5, "mean", "batchNorm");
    const $variance = convertToTensor(variance, "variance", "batchNorm");
    let $scale;
    if (scale2 != null) {
      $scale = convertToTensor(scale2, "scale", "batchNorm");
    }
    let $offset;
    if (offset != null) {
      $offset = convertToTensor(offset, "offset", "batchNorm");
    }
    assert($x.rank === 3, () => `Error in batchNorm3D: x must be rank 3 but got rank ${$x.rank}.`);
    assert($mean.rank === 3 || $mean.rank === 1, () => `Error in batchNorm3D: mean must be rank 3 or rank 1 but got rank ${$mean.rank}.`);
    assert($variance.rank === 3 || $variance.rank === 1, () => `Error in batchNorm3D: variance must be rank 3 or rank 1 but got rank ${$variance.rank}.`);
    if ($scale != null) {
      assert($scale.rank === 3 || $scale.rank === 1, () => `Error in batchNorm3D: scale must be rank 3 or rank 1 but got rank ${$scale.rank}.`);
    }
    if ($offset != null) {
      assert($offset.rank === 3 || $offset.rank === 1, () => `Error in batchNorm3D: offset must be rank 3 or rank 1 but got rank ${$offset.rank}.`);
    }
    return batchNorm($x, $mean, $variance, $offset, $scale, varianceEpsilon);
  }
  const batchNorm3d = op({batchNorm3d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/batchnorm4d.js
  function batchNorm4d_(x, mean5, variance, offset, scale2, varianceEpsilon) {
    const $x = convertToTensor(x, "x", "batchNorm");
    const $mean = convertToTensor(mean5, "mean", "batchNorm");
    const $variance = convertToTensor(variance, "variance", "batchNorm");
    let $scale;
    if (scale2 != null) {
      $scale = convertToTensor(scale2, "scale", "batchNorm");
    }
    let $offset;
    if (offset != null) {
      $offset = convertToTensor(offset, "offset", "batchNorm");
    }
    assert($x.rank === 4, () => `Error in batchNorm4D: x must be rank 4 but got rank ${$x.rank}.`);
    assert($mean.rank === 4 || $mean.rank === 1, () => `Error in batchNorm4D: mean must be rank 4 or rank 1 but got rank ${$mean.rank}.`);
    assert($variance.rank === 4 || $variance.rank === 1, () => `Error in batchNorm4D: variance must be rank 4 or rank 1 but got rank ${$variance.rank}.`);
    if ($scale != null) {
      assert($scale.rank === 4 || $scale.rank === 1, () => `Error in batchNorm4D: scale must be rank 4 or rank 1 but got rank ${$scale.rank}.`);
    }
    if ($offset != null) {
      assert($offset.rank === 4 || $offset.rank === 1, () => `Error in batchNorm4D: offset must be rank 4 or rank 1 but got rank ${$offset.rank}.`);
    }
    return batchNorm($x, $mean, $variance, $offset, $scale, varianceEpsilon);
  }
  const batchNorm4d = op({batchNorm4d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/clone.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function clone_(x) {
    const $x = convertToTensor(x, "x", "clone", null);
    const forward = () => ENGINE.makeTensorFromDataId($x.dataId, $x.shape, $x.dtype);
    const inputs = {x: $x};
    return ENGINE.runKernelFunc(forward, inputs, null, Identity);
  }
  const clone = op({clone_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/broadcast_to.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function broadcastTo_(x, shape) {
    let input = convertToTensor(x, "broadcastTo", "x");
    const xShape = input.shape;
    if (shape.some((d) => !(d > 0) || d % 1 !== 0)) {
      throw new Error(`broadcastTo(): Invalid broadcast shape [${shape}].`);
    }
    if (shape.length < input.rank) {
      throw new Error(`broadcastTo(): shape.length=${shape.length} < input.rank=${input.rank}.`);
    }
    if (shape.length > input.rank) {
      const newShape = input.shape.slice();
      while (newShape.length < shape.length) {
        newShape.unshift(1);
      }
      input = reshape(input, newShape);
    }
    const inputShape = input.shape;
    const reps = Array.from(shape);
    for (let i = shape.length - 1; i >= 0; i--) {
      if (inputShape[i] === shape[i]) {
        reps[i] = 1;
      } else if (input.shape[i] !== 1) {
        throw new Error(`broadcastTo(): [${xShape}] cannot be broadcast to [${shape}].`);
      }
    }
    const axes = reps.map((n, i) => n > 1 ? i : -1).filter((i) => i >= 0);
    if (axes.length === 0) {
      return clone(input);
    }
    const forward = (backend2) => backend2.tile(input, reps);
    const inputs = {x: input};
    const attrs = {shape, inputShape};
    return ENGINE.runKernelFunc(forward, inputs, null, BroadcastTo, attrs);
  }
  const broadcastTo = op({broadcastTo_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/buffer.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function buffer(shape, dtype = "float32", values) {
    dtype = dtype || "float32";
    assertNonNegativeIntegerDimensions(shape);
    return new TensorBuffer(shape, dtype, values);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/ceil.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function ceil_(x) {
    const $x = convertToTensor(x, "x", "ceil");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2) => backend2.ceil($x), inputs, null, Ceil);
  }
  const ceil = op({ceil_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/clip_by_value.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function clipByValue_(x, clipValueMin, clipValueMax) {
    const $x = convertToTensor(x, "x", "clipByValue");
    assert(clipValueMin <= clipValueMax, () => `Error in clip: min (${clipValueMin}) must be less than or equal to max (${clipValueMax}).`);
    const inputs = {x: $x};
    const attrs = {clipValueMin, clipValueMax};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.clip($x, clipValueMin, clipValueMax);
      save([$x]);
      return res;
    }, inputs, null, ClipByValue, attrs);
  }
  const clipByValue = op({clipByValue_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/complex.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function complex_(real6, imag6) {
    const $real = convertToTensor(real6, "real", "complex");
    const $imag = convertToTensor(imag6, "imag", "complex");
    assertShapesMatch($real.shape, $imag.shape, `real and imag shapes, ${$real.shape} and ${$imag.shape}, must match in call to tf.complex().`);
    const forward = (backend2) => {
      return backend2.complex($real, $imag);
    };
    const inputs = {real: $real, imag: $imag};
    return ENGINE.runKernelFunc(forward, inputs, null, Complex);
  }
  const complex = op({complex_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/concat_1d.js
  function concat1d_(tensors) {
    return concat(tensors, 0);
  }
  const concat1d = op({concat1d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/concat_2d.js
  function concat2d_(tensors, axis) {
    return concat(tensors, axis);
  }
  const concat2d = op({concat2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/concat_3d.js
  function concat3d_(tensors, axis) {
    return concat(tensors, axis);
  }
  const concat3d = op({concat3d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/concat_4d.js
  function concat4d_(tensors, axis) {
    return concat(tensors, axis);
  }
  const concat4d = op({concat4d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/conv2d.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function conv2d_(x, filter, strides, pad8, dataFormat = "NHWC", dilations = [1, 1], dimRoundingMode) {
    const $x = convertToTensor(x, "x", "conv2d");
    const $filter = convertToTensor(filter, "filter", "conv2d");
    let x4D = $x;
    let reshapedTo4D = false;
    if ($x.rank === 3) {
      reshapedTo4D = true;
      x4D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2]]);
    }
    assert(x4D.rank === 4, () => `Error in conv2d: input must be rank 4, but got rank ${x4D.rank}.`);
    assert($filter.rank === 4, () => `Error in conv2d: filter must be rank 4, but got rank ${$filter.rank}.`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in conv2d: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const inDepth = dataFormat === "NHWC" ? x4D.shape[3] : x4D.shape[1];
    assert(inDepth === $filter.shape[2], () => `Error in conv2d: depth of input (${inDepth}) must match input depth for filter ${$filter.shape[2]}.`);
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in conv2D: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    const forward = (backend2, save) => {
      const $dataFormat = convertConv2DDataFormat(dataFormat);
      const convInfo = computeConv2DInfo(x4D.shape, $filter.shape, strides, dilations, pad8, dimRoundingMode, false, $dataFormat);
      const res2 = backend2.conv2d(x4D, $filter, convInfo);
      save([x4D, $filter]);
      return res2;
    };
    const inputs = {x: x4D, filter: $filter};
    const attrs = {strides, pad: pad8, dataFormat, dilations, dimRoundingMode};
    const res = ENGINE.runKernelFunc(forward, inputs, null, Conv2D, attrs);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const conv2d = op({conv2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/conv1d.js
  function conv1d_(x, filter, stride, pad8, dataFormat = "NWC", dilation = 1, dimRoundingMode) {
    const $x = convertToTensor(x, "x", "conv1d");
    const $filter = convertToTensor(filter, "filter", "conv1d");
    let x3D = $x;
    let reshapedTo3D = false;
    if ($x.rank === 2) {
      reshapedTo3D = true;
      x3D = reshape($x, [1, $x.shape[0], $x.shape[1]]);
    }
    assert(x3D.rank === 3, () => `Error in conv1d: input must be rank 3, but got rank ${x3D.rank}.`);
    assert($filter.rank === 3, () => `Error in conv1d: filter must be rank 3, but got rank ${$filter.rank}.`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in conv1d: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    assert(x3D.shape[2] === $filter.shape[1], () => `Error in conv1d: depth of input (${x3D.shape[2]}) must match input depth for filter ${$filter.shape[1]}.`);
    assert(eitherStridesOrDilationsAreOne(stride, dilation), () => `Error in conv1D: Either stride or dilation must be 1. Got stride ${stride} and dilation '${dilation}'`);
    assert(dataFormat === "NWC", () => `Error in conv1d: got dataFormat of ${dataFormat} but only NWC is currently supported.`);
    const filter4D = reshape($filter, [1, $filter.shape[0], $filter.shape[1], $filter.shape[2]]);
    const input4D = reshape(x3D, [x3D.shape[0], 1, x3D.shape[1], x3D.shape[2]]);
    const strides = [1, stride];
    const dilations = [1, dilation];
    const conv2dDataFormat = "NHWC";
    const res = conv2d(input4D, filter4D, strides, pad8, conv2dDataFormat, dilations, dimRoundingMode);
    if (reshapedTo3D) {
      return reshape(res, [res.shape[2], res.shape[3]]);
    }
    return reshape(res, [res.shape[0], res.shape[2], res.shape[3]]);
  }
  const conv1d = op({conv1d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/conv2d_backprop_input.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function conv2DBackpropInput_(xShape, dy, filter, strides, pad8, dataFormat = "NHWC", dimRoundingMode) {
    assert(xShape.length === dy.rank, () => `Length of inShape (${xShape.length}) and rank of dy (${dy.rank}) must match`);
    let xShape4D = xShape;
    let dy4D = dy;
    let reshapedTo4D = false;
    if (dy.rank === 3) {
      reshapedTo4D = true;
      dy4D = reshape(dy, [1, dy.shape[0], dy.shape[1], dy.shape[2]]);
      xShape4D = [1, xShape[0], xShape[1], xShape[2]];
    }
    assert(xShape4D.length === 4, () => `Error in conv2dDerInput: inShape must be length 4, but got length ${xShape4D.length}.`);
    assert(dy4D.rank === 4, () => `Error in conv2dDerInput: dy must be rank 4, but got rank ${dy4D.rank}`);
    assert(filter.rank === 4, () => `Error in conv2dDerInput: filter must be rank 4, but got rank ${filter.rank}`);
    const inDepth = dataFormat === "NHWC" ? xShape4D[3] : xShape4D[1];
    const outDepth = dataFormat === "NHWC" ? dy4D.shape[3] : dy4D.shape[1];
    assert(inDepth === filter.shape[2], () => `Error in conv2dDerInput: depth of input (${inDepth}) must match input depth for filter ${filter.shape[2]}.`);
    assert(outDepth === filter.shape[3], () => `Error in conv2dDerInput: depth of output (${outDepth}) must match output depth for filter ${filter.shape[3]}.`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in conv2dDerInput: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const forward = (backend2, save) => {
      const dilations = 1;
      const $dataFormat = convertConv2DDataFormat(dataFormat);
      const convInfo = computeConv2DInfo(xShape4D, filter.shape, strides, dilations, pad8, dimRoundingMode, false, $dataFormat);
      const res2 = backend2.conv2dDerInput(dy4D, filter, convInfo);
      save([dy4D, filter]);
      return res2;
    };
    const inputs = {dy: dy4D, filter};
    const attrs = {strides, pad: pad8, dataFormat, dimRoundingMode, inputShape: xShape4D};
    const res = ENGINE.runKernelFunc(forward, inputs, null, Conv2DBackpropInput, attrs);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const conv2DBackpropInput = op({conv2DBackpropInput_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/conv2d_transpose.js
  function conv2dTranspose_(x, filter, outputShape, strides, pad8, dimRoundingMode) {
    const $x = convertToTensor(x, "x", "conv2dTranspose");
    const $filter = convertToTensor(filter, "filter", "conv2dTranspose");
    return conv2DBackpropInput(outputShape, $x, $filter, strides, pad8, "NHWC", dimRoundingMode);
  }
  const conv2dTranspose = op({conv2dTranspose_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/conv3d.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function conv3d_(x, filter, strides, pad8, dataFormat = "NDHWC", dilations = [1, 1, 1]) {
    const $x = convertToTensor(x, "x", "conv3d");
    const $filter = convertToTensor(filter, "filter", "conv3d");
    let x5D = $x;
    let reshapedTo5D = false;
    if ($x.rank === 4) {
      reshapedTo5D = true;
      x5D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2], $x.shape[3]]);
    }
    assert(x5D.rank === 5, () => `Error in conv3d: input must be rank 5, but got rank ${x5D.rank}.`);
    assert($filter.rank === 5, () => `Error in conv3d: filter must be rank 5, but got rank ${$filter.rank}.`);
    assert(x5D.shape[4] === $filter.shape[3], () => `Error in conv3d: depth of input (${x5D.shape[4]}) must match input depth for filter ${$filter.shape[3]}.`);
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in conv3D: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    assert(dataFormat === "NDHWC", () => `Error in conv3d: got dataFormat of ${dataFormat} but only NDHWC is currently supported.`);
    const forward = (backend2, save) => {
      const convInfo = computeConv3DInfo(x5D.shape, $filter.shape, strides, dilations, pad8);
      const res2 = backend2.conv3d(x5D, $filter, convInfo);
      save([x5D, $filter]);
      return res2;
    };
    const inputs = {x: x5D, filter: $filter};
    const attrs = {strides, pad: pad8, dataFormat, dilations};
    const res = ENGINE.runKernelFunc(forward, inputs, null, Conv3D, attrs);
    if (reshapedTo5D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3], res.shape[4]]);
    }
    return res;
  }
  const conv3d = op({conv3d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/conv3d_backprop_input.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function conv3DBackpropInput_(xShape, dy, filter, strides, pad8) {
    assert(xShape.length === dy.rank, () => `Length of inShape (${xShape.length}) and rank of dy (${dy.rank}) must match`);
    let xShape5D = xShape;
    let dy5D = dy;
    let reshapedTo5D = false;
    if (dy.rank === 4) {
      reshapedTo5D = true;
      dy5D = reshape(dy, [1, dy.shape[0], dy.shape[1], dy.shape[2], dy.shape[3]]);
      xShape5D = [1, xShape[0], xShape[1], xShape[2], xShape[3]];
    }
    const inDepth = xShape5D[4];
    const outDepth = dy5D.shape[4];
    assert(xShape5D.length === 5, () => `Error in conv3dDerInput: inShape must be length 5, but got length ${xShape5D.length}.`);
    assert(dy5D.rank === 5, () => `Error in conv3dDerInput: dy must be rank 5, but got rank ${dy5D.rank}`);
    assert(filter.rank === 5, () => `Error in conv3dDerInput: filter must be rank 5, but got rank ${filter.rank}`);
    assert(inDepth === filter.shape[3], () => `Error in conv3dDerInput: depth of input (${inDepth}) must match input depth for filter ${filter.shape[3]}.`);
    assert(outDepth === filter.shape[4], () => `Error in conv3dDerInput: depth of output (${outDepth}) must match output depth for filter ${filter.shape[4]}.`);
    const forward = (backend2) => {
      const dilations = 1;
      const convInfo = computeConv3DInfo(xShape5D, filter.shape, strides, dilations, pad8);
      return backend2.conv3dDerInput(dy5D, filter, convInfo);
    };
    const inputs = {dy: dy5D};
    const attrs = {pad: pad8};
    const res = ENGINE.runKernelFunc(forward, inputs, null, Conv3DBackpropInputV2, attrs);
    if (reshapedTo5D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3], res.shape[4]]);
    }
    return res;
  }
  const conv3DBackpropInput = op({conv3DBackpropInput_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/conv3d_transpose.js
  function conv3dTranspose_(x, filter, outputShape, strides, pad8) {
    const $x = convertToTensor(x, "x", "conv3dTranspose");
    const $filter = convertToTensor(filter, "filter", "conv3dTranspose");
    return conv3DBackpropInput(outputShape, $x, $filter, strides, pad8);
  }
  const conv3dTranspose = op({conv3dTranspose_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/cos.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function cos_(x) {
    const $x = convertToTensor(x, "x", "cos");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.cos($x);
      save([$x]);
      return res;
    }, inputs, null, Cos);
  }
  const cos = op({cos_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/cosh.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function cosh_(x) {
    const $x = convertToTensor(x, "x", "cosh");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.cosh($x);
      save([$x]);
      return res;
    }, inputs, null, Cosh);
  }
  const cosh = op({cosh_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/cumsum.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function cumsum_(x, axis = 0, exclusive = false, reverse9 = false) {
    const $x = convertToTensor(x, "x", "cumsum");
    const forward = (backend2, save) => {
      const permutation = getAxesPermutation([axis], $x.rank);
      let permutedX = $x;
      if (permutation != null) {
        permutedX = transpose($x, permutation);
      }
      const permutedAxis = getInnerMostAxes(1, $x.rank)[0];
      let value = backend2.cumsum(permutedX, permutedAxis, exclusive, reverse9);
      save([$x]);
      if (permutation != null) {
        const reversePermutation = getUndoAxesPermutation(permutation);
        value = transpose(value, reversePermutation);
      }
      return value;
    };
    const inputs = {x: $x};
    const attrs = {axis, exclusive, reverse: reverse9};
    return ENGINE.runKernelFunc(forward, inputs, null, Cumsum, attrs);
  }
  const cumsum = op({cumsum_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/depth_to_space.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function depthToSpace_(x, blockSize, dataFormat = "NHWC") {
    const $x = convertToTensor(x, "x", "depthToSpace");
    const inputHeight = dataFormat === "NHWC" ? $x.shape[1] : $x.shape[2];
    const inputWidth = dataFormat === "NHWC" ? $x.shape[2] : $x.shape[3];
    const inputDepth = dataFormat === "NHWC" ? $x.shape[3] : $x.shape[1];
    assert(inputHeight * blockSize >= 0, () => `Negative dimension size caused by overflow when multiplying
    ${inputHeight} and ${blockSize}  for depthToSpace with input shape
    ${$x.shape}`);
    assert(inputWidth * blockSize >= 0, () => `Negative dimension size caused by overflow when multiplying
    ${inputWidth} and ${blockSize} for depthToSpace with input shape
        ${$x.shape}`);
    assert(inputDepth % (blockSize * blockSize) === 0, () => `Dimension size must be evenly divisible by ${blockSize * blockSize} but is ${inputDepth} for depthToSpace with input shape ${$x.shape}`);
    const forward = (backend2) => backend2.depthToSpace($x, blockSize, dataFormat);
    const inputs = {x: $x};
    const attrs = {blockSize, dataFormat};
    return ENGINE.runKernelFunc(forward, inputs, null, DepthToSpace, attrs);
  }
  const depthToSpace = op({depthToSpace_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/depthwise_conv2d.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function depthwiseConv2d_(x, filter, strides, pad8, dataFormat = "NHWC", dilations = [1, 1], dimRoundingMode) {
    const $x = convertToTensor(x, "x", "depthwiseConv2d");
    const $filter = convertToTensor(filter, "filter", "depthwiseConv2d");
    let x4D = $x;
    let reshapedTo4D = false;
    if ($x.rank === 3) {
      reshapedTo4D = true;
      x4D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2]]);
    }
    assert(x4D.rank === 4, () => `Error in depthwiseConv2d: input must be rank 4, but got rank ${x4D.rank}.`);
    assert($filter.rank === 4, () => `Error in depthwiseConv2d: filter must be rank 4, but got rank ${$filter.rank}.`);
    assert(x4D.shape[3] === $filter.shape[2], () => `Error in depthwiseConv2d: number of input channels (${x4D.shape[3]}) must match the inChannels dimension in filter ${$filter.shape[2]}.`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in depthwiseConv2d: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const forward = (backend2, save) => {
      if (dilations == null) {
        dilations = [1, 1];
      }
      assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in depthwiseConv2d: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
      const convInfo = computeConv2DInfo(x4D.shape, $filter.shape, strides, dilations, pad8, dimRoundingMode, true);
      const res2 = backend2.depthwiseConv2D(x4D, $filter, convInfo);
      save([x4D, $filter]);
      return res2;
    };
    const inputs = {x: x4D, filter: $filter};
    const attrs = {strides, pad: pad8, dataFormat, dilations, dimRoundingMode};
    const res = ENGINE.runKernelFunc(forward, inputs, null, DepthwiseConv2dNative, attrs);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const depthwiseConv2d = op({depthwiseConv2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/diag.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function diag_(x) {
    const $x = convertToTensor(x, "x", "diag");
    const forward = (backend2) => {
      const flat = reshape($x, [$x.size]);
      const result = backend2.diag(flat);
      const outShape = [...x.shape, ...x.shape];
      return reshape(result, outShape);
    };
    const inputs = {x: $x};
    return ENGINE.runKernelFunc(forward, inputs, null, Diag);
  }
  const diag = op({diag_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/dilation2d.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function dilation2d_(x, filter, strides, pad8, dilations = [1, 1], dataFormat = "NHWC") {
    const $x = convertToTensor(x, "x", "dilation2d");
    const $filter = convertToTensor(filter, "filter", "dilation2d");
    assert($x.rank === 3 || $x.rank === 4, () => `Error in dilation2d: input must be rank 3 or 4, but got rank ${$x.rank}.`);
    assert($filter.rank === 3, () => `Error in dilation2d: filter must be rank 3, but got rank ${$filter.rank}.`);
    assert(dataFormat === "NHWC", () => `Error in dilation2d: Only NHWC is currently supported, but got dataFormat of ${dataFormat}`);
    let x4D = $x;
    let reshapedTo4D = false;
    if ($x.rank === 3) {
      x4D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2]]);
      reshapedTo4D = true;
    }
    const inputs = {x: x4D, filter: $filter};
    const attrs = {strides, pad: pad8, dilations};
    const res = ENGINE.runKernel(Dilation2D, inputs, attrs);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const dilation2d = op({dilation2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/floorDiv.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function floorDiv_(a, b) {
    let $a = convertToTensor(a, "a", "floorDiv");
    let $b = convertToTensor(b, "b", "floorDiv");
    [$a, $b] = makeTypesMatch($a, $b);
    const forward = (backend2, save) => {
      const res = backend2.floorDiv($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, FloorDiv);
  }
  const floorDiv = op({floorDiv_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/div.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function div_(a, b) {
    let $a = convertToTensor(a, "a", "div");
    let $b = convertToTensor(b, "b", "div");
    [$a, $b] = makeTypesMatch($a, $b);
    if ($a.dtype === "int32" && $b.dtype === "int32") {
      return floorDiv($a, $b);
    }
    const forward = (backend2, save) => {
      const res = backend2.realDivide($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    const attrs = {};
    return ENGINE.runKernelFunc(forward, inputs, null, Div, attrs);
  }
  const div = op({div_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/broadcast_util.js
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function getBroadcastDims(inShape, outShape) {
    const inRank = inShape.length;
    const dims = [];
    for (let i = 0; i < inRank; i++) {
      const dim = inRank - 1 - i;
      const a = inShape[dim] || 1;
      const b = outShape[outShape.length - 1 - i] || 1;
      if (b > 1 && a === 1) {
        dims.unshift(dim);
      }
    }
    return dims;
  }
  function getReductionAxes(inShape, outShape) {
    const result = [];
    for (let i = 0; i < outShape.length; i++) {
      const inDim = inShape[inShape.length - i - 1];
      const outAxis = outShape.length - i - 1;
      const outDim = outShape[outAxis];
      if (inDim == null || inDim === 1 && outDim > 1) {
        result.unshift(outAxis);
      }
    }
    return result;
  }
  function assertAndGetBroadcastShape(shapeA, shapeB) {
    const result = [];
    const l = Math.max(shapeA.length, shapeB.length);
    for (let i = 0; i < l; i++) {
      let a = shapeA[shapeA.length - i - 1];
      if (a == null) {
        a = 1;
      }
      let b = shapeB[shapeB.length - i - 1];
      if (b == null) {
        b = 1;
      }
      if (a === 1) {
        result.unshift(b);
      } else if (b === 1) {
        result.unshift(a);
      } else if (a !== b) {
        const errMsg = `Operands could not be broadcast together with shapes ${shapeA} and ${shapeB}.`;
        throw Error(errMsg);
      } else {
        result.unshift(a);
      }
    }
    return result;
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/equal.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function equal_(a, b) {
    let $a = convertToTensor(a, "a", "equal");
    let $b = convertToTensor(b, "b", "equal");
    [$a, $b] = makeTypesMatch($a, $b);
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const forward = (backend2) => backend2.equal($a, $b);
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, Equal);
  }
  const equal = op({equal_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/where.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function where_(condition, a, b) {
    const $a = convertToTensor(a, "a", "where");
    const $b = convertToTensor(b, "b", "where");
    const $condition = convertToTensor(condition, "condition", "where", "bool");
    const broadcastShape = assertAndGetBroadcastShape($a.shape, $b.shape);
    const $broadcastedA = broadcastTo($a, broadcastShape);
    const $broadcastedB = broadcastTo($b, broadcastShape);
    if ($condition.rank === 1) {
      assert($condition.shape[0] === $a.shape[0], () => "The first dimension of `a` must match the size of `condition`.");
    }
    if ($condition.rank !== 1) {
      assertShapesMatch($condition.shape, $broadcastedB.shape, "Error in where: ");
    }
    const forward = (backend2, save) => {
      const res = backend2.select($condition, $broadcastedA, $broadcastedB);
      save([$condition]);
      return res;
    };
    const inputs = {
      condition: $condition,
      t: $broadcastedA,
      e: $broadcastedB
    };
    return ENGINE.runKernelFunc(forward, inputs, null, SelectV2);
  }
  const where = op({where_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/zeros_like.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function zerosLike_(x) {
    const $x = convertToTensor(x, "x", "zerosLike");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2) => backend2.zerosLike($x), inputs, null, ZerosLike);
  }
  const zerosLike = op({zerosLike_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/div_no_nan.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function divNoNan_(a, b) {
    let $a = convertToTensor(a, "a", "div");
    let $b = convertToTensor(b, "b", "div");
    [$a, $b] = makeTypesMatch($a, $b);
    const divResult = div($a, $b);
    const zeros9 = zerosLike(divResult);
    const bEqualsZero = equal($b, zeros9);
    return where(bEqualsZero, zeros9, divResult);
  }
  const divNoNan = op({divNoNan_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/dot.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function dot_(t1, t2) {
    const $t1 = convertToTensor(t1, "t1", "dot");
    const $t2 = convertToTensor(t2, "t2", "dot");
    assert(($t1.rank === 1 || $t1.rank === 2) && ($t2.rank === 1 || $t2.rank === 2), () => `Error in dot: inputs must all be rank 1 or 2, but got ranks ${$t1.rank} and ${$t2.rank}.`);
    const t1Inner = $t1.rank === 1 ? $t1.size : $t1.shape[1];
    const t2Inner = $t2.rank === 1 ? $t2.size : $t2.shape[0];
    assert(t1Inner === t2Inner, () => `Error in dot: inner dimensions of inputs must match, but got ${t1Inner} and ${t2Inner}.`);
    if ($t1.rank === 1 && $t2.rank === 1) {
      const t12D = reshape($t1, [1, -1]);
      const t22D = reshape($t2, [-1, 1]);
      const t1t2 = matMul(t12D, t22D);
      return reshape(t1t2, []);
    } else if ($t1.rank === 1 && $t2.rank === 2) {
      const t12D = reshape($t1, [1, -1]);
      const t22D = reshape($t2, [$t2.shape[0], $t2.shape[1]]);
      const t1t2 = matMul(t12D, t22D);
      return reshape(t1t2, [t1t2.size]);
    } else if ($t1.rank === 2 && $t2.rank === 1) {
      const t22D = reshape($t2, [-1, 1]);
      const t1t2 = matMul($t1, t22D);
      return reshape(t1t2, [t1t2.size]);
    } else {
      const t22D = reshape($t2, [$t2.shape[0], $t2.shape[1]]);
      const t1t2 = matMul($t1, t22D);
      return t1t2;
    }
  }
  const dot = op({dot_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/elu.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function elu_(x) {
    const $x = convertToTensor(x, "x", "elu");
    const forward = (backend2, save) => {
      const y = backend2.elu($x);
      save([y]);
      return y;
    };
    const inputs = {x: $x};
    return ENGINE.runKernelFunc(forward, inputs, null, Elu);
  }
  const elu = op({elu_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/erf.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function erf_(x) {
    let $x = convertToTensor(x, "x", "erf");
    assert($x.dtype === "int32" || $x.dtype === "float32", () => "Input dtype must be `int32` or `float32`.");
    if ($x.dtype === "int32") {
      $x = cast($x, "float32");
    }
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.erf($x);
      save([$x]);
      return res;
    }, inputs, null, Erf);
  }
  const erf = op({erf_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/exp.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function exp_(x) {
    const $x = convertToTensor(x, "x", "exp");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.exp($x);
      save([res]);
      return res;
    }, inputs, null, Exp);
  }
  const exp = op({exp_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/expand_dims.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function expandDims_(x, axis = 0) {
    const parseAs = null;
    const $x = convertToTensor(x, "x", "expandDims", parseAs);
    assert(axis <= $x.rank, () => "Axis must be <= rank of the tensor");
    const newShape = $x.shape.slice();
    if (axis < 0) {
      assert(-($x.rank + 1) <= axis, () => `Axis must be in the interval [${-($x.rank + 1)}, ${$x.rank}]`);
      axis = $x.rank + axis + 1;
    }
    newShape.splice(axis, 0, 1);
    return reshape($x, newShape);
  }
  const expandDims = op({expandDims_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/expm1.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function expm1_(x) {
    const $x = convertToTensor(x, "x", "expm1");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.expm1($x);
      save([$x]);
      return res;
    }, inputs, null, Expm1);
  }
  const expm1 = op({expm1_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/tile.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tile_(x, reps) {
    const parseAs = null;
    const $x = convertToTensor(x, "x", "tile", parseAs);
    assert($x.rank === reps.length, () => `Error in transpose: rank of input ${$x.rank} must match length of reps ${reps}.`);
    const forward = (backend2, save) => {
      const res = backend2.tile($x, reps);
      save([$x]);
      return res;
    };
    const inputsToSave = [$x];
    const inputs = {x: $x};
    const attrs = {reps};
    return ENGINE.runKernelFunc(forward, inputs, null, Tile, attrs, inputsToSave);
  }
  const tile = op({tile_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/eye.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function eye_(numRows, numColumns, batchShape, dtype = "float32") {
    if (numColumns == null) {
      numColumns = numRows;
    }
    const buff = buffer([numRows, numColumns], dtype);
    const n = numRows <= numColumns ? numRows : numColumns;
    for (let i = 0; i < n; ++i) {
      buff.set(1, i, i);
    }
    const out = reshape(buff.toTensor(), [numRows, numColumns]);
    if (batchShape == null) {
      return out;
    } else {
      if (batchShape.length === 1) {
        return tile(expandDims(out, 0), [batchShape[0], 1, 1]);
      } else if (batchShape.length === 2) {
        return tile(expandDims(expandDims(out, 0), 0), [batchShape[0], batchShape[1], 1, 1]);
      } else if (batchShape.length === 3) {
        return tile(expandDims(expandDims(expandDims(out, 0), 0), 0), [
          batchShape[0],
          batchShape[1],
          batchShape[2],
          1,
          1
        ]);
      } else {
        throw new Error(`eye() currently supports only 1D and 2D batchShapes, but received ${batchShape.length}D.`);
      }
    }
  }
  const eye = op({eye_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/fft.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function fft_(input) {
    assert(input.dtype === "complex64", () => `The dtype for tf.spectral.fft() must be complex64 but got ${input.dtype}.`);
    const inputs = {input};
    return ENGINE.runKernelFunc((backend2) => {
      const innerDimensionSize = input.shape[input.shape.length - 1];
      const batch = input.size / innerDimensionSize;
      const input2D = input.as2D(batch, innerDimensionSize);
      const result = backend2.fft(input2D);
      return result.reshape(input.shape);
    }, inputs, null, FFT);
  }
  const fft = op({fft_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/fill.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function fill(shape, value, dtype) {
    const attrs = {shape, value, dtype};
    return ENGINE.runKernelFunc((backend2) => backend2.fill(shape, value, dtype), {}, null, Fill, attrs);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/floor.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function floor_(x) {
    const $x = convertToTensor(x, "x", "floor");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2) => backend2.floor($x), inputs, null, Floor);
  }
  const floor = op({floor_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/reduce_util.js
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const PARALLELIZE_THRESHOLD = 30;
  function computeOptimalWindowSize(inSize) {
    if (inSize <= PARALLELIZE_THRESHOLD) {
      return inSize;
    }
    return nearestDivisor(inSize, Math.floor(Math.sqrt(inSize)));
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/segment_util.js
  const segment_util_exports = {};
  __export(segment_util_exports, {
    collectGatherOpShapeInfo: () => collectGatherOpShapeInfo,
    computeOutShape: () => computeOutShape3,
    segOpComputeOptimalWindowSize: () => segOpComputeOptimalWindowSize
  });
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function segOpComputeOptimalWindowSize(inSize, numSegments) {
    let done = false;
    let res;
    if (inSize <= PARALLELIZE_THRESHOLD) {
      res = inSize;
      done = true;
    } else {
      res = nearestDivisor(inSize, Math.floor(Math.sqrt(inSize)));
    }
    while (!done) {
      if (res > numSegments || res === inSize) {
        done = true;
      } else {
        res = nearestDivisor(inSize, res + 1);
      }
    }
    return res;
  }
  function computeOutShape3(aShape, axis, numSegments) {
    const outShape = [];
    const rank = aShape.length;
    for (let dim = 0; dim < rank; dim++) {
      if (dim !== axis) {
        outShape.push(aShape[dim]);
      } else {
        outShape.push(numSegments);
      }
    }
    return outShape;
  }
  function collectGatherOpShapeInfo(x, indices, axis) {
    const dimSize = x.shape[axis];
    const outputShape = [];
    let batchSize = 1;
    let sliceSize = 1;
    for (let i = 0; i < axis; i++) {
      outputShape.push(x.shape[i]);
      batchSize *= x.shape[i];
    }
    for (let i = 0; i < indices.rank; i++) {
      outputShape.push(indices.shape[i]);
    }
    for (let i = axis + 1; i < x.rank; i++) {
      outputShape.push(x.shape[i]);
      sliceSize *= x.shape[i];
    }
    return {batchSize, sliceSize, dimSize, outputShape};
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/gather.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function gather_(x, indices, axis = 0) {
    const $x = convertToTensor(x, "x", "gather");
    const $indices = convertToTensor(indices, "indices", "gather", "int32");
    const inputs = {x: $x, indices: $indices};
    const attrs = {axis};
    const forward = (backend2, save) => {
      const parsedAxis = parseAxisParam(axis, $x.shape)[0];
      const shapeInfo = collectGatherOpShapeInfo($x, $indices, parsedAxis);
      const res = backend2.gather($x, reshape($indices, [$indices.size]), parsedAxis);
      save([$x, $indices]);
      return reshape(res, shapeInfo.outputShape);
    };
    return ENGINE.runKernelFunc(forward, inputs, null, GatherV2, attrs);
  }
  const gather = op({gather_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/greater.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function greater_(a, b) {
    let $a = convertToTensor(a, "a", "greater");
    let $b = convertToTensor(b, "b", "greater");
    [$a, $b] = makeTypesMatch($a, $b);
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const forward = (backend2) => backend2.greater($a, $b);
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, Greater);
  }
  const greater = op({greater_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/greater_equal.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function greaterEqual_(a, b) {
    let $a = convertToTensor(a, "a", "greaterEqual");
    let $b = convertToTensor(b, "b", "greaterEqual");
    [$a, $b] = makeTypesMatch($a, $b);
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const forward = (backend2, save) => {
      const res = backend2.greaterEqual($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, GreaterEqual);
  }
  const greaterEqual = op({greaterEqual_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/ifft.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function ifft_(input) {
    assert(input.dtype === "complex64", () => `The dtype for tf.spectral.ifft() must be complex64 but got ${input.dtype}.`);
    const inputs = {input};
    return ENGINE.runKernelFunc((backend2) => {
      const innerDimensionSize = input.shape[input.shape.length - 1];
      const batch = input.size / innerDimensionSize;
      const input2D = reshape(input, [batch, innerDimensionSize]);
      const result = backend2.ifft(input2D);
      return reshape(result, input.shape);
    }, inputs, null, IFFT);
  }
  const ifft = op({ifft_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/imag.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function imag_(input) {
    const $input = convertToTensor(input, "input", "imag");
    const forward = (backend2) => {
      return backend2.imag($input);
    };
    const inputs = {input: $input};
    return ENGINE.runKernelFunc(forward, inputs, null, Imag);
  }
  const imag = op({imag_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/real.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function real_(input) {
    const $input = convertToTensor(input, "input", "real");
    const forward = (backend2) => {
      return backend2.real($input);
    };
    const inputs = {input: $input};
    return ENGINE.runKernelFunc(forward, inputs, null, Real);
  }
  const real = op({real_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/reverse.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function reverse_(x, axis) {
    const $x = convertToTensor(x, "x", "reverse");
    const forward = (backend2) => {
      const axes = parseAxisParam(axis, $x.shape);
      if ($x.rank === 0) {
        return clone($x);
      }
      const res = backend2.reverse($x, axes);
      return reshape(res, $x.shape);
    };
    const inputs = {x: $x};
    const attrs = {dims: axis};
    return ENGINE.runKernelFunc(forward, inputs, null, Reverse, attrs);
  }
  const reverse = op({reverse_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/scalar.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function scalar(value, dtype) {
    if ((isTypedArray(value) && dtype !== "string" || Array.isArray(value)) && dtype !== "complex64") {
      throw new Error("Error creating a new Scalar: value must be a primitive (number|boolean|string)");
    }
    if (dtype === "string" && isTypedArray(value) && !(value instanceof Uint8Array)) {
      throw new Error("When making a scalar from encoded string, the value must be `Uint8Array`.");
    }
    const shape = [];
    const inferredShape = [];
    return makeTensor(value, shape, inferredShape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/irfft.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function irfft_(input) {
    const innerDimensionSize = input.shape[input.shape.length - 1];
    const batch = input.size / innerDimensionSize;
    let ret;
    if (innerDimensionSize <= 2) {
      const complexInput = reshape(input, [batch, innerDimensionSize]);
      ret = ifft(complexInput);
    } else {
      const outputShape = [batch, 2 * (innerDimensionSize - 1)];
      const realInput = reshape(real(input), [batch, innerDimensionSize]);
      const imagInput = reshape(imag(input), [batch, innerDimensionSize]);
      const realConjugate = reverse(slice(realInput, [0, 1], [batch, innerDimensionSize - 2]), 1);
      const imagConjugate = mul(reverse(slice(imagInput, [0, 1], [batch, innerDimensionSize - 2]), 1), scalar(-1));
      const r = concat([realInput, realConjugate], 1);
      const i = concat([imagInput, imagConjugate], 1);
      const complexInput = reshape(complex(r, i), [outputShape[0], outputShape[1]]);
      ret = ifft(complexInput);
    }
    ret = real(ret);
    if (input.rank === 3 && input.shape[0] !== 0) {
      const temp = ret;
      const batch2 = input.shape[0];
      ret = reshape(ret, [batch2, ret.shape[0] / batch2, ret.shape[1]]);
      temp.dispose();
    }
    return ret;
  }
  const irfft = op({irfft_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/is_finite.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function isFinite_(x) {
    const $x = convertToTensor(x, "x", "isFinite");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2) => backend2.isFinite($x), inputs, null, IsFinite);
  }
  const isFinite2 = op({isFinite_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/is_inf.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function isInf_(x) {
    const $x = convertToTensor(x, "x", "isInf");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2) => backend2.isInf($x), inputs, null, IsInf);
  }
  const isInf = op({isInf_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/is_nan.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function isNaN_(x) {
    const $x = convertToTensor(x, "x", "isNaN");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2) => backend2.isNaN($x), inputs, null, IsNan);
  }
  const isNaN2 = op({isNaN_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/maximum.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function maximum_(a, b) {
    let $a = convertToTensor(a, "a", "maximum");
    let $b = convertToTensor(b, "b", "maximum");
    [$a, $b] = makeTypesMatch($a, $b);
    if ($a.dtype === "bool") {
      $a = cast($a, "int32");
      $b = cast($b, "int32");
    }
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const forward = (backend2, save) => {
      const res = backend2.maximum($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, Maximum);
  }
  const maximum = op({maximum_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/leaky_relu.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function leakyRelu_(x, alpha = 0.2) {
    const $x = convertToTensor(x, "x", "leakyRelu");
    return maximum(mul(scalar(alpha), $x), $x);
  }
  const leakyRelu = op({leakyRelu_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/less.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function less_(a, b) {
    let $a = convertToTensor(a, "a", "less");
    let $b = convertToTensor(b, "b", "less");
    [$a, $b] = makeTypesMatch($a, $b);
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const forward = (backend2) => backend2.less($a, $b);
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, Less);
  }
  const less = op({less_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/less_equal.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function lessEqual_(a, b) {
    let $a = convertToTensor(a, "a", "lessEqual");
    let $b = convertToTensor(b, "b", "lessEqual");
    [$a, $b] = makeTypesMatch($a, $b);
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const forward = (backend2, save) => {
      const res = backend2.lessEqual($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, LessEqual);
  }
  const lessEqual = op({lessEqual_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/linspace.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function linspace(start, stop, num) {
    if (num <= 0) {
      throw new Error("The number of values should be positive.");
    }
    const attrs = {start, stop, num};
    return ENGINE.runKernelFunc((backend2) => backend2.linspace(start, stop, num), {}, null, LinSpace, attrs);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/local_response_normalization.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function localResponseNormalization_(x, depthRadius = 5, bias = 1, alpha = 1, beta = 0.5) {
    const $x = convertToTensor(x, "x", "localResponseNormalization");
    assert($x.rank === 4 || $x.rank === 3, () => `Error in localResponseNormalization: x must be rank 3 or 4 but got
               rank ${$x.rank}.`);
    assert(isInt(depthRadius), () => `Error in localResponseNormalization: depthRadius must be an integer but got depthRadius ${depthRadius}.`);
    let x4D = $x;
    let reshapedTo4D = false;
    if ($x.rank === 3) {
      reshapedTo4D = true;
      x4D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2]]);
    }
    const forward = (backend2, save) => {
      const y = backend2.localResponseNormalization4D(x4D, depthRadius, bias, alpha, beta);
      save([x4D, y]);
      return y;
    };
    const inputs = {x: x4D};
    const attrs = {depthRadius, bias, alpha, beta};
    const res = ENGINE.runKernelFunc(forward, inputs, null, LRN, attrs);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    } else {
      return res;
    }
  }
  const localResponseNormalization = op({localResponseNormalization_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/log.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function log_(x) {
    const $x = convertToTensor(x, "x", "log");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.log($x);
      save([$x]);
      return res;
    }, inputs, null, Log);
  }
  const log = op({log_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/log1p.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function log1p_(x) {
    const $x = convertToTensor(x, "x", "log1p");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.log1p($x);
      save([$x]);
      return res;
    }, inputs, null, Log1p);
  }
  const log1p = op({log1p_});

  // node_modules/@tensorflow/tfjs-core/dist/gradients.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function grad(f) {
    assert(isFunction(f), () => "The f passed in grad(f) must be a function");
    return (x, dy) => {
      const $x = convertToTensor(x, "x", "tf.grad", null);
      const $dy = dy != null ? convertToTensor(dy, "dy", "tf.grad") : null;
      return ENGINE.tidy(() => {
        const {value, grads: grads2} = ENGINE.gradients(() => f($x), [$x], $dy);
        if ($dy != null) {
          assertShapesMatch(value.shape, $dy.shape, "The shape of dy passed in grad(f)(x, dy) must match the shape returned by f(x)");
        }
        checkGrads(grads2);
        return grads2[0];
      });
    };
  }
  function grads(f) {
    assert(isFunction(f), () => "The f passed in grads(f) must be a function");
    return (args, dy) => {
      assert(Array.isArray(args), () => "The args passed in grads(f)(args) must be an array of `Tensor`s or `TensorLike`s");
      const $args = convertToTensorArray(args, "args", "tf.grads", null);
      const $dy = dy != null ? convertToTensor(dy, "dy", "tf.grads") : null;
      return ENGINE.tidy(() => {
        const {value, grads: grads2} = ENGINE.gradients(() => f(...$args), $args, $dy);
        if ($dy != null) {
          assertShapesMatch(value.shape, $dy.shape, "The shape of dy passed in grads(f)([x1,...], dy) must match the shape returned by f([x1,...])");
        }
        checkGrads(grads2);
        return grads2;
      });
    };
  }
  function valueAndGrad(f) {
    assert(isFunction(f), () => "The f passed in valueAndGrad(f) must be a function");
    return (x, dy) => {
      assert(x instanceof Tensor, () => "The x passed in valueAndGrad(f)(x) must be a tensor");
      assert(dy == null || dy instanceof Tensor, () => "The dy passed in valueAndGrad(f)(x, dy) must be a tensor");
      const {grads: grads2, value} = ENGINE.gradients(() => f(x), [x], dy);
      checkGrads(grads2);
      return {grad: grads2[0], value};
    };
  }
  function valueAndGrads(f) {
    assert(isFunction(f), () => "The f passed in valueAndGrads(f) must be a function");
    return (args, dy) => {
      assert(Array.isArray(args) && args.every((arg) => arg instanceof Tensor), () => "The args passed in valueAndGrads(f)(args) must be array of tensors");
      assert(dy == null || dy instanceof Tensor, () => "The dy passed in valueAndGrads(f)(args, dy) must be a tensor");
      const res = ENGINE.gradients(() => f(...args), args, dy);
      if (dy != null) {
        assertShapesMatch(res.value.shape, dy.shape, "The shape of dy passed in valueAndGrads(f)([x1,...], dy) must match the shape returned by f([x1,...])");
      }
      checkGrads(res.grads);
      return res;
    };
  }
  function variableGrads(f, varList) {
    assert(isFunction(f), () => "The f passed in variableGrads(f) must be a function");
    assert(varList == null || Array.isArray(varList) && varList.every((v) => v instanceof Variable), () => "The varList passed in variableGrads(f, varList) must be an array of variables");
    const specifiedVarList = varList != null;
    if (!specifiedVarList) {
      varList = [];
      for (const varName in ENGINE.registeredVariables) {
        varList.push(ENGINE.registeredVariables[varName]);
      }
    }
    const specifiedNonTrainable = specifiedVarList ? varList.filter((variable3) => !variable3.trainable) : null;
    const originalVarCount = varList.length;
    varList = varList.filter((variable3) => variable3.trainable);
    assert(varList.length > 0, () => `variableGrads() expects at least one of the input variables to be trainable, but none of the ${originalVarCount} variables is trainable.`);
    const allowNoGradients = true;
    const {value, grads: grads2} = ENGINE.gradients(f, varList, null, allowNoGradients);
    assert(grads2.some((g) => g != null), () => "Cannot find a connection between any variable and the result of the loss function y=f(x). Please make sure the operations that use variables are inside the function f passed to minimize().");
    assert(value.rank === 0, () => `The f passed in variableGrads(f) must return a scalar, but it returned a rank-${value.rank} tensor`);
    const namedGrads = {};
    varList.forEach((v, i) => {
      if (grads2[i] != null) {
        namedGrads[v.name] = grads2[i];
      }
    });
    if (specifiedNonTrainable != null) {
      specifiedNonTrainable.forEach((v) => namedGrads[v.name] = null);
    }
    return {value, grads: namedGrads};
  }
  function customGrad(f) {
    return ENGINE.customGrad(f);
  }
  function checkGrads(grads2) {
    const numNullGradients = grads2.filter((g) => g == null).length;
    if (numNullGradients > 0) {
      throw new Error(`Cannot compute gradient of y=f(x) with respect to x. Make sure that
    the f you passed encloses all operations that lead from x to y.`);
    }
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/neg.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function neg_(x) {
    const $x = convertToTensor(x, "x", "neg");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2) => backend2.neg($x), inputs, null, Negate);
  }
  const neg = op({neg_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/softplus.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function softplus_(x) {
    const $x = convertToTensor(x, "x", "softplus");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.softplus($x);
      save([$x]);
      return res;
    }, inputs, null, Softplus);
  }
  const softplus = op({softplus_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/log_sigmoid.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function logSigmoid_(x) {
    const $x = convertToTensor(x, "x", "logSigmoid");
    const customOp = customGrad((x2) => {
      const value = neg(softplus(neg(x2)));
      const gradFunc = (dy) => {
        const derX = mul(dy, sigmoid(neg(x2)));
        return derX;
      };
      return {value, gradFunc};
    });
    return customOp($x);
  }
  const logSigmoid = op({logSigmoid_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/max.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function max_(x, axis = null, keepDims = false) {
    const $x = convertToTensor(x, "x", "max");
    const forward = (backend2, save) => {
      const origAxes = parseAxisParam(axis, $x.shape);
      let axes = origAxes;
      const permutedAxes = getAxesPermutation(axes, $x.rank);
      let maxInput = $x;
      if (permutedAxes != null) {
        maxInput = transpose($x, permutedAxes);
        axes = getInnerMostAxes(axes.length, maxInput.rank);
      }
      const y = backend2.max(maxInput, axes);
      if (permutedAxes != null) {
        maxInput.dispose();
      }
      let res = y;
      if (keepDims) {
        const expandedShape = expandShapeToKeepDim(res.shape, parseAxisParam(axis, $x.shape));
        res = reshape(res, expandedShape);
        y.dispose();
      }
      save([$x, res]);
      return res;
    };
    const inputs = {x: $x};
    const attrs = {reductionIndices: axis, keepDims};
    return ENGINE.runKernelFunc(forward, inputs, null, Max, attrs);
  }
  const max = op({max_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/sub.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function sub_(a, b) {
    let $a = convertToTensor(a, "a", "sub");
    let $b = convertToTensor(b, "b", "sub");
    [$a, $b] = makeTypesMatch($a, $b);
    const forward = (backend2, save) => {
      const res = backend2.subtract($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, Sub);
  }
  const sub = op({sub_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/sum.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function sum_(x, axis = null, keepDims = false) {
    let $x = convertToTensor(x, "x", "sum");
    if ($x.dtype === "bool") {
      $x = cast($x, "int32");
    }
    const forward = (backend2, save) => {
      save([$x]);
      const axes = parseAxisParam(axis, $x.shape);
      const permutation = getAxesPermutation(axes, $x.rank);
      let reductionAxes = axes;
      let permutedX = $x;
      if (permutation != null) {
        permutedX = transpose($x, permutation);
        reductionAxes = getInnerMostAxes(reductionAxes.length, $x.rank);
      }
      let value = backend2.sum(permutedX, reductionAxes);
      if (keepDims) {
        const newShape = expandShapeToKeepDim(value.shape, axes);
        value = reshape(value, newShape);
      }
      return value;
    };
    const inputs = {x: $x};
    const attrs = {axis, keepDims};
    return ENGINE.runKernelFunc(forward, inputs, null, Sum, attrs);
  }
  const sum2 = op({sum_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/log_softmax.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function logSoftmax_(logits, axis = -1) {
    const $logits = convertToTensor(logits, "logits", "logSoftmax");
    if (axis === -1) {
      axis = $logits.rank - 1;
    }
    if (axis !== $logits.rank - 1) {
      throw Error(`Log Softmax along a non-last dimension is not yet supported. Logits was rank ${$logits.rank} and axis was ${axis}`);
    }
    const forward = (backend2, save) => {
      const keepDims = true;
      const xMax = max(logits, axis, true);
      const shifted = sub(logits, xMax);
      const value = sub(cast(shifted, "float32"), log(sum2(exp(shifted), axis, keepDims)));
      save([value]);
      return value;
    };
    const inputs = {logits: $logits};
    const attrs = {axis};
    return ENGINE.runKernelFunc(forward, inputs, null, LogSoftmax, attrs);
  }
  const logSoftmax = op({logSoftmax_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/log_sum_exp.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function logSumExp_(x, axis = null, keepDims = false) {
    const $x = convertToTensor(x, "x", "logSumExp");
    const axes = parseAxisParam(axis, $x.shape);
    const xMax = max($x, axes, true);
    const a = sub($x, xMax);
    const b = exp(a);
    const c = sum2(b, axes);
    const d = log(c);
    const res = add2(reshape(xMax, d.shape), d);
    if (keepDims) {
      const newShape = expandShapeToKeepDim(res.shape, axes);
      return reshape(res, newShape);
    }
    return res;
  }
  const logSumExp = op({logSumExp_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/logical_and.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function logicalAnd_(a, b) {
    const $a = convertToTensor(a, "a", "logicalAnd", "bool");
    const $b = convertToTensor(b, "b", "logicalAnd", "bool");
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc((backend2) => backend2.logicalAnd($a, $b), inputs, null, LogicalAnd);
  }
  const logicalAnd = op({logicalAnd_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/logical_not.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function logicalNot_(x) {
    const $x = convertToTensor(x, "x", "logicalNot", "bool");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2) => backend2.logicalNot($x), inputs, null, LogicalNot);
  }
  const logicalNot = op({logicalNot_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/logical_or.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function logicalOr_(a, b) {
    const $a = convertToTensor(a, "a", "logicalOr", "bool");
    const $b = convertToTensor(b, "b", "logicalOr", "bool");
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc((backend2) => backend2.logicalOr($a, $b), inputs, null, LogicalOr);
  }
  const logicalOr = op({logicalOr_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/logical_xor.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function logicalXor_(a, b) {
    const $a = convertToTensor(a, "a", "logicalXor", "bool");
    const $b = convertToTensor(b, "b", "logicalXor", "bool");
    assertAndGetBroadcastShape($a.shape, $b.shape);
    return logicalAnd(logicalOr(a, b), logicalNot(logicalAnd(a, b)));
  }
  const logicalXor = op({logicalXor_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/max_pool.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function maxPool_(x, filterSize, strides, pad8, dimRoundingMode) {
    const $x = convertToTensor(x, "x", "maxPool");
    const dilations = 1;
    let x4D = $x;
    let reshapedTo4D = false;
    if ($x.rank === 3) {
      reshapedTo4D = true;
      x4D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2]]);
    }
    assert(x4D.rank === 4, () => `Error in maxPool: input must be rank 4 but got rank ${x4D.rank}.`);
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in maxPool: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in maxPool: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const forward = (backend2, save) => {
      const convInfo = computePool2DInfo(x4D.shape, filterSize, strides, 1, pad8, dimRoundingMode);
      let y;
      if (convInfo.filterWidth === 1 && convInfo.filterHeight === 1 && arraysEqual(convInfo.inShape, convInfo.outShape)) {
        y = x4D.clone();
      } else {
        y = backend2.maxPool(x4D, convInfo);
      }
      save([x4D, y]);
      return y;
    };
    const inputs = {x: x4D};
    const attrs = {filterSize, strides, pad: pad8, dimRoundingMode};
    const res = ENGINE.runKernelFunc(forward, inputs, null, MaxPool, attrs);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const maxPool = op({maxPool_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/max_pool_3d.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function maxPool3d_(x, filterSize = [1, 1, 1], strides, pad8, dimRoundingMode, dataFormat = "NDHWC", dilations) {
    if (dilations == null) {
      dilations = [1, 1, 1];
    } else {
      deprecationWarn("dilations is deprecated, this field will be gone in v3.0.0.");
    }
    const $x = convertToTensor(x, "x", "maxPool3d");
    let x5D = $x;
    let reshapedTo5D = false;
    if ($x.rank === 4) {
      reshapedTo5D = true;
      x5D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2], $x.shape[3]]);
    }
    assert(x5D.rank === 5, () => `Error in maxPool3d: x must be rank 5 but got rank ${x5D.rank}.`);
    assert(dataFormat === "NDHWC", () => `Error in maxPool3d: Only NDHWC is currently supported, but got dataFormat of ${dataFormat}`);
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in maxPool3d: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in maxPool3d: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const forward = (backend2, save) => {
      if (dilations == null) {
        dilations = [1, 1, 1];
      }
      const convInfo = computePool3DInfo(x5D.shape, filterSize, strides, dilations, pad8, dimRoundingMode, dataFormat);
      const y = backend2.maxPool3d(x5D, convInfo);
      save([x5D, y]);
      return y;
    };
    const inputs = {x: x5D};
    const attrs = {filterSize, strides, pad: pad8, dimRoundingMode, dataFormat, dilations};
    const res = ENGINE.runKernelFunc(forward, inputs, null, MaxPool3D, attrs);
    if (reshapedTo5D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3], res.shape[4]]);
    }
    return res;
  }
  const maxPool3d = op({maxPool3d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/max_pool_with_argmax.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function maxPoolWithArgmax_(x, filterSize, strides, pad8, includeBatchInIndex = false) {
    const $x = convertToTensor(x, "x", "maxPoolWithArgmax");
    const inputs = {x: $x};
    const attrs = {filterSize, strides, pad: pad8, includeBatchInIndex};
    const result = ENGINE.runKernel(MaxPoolWithArgmax, inputs, attrs);
    return {result: result[0], indexes: result[1]};
  }
  const maxPoolWithArgmax = op({maxPoolWithArgmax_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/zeros.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function zeros(shape, dtype = "float32") {
    if (dtype === "complex64") {
      const real6 = zeros(shape, "float32");
      const imag6 = zeros(shape, "float32");
      return complex(real6, imag6);
    }
    const values = makeZerosTypedArray(sizeFromShape(shape), dtype);
    return ENGINE.makeTensor(values, shape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/ones.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function ones2(shape, dtype = "float32") {
    if (dtype === "complex64") {
      const real6 = ones2(shape, "float32");
      const imag6 = zeros(shape, "float32");
      return complex(real6, imag6);
    }
    const values = makeOnesTypedArray(sizeFromShape(shape), dtype);
    return ENGINE.makeTensor(values, shape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/mean.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function mean_(x, axis = null, keepDims = false) {
    const $x = convertToTensor(x, "x", "mean");
    const axes = parseAxisParam(axis, $x.shape);
    const shapes = computeOutAndReduceShapes($x.shape, axes);
    const reduceShape = shapes[1];
    const reduceSize = sizeFromShape(reduceShape);
    const customOp = customGrad((x2) => {
      const reduceSizeScalar = scalar(reduceSize);
      const xReduce = reduceSizeScalar.dtype === x2.dtype ? x2 : cast(x2, reduceSizeScalar.dtype);
      const res = div(xReduce, reduceSizeScalar);
      const value = sum2(res, axis, keepDims);
      const gradFunc = (dy) => {
        const expandedDyShape = x2.shape.slice();
        axes.forEach((axis2) => {
          expandedDyShape[axis2] = 1;
        });
        const expandedDy = reshape(dy, expandedDyShape);
        const derX = div(mul(expandedDy, ones2(x2.shape, "float32")), reduceSize);
        return derX;
      };
      return {value, gradFunc};
    });
    return customOp($x);
  }
  const mean = op({mean_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/min.js
  function min_(x, axis = null, keepDims = false) {
    const $x = convertToTensor(x, "x", "min");
    const forward = (backend2, save) => {
      const origAxes = parseAxisParam(axis, $x.shape);
      let axes = origAxes;
      const permutedAxes = getAxesPermutation(axes, $x.rank);
      let minInput = $x;
      if (permutedAxes != null) {
        minInput = transpose($x, permutedAxes);
        axes = getInnerMostAxes(axes.length, $x.rank);
      }
      const y = backend2.min(minInput, axes);
      if (permutedAxes != null) {
        minInput.dispose();
      }
      let res = y;
      if (keepDims) {
        const expandedShape = expandShapeToKeepDim(res.shape, origAxes);
        res = reshape(y, expandedShape);
        y.dispose();
      }
      save([$x, res]);
      return res;
    };
    const inputs = {x: $x};
    const attrs = {axis, keepDims};
    return ENGINE.runKernelFunc(forward, inputs, null, Min, attrs);
  }
  const min = op({min_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/minimum.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function minimum_(a, b) {
    let $a = convertToTensor(a, "a", "minimum");
    let $b = convertToTensor(b, "b", "minimum");
    [$a, $b] = makeTypesMatch($a, $b);
    if ($a.dtype === "bool") {
      $a = cast($a, "int32");
      $b = cast($b, "int32");
    }
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const forward = (backend2, save) => {
      const res = backend2.minimum($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, Minimum);
  }
  const minimum = op({minimum_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/mod.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function mod_(a, b) {
    let $a = convertToTensor(a, "a", "mod");
    let $b = convertToTensor(b, "b", "mod");
    [$a, $b] = makeTypesMatch($a, $b);
    const forward = (backend2, save) => {
      const res = backend2.mod($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, Mod);
  }
  const mod = op({mod_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/square.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function square_(x) {
    const $x = convertToTensor(x, "x", "square");
    const attrs = {};
    const inputsToSave = [$x];
    const outputsToSave = [];
    return ENGINE.runKernelFunc((backend2, save) => {
      save([$x]);
      return backend2.square($x);
    }, {x: $x}, null, "Square", attrs, inputsToSave, outputsToSave);
  }
  const square = op({square_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/moments.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function moments_(x, axis = null, keepDims = false) {
    x = convertToTensor(x, "x", "moments");
    const axes = parseAxisParam(axis, x.shape);
    const xMean = mean(x, axes, keepDims);
    let keepDimsShape = xMean.shape;
    if (!keepDims) {
      keepDimsShape = expandShapeToKeepDim(xMean.shape, axes);
    }
    const devSquared = square(sub(cast(x, "float32"), reshape(xMean, keepDimsShape)));
    const variance = mean(devSquared, axes, keepDims);
    return {mean: xMean, variance};
  }
  const moments = op({moments_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/multi_rnn_cell.js
  function multiRNNCell_(lstmCells, data, c, h) {
    const $data = convertToTensor(data, "data", "multiRNNCell");
    const $c = convertToTensorArray(c, "c", "multiRNNCell");
    const $h = convertToTensorArray(h, "h", "multiRNNCell");
    let input = $data;
    const newStates = [];
    for (let i = 0; i < lstmCells.length; i++) {
      const output = lstmCells[i](input, $c[i], $h[i]);
      newStates.push(output[0]);
      newStates.push(output[1]);
      input = output[1];
    }
    const newC = [];
    const newH = [];
    for (let i = 0; i < newStates.length; i += 2) {
      newC.push(newStates[i]);
      newH.push(newStates[i + 1]);
    }
    return [newC, newH];
  }
  const multiRNNCell = op({multiRNNCell_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/multinomial.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function multinomial_(logits, numSamples, seed, normalized = false) {
    const $logits = convertToTensor(logits, "logits", "multinomial");
    const numOutcomes = $logits.size;
    const origRank = $logits.rank;
    if (numOutcomes < 2) {
      throw new Error(`Error in multinomial: you need at least 2 outcomes, but got ${numOutcomes}.`);
    }
    if (origRank > 2) {
      throw new Error(`Rank of probabilities must be 1 or 2, but is ${origRank}`);
    }
    seed = seed || Math.random();
    const logits2D = origRank === 1 ? reshape($logits, [1, -1]) : $logits;
    const res = ENGINE.runKernelFunc((backend2) => backend2.multinomial(logits2D, normalized, numSamples, seed), {logits2D});
    return origRank === 1 ? reshape(res, [res.size]) : res;
  }
  const multinomial = op({multinomial_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/not_equal.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function notEqual_(a, b) {
    let $a = convertToTensor(a, "a", "notEqual");
    let $b = convertToTensor(b, "b", "notEqual");
    [$a, $b] = makeTypesMatch($a, $b);
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const forward = (backend2) => backend2.notEqual($a, $b);
    const inputs = {a: $a, b: $b};
    return ENGINE.runKernelFunc(forward, inputs, null, NotEqual);
  }
  const notEqual = op({notEqual_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/one_hot.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function oneHot_(indices, depth, onValue = 1, offValue = 0) {
    if (depth < 2) {
      throw new Error(`Error in oneHot: depth must be >=2, but it is ${depth}`);
    }
    const $indices = convertToTensor(indices, "indices", "oneHot", "int32");
    const outShape = [...$indices.shape, depth];
    const forward = (backend2, save) => {
      save([$indices]);
      return reshape(backend2.oneHot(reshape($indices, [$indices.size]), depth, onValue, offValue), outShape);
    };
    const inputs = {indices: $indices};
    const attrs = {depth, onValue, offValue};
    return ENGINE.runKernelFunc(forward, inputs, null, OneHot, attrs);
  }
  const oneHot = op({oneHot_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/ones_like.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function onesLike_(x) {
    const $x = convertToTensor(x, "x", "onesLike");
    const forward = (backend2, save) => {
      if ($x.dtype === "complex64") {
        const r = onesLike(real($x));
        const i = zerosLike(imag($x));
        return complex(r, i);
      }
      return backend2.onesLike($x);
    };
    const inputs = {x: $x};
    return ENGINE.runKernelFunc(forward, inputs, null, OnesLike);
  }
  const onesLike = op({onesLike_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/outer_product.js
  function outerProduct_(v1, v2) {
    const $v1 = convertToTensor(v1, "v1", "outerProduct");
    const $v2 = convertToTensor(v2, "v2", "outerProduct");
    assert($v1.rank === 1 && $v2.rank === 1, () => `Error in outerProduct: inputs must be rank 1, but got ranks ${$v1.rank} and ${$v2.rank}.`);
    const v12D = reshape($v1, [-1, 1]);
    const v22D = reshape($v2, [1, -1]);
    return matMul(v12D, v22D);
  }
  const outerProduct = op({outerProduct_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/pad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function pad_(x, paddings, constantValue = 0) {
    const $x = convertToTensor(x, "x", "pad");
    if ($x.rank === 0) {
      throw new Error("pad(scalar) is not defined. Pass non-scalar to pad");
    }
    const forward = (backend2, save) => {
      save([$x]);
      return backend2.pad($x, paddings, constantValue);
    };
    const attrs = {paddings, constantValue};
    const inputs = {x: $x};
    return ENGINE.runKernelFunc(forward, inputs, null, PadV2, attrs);
  }
  const pad = op({pad_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/pad1d.js
  function pad1d_(x, paddings, constantValue = 0) {
    assert(paddings.length === 2, () => "Invalid number of paddings. Must be length of 2.");
    return pad(x, [paddings], constantValue);
  }
  const pad1d = op({pad1d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/pad2d.js
  function pad2d_(x, paddings, constantValue = 0) {
    assert(paddings.length === 2 && paddings[0].length === 2 && paddings[1].length === 2, () => "Invalid number of paddings. Must be length of 2 each.");
    return pad(x, paddings, constantValue);
  }
  const pad2d = op({pad2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/pad3d.js
  function pad3d_(x, paddings, constantValue = 0) {
    assert(paddings.length === 3 && paddings[0].length === 2 && paddings[1].length === 2 && paddings[2].length === 2, () => "Invalid number of paddings. Must be length of 2 each.");
    return pad(x, paddings, constantValue);
  }
  const pad3d = op({pad3d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/pad4d.js
  function pad4d_(x, paddings, constantValue = 0) {
    assert(paddings.length === 4 && paddings[0].length === 2 && paddings[1].length === 2 && paddings[2].length === 2 && paddings[3].length === 2, () => "Invalid number of paddings. Must be length of 2 each.");
    return pad(x, paddings, constantValue);
  }
  const pad4d = op({pad4d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/space_to_batch_nd.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function spaceToBatchND_(x, blockShape, paddings) {
    const $x = convertToTensor(x, "x", "spaceToBatchND");
    assert($x.rank >= 1 + blockShape.length, () => `input rank ${$x.rank} should be > than [blockShape] ${blockShape.length}`);
    assert(paddings.length === blockShape.length, () => `paddings.shape[0] ${paddings.length} must be equal to [blockShape] ${blockShape.length}`);
    assert($x.shape.reduce((a, b, i) => {
      if (i > 0 && i <= blockShape.length) {
        return a && (b + paddings[i - 1][0] + paddings[i - 1][1]) % blockShape[i - 1] === 0;
      }
      return a;
    }, true), () => `input spatial dimensions ${$x.shape.slice(1)} with paddings ${paddings.toString()} must be divisible by blockShapes ${blockShape.toString()}`);
    const forward = (backend2) => backend2.spaceToBatchND($x, blockShape, paddings);
    const inputs = {x: $x};
    const attrs = {blockShape, paddings};
    return ENGINE.runKernelFunc(forward, inputs, null, SpaceToBatchND, attrs);
  }
  const spaceToBatchND = op({spaceToBatchND_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/pool.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function pool_(input, windowShape, poolingType, pad8, dilations, strides) {
    if (dilations == null) {
      dilations = [1, 1];
    }
    if (strides == null) {
      strides = 1;
    }
    if (pad8 === 0) {
      pad8 = "valid";
    }
    const $x = convertToTensor(input, "x", "maxPool");
    let x4D = $x;
    let reshapedTo4D = false;
    if ($x.rank === 3) {
      reshapedTo4D = true;
      x4D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2]]);
    }
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in pool: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    const convInfo = computePool2DInfo(x4D.shape, windowShape, strides, dilations, pad8);
    const dilation = [convInfo.dilationHeight, convInfo.dilationWidth];
    let basePadding;
    if (pad8 === "same") {
      basePadding = withSpaceToBatchBasePaddings([convInfo.filterHeight, convInfo.filterWidth], dilation);
    } else {
      basePadding = [[0, 0], [0, 0]];
    }
    const isDilationOne = dilation[0] === 1 && dilation[1] === 1;
    const [adjustedPadding, adjustedCrops] = requiredSpaceToBatchPaddings([convInfo.inHeight, convInfo.inWidth], dilation, basePadding);
    const convertedPad = isDilationOne ? pad8 : "valid";
    const convertedX = isDilationOne ? x4D : spaceToBatchND(x4D, dilation, adjustedPadding);
    const forwardOp = poolingType === "avg" ? () => avgPool(convertedX, windowShape, strides, convertedPad) : () => maxPool(convertedX, windowShape, strides, convertedPad);
    const y = forwardOp();
    const res = isDilationOne ? y : batchToSpaceND(y, dilation, adjustedCrops);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  function requiredSpaceToBatchPaddings(inputShape, blockShape, basePadding) {
    const padStart = basePadding.map((b) => b[0]);
    const origPadEnd = basePadding.map((b) => b[1]);
    const fullInputShape = inputShape.concat(padStart, origPadEnd);
    const padEndExtra = blockShape.map((b, i) => (b - fullInputShape[i] % b) % b);
    const padEnd = origPadEnd.map((s, i) => s + padEndExtra[i]);
    const paddings = blockShape.map((_, i) => [padStart[i], padEnd[i]]);
    const crops = blockShape.map((_, i) => [0, padEndExtra[i]]);
    return [paddings, crops];
  }
  function withSpaceToBatchBasePaddings(filterShape, dilation) {
    const dilatedFilterShape = filterShape.map((s, i) => {
      return s + (s - 1) * (dilation[i] - 1);
    });
    const padExtraShape = dilatedFilterShape.map((s) => s - 1);
    const padExtraStart = padExtraShape.map((s) => Math.floor(s / 2));
    const padExtraEnd = padExtraShape.map((s, i) => s - padExtraStart[i]);
    return padExtraShape.map((_, i) => {
      return [padExtraStart[i], padExtraEnd[i]];
    });
  }
  const pool = op({pool_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/pow.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function pow_(base, exp11) {
    let $base = convertToTensor(base, "base", "pow");
    let $exp = convertToTensor(exp11, "exp", "pow");
    [$base, $exp] = makeTypesMatch($base, $exp);
    const inputs = {a: $base, b: $exp};
    const forward = (backend2, save) => {
      const y = backend2.pow($base, $exp);
      save([$base, $exp, y]);
      return y;
    };
    return ENGINE.runKernelFunc(forward, inputs, null, Pow);
  }
  const pow = op({pow_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/prelu.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function prelu_(x, alpha) {
    const $x = convertToTensor(x, "x", "prelu");
    const $alpha = convertToTensor(alpha, "alpha", "prelu");
    const forward = (backend2, save) => {
      const res = backend2.prelu($x, $alpha);
      save([$x, $alpha]);
      return res;
    };
    const inputs = {x: $x, alpha: $alpha};
    return ENGINE.runKernelFunc(forward, inputs, null, Prelu);
  }
  const prelu = op({prelu_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/print.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function print(x, verbose = false) {
    console.log(x.toString(verbose));
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/prod.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function prod_(x, axis = null, keepDims = false) {
    let $x = convertToTensor(x, "x", "prod");
    const forward = (backend2) => {
      if ($x.dtype === "bool") {
        $x = cast($x, "int32");
      }
      const axes = parseAxisParam(axis, $x.shape);
      const permutation = getAxesPermutation(axes, $x.rank);
      let reductionAxes = axes;
      let permutedX = $x;
      if (permutation != null) {
        permutedX = transpose($x, permutation);
        reductionAxes = getInnerMostAxes(reductionAxes.length, $x.rank);
      }
      let value = backend2.prod(permutedX, reductionAxes);
      if (keepDims) {
        const newShape = expandShapeToKeepDim(value.shape, axes);
        value = reshape(value, newShape);
      }
      return value;
    };
    const inputs = {x: $x};
    const attrs = {axis, keepDims};
    return ENGINE.runKernelFunc(forward, inputs, null, Prod, attrs);
  }
  const prod = op({prod_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/rand.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function rand_(shape, randFunction, dtype) {
    const size = sizeFromShape(shape);
    let values = null;
    if (dtype == null || dtype === "float32") {
      values = new Float32Array(size);
    } else if (dtype === "int32") {
      values = new Int32Array(size);
    } else if (dtype === "bool") {
      values = new Uint8Array(size);
    } else {
      throw new Error(`Unknown data type ${dtype}`);
    }
    for (let i = 0; i < size; i++) {
      values[i] = randFunction();
    }
    return ENGINE.makeTensor(values, shape, dtype);
  }
  const rand = op({rand_});

  // node_modules/@tensorflow/tfjs-core/dist/test_util.js
  const test_util_exports = {};
  __export(test_util_exports, {
    TEST_EPSILON_FLOAT16: () => TEST_EPSILON_FLOAT16,
    expectArrayBuffersEqual: () => expectArrayBuffersEqual,
    expectArraysClose: () => expectArraysClose,
    expectArraysEqual: () => expectArraysEqual,
    expectNumbersClose: () => expectNumbersClose,
    expectPromiseToFail: () => expectPromiseToFail,
    expectValuesInRange: () => expectValuesInRange,
    testEpsilon: () => testEpsilon
  });
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const TEST_EPSILON_FLOAT32 = 1e-3;
  const TEST_EPSILON_FLOAT16 = 0.1;
  function expectArraysClose(actual, expected, epsilon2) {
    if (epsilon2 == null) {
      epsilon2 = testEpsilon();
    }
    return expectArraysPredicate(actual, expected, (a, b) => areClose(a, b, epsilon2));
  }
  function testEpsilon() {
    return ENGINE.backend.floatPrecision() === 32 ? TEST_EPSILON_FLOAT32 : TEST_EPSILON_FLOAT16;
  }
  function expectArraysPredicate(actual, expected, predicate) {
    let checkClassType = true;
    if (isTypedArray(actual) || isTypedArray(expected)) {
      checkClassType = false;
    }
    if (isTypedArray(actual) && isTypedArray(expected)) {
      checkClassType = true;
    }
    if (checkClassType) {
      const aType = actual.constructor.name;
      const bType = expected.constructor.name;
      if (aType !== bType) {
        throw new Error(`Arrays are of different type. Actual: ${aType}. Expected: ${bType}`);
      }
    }
    if (Array.isArray(actual) && Array.isArray(expected)) {
      const actualShape = inferShape(actual);
      const expectedShape = inferShape(expected);
      if (!arraysEqual(actualShape, expectedShape)) {
        throw new Error(`Arrays have different shapes. Actual: [${actualShape}]. Expected: [${expectedShape}]`);
      }
    }
    const actualFlat = isTypedArray(actual) ? actual : flatten(actual);
    const expectedFlat = isTypedArray(expected) ? expected : flatten(expected);
    if (actualFlat.length !== expectedFlat.length) {
      throw new Error(`Arrays have different lengths actual: ${actualFlat.length} vs expected: ${expectedFlat.length}.
Actual:   ${actualFlat}.
Expected: ${expectedFlat}.`);
    }
    for (let i = 0; i < expectedFlat.length; ++i) {
      const a = actualFlat[i];
      const e = expectedFlat[i];
      if (!predicate(a, e)) {
        throw new Error(`Arrays differ: actual[${i}] = ${a}, expected[${i}] = ${e}.
Actual:   ${actualFlat}.
Expected: ${expectedFlat}.`);
      }
    }
  }
  function expectPromiseToFail(fn, done) {
    fn().then(() => done.fail(), () => done());
  }
  function expectArraysEqual(actual, expected) {
    const exp11 = typeof expected === "string" || typeof expected === "number" || typeof expected === "boolean" ? [expected] : expected;
    if (isString(actual) || isString(actual[0]) || isString(expected) || isString(expected[0])) {
      return expectArraysPredicate(actual, exp11, (a, b) => a == b);
    }
    return expectArraysPredicate(actual, expected, (a, b) => areClose(a, b, 0));
  }
  function expectNumbersClose(a, e, epsilon2) {
    if (epsilon2 == null) {
      epsilon2 = testEpsilon();
    }
    if (!areClose(a, e, epsilon2)) {
      throw new Error(`Numbers differ: actual === ${a}, expected === ${e}`);
    }
  }
  function areClose(a, e, epsilon2) {
    if (!isFinite(a) && !isFinite(e)) {
      return true;
    }
    if (isNaN(a) || isNaN(e) || Math.abs(a - e) > epsilon2) {
      return false;
    }
    return true;
  }
  function expectValuesInRange(actual, low, high) {
    for (let i = 0; i < actual.length; i++) {
      if (actual[i] < low || actual[i] > high) {
        throw new Error(`Value out of range:${actual[i]} low: ${low}, high: ${high}`);
      }
    }
  }
  function expectArrayBuffersEqual(actual, expected) {
    expect(new Float32Array(actual)).toEqual(new Float32Array(expected));
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/rand_util.js
  const seedrandom = __toModule(require_seedrandom2());
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class MPRandGauss {
    constructor(mean5, stdDeviation, dtype, truncated, seed) {
      this.mean = mean5;
      this.stdDev = stdDeviation;
      this.dtype = dtype;
      this.nextVal = NaN;
      this.truncated = truncated;
      if (this.truncated) {
        this.upper = this.mean + this.stdDev * 2;
        this.lower = this.mean - this.stdDev * 2;
      }
      const seedValue = seed ? seed : Math.random();
      this.random = seedrandom.alea(seedValue.toString());
    }
    nextValue() {
      if (!isNaN(this.nextVal)) {
        const value = this.nextVal;
        this.nextVal = NaN;
        return value;
      }
      let resultX, resultY;
      let isValid = false;
      while (!isValid) {
        let v1, v2, s;
        do {
          v1 = 2 * this.random() - 1;
          v2 = 2 * this.random() - 1;
          s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s === 0);
        const mul62 = Math.sqrt(-2 * Math.log(s) / s);
        resultX = this.mean + this.stdDev * v1 * mul62;
        resultY = this.mean + this.stdDev * v2 * mul62;
        if (!this.truncated || this.isValidTruncated(resultX)) {
          isValid = true;
        }
      }
      if (!this.truncated || this.isValidTruncated(resultY)) {
        this.nextVal = this.convertValue(resultY);
      }
      return this.convertValue(resultX);
    }
    convertValue(value) {
      if (this.dtype == null || this.dtype === "float32") {
        return value;
      }
      return Math.round(value);
    }
    isValidTruncated(value) {
      return value <= this.upper && value >= this.lower;
    }
  }
  class RandGamma {
    constructor(alpha, beta, dtype, seed) {
      this.alpha = alpha;
      this.beta = 1 / beta;
      this.dtype = dtype;
      const seedValue = seed ? seed : Math.random();
      this.randu = seedrandom.alea(seedValue.toString());
      this.randn = new MPRandGauss(0, 1, dtype, false, this.randu());
      if (alpha < 1) {
        this.d = alpha + 2 / 3;
      } else {
        this.d = alpha - 1 / 3;
      }
      this.c = 1 / Math.sqrt(9 * this.d);
    }
    nextValue() {
      let x2, v0, v1, x, u, v;
      while (true) {
        do {
          x = this.randn.nextValue();
          v = 1 + this.c * x;
        } while (v <= 0);
        v *= v * v;
        x2 = x * x;
        v0 = 1 - 0.331 * x2 * x2;
        v1 = 0.5 * x2 + this.d * (1 - v + Math.log(v));
        u = this.randu();
        if (u < v0 || Math.log(u) < v1) {
          break;
        }
      }
      v = 1 / this.beta * this.d * v;
      if (this.alpha < 1) {
        v *= Math.pow(this.randu(), 1 / this.alpha);
      }
      return this.convertValue(v);
    }
    convertValue(value) {
      if (this.dtype === "float32") {
        return value;
      }
      return Math.round(value);
    }
  }
  class UniformRandom {
    constructor(min5 = 0, max7 = 1, dtype, seed) {
      this.canReturnFloat = () => this.dtype == null || this.dtype === "float32";
      this.min = min5;
      this.range = max7 - min5;
      this.dtype = dtype;
      if (seed == null) {
        seed = Math.random();
      }
      if (typeof seed === "number") {
        seed = seed.toString();
      }
      if (!this.canReturnFloat() && this.range <= 1) {
        throw new Error(`The difference between ${min5} - ${max7} <= 1 and dtype is not float`);
      }
      this.random = seedrandom.alea(seed);
    }
    convertValue(value) {
      if (this.canReturnFloat()) {
        return value;
      }
      return Math.round(value);
    }
    nextValue() {
      return this.convertValue(this.min + this.range * this.random());
    }
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/random_gamma.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function randomGamma_(shape, alpha, beta = 1, dtype = "float32", seed) {
    if (beta == null) {
      beta = 1;
    }
    if (dtype == null) {
      dtype = "float32";
    }
    if (dtype !== "float32" && dtype !== "int32") {
      throw new Error(`Unsupported data type ${dtype}`);
    }
    const rgamma = new RandGamma(alpha, beta, dtype, seed);
    const res = buffer(shape, dtype);
    for (let i = 0; i < res.values.length; i++) {
      res.values[i] = rgamma.nextValue();
    }
    return res.toTensor();
  }
  const randomGamma = op({randomGamma_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/random_normal.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function randomNormal_(shape, mean5 = 0, stdDev = 1, dtype, seed) {
    if (dtype != null && dtype === "bool") {
      throw new Error(`Unsupported data type ${dtype}`);
    }
    const randGauss = new MPRandGauss(mean5, stdDev, dtype, false, seed);
    const res = buffer(shape, dtype);
    for (let i = 0; i < res.values.length; i++) {
      res.values[i] = randGauss.nextValue();
    }
    return res.toTensor();
  }
  const randomNormal = op({randomNormal_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/random_uniform.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function randomUniform_(shape, minval = 0, maxval = 1, dtype = "float32", seed) {
    const res = buffer(shape, dtype);
    const random = new UniformRandom(minval, maxval, null, seed);
    for (let i = 0; i < res.values.length; i++) {
      res.values[i] = random.nextValue();
    }
    return res.toTensor();
  }
  const randomUniform = op({randomUniform_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/tensor1d.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tensor1d(values, dtype) {
    assertNonNull(values);
    const inferredShape = inferShape(values, dtype);
    if (inferredShape.length !== 1) {
      throw new Error("tensor1d() requires values to be a flat/TypedArray");
    }
    const shape = null;
    return makeTensor(values, shape, inferredShape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/range.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function range(start, stop, step7 = 1, dtype = "float32") {
    if (step7 === 0) {
      throw new Error("Cannot have a step of zero");
    }
    const forward = () => {
      const sameStartStop = start === stop;
      const increasingRangeNegativeStep = start < stop && step7 < 0;
      const decreasingRangePositiveStep = stop < start && step7 > 1;
      if (sameStartStop || increasingRangeNegativeStep || decreasingRangePositiveStep) {
        return zeros([0], dtype);
      }
      const numElements = Math.abs(Math.ceil((stop - start) / step7));
      const values = makeZerosTypedArray(numElements, dtype);
      if (stop < start && step7 === 1) {
        step7 = -1;
      }
      values[0] = start;
      for (let i = 1; i < values.length; i++) {
        values[i] = values[i - 1] + step7;
      }
      return tensor1d(values, dtype);
    };
    const attrs = {start, stop, step: step7, dtype};
    return ENGINE.runKernelFunc(forward, {}, null, Range, attrs);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/reciprocal.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function reciprocal_(x) {
    const $x = convertToTensor(x, "x", "reciprocal");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.reciprocal($x);
      save([$x]);
      return res;
    }, inputs, null, Reciprocal);
  }
  const reciprocal = op({reciprocal_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/relu.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function relu_(x) {
    const $x = convertToTensor(x, "x", "relu");
    const forward = (backend2, save) => {
      save([$x]);
      if ($x.dtype === "bool") {
        return cast($x, "int32");
      }
      return backend2.relu($x);
    };
    const inputs = {x: $x};
    return ENGINE.runKernelFunc(forward, inputs, null, Relu);
  }
  const relu = op({relu_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/relu6.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function relu6_(x) {
    const $x = convertToTensor(x, "x", "relu6");
    const forward = (backend2, save) => {
      save([$x]);
      if ($x.dtype === "bool") {
        return cast($x, "int32");
      }
      return backend2.relu6($x);
    };
    const inputs = {x: $x};
    return ENGINE.runKernelFunc(forward, inputs, null, Relu6);
  }
  const relu6 = op({relu6_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/reverse_1d.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function reverse1d_(x) {
    const $x = convertToTensor(x, "x", "reverse");
    assert($x.rank === 1, () => `Error in reverse1D: x must be rank 1 but got rank ${$x.rank}.`);
    return reverse($x, 0);
  }
  const reverse1d = op({reverse1d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/reverse_2d.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function reverse2d_(x, axis) {
    const $x = convertToTensor(x, "x", "reverse");
    assert($x.rank === 2, () => `Error in reverse2D: x must be rank 2 but got rank ${$x.rank}.`);
    return reverse($x, axis);
  }
  const reverse2d = op({reverse2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/reverse_3d.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function reverse3d_(x, axis) {
    const $x = convertToTensor(x, "x", "reverse");
    assert($x.rank === 3, () => `Error in reverse3D: x must be rank 3 but got rank ${$x.rank}.`);
    return reverse($x, axis);
  }
  const reverse3d = op({reverse3d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/reverse_4d.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function reverse4d_(x, axis) {
    const $x = convertToTensor(x, "x", "reverse");
    assert($x.rank === 4, () => `Error in reverse4D: x must be rank 4 but got rank ${$x.rank}.`);
    return reverse($x, axis);
  }
  const reverse4d = op({reverse4d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/split_util.js
  function prepareSplitSize(x, numOrSizeSplits, axis = 0) {
    let splitSizes = [];
    if (typeof numOrSizeSplits === "number") {
      assert(x.shape[axis] % numOrSizeSplits === 0, () => "Number of splits must evenly divide the axis.");
      splitSizes = new Array(numOrSizeSplits).fill(x.shape[axis] / numOrSizeSplits);
    } else {
      const numOfNegs = numOrSizeSplits.reduce((count, value) => {
        if (value === -1) {
          count += 1;
        }
        return count;
      }, 0);
      assert(numOfNegs <= 1, () => "There should be only one negative value in split array.");
      const negIndex = numOrSizeSplits.indexOf(-1);
      if (negIndex !== -1) {
        const total = numOrSizeSplits.reduce((a, b) => b > 0 ? a + b : a);
        numOrSizeSplits[negIndex] = x.shape[axis] - total;
      }
      assert(x.shape[axis] === numOrSizeSplits.reduce((a, b) => a + b), () => "The sum of sizes must match the size of the axis dimension.");
      splitSizes = numOrSizeSplits;
    }
    return splitSizes;
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/split.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function split_(x, numOrSizeSplits, axis = 0) {
    const $x = convertToTensor(x, "x", "split");
    const forward = (backend2, _) => {
      const $axis = parseAxisParam(axis, $x.shape)[0];
      const splitSizes = prepareSplitSize($x, numOrSizeSplits, $axis);
      return backend2.split($x, splitSizes, $axis);
    };
    const inputs = {x: $x};
    const attr = {numOrSizeSplits, axis};
    return ENGINE.runKernelFunc(forward, inputs, null, SplitV, attr);
  }
  const split = op({split_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/rfft.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function rfft_(input, fftLength) {
    assert(input.dtype === "float32", () => `The dtype for rfft() must be real value but got ${input.dtype}`);
    let innerDimensionSize = input.shape[input.shape.length - 1];
    const batch = input.size / innerDimensionSize;
    let adjustedInput;
    if (fftLength != null && fftLength < innerDimensionSize) {
      const begin = input.shape.map((v) => 0);
      const size = input.shape.map((v) => v);
      size[input.shape.length - 1] = fftLength;
      adjustedInput = slice(input, begin, size);
      innerDimensionSize = fftLength;
    } else if (fftLength != null && fftLength > innerDimensionSize) {
      const zerosShape = input.shape.map((v) => v);
      zerosShape[input.shape.length - 1] = fftLength - innerDimensionSize;
      adjustedInput = concat([input, zeros(zerosShape)], input.shape.length - 1);
      innerDimensionSize = fftLength;
    } else {
      adjustedInput = input;
    }
    const zerosInput = zerosLike(adjustedInput);
    const complexInput = reshape(complex(adjustedInput, zerosInput), [batch, innerDimensionSize]);
    const ret = fft(complexInput);
    const half = Math.floor(innerDimensionSize / 2) + 1;
    const realValues = real(ret);
    const imagValues = imag(ret);
    const realComplexConjugate = split(realValues, [half, innerDimensionSize - half], realValues.shape.length - 1);
    const imagComplexConjugate = split(imagValues, [half, innerDimensionSize - half], imagValues.shape.length - 1);
    const outputShape = adjustedInput.shape.slice();
    outputShape[adjustedInput.shape.length - 1] = half;
    return reshape(complex(realComplexConjugate[0], imagComplexConjugate[0]), outputShape);
  }
  const rfft = op({rfft_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/round.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function round_(x) {
    const $x = convertToTensor(x, "x", "round");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2) => backend2.round($x), inputs, null, Round);
  }
  const round = op({round_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/rsqrt.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function rsqrt_(x) {
    const $x = convertToTensor(x, "x", "rsqrt");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.rsqrt($x);
      save([$x]);
      return res;
    }, inputs, null, Rsqrt);
  }
  const rsqrt = op({rsqrt_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/selu.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function selu_(x) {
    const $x = convertToTensor(x, "x", "selu");
    const forward = (backend2, save) => {
      const res = backend2.selu($x);
      save([$x]);
      return res;
    };
    const inputs = {x: $x};
    return ENGINE.runKernelFunc(forward, inputs, null, Selu);
  }
  const selu = op({selu_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/separable_conv2d.js
  function separableConv2d_(x, depthwiseFilter, pointwiseFilter, strides, pad8, dilation = [1, 1], dataFormat = "NHWC") {
    const $x = convertToTensor(x, "x", "separableConv2d");
    const $depthwiseFilter = convertToTensor(depthwiseFilter, "depthwiseFilter", "separableConv2d");
    const $pointwiseFilter = convertToTensor(pointwiseFilter, "pointwiseFilter", "separableConv2d");
    let x4D = $x;
    let reshapedTo4D = false;
    if ($x.rank === 3) {
      reshapedTo4D = true;
      x4D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2]]);
    }
    if (dataFormat === "NCHW") {
      throw new Error("separableConv2d currently does not support dataFormat NCHW; only NHWC is supported");
    }
    assert(x4D.rank === 4, () => `Error in separableConv2d: input must be rank 4, but got rank ${x4D.rank}.`);
    assert($depthwiseFilter.rank === 4, () => `Error in separableConv2d: depthwise filter must be rank 4, but got rank ${$depthwiseFilter.rank}.`);
    assert($pointwiseFilter.rank === 4, () => `Error in separableConv2d: pointwise filter must be rank 4, but got rank ${$depthwiseFilter.rank}.`);
    assert($pointwiseFilter.shape[0] === 1, () => `Error in separableConv2d: the first dimension of pointwise filter  must be 1, but got ${$pointwiseFilter.shape[0]}.`);
    assert($pointwiseFilter.shape[1] === 1, () => `Error in separableConv2d: the second dimension of pointwise filter must be 1, but got ${$pointwiseFilter.shape[1]}.`);
    const inChannels = $depthwiseFilter.shape[2];
    const channelMultiplier = $depthwiseFilter.shape[3];
    assert($pointwiseFilter.shape[2] === inChannels * channelMultiplier, () => `Error in separableConv2d: the third dimension of pointwise filter must be ${inChannels * channelMultiplier}, but got ${$pointwiseFilter.shape[2]}.`);
    const depthwise = depthwiseConv2d(x4D, $depthwiseFilter, strides, pad8, dataFormat, dilation);
    const pointwiseStride = 1;
    const res = conv2d(depthwise, $pointwiseFilter, pointwiseStride, "valid", dataFormat);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const separableConv2d = op({separableConv2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/setdiff1d_async.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  async function setdiff1dAsync_(x, y) {
    const $x = convertToTensor(x, "x", "setdiff1d");
    const $y = convertToTensor(y, "y", "setdiff1d");
    assert($x.dtype === $y.dtype, () => `x and y should have the same dtype, but got x (${$x.dtype}) and y (${$y.dtype}).`);
    assert($x.rank === 1, () => `x should be 1D tensor, but got x (${$x.shape}).`);
    assert($y.rank === 1, () => `y should be 1D tensor, but got y (${$y.shape}).`);
    const xVals = await $x.data();
    const yVals = await $y.data();
    const ySet = new Set(yVals);
    let outputSize = 0;
    for (let i = 0; i < xVals.length; i++) {
      if (!ySet.has(xVals[i])) {
        outputSize++;
      }
    }
    const buffer10 = new TensorBuffer([outputSize], $x.dtype);
    const indices = new TensorBuffer([outputSize], "int32");
    for (let i = 0, p = 0; i < xVals.length; i++) {
      if (!ySet.has(xVals[i])) {
        buffer10.values[p] = xVals[i];
        indices.values[p] = i;
        p++;
      }
    }
    return [buffer10.toTensor(), indices.toTensor()];
  }
  const setdiff1dAsync = setdiff1dAsync_;

  // node_modules/@tensorflow/tfjs-core/dist/ops/sign.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function sign_(x) {
    const $x = convertToTensor(x, "x", "sign");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2) => backend2.sign($x), inputs, null, Sign);
  }
  const sign = op({sign_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/sin.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function sin_(x) {
    const $x = convertToTensor(x, "x", "sin");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.sin($x);
      save([$x]);
      return res;
    }, inputs, null, Sin);
  }
  const sin = op({sin_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/sinh.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function sinh_(x) {
    const $x = convertToTensor(x, "x", "sinh");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.sinh($x);
      save([$x]);
      return res;
    }, inputs, null, Sinh);
  }
  const sinh = op({sinh_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/slice1d.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function slice1d_(x, begin, size) {
    const $x = convertToTensor(x, "x", "slice1d");
    assert($x.rank === 1, () => `slice1d expects a rank-1 tensor, but got a rank-${$x.rank} tensor`);
    return slice($x, [begin], [size]);
  }
  const slice1d = op({slice1d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/slice2d.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function slice2d_(x, begin, size) {
    const $x = convertToTensor(x, "x", "slice2d");
    assert($x.rank === 2, () => `slice2d expects a rank-2 tensor, but got a rank-${$x.rank} tensor`);
    return slice($x, begin, size);
  }
  const slice2d = op({slice2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/slice3d.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function slice3d_(x, begin, size) {
    const $x = convertToTensor(x, "x", "slice3d");
    assert($x.rank === 3, () => `slice3d expects a rank-3 tensor, but got a rank-${$x.rank} tensor`);
    return slice($x, begin, size);
  }
  const slice3d = op({slice3d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/slice4d.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function slice4d_(x, begin, size) {
    const $x = convertToTensor(x, "x", "slice4d");
    assert($x.rank === 4, () => `slice4d expects a rank-4 tensor, but got a rank-${$x.rank} tensor`);
    return slice($x, begin, size);
  }
  const slice4d = op({slice4d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/softmax.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function softmax_(logits, dim = -1) {
    const $logits = convertToTensor(logits, "logits", "softmax", "float32");
    if (dim === -1) {
      dim = $logits.rank - 1;
    }
    if (dim !== $logits.rank - 1) {
      throw Error(`Softmax along a non-last dimension is not yet supported. Logits was rank ${$logits.rank} and dim was ${dim}`);
    }
    const inputs = {logits: $logits};
    const attrs = {dim};
    return ENGINE.runKernelFunc((backend2, save) => {
      const y = backend2.softmax($logits, dim);
      save([y]);
      return y;
    }, inputs, null, Softmax, attrs);
  }
  const softmax = op({softmax_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/sqrt.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function sqrt_(x) {
    const $x = convertToTensor(x, "x", "sqrt");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.sqrt($x);
      save([$x]);
      return res;
    }, inputs, null, Sqrt);
  }
  const sqrt = op({sqrt_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/squared_difference.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function squaredDifference_(a, b) {
    let $a = convertToTensor(a, "a", "squaredDifference");
    let $b = convertToTensor(b, "b", "squaredDifference");
    [$a, $b] = makeTypesMatch($a, $b);
    assertAndGetBroadcastShape($a.shape, $b.shape);
    const forward = (backend2, save) => {
      const res = backend2.squaredDifference($a, $b);
      save([$a, $b]);
      return res;
    };
    const inputs = {a: $a, b: $b};
    const attrs = {};
    return ENGINE.runKernelFunc(forward, inputs, null, SquaredDifference, attrs);
  }
  const squaredDifference = op({squaredDifference_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/squeeze.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function squeeze_(x, axis) {
    const $x = convertToTensor(x, "x", "squeeze");
    return reshape($x, squeezeShape($x.shape, axis).newShape);
  }
  const squeeze = op({squeeze_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/stack.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function stack_(tensors, axis = 0) {
    const $tensors = convertToTensorArray(tensors, "tensors", "stack");
    assert($tensors.length >= 1, () => "Pass at least one tensor to tf.stack");
    if ($tensors.length === 1) {
      return expandDims($tensors[0], axis);
    }
    const rank = $tensors[0].rank;
    const shape = $tensors[0].shape;
    const dtype = $tensors[0].dtype;
    assert(axis <= rank, () => "Axis must be <= rank of the tensor");
    $tensors.forEach((t) => {
      assertShapesMatch(shape, t.shape, "All tensors passed to stack must have matching shapes");
      assert(dtype === t.dtype, () => "All tensors passed to stack must have matching dtypes");
    });
    const expandedTensors = $tensors.map((t) => expandDims(t, axis));
    return concat(expandedTensors, axis);
  }
  const stack = op({stack_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/step.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function step_(x, alpha = 0) {
    const $x = convertToTensor(x, "x", "step");
    const inputs = {x: $x};
    const attrs = {alpha};
    return ENGINE.runKernelFunc((backend2) => backend2.step($x, alpha), inputs, null, Step, attrs);
  }
  const step = op({step_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/strided_slice.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function stridedSlice_(x, begin, end, strides, beginMask = 0, endMask = 0, ellipsisMask = 0, newAxisMask = 0, shrinkAxisMask = 0) {
    if (strides == null) {
      strides = new Array(begin.length);
    }
    let $x = convertToTensor(x, "x", "stridedSlice");
    const forward = (backend2) => {
      const ellipsisAxes = maskToAxes(ellipsisMask);
      if (ellipsisAxes.length > 1) {
        throw new Error("Multiple ellipses in slice is not allowed.");
      }
      if (ellipsisMask !== 0 && newAxisMask !== 0) {
        throw new Error("Using both ellipsisMask and newAxisMask is not yet supported.");
      }
      if (ellipsisMask !== 0 && shrinkAxisMask !== 0) {
        throw new Error("Using both ellipsisMask and shrinkAxisMask is not yet supported.");
      }
      const numInterpolatedAxes = $x.rank - begin.length;
      const expandAxes = maskToAxes(newAxisMask);
      const newShape = $x.shape.slice();
      expandAxes.forEach((axis) => {
        begin[axis] = 0;
        end[axis] = 1;
        newShape.splice(axis, 0, 1);
      });
      $x = reshape($x, newShape);
      if (ellipsisAxes.length && numInterpolatedAxes > 0) {
        const fullIndex = ellipsisAxes[0];
        const numElidedAxes = numInterpolatedAxes + 1;
        begin = startIndicesWithElidedDims(beginMask, fullIndex, numElidedAxes, begin, $x.shape);
        end = stopIndicesWithElidedDims(endMask, fullIndex, numElidedAxes, end, $x.shape);
        strides = stridesWithElidedDims(strides, fullIndex, numElidedAxes, $x.shape);
      } else {
        for (let axis = 0; axis < $x.rank; axis++) {
          begin[axis] = startForAxis(beginMask, begin, strides, $x.shape, axis, ellipsisMask);
          end[axis] = stopForAxis(endMask, end, strides, $x.shape, axis, ellipsisMask);
          strides[axis] = stridesForAxis(strides, axis, ellipsisMask);
        }
      }
      const shrinkAxes = maskToAxes(shrinkAxisMask);
      shrinkAxes.forEach((axis) => {
        end[axis] = begin[axis] + 1;
        strides[axis] = 1;
      });
      const size = computeOutShape2(begin, end, strides);
      const outShape = size.filter((_, axis) => shrinkAxes.indexOf(axis) === -1);
      const nonStrided = strides.every((v) => v === 1);
      if (nonStrided) {
        return reshape(slice($x, begin, size), outShape);
      }
      const res = backend2.stridedSlice($x, begin, end, strides);
      return reshape(res, outShape);
    };
    const inputs = {x: $x};
    const attrs = {
      begin,
      end,
      strides,
      beginMask,
      endMask,
      ellipsisMask,
      newAxisMask,
      shrinkAxisMask
    };
    return ENGINE.runKernelFunc(forward, inputs, null, StridedSlice, attrs);
  }
  const stridedSlice = op({stridedSlice_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/tan.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tan_(x) {
    const $x = convertToTensor(x, "x", "tan");
    const inputs = {x: $x};
    return ENGINE.runKernelFunc((backend2, save) => {
      const res = backend2.tan($x);
      save([$x]);
      return res;
    }, inputs, null, Tan);
  }
  const tan = op({tan_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/tensor2d.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tensor2d(values, shape, dtype) {
    assertNonNull(values);
    if (shape != null && shape.length !== 2) {
      throw new Error("tensor2d() requires shape to have two numbers");
    }
    const inferredShape = inferShape(values, dtype);
    if (inferredShape.length !== 2 && inferredShape.length !== 1) {
      throw new Error("tensor2d() requires values to be number[][] or flat/TypedArray");
    }
    if (inferredShape.length === 1 && shape == null) {
      throw new Error("tensor2d() requires shape to be provided when `values` are a flat/TypedArray");
    }
    return makeTensor(values, shape, inferredShape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/tensor3d.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tensor3d(values, shape, dtype) {
    assertNonNull(values);
    if (shape != null && shape.length !== 3) {
      throw new Error("tensor3d() requires shape to have three numbers");
    }
    const inferredShape = inferShape(values, dtype);
    if (inferredShape.length !== 3 && inferredShape.length !== 1) {
      throw new Error("tensor3d() requires values to be number[][][] or flat/TypedArray");
    }
    if (inferredShape.length === 1 && shape == null) {
      throw new Error("tensor3d() requires shape to be provided when `values` are a flat array");
    }
    return makeTensor(values, shape, inferredShape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/tensor4d.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tensor4d(values, shape, dtype) {
    assertNonNull(values);
    if (shape != null && shape.length !== 4) {
      throw new Error("tensor4d() requires shape to have four numbers");
    }
    const inferredShape = inferShape(values, dtype);
    if (inferredShape.length !== 4 && inferredShape.length !== 1) {
      throw new Error("tensor4d() requires values to be number[][][][] or flat/TypedArray");
    }
    if (inferredShape.length === 1 && shape == null) {
      throw new Error("tensor4d() requires shape to be provided when `values` are a flat array");
    }
    return makeTensor(values, shape, inferredShape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/tensor5d.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tensor5d(values, shape, dtype) {
    assertNonNull(values);
    if (shape != null && shape.length !== 5) {
      throw new Error("tensor5d() requires shape to have five numbers");
    }
    const inferredShape = inferShape(values, dtype);
    if (inferredShape.length !== 5 && inferredShape.length !== 1) {
      throw new Error("tensor5d() requires values to be number[][][][][] or flat/TypedArray");
    }
    if (inferredShape.length === 1 && shape == null) {
      throw new Error("tensor5d() requires shape to be provided when `values` are a flat array");
    }
    return makeTensor(values, shape, inferredShape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/tensor6d.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tensor6d(values, shape, dtype) {
    assertNonNull(values);
    if (shape != null && shape.length !== 6) {
      throw new Error("tensor6d() requires shape to have six numbers");
    }
    const inferredShape = inferShape(values, dtype);
    if (inferredShape.length !== 6 && inferredShape.length !== 1) {
      throw new Error("tensor6d() requires values to be number[][][][][][] or flat/TypedArray");
    }
    if (inferredShape.length === 1 && shape == null) {
      throw new Error("tensor6d() requires shape to be provided when `values` are a flat array");
    }
    shape = shape || inferredShape;
    return makeTensor(values, shape, inferredShape, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/topk.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function topk_(x, k = 1, sorted = true) {
    const $x = convertToTensor(x, "x", "topk");
    if ($x.rank === 0) {
      throw new Error("topk() expects the input to be of rank 1 or higher");
    }
    const lastDim = $x.shape[$x.shape.length - 1];
    if (k > lastDim) {
      throw new Error(`'k' passed to topk() must be <= the last dimension (${lastDim}) but got ${k}`);
    }
    const inputs = {x: $x};
    const attrs = {k, sorted};
    const [values, indices] = ENGINE.runKernelFunc((b) => b.topk($x, k, sorted), inputs, null, TopK, attrs);
    return {values, indices};
  }
  const topk = op({topk_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/truncated_normal.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function truncatedNormal_(shape, mean5 = 0, stdDev = 1, dtype, seed) {
    if (dtype != null && dtype === "bool") {
      throw new Error(`Unsupported data type $ { dtype }`);
    }
    const randGauss = new MPRandGauss(mean5, stdDev, dtype, true, seed);
    const res = buffer(shape, dtype);
    for (let i = 0; i < res.values.length; i++) {
      res.values[i] = randGauss.nextValue();
    }
    return res.toTensor();
  }
  const truncatedNormal = op({truncatedNormal_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/unsorted_segment_sum.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function unsortedSegmentSum_(x, segmentIds, numSegments) {
    const $x = convertToTensor(x, "x", "unsortedSegmentSum");
    const $segmentIds = convertToTensor(segmentIds, "segmentIds", "unsortedSegmentSum", "int32");
    assert(isInt(numSegments), () => "numSegments must be of dtype int");
    const inputs = {x: $x, segmentIds: $segmentIds};
    const attrs = {numSegments};
    const forward = (backend2, save) => {
      const res = backend2.unsortedSegmentSum($x, $segmentIds, numSegments);
      save([$segmentIds]);
      return res;
    };
    return ENGINE.runKernelFunc(forward, inputs, null, UnsortedSegmentSum, attrs);
  }
  const unsortedSegmentSum = op({unsortedSegmentSum_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/unstack.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function unstack_(x, axis = 0) {
    const $x = convertToTensor(x, "x", "unstack");
    assert(axis >= -$x.shape.length && axis < $x.shape.length, () => `Axis = ${axis} is not in [-${$x.shape.length}, ${$x.shape.length})`);
    if (axis < 0) {
      axis += $x.shape.length;
    }
    const inputs = {value: $x};
    const attrs = {axis};
    const forward = (backend2) => backend2.unstack($x, axis);
    return ENGINE.runKernelFunc(forward, inputs, null, Unpack, attrs);
  }
  const unstack = op({unstack_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/variable.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function variable(initialValue, trainable = true, name, dtype) {
    return ENGINE.makeVariable(initialValue, trainable, name, dtype);
  }

  // node_modules/@tensorflow/tfjs-core/dist/backends/where_impl.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function whereImpl(condShape, condVals) {
    const indices = [];
    for (let i = 0; i < condVals.length; i++) {
      if (condVals[i]) {
        indices.push(i);
      }
    }
    const inBuffer = buffer(condShape, "int32");
    const out = buffer([indices.length, condShape.length], "int32");
    for (let i = 0; i < indices.length; i++) {
      const loc = inBuffer.indexToLoc(indices[i]);
      const offset = i * condShape.length;
      out.values.set(loc, offset);
    }
    return out.toTensor();
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/where_async.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  async function whereAsync_(condition) {
    const $condition = convertToTensor(condition, "condition", "whereAsync", "bool");
    const vals = await $condition.data();
    const res = whereImpl($condition.shape, vals);
    if (condition !== $condition) {
      $condition.dispose();
    }
    return res;
  }
  const whereAsync = whereAsync_;

  // node_modules/@tensorflow/tfjs-core/dist/ops/boolean_mask.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  async function booleanMaskAsync_(tensor17, mask, axis) {
    const $tensor = convertToTensor(tensor17, "tensor", "boolMask");
    const $mask = convertToTensor(mask, "mask", "boolMask", "bool");
    const axisFrom = axis == null ? 0 : axis;
    const maskDim = $mask.rank;
    const tensorShape = $tensor.shape;
    assert(maskDim > 0, () => "mask cannot be scalar");
    assertShapesMatch(tensorShape.slice(axisFrom, axisFrom + maskDim), $mask.shape, `mask's shape must match the first K dimensions of tensor's shape,`);
    let leadingSize = 1;
    for (let i = axisFrom; i < axisFrom + maskDim; i++) {
      leadingSize *= tensorShape[i];
    }
    const targetTensorShape = tensorShape.slice(0, axisFrom).concat([leadingSize], tensorShape.slice(axisFrom + maskDim));
    const reshapedTensor = reshape($tensor, targetTensorShape);
    const reshapedMask = reshape($mask, [-1]);
    const positivePositions = await whereAsync(reshapedMask);
    const indices = squeeze(positivePositions, [1]);
    const res = gather(reshapedTensor, indices, axisFrom);
    if (tensor17 !== $tensor) {
      $tensor.dispose();
    }
    if (mask !== $mask) {
      $mask.dispose();
    }
    indices.dispose();
    reshapedTensor.dispose();
    reshapedMask.dispose();
    positivePositions.dispose();
    return res;
  }
  const booleanMaskAsync = booleanMaskAsync_;

  // node_modules/@tensorflow/tfjs-core/dist/ops/compare.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function notEqualStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "notEqualStrict");
    const $b = convertToTensor(b, "b", "notEqualStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in notEqualStrict: ");
    return notEqual($a, $b);
  }
  function lessStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "lessStrict");
    const $b = convertToTensor(b, "b", "lessStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in lessStrict: ");
    return less($a, $b);
  }
  function equalStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "equalStrict");
    const $b = convertToTensor(b, "b", "equalStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in equalStrict: ");
    return equal($a, $b);
  }
  function lessEqualStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "lessEqualStrict");
    const $b = convertToTensor(b, "b", "lessEqualStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in lessEqualStrict: ");
    return lessEqual($a, $b);
  }
  function greaterStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "greaterStrict");
    const $b = convertToTensor(b, "b", "greaterStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in greaterStrict: ");
    return greater($a, $b);
  }
  function greaterEqualStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "greaterEqualStrict");
    const $b = convertToTensor(b, "b", "greaterEqualStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in greaterEqualStrict: ");
    return greaterEqual($a, $b);
  }
  const equalStrict = op({equalStrict_});
  const greaterEqualStrict = op({greaterEqualStrict_});
  const greaterStrict = op({greaterStrict_});
  const lessEqualStrict = op({lessEqualStrict_});
  const lessStrict = op({lessStrict_});
  const notEqualStrict = op({notEqualStrict_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/binary_ops.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function addStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "addStrict");
    const $b = convertToTensor(b, "b", "addStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in addStrict: ");
    return add2($a, $b);
  }
  function subStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "subStrict");
    const $b = convertToTensor(b, "b", "subStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in subStrict: ");
    return sub($a, $b);
  }
  function powStrict_(base, exp11) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    assertShapesMatch(base.shape, exp11.shape, "Error in powStrict: ");
    return pow(base, exp11);
  }
  function mulStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "mul");
    const $b = convertToTensor(b, "b", "mul");
    assertShapesMatch($a.shape, $b.shape, "Error in multiplyStrict: ");
    return mul($a, $b);
  }
  function divStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "div");
    const $b = convertToTensor(b, "b", "div");
    assertShapesMatch($a.shape, $b.shape, "Error in divideStrict: ");
    return div($a, $b);
  }
  function modStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "modStrict");
    const $b = convertToTensor(b, "b", "modStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in modStrict: ");
    return mod($a, $b);
  }
  function minimumStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "minimumStrict");
    const $b = convertToTensor(b, "b", "minimumStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in minimumStrict: ");
    return minimum($a, $b);
  }
  function maximumStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "maximumStrict");
    const $b = convertToTensor(b, "b", "maximumStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in maximumStrict: ");
    return maximum($a, $b);
  }
  function squaredDifferenceStrict_(a, b) {
    deprecationWarn("strict variants of ops have been deprecated and will be removed in future");
    const $a = convertToTensor(a, "a", "squaredDifferenceStrict");
    const $b = convertToTensor(b, "b", "squaredDifferenceStrict");
    assertShapesMatch($a.shape, $b.shape, "Error in squaredDifferenceStrict: ");
    return squaredDifference($a, $b);
  }
  const addStrict = op({addStrict_});
  const divStrict = op({divStrict_});
  const maximumStrict = op({maximumStrict_});
  const minimumStrict = op({minimumStrict_});
  const modStrict = op({modStrict_});
  const mulStrict = op({mulStrict_});
  const powStrict = op({powStrict_});
  const squaredDifferenceStrict = op({squaredDifferenceStrict_});
  const subStrict = op({subStrict_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/norm.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function norm_(x, ord = "euclidean", axis = null, keepDims = false) {
    x = convertToTensor(x, "x", "norm");
    const norm4 = normImpl(x, ord, axis);
    let keepDimsShape = norm4.shape;
    if (keepDims) {
      const axes = parseAxisParam(axis, x.shape);
      keepDimsShape = expandShapeToKeepDim(norm4.shape, axes);
    }
    return reshape(norm4, keepDimsShape);
  }
  function normImpl(x, p, axis = null) {
    if (x.rank === 0) {
      return abs(x);
    }
    if (x.rank !== 1 && axis === null) {
      return normImpl(reshape(x, [-1]), p, axis);
    }
    if (x.rank === 1 || typeof axis === "number" || Array.isArray(axis) && axis.length === 1) {
      if (p === 1) {
        return sum2(abs(x), axis);
      }
      if (p === Infinity) {
        return max(abs(x), axis);
      }
      if (p === -Infinity) {
        return min(abs(x), axis);
      }
      if (p === "euclidean" || p === 2) {
        return sqrt(sum2(pow(abs(x), scalar(2, "int32")), axis));
      }
      throw new Error(`Error in norm: invalid ord value: ${p}`);
    }
    if (Array.isArray(axis) && axis.length === 2) {
      if (p === 1) {
        return max(sum2(abs(x), axis[0]), axis[1] - 1);
      }
      if (p === Infinity) {
        return max(sum2(abs(x), axis[1]), axis[0]);
      }
      if (p === -Infinity) {
        return min(sum2(abs(x), axis[1]), axis[0]);
      }
      if (p === "fro" || p === "euclidean") {
        return sqrt(sum2(square(x), axis));
      }
      throw new Error(`Error in norm: invalid ord value: ${p}`);
    }
    throw new Error(`Error in norm: invalid axis: ${axis}`);
  }
  const norm = op({norm_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/moving_average.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function movingAverage_(v, x, decay, step7, zeroDebias = true) {
    const $v = convertToTensor(v, "v", "movingAverage");
    const $x = convertToTensor(x, "x", "movingAverage");
    const $decay = convertToTensor(decay, "decay", "movingAverage");
    assertTypesMatch($v, $x);
    assert(arraysEqual($v.shape, $x.shape), () => "Shape mismatch in v and x");
    const one = scalar(1);
    const oneMinusDecay = sub(one, $decay);
    let update = mul(sub($x, $v), oneMinusDecay);
    if (zeroDebias) {
      assert(step7 != null, () => "When using zeroDebias: true, step is required.");
      const $step = convertToTensor(step7, "step", "movingAverage");
      update = div(update, sub(one, pow($decay, $step)));
    }
    return add2($v, update);
  }
  const movingAverage = op({movingAverage_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/scatter_nd_util.js
  const scatter_nd_util_exports = {};
  __export(scatter_nd_util_exports, {
    calculateShapes: () => calculateShapes,
    validateInput: () => validateInput,
    validateUpdateShape: () => validateUpdateShape
  });
  function validateUpdateShape(shape, indices, updates) {
    const sliceDim = indices.rank > 1 ? indices.shape[indices.rank - 1] : 1;
    const batchDim = indices.rank > 1 ? indices.rank - 1 : 1;
    const shapeError = `Must have updates.shape = indices.shape[:batchDim] + shape[sliceDim:], got updates.shape: ${updates.shape}, indices.shape: ${indices.shape}, shape: ${shape}, sliceDim: ${sliceDim}, and batchDim: ${batchDim}.`;
    if (updates.rank < batchDim) {
      throw new Error(shapeError + ` update.rank < ${batchDim}. `);
    }
    if (shape.length < sliceDim + (updates.rank - batchDim)) {
      throw new Error(shapeError + ` Output shape length < ${sliceDim + (updates.rank - batchDim)}`);
    }
    if (updates.rank !== batchDim + shape.length - sliceDim) {
      throw new Error(shapeError + ` update.rank != ${batchDim + shape.length - sliceDim}`);
    }
    for (let d = 0; d < batchDim; ++d) {
      if (updates.shape[d] !== indices.shape[d]) {
        throw new Error(shapeError + ` updates.shape[${d}] (${updates.shape[d]}) != indices.shape[${d}] (${indices.shape[d]}).`);
      }
    }
    for (let d = 0; d < updates.rank - batchDim; ++d) {
      if (updates.shape[d + batchDim] !== shape[d + sliceDim]) {
        throw new Error(shapeError + ` updates.shape[${d + batchDim}] (${updates.shape[d + batchDim]}) != shape[${d + batchDim}] (${shape[d + batchDim]})`);
      }
    }
  }
  function validateInput(updates, indices, shape) {
    if (indices.rank < 1) {
      throw new Error(`tf.scatterND() expects the indices to be rank 1 or higher, but the rank was ${indices.rank}.`);
    }
    if (updates.rank < 1) {
      throw new Error(`tf.scatterND() expects the updates to be rank 1 or higher, but the rank was ${updates.rank}.`);
    }
    if (indices.dtype !== "int32") {
      throw new Error(`The dtype of 'indices' should be int32, but got dtype: ${indices.dtype}`);
    }
    if (shape.length < 1) {
      throw new Error(`Output rank must be greater or equal to 1, but got shape: ${shape}`);
    }
    if (shape.length === 0) {
      if (indices.size === 0) {
        throw new Error(`Indices specified for empty output. indices shape: ${indices.shape}`);
      }
      if (updates.size === 0) {
        throw new Error(`Updates specified for empty output. updates shape: ${updates.shape}`);
      }
    }
    validateUpdateShape(shape, indices, updates);
  }
  function calculateShapes(updates, indices, shape) {
    const indicesRank = indices.shape.length;
    const sliceRank = indicesRank > 1 ? indices.shape[indicesRank - 1] : 1;
    const totalNd = shape.length;
    let sliceSize = 1;
    for (let i = sliceRank; i < totalNd; ++i) {
      sliceSize *= shape[i];
    }
    const safeSliceDim = sliceRank < 1 ? 1 : sliceRank;
    const numUpdates = sizeFromShape(indices.shape) / safeSliceDim;
    const strides = [...computeStrides(shape.slice(0, sliceRank)), 1];
    const outputSize = sizeFromShape(shape);
    return {sliceRank, numUpdates, sliceSize, strides, outputSize};
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/scatter_nd.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function scatterND_(indices, updates, shape) {
    const $indices = convertToTensor(indices, "indices", "scatterND", "int32");
    const $updates = convertToTensor(updates, "updates", "scatterND");
    validateInput($updates, $indices, shape);
    const forward = (backend2) => {
      return backend2.scatterND($indices, $updates, shape);
    };
    const inputs = {indices: $indices, updates: $updates};
    const attrs = {shape};
    return ENGINE.runKernelFunc(forward, inputs, null, ScatterNd, attrs);
  }
  const scatterND = op({scatterND_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/sparse_to_dense_util.js
  function validateInput2(sparseIndices, sparseValues, outputShape, defaultValues) {
    if (sparseIndices.dtype !== "int32") {
      throw new Error(`tf.sparseToDense() expects the indices to be int32 type, but the dtype was ${sparseIndices.dtype}.`);
    }
    if (sparseIndices.rank > 2) {
      throw new Error(`sparseIndices should be a scalar, vector, or matrix, but got shape ${sparseIndices.shape}.`);
    }
    const numElems = sparseIndices.rank > 0 ? sparseIndices.shape[0] : 1;
    const numDims = sparseIndices.rank > 1 ? sparseIndices.shape[1] : 1;
    if (outputShape.length !== numDims) {
      throw new Error(`outputShape has incorrect number of elements:, ${outputShape.length}, should be: ${numDims}.`);
    }
    const numValues = sparseValues.size;
    if (!(sparseValues.rank === 0 || sparseValues.rank === 1 && numValues === numElems)) {
      throw new Error(`sparseValues has incorrect shape ${sparseValues.shape}, should be [] or [${numElems}]`);
    }
    if (sparseValues.dtype !== defaultValues.dtype) {
      throw new Error("sparseValues.dtype must match defaultValues.dtype");
    }
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/sparse_to_dense.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function sparseToDense_(sparseIndices, sparseValues, outputShape, defaultValue = 0) {
    const $sparseIndices = convertToTensor(sparseIndices, "sparseIndices", "sparseToDense", "int32");
    const $sparseValues = convertToTensor(sparseValues, "sparseValues", "sparseToDense");
    const $defaultValue = convertToTensor(defaultValue, "defaultValue", "sparseToDense", $sparseValues.dtype);
    validateInput2($sparseIndices, $sparseValues, outputShape, $defaultValue);
    const inputs = {
      sparseIndices: $sparseIndices,
      sparseValues: $sparseValues,
      defaultValue: $defaultValue
    };
    const attrs = {outputShape};
    return ENGINE.runKernelFunc((backend2) => backend2.sparseToDense($sparseIndices, $sparseValues, outputShape, $defaultValue), inputs, null, SparseToDense, attrs);
  }
  const sparseToDense = op({sparseToDense_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/gather_nd.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function gatherND_(x, indices) {
    const $indices = convertToTensor(indices, "indices", "gatherND", "int32");
    const $x = convertToTensor(x, "x", "gatherND");
    const forward = (backend2) => {
      return backend2.gatherND($x, $indices);
    };
    const inputs = {params: $x, indices: $indices};
    return ENGINE.runKernelFunc(forward, inputs, null, GatherNd);
  }
  const gatherND = op({gatherND_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/dropout_util.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function getNoiseShape(x, noiseShape) {
    if (noiseShape == null) {
      return x.shape.slice();
    }
    if (arraysEqual(x.shape, noiseShape)) {
      return noiseShape;
    }
    if (x.shape.length === noiseShape.length) {
      const newDimension = [];
      for (let i = 0; i < x.shape.length; i++) {
        if (noiseShape[i] == null && x.shape[i] != null) {
          newDimension.push(x.shape[i]);
        } else {
          newDimension.push(noiseShape[i]);
        }
      }
      return newDimension;
    }
    return noiseShape;
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/dropout.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function dropout_(x, rate, noiseShape, seed) {
    const $x = convertToTensor(x, "x", "dropout");
    assert($x.dtype === "float32", () => `x has to be a floating point tensor since it's going to be scaled, but got a ${$x.dtype} tensor instead.`);
    assert(rate >= 0 && rate < 1, () => `rate must be a float in the range [0, 1), but got ${rate}.`);
    if (rate === 0) {
      return x instanceof Tensor ? $x.clone() : $x;
    }
    const $noiseShape = getNoiseShape($x, noiseShape);
    const keepProb = 1 - rate;
    const multiplier = div(floor(add2(randomUniform($noiseShape, 0, 1, "float32", seed), keepProb)), keepProb);
    return mul($x, multiplier);
  }
  const dropout = op({dropout_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/signal_ops_util.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function enclosingPowerOfTwo(value) {
    return Math.floor(Math.pow(2, Math.ceil(Math.log(value) / Math.log(2))));
  }
  function cosineWindow(windowLength, a, b) {
    const even = 1 - windowLength % 2;
    const newValues = new Float32Array(windowLength);
    for (let i = 0; i < windowLength; ++i) {
      const cosArg = 2 * Math.PI * i / (windowLength + even - 1);
      newValues[i] = a - b * Math.cos(cosArg);
    }
    return tensor1d(newValues, "float32");
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/in_top_k.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  async function inTopKAsync_(predictions, targets, k = 1) {
    const $predictions = convertToTensor(predictions, "predictions", "inTopK");
    const $targets = convertToTensor(targets, "targets", "inTopK");
    assert($predictions.rank > 1, () => `inTopK() expects the predictions to be of rank 2 or higher, but got ${$predictions.rank}`);
    assert($predictions.rank - 1 === $targets.rank, () => `predictions rank should be 1 larger than targets rank, but got predictions rank ${$predictions.rank} and targets rank ${$targets.rank}`);
    assertShapesMatch($predictions.shape.slice(0, $predictions.shape.length - 1), $targets.shape, `predictions's shape should be align with the targets' shape, except the last dimension.`);
    const lastDim = $predictions.shape[$predictions.shape.length - 1];
    assert(k > 0 && k <= lastDim, () => `'k' passed to inTopK() must be > 0 && <= the predictions last dimension (${lastDim}), but got ${k}`);
    const predictionsVals = await $predictions.data();
    const targetsVals = await $targets.data();
    const [batch, size] = [predictionsVals.length / lastDim, lastDim];
    const precision = getTypedArrayFromDType("bool", batch);
    for (let b = 0; b < batch; b++) {
      const offset = b * size;
      const vals = predictionsVals.subarray(offset, offset + size);
      const valAndInd = [];
      for (let i = 0; i < vals.length; i++) {
        valAndInd.push({value: vals[i], index: i});
      }
      valAndInd.sort((a, b2) => b2.value - a.value);
      precision[b] = 0;
      for (let i = 0; i < k; i++) {
        if (valAndInd[i].index === targetsVals[b]) {
          precision[b] = 1;
          break;
        }
      }
    }
    if (predictions !== $predictions) {
      $predictions.dispose();
    }
    if (targets !== $targets) {
      $targets.dispose();
    }
    return tensor5(precision, $targets.shape, "bool");
  }
  const inTopKAsync = inTopKAsync_;

  // node_modules/@tensorflow/tfjs-core/dist/ops/conv2d_backprop_filter.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function conv2DBackpropFilter_(x, dy, filterShape, strides, pad8, dataFormat = "NHWC", dimRoundingMode) {
    let x4D = x;
    if (x.rank === 3) {
      x4D = reshape(x, [1, x.shape[0], x.shape[1], x.shape[2]]);
    }
    let dy4D = dy;
    if (dy4D.rank === 3) {
      dy4D = reshape(dy, [1, dy.shape[0], dy.shape[1], dy.shape[2]]);
    }
    assert(x4D.rank === 4, () => `Error in conv2dDerFilter: input must be rank 4, but got shape ${x4D.shape}.`);
    assert(dy4D.rank === 4, () => `Error in conv2dDerFilter: dy must be rank 4, but got shape ${dy4D.shape}.`);
    assert(filterShape.length === 4, () => `Error in conv2dDerFilter: filterShape must be length 4, but got ${filterShape}.`);
    const inDepth = dataFormat === "NHWC" ? x4D.shape[3] : x4D.shape[1];
    const outDepth = dataFormat === "NHWC" ? dy4D.shape[3] : dy4D.shape[1];
    assert(inDepth === filterShape[2], () => `Error in conv2dDerFilter: depth of input ${inDepth}) must match input depth in filter (${filterShape[2]}.`);
    assert(outDepth === filterShape[3], () => `Error in conv2dDerFilter: depth of dy (${outDepth}) must match output depth for filter (${filterShape[3]}).`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in conv2dDerFilter: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const forward = (backend2) => {
      const dilations = 1;
      const $dataFormat = convertConv2DDataFormat(dataFormat);
      const convInfo = computeConv2DInfo(x4D.shape, filterShape, strides, dilations, pad8, dimRoundingMode, false, $dataFormat);
      return backend2.conv2dDerFilter(x4D, dy4D, convInfo);
    };
    const inputs = {x: x4D, dy: dy4D};
    const attrs = {strides, pad: pad8, dataFormat, dimRoundingMode};
    return ENGINE.runKernelFunc(forward, inputs, null, Conv2DBackpropFilter, attrs);
  }
  const conv2DBackpropFilter = op({conv2DBackpropFilter_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/fused_util.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function getFusedDyActivation(dy, y, activation) {
    if (activation == null || activation === "linear") {
      return dy;
    }
    if (activation === "relu") {
      return mul(dy, step(y));
    }
    throw new Error(`Cannot compute gradient for fused activation ${activation}.`);
  }
  function getFusedBiasGradient(bias, dyActivation) {
    let res = dyActivation;
    const reduceAxes = getReductionAxes(bias.shape, dyActivation.shape);
    if (reduceAxes.length > 0) {
      res = sum2(res, reduceAxes);
    }
    return reshape(res, bias.shape);
  }
  function applyActivation(x, activation, preluActivationWeights) {
    if (activation === "linear") {
      return x;
    } else if (activation === "relu") {
      return relu(x);
    } else if (activation === "elu") {
      return elu(x);
    } else if (activation === "relu6") {
      return relu6(x);
    } else if (activation === "prelu") {
      return prelu(x, preluActivationWeights);
    }
    throw new Error(`Unknown fused activation ${activation}.`);
  }
  const shouldFuse = (gradientDepth, activation) => {
    const gradientMode = gradientDepth > 0;
    return !gradientMode || activation === "linear";
  };

  // node_modules/@tensorflow/tfjs-core/dist/ops/fused_conv2d.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function fusedConv2d_({x, filter, strides, pad: pad8, dataFormat = "NHWC", dilations = [1, 1], dimRoundingMode, bias, activation = "linear", preluActivationWeights}) {
    activation = activation || "linear";
    if (shouldFuse(ENGINE.state.gradientDepth, activation) === false) {
      let result = conv2d(x, filter, strides, pad8, dataFormat, dilations, dimRoundingMode);
      if (bias != null) {
        result = add2(result, bias);
      }
      return applyActivation(result, activation, preluActivationWeights);
    }
    const $x = convertToTensor(x, "x", "conv2d");
    const $filter = convertToTensor(filter, "filter", "conv2d");
    let x4D = $x;
    let reshapedTo4D = false;
    if ($x.rank === 3) {
      reshapedTo4D = true;
      x4D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2]]);
    }
    assert(x4D.rank === 4, () => `Error in fused conv2d: input must be rank 4, but got rank ${x4D.rank}.`);
    assert($filter.rank === 4, () => `Error in fused conv2d: filter must be rank 4, but got rank ${$filter.rank}.`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in fused conv2d: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    assert(x4D.shape[3] === $filter.shape[2], () => `Error in conv2d: depth of input (${x4D.shape[3]}) must match input depth for filter ${$filter.shape[2]}.`);
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in conv2D: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    assert(dataFormat === "NHWC", () => `Error in conv2d: got dataFormat of ${dataFormat} but only NHWC is currently supported.`);
    const convInfo = computeConv2DInfo(x4D.shape, $filter.shape, strides, dilations, pad8, dimRoundingMode);
    let $bias;
    if (bias != null) {
      $bias = convertToTensor(bias, "bias", "fused conv2d");
      [$bias] = makeTypesMatch($bias, $x);
      assertAndGetBroadcastShape(convInfo.outShape, $bias.shape);
    }
    let $preluActivationWeights;
    if (preluActivationWeights != null) {
      $preluActivationWeights = convertToTensor(preluActivationWeights, "prelu weights", "fused conv2d");
    }
    const grad2 = (dy, saved) => {
      const [$filter2, x4D2, y, $bias2] = saved;
      const dyActivation = getFusedDyActivation(dy, y, activation);
      assert(tupleValuesAreOne(dilations), () => `Error in gradient of fused conv2D: dilation rates greater than 1 are not yet supported in gradients. Got dilations '${dilations}'`);
      const xDer = conv2DBackpropInput(x4D2.shape, dyActivation, $filter2, strides, pad8);
      const filterDer = conv2DBackpropFilter(x4D2, dyActivation, $filter2.shape, strides, pad8);
      const der = [xDer, filterDer];
      if ($bias2 != null) {
        const biasDer = getFusedBiasGradient($bias2, dyActivation);
        der.push(biasDer);
      }
      return der;
    };
    const forward = (backend2) => {
      const res = backend2.fusedConv2d({
        input: x4D,
        filter: $filter,
        convInfo,
        bias: $bias,
        activation,
        preluActivationWeights: $preluActivationWeights
      });
      return res;
    };
    const inputs = {
      x: x4D,
      filter: $filter,
      bias: $bias,
      preluActivationWeights: $preluActivationWeights
    };
    const attrs = {strides, pad: pad8, dataFormat, dilations, dimRoundingMode, activation};
    if (bias == null) {
      const customOp = customGrad((x4D2, filter2, save) => {
        let res = ENGINE.runKernelFunc(forward, inputs, null, FusedConv2D, attrs);
        save([filter2, x4D2, res]);
        if (reshapedTo4D) {
          res = reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
        }
        return {value: res, gradFunc: grad2};
      });
      return customOp(x4D, $filter);
    } else {
      const customOpWithBias = customGrad((x4D2, filter2, bias2, save) => {
        let res = ENGINE.runKernelFunc(forward, inputs, null, FusedConv2D, attrs);
        save([filter2, x4D2, res, bias2]);
        if (reshapedTo4D) {
          res = reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
        }
        return {value: res, gradFunc: grad2};
      });
      return customOpWithBias(x4D, $filter, $bias);
    }
  }
  const conv2d5 = op({fusedConv2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/depthwise_conv2d_native_backprop_filter.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function depthwiseConv2dNativeBackpropFilter_(x, dy, filterShape, convInfo) {
    let x4D = x;
    if (x.rank === 3) {
      x4D = reshape(x, [1, x.shape[0], x.shape[1], x.shape[2]]);
    }
    let dy4D = dy;
    if (dy4D.rank === 3) {
      dy4D = reshape(dy, [1, dy.shape[0], dy.shape[1], dy.shape[2]]);
    }
    const forward = (backend2) => backend2.depthwiseConv2DDerFilter(x4D, dy4D, convInfo);
    const inputs = {x: x4D, dy: dy4D};
    return ENGINE.runKernelFunc(forward, inputs, null, DepthwiseConv2dNativeBackpropFilter);
  }
  const depthwiseConv2dNativeBackpropFilter = op({depthwiseConv2dNativeBackpropFilter_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/depthwise_conv2d_native_backprop_input.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function depthwiseConv2dNativeBackpropInput_(xShape, dy, filter, convInfo) {
    let dy4D = dy;
    let reshapedTo4D = false;
    if (dy.rank === 3) {
      reshapedTo4D = true;
      dy4D = reshape(dy, [1, dy.shape[0], dy.shape[1], dy.shape[2]]);
    }
    const forward = (backend2) => backend2.depthwiseConv2DDerInput(dy4D, filter, convInfo);
    const inputs = {dy: dy4D};
    const res = ENGINE.runKernelFunc(forward, inputs, null, DepthwiseConv2dNativeBackpropInput);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const depthwiseConv2dNativeBackpropInput = op({depthwiseConv2dNativeBackpropInput_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/fused_depthwise_conv2d.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function fusedDepthwiseConv2d_({x, filter, strides, pad: pad8, dataFormat = "NHWC", dilations = [1, 1], dimRoundingMode, bias, activation = "linear", preluActivationWeights}) {
    if (shouldFuse(ENGINE.state.gradientDepth, activation) === false) {
      let result = depthwiseConv2d(x, filter, strides, pad8, dataFormat, dilations, dimRoundingMode);
      if (bias != null) {
        result = add2(result, bias);
      }
      return applyActivation(result, activation, preluActivationWeights);
    }
    const $x = convertToTensor(x, "x", "depthwiseConv2d");
    const $filter = convertToTensor(filter, "filter", "depthwiseConv2d");
    let x4D = $x;
    let reshapedTo4D = false;
    if ($x.rank === 3) {
      reshapedTo4D = true;
      x4D = reshape($x, [1, $x.shape[0], $x.shape[1], $x.shape[2]]);
    }
    assert(x4D.rank === 4, () => `Error in fused depthwiseConv2d: input must be rank 4, but got rank ${x4D.rank}.`);
    assert($filter.rank === 4, () => `Error in fused depthwiseConv2d: filter must be rank 4, but got rank ${$filter.rank}.`);
    assert(x4D.shape[3] === $filter.shape[2], () => `Error in fused depthwiseConv2d: number of input channels (${x4D.shape[3]}) must match the inChannels dimension in filter ${$filter.shape[2]}.`);
    if (dilations == null) {
      dilations = [1, 1];
    }
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in fused depthwiseConv2d: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in fused depthwiseConv2d: pad must be an integer when using dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const convInfo = computeConv2DInfo(x4D.shape, $filter.shape, strides, dilations, pad8, dimRoundingMode, true);
    let $bias;
    if (bias != null) {
      $bias = convertToTensor(bias, "bias", "fused conv2d");
      [$bias] = makeTypesMatch($bias, $x);
      assertAndGetBroadcastShape(convInfo.outShape, $bias.shape);
    }
    let $preluActivationWeights;
    if (preluActivationWeights != null) {
      $preluActivationWeights = convertToTensor(preluActivationWeights, "prelu weights", "fused depthwiseConv2d");
    }
    const grad2 = (dy, saved) => {
      assert(tupleValuesAreOne(dilations), () => `Error in gradient of fused depthwiseConv2d: dilation rates greater than 1 are not yet supported. Got dilations '${dilations}'`);
      const [$filter2, x4D2, y, bias2] = saved;
      const dyActivation = getFusedDyActivation(dy, y, activation);
      const xDer = depthwiseConv2dNativeBackpropInput(x4D2.shape, dyActivation, $filter2, convInfo);
      const filterDer = depthwiseConv2dNativeBackpropFilter(x4D2, dyActivation, $filter2.shape, convInfo);
      if (bias2 != null) {
        const biasDer = getFusedBiasGradient($bias, dyActivation);
        return [xDer, filterDer, biasDer];
      }
      return [xDer, filterDer];
    };
    const forward = (backend2) => {
      const res = backend2.fusedDepthwiseConv2D({
        input: x4D,
        filter: $filter,
        convInfo,
        bias: $bias,
        activation,
        preluActivationWeights: $preluActivationWeights
      });
      return res;
    };
    const inputs = {
      x: x4D,
      filter: $filter,
      bias: $bias,
      preluActivationWeights: $preluActivationWeights
    };
    const attrs = {strides, pad: pad8, dataFormat, dilations, dimRoundingMode, activation};
    if (bias == null) {
      const customOp = customGrad((x4D2, filter2, save) => {
        let res = ENGINE.runKernelFunc(forward, inputs, null, FusedDepthwiseConv2D, attrs);
        save([filter2, x4D2, res]);
        if (reshapedTo4D) {
          res = reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
        }
        return {value: res, gradFunc: grad2};
      });
      return customOp(x4D, $filter);
    } else {
      const customOpWithBias = customGrad((x4D2, filter2, bias2, save) => {
        let res = ENGINE.runKernelFunc(forward, inputs, null, FusedDepthwiseConv2D, attrs);
        save([filter2, x4D2, res, bias2]);
        if (reshapedTo4D) {
          res = reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
        }
        return {value: res, gradFunc: grad2};
      });
      return customOpWithBias(x4D, $filter, $bias);
    }
  }
  const depthwiseConv2d2 = op({fusedDepthwiseConv2d_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/fused_mat_mul.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function fusedMatMul_({a, b, transposeA = false, transposeB = false, bias, activation = "linear", preluActivationWeights}) {
    if (shouldFuse(ENGINE.state.gradientDepth, activation) === false) {
      let result = matMul(a, b, transposeA, transposeB);
      if (bias != null) {
        result = add2(result, bias);
      }
      return applyActivation(result, activation, preluActivationWeights);
    }
    let $a = convertToTensor(a, "a", "fused matMul");
    let $b = convertToTensor(b, "b", "fused matMul");
    [$a, $b] = makeTypesMatch($a, $b);
    const innerShapeA = transposeA ? $a.shape[$a.rank - 2] : $a.shape[$a.rank - 1];
    const innerShapeB = transposeB ? $b.shape[$b.rank - 1] : $b.shape[$b.rank - 2];
    const outerShapeA = transposeA ? $a.shape[$a.rank - 1] : $a.shape[$a.rank - 2];
    const outerShapeB = transposeB ? $b.shape[$b.rank - 2] : $b.shape[$b.rank - 1];
    const outerDimsA = $a.shape.slice(0, -2);
    const outerDimsB = $b.shape.slice(0, -2);
    const batchDimA = sizeFromShape(outerDimsA);
    const batchDimB = sizeFromShape(outerDimsB);
    assert($a.rank >= 2 && $b.rank >= 2 && $a.rank === $b.rank, () => `Error in fused matMul: inputs must have the same rank of at least 2, got ranks ${$a.rank} and ${$b.rank}.`);
    assert(arraysEqual(outerDimsA, outerDimsB), () => `Error in fused matMul: outer dimensions (${outerDimsA}) and (${outerDimsB}) of Tensors with shapes ${$a.shape} and ${$b.shape} must match.`);
    assert(innerShapeA === innerShapeB, () => `Error in fused matMul: inner shapes (${innerShapeA}) and (${innerShapeB}) of Tensors with shapes ${$a.shape} and ${$b.shape} and transposeA=${transposeA} and transposeB=${transposeB} must match.`);
    const outShape = $a.shape.slice(0, -2).concat([outerShapeA, outerShapeB]);
    const a3D = transposeA ? reshape($a, [batchDimA, innerShapeA, outerShapeA]) : reshape($a, [batchDimA, outerShapeA, innerShapeA]);
    const b3D = transposeB ? reshape($b, [batchDimB, outerShapeB, innerShapeB]) : reshape($b, [batchDimB, innerShapeB, outerShapeB]);
    let $bias;
    if (bias != null) {
      $bias = convertToTensor(bias, "bias", "fused matMul");
      [$bias] = makeTypesMatch($bias, $a);
      assertAndGetBroadcastShape(outShape, $bias.shape);
    }
    let $preluActivationWeights;
    if (preluActivationWeights != null) {
      $preluActivationWeights = convertToTensor(preluActivationWeights, "prelu weights", "fused matMul");
    }
    const grad2 = (dy, saved) => {
      const [a3D2, b3D2, y, $bias2] = saved;
      const dyActivation = getFusedDyActivation(reshape(dy, y.shape), y, activation);
      let aDer;
      let bDer;
      if (!transposeA && !transposeB) {
        aDer = matMul(dyActivation, b3D2, false, true);
        bDer = matMul(a3D2, dyActivation, true, false);
      } else if (!transposeA && transposeB) {
        aDer = matMul(dyActivation, b3D2, false, false);
        bDer = matMul(dyActivation, a3D2, true, false);
      } else if (transposeA && !transposeB) {
        aDer = matMul(b3D2, dyActivation, false, true);
        bDer = matMul(a3D2, dyActivation, false, false);
      } else {
        aDer = matMul(b3D2, dyActivation, true, true);
        bDer = matMul(dyActivation, a3D2, true, true);
      }
      if (bias != null) {
        const biasDer = getFusedBiasGradient($bias2, dyActivation);
        return [aDer, bDer, biasDer];
      } else {
        return [aDer, bDer];
      }
    };
    const forward = (backend2) => {
      const y = backend2.fusedBatchMatMul({
        a: a3D,
        b: b3D,
        transposeA,
        transposeB,
        bias: $bias,
        activation,
        preluActivationWeights: $preluActivationWeights
      });
      return y;
    };
    const inputs = {
      a: a3D,
      b: b3D,
      bias: $bias,
      preluActivationWeights: $preluActivationWeights
    };
    const attrs = {transposeA, transposeB, activation};
    if (bias == null) {
      const customOp = customGrad((a3D2, b3D2, save) => {
        const res = ENGINE.runKernelFunc(forward, inputs, null, _FusedMatMul, attrs);
        save([a3D2, b3D2, res]);
        return {value: reshape(res, outShape), gradFunc: grad2};
      });
      return customOp(a3D, b3D);
    } else {
      const customOpWithBias = customGrad((a3D2, b3D2, $bias2, save) => {
        const res = ENGINE.runKernelFunc(forward, inputs, null, _FusedMatMul, attrs);
        save([a3D2, b3D2, res, $bias2]);
        return {value: reshape(res, outShape), gradFunc: grad2};
      });
      return customOpWithBias(a3D, b3D, $bias);
    }
  }
  const matMul2 = op({fusedMatMul_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/fused_ops.js
  const fused_ops_exports = {};
  __export(fused_ops_exports, {
    conv2d: () => conv2d5,
    depthwiseConv2d: () => depthwiseConv2d2,
    matMul: () => matMul2
  });
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */

  // node_modules/@tensorflow/tfjs-core/dist/ops/hamming_window.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function hammingWindow_(windowLength) {
    return cosineWindow(windowLength, 0.54, 0.46);
  }
  const hammingWindow = op({hammingWindow_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/hann_window.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function hannWindow_(windowLength) {
    return cosineWindow(windowLength, 0.5, 0.5);
  }
  const hannWindow = op({hannWindow_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/frame.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function frame_(signal2, frameLength, frameStep, padEnd = false, padValue = 0) {
    let start = 0;
    const output = [];
    while (start + frameLength <= signal2.size) {
      output.push(slice(signal2, start, frameLength));
      start += frameStep;
    }
    if (padEnd) {
      while (start < signal2.size) {
        const padLen = start + frameLength - signal2.size;
        const pad8 = concat([
          slice(signal2, start, frameLength - padLen),
          fill([padLen], padValue)
        ]);
        output.push(pad8);
        start += frameStep;
      }
    }
    if (output.length === 0) {
      return tensor2d([], [0, frameLength]);
    }
    return reshape(concat(output), [output.length, frameLength]);
  }
  const frame = op({frame_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/stft.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function stft_(signal2, frameLength, frameStep, fftLength, windowFn = hannWindow) {
    if (fftLength == null) {
      fftLength = enclosingPowerOfTwo(frameLength);
    }
    const framedSignal = frame(signal2, frameLength, frameStep);
    const windowedSignal = mul(framedSignal, windowFn(frameLength));
    const output = [];
    for (let i = 0; i < framedSignal.shape[0]; i++) {
      output.push(rfft(slice(windowedSignal, [i, 0], [1, frameLength]), fftLength));
    }
    return concat(output);
  }
  const stft = op({stft_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/crop_and_resize.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function cropAndResize_(image2, boxes, boxInd, cropSize, method, extrapolationValue) {
    const $image = convertToTensor(image2, "image", "cropAndResize");
    const $boxes = convertToTensor(boxes, "boxes", "cropAndResize", "float32");
    const $boxInd = convertToTensor(boxInd, "boxInd", "cropAndResize", "int32");
    method = method || "bilinear";
    extrapolationValue = extrapolationValue || 0;
    const numBoxes = $boxes.shape[0];
    assert($image.rank === 4, () => `Error in cropAndResize: image must be rank 4,but got rank ${$image.rank}.`);
    assert($boxes.rank === 2 && $boxes.shape[1] === 4, () => `Error in cropAndResize: boxes must be have size [${numBoxes},4] but had shape ${$boxes.shape}.`);
    assert($boxInd.rank === 1 && $boxInd.shape[0] === numBoxes, () => `Error in cropAndResize: boxInd must be have size [${numBoxes}] but had shape ${$boxes.shape}.`);
    assert(cropSize.length === 2, () => `Error in cropAndResize: cropSize must be of length 2, but got length ${cropSize.length}.`);
    assert(cropSize[0] >= 1 && cropSize[1] >= 1, () => `cropSize must be atleast [1,1], but was ${cropSize}`);
    assert(method === "bilinear" || method === "nearest", () => `method must be bilinear or nearest, but was ${method}`);
    const forward = (backend2) => backend2.cropAndResize($image, $boxes, $boxInd, cropSize, method, extrapolationValue);
    const inputs = {image: $image, boxes: $boxes, boxInd: $boxInd};
    const attrs = {method, extrapolationValue, cropSize};
    const res = ENGINE.runKernelFunc(forward, inputs, null, CropAndResize, attrs);
    return res;
  }
  const cropAndResize = op({cropAndResize_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/flip_left_right.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function flipLeftRight_(image2) {
    const $image = convertToTensor(image2, "image", "flipLeftRight", "float32");
    assert($image.rank === 4, () => `Error in flipLeftRight: image must be rank 4,but got rank ${$image.rank}.`);
    const inputs = {image: $image};
    const res = ENGINE.runKernel(FlipLeftRight, inputs, {});
    return res;
  }
  const flipLeftRight = op({flipLeftRight_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/rotate_with_offset.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function rotateWithOffset_(image2, radians, fillValue = 0, center = 0.5) {
    const $image = convertToTensor(image2, "image", "rotateWithOffset", "float32");
    assert($image.rank === 4, () => `Error in rotateWithOffset: image must be rank 4,but got rank ${$image.rank}.`);
    const inputs = {image: $image};
    const attrs = {radians, fillValue, center};
    const res = ENGINE.runKernel(RotateWithOffset, inputs, attrs);
    return res;
  }
  const rotateWithOffset = op({rotateWithOffset_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/nonmax_util.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function nonMaxSuppSanityCheck(boxes, scores, maxOutputSize, iouThreshold, scoreThreshold, softNmsSigma) {
    if (iouThreshold == null) {
      iouThreshold = 0.5;
    }
    if (scoreThreshold == null) {
      scoreThreshold = Number.NEGATIVE_INFINITY;
    }
    if (softNmsSigma == null) {
      softNmsSigma = 0;
    }
    const numBoxes = boxes.shape[0];
    maxOutputSize = Math.min(maxOutputSize, numBoxes);
    assert(0 <= iouThreshold && iouThreshold <= 1, () => `iouThreshold must be in [0, 1], but was '${iouThreshold}'`);
    assert(boxes.rank === 2, () => `boxes must be a 2D tensor, but was of rank '${boxes.rank}'`);
    assert(boxes.shape[1] === 4, () => `boxes must have 4 columns, but 2nd dimension was ${boxes.shape[1]}`);
    assert(scores.rank === 1, () => "scores must be a 1D tensor");
    assert(scores.shape[0] === numBoxes, () => `scores has incompatible shape with boxes. Expected ${numBoxes}, but was ${scores.shape[0]}`);
    assert(0 <= softNmsSigma && softNmsSigma <= 1, () => `softNmsSigma must be in [0, 1], but was '${softNmsSigma}'`);
    return {maxOutputSize, iouThreshold, scoreThreshold, softNmsSigma};
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/non_max_suppression.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function nonMaxSuppression_(boxes, scores, maxOutputSize, iouThreshold = 0.5, scoreThreshold = Number.NEGATIVE_INFINITY) {
    const $boxes = convertToTensor(boxes, "boxes", "nonMaxSuppression");
    const $scores = convertToTensor(scores, "scores", "nonMaxSuppression");
    const inputs = nonMaxSuppSanityCheck($boxes, $scores, maxOutputSize, iouThreshold, scoreThreshold);
    maxOutputSize = inputs.maxOutputSize;
    iouThreshold = inputs.iouThreshold;
    scoreThreshold = inputs.scoreThreshold;
    const attrs = {maxOutputSize, iouThreshold, scoreThreshold};
    return ENGINE.runKernelFunc((b) => b.nonMaxSuppression($boxes, $scores, maxOutputSize, iouThreshold, scoreThreshold), {boxes: $boxes, scores: $scores}, null, NonMaxSuppressionV3, attrs);
  }
  const nonMaxSuppression = op({nonMaxSuppression_});

  // node_modules/@tensorflow/tfjs-core/dist/backends/array_util.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function binaryInsert(arr, element, comparator) {
    const index = binarySearch(arr, element, comparator);
    const insertionPoint = index < 0 ? -(index + 1) : index;
    arr.splice(insertionPoint, 0, element);
  }
  function binarySearch(arr, target, comparator) {
    return binarySearch_(arr, target, comparator || defaultComparator);
  }
  function defaultComparator(a, b) {
    return a > b ? 1 : a < b ? -1 : 0;
  }
  function binarySearch_(arr, target, comparator) {
    let left = 0;
    let right = arr.length;
    let middle = 0;
    let found = false;
    while (left < right) {
      middle = left + (right - left >>> 1);
      const compareResult = comparator(target, arr[middle]);
      if (compareResult > 0) {
        left = middle + 1;
      } else {
        right = middle;
        found = !compareResult;
      }
    }
    return found ? left : -left - 1;
  }

  // node_modules/@tensorflow/tfjs-core/dist/backends/non_max_suppression_impl.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function nonMaxSuppressionV3Impl(boxes, scores, maxOutputSize, iouThreshold, scoreThreshold) {
    return nonMaxSuppressionImpl_(boxes, scores, maxOutputSize, iouThreshold, scoreThreshold, 0).selectedIndices;
  }
  function nonMaxSuppressionV4Impl(boxes, scores, maxOutputSize, iouThreshold, scoreThreshold, padToMaxOutputSize) {
    return nonMaxSuppressionImpl_(boxes, scores, maxOutputSize, iouThreshold, scoreThreshold, 0, false, padToMaxOutputSize, true);
  }
  function nonMaxSuppressionV5Impl(boxes, scores, maxOutputSize, iouThreshold, scoreThreshold, softNmsSigma) {
    return nonMaxSuppressionImpl_(boxes, scores, maxOutputSize, iouThreshold, scoreThreshold, softNmsSigma, true);
  }
  function nonMaxSuppressionImpl_(boxes, scores, maxOutputSize, iouThreshold, scoreThreshold, softNmsSigma, returnScoresTensor = false, padToMaxOutputSize = false, returnValidOutputs = false) {
    const candidates = [];
    for (let i = 0; i < scores.length; i++) {
      if (scores[i] > scoreThreshold) {
        candidates.push({score: scores[i], boxIndex: i, suppressBeginIndex: 0});
      }
    }
    candidates.sort(ascendingComparator);
    const scale2 = softNmsSigma > 0 ? -0.5 / softNmsSigma : 0;
    const selectedIndices = [];
    const selectedScores = [];
    while (selectedIndices.length < maxOutputSize && candidates.length > 0) {
      const candidate = candidates.pop();
      const {score: originalScore, boxIndex, suppressBeginIndex} = candidate;
      if (originalScore < scoreThreshold) {
        break;
      }
      let ignoreCandidate = false;
      for (let j = selectedIndices.length - 1; j >= suppressBeginIndex; --j) {
        const iou3 = intersectionOverUnion(boxes, boxIndex, selectedIndices[j]);
        if (iou3 >= iouThreshold) {
          ignoreCandidate = true;
          break;
        }
        candidate.score = candidate.score * suppressWeight(iouThreshold, scale2, iou3);
        if (candidate.score <= scoreThreshold) {
          break;
        }
      }
      candidate.suppressBeginIndex = selectedIndices.length;
      if (!ignoreCandidate) {
        if (candidate.score === originalScore) {
          selectedIndices.push(boxIndex);
          selectedScores.push(candidate.score);
        } else if (candidate.score > scoreThreshold) {
          binaryInsert(candidates, candidate, ascendingComparator);
        }
      }
    }
    const validOutputs = selectedIndices.length;
    const elemsToPad = maxOutputSize - validOutputs;
    if (padToMaxOutputSize && elemsToPad > 0) {
      selectedIndices.push(...new Array(elemsToPad).fill(0));
      selectedScores.push(...new Array(elemsToPad).fill(0));
    }
    const result = {selectedIndices: tensor1d(selectedIndices, "int32")};
    if (returnScoresTensor) {
      result["selectedScores"] = tensor1d(selectedScores, "float32");
    }
    if (returnValidOutputs) {
      result["validOutputs"] = scalar(validOutputs, "int32");
    }
    return result;
  }
  function intersectionOverUnion(boxes, i, j) {
    const iCoord = boxes.subarray(i * 4, i * 4 + 4);
    const jCoord = boxes.subarray(j * 4, j * 4 + 4);
    const yminI = Math.min(iCoord[0], iCoord[2]);
    const xminI = Math.min(iCoord[1], iCoord[3]);
    const ymaxI = Math.max(iCoord[0], iCoord[2]);
    const xmaxI = Math.max(iCoord[1], iCoord[3]);
    const yminJ = Math.min(jCoord[0], jCoord[2]);
    const xminJ = Math.min(jCoord[1], jCoord[3]);
    const ymaxJ = Math.max(jCoord[0], jCoord[2]);
    const xmaxJ = Math.max(jCoord[1], jCoord[3]);
    const areaI = (ymaxI - yminI) * (xmaxI - xminI);
    const areaJ = (ymaxJ - yminJ) * (xmaxJ - xminJ);
    if (areaI <= 0 || areaJ <= 0) {
      return 0;
    }
    const intersectionYmin = Math.max(yminI, yminJ);
    const intersectionXmin = Math.max(xminI, xminJ);
    const intersectionYmax = Math.min(ymaxI, ymaxJ);
    const intersectionXmax = Math.min(xmaxI, xmaxJ);
    const intersectionArea = Math.max(intersectionYmax - intersectionYmin, 0) * Math.max(intersectionXmax - intersectionXmin, 0);
    return intersectionArea / (areaI + areaJ - intersectionArea);
  }
  function suppressWeight(iouThreshold, scale2, iou3) {
    const weight = Math.exp(scale2 * iou3 * iou3);
    return iou3 <= iouThreshold ? weight : 0;
  }
  function ascendingComparator(c1, c2) {
    return c1.score - c2.score || c1.score === c2.score && c2.boxIndex - c1.boxIndex;
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/non_max_suppression_async.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  async function nonMaxSuppressionAsync_(boxes, scores, maxOutputSize, iouThreshold = 0.5, scoreThreshold = Number.NEGATIVE_INFINITY) {
    const $boxes = convertToTensor(boxes, "boxes", "nonMaxSuppressionAsync");
    const $scores = convertToTensor(scores, "scores", "nonMaxSuppressionAsync");
    const inputs = nonMaxSuppSanityCheck($boxes, $scores, maxOutputSize, iouThreshold, scoreThreshold);
    maxOutputSize = inputs.maxOutputSize;
    iouThreshold = inputs.iouThreshold;
    scoreThreshold = inputs.scoreThreshold;
    const boxesAndScores = await Promise.all([$boxes.data(), $scores.data()]);
    const boxesVals = boxesAndScores[0];
    const scoresVals = boxesAndScores[1];
    const res = nonMaxSuppressionV3Impl(boxesVals, scoresVals, maxOutputSize, iouThreshold, scoreThreshold);
    if ($boxes !== boxes) {
      $boxes.dispose();
    }
    if ($scores !== scores) {
      $scores.dispose();
    }
    return res;
  }
  const nonMaxSuppressionAsync = nonMaxSuppressionAsync_;

  // node_modules/@tensorflow/tfjs-core/dist/ops/non_max_suppression_with_score.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function nonMaxSuppressionWithScore_(boxes, scores, maxOutputSize, iouThreshold = 0.5, scoreThreshold = Number.NEGATIVE_INFINITY, softNmsSigma = 0) {
    const $boxes = convertToTensor(boxes, "boxes", "nonMaxSuppression");
    const $scores = convertToTensor(scores, "scores", "nonMaxSuppression");
    const params = nonMaxSuppSanityCheck($boxes, $scores, maxOutputSize, iouThreshold, scoreThreshold, softNmsSigma);
    maxOutputSize = params.maxOutputSize;
    iouThreshold = params.iouThreshold;
    scoreThreshold = params.scoreThreshold;
    softNmsSigma = params.softNmsSigma;
    const inputs = {boxes: $boxes, scores: $scores};
    const attrs = {maxOutputSize, iouThreshold, scoreThreshold, softNmsSigma};
    const result = ENGINE.runKernel(NonMaxSuppressionV5, inputs, attrs);
    return {selectedIndices: result[0], selectedScores: result[1]};
  }
  const nonMaxSuppressionWithScore = op({nonMaxSuppressionWithScore_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/non_max_suppression_with_score_async.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  async function nonMaxSuppressionWithScoreAsync_(boxes, scores, maxOutputSize, iouThreshold = 0.5, scoreThreshold = Number.NEGATIVE_INFINITY, softNmsSigma = 0) {
    const $boxes = convertToTensor(boxes, "boxes", "nonMaxSuppressionAsync");
    const $scores = convertToTensor(scores, "scores", "nonMaxSuppressionAsync");
    const params = nonMaxSuppSanityCheck($boxes, $scores, maxOutputSize, iouThreshold, scoreThreshold, softNmsSigma);
    maxOutputSize = params.maxOutputSize;
    iouThreshold = params.iouThreshold;
    scoreThreshold = params.scoreThreshold;
    softNmsSigma = params.softNmsSigma;
    const boxesAndScores = await Promise.all([$boxes.data(), $scores.data()]);
    const boxesVals = boxesAndScores[0];
    const scoresVals = boxesAndScores[1];
    const res = nonMaxSuppressionV5Impl(boxesVals, scoresVals, maxOutputSize, iouThreshold, scoreThreshold, softNmsSigma);
    if ($boxes !== boxes) {
      $boxes.dispose();
    }
    if ($scores !== scores) {
      $scores.dispose();
    }
    return res;
  }
  const nonMaxSuppressionWithScoreAsync = nonMaxSuppressionWithScoreAsync_;

  // node_modules/@tensorflow/tfjs-core/dist/ops/non_max_suppression_padded.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function nonMaxSuppressionPadded_(boxes, scores, maxOutputSize, iouThreshold = 0.5, scoreThreshold = Number.NEGATIVE_INFINITY, padToMaxOutputSize = false) {
    const $boxes = convertToTensor(boxes, "boxes", "nonMaxSuppression");
    const $scores = convertToTensor(scores, "scores", "nonMaxSuppression");
    const params = nonMaxSuppSanityCheck($boxes, $scores, maxOutputSize, iouThreshold, scoreThreshold, null);
    const $maxOutputSize = params.maxOutputSize;
    const $iouThreshold = params.iouThreshold;
    const $scoreThreshold = params.scoreThreshold;
    const inputs = {boxes: $boxes, scores: $scores};
    const attrs = {
      maxOutputSize: $maxOutputSize,
      iouThreshold: $iouThreshold,
      scoreThreshold: $scoreThreshold,
      padToMaxOutputSize
    };
    const result = ENGINE.runKernel(NonMaxSuppressionV4, inputs, attrs);
    return {selectedIndices: result[0], validOutputs: result[1]};
  }
  const nonMaxSuppressionPadded = op({nonMaxSuppressionPadded_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/non_max_suppression_padded_async.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  async function nonMaxSuppressionPaddedAsync_(boxes, scores, maxOutputSize, iouThreshold = 0.5, scoreThreshold = Number.NEGATIVE_INFINITY, padToMaxOutputSize = false) {
    const $boxes = convertToTensor(boxes, "boxes", "nonMaxSuppressionAsync");
    const $scores = convertToTensor(scores, "scores", "nonMaxSuppressionAsync");
    const params = nonMaxSuppSanityCheck($boxes, $scores, maxOutputSize, iouThreshold, scoreThreshold, null);
    const $maxOutputSize = params.maxOutputSize;
    const $iouThreshold = params.iouThreshold;
    const $scoreThreshold = params.scoreThreshold;
    const [boxesVals, scoresVals] = await Promise.all([$boxes.data(), $scores.data()]);
    const res = nonMaxSuppressionV4Impl(boxesVals, scoresVals, $maxOutputSize, $iouThreshold, $scoreThreshold, padToMaxOutputSize);
    if ($boxes !== boxes) {
      $boxes.dispose();
    }
    if ($scores !== scores) {
      $scores.dispose();
    }
    return res;
  }
  const nonMaxSuppressionPaddedAsync = nonMaxSuppressionPaddedAsync_;

  // node_modules/@tensorflow/tfjs-core/dist/ops/resize_bilinear.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function resizeBilinear_(images, size, alignCorners = false) {
    const $images = convertToTensor(images, "images", "resizeBilinear");
    assert($images.rank === 3 || $images.rank === 4, () => `Error in resizeBilinear: x must be rank 3 or 4, but got rank ${$images.rank}.`);
    assert(size.length === 2, () => `Error in resizeBilinear: new shape must 2D, but got shape ${size}.`);
    let batchImages = $images;
    let reshapedTo4D = false;
    if ($images.rank === 3) {
      reshapedTo4D = true;
      batchImages = reshape($images, [1, $images.shape[0], $images.shape[1], $images.shape[2]]);
    }
    const [newHeight, newWidth] = size;
    const forward = (backend2, save) => {
      save([batchImages]);
      return backend2.resizeBilinear(batchImages, newHeight, newWidth, alignCorners);
    };
    const inputs = {images: batchImages};
    const attrs = {alignCorners, size};
    const res = ENGINE.runKernelFunc(forward, inputs, null, ResizeBilinear, attrs);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const resizeBilinear = op({resizeBilinear_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/resize_nearest_neighbor.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function resizeNearestNeighbor_(images, size, alignCorners = false) {
    const $images = convertToTensor(images, "images", "resizeNearestNeighbor");
    assert($images.rank === 3 || $images.rank === 4, () => `Error in resizeNearestNeighbor: x must be rank 3 or 4, but got rank ${$images.rank}.`);
    assert(size.length === 2, () => `Error in resizeNearestNeighbor: new shape must 2D, but got shape ${size}.`);
    assert($images.dtype === "float32" || $images.dtype === "int32", () => "`images` must have `int32` or `float32` as dtype");
    let batchImages = $images;
    let reshapedTo4D = false;
    if ($images.rank === 3) {
      reshapedTo4D = true;
      batchImages = reshape($images, [1, $images.shape[0], $images.shape[1], $images.shape[2]]);
    }
    const [newHeight, newWidth] = size;
    const inputs = {images: batchImages};
    const attrs = {alignCorners, size};
    const forward = (backend2, save) => {
      save([batchImages]);
      return backend2.resizeNearestNeighbor(batchImages, newHeight, newWidth, alignCorners);
    };
    const res = ENGINE.runKernelFunc(forward, inputs, null, ResizeNearestNeighbor, attrs);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const resizeNearestNeighbor = op({resizeNearestNeighbor_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/band_part.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function bandPart_(a, numLower, numUpper) {
    assert(numLower % 1 === 0, () => `bandPart(): numLower must be an integer, got ${numLower}.`);
    assert(numUpper % 1 === 0, () => `bandPart(): numUpper must be an integer, got ${numUpper}.`);
    const $a = convertToTensor(a, "a", "bandPart");
    assert($a.rank >= 2, () => `bandPart(): Rank must be at least 2, got ${$a.rank}.`);
    const shape = $a.shape;
    const [M, N] = $a.shape.slice(-2);
    if (!(numLower <= M)) {
      throw new Error(`bandPart(): numLower (${numLower}) must not be greater than the number of rows (${M}).`);
    }
    if (!(numUpper <= N)) {
      throw new Error(`bandPart(): numUpper (${numUpper}) must not be greater than the number of columns (${N}).`);
    }
    if (numLower < 0) {
      numLower = M;
    }
    if (numUpper < 0) {
      numUpper = N;
    }
    const i = reshape(range(0, M, 1, "int32"), [-1, 1]);
    const j = range(0, N, 1, "int32");
    const ij = sub(i, j);
    const inBand = logicalAnd(lessEqual(ij, scalar(+numLower, "int32")), greaterEqual(ij, scalar(-numUpper, "int32")));
    const zero = zeros([M, N], $a.dtype);
    return reshape(stack(unstack(reshape($a, [-1, M, N])).map((mat) => where(inBand, mat, zero))), shape);
  }
  const bandPart = op({bandPart_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/gram_schmidt.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function gramSchmidt_(xs) {
    let inputIsTensor2D;
    if (Array.isArray(xs)) {
      inputIsTensor2D = false;
      assert(xs != null && xs.length > 0, () => "Gram-Schmidt process: input must not be null, undefined, or empty");
      const dim = xs[0].shape[0];
      for (let i = 1; i < xs.length; ++i) {
        assert(xs[i].shape[0] === dim, () => `Gram-Schmidt: Non-unique lengths found in the input vectors: (${xs[i].shape[0]} vs. ${dim})`);
      }
    } else {
      inputIsTensor2D = true;
      xs = split(xs, xs.shape[0], 0).map((x) => squeeze(x, [0]));
    }
    assert(xs.length <= xs[0].shape[0], () => `Gram-Schmidt: Number of vectors (${xs.length}) exceeds number of dimensions (${xs[0].shape[0]}).`);
    const ys = [];
    const xs1d = xs;
    for (let i = 0; i < xs.length; ++i) {
      ys.push(ENGINE.tidy(() => {
        let x = xs1d[i];
        if (i > 0) {
          for (let j = 0; j < i; ++j) {
            const proj = mul(sum2(mul(ys[j], x)), ys[j]);
            x = sub(x, proj);
          }
        }
        return div(x, norm(x, "euclidean"));
      }));
    }
    if (inputIsTensor2D) {
      return stack(ys, 0);
    } else {
      return ys;
    }
  }
  const gramSchmidt = op({gramSchmidt_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/qr.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function qr_(x, fullMatrices = false) {
    assert(x.rank >= 2, () => `qr() requires input tensor to have a rank >= 2, but got rank ${x.rank}`);
    if (x.rank === 2) {
      return qr2d(x, fullMatrices);
    } else {
      const outerDimsProd = x.shape.slice(0, x.shape.length - 2).reduce((value, prev) => value * prev);
      const x2ds = unstack(reshape(x, [
        outerDimsProd,
        x.shape[x.shape.length - 2],
        x.shape[x.shape.length - 1]
      ]), 0);
      const q2ds = [];
      const r2ds = [];
      x2ds.forEach((x2d) => {
        const [q2d, r2d] = qr2d(x2d, fullMatrices);
        q2ds.push(q2d);
        r2ds.push(r2d);
      });
      const q = reshape(stack(q2ds, 0), x.shape);
      const r = reshape(stack(r2ds, 0), x.shape);
      return [q, r];
    }
  }
  function qr2d(x, fullMatrices = false) {
    return ENGINE.tidy(() => {
      assert(x.shape.length === 2, () => `qr2d() requires a 2D Tensor, but got a ${x.shape.length}D Tensor.`);
      const m = x.shape[0];
      const n = x.shape[1];
      let q = eye(m);
      let r = clone(x);
      const one2D = tensor2d([[1]], [1, 1]);
      let w = clone(one2D);
      const iters = m >= n ? n : m;
      for (let j = 0; j < iters; ++j) {
        const rTemp = r;
        const wTemp = w;
        const qTemp = q;
        [w, r, q] = ENGINE.tidy(() => {
          const rjEnd1 = slice(r, [j, j], [m - j, 1]);
          const normX = norm(rjEnd1);
          const rjj = slice(r, [j, j], [1, 1]);
          const s = where(greater(rjj, 0), tensor2d([[-1]]), tensor2d([[1]]));
          const u1 = sub(rjj, mul(s, normX));
          const wPre = div(rjEnd1, u1);
          if (wPre.shape[0] === 1) {
            w = clone(one2D);
          } else {
            w = concat([
              one2D,
              slice(wPre, [1, 0], [wPre.shape[0] - 1, wPre.shape[1]])
            ], 0);
          }
          const tau = neg(div(matMul(s, u1), normX));
          const rjEndAll = slice(r, [j, 0], [m - j, n]);
          const tauTimesW = mul(tau, w);
          const wT = transpose(w);
          if (j === 0) {
            r = sub(rjEndAll, matMul(tauTimesW, matMul(wT, rjEndAll)));
          } else {
            const rTimesTau = sub(rjEndAll, matMul(tauTimesW, matMul(wT, rjEndAll)));
            r = concat([slice(r, [0, 0], [j, n]), rTimesTau], 0);
          }
          const tawTimesWT = transpose(tauTimesW);
          const qAllJEnd = slice(q, [0, j], [m, q.shape[1] - j]);
          if (j === 0) {
            q = sub(qAllJEnd, matMul(matMul(qAllJEnd, w), tawTimesWT));
          } else {
            const qTimesTau = sub(qAllJEnd, matMul(matMul(qAllJEnd, w), tawTimesWT));
            q = concat([slice(q, [0, 0], [m, j]), qTimesTau], 1);
          }
          return [w, r, q];
        });
        dispose([rTemp, wTemp, qTemp]);
      }
      if (!fullMatrices && m > n) {
        q = slice(q, [0, 0], [m, n]);
        r = slice(r, [0, 0], [n, n]);
      }
      return [q, r];
    });
  }
  const qr = op({qr_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/loss_ops_utils.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  var Reduction;
  (function(Reduction2) {
    Reduction2[Reduction2["NONE"] = 0] = "NONE";
    Reduction2[Reduction2["MEAN"] = 1] = "MEAN";
    Reduction2[Reduction2["SUM"] = 2] = "SUM";
    Reduction2[Reduction2["SUM_BY_NONZERO_WEIGHTS"] = 3] = "SUM_BY_NONZERO_WEIGHTS";
  })(Reduction || (Reduction = {}));

  // node_modules/@tensorflow/tfjs-core/dist/ops/compute_weighted_loss.js
  function computeWeightedLoss_(losses2, weights, reduction = Reduction.SUM_BY_NONZERO_WEIGHTS) {
    const $losses = convertToTensor(losses2, "losses", "computeWeightedLoss");
    let $weights = null;
    if (weights != null) {
      $weights = convertToTensor(weights, "weights", "computeWeightedLoss");
    }
    const weightedLoss = $weights == null ? $losses : mul($losses, $weights);
    if (reduction === Reduction.NONE) {
      return weightedLoss;
    }
    if (reduction === Reduction.SUM) {
      return sum2(weightedLoss);
    }
    if (reduction === Reduction.MEAN) {
      if ($weights == null) {
        return mean(weightedLoss);
      } else {
        const broadcastFactor = $losses.size / $weights.size;
        const result = div(sum2(weightedLoss), sum2($weights));
        return broadcastFactor > 1 ? div(result, scalar(broadcastFactor)) : result;
      }
    }
    if (reduction === Reduction.SUM_BY_NONZERO_WEIGHTS) {
      if ($weights == null) {
        return div(sum2(weightedLoss), scalar($losses.size));
      } else {
        const broadcastedWeights = mul($weights, ones2($losses.shape));
        const numNonZeros = cast(sum2(notEqual(broadcastedWeights, scalar(0))), "float32");
        return div(sum2(weightedLoss), numNonZeros);
      }
    }
    throw Error(`Unknown reduction: ${reduction}`);
  }
  const computeWeightedLoss = op({computeWeightedLoss_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/absolute_difference.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function absoluteDifference_(labels, predictions, weights, reduction = Reduction.SUM_BY_NONZERO_WEIGHTS) {
    const $labels = convertToTensor(labels, "labels", "absoluteDifference");
    const $predictions = convertToTensor(predictions, "predictions", "absoluteDifference");
    let $weights = null;
    if (weights != null) {
      $weights = convertToTensor(weights, "weights", "absoluteDifference");
    }
    assertShapesMatch($labels.shape, $predictions.shape, "Error in absoluteDifference: ");
    const losses2 = abs(sub($labels, $predictions));
    return computeWeightedLoss(losses2, $weights, reduction);
  }
  const absoluteDifference = op({absoluteDifference_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/cosine_distance.js
  function cosineDistance_(labels, predictions, axis, weights, reduction = Reduction.SUM_BY_NONZERO_WEIGHTS) {
    const $labels = convertToTensor(labels, "labels", "cosineDistance");
    const $predictions = convertToTensor(predictions, "predictions", "cosineDistance");
    let $weights = null;
    if (weights != null) {
      $weights = convertToTensor(weights, "weights", "cosineDistance");
    }
    assertShapesMatch($labels.shape, $predictions.shape, "Error in cosineDistance: ");
    const one = scalar(1);
    const losses2 = sub(one, sum2(mul($labels, $predictions), axis, true));
    return computeWeightedLoss(losses2, $weights, reduction);
  }
  const cosineDistance = op({cosineDistance_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/hinge_loss.js
  function hingeLoss_(labels, predictions, weights, reduction = Reduction.SUM_BY_NONZERO_WEIGHTS) {
    let $labels = convertToTensor(labels, "labels", "hingeLoss");
    const $predictions = convertToTensor(predictions, "predictions", "hingeLoss");
    let $weights = null;
    if (weights != null) {
      $weights = convertToTensor(weights, "weights", "hingeLoss");
    }
    assertShapesMatch($labels.shape, $predictions.shape, "Error in hingeLoss: ");
    const one = scalar(1);
    $labels = sub(mul(scalar(2), $labels), one);
    const losses2 = relu(sub(one, mul($labels, $predictions)));
    return computeWeightedLoss(losses2, $weights, reduction);
  }
  const hingeLoss = op({hingeLoss_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/huber_loss.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function huberLoss_(labels, predictions, weights, delta = 1, reduction = Reduction.SUM_BY_NONZERO_WEIGHTS) {
    const $labels = convertToTensor(labels, "labels", "huberLoss");
    const $predictions = convertToTensor(predictions, "predictions", "huberLoss");
    let $weights = null;
    if (weights != null) {
      $weights = convertToTensor(weights, "weights", "huberLoss");
    }
    assertShapesMatch($labels.shape, $predictions.shape, "Error in huberLoss: ");
    const deltaScalar = scalar(delta);
    const error = abs(sub($predictions, $labels));
    const quadratic = minimum(error, deltaScalar);
    const linear = sub(error, quadratic);
    const losses2 = add2(mul(scalar(0.5), square(quadratic)), mul(deltaScalar, linear));
    return computeWeightedLoss(losses2, $weights, reduction);
  }
  const huberLoss = op({huberLoss_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/log_loss.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function logLoss_(labels, predictions, weights, epsilon2 = 1e-7, reduction = Reduction.SUM_BY_NONZERO_WEIGHTS) {
    const $labels = convertToTensor(labels, "labels", "logLoss");
    const $predictions = convertToTensor(predictions, "predictions", "logLoss");
    let $weights = null;
    if (weights != null) {
      $weights = convertToTensor(weights, "weights", "logLoss");
    }
    assertShapesMatch($labels.shape, $predictions.shape, "Error in logLoss: ");
    const one = scalar(1);
    const epsilonScalar = scalar(epsilon2);
    const l1 = neg(mul($labels, log(add2($predictions, epsilonScalar))));
    const l2 = mul(sub(one, $labels), log(add2(sub(one, $predictions), epsilonScalar)));
    const losses2 = sub(l1, l2);
    return computeWeightedLoss(losses2, $weights, reduction);
  }
  const logLoss = op({logLoss_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/mean_squared_error.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function meanSquaredError_(labels, predictions, weights, reduction = Reduction.SUM_BY_NONZERO_WEIGHTS) {
    const $labels = convertToTensor(labels, "labels", "meanSquaredError");
    const $predictions = convertToTensor(predictions, "predictions", "meanSquaredError");
    let $weights = null;
    if (weights != null) {
      $weights = convertToTensor(weights, "weights", "meanSquaredError");
    }
    assertShapesMatch($labels.shape, $predictions.shape, "Error in meanSquaredError: ");
    const losses2 = squaredDifference($labels, $predictions);
    return computeWeightedLoss(losses2, $weights, reduction);
  }
  const meanSquaredError = op({meanSquaredError_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/sigmoid_cross_entropy.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function sigmoidCrossEntropyWithLogits_(labels, logits) {
    const $labels = convertToTensor(labels, "labels", "sigmoidCrossEntropyWithLogits");
    const $logits = convertToTensor(logits, "logits", "sigmoidCrossEntropyWithLogits");
    assertShapesMatch($labels.shape, $logits.shape, "Error in sigmoidCrossEntropyWithLogits: ");
    const maxOutput = relu($logits);
    const outputXTarget = mul($logits, $labels);
    const sigmoidOutput = log1p(exp(neg(abs($logits))));
    return add2(sub(maxOutput, outputXTarget), sigmoidOutput);
  }
  function sigmoidCrossEntropy_(multiClassLabels, logits, weights, labelSmoothing = 0, reduction = Reduction.SUM_BY_NONZERO_WEIGHTS) {
    let $multiClassLabels = convertToTensor(multiClassLabels, "multiClassLabels", "sigmoidCrossEntropy");
    const $logits = convertToTensor(logits, "logits", "sigmoidCrossEntropy");
    let $weights = null;
    if (weights != null) {
      $weights = convertToTensor(weights, "weights", "sigmoidCrossEntropy");
    }
    assertShapesMatch($multiClassLabels.shape, $logits.shape, "Error in sigmoidCrossEntropy: ");
    if (labelSmoothing > 0) {
      const labelSmoothingScalar = scalar(labelSmoothing);
      const one = scalar(1);
      const half = scalar(0.5);
      $multiClassLabels = add2(mul($multiClassLabels, sub(one, labelSmoothingScalar)), mul(half, labelSmoothingScalar));
    }
    const losses2 = sigmoidCrossEntropyWithLogits_($multiClassLabels, $logits);
    return computeWeightedLoss(losses2, $weights, reduction);
  }
  const sigmoidCrossEntropy = op({sigmoidCrossEntropy_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/softmax_cross_entropy.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function softmaxCrossEntropyWithLogits_(labels, logits, dim = -1) {
    if (dim === -1) {
      dim = logits.rank - 1;
    }
    if (dim !== logits.rank - 1) {
      throw Error(`Softmax cross entropy along a non-last dimension is not yet supported. Labels / logits was rank ${logits.rank} and dim was ${dim}`);
    }
    const customOp = customGrad((labels2, logits2, save) => {
      const keepDims = true;
      const lse = logSumExp(logits2, [dim], keepDims);
      const logResult = sub(cast(logits2, "float32"), lse);
      save([labels2, logResult]);
      const costVector = neg(mul(logResult, labels2));
      const value = sum2(costVector, [dim]);
      const gradFunc = (dy, saved) => {
        const [labels3, logResult2] = saved;
        const dyShape = expandShapeToKeepDim(dy.shape, [dim]);
        return [
          mul(reshape(dy, dyShape), sub(cast(labels3, "float32"), exp(logResult2))),
          mul(reshape(dy, dyShape), sub(exp(logResult2), cast(labels3, "float32")))
        ];
      };
      return {value, gradFunc};
    });
    return customOp(labels, logits);
  }
  function softmaxCrossEntropy_(onehotLabels, logits, weights, labelSmoothing = 0, reduction = Reduction.SUM_BY_NONZERO_WEIGHTS) {
    let $onehotLabels = convertToTensor(onehotLabels, "onehotLabels", "softmaxCrossEntropy");
    const $logits = convertToTensor(logits, "logits", "softmaxCrossEntropy");
    let $weights = null;
    if (weights != null) {
      $weights = convertToTensor(weights, "weights", "softmaxCrossEntropy");
    }
    assertShapesMatch($onehotLabels.shape, $logits.shape, "Error in softmaxCrossEntropy: ");
    if (labelSmoothing > 0) {
      const labelSmoothingScalar = scalar(labelSmoothing);
      const one = scalar(1);
      const numClasses = scalar($onehotLabels.shape[1]);
      $onehotLabels = add2(mul($onehotLabels, sub(one, labelSmoothingScalar)), div(labelSmoothingScalar, numClasses));
    }
    const losses2 = softmaxCrossEntropyWithLogits_($onehotLabels, $logits);
    return computeWeightedLoss(losses2, $weights, reduction);
  }
  const softmaxCrossEntropy = op({softmaxCrossEntropy_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/ops.js
  const ops_exports = {};
  __export(ops_exports, {
    abs: () => abs,
    acos: () => acos,
    acosh: () => acosh,
    add: () => add2,
    addN: () => addN,
    addStrict: () => addStrict,
    all: () => all,
    any: () => any,
    argMax: () => argMax,
    argMin: () => argMin,
    asin: () => asin,
    asinh: () => asinh,
    atan: () => atan,
    atan2: () => atan2,
    atanh: () => atanh,
    avgPool: () => avgPool,
    avgPool3d: () => avgPool3d,
    basicLSTMCell: () => basicLSTMCell,
    batchNorm: () => batchNorm,
    batchNorm2d: () => batchNorm2d,
    batchNorm3d: () => batchNorm3d,
    batchNorm4d: () => batchNorm4d,
    batchToSpaceND: () => batchToSpaceND,
    booleanMaskAsync: () => booleanMaskAsync,
    broadcastTo: () => broadcastTo,
    buffer: () => buffer,
    cast: () => cast,
    ceil: () => ceil,
    clipByValue: () => clipByValue,
    clone: () => clone,
    complex: () => complex,
    concat: () => concat,
    concat1d: () => concat1d,
    concat2d: () => concat2d,
    concat3d: () => concat3d,
    concat4d: () => concat4d,
    conv1d: () => conv1d,
    conv2d: () => conv2d,
    conv2dTranspose: () => conv2dTranspose,
    conv3d: () => conv3d,
    conv3dTranspose: () => conv3dTranspose,
    cos: () => cos,
    cosh: () => cosh,
    cosineWindow: () => cosineWindow,
    cumsum: () => cumsum,
    depthToSpace: () => depthToSpace,
    depthwiseConv2d: () => depthwiseConv2d,
    diag: () => diag,
    dilation2d: () => dilation2d,
    div: () => div,
    divNoNan: () => divNoNan,
    divStrict: () => divStrict,
    dot: () => dot,
    dropout: () => dropout,
    elu: () => elu,
    enclosingPowerOfTwo: () => enclosingPowerOfTwo,
    equal: () => equal,
    equalStrict: () => equalStrict,
    erf: () => erf,
    exp: () => exp,
    expandDims: () => expandDims,
    expm1: () => expm1,
    eye: () => eye,
    fft: () => fft,
    fill: () => fill,
    floor: () => floor,
    floorDiv: () => floorDiv,
    fused: () => fused_ops_exports,
    gather: () => gather,
    gatherND: () => gatherND,
    greater: () => greater,
    greaterEqual: () => greaterEqual,
    greaterEqualStrict: () => greaterEqualStrict,
    greaterStrict: () => greaterStrict,
    ifft: () => ifft,
    imag: () => imag,
    image: () => image,
    inTopKAsync: () => inTopKAsync,
    irfft: () => irfft,
    isFinite: () => isFinite2,
    isInf: () => isInf,
    isNaN: () => isNaN2,
    leakyRelu: () => leakyRelu,
    less: () => less,
    lessEqual: () => lessEqual,
    lessEqualStrict: () => lessEqualStrict,
    lessStrict: () => lessStrict,
    linalg: () => linalg,
    linspace: () => linspace,
    localResponseNormalization: () => localResponseNormalization,
    log: () => log,
    log1p: () => log1p,
    logSigmoid: () => logSigmoid,
    logSoftmax: () => logSoftmax,
    logSumExp: () => logSumExp,
    logicalAnd: () => logicalAnd,
    logicalNot: () => logicalNot,
    logicalOr: () => logicalOr,
    logicalXor: () => logicalXor,
    losses: () => losses,
    matMul: () => matMul,
    max: () => max,
    maxPool: () => maxPool,
    maxPool3d: () => maxPool3d,
    maxPoolWithArgmax: () => maxPoolWithArgmax,
    maximum: () => maximum,
    maximumStrict: () => maximumStrict,
    mean: () => mean,
    min: () => min,
    minimum: () => minimum,
    minimumStrict: () => minimumStrict,
    mod: () => mod,
    modStrict: () => modStrict,
    moments: () => moments,
    movingAverage: () => movingAverage,
    mul: () => mul,
    mulStrict: () => mulStrict,
    multiRNNCell: () => multiRNNCell,
    multinomial: () => multinomial,
    neg: () => neg,
    norm: () => norm,
    notEqual: () => notEqual,
    notEqualStrict: () => notEqualStrict,
    oneHot: () => oneHot,
    ones: () => ones2,
    onesLike: () => onesLike,
    op: () => op,
    outerProduct: () => outerProduct,
    pad: () => pad,
    pad1d: () => pad1d,
    pad2d: () => pad2d,
    pad3d: () => pad3d,
    pad4d: () => pad4d,
    pool: () => pool,
    pow: () => pow,
    powStrict: () => powStrict,
    prelu: () => prelu,
    print: () => print,
    prod: () => prod,
    rand: () => rand,
    randomGamma: () => randomGamma,
    randomNormal: () => randomNormal,
    randomUniform: () => randomUniform,
    range: () => range,
    real: () => real,
    reciprocal: () => reciprocal,
    relu: () => relu,
    relu6: () => relu6,
    reshape: () => reshape,
    reverse: () => reverse,
    reverse1d: () => reverse1d,
    reverse2d: () => reverse2d,
    reverse3d: () => reverse3d,
    reverse4d: () => reverse4d,
    rfft: () => rfft,
    round: () => round,
    rsqrt: () => rsqrt,
    scalar: () => scalar,
    scatterND: () => scatterND,
    selu: () => selu,
    separableConv2d: () => separableConv2d,
    setdiff1dAsync: () => setdiff1dAsync,
    sigmoid: () => sigmoid,
    sign: () => sign,
    signal: () => signal,
    sin: () => sin,
    sinh: () => sinh,
    slice: () => slice,
    slice1d: () => slice1d,
    slice2d: () => slice2d,
    slice3d: () => slice3d,
    slice4d: () => slice4d,
    softmax: () => softmax,
    softplus: () => softplus,
    spaceToBatchND: () => spaceToBatchND,
    sparseToDense: () => sparseToDense,
    spectral: () => spectral,
    split: () => split,
    sqrt: () => sqrt,
    square: () => square,
    squaredDifference: () => squaredDifference,
    squaredDifferenceStrict: () => squaredDifferenceStrict,
    squeeze: () => squeeze,
    stack: () => stack,
    step: () => step,
    stridedSlice: () => stridedSlice,
    sub: () => sub,
    subStrict: () => subStrict,
    sum: () => sum2,
    tan: () => tan,
    tanh: () => tanh2,
    tensor: () => tensor5,
    tensor1d: () => tensor1d,
    tensor2d: () => tensor2d,
    tensor3d: () => tensor3d,
    tensor4d: () => tensor4d,
    tensor5d: () => tensor5d,
    tensor6d: () => tensor6d,
    tile: () => tile,
    topk: () => topk,
    transpose: () => transpose,
    truncatedNormal: () => truncatedNormal,
    unsortedSegmentSum: () => unsortedSegmentSum,
    unstack: () => unstack,
    variable: () => variable,
    where: () => where,
    whereAsync: () => whereAsync,
    zeros: () => zeros,
    zerosLike: () => zerosLike
  });
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const spectral = {
    fft,
    ifft,
    rfft,
    irfft
  };
  const signal = {
    hammingWindow,
    hannWindow,
    frame,
    stft
  };
  const image = {
    flipLeftRight,
    resizeNearestNeighbor,
    resizeBilinear,
    rotateWithOffset,
    cropAndResize,
    nonMaxSuppression,
    nonMaxSuppressionAsync,
    nonMaxSuppressionWithScore,
    nonMaxSuppressionWithScoreAsync,
    nonMaxSuppressionPadded,
    nonMaxSuppressionPaddedAsync
  };
  const linalg = {
    bandPart,
    gramSchmidt,
    qr
  };
  const losses = {
    absoluteDifference,
    computeWeightedLoss,
    cosineDistance,
    hingeLoss,
    huberLoss,
    logLoss,
    meanSquaredError,
    sigmoidCrossEntropy,
    softmaxCrossEntropy
  };

  // node_modules/@tensorflow/tfjs-core/dist/base_side_effects.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  setOpHandler(ops_exports);

  // node_modules/@tensorflow/tfjs-core/dist/io/types.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const DTYPE_VALUE_SIZE_MAP = {
    float32: 4,
    float16: 2,
    int32: 4,
    uint16: 2,
    uint8: 1,
    bool: 1,
    complex64: 8
  };

  // node_modules/@tensorflow/tfjs-core/dist/io/io_utils.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const NUM_BYTES_STRING_LENGTH = 4;
  async function encodeWeights(tensors, group) {
    const specs = [];
    const dataPromises = [];
    const names = Array.isArray(tensors) ? tensors.map((tensor17) => tensor17.name) : Object.keys(tensors);
    for (let i = 0; i < names.length; ++i) {
      const name = names[i];
      const t = Array.isArray(tensors) ? tensors[i].tensor : tensors[name];
      if (t.dtype !== "float32" && t.dtype !== "int32" && t.dtype !== "bool" && t.dtype !== "string" && t.dtype !== "complex64") {
        throw new Error(`Unsupported dtype in weight '${name}': ${t.dtype}`);
      }
      const spec = {name, shape: t.shape, dtype: t.dtype};
      if (t.dtype === "string") {
        const utf8bytes = new Promise(async (resolve) => {
          const vals = await t.bytes();
          const totalNumBytes = vals.reduce((p, c) => p + c.length, 0) + NUM_BYTES_STRING_LENGTH * vals.length;
          const bytes = new Uint8Array(totalNumBytes);
          let offset = 0;
          for (let i2 = 0; i2 < vals.length; i2++) {
            const val = vals[i2];
            const bytesOfLength = new Uint8Array(new Uint32Array([val.length]).buffer);
            bytes.set(bytesOfLength, offset);
            offset += NUM_BYTES_STRING_LENGTH;
            bytes.set(val, offset);
            offset += val.length;
          }
          resolve(bytes);
        });
        dataPromises.push(utf8bytes);
      } else {
        dataPromises.push(t.data());
      }
      if (group != null) {
        spec.group = group;
      }
      specs.push(spec);
    }
    const tensorValues = await Promise.all(dataPromises);
    return {data: concatenateTypedArrays(tensorValues), specs};
  }
  function decodeWeights(buffer10, specs) {
    const out = {};
    let float16Decode;
    let offset = 0;
    for (const spec of specs) {
      const name = spec.name;
      const dtype = spec.dtype;
      const shape = spec.shape;
      const size = sizeFromShape(shape);
      let values;
      if ("quantization" in spec) {
        const quantization = spec.quantization;
        if (quantization.dtype === "uint8" || quantization.dtype === "uint16") {
          if (!("min" in quantization && "scale" in quantization)) {
            throw new Error(`Weight ${spec.name} with quantization ${quantization.dtype} doesn't have corresponding metadata min and scale.`);
          }
        } else if (quantization.dtype === "float16") {
          if (dtype !== "float32") {
            throw new Error(`Weight ${spec.name} is quantized with ${quantization.dtype} which only supports weights of type float32 not ${dtype}.`);
          }
        } else {
          throw new Error(`Weight ${spec.name} has unknown quantization dtype ${quantization.dtype}. Supported quantization dtypes are: 'uint8', 'uint16', and 'float16'.`);
        }
        const quantizationSizeFactor = DTYPE_VALUE_SIZE_MAP[quantization.dtype];
        const byteBuffer = buffer10.slice(offset, offset + size * quantizationSizeFactor);
        const quantizedArray = quantization.dtype === "uint8" ? new Uint8Array(byteBuffer) : new Uint16Array(byteBuffer);
        if (dtype === "float32") {
          if (quantization.dtype === "uint8" || quantization.dtype === "uint16") {
            values = new Float32Array(quantizedArray.length);
            for (let i = 0; i < quantizedArray.length; i++) {
              const v = quantizedArray[i];
              values[i] = v * quantization.scale + quantization.min;
            }
          } else if (quantization.dtype === "float16") {
            if (float16Decode === void 0) {
              float16Decode = getFloat16Decoder();
            }
            values = float16Decode(quantizedArray);
          } else {
            throw new Error(`Unsupported quantization type ${quantization.dtype} for weight type float32.`);
          }
        } else if (dtype === "int32") {
          if (quantization.dtype !== "uint8" && quantization.dtype !== "uint16") {
            throw new Error(`Unsupported quantization type ${quantization.dtype} for weight type int32.`);
          }
          values = new Int32Array(quantizedArray.length);
          for (let i = 0; i < quantizedArray.length; i++) {
            const v = quantizedArray[i];
            values[i] = Math.round(v * quantization.scale + quantization.min);
          }
        } else {
          throw new Error(`Unsupported dtype in weight '${name}': ${dtype}`);
        }
        offset += size * quantizationSizeFactor;
      } else if (dtype === "string") {
        const size2 = sizeFromShape(spec.shape);
        values = [];
        for (let i = 0; i < size2; i++) {
          const byteLength = new Uint32Array(buffer10.slice(offset, offset + NUM_BYTES_STRING_LENGTH))[0];
          offset += NUM_BYTES_STRING_LENGTH;
          const bytes = new Uint8Array(buffer10.slice(offset, offset + byteLength));
          values.push(bytes);
          offset += byteLength;
        }
      } else {
        const dtypeFactor = DTYPE_VALUE_SIZE_MAP[dtype];
        const byteBuffer = buffer10.slice(offset, offset + size * dtypeFactor);
        if (dtype === "float32") {
          values = new Float32Array(byteBuffer);
        } else if (dtype === "int32") {
          values = new Int32Array(byteBuffer);
        } else if (dtype === "bool") {
          values = new Uint8Array(byteBuffer);
        } else if (dtype === "complex64") {
          values = new Float32Array(byteBuffer);
          const real6 = new Float32Array(values.length / 2);
          const image2 = new Float32Array(values.length / 2);
          for (let i = 0; i < real6.length; i++) {
            real6[i] = values[i * 2];
            image2[i] = values[i * 2 + 1];
          }
          const realTensor = tensor5(real6, shape, "float32");
          const imageTensor = tensor5(image2, shape, "float32");
          out[name] = complex(realTensor, imageTensor);
        } else {
          throw new Error(`Unsupported dtype in weight '${name}': ${dtype}`);
        }
        offset += size * dtypeFactor;
      }
      if (dtype !== "complex64") {
        out[name] = tensor5(values, shape, dtype);
      }
    }
    return out;
  }
  function concatenateTypedArrays(xs) {
    if (xs === null) {
      throw new Error(`Invalid input value: ${JSON.stringify(xs)}`);
    }
    let totalByteLength = 0;
    const normalizedXs = [];
    xs.forEach((x) => {
      totalByteLength += x.byteLength;
      normalizedXs.push(x.byteLength === x.buffer.byteLength ? x : new x.constructor(x));
      if (!(x instanceof Float32Array || x instanceof Int32Array || x instanceof Uint8Array)) {
        throw new Error(`Unsupported TypedArray subtype: ${x.constructor.name}`);
      }
    });
    const y = new Uint8Array(totalByteLength);
    let offset = 0;
    normalizedXs.forEach((x) => {
      y.set(new Uint8Array(x.buffer), offset);
      offset += x.byteLength;
    });
    return y.buffer;
  }
  const useNodeBuffer = typeof Buffer !== "undefined" && (typeof Blob === "undefined" || typeof atob === "undefined" || typeof btoa === "undefined");
  function stringByteLength(str) {
    if (useNodeBuffer) {
      return Buffer.byteLength(str);
    }
    return new Blob([str]).size;
  }
  function concatenateArrayBuffers(buffers) {
    if (buffers.length === 1) {
      return buffers[0];
    }
    let totalByteLength = 0;
    buffers.forEach((buffer10) => {
      totalByteLength += buffer10.byteLength;
    });
    const temp = new Uint8Array(totalByteLength);
    let offset = 0;
    buffers.forEach((buffer10) => {
      temp.set(new Uint8Array(buffer10), offset);
      offset += buffer10.byteLength;
    });
    return temp.buffer;
  }
  function basename(path) {
    const SEPARATOR = "/";
    path = path.trim();
    while (path.endsWith(SEPARATOR)) {
      path = path.slice(0, path.length - 1);
    }
    const items = path.split(SEPARATOR);
    return items[items.length - 1];
  }
  function getModelArtifactsInfoForJSON(modelArtifacts) {
    if (modelArtifacts.modelTopology instanceof ArrayBuffer) {
      throw new Error("Expected JSON model topology, received ArrayBuffer.");
    }
    return {
      dateSaved: new Date(),
      modelTopologyType: "JSON",
      modelTopologyBytes: modelArtifacts.modelTopology == null ? 0 : stringByteLength(JSON.stringify(modelArtifacts.modelTopology)),
      weightSpecsBytes: modelArtifacts.weightSpecs == null ? 0 : stringByteLength(JSON.stringify(modelArtifacts.weightSpecs)),
      weightDataBytes: modelArtifacts.weightData == null ? 0 : modelArtifacts.weightData.byteLength
    };
  }
  function computeFloat16MantisaTable() {
    const convertMantissa = (i) => {
      let m = i << 13;
      let e = 0;
      while ((m & 8388608) === 0) {
        e -= 8388608;
        m <<= 1;
      }
      m &= ~8388608;
      e += 947912704;
      return m | e;
    };
    const mantisaTable = new Uint32Array(2048);
    mantisaTable[0] = 0;
    for (let i = 1; i < 1024; i++) {
      mantisaTable[i] = convertMantissa(i);
    }
    for (let i = 1024; i < 2048; i++) {
      mantisaTable[i] = 939524096 + (i - 1024 << 13);
    }
    return mantisaTable;
  }
  function computeFloat16ExponentTable() {
    const exponentTable = new Uint32Array(64);
    exponentTable[0] = 0;
    exponentTable[31] = 1199570944;
    exponentTable[32] = 2147483648;
    exponentTable[63] = 3347054592;
    for (let i = 1; i < 31; i++) {
      exponentTable[i] = i << 23;
    }
    for (let i = 33; i < 63; i++) {
      exponentTable[i] = 2147483648 + (i - 32 << 23);
    }
    return exponentTable;
  }
  function computeFloat16OffsetTable() {
    const offsetTable = new Uint32Array(64);
    for (let i = 0; i < 64; i++) {
      offsetTable[i] = 1024;
    }
    offsetTable[0] = offsetTable[32] = 0;
    return offsetTable;
  }
  function getFloat16Decoder() {
    const mantisaTable = computeFloat16MantisaTable();
    const exponentTable = computeFloat16ExponentTable();
    const offsetTable = computeFloat16OffsetTable();
    return (quantizedArray) => {
      const buffer10 = new ArrayBuffer(4 * quantizedArray.length);
      const bufferUint32View = new Uint32Array(buffer10);
      for (let index = 0; index < quantizedArray.length; index++) {
        const float16Bits = quantizedArray[index];
        const float32Bits = mantisaTable[offsetTable[float16Bits >> 10] + (float16Bits & 1023)] + exponentTable[float16Bits >> 10];
        bufferUint32View[index] = float32Bits;
      }
      return new Float32Array(buffer10);
    };
  }

  // node_modules/@tensorflow/tfjs-core/dist/io/router_registry.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class IORouterRegistry {
    constructor() {
      this.saveRouters = [];
      this.loadRouters = [];
    }
    static getInstance() {
      if (IORouterRegistry.instance == null) {
        IORouterRegistry.instance = new IORouterRegistry();
      }
      return IORouterRegistry.instance;
    }
    static registerSaveRouter(saveRouter) {
      IORouterRegistry.getInstance().saveRouters.push(saveRouter);
    }
    static registerLoadRouter(loadRouter) {
      IORouterRegistry.getInstance().loadRouters.push(loadRouter);
    }
    static getSaveHandlers(url) {
      return IORouterRegistry.getHandlers(url, "save");
    }
    static getLoadHandlers(url, loadOptions) {
      return IORouterRegistry.getHandlers(url, "load", loadOptions);
    }
    static getHandlers(url, handlerType, loadOptions) {
      const validHandlers = [];
      const routers = handlerType === "load" ? IORouterRegistry.getInstance().loadRouters : IORouterRegistry.getInstance().saveRouters;
      routers.forEach((router) => {
        const handler = router(url, loadOptions);
        if (handler !== null) {
          validHandlers.push(handler);
        }
      });
      return validHandlers;
    }
  }
  const registerSaveRouter = (loudRouter) => IORouterRegistry.registerSaveRouter(loudRouter);
  const registerLoadRouter = (loudRouter) => IORouterRegistry.registerLoadRouter(loudRouter);
  const getSaveHandlers = (url) => IORouterRegistry.getSaveHandlers(url);
  const getLoadHandlers = (url, loadOptions) => IORouterRegistry.getLoadHandlers(url, loadOptions);

  // node_modules/@tensorflow/tfjs-core/dist/io/model_management.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const URL_SCHEME_SUFFIX = "://";
  class ModelStoreManagerRegistry {
    constructor() {
      this.managers = {};
    }
    static getInstance() {
      if (ModelStoreManagerRegistry.instance == null) {
        ModelStoreManagerRegistry.instance = new ModelStoreManagerRegistry();
      }
      return ModelStoreManagerRegistry.instance;
    }
    static registerManager(scheme, manager) {
      assert(scheme != null, () => "scheme must not be undefined or null.");
      if (scheme.endsWith(URL_SCHEME_SUFFIX)) {
        scheme = scheme.slice(0, scheme.indexOf(URL_SCHEME_SUFFIX));
      }
      assert(scheme.length > 0, () => "scheme must not be an empty string.");
      const registry = ModelStoreManagerRegistry.getInstance();
      assert(registry.managers[scheme] == null, () => `A model store manager is already registered for scheme '${scheme}'.`);
      registry.managers[scheme] = manager;
    }
    static getManager(scheme) {
      const manager = this.getInstance().managers[scheme];
      if (manager == null) {
        throw new Error(`Cannot find model manager for scheme '${scheme}'`);
      }
      return manager;
    }
    static getSchemes() {
      return Object.keys(this.getInstance().managers);
    }
  }
  function parseURL(url) {
    if (url.indexOf(URL_SCHEME_SUFFIX) === -1) {
      throw new Error(`The url string provided does not contain a scheme. Supported schemes are: ${ModelStoreManagerRegistry.getSchemes().join(",")}`);
    }
    return {
      scheme: url.split(URL_SCHEME_SUFFIX)[0],
      path: url.split(URL_SCHEME_SUFFIX)[1]
    };
  }
  async function cloneModelInternal(sourceURL, destURL, deleteSource = false) {
    assert(sourceURL !== destURL, () => `Old path and new path are the same: '${sourceURL}'`);
    const loadHandlers = IORouterRegistry.getLoadHandlers(sourceURL);
    assert(loadHandlers.length > 0, () => `Copying failed because no load handler is found for source URL ${sourceURL}.`);
    assert(loadHandlers.length < 2, () => `Copying failed because more than one (${loadHandlers.length}) load handlers for source URL ${sourceURL}.`);
    const loadHandler = loadHandlers[0];
    const saveHandlers = IORouterRegistry.getSaveHandlers(destURL);
    assert(saveHandlers.length > 0, () => `Copying failed because no save handler is found for destination URL ${destURL}.`);
    assert(saveHandlers.length < 2, () => `Copying failed because more than one (${loadHandlers.length}) save handlers for destination URL ${destURL}.`);
    const saveHandler = saveHandlers[0];
    const sourceScheme = parseURL(sourceURL).scheme;
    const sourcePath = parseURL(sourceURL).path;
    const sameMedium = sourceScheme === parseURL(sourceURL).scheme;
    const modelArtifacts = await loadHandler.load();
    if (deleteSource && sameMedium) {
      await ModelStoreManagerRegistry.getManager(sourceScheme).removeModel(sourcePath);
    }
    const saveResult = await saveHandler.save(modelArtifacts);
    if (deleteSource && !sameMedium) {
      await ModelStoreManagerRegistry.getManager(sourceScheme).removeModel(sourcePath);
    }
    return saveResult.modelArtifactsInfo;
  }
  async function listModels() {
    const schemes = ModelStoreManagerRegistry.getSchemes();
    const out = {};
    for (const scheme of schemes) {
      const schemeOut = await ModelStoreManagerRegistry.getManager(scheme).listModels();
      for (const path in schemeOut) {
        const url = scheme + URL_SCHEME_SUFFIX + path;
        out[url] = schemeOut[path];
      }
    }
    return out;
  }
  async function removeModel(url) {
    const schemeAndPath = parseURL(url);
    const manager = ModelStoreManagerRegistry.getManager(schemeAndPath.scheme);
    return manager.removeModel(schemeAndPath.path);
  }
  async function copyModel(sourceURL, destURL) {
    const deleteSource = false;
    return cloneModelInternal(sourceURL, destURL, deleteSource);
  }
  async function moveModel(sourceURL, destURL) {
    const deleteSource = true;
    return cloneModelInternal(sourceURL, destURL, deleteSource);
  }

  // node_modules/@tensorflow/tfjs-core/dist/io/browser_files.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const DEFAULT_FILE_NAME_PREFIX = "model";
  const DEFAULT_JSON_EXTENSION_NAME = ".json";
  const DEFAULT_WEIGHT_DATA_EXTENSION_NAME = ".weights.bin";
  function defer(f) {
    return new Promise((resolve) => setTimeout(resolve)).then(f);
  }
  class BrowserDownloads {
    constructor(fileNamePrefix) {
      if (!env().getBool("IS_BROWSER")) {
        throw new Error("browserDownloads() cannot proceed because the current environment is not a browser.");
      }
      if (fileNamePrefix.startsWith(BrowserDownloads.URL_SCHEME)) {
        fileNamePrefix = fileNamePrefix.slice(BrowserDownloads.URL_SCHEME.length);
      }
      if (fileNamePrefix == null || fileNamePrefix.length === 0) {
        fileNamePrefix = DEFAULT_FILE_NAME_PREFIX;
      }
      this.modelTopologyFileName = fileNamePrefix + DEFAULT_JSON_EXTENSION_NAME;
      this.weightDataFileName = fileNamePrefix + DEFAULT_WEIGHT_DATA_EXTENSION_NAME;
    }
    async save(modelArtifacts) {
      if (typeof document === "undefined") {
        throw new Error("Browser downloads are not supported in this environment since `document` is not present");
      }
      const weightsURL = window.URL.createObjectURL(new Blob([modelArtifacts.weightData], {type: "application/octet-stream"}));
      if (modelArtifacts.modelTopology instanceof ArrayBuffer) {
        throw new Error("BrowserDownloads.save() does not support saving model topology in binary formats yet.");
      } else {
        const weightsManifest = [{
          paths: ["./" + this.weightDataFileName],
          weights: modelArtifacts.weightSpecs
        }];
        const modelTopologyAndWeightManifest = {
          modelTopology: modelArtifacts.modelTopology,
          format: modelArtifacts.format,
          generatedBy: modelArtifacts.generatedBy,
          convertedBy: modelArtifacts.convertedBy,
          weightsManifest
        };
        const modelTopologyAndWeightManifestURL = window.URL.createObjectURL(new Blob([JSON.stringify(modelTopologyAndWeightManifest)], {type: "application/json"}));
        const jsonAnchor = this.jsonAnchor == null ? document.createElement("a") : this.jsonAnchor;
        jsonAnchor.download = this.modelTopologyFileName;
        jsonAnchor.href = modelTopologyAndWeightManifestURL;
        await defer(() => jsonAnchor.dispatchEvent(new MouseEvent("click")));
        if (modelArtifacts.weightData != null) {
          const weightDataAnchor = this.weightDataAnchor == null ? document.createElement("a") : this.weightDataAnchor;
          weightDataAnchor.download = this.weightDataFileName;
          weightDataAnchor.href = weightsURL;
          await defer(() => weightDataAnchor.dispatchEvent(new MouseEvent("click")));
        }
        return {modelArtifactsInfo: getModelArtifactsInfoForJSON(modelArtifacts)};
      }
    }
  }
  BrowserDownloads.URL_SCHEME = "downloads://";
  class BrowserFiles {
    constructor(files) {
      if (files == null || files.length < 1) {
        throw new Error(`When calling browserFiles, at least 1 file is required, but received ${files}`);
      }
      this.files = files;
    }
    async load() {
      const jsonFile = this.files[0];
      const weightFiles = this.files.slice(1);
      return new Promise((resolve, reject) => {
        const jsonReader = new FileReader();
        jsonReader.onload = (event) => {
          const modelJSON = JSON.parse(event.target.result);
          const modelTopology = modelJSON.modelTopology;
          if (modelTopology == null) {
            reject(new Error(`modelTopology field is missing from file ${jsonFile.name}`));
            return;
          }
          if (weightFiles.length === 0) {
            resolve({modelTopology});
          }
          const weightsManifest = modelJSON.weightsManifest;
          if (weightsManifest == null) {
            reject(new Error(`weightManifest field is missing from file ${jsonFile.name}`));
            return;
          }
          let pathToFile;
          try {
            pathToFile = this.checkManifestAndWeightFiles(weightsManifest, weightFiles);
          } catch (err) {
            reject(err);
            return;
          }
          const weightSpecs = [];
          const paths = [];
          const perFileBuffers = [];
          weightsManifest.forEach((weightsGroup) => {
            weightsGroup.paths.forEach((path) => {
              paths.push(path);
              perFileBuffers.push(null);
            });
            weightSpecs.push(...weightsGroup.weights);
          });
          weightsManifest.forEach((weightsGroup) => {
            weightsGroup.paths.forEach((path) => {
              const weightFileReader = new FileReader();
              weightFileReader.onload = (event2) => {
                const weightData = event2.target.result;
                const index = paths.indexOf(path);
                perFileBuffers[index] = weightData;
                if (perFileBuffers.indexOf(null) === -1) {
                  resolve({
                    modelTopology,
                    weightSpecs,
                    weightData: concatenateArrayBuffers(perFileBuffers),
                    format: modelJSON.format,
                    generatedBy: modelJSON.generatedBy,
                    convertedBy: modelJSON.convertedBy,
                    userDefinedMetadata: modelJSON.userDefinedMetadata
                  });
                }
              };
              weightFileReader.onerror = (error) => reject(`Failed to weights data from file of path '${path}'.`);
              weightFileReader.readAsArrayBuffer(pathToFile[path]);
            });
          });
        };
        jsonReader.onerror = (error) => reject(`Failed to read model topology and weights manifest JSON from file '${jsonFile.name}'. BrowserFiles supports loading Keras-style tf.Model artifacts only.`);
        jsonReader.readAsText(jsonFile);
      });
    }
    checkManifestAndWeightFiles(manifest, files) {
      const basenames = [];
      const fileNames = files.map((file) => basename(file.name));
      const pathToFile = {};
      for (const group of manifest) {
        group.paths.forEach((path) => {
          const pathBasename = basename(path);
          if (basenames.indexOf(pathBasename) !== -1) {
            throw new Error(`Duplicate file basename found in weights manifest: '${pathBasename}'`);
          }
          basenames.push(pathBasename);
          if (fileNames.indexOf(pathBasename) === -1) {
            throw new Error(`Weight file with basename '${pathBasename}' is not provided.`);
          } else {
            pathToFile[path] = files[fileNames.indexOf(pathBasename)];
          }
        });
      }
      if (basenames.length !== files.length) {
        throw new Error(`Mismatch in the number of files in weights manifest (${basenames.length}) and the number of weight files provided (${files.length}).`);
      }
      return pathToFile;
    }
  }
  const browserDownloadsRouter = (url) => {
    if (!env().getBool("IS_BROWSER")) {
      return null;
    } else {
      if (!Array.isArray(url) && url.startsWith(BrowserDownloads.URL_SCHEME)) {
        return browserDownloads(url.slice(BrowserDownloads.URL_SCHEME.length));
      } else {
        return null;
      }
    }
  };
  IORouterRegistry.registerSaveRouter(browserDownloadsRouter);
  function browserDownloads(fileNamePrefix = "model") {
    return new BrowserDownloads(fileNamePrefix);
  }
  function browserFiles(files) {
    return new BrowserFiles(files);
  }

  // node_modules/@tensorflow/tfjs-core/dist/io/progress.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function monitorPromisesProgress(promises, onProgress, startFraction, endFraction) {
    checkPromises(promises);
    startFraction = startFraction == null ? 0 : startFraction;
    endFraction = endFraction == null ? 1 : endFraction;
    checkFraction(startFraction, endFraction);
    let resolvedPromise = 0;
    const registerMonitor = (promise) => {
      promise.then((value) => {
        const fraction = startFraction + ++resolvedPromise / promises.length * (endFraction - startFraction);
        onProgress(fraction);
        return value;
      });
      return promise;
    };
    function checkPromises(promises2) {
      assert(promises2 != null && Array.isArray(promises2) && promises2.length > 0, () => "promises must be a none empty array");
    }
    function checkFraction(startFraction2, endFraction2) {
      assert(startFraction2 >= 0 && startFraction2 <= 1, () => `Progress fraction must be in range [0, 1], but got startFraction ${startFraction2}`);
      assert(endFraction2 >= 0 && endFraction2 <= 1, () => `Progress fraction must be in range [0, 1], but got endFraction ${endFraction2}`);
      assert(endFraction2 >= startFraction2, () => `startFraction must be no more than endFraction, but got startFraction ${startFraction2} and endFraction ${endFraction2}`);
    }
    return Promise.all(promises.map(registerMonitor));
  }

  // node_modules/@tensorflow/tfjs-core/dist/io/weights_loader.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  async function loadWeightsAsArrayBuffer(fetchURLs, loadOptions) {
    if (loadOptions == null) {
      loadOptions = {};
    }
    const fetchFunc = loadOptions.fetchFunc == null ? env().platform.fetch : loadOptions.fetchFunc;
    const requests = fetchURLs.map((fetchURL) => fetchFunc(fetchURL, loadOptions.requestInit, {isBinary: true}));
    const fetchStartFraction = 0;
    const fetchEndFraction = 0.5;
    const responses = loadOptions.onProgress == null ? await Promise.all(requests) : await monitorPromisesProgress(requests, loadOptions.onProgress, fetchStartFraction, fetchEndFraction);
    const bufferPromises = responses.map((response) => response.arrayBuffer());
    const bufferStartFraction = 0.5;
    const bufferEndFraction = 1;
    const buffers = loadOptions.onProgress == null ? await Promise.all(bufferPromises) : await monitorPromisesProgress(bufferPromises, loadOptions.onProgress, bufferStartFraction, bufferEndFraction);
    return buffers;
  }
  async function loadWeights(manifest, filePathPrefix = "", weightNames, requestInit) {
    const fetchWeights = (fetchUrls) => loadWeightsAsArrayBuffer(fetchUrls, {requestInit});
    const loadWeights2 = weightsLoaderFactory(fetchWeights);
    return loadWeights2(manifest, filePathPrefix, weightNames);
  }
  function weightsLoaderFactory(fetchWeightsFunction) {
    return async (manifest, filePathPrefix = "", weightNames) => {
      const groupIndicesToFetchMap = manifest.map(() => false);
      const groupWeightsToFetch = {};
      const weightsFound = weightNames != null ? weightNames.map(() => false) : [];
      const allManifestWeightNames = [];
      manifest.forEach((manifestGroupConfig, groupIndex) => {
        let groupOffset = 0;
        manifestGroupConfig.weights.forEach((weightsEntry) => {
          const rawDtype = "quantization" in weightsEntry ? weightsEntry.quantization.dtype : weightsEntry.dtype;
          const weightsBytes = DTYPE_VALUE_SIZE_MAP[rawDtype] * sizeFromShape(weightsEntry.shape);
          const enqueueWeightsForFetchingFn = () => {
            groupIndicesToFetchMap[groupIndex] = true;
            if (groupWeightsToFetch[groupIndex] == null) {
              groupWeightsToFetch[groupIndex] = [];
            }
            groupWeightsToFetch[groupIndex].push({
              manifestEntry: weightsEntry,
              groupOffset,
              sizeBytes: weightsBytes
            });
          };
          if (weightNames != null) {
            weightNames.forEach((weightName, weightIndex) => {
              if (weightName === weightsEntry.name) {
                enqueueWeightsForFetchingFn();
                weightsFound[weightIndex] = true;
              }
            });
          } else {
            enqueueWeightsForFetchingFn();
          }
          allManifestWeightNames.push(weightsEntry.name);
          groupOffset += weightsBytes;
        });
      });
      if (!weightsFound.every((found) => found)) {
        const weightsNotFound = weightNames.filter((_, i) => !weightsFound[i]);
        throw new Error(`Could not find weights in manifest with names: ${weightsNotFound.join(", ")}. 
Manifest JSON has weights with names: ${allManifestWeightNames.join(", ")}.`);
      }
      const groupIndicesToFetch = groupIndicesToFetchMap.reduce((accumulator, shouldFetch, i) => {
        if (shouldFetch) {
          accumulator.push(i);
        }
        return accumulator;
      }, []);
      const fetchUrls = [];
      groupIndicesToFetch.forEach((i) => {
        manifest[i].paths.forEach((filepath) => {
          const fetchUrl = filePathPrefix + (!filePathPrefix.endsWith("/") ? "/" : "") + filepath;
          fetchUrls.push(fetchUrl);
        });
      });
      const buffers = await fetchWeightsFunction(fetchUrls);
      const weightsTensorMap = {};
      let bufferIndexOffset = 0;
      groupIndicesToFetch.forEach((i) => {
        const numBuffers = manifest[i].paths.length;
        let groupBytes = 0;
        for (let i2 = 0; i2 < numBuffers; i2++) {
          groupBytes += buffers[bufferIndexOffset + i2].byteLength;
        }
        const groupBuffer = new ArrayBuffer(groupBytes);
        const groupByteBuffer = new Uint8Array(groupBuffer);
        let groupBufferOffset = 0;
        for (let i2 = 0; i2 < numBuffers; i2++) {
          const buffer10 = new Uint8Array(buffers[bufferIndexOffset + i2]);
          groupByteBuffer.set(buffer10, groupBufferOffset);
          groupBufferOffset += buffer10.byteLength;
        }
        const weightsEntries = groupWeightsToFetch[i];
        weightsEntries.forEach((weightsEntry) => {
          const byteBuffer = groupBuffer.slice(weightsEntry.groupOffset, weightsEntry.groupOffset + weightsEntry.sizeBytes);
          const nameToTensorMap = decodeWeights(byteBuffer, [weightsEntry.manifestEntry]);
          for (const name in nameToTensorMap) {
            weightsTensorMap[name] = nameToTensorMap[name];
          }
        });
        bufferIndexOffset += numBuffers;
      });
      return weightsTensorMap;
    };
  }

  // node_modules/@tensorflow/tfjs-core/dist/io/http.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const OCTET_STREAM_MIME_TYPE = "application/octet-stream";
  const JSON_TYPE = "application/json";
  class HTTPRequest {
    constructor(path, loadOptions) {
      this.DEFAULT_METHOD = "POST";
      if (loadOptions == null) {
        loadOptions = {};
      }
      this.weightPathPrefix = loadOptions.weightPathPrefix;
      this.onProgress = loadOptions.onProgress;
      if (loadOptions.fetchFunc != null) {
        assert(typeof loadOptions.fetchFunc === "function", () => "Must pass a function that matches the signature of `fetch` (see https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)");
        this.fetch = loadOptions.fetchFunc;
      } else {
        this.fetch = env().platform.fetch;
      }
      assert(path != null && path.length > 0, () => "URL path for http must not be null, undefined or empty.");
      if (Array.isArray(path)) {
        assert(path.length === 2, () => `URL paths for http must have a length of 2, (actual length is ${path.length}).`);
      }
      this.path = path;
      if (loadOptions.requestInit != null && loadOptions.requestInit.body != null) {
        throw new Error("requestInit is expected to have no pre-existing body, but has one.");
      }
      this.requestInit = loadOptions.requestInit || {};
    }
    async save(modelArtifacts) {
      if (modelArtifacts.modelTopology instanceof ArrayBuffer) {
        throw new Error("BrowserHTTPRequest.save() does not support saving model topology in binary formats yet.");
      }
      const init = Object.assign({method: this.DEFAULT_METHOD}, this.requestInit);
      init.body = new FormData();
      const weightsManifest = [{
        paths: ["./model.weights.bin"],
        weights: modelArtifacts.weightSpecs
      }];
      const modelTopologyAndWeightManifest = {
        modelTopology: modelArtifacts.modelTopology,
        format: modelArtifacts.format,
        generatedBy: modelArtifacts.generatedBy,
        convertedBy: modelArtifacts.convertedBy,
        userDefinedMetadata: modelArtifacts.userDefinedMetadata,
        weightsManifest
      };
      init.body.append("model.json", new Blob([JSON.stringify(modelTopologyAndWeightManifest)], {type: JSON_TYPE}), "model.json");
      if (modelArtifacts.weightData != null) {
        init.body.append("model.weights.bin", new Blob([modelArtifacts.weightData], {type: OCTET_STREAM_MIME_TYPE}), "model.weights.bin");
      }
      const response = await this.fetch(this.path, init);
      if (response.ok) {
        return {
          modelArtifactsInfo: getModelArtifactsInfoForJSON(modelArtifacts),
          responses: [response]
        };
      } else {
        throw new Error(`BrowserHTTPRequest.save() failed due to HTTP response status ${response.status}.`);
      }
    }
    async load() {
      const modelConfigRequest = await this.fetch(this.path, this.requestInit);
      if (!modelConfigRequest.ok) {
        throw new Error(`Request to ${this.path} failed with status code ${modelConfigRequest.status}. Please verify this URL points to the model JSON of the model to load.`);
      }
      let modelConfig;
      try {
        modelConfig = await modelConfigRequest.json();
      } catch (e) {
        let message = `Failed to parse model JSON of response from ${this.path}.`;
        if (this.path.endsWith(".pb")) {
          message += " Your path contains a .pb file extension. Support for .pb models have been removed in TensorFlow.js 1.0 in favor of .json models. You can re-convert your Python TensorFlow model using the TensorFlow.js 1.0 conversion scripts or you can convert your.pb models with the 'pb2json'NPM script in the tensorflow/tfjs-converter repository.";
        } else {
          message += " Please make sure the server is serving valid JSON for this request.";
        }
        throw new Error(message);
      }
      const modelTopology = modelConfig.modelTopology;
      const weightsManifest = modelConfig.weightsManifest;
      const generatedBy = modelConfig.generatedBy;
      const convertedBy = modelConfig.convertedBy;
      const format = modelConfig.format;
      const userDefinedMetadata = modelConfig.userDefinedMetadata;
      if (modelTopology == null && weightsManifest == null) {
        throw new Error(`The JSON from HTTP path ${this.path} contains neither model topology or manifest for weights.`);
      }
      let weightSpecs;
      let weightData;
      if (weightsManifest != null) {
        const results = await this.loadWeights(weightsManifest);
        [weightSpecs, weightData] = results;
      }
      return {
        modelTopology,
        weightSpecs,
        weightData,
        userDefinedMetadata,
        generatedBy,
        convertedBy,
        format
      };
    }
    async loadWeights(weightsManifest) {
      const weightPath = Array.isArray(this.path) ? this.path[1] : this.path;
      const [prefix, suffix] = parseUrl(weightPath);
      const pathPrefix = this.weightPathPrefix || prefix;
      const weightSpecs = [];
      for (const entry of weightsManifest) {
        weightSpecs.push(...entry.weights);
      }
      const fetchURLs = [];
      weightsManifest.forEach((weightsGroup) => {
        weightsGroup.paths.forEach((path) => {
          fetchURLs.push(pathPrefix + path + suffix);
        });
      });
      const buffers = await loadWeightsAsArrayBuffer(fetchURLs, {
        requestInit: this.requestInit,
        fetchFunc: this.fetch,
        onProgress: this.onProgress
      });
      return [weightSpecs, concatenateArrayBuffers(buffers)];
    }
  }
  HTTPRequest.URL_SCHEME_REGEX = /^https?:\/\//;
  function parseUrl(url) {
    const lastSlash = url.lastIndexOf("/");
    const lastSearchParam = url.lastIndexOf("?");
    const prefix = url.substring(0, lastSlash);
    const suffix = lastSearchParam > lastSlash ? url.substring(lastSearchParam) : "";
    return [prefix + "/", suffix];
  }
  function isHTTPScheme(url) {
    return url.match(HTTPRequest.URL_SCHEME_REGEX) != null;
  }
  const httpRouter = (url, loadOptions) => {
    if (typeof fetch === "undefined" && (loadOptions == null || loadOptions.fetchFunc == null)) {
      return null;
    } else {
      let isHTTP = true;
      if (Array.isArray(url)) {
        isHTTP = url.every((urlItem) => isHTTPScheme(urlItem));
      } else {
        isHTTP = isHTTPScheme(url);
      }
      if (isHTTP) {
        return http(url, loadOptions);
      }
    }
    return null;
  };
  IORouterRegistry.registerSaveRouter(httpRouter);
  IORouterRegistry.registerLoadRouter(httpRouter);
  function http(path, loadOptions) {
    return new HTTPRequest(path, loadOptions);
  }
  function browserHTTPRequest(path, loadOptions) {
    return http(path, loadOptions);
  }

  // node_modules/@tensorflow/tfjs-core/dist/io/passthrough.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class PassthroughLoader {
    constructor(modelArtifacts) {
      this.modelArtifacts = modelArtifacts;
    }
    async load() {
      return this.modelArtifacts;
    }
  }
  class PassthroughSaver {
    constructor(saveHandler) {
      this.saveHandler = saveHandler;
    }
    async save(modelArtifacts) {
      return this.saveHandler(modelArtifacts);
    }
  }
  function fromMemory(modelArtifacts, weightSpecs, weightData, trainingConfig) {
    if (arguments.length === 1) {
      const isModelArtifacts = modelArtifacts.modelTopology != null || modelArtifacts.weightSpecs != null;
      if (isModelArtifacts) {
        return new PassthroughLoader(modelArtifacts);
      } else {
        console.warn("Please call tf.io.fromMemory() with only one argument. The argument should be of type ModelArtifacts. The multi-argument signature of tf.io.fromMemory() has been deprecated and will be removed in a future release.");
        return new PassthroughLoader({modelTopology: modelArtifacts});
      }
    } else {
      console.warn("Please call tf.io.fromMemory() with only one argument. The argument should be of type ModelArtifacts. The multi-argument signature of tf.io.fromMemory() has been deprecated and will be removed in a future release.");
      return new PassthroughLoader({
        modelTopology: modelArtifacts,
        weightSpecs,
        weightData,
        trainingConfig
      });
    }
  }
  function withSaveHandler(saveHandler) {
    return new PassthroughSaver(saveHandler);
  }

  // node_modules/@tensorflow/tfjs-core/dist/io/io.js
  const io_exports = {};
  __export(io_exports, {
    browserFiles: () => browserFiles,
    browserHTTPRequest: () => browserHTTPRequest,
    concatenateArrayBuffers: () => concatenateArrayBuffers,
    copyModel: () => copyModel,
    decodeWeights: () => decodeWeights,
    encodeWeights: () => encodeWeights,
    fromMemory: () => fromMemory,
    getLoadHandlers: () => getLoadHandlers,
    getModelArtifactsInfoForJSON: () => getModelArtifactsInfoForJSON,
    getSaveHandlers: () => getSaveHandlers,
    http: () => http,
    isHTTPScheme: () => isHTTPScheme,
    listModels: () => listModels,
    loadWeights: () => loadWeights,
    moveModel: () => moveModel,
    registerLoadRouter: () => registerLoadRouter,
    registerSaveRouter: () => registerSaveRouter,
    removeModel: () => removeModel,
    weightsLoaderFactory: () => weightsLoaderFactory,
    withSaveHandler: () => withSaveHandler
  });
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */

  // node_modules/@tensorflow/tfjs-core/dist/ops/confusion_matrix.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function confusionMatrix_(labels, predictions, numClasses) {
    const $labels = convertToTensor(labels, "labels", "confusionMatrix");
    const $predictions = convertToTensor(predictions, "predictions", "confusionMatrix");
    assert(numClasses == null || numClasses > 0 && Number.isInteger(numClasses), () => `If provided, numClasses must be a positive integer, but got ${numClasses}`);
    assert($labels.rank === 1, () => `Expected the rank of labels to be 1, but got ${$labels.rank}`);
    assert($predictions.rank === 1, () => `Expected the rank of predictions to be 1, but got ${$predictions.rank}`);
    assert($labels.shape[0] === $predictions.shape[0], () => `Mismatch in the number of examples: ${$labels.shape[0]} vs. ${$predictions.shape[0]}. Labels and predictions should have the same number of elements.`);
    assert(numClasses > 0 && Number.isInteger(numClasses), () => `numClasses is required to be a positive integer, but got ${numClasses}`);
    const oneHotLabels = oneHot(cast($labels, "int32"), numClasses);
    const oneHotPredictions = oneHot(cast($predictions, "int32"), numClasses);
    const oneHotLabelsT = transpose(oneHotLabels);
    return cast(matMul(oneHotLabelsT, oneHotPredictions), "int32");
  }
  const confusionMatrix = op({confusionMatrix_});

  // node_modules/@tensorflow/tfjs-core/dist/math.js
  const math_exports = {};
  __export(math_exports, {
    confusionMatrix: () => confusionMatrix
  });
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */

  // node_modules/@tensorflow/tfjs-core/dist/ops/browser.js
  const browser_exports = {};
  __export(browser_exports, {
    fromPixels: () => fromPixels,
    toPixels: () => toPixels
  });
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  let fromPixels2DContext;
  function fromPixels_(pixels, numChannels = 3) {
    if (numChannels > 4) {
      throw new Error("Cannot construct Tensor with more than 4 channels from pixels.");
    }
    if (pixels == null) {
      throw new Error("pixels passed to tf.browser.fromPixels() can not be null");
    }
    let isPixelData = false;
    let isImageData = false;
    let isVideo = false;
    let isImage = false;
    let isCanvasLike = false;
    if (pixels.data instanceof Uint8Array) {
      isPixelData = true;
    } else if (typeof ImageData !== "undefined" && pixels instanceof ImageData) {
      isImageData = true;
    } else if (typeof HTMLVideoElement !== "undefined" && pixels instanceof HTMLVideoElement) {
      isVideo = true;
    } else if (typeof HTMLImageElement !== "undefined" && pixels instanceof HTMLImageElement) {
      isImage = true;
    } else if (pixels.getContext != null) {
      isCanvasLike = true;
    } else {
      throw new Error(`pixels passed to tf.browser.fromPixels() must be either an HTMLVideoElement, HTMLImageElement, HTMLCanvasElement, ImageData in browser, or OffscreenCanvas, ImageData in webworker or {data: Uint32Array, width: number, height: number}, but was ${pixels.constructor.name}`);
    }
    if (isVideo) {
      const HAVE_CURRENT_DATA_READY_STATE = 2;
      if (isVideo && pixels.readyState < HAVE_CURRENT_DATA_READY_STATE) {
        throw new Error("The video element has not loaded data yet. Please wait for `loadeddata` event on the <video> element.");
      }
    }
    const kernel = getKernel(FromPixels, ENGINE.backendName);
    if (kernel != null) {
      const inputs = {pixels};
      const attrs = {numChannels};
      return ENGINE.runKernel(FromPixels, inputs, attrs);
    }
    const [width, height] = isVideo ? [
      pixels.videoWidth,
      pixels.videoHeight
    ] : [pixels.width, pixels.height];
    let vals;
    if (isCanvasLike) {
      vals = pixels.getContext("2d").getImageData(0, 0, width, height).data;
    } else if (isImageData || isPixelData) {
      vals = pixels.data;
    } else if (isImage || isVideo) {
      if (fromPixels2DContext == null) {
        fromPixels2DContext = document.createElement("canvas").getContext("2d");
      }
      fromPixels2DContext.canvas.width = width;
      fromPixels2DContext.canvas.height = height;
      fromPixels2DContext.drawImage(pixels, 0, 0, width, height);
      vals = fromPixels2DContext.getImageData(0, 0, width, height).data;
    }
    let values;
    if (numChannels === 4) {
      values = new Int32Array(vals);
    } else {
      const numPixels = width * height;
      values = new Int32Array(numPixels * numChannels);
      for (let i = 0; i < numPixels; i++) {
        for (let channel = 0; channel < numChannels; ++channel) {
          values[i * numChannels + channel] = vals[i * 4 + channel];
        }
      }
    }
    const outShape = [height, width, numChannels];
    return tensor3d(values, outShape, "int32");
  }
  async function toPixels(img, canvas) {
    let $img = convertToTensor(img, "img", "toPixels");
    if (!(img instanceof Tensor)) {
      const originalImgTensor = $img;
      $img = cast(originalImgTensor, "int32");
      originalImgTensor.dispose();
    }
    if ($img.rank !== 2 && $img.rank !== 3) {
      throw new Error(`toPixels only supports rank 2 or 3 tensors, got rank ${$img.rank}.`);
    }
    const [height, width] = $img.shape.slice(0, 2);
    const depth = $img.rank === 2 ? 1 : $img.shape[2];
    if (depth > 4 || depth === 2) {
      throw new Error(`toPixels only supports depth of size 1, 3 or 4 but got ${depth}`);
    }
    const data = await $img.data();
    const minTensor = min($img);
    const maxTensor = max($img);
    const vals = await Promise.all([minTensor.data(), maxTensor.data()]);
    const minVals = vals[0];
    const maxVals = vals[1];
    const minVal = minVals[0];
    const maxVal = maxVals[0];
    minTensor.dispose();
    maxTensor.dispose();
    if ($img.dtype === "float32") {
      if (minVal < 0 || maxVal > 1) {
        throw new Error(`Tensor values for a float32 Tensor must be in the range [0 - 1] but got range [${minVal} - ${maxVal}].`);
      }
    } else if ($img.dtype === "int32") {
      if (minVal < 0 || maxVal > 255) {
        throw new Error(`Tensor values for a int32 Tensor must be in the range [0 - 255] but got range [${minVal} - ${maxVal}].`);
      }
    } else {
      throw new Error(`Unsupported type for toPixels: ${$img.dtype}. Please use float32 or int32 tensors.`);
    }
    const multiplier = $img.dtype === "float32" ? 255 : 1;
    const bytes = new Uint8ClampedArray(width * height * 4);
    for (let i = 0; i < height * width; ++i) {
      let r, g, b, a;
      if (depth === 1) {
        r = data[i] * multiplier;
        g = data[i] * multiplier;
        b = data[i] * multiplier;
        a = 255;
      } else if (depth === 3) {
        r = data[i * 3] * multiplier;
        g = data[i * 3 + 1] * multiplier;
        b = data[i * 3 + 2] * multiplier;
        a = 255;
      } else if (depth === 4) {
        r = data[i * 4] * multiplier;
        g = data[i * 4 + 1] * multiplier;
        b = data[i * 4 + 2] * multiplier;
        a = data[i * 4 + 3] * multiplier;
      }
      const j = i * 4;
      bytes[j + 0] = Math.round(r);
      bytes[j + 1] = Math.round(g);
      bytes[j + 2] = Math.round(b);
      bytes[j + 3] = Math.round(a);
    }
    if (canvas != null) {
      canvas.width = width;
      canvas.height = height;
      const ctx = canvas.getContext("2d");
      const imageData = new ImageData(bytes, width, height);
      ctx.putImageData(imageData, 0, 0);
    }
    if ($img !== img) {
      $img.dispose();
    }
    return bytes;
  }
  const fromPixels = op({fromPixels_});

  // node_modules/@tensorflow/tfjs-core/dist/ops/gather_nd_util.js
  const gather_nd_util_exports = {};
  __export(gather_nd_util_exports, {
    prepareAndValidate: () => prepareAndValidate
  });
  function prepareAndValidate(tensor17, indices) {
    if (tensor17.rank < 1) {
      throw new Error(`tf.gatherND() expects the input to be rank 1 or higher, but the rank was ${tensor17.rank}.`);
    }
    if (indices.rank < 1) {
      throw new Error(`tf.gatherND() expects the indices to be rank 1 or higher, but the rank was ${indices.rank}.`);
    }
    if (indices.dtype !== "int32") {
      throw new Error(`tf.gatherND() expects the indices to be int32 type, but the dtype was ${indices.dtype}.`);
    }
    if (indices.shape[indices.rank - 1] > tensor17.rank) {
      throw new Error(`index innermost dimension length must be <= tensor rank; saw: ${indices.shape[indices.rank - 1]} vs. ${tensor17.rank}`);
    }
    if (tensor17.size === 0) {
      throw new Error(`Requested more than 0 entries, but input is empty. Input shape: ${tensor17.shape}.`);
    }
    const indicesShape = indices.shape;
    const sliceRank = indicesShape[indicesShape.length - 1];
    let nResult = 1;
    for (let i = 0; i < indicesShape.length - 1; ++i) {
      nResult *= indicesShape[i];
    }
    const inputShape = tensor17.shape;
    const resultShape = indicesShape.slice();
    resultShape.pop();
    let sliceSize = 1;
    for (let i = sliceRank; i < tensor17.rank; ++i) {
      sliceSize *= inputShape[i];
      resultShape.push(inputShape[i]);
    }
    const strides = [
      ...computeStrides(tensor17.shape).map((stride) => stride / sliceSize),
      1
    ].slice(0, sliceRank);
    return [resultShape, nResult, sliceSize, strides];
  }

  // node_modules/@tensorflow/tfjs-core/dist/serialization.js
  const serialization_exports = {};
  __export(serialization_exports, {
    Serializable: () => Serializable,
    SerializationMap: () => SerializationMap,
    registerClass: () => registerClass
  });
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class Serializable {
    getClassName() {
      return this.constructor.className;
    }
    static fromConfig(cls, config2) {
      return new cls(config2);
    }
  }
  class SerializationMap {
    constructor() {
      this.classNameMap = {};
    }
    static getMap() {
      if (SerializationMap.instance == null) {
        SerializationMap.instance = new SerializationMap();
      }
      return SerializationMap.instance;
    }
    static register(cls) {
      SerializationMap.getMap().classNameMap[cls.className] = [cls, cls.fromConfig];
    }
  }
  function registerClass(cls) {
    assert(cls.className != null, () => `Class being registered does not have the static className property defined.`);
    assert(typeof cls.className === "string", () => `className is required to be a string, but got type ` + typeof cls.className);
    assert(cls.className.length > 0, () => `Class being registered has an empty-string as its className, which is disallowed.`);
    SerializationMap.register(cls);
  }

  // node_modules/@tensorflow/tfjs-core/dist/version.js
  /** @license See the LICENSE file. */
  const version = "2.3.0";

  // node_modules/@tensorflow/tfjs-core/dist/optimizers/optimizer.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class Optimizer extends Serializable {
    minimize(f, returnCost = false, varList) {
      const {value, grads: grads2} = this.computeGradients(f, varList);
      if (varList != null) {
        const gradArray = varList.map((v) => ({name: v.name, tensor: grads2[v.name]}));
        this.applyGradients(gradArray);
      } else {
        this.applyGradients(grads2);
      }
      dispose(grads2);
      if (returnCost) {
        return value;
      } else {
        value.dispose();
        return null;
      }
    }
    get iterations() {
      if (this.iterations_ == null) {
        this.iterations_ = 0;
      }
      return this.iterations_;
    }
    incrementIterations() {
      this.iterations_ = this.iterations + 1;
    }
    computeGradients(f, varList) {
      return variableGrads(f, varList);
    }
    dispose() {
      if (this.iterations_ != null) {
        dispose(this.iterations_);
      }
    }
    async saveIterations() {
      if (this.iterations_ == null) {
        this.iterations_ = 0;
      }
      return {
        name: "iter",
        tensor: scalar(this.iterations_, "int32")
      };
    }
    async getWeights() {
      throw new Error("getWeights() is not implemented for this optimizer yet.");
    }
    async setWeights(weightValues) {
      throw new Error(`setWeights() is not implemented for this optimizer class ${this.getClassName()}`);
    }
    async extractIterations(weightValues) {
      this.iterations_ = (await weightValues[0].tensor.data())[0];
      return weightValues.slice(1);
    }
  }
  Object.defineProperty(Optimizer, Symbol.hasInstance, {
    value: (instance) => {
      return instance.minimize != null && instance.computeGradients != null && instance.applyGradients != null;
    }
  });

  // node_modules/@tensorflow/tfjs-core/dist/optimizers/adadelta_optimizer.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class AdadeltaOptimizer extends Optimizer {
    constructor(learningRate, rho, epsilon2 = null) {
      super();
      this.learningRate = learningRate;
      this.rho = rho;
      this.epsilon = epsilon2;
      this.accumulatedGrads = [];
      this.accumulatedUpdates = [];
      if (epsilon2 == null) {
        this.epsilon = ENGINE.backend.epsilon();
      }
    }
    applyGradients(variableGradients) {
      const variableNames = Array.isArray(variableGradients) ? variableGradients.map((item) => item.name) : Object.keys(variableGradients);
      variableNames.forEach((name, i) => {
        const value = ENGINE.registeredVariables[name];
        const trainable = false;
        if (this.accumulatedGrads[i] == null) {
          this.accumulatedGrads[i] = {
            originalName: `${name}/accum_grad`,
            variable: tidy(() => zerosLike(value).variable(trainable))
          };
        }
        if (this.accumulatedUpdates[i] == null) {
          this.accumulatedUpdates[i] = {
            originalName: `${name}/accum_var`,
            variable: tidy(() => zerosLike(value).variable(trainable))
          };
        }
        const gradient = Array.isArray(variableGradients) ? variableGradients[i].tensor : variableGradients[name];
        if (gradient == null) {
          return;
        }
        const accumulatedGrad = this.accumulatedGrads[i].variable;
        const accumulatedUpdate = this.accumulatedUpdates[i].variable;
        tidy(() => {
          const newAccumulatedGrad = add2(mul(accumulatedGrad, this.rho), mul(square(gradient), 1 - this.rho));
          const updates = mul(div(sqrt(add2(accumulatedUpdate, this.epsilon)), sqrt(add2(accumulatedGrad, this.epsilon))), gradient);
          const newAccumulatedUpdate = add2(mul(accumulatedUpdate, this.rho), mul(square(updates), 1 - this.rho));
          accumulatedGrad.assign(newAccumulatedGrad);
          accumulatedUpdate.assign(newAccumulatedUpdate);
          const newValue = add2(mul(updates, -this.learningRate), value);
          value.assign(newValue);
        });
      });
      this.incrementIterations();
    }
    dispose() {
      if (this.accumulatedUpdates != null) {
        dispose(this.accumulatedGrads.map((v) => v.variable));
        dispose(this.accumulatedUpdates.map((v) => v.variable));
      }
    }
    async getWeights() {
      const variables = [...this.accumulatedGrads, ...this.accumulatedUpdates];
      return [await this.saveIterations()].concat(variables.map((v) => ({name: v.originalName, tensor: v.variable})));
    }
    async setWeights(weightValues) {
      weightValues = await this.extractIterations(weightValues);
      const variableCount = weightValues.length / 2;
      const trainable = false;
      this.accumulatedGrads = weightValues.slice(0, variableCount).map((v) => ({
        originalName: v.name,
        variable: v.tensor.variable(trainable)
      }));
      this.accumulatedUpdates = weightValues.slice(variableCount, variableCount * 2).map((v) => ({
        originalName: v.name,
        variable: v.tensor.variable(trainable)
      }));
    }
    getConfig() {
      return {
        learningRate: this.learningRate,
        rho: this.rho,
        epsilon: this.epsilon
      };
    }
    static fromConfig(cls, config2) {
      return new cls(config2["learningRate"], config2["rho"], config2["epsilon"]);
    }
  }
  AdadeltaOptimizer.className = "Adadelta";
  registerClass(AdadeltaOptimizer);

  // node_modules/@tensorflow/tfjs-core/dist/optimizers/adagrad_optimizer.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class AdagradOptimizer extends Optimizer {
    constructor(learningRate, initialAccumulatorValue = 0.1) {
      super();
      this.learningRate = learningRate;
      this.initialAccumulatorValue = initialAccumulatorValue;
      this.accumulatedGrads = [];
    }
    applyGradients(variableGradients) {
      const variableNames = Array.isArray(variableGradients) ? variableGradients.map((item) => item.name) : Object.keys(variableGradients);
      variableNames.forEach((name, i) => {
        const value = ENGINE.registeredVariables[name];
        if (this.accumulatedGrads[i] == null) {
          const trainable = false;
          this.accumulatedGrads[i] = {
            originalName: `${name}/accumulator`,
            variable: tidy(() => fill(value.shape, this.initialAccumulatorValue).variable(trainable))
          };
        }
        const gradient = Array.isArray(variableGradients) ? variableGradients[i].tensor : variableGradients[name];
        if (gradient == null) {
          return;
        }
        const accumulatedGrad = this.accumulatedGrads[i].variable;
        tidy(() => {
          const newAccumulatedGrad = add2(accumulatedGrad, square(gradient));
          accumulatedGrad.assign(newAccumulatedGrad);
          const newValue = add2(mul(div(gradient, sqrt(add2(newAccumulatedGrad, ENGINE.backend.epsilon()))), -this.learningRate), value);
          value.assign(newValue);
        });
      });
      this.incrementIterations();
    }
    dispose() {
      if (this.accumulatedGrads != null) {
        dispose(this.accumulatedGrads.map((v) => v.variable));
      }
    }
    async getWeights() {
      return [await this.saveIterations()].concat(this.accumulatedGrads.map((v) => ({name: v.originalName, tensor: v.variable})));
    }
    async setWeights(weightValues) {
      weightValues = await this.extractIterations(weightValues);
      const trainable = false;
      this.accumulatedGrads = weightValues.map((v) => ({originalName: v.name, variable: v.tensor.variable(trainable)}));
    }
    getConfig() {
      return {
        learningRate: this.learningRate,
        initialAccumulatorValue: this.initialAccumulatorValue
      };
    }
    static fromConfig(cls, config2) {
      return new cls(config2["learningRate"], config2["initialAccumulatorValue"]);
    }
  }
  AdagradOptimizer.className = "Adagrad";
  registerClass(AdagradOptimizer);

  // node_modules/@tensorflow/tfjs-core/dist/optimizers/adam_optimizer.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class AdamOptimizer extends Optimizer {
    constructor(learningRate, beta1, beta2, epsilon2 = null) {
      super();
      this.learningRate = learningRate;
      this.beta1 = beta1;
      this.beta2 = beta2;
      this.epsilon = epsilon2;
      this.accumulatedFirstMoment = [];
      this.accumulatedSecondMoment = [];
      tidy(() => {
        this.accBeta1 = scalar(beta1).variable();
        this.accBeta2 = scalar(beta2).variable();
      });
      if (epsilon2 == null) {
        this.epsilon = ENGINE.backend.epsilon();
      }
    }
    applyGradients(variableGradients) {
      const varNames = Array.isArray(variableGradients) ? variableGradients.map((v) => v.name) : Object.keys(variableGradients);
      tidy(() => {
        const oneMinusAccBeta1 = sub(1, this.accBeta1);
        const oneMinusAccBeta2 = sub(1, this.accBeta2);
        varNames.forEach((name, i) => {
          const value = ENGINE.registeredVariables[name];
          const trainable = false;
          if (this.accumulatedFirstMoment[i] == null) {
            this.accumulatedFirstMoment[i] = {
              originalName: `${name}/m`,
              variable: tidy(() => zerosLike(value).variable(trainable))
            };
          }
          if (this.accumulatedSecondMoment[i] == null) {
            this.accumulatedSecondMoment[i] = {
              originalName: `${name}/v`,
              variable: tidy(() => zerosLike(value).variable(trainable))
            };
          }
          const gradient = Array.isArray(variableGradients) ? variableGradients[i].tensor : variableGradients[name];
          if (gradient == null) {
            return;
          }
          const firstMoment = this.accumulatedFirstMoment[i].variable;
          const secondMoment = this.accumulatedSecondMoment[i].variable;
          const newFirstMoment = add2(mul(firstMoment, this.beta1), mul(gradient, 1 - this.beta1));
          const newSecondMoment = add2(mul(secondMoment, this.beta2), mul(square(gradient), 1 - this.beta2));
          const biasCorrectedFirstMoment = div(newFirstMoment, oneMinusAccBeta1);
          const biasCorrectedSecondMoment = div(newSecondMoment, oneMinusAccBeta2);
          firstMoment.assign(newFirstMoment);
          secondMoment.assign(newSecondMoment);
          const newValue = add2(mul(div(biasCorrectedFirstMoment, add2(sqrt(biasCorrectedSecondMoment), this.epsilon)), -this.learningRate), value);
          value.assign(newValue);
        });
        this.accBeta1.assign(mul(this.accBeta1, this.beta1));
        this.accBeta2.assign(mul(this.accBeta2, this.beta2));
      });
      this.incrementIterations();
    }
    dispose() {
      this.accBeta1.dispose();
      this.accBeta2.dispose();
      if (this.accumulatedFirstMoment != null) {
        dispose(this.accumulatedFirstMoment.map((v) => v.variable));
      }
      if (this.accumulatedSecondMoment != null) {
        dispose(this.accumulatedSecondMoment.map((v) => v.variable));
      }
    }
    async getWeights() {
      const variables = [...this.accumulatedFirstMoment, ...this.accumulatedSecondMoment];
      return [await this.saveIterations()].concat(variables.map((v) => ({name: v.originalName, tensor: v.variable})));
    }
    async setWeights(weightValues) {
      weightValues = await this.extractIterations(weightValues);
      tidy(() => {
        this.accBeta1.assign(pow(this.beta1, this.iterations_ + 1));
        this.accBeta2.assign(pow(this.beta2, this.iterations_ + 1));
      });
      const variableCount = weightValues.length / 2;
      const trainable = false;
      this.accumulatedFirstMoment = weightValues.slice(0, variableCount).map((v) => ({
        originalName: v.name,
        variable: v.tensor.variable(trainable)
      }));
      this.accumulatedSecondMoment = weightValues.slice(variableCount, variableCount * 2).map((v) => ({
        originalName: v.name,
        variable: v.tensor.variable(trainable)
      }));
    }
    getConfig() {
      return {
        learningRate: this.learningRate,
        beta1: this.beta1,
        beta2: this.beta2,
        epsilon: this.epsilon
      };
    }
    static fromConfig(cls, config2) {
      return new cls(config2["learningRate"], config2["beta1"], config2["beta2"], config2["epsilon"]);
    }
  }
  AdamOptimizer.className = "Adam";
  registerClass(AdamOptimizer);

  // node_modules/@tensorflow/tfjs-core/dist/optimizers/adamax_optimizer.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class AdamaxOptimizer extends Optimizer {
    constructor(learningRate, beta1, beta2, epsilon2 = null, decay = 0) {
      super();
      this.learningRate = learningRate;
      this.beta1 = beta1;
      this.beta2 = beta2;
      this.epsilon = epsilon2;
      this.decay = decay;
      this.accumulatedFirstMoment = [];
      this.accumulatedWeightedInfNorm = [];
      tidy(() => {
        this.iteration = scalar(0).variable();
        this.accBeta1 = scalar(beta1).variable();
      });
      if (epsilon2 == null) {
        this.epsilon = ENGINE.backend.epsilon();
      }
    }
    applyGradients(variableGradients) {
      const variableNames = Array.isArray(variableGradients) ? variableGradients.map((item) => item.name) : Object.keys(variableGradients);
      tidy(() => {
        const oneMinusAccBeta1 = sub(1, this.accBeta1);
        const lr = div(-this.learningRate, add2(mul(this.iteration, this.decay), 1));
        variableNames.forEach((name, i) => {
          const value = ENGINE.registeredVariables[name];
          const trainable = false;
          if (this.accumulatedFirstMoment[i] == null) {
            this.accumulatedFirstMoment[i] = {
              originalName: `${name}/m`,
              variable: zerosLike(value).variable(trainable)
            };
          }
          if (this.accumulatedWeightedInfNorm[i] == null) {
            this.accumulatedWeightedInfNorm[i] = {
              originalName: `${name}/v`,
              variable: zerosLike(value).variable(trainable)
            };
          }
          const gradient = Array.isArray(variableGradients) ? variableGradients[i].tensor : variableGradients[name];
          if (gradient == null) {
            return;
          }
          const firstMoment = this.accumulatedFirstMoment[i].variable;
          const weightedInfNorm = this.accumulatedWeightedInfNorm[i].variable;
          const newFirstMoment = add2(mul(firstMoment, this.beta1), mul(gradient, 1 - this.beta1));
          const ut0 = mul(weightedInfNorm, this.beta2);
          const ut1 = abs(gradient);
          const newWeightedInfNorm = maximum(ut0, ut1);
          firstMoment.assign(newFirstMoment);
          weightedInfNorm.assign(newWeightedInfNorm);
          const newValue = add2(mul(div(lr, oneMinusAccBeta1), div(newFirstMoment, add2(newWeightedInfNorm, this.epsilon))), value);
          value.assign(newValue);
        });
        this.iteration.assign(add2(this.iteration, 1));
        this.accBeta1.assign(mul(this.accBeta1, this.beta1));
      });
      this.incrementIterations();
    }
    dispose() {
      this.accBeta1.dispose();
      this.iteration.dispose();
      if (this.accumulatedFirstMoment != null) {
        dispose(this.accumulatedFirstMoment.map((v) => v.variable));
      }
      if (this.accumulatedWeightedInfNorm != null) {
        dispose(this.accumulatedWeightedInfNorm.map((v) => v.variable));
      }
    }
    async getWeights() {
      throw new Error("getWeights() is not implemented for Adamax yet.");
    }
    async setWeights(weightValues) {
      throw new Error("setWeights() is not implemented for Adamax yet.");
    }
    getConfig() {
      return {
        learningRate: this.learningRate,
        beta1: this.beta1,
        beta2: this.beta2,
        epsilon: this.epsilon,
        decay: this.decay
      };
    }
    static fromConfig(cls, config2) {
      return new cls(config2["learningRate"], config2["beta1"], config2["beta2"], config2["epsilon"], config2["decay"]);
    }
  }
  AdamaxOptimizer.className = "Adamax";
  registerClass(AdamaxOptimizer);

  // node_modules/@tensorflow/tfjs-core/dist/optimizers/sgd_optimizer.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class SGDOptimizer extends Optimizer {
    constructor(learningRate) {
      super();
      this.learningRate = learningRate;
      this.setLearningRate(learningRate);
    }
    applyGradients(variableGradients) {
      const varNames = Array.isArray(variableGradients) ? variableGradients.map((v) => v.name) : Object.keys(variableGradients);
      varNames.forEach((name, i) => {
        const gradient = Array.isArray(variableGradients) ? variableGradients[i].tensor : variableGradients[name];
        if (gradient == null) {
          return;
        }
        const value = ENGINE.registeredVariables[name];
        tidy(() => {
          const newValue = add2(mul(this.c, gradient), value);
          value.assign(newValue);
        });
      });
      this.incrementIterations();
    }
    setLearningRate(learningRate) {
      this.learningRate = learningRate;
      if (this.c != null) {
        this.c.dispose();
      }
      this.c = keep(scalar(-learningRate));
    }
    dispose() {
      this.c.dispose();
    }
    async getWeights() {
      return [await this.saveIterations()];
    }
    async setWeights(weightValues) {
      weightValues = await this.extractIterations(weightValues);
      if (weightValues.length !== 0) {
        throw new Error("SGD optimizer does not have settable weights.");
      }
    }
    getConfig() {
      return {learningRate: this.learningRate};
    }
    static fromConfig(cls, config2) {
      return new cls(config2["learningRate"]);
    }
  }
  SGDOptimizer.className = "SGD";
  registerClass(SGDOptimizer);

  // node_modules/@tensorflow/tfjs-core/dist/optimizers/momentum_optimizer.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class MomentumOptimizer extends SGDOptimizer {
    constructor(learningRate, momentum, useNesterov = false) {
      super(learningRate);
      this.learningRate = learningRate;
      this.momentum = momentum;
      this.useNesterov = useNesterov;
      this.accumulations = [];
      this.m = scalar(this.momentum);
    }
    applyGradients(variableGradients) {
      const variableNames = Array.isArray(variableGradients) ? variableGradients.map((item) => item.name) : Object.keys(variableGradients);
      variableNames.forEach((name, i) => {
        const value = ENGINE.registeredVariables[name];
        if (this.accumulations[i] == null) {
          const trainable = false;
          this.accumulations[i] = {
            originalName: `${name}/momentum`,
            variable: tidy(() => zerosLike(value).variable(trainable))
          };
        }
        const accumulation = this.accumulations[i].variable;
        const gradient = Array.isArray(variableGradients) ? variableGradients[i].tensor : variableGradients[name];
        if (gradient == null) {
          return;
        }
        tidy(() => {
          let newValue;
          const newAccumulation = add2(mul(this.m, accumulation), gradient);
          if (this.useNesterov) {
            newValue = add2(mul(this.c, add2(gradient, mul(newAccumulation, this.m))), value);
          } else {
            newValue = add2(mul(this.c, newAccumulation), value);
          }
          accumulation.assign(newAccumulation);
          value.assign(newValue);
        });
      });
      this.incrementIterations();
    }
    dispose() {
      this.m.dispose();
      if (this.accumulations != null) {
        dispose(this.accumulations.map((v) => v.variable));
      }
    }
    setMomentum(momentum) {
      this.momentum = momentum;
    }
    async getWeights() {
      return [await this.saveIterations()].concat(this.accumulations.map((v) => ({name: v.originalName, tensor: v.variable})));
    }
    async setWeights(weightValues) {
      weightValues = await this.extractIterations(weightValues);
      const trainable = false;
      this.accumulations = weightValues.map((v) => ({originalName: v.name, variable: v.tensor.variable(trainable)}));
    }
    getConfig() {
      return {
        learningRate: this.learningRate,
        momentum: this.momentum,
        useNesterov: this.useNesterov
      };
    }
    static fromConfig(cls, config2) {
      return new cls(config2["learningRate"], config2["momentum"], config2["useNesterov"]);
    }
  }
  MomentumOptimizer.className = "Momentum";
  registerClass(MomentumOptimizer);

  // node_modules/@tensorflow/tfjs-core/dist/optimizers/rmsprop_optimizer.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class RMSPropOptimizer extends Optimizer {
    constructor(learningRate, decay = 0.9, momentum = 0, epsilon2 = null, centered = false) {
      super();
      this.learningRate = learningRate;
      this.decay = decay;
      this.momentum = momentum;
      this.epsilon = epsilon2;
      this.accumulatedMeanSquares = [];
      this.accumulatedMoments = [];
      this.accumulatedMeanGrads = [];
      this.centered = centered;
      if (epsilon2 == null) {
        this.epsilon = ENGINE.backend.epsilon();
      }
      if (learningRate == null) {
        throw new Error(`learningRate for RMSPropOptimizer must be defined.`);
      }
    }
    applyGradients(variableGradients) {
      const variableNames = Array.isArray(variableGradients) ? variableGradients.map((item) => item.name) : Object.keys(variableGradients);
      variableNames.forEach((name, i) => {
        const value = ENGINE.registeredVariables[name];
        const trainable = false;
        if (this.accumulatedMeanSquares[i] == null) {
          this.accumulatedMeanSquares[i] = {
            originalName: `${name}/rms`,
            variable: tidy(() => zerosLike(value).variable(trainable))
          };
        }
        if (this.accumulatedMoments[i] == null) {
          this.accumulatedMoments[i] = {
            originalName: `${name}/momentum`,
            variable: tidy(() => zerosLike(value).variable(trainable))
          };
        }
        if (this.accumulatedMeanGrads[i] == null && this.centered) {
          this.accumulatedMeanGrads[i] = {
            originalName: `${name}/mg`,
            variable: tidy(() => zerosLike(value).variable(trainable))
          };
        }
        const gradient = Array.isArray(variableGradients) ? variableGradients[i].tensor : variableGradients[name];
        if (gradient == null) {
          return;
        }
        const accumulatedMeanSquare = this.accumulatedMeanSquares[i].variable;
        const accumulatedMoments = this.accumulatedMoments[i].variable;
        tidy(() => {
          const newAccumulatedMeanSquare = add2(mul(accumulatedMeanSquare, this.decay), mul(square(gradient), 1 - this.decay));
          if (this.centered) {
            const accumulatedMeanGrad = this.accumulatedMeanGrads[i].variable;
            const newAccumulatedMeanGrad = add2(mul(accumulatedMeanGrad, this.decay), mul(gradient, 1 - this.decay));
            const gradContribution = div(mul(gradient, this.learningRate), sqrt(sub(newAccumulatedMeanSquare, add2(square(newAccumulatedMeanGrad), this.epsilon))));
            const newAccumulatedMoments = add2(mul(accumulatedMoments, this.momentum), gradContribution);
            accumulatedMeanSquare.assign(newAccumulatedMeanSquare);
            accumulatedMeanGrad.assign(newAccumulatedMeanGrad);
            accumulatedMoments.assign(newAccumulatedMoments);
            const newValue = sub(value, newAccumulatedMoments);
            value.assign(newValue);
          } else {
            const newAccumulatedMeanSquare2 = add2(mul(accumulatedMeanSquare, this.decay), mul(square(gradient), 1 - this.decay));
            const newAccumulatedMoments = add2(mul(accumulatedMoments, this.momentum), div(mul(gradient, this.learningRate), sqrt(add2(newAccumulatedMeanSquare2, this.epsilon))));
            accumulatedMeanSquare.assign(newAccumulatedMeanSquare2);
            accumulatedMoments.assign(newAccumulatedMoments);
            const newValue = sub(value, newAccumulatedMoments);
            value.assign(newValue);
          }
        });
      });
      this.incrementIterations();
    }
    dispose() {
      if (this.accumulatedMeanSquares != null) {
        dispose(this.accumulatedMeanSquares.map((v) => v.variable));
      }
      if (this.accumulatedMeanGrads != null && this.centered) {
        dispose(this.accumulatedMeanGrads.map((v) => v.variable));
      }
      if (this.accumulatedMoments != null) {
        dispose(this.accumulatedMoments.map((v) => v.variable));
      }
    }
    async getWeights() {
      const variables = [...this.accumulatedMeanSquares, ...this.accumulatedMoments];
      if (this.centered) {
        variables.push(...this.accumulatedMeanGrads);
      }
      return [await this.saveIterations()].concat(variables.map((v) => ({name: v.originalName, tensor: v.variable})));
    }
    async setWeights(weightValues) {
      weightValues = await this.extractIterations(weightValues);
      const variableCount = this.centered ? weightValues.length / 3 : weightValues.length / 2;
      const trainable = false;
      this.accumulatedMeanSquares = weightValues.slice(0, variableCount).map((v) => ({
        originalName: v.name,
        variable: v.tensor.variable(trainable)
      }));
      this.accumulatedMoments = weightValues.slice(variableCount, variableCount * 2).map((v) => ({
        originalName: v.name,
        variable: v.tensor.variable(trainable)
      }));
      if (this.centered) {
        this.accumulatedMeanGrads = weightValues.slice(variableCount * 2, variableCount * 3).map((v) => ({
          originalName: v.name,
          variable: v.tensor.variable(trainable)
        }));
      }
    }
    getConfig() {
      return {
        learningRate: this.learningRate,
        decay: this.decay,
        momentum: this.momentum,
        epsilon: this.epsilon,
        centered: this.centered
      };
    }
    static fromConfig(cls, config2) {
      return new cls(config2["learningRate"], config2["decay"], config2["momentum"], config2["epsilon"], config2["centered"]);
    }
  }
  RMSPropOptimizer.className = "RMSProp";
  registerClass(RMSPropOptimizer);

  // node_modules/@tensorflow/tfjs-core/dist/optimizers/optimizer_constructors.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  class OptimizerConstructors {
    static sgd(learningRate) {
      return new SGDOptimizer(learningRate);
    }
    static momentum(learningRate, momentum, useNesterov = false) {
      return new MomentumOptimizer(learningRate, momentum, useNesterov);
    }
    static rmsprop(learningRate, decay = 0.9, momentum = 0, epsilon2 = null, centered = false) {
      return new RMSPropOptimizer(learningRate, decay, momentum, epsilon2, centered);
    }
    static adam(learningRate = 1e-3, beta1 = 0.9, beta2 = 0.999, epsilon2 = null) {
      return new AdamOptimizer(learningRate, beta1, beta2, epsilon2);
    }
    static adadelta(learningRate = 1e-3, rho = 0.95, epsilon2 = null) {
      return new AdadeltaOptimizer(learningRate, rho, epsilon2);
    }
    static adamax(learningRate = 2e-3, beta1 = 0.9, beta2 = 0.999, epsilon2 = null, decay = 0) {
      return new AdamaxOptimizer(learningRate, beta1, beta2, epsilon2, decay);
    }
    static adagrad(learningRate, initialAccumulatorValue = 0.1) {
      return new AdagradOptimizer(learningRate, initialAccumulatorValue);
    }
  }

  // node_modules/@tensorflow/tfjs-core/dist/train.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  [
    MomentumOptimizer,
    SGDOptimizer,
    AdadeltaOptimizer,
    AdagradOptimizer,
    RMSPropOptimizer,
    AdamaxOptimizer,
    AdamOptimizer
  ];
  const train = {
    sgd: OptimizerConstructors.sgd,
    momentum: OptimizerConstructors.momentum,
    adadelta: OptimizerConstructors.adadelta,
    adagrad: OptimizerConstructors.adagrad,
    rmsprop: OptimizerConstructors.rmsprop,
    adamax: OptimizerConstructors.adamax,
    adam: OptimizerConstructors.adam
  };

  // node_modules/@tensorflow/tfjs-core/dist/browser_util.js
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const delayCallback = (() => {
    if (typeof requestAnimationFrame !== "undefined") {
      return requestAnimationFrame;
    } else if (typeof setImmediate !== "undefined") {
      return setImmediate;
    }
    return (f) => f();
  })();
  function nextFrame() {
    return new Promise((resolve) => delayCallback(() => resolve()));
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/rotate_util.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function getImageCenter(center, imageHeight, imageWidth) {
    const centerX = imageWidth * (typeof center === "number" ? center : center[0]);
    const centerY = imageHeight * (typeof center === "number" ? center : center[1]);
    return [centerX, centerY];
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/array_ops_util.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function getReshaped(inputShape, blockShape, prod3, batchToSpace = true) {
    let reshaped = [];
    if (batchToSpace) {
      reshaped = reshaped.concat(blockShape.slice(0));
      reshaped.push(inputShape[0] / prod3);
      reshaped = reshaped.concat(inputShape.slice(1));
    } else {
      reshaped = reshaped.concat(inputShape[0]);
      const spatialLength = blockShape.length;
      for (let i = 0; i < spatialLength; ++i) {
        reshaped = reshaped.concat([inputShape[i + 1] / blockShape[i], blockShape[i]]);
      }
      reshaped = reshaped.concat(inputShape.slice(spatialLength + 1));
    }
    return reshaped;
  }
  function getPermuted(reshapedRank, blockShapeRank, batchToSpace = true) {
    const permuted = [];
    if (batchToSpace) {
      permuted.push(blockShapeRank);
      for (let i = blockShapeRank + 1; i < reshapedRank; ++i) {
        if (i <= 2 * blockShapeRank) {
          permuted.push(i);
          permuted.push(i - (blockShapeRank + 1));
        } else {
          permuted.push(i);
        }
      }
    } else {
      const permutedBeforeBatch = [];
      const permutedAfterBatch = [];
      for (let i = 1; i < reshapedRank; ++i) {
        if (i >= blockShapeRank * 2 + 1 || i % 2 === 1) {
          permutedAfterBatch.push(i);
        } else {
          permutedBeforeBatch.push(i);
        }
      }
      permuted.push(...permutedBeforeBatch);
      permuted.push(0);
      permuted.push(...permutedAfterBatch);
    }
    return permuted;
  }
  function getReshapedPermuted(inputShape, blockShape, prod3, batchToSpace = true) {
    const reshapedPermuted = [];
    if (batchToSpace) {
      reshapedPermuted.push(inputShape[0] / prod3);
    } else {
      reshapedPermuted.push(inputShape[0] * prod3);
    }
    for (let i = 1; i < inputShape.length; ++i) {
      if (i <= blockShape.length) {
        if (batchToSpace) {
          reshapedPermuted.push(blockShape[i - 1] * inputShape[i]);
        } else {
          reshapedPermuted.push(inputShape[i] / blockShape[i - 1]);
        }
      } else {
        reshapedPermuted.push(inputShape[i]);
      }
    }
    return reshapedPermuted;
  }
  function getSliceBeginCoords(crops, blockShape) {
    const sliceBeginCoords = [0];
    for (let i = 0; i < blockShape; ++i) {
      sliceBeginCoords.push(crops[i][0]);
    }
    return sliceBeginCoords;
  }
  function getSliceSize(uncroppedShape, crops, blockShape) {
    const sliceSize = uncroppedShape.slice(0, 1);
    for (let i = 0; i < blockShape; ++i) {
      sliceSize.push(uncroppedShape[i + 1] - crops[i][0] - crops[i][1]);
    }
    return sliceSize;
  }

  // node_modules/@tensorflow/tfjs-core/dist/ops/selu_util.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const SELU_SCALEALPHA = 1.7580993408473768;
  const SELU_SCALE = 1.0507009873554805;

  // node_modules/@tensorflow/tfjs-core/dist/ops/erf_util.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const ERF_P = 0.3275911;
  const ERF_A1 = 0.254829592;
  const ERF_A2 = -0.284496736;
  const ERF_A3 = 1.421413741;
  const ERF_A4 = -1.453152027;
  const ERF_A5 = 1.061405429;

  // node_modules/@tensorflow/tfjs-core/dist/log.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function warn(...msg) {
    if (!env().getBool("IS_TEST")) {
      console.warn(...msg);
    }
  }
  function log6(...msg) {
    if (!env().getBool("IS_TEST")) {
      console.log(...msg);
    }
  }

  // node_modules/@tensorflow/tfjs-core/dist/backends/complex_util.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function mergeRealAndImagArrays(real6, imag6) {
    if (real6.length !== imag6.length) {
      throw new Error(`Cannot merge real and imag arrays of different lengths. real:${real6.length}, imag: ${imag6.length}.`);
    }
    const result = new Float32Array(real6.length * 2);
    for (let i = 0; i < result.length; i += 2) {
      result[i] = real6[i / 2];
      result[i + 1] = imag6[i / 2];
    }
    return result;
  }
  function splitRealAndImagArrays(complex9) {
    const real6 = new Float32Array(complex9.length / 2);
    const imag6 = new Float32Array(complex9.length / 2);
    for (let i = 0; i < complex9.length; i += 2) {
      real6[i / 2] = complex9[i];
      imag6[i / 2] = complex9[i + 1];
    }
    return {real: real6, imag: imag6};
  }
  function complexWithEvenIndex(complex9) {
    const len = Math.ceil(complex9.length / 4);
    const real6 = new Float32Array(len);
    const imag6 = new Float32Array(len);
    for (let i = 0; i < complex9.length; i += 4) {
      real6[Math.floor(i / 4)] = complex9[i];
      imag6[Math.floor(i / 4)] = complex9[i + 1];
    }
    return {real: real6, imag: imag6};
  }
  function complexWithOddIndex(complex9) {
    const len = Math.floor(complex9.length / 4);
    const real6 = new Float32Array(len);
    const imag6 = new Float32Array(len);
    for (let i = 2; i < complex9.length; i += 4) {
      real6[Math.floor(i / 4)] = complex9[i];
      imag6[Math.floor(i / 4)] = complex9[i + 1];
    }
    return {real: real6, imag: imag6};
  }
  function getComplexWithIndex(complex9, index) {
    const real6 = complex9[index * 2];
    const imag6 = complex9[index * 2 + 1];
    return {real: real6, imag: imag6};
  }
  function assignToTypedArray(data, real6, imag6, index) {
    data[index * 2] = real6;
    data[index * 2 + 1] = imag6;
  }
  function exponents(n, inverse) {
    const real6 = new Float32Array(n / 2);
    const imag6 = new Float32Array(n / 2);
    for (let i = 0; i < Math.ceil(n / 2); i++) {
      const x = (inverse ? 2 : -2) * Math.PI * (i / n);
      real6[i] = Math.cos(x);
      imag6[i] = Math.sin(x);
    }
    return {real: real6, imag: imag6};
  }
  function exponent(k, n, inverse) {
    const x = (inverse ? 2 : -2) * Math.PI * (k / n);
    const real6 = Math.cos(x);
    const imag6 = Math.sin(x);
    return {real: real6, imag: imag6};
  }

  // node_modules/@tensorflow/tfjs-core/dist/backends/backend_util.js
  const backend_util_exports = {};
  __export(backend_util_exports, {
    ERF_A1: () => ERF_A1,
    ERF_A2: () => ERF_A2,
    ERF_A3: () => ERF_A3,
    ERF_A4: () => ERF_A4,
    ERF_A5: () => ERF_A5,
    ERF_P: () => ERF_P,
    PARALLELIZE_THRESHOLD: () => PARALLELIZE_THRESHOLD,
    SELU_SCALE: () => SELU_SCALE,
    SELU_SCALEALPHA: () => SELU_SCALEALPHA,
    applyActivation: () => applyActivation,
    assertAndGetBroadcastShape: () => assertAndGetBroadcastShape,
    assertAxesAreInnerMostDims: () => assertAxesAreInnerMostDims,
    assertParamsConsistent: () => assertParamsConsistent,
    assignToTypedArray: () => assignToTypedArray,
    axesAreInnerMostDims: () => axesAreInnerMostDims,
    calculateShapes: () => calculateShapes,
    castTensor: () => castTensor,
    combineLocations: () => combineLocations,
    complexWithEvenIndex: () => complexWithEvenIndex,
    complexWithOddIndex: () => complexWithOddIndex,
    computeConv2DInfo: () => computeConv2DInfo,
    computeConv3DInfo: () => computeConv3DInfo,
    computeDefaultPad: () => computeDefaultPad,
    computeDilation2DInfo: () => computeDilation2DInfo,
    computeOptimalWindowSize: () => computeOptimalWindowSize,
    computeOutAndReduceShapes: () => computeOutAndReduceShapes,
    computeOutShape: () => computeOutShape,
    computePool2DInfo: () => computePool2DInfo,
    computePool3DInfo: () => computePool3DInfo,
    convertConv2DDataFormat: () => convertConv2DDataFormat,
    eitherStridesOrDilationsAreOne: () => eitherStridesOrDilationsAreOne,
    expandShapeToKeepDim: () => expandShapeToKeepDim,
    exponent: () => exponent,
    exponents: () => exponents,
    getAxesPermutation: () => getAxesPermutation,
    getBroadcastDims: () => getBroadcastDims,
    getComplexWithIndex: () => getComplexWithIndex,
    getFusedBiasGradient: () => getFusedBiasGradient,
    getFusedDyActivation: () => getFusedDyActivation,
    getImageCenter: () => getImageCenter,
    getInnerMostAxes: () => getInnerMostAxes,
    getPermuted: () => getPermuted,
    getReductionAxes: () => getReductionAxes,
    getReshaped: () => getReshaped,
    getReshapedPermuted: () => getReshapedPermuted,
    getSliceBeginCoords: () => getSliceBeginCoords,
    getSliceSize: () => getSliceSize,
    getUndoAxesPermutation: () => getUndoAxesPermutation,
    linspaceImpl: () => linspaceImpl,
    log: () => log6,
    mergeRealAndImagArrays: () => mergeRealAndImagArrays,
    prepareAndValidate: () => prepareAndValidate,
    prepareSplitSize: () => prepareSplitSize,
    reshapeTensor: () => reshapeTensor,
    segment_util: () => segment_util_exports,
    shouldFuse: () => shouldFuse,
    splitRealAndImagArrays: () => splitRealAndImagArrays,
    tupleValuesAreOne: () => tupleValuesAreOne,
    upcastType: () => upcastType,
    validateInput: () => validateInput,
    validateUpdateShape: () => validateUpdateShape,
    warn: () => warn
  });
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function castTensor(x, dtype, backend2) {
    if (dtype === "complex64") {
      if (x.dtype === "complex64") {
        return x.clone();
      }
      const zerosTensor = zeros(x.shape);
      const floatX = cast(x, "float32");
      const result = backend2.complex(floatX, zerosTensor);
      zerosTensor.dispose();
      floatX.dispose();
      return result;
    }
    if (!hasEncodingLoss(x.dtype, dtype)) {
      return ENGINE.makeTensorFromDataId(x.dataId, x.shape, dtype);
    }
    if (x.dtype === "complex64") {
      const real6 = backend2.real(x);
      const result = cast(real6, dtype);
      real6.dispose();
      return result;
    }
    if (dtype === "int32") {
      return backend2.int(x);
    } else if (dtype === "bool") {
      const zero = scalar(0, x.dtype);
      const result = backend2.notEqual(x, zero);
      zero.dispose();
      return result;
    } else {
      throw new Error(`Error in Cast: failed to cast ${x.dtype} to ${dtype}`);
    }
  }
  function reshapeTensor(x, shape) {
    return ENGINE.makeTensorFromDataId(x.dataId, shape, x.dtype);
  }
  function linspaceImpl(start, stop, num) {
    const step7 = (stop - start) / (num - 1);
    const values = makeZerosTypedArray(num, "float32");
    values[0] = start;
    for (let i = 1; i < values.length; i++) {
      values[i] = values[i - 1] + step7;
    }
    return tensor1d(values, "float32");
  }

  // node_modules/@tensorflow/tfjs-core/dist/backends/split_shared.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function split5(x, sizeSplits, axis) {
    const begin = new Array(x.rank).fill(0);
    const size = x.shape.slice();
    return sizeSplits.map((s) => {
      const sliceSize = [...size];
      sliceSize[axis] = s;
      const sliceT = slice(x, begin, sliceSize);
      begin[axis] += s;
      return sliceT;
    });
  }

  // node_modules/@tensorflow/tfjs-core/dist/backends/tile_impl.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function tile4(xBuf, reps) {
    const newShape = new Array(xBuf.rank);
    for (let i = 0; i < newShape.length; i++) {
      newShape[i] = xBuf.shape[i] * reps[i];
    }
    const result = buffer(newShape, xBuf.dtype);
    for (let i = 0; i < result.values.length; ++i) {
      const newLoc = result.indexToLoc(i);
      const originalLoc = new Array(xBuf.rank);
      for (let j = 0; j < originalLoc.length; j++) {
        originalLoc[j] = newLoc[j] % xBuf.shape[j];
      }
      const originalIndex = xBuf.locToIndex(originalLoc);
      result.values[i] = xBuf.values[originalIndex];
    }
    return result.toTensor();
  }

  // node_modules/@tensorflow/tfjs-core/dist/backends/topk_impl.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function topkImpl(x, xShape, xDtype, k, sorted) {
    const lastDim = xShape[xShape.length - 1];
    const [batch, size] = [x.length / lastDim, lastDim];
    const allTopKVals = getTypedArrayFromDType(xDtype, batch * k);
    const allTopKIndices = getTypedArrayFromDType("int32", batch * k);
    for (let b = 0; b < batch; b++) {
      const offset = b * size;
      const vals = x.subarray(offset, offset + size);
      const valAndInd = [];
      for (let i = 0; i < vals.length; i++) {
        valAndInd.push({value: vals[i], index: i});
      }
      valAndInd.sort((a, b2) => b2.value - a.value);
      const outOffset = b * k;
      const topKVals = allTopKVals.subarray(outOffset, outOffset + k);
      const topKIndices = allTopKIndices.subarray(outOffset, outOffset + k);
      for (let i = 0; i < k; i++) {
        topKVals[i] = valAndInd[i].value;
        topKIndices[i] = valAndInd[i].index;
      }
    }
    const outputShape = xShape.slice();
    outputShape[outputShape.length - 1] = k;
    return [
      tensor5(allTopKVals, outputShape, xDtype),
      tensor5(allTopKIndices, outputShape, "int32")
    ];
  }

  // node_modules/@tensorflow/tfjs-core/dist/backends/kernel_impls.js
  const kernel_impls_exports = {};
  __export(kernel_impls_exports, {
    nonMaxSuppressionV3Impl: () => nonMaxSuppressionV3Impl,
    nonMaxSuppressionV4Impl: () => nonMaxSuppressionV4Impl,
    nonMaxSuppressionV5Impl: () => nonMaxSuppressionV5Impl,
    split: () => split5,
    tile: () => tile4,
    topkImpl: () => topkImpl,
    whereImpl: () => whereImpl
  });
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */

  // node_modules/@tensorflow/tfjs-core/dist/backends/backend.js
  /**
   * @license
   * Copyright 2018 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const EPSILON_FLOAT32 = 1e-7;
  const EPSILON_FLOAT16 = 1e-4;
  class DataStorage {
    constructor(backend2, dataMover) {
      this.backend = backend2;
      this.dataMover = dataMover;
      this.data = new WeakMap();
      this.dataIdsCount = 0;
    }
    get(dataId) {
      if (!this.data.has(dataId)) {
        this.dataMover.moveData(this.backend, dataId);
      }
      return this.data.get(dataId);
    }
    set(dataId, value) {
      this.dataIdsCount++;
      this.data.set(dataId, value);
    }
    has(dataId) {
      return this.data.has(dataId);
    }
    delete(dataId) {
      this.dataIdsCount--;
      return this.data.delete(dataId);
    }
    numDataIds() {
      return this.dataIdsCount;
    }
  }
  class KernelBackend {
    time(f) {
      return notYetImplemented("time");
    }
    read(dataId) {
      return notYetImplemented("read");
    }
    readSync(dataId) {
      return notYetImplemented("readSync");
    }
    numDataIds() {
      return notYetImplemented("numDataIds");
    }
    disposeData(dataId) {
      return notYetImplemented("disposeData");
    }
    write(values, shape, dtype) {
      return notYetImplemented("write");
    }
    move(dataId, values, shape, dtype) {
      return notYetImplemented("move");
    }
    memory() {
      return notYetImplemented("memory");
    }
    floatPrecision() {
      return notYetImplemented("floatPrecision");
    }
    epsilon() {
      return this.floatPrecision() === 32 ? EPSILON_FLOAT32 : EPSILON_FLOAT16;
    }
    batchMatMul(a, b, transposeA, transposeB) {
      return notYetImplemented("batchMatMul");
    }
    fusedBatchMatMul({a, b, transposeA, transposeB, bias, activation, preluActivationWeights}) {
      return notYetImplemented("fusedBatchMatMul");
    }
    slice(x, begin, size) {
      return notYetImplemented("slice");
    }
    stridedSlice(x, begin, end, strides) {
      return notYetImplemented("stridedSlice");
    }
    unstack(x, axis) {
      return notYetImplemented("unstack");
    }
    reverse(a, axis) {
      return notYetImplemented("reverse");
    }
    concat(tensors, axis) {
      return notYetImplemented("concat");
    }
    neg(a) {
      return notYetImplemented("neg");
    }
    add(a, b) {
      return notYetImplemented("add");
    }
    addN(tensors) {
      return notYetImplemented("addN");
    }
    subtract(a, b) {
      return notYetImplemented("subtract");
    }
    multiply(a, b) {
      return notYetImplemented("multiply");
    }
    realDivide(a, b) {
      return notYetImplemented("realDivide");
    }
    floorDiv(a, b) {
      return notYetImplemented("floorDiv");
    }
    sum(x, axes) {
      return notYetImplemented("sum");
    }
    prod(x, axes) {
      return notYetImplemented("prod");
    }
    unsortedSegmentSum(x, segmentIds, numSegments) {
      return notYetImplemented("unsortedSegmentSum");
    }
    argMin(x, axis) {
      return notYetImplemented("argMin");
    }
    argMax(x, axis) {
      return notYetImplemented("argMax");
    }
    equal(a, b) {
      return notYetImplemented("equal");
    }
    notEqual(a, b) {
      return notYetImplemented("notEqual");
    }
    less(a, b) {
      return notYetImplemented("less");
    }
    lessEqual(a, b) {
      return notYetImplemented("lessEqual");
    }
    greater(a, b) {
      return notYetImplemented("greater");
    }
    greaterEqual(a, b) {
      return notYetImplemented("greaterEqual");
    }
    logicalNot(a) {
      return notYetImplemented("logicalNot");
    }
    logicalAnd(a, b) {
      return notYetImplemented("logicalAnd");
    }
    logicalOr(a, b) {
      return notYetImplemented("logicalOr");
    }
    where(condition) {
      return notYetImplemented("where");
    }
    select(condition, a, b) {
      return notYetImplemented("select");
    }
    topk(x, k, sorted) {
      return notYetImplemented("topk");
    }
    min(x, axes) {
      return notYetImplemented("min");
    }
    minimum(a, b) {
      return notYetImplemented("minimum");
    }
    mod(a, b) {
      return notYetImplemented("mod");
    }
    max(x, axes) {
      return notYetImplemented("max");
    }
    maximum(a, b) {
      return notYetImplemented("maximum");
    }
    all(x, axes) {
      return notYetImplemented("all");
    }
    any(x, axes) {
      return notYetImplemented("any");
    }
    squaredDifference(a, b) {
      return notYetImplemented("squaredDifference");
    }
    ceil(x) {
      return notYetImplemented("ceil");
    }
    floor(x) {
      return notYetImplemented("floor");
    }
    round(x) {
      return notYetImplemented("round");
    }
    sign(x) {
      return notYetImplemented("sign");
    }
    isNaN(x) {
      return notYetImplemented("isNaN");
    }
    isInf(x) {
      return notYetImplemented("isInf");
    }
    isFinite(x) {
      return notYetImplemented("isFinite");
    }
    pow(a, b) {
      return notYetImplemented("pow");
    }
    exp(x) {
      return notYetImplemented("exp");
    }
    expm1(x) {
      return notYetImplemented("expm1");
    }
    softmax(x, dim) {
      return notYetImplemented("softmax");
    }
    log(x) {
      return notYetImplemented("log");
    }
    log1p(x) {
      return notYetImplemented("log1p");
    }
    sqrt(x) {
      return notYetImplemented("sqrt");
    }
    rsqrt(x) {
      return notYetImplemented("rsqrt");
    }
    square(x) {
      return notYetImplemented("square");
    }
    reciprocal(x) {
      return notYetImplemented("reciprocal");
    }
    relu(x) {
      return notYetImplemented("relu");
    }
    relu6(x) {
      return notYetImplemented("relu6");
    }
    prelu(x, a) {
      return notYetImplemented("prelu");
    }
    elu(x) {
      return notYetImplemented("elu");
    }
    eluDer(dy, y) {
      return notYetImplemented("eluDer");
    }
    selu(x) {
      return notYetImplemented("selu");
    }
    int(x) {
      return notYetImplemented("int");
    }
    clip(x, min5, max7) {
      return notYetImplemented("clip");
    }
    abs(x) {
      return notYetImplemented("abs");
    }
    complexAbs(x) {
      return notYetImplemented("complexAbs");
    }
    sigmoid(x) {
      return notYetImplemented("sigmoid");
    }
    softplus(x) {
      return notYetImplemented("softplus");
    }
    sin(x) {
      return notYetImplemented("sin");
    }
    cos(x) {
      return notYetImplemented("cos");
    }
    tan(x) {
      return notYetImplemented("tan");
    }
    asin(x) {
      return notYetImplemented("asin");
    }
    acos(x) {
      return notYetImplemented("acos");
    }
    atan(x) {
      return notYetImplemented("atan");
    }
    atan2(a, b) {
      return notYetImplemented("atan2");
    }
    sinh(x) {
      return notYetImplemented("sinh");
    }
    cosh(x) {
      return notYetImplemented("cosh");
    }
    tanh(x) {
      return notYetImplemented("tanh");
    }
    asinh(x) {
      return notYetImplemented("asinh");
    }
    acosh(x) {
      return notYetImplemented("acosh");
    }
    atanh(x) {
      return notYetImplemented("atanh");
    }
    erf(x) {
      return notYetImplemented("erf");
    }
    step(x, alpha) {
      return notYetImplemented("step");
    }
    fusedConv2d({input, filter, convInfo, bias, activation, preluActivationWeights}) {
      return notYetImplemented("fusedConv2d");
    }
    conv2d(x, filter, convInfo) {
      return notYetImplemented("conv2d");
    }
    conv2dDerInput(dy, filter, convInfo) {
      return notYetImplemented("conv2dDerInput");
    }
    conv2dDerFilter(x, dY, convInfo) {
      return notYetImplemented("conv2dDerFilter");
    }
    fusedDepthwiseConv2D({input, filter, convInfo, bias, activation, preluActivationWeights}) {
      return notYetImplemented("fusedDepthwiseConv2D");
    }
    depthwiseConv2D(input, filter, convInfo) {
      return notYetImplemented("depthwiseConv2D");
    }
    depthwiseConv2DDerInput(dy, filter, convInfo) {
      return notYetImplemented("depthwiseConv2DDerInput");
    }
    depthwiseConv2DDerFilter(x, dY, convInfo) {
      return notYetImplemented("depthwiseConv2DDerFilter");
    }
    conv3d(x, filter, convInfo) {
      return notYetImplemented("conv3d");
    }
    conv3dDerInput(dy, filter, convInfo) {
      return notYetImplemented("conv3dDerInput");
    }
    conv3dDerFilter(x, dY, convInfo) {
      return notYetImplemented("conv3dDerFilter");
    }
    maxPool(x, convInfo) {
      return notYetImplemented("maxPool");
    }
    maxPoolBackprop(dy, x, y, convInfo) {
      return notYetImplemented("maxPoolBackprop");
    }
    avgPool(x, convInfo) {
      return notYetImplemented("avgPool");
    }
    avgPoolBackprop(dy, x, convInfo) {
      return notYetImplemented("avgPoolBackprop");
    }
    avgPool3d(x, convInfo) {
      return notYetImplemented("avgPool3d");
    }
    avgPool3dBackprop(dy, x, convInfo) {
      return notYetImplemented("avgPool3dBackprop");
    }
    maxPool3d(x, convInfo) {
      return notYetImplemented("maxPool3d");
    }
    maxPool3dBackprop(dy, x, y, convInfo) {
      return notYetImplemented("maxPool3dBackprop");
    }
    reshape(x, shape) {
      return notYetImplemented("reshape");
    }
    cast(x, dtype) {
      return notYetImplemented("cast");
    }
    tile(x, reps) {
      return notYetImplemented("tile");
    }
    pad(x, paddings, constantValue) {
      return notYetImplemented("pad");
    }
    transpose(x, perm) {
      return notYetImplemented("transpose");
    }
    gather(x, indices, axis) {
      return notYetImplemented("gather");
    }
    gatherND(x, indices) {
      return notYetImplemented("gatherND");
    }
    scatterND(indices, updates, shape) {
      return notYetImplemented("scatterND");
    }
    batchToSpaceND(x, blockShape, crops) {
      return notYetImplemented("batchToSpaceND");
    }
    spaceToBatchND(x, blockShape, paddings) {
      return notYetImplemented("spaceToBatchND");
    }
    resizeBilinear(x, newHeight, newWidth, alignCorners) {
      return notYetImplemented("resizeBilinear");
    }
    resizeBilinearBackprop(dy, x, alignCorners) {
      return notYetImplemented("resizeBilinearBackprop");
    }
    resizeNearestNeighbor(x, newHEight, newWidth, alignCorners) {
      return notYetImplemented("resizeNearestNeighbor");
    }
    resizeNearestNeighborBackprop(dy, x, alignCorners) {
      return notYetImplemented("resizeNearestNeighborBackprop");
    }
    batchNorm(x, mean5, variance, offset, scale2, varianceEpsilon) {
      return notYetImplemented("batchNorm");
    }
    localResponseNormalization4D(x, radius, bias, alpha, beta) {
      return notYetImplemented("localResponseNormalization4D");
    }
    LRNGrad(dy, inputImage, outputImage, radius, bias, alpha, beta) {
      return notYetImplemented("LRNGrad");
    }
    multinomial(logits, normalized, numSamples, seed) {
      return notYetImplemented("multinomial");
    }
    oneHot(indices, depth, onValue, offValue) {
      return notYetImplemented("oneHot");
    }
    cumsum(x, axis, exclusive, reverse9) {
      return notYetImplemented("cumsum");
    }
    nonMaxSuppression(boxes, scores, maxOutputSize, iouThreshold, scoreThreshold) {
      return notYetImplemented("nonMaxSuppression");
    }
    fft(x) {
      return notYetImplemented("fft");
    }
    ifft(x) {
      return notYetImplemented("ifft");
    }
    complex(real6, imag6) {
      return notYetImplemented("complex");
    }
    real(input) {
      return notYetImplemented("real");
    }
    imag(input) {
      return notYetImplemented("imag");
    }
    cropAndResize(image2, boxes, boxIndex, cropSize, method, extrapolationValue) {
      return notYetImplemented("cropAndResize");
    }
    depthToSpace(x, blockSize, dataFormat) {
      return notYetImplemented("depthToSpace");
    }
    split(value, sizeSplits, axis) {
      return notYetImplemented("split");
    }
    sparseToDense(sparseIndices, sparseValues, outputShape, defaultValue) {
      return notYetImplemented("sparseToDense");
    }
    diag(x) {
      return notYetImplemented("diag");
    }
    fill(shape, value, dtype) {
      return notYetImplemented("fill");
    }
    onesLike(x) {
      return notYetImplemented("onesLike");
    }
    zerosLike(x) {
      return notYetImplemented("zerosLike");
    }
    linspace(start, stop, num) {
      return notYetImplemented("linspace");
    }
    dispose() {
      return notYetImplemented("dispose");
    }
  }
  function notYetImplemented(kernelName) {
    throw new Error(`'${kernelName}' not yet implemented or not found in the registry. Did you forget to import the kernel?`);
  }

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Abs_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const absGradConfig = {
    kernelName: Abs,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => mul(dy, step(cast(x, "float32"), -1))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Acos_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const acosGradConfig = {
    kernelName: Acos,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {
        x: () => {
          const a = square(cast(x, "float32"));
          const b = sqrt(sub(scalar(1), a));
          return neg(div(dy, b));
        }
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Acosh_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const acoshGradConfig = {
    kernelName: Acosh,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {
        x: () => {
          const a = sqrt(sub(square(cast(x, "float32")), 1));
          return div(dy, a);
        }
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Add_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const addGradConfig = {
    kernelName: Add,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      const outShape = assertAndGetBroadcastShape(a.shape, b.shape);
      const derA = () => {
        let res = dy;
        const reduceAxes = getReductionAxes(a.shape, outShape);
        if (reduceAxes.length > 0) {
          res = sum2(res, reduceAxes);
        }
        return reshape(res, a.shape);
      };
      const derB = () => {
        let res = dy;
        const reduceAxes = getReductionAxes(b.shape, outShape);
        if (reduceAxes.length > 0) {
          res = sum2(res, reduceAxes);
        }
        return reshape(res, b.shape);
      };
      return {a: derA, b: derB};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/AddN_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const addNGradConfig = {
    kernelName: AddN,
    saveAllInputs: true,
    gradFunc: (dy, saved) => {
      const ders = {};
      saved.forEach((_, i) => {
        ders[i] = () => dy.clone();
      });
      return ders;
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/ArgMax_grad.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const argMaxGradConfig = {
    kernelName: ArgMax,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => zerosLike(x)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/ArgMin_grad.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const argMinGradConfig = {
    kernelName: ArgMin,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => zerosLike(x)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Asin_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const asinGradConfig = {
    kernelName: Asin,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => div(dy, sqrt(sub(scalar(1), square(cast(x, "float32")))))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Asinh_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const asinhGradConfig = {
    kernelName: Asinh,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {
        x: () => {
          const a = sqrt(add2(scalar(1), square(cast(x, "float32"))));
          return div(dy, a);
        }
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Atan2_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const atan2GradConfig = {
    kernelName: Atan2,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      const outShape = assertAndGetBroadcastShape(a.shape, b.shape);
      const derA = () => {
        const d = add2(square(a), square(b));
        let res = mul(dy, div(b, d));
        const reduceAxes = getReductionAxes(a.shape, outShape);
        if (reduceAxes.length > 0) {
          res = sum2(res, reduceAxes);
        }
        return reshape(res, a.shape);
      };
      const derB = () => {
        const d = add2(square(a), square(b));
        let res = neg(mul(dy, div(a, d)));
        const reduceAxes = getReductionAxes(b.shape, outShape);
        if (reduceAxes.length > 0) {
          res = sum2(res, reduceAxes);
        }
        return reshape(res, b.shape);
      };
      return {a: derA, b: derB};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Atan_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const atanGradConfig = {
    kernelName: Atan,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => div(dy, add2(square(cast(x, "float32")), 1))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Atanh_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const atanhGradConfig = {
    kernelName: Atanh,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => div(dy, sub(scalar(1), square(cast(x, "float32"))))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/ops/avg_pool_3d_backprop.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function avgPool3dBackprop_(dy, input, filterSize, strides, dilations = [1, 1, 1], pad8, dimRoundingMode) {
    const $dy = convertToTensor(dy, "dy", "avgPool3dBackprop");
    const $input = convertToTensor(input, "input", "avgPool3dBackprop");
    let dy5D = $dy;
    let input5D = $input;
    let reshapedTo5D = false;
    if ($input.rank === 4) {
      reshapedTo5D = true;
      dy5D = reshape($dy, [1, $dy.shape[0], $dy.shape[1], $dy.shape[2], $dy.shape[3]]);
      input5D = reshape($input, [
        1,
        $input.shape[0],
        $input.shape[1],
        $input.shape[2],
        $input.shape[3]
      ]);
    }
    assert(dy5D.rank === 5, () => `Error in avgPool3dBackprop: dy must be rank 5 but got rank ${dy5D.rank}.`);
    assert(input5D.rank === 5, () => `Error in avgPool3dBackprop: input must be rank 5 but got rank ${input5D.rank}.`);
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in avgPool3dBackprop: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in maxPool3dBackprop: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const forward = (backend2) => {
      const convInfo = computePool3DInfo(input5D.shape, filterSize, strides, dilations, pad8, dimRoundingMode);
      return backend2.avgPool3dBackprop(dy5D, input5D, convInfo);
    };
    const inputs = {dy: dy5D, input: input5D};
    const attrs = {filterSize, strides, dilations, pad: pad8, dimRoundingMode};
    const res = ENGINE.runKernelFunc(forward, inputs, null, AvgPool3DBackprop, attrs);
    if (reshapedTo5D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3], res.shape[4]]);
    }
    return res;
  }
  const avgPool3dBackprop = op({avgPool3dBackprop_});

  // node_modules/@tensorflow/tfjs-core/dist/gradients/AvgPool3D_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const avgPool3DGradConfig = {
    kernelName: AvgPool3D,
    inputsToSave: ["x"],
    gradFunc: (dy, saved, attrs) => {
      const [x] = saved;
      const {filterSize, strides, dilations, pad: pad8, dimRoundingMode} = attrs;
      const $dilations = dilations == null ? [1, 1, 1] : dilations;
      return {
        x: () => avgPool3dBackprop(dy, x, filterSize, strides, $dilations, pad8, dimRoundingMode)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/ops/avg_pool_backprop.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function avgPoolBackprop_(dy, input, filterSize, strides, pad8) {
    const $dy = convertToTensor(dy, "dy", "avgPoolBackprop");
    const $input = convertToTensor(input, "input", "avgPoolBackprop");
    assert($input.rank === $dy.rank, () => `Rank of input (${$input.rank}) does not match rank of dy (${$dy.rank})`);
    let input4D = $input;
    let dy4D = $dy;
    let reshapedTo4D = false;
    if ($input.rank === 3) {
      reshapedTo4D = true;
      input4D = reshape($input, [1, $input.shape[0], $input.shape[1], $input.shape[2]]);
      dy4D = reshape($dy, [1, $dy.shape[0], $dy.shape[1], $dy.shape[2]]);
    }
    assert(dy4D.rank === 4, () => `Error in avgPoolBackprop: dy must be rank 4 but got rank ${dy4D.rank}.`);
    assert(input4D.rank === 4, () => `Error in avgPoolBackprop: input must be rank 4 but got rank ${input4D.rank}.`);
    const forward = (backend2) => {
      const convInfo = computePool2DInfo(input4D.shape, filterSize, strides, 1, pad8);
      return backend2.avgPoolBackprop(dy4D, input4D, convInfo);
    };
    const inputs = {dy: dy4D, input: input4D};
    const attrs = {filterSize, strides, pad: pad8};
    const res = ENGINE.runKernelFunc(forward, inputs, null, AvgPoolBackprop, attrs);
    if (reshapedTo4D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3]]);
    }
    return res;
  }
  const avgPoolBackprop = op({avgPoolBackprop_});

  // node_modules/@tensorflow/tfjs-core/dist/gradients/AvgPool_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const avgPoolGradConfig = {
    kernelName: AvgPool,
    inputsToSave: ["x"],
    gradFunc: (dy, saved, attrs) => {
      const [x] = saved;
      const {filterSize, strides, pad: pad8} = attrs;
      return {
        x: () => avgPoolBackprop(dy, x, filterSize, strides, pad8)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/BatchMatMul_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const batchMatMulGradConfig = {
    kernelName: BatchMatMul,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved, attrs) => {
      const [a, b] = saved;
      const {transposeA, transposeB} = attrs;
      if (!transposeA && !transposeB) {
        return {
          a: () => matMul(dy, b, false, true),
          b: () => matMul(a, dy, true, false)
        };
      } else if (!transposeA && transposeB) {
        return {
          a: () => matMul(dy, b, false, false),
          b: () => matMul(dy, a, true, false)
        };
      } else if (transposeA && !transposeB) {
        return {
          a: () => matMul(b, dy, false, true),
          b: () => matMul(a, dy, false, false)
        };
      } else {
        return {
          a: () => matMul(b, dy, true, true),
          b: () => matMul(dy, a, true, true)
        };
      }
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/BatchToSpaceND_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const batchToSpaceNDGradConfig = {
    kernelName: BatchToSpaceND,
    gradFunc: (dy, saved, attrs) => {
      const {blockShape, crops} = attrs;
      return {x: () => spaceToBatchND(dy, blockShape, crops)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/BroadcastTo_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const broadcastToGradConfig = {
    kernelName: BroadcastTo,
    gradFunc: (dy, saved, attrs) => {
      const broadCastToAttrs = attrs;
      const inputShape = broadCastToAttrs.inputShape;
      const outputShape = broadCastToAttrs.shape;
      const reps = Array.from(outputShape);
      for (let i = inputShape.length - 1; i >= 0; i--) {
        if (inputShape[i] === outputShape[i]) {
          reps[i] = 1;
        } else if (inputShape[i] !== 1) {
          throw new Error(`broadcastTo(): [${inputShape}] cannot be broadcast to [${outputShape}].`);
        }
      }
      const axes = [];
      for (let i = 0; i < reps.length; i++) {
        if (reps[i] > 1) {
          axes.push(i);
        }
      }
      return {x: () => sum2(dy, axes, true)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Cast_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const castGradConfig = {
    kernelName: Cast,
    gradFunc: (dy) => {
      return {x: () => dy.clone()};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Ceil_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const ceilGradConfig = {
    kernelName: Ceil,
    gradFunc: (dy) => {
      return {x: () => zerosLike(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/ClipByValue_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const clipByValueGradConfig = {
    kernelName: ClipByValue,
    inputsToSave: ["x"],
    gradFunc: (dy, saved, attrs) => {
      const [x] = saved;
      const {clipValueMin, clipValueMax} = attrs;
      return {
        x: () => where(logicalAnd(greaterEqual(x, clipValueMin), lessEqual(x, clipValueMax)), dy, zerosLike(dy))
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Concat_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const concatGradConfig = {
    kernelName: Concat,
    saveAllInputs: true,
    gradFunc: (dy, saved, attrs) => {
      const shapes = saved.map((t) => t.shape);
      const {axis} = attrs;
      const $axis = parseAxisParam(axis, saved[0].shape)[0];
      const sizeSplits = shapes.map((s) => s[$axis]);
      const derTensors = split(dy, sizeSplits, $axis);
      return derTensors.map((t) => () => t);
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Conv2D_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const conv2DGradConfig = {
    kernelName: Conv2D,
    inputsToSave: ["x", "filter"],
    gradFunc: (dy, saved, attrs) => {
      const [x4D, $filter] = saved;
      const {dilations, strides, pad: pad8, dataFormat} = attrs;
      assert(tupleValuesAreOne(dilations), () => `Error in gradient of conv2D: dilation rates greater than 1 are not yet supported in gradients. Got dilations '${dilations}'`);
      return {
        x: () => conv2DBackpropInput(x4D.shape, dy, $filter, strides, pad8, dataFormat),
        filter: () => conv2DBackpropFilter(x4D, dy, $filter.shape, strides, pad8, dataFormat)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Conv2DBackpropInput_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const conv2DBackpropInputGradConfig = {
    kernelName: Conv2DBackpropInput,
    inputsToSave: ["dy", "filter"],
    gradFunc: (ddx, saved, attrs) => {
      const [dy, filter] = saved;
      const {strides, pad: pad8, dataFormat, dimRoundingMode} = attrs;
      return {
        dy: () => conv2d(ddx, filter, strides, pad8, dataFormat, 1, dimRoundingMode),
        filter: () => conv2DBackpropFilter(ddx, dy, filter.shape, strides, pad8, dataFormat, dimRoundingMode)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/ops/conv3d_backprop_filter.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function conv3DBackpropFilter_(x, dy, filterShape, strides, pad8) {
    let x5D = x;
    if (x.rank === 4) {
      x5D = reshape(x, [1, x.shape[0], x.shape[1], x.shape[2], x.shape[3]]);
    }
    let dy5D = dy;
    if (dy5D.rank === 4) {
      dy5D = reshape(dy, [1, dy.shape[0], dy.shape[1], dy.shape[2], dy.shape[3]]);
    }
    assert(x5D.rank === 5, () => `Error in conv3dDerFilter: input must be rank 5, but got shape ${x5D.shape}.`);
    assert(dy5D.rank === 5, () => `Error in conv3dDerFilter: dy must be rank 5, but got shape ${dy5D.shape}.`);
    assert(filterShape.length === 5, () => `Error in conv3dDerFilter: filterShape must be length 5, but got ${filterShape}.`);
    assert(x5D.shape[4] === filterShape[3], () => `Error in conv3dDerFilter: depth of input ${x5D.shape[4]}) must match input depth in filter (${filterShape[3]}.`);
    assert(dy5D.shape[4] === filterShape[4], () => `Error in conv3dDerFilter: depth of dy (${dy5D.shape[4]}) must match output depth for filter (${filterShape[4]}).`);
    const forward = (backend2) => {
      const dilations = 1;
      const convInfo = computeConv3DInfo(x5D.shape, filterShape, strides, dilations, pad8);
      return backend2.conv3dDerFilter(x5D, dy5D, convInfo);
    };
    const inputs = {x: x5D, y: dy5D};
    const attrs = {strides, pad: pad8};
    return ENGINE.runKernelFunc(forward, inputs, null, Conv3DBackpropFilterV2, attrs);
  }
  const conv3DBackpropFilter = op({conv3DBackpropFilter_});

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Conv3D_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const conv3DGradConfig = {
    kernelName: Conv3D,
    inputsToSave: ["x", "filter"],
    gradFunc: (dy, saved, attrs) => {
      const {dilations, strides, pad: pad8} = attrs;
      assert(tupleValuesAreOne(dilations), () => `Error in gradient of conv3D: dilation rates greater than 1 are not yet supported in gradients. Got dilations '${dilations}'`);
      const [x5D, $filter] = saved;
      return {
        x: () => conv3DBackpropInput(x5D.shape, dy, $filter, strides, pad8),
        filter: () => conv3DBackpropFilter(x5D, dy, $filter.shape, strides, pad8)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Cos_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const cosGradConfig = {
    kernelName: Cos,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => mul(neg(sin(cast(x, "float32"))), dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Cosh_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const coshGradConfig = {
    kernelName: Cosh,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => mul(sinh(cast(x, "float32")), dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Cumsum_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const cumsumGradConfig = {
    kernelName: Cumsum,
    inputsToSave: ["x"],
    gradFunc: (dy, saved, attrs) => {
      const [x] = saved;
      const {axis, exclusive, reverse: reverse9} = attrs;
      return {
        x: () => {
          const permutation = getAxesPermutation([axis], x.rank);
          let out = cumsum(dy, axis, exclusive, !reverse9);
          if (permutation != null) {
            out = transpose(out, permutation);
          }
          return out;
        }
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/DepthwiseConv2dNative_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const depthwiseConv2dNativeGradConfig = {
    kernelName: DepthwiseConv2dNative,
    inputsToSave: ["x", "filter"],
    gradFunc: (dy, saved, attrs) => {
      const {dilations, strides, pad: pad8, dimRoundingMode} = attrs;
      const $dilations = dilations == null ? [1, 1] : dilations;
      assert(tupleValuesAreOne($dilations), () => `Error in gradient of depthwiseConv2dNative: dilation rates greater than 1 are not yet supported. Got dilations '${$dilations}'`);
      const [x, filter] = saved;
      assert(x.rank === 4, () => `Error in gradient of depthwiseConv2dNative: input must be rank 4, but got rank ${x.rank}.`);
      assert(filter.rank === 4, () => `Error in gradient of depthwiseConv2dNative: filter must be rank 4, but got rank ${filter.rank}.`);
      assert(x.shape[3] === filter.shape[2], () => `Error in gradient of depthwiseConv2d: number of input channels (${x.shape[3]}) must match the inChannels dimension in filter ${filter.shape[2]}.`);
      assert(eitherStridesOrDilationsAreOne(strides, $dilations), () => `Error in gradient of depthwiseConv2d: Either strides or dilations must be  1. Got strides ${strides} and dilations '${$dilations}'.`);
      if (dimRoundingMode != null) {
        assert(isInt(pad8), () => `Error in depthwiseConv2d: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
      }
      const convInfo = computeConv2DInfo(x.shape, filter.shape, strides, $dilations, pad8, dimRoundingMode, true);
      return {
        x: () => depthwiseConv2dNativeBackpropInput(x.shape, dy, filter, convInfo),
        filter: () => depthwiseConv2dNativeBackpropFilter(x, dy, filter.shape, convInfo)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Dilation2D_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const dilation2dGradConfig = {
    kernelName: Dilation2D,
    inputsToSave: ["x", "filter"],
    gradFunc: (dy, saved, attrs) => {
      const [x, filter] = saved;
      const inputInputs = {x, filter, dy};
      const filterInputs = {x, filter, dy};
      return {
        x: () => ENGINE.runKernel(Dilation2DBackpropInput, inputInputs, attrs),
        filter: () => ENGINE.runKernel(Dilation2DBackpropFilter, filterInputs, attrs)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Div_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const divGradConfig = {
    kernelName: Div,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      const outShape = assertAndGetBroadcastShape(a.shape, b.shape);
      const derA = () => {
        const res = div(dy, cast(b, "float32"));
        const reduceAxes = getReductionAxes(a.shape, outShape);
        if (reduceAxes.length > 0) {
          return reshape(sum2(res, reduceAxes), a.shape);
        }
        return res;
      };
      const derB = () => {
        let res = mul(dy, cast(a, "float32"));
        const reduceAxes = getReductionAxes(b.shape, outShape);
        if (reduceAxes.length > 0) {
          res = reshape(sum2(res, reduceAxes), b.shape);
        }
        const tmp = square(b);
        return neg(div(res, cast(tmp, "float32")));
      };
      return {a: derA, b: derB};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Elu_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const eluGradConfig = {
    kernelName: Elu,
    outputsToSave: [true],
    gradFunc: (dy, saved) => {
      const [y] = saved;
      const backPropKernelFunc = (backend2) => {
        return backend2.eluDer(dy, y);
      };
      const inputs = {dy, y};
      return {
        x: () => ENGINE.runKernelFunc(backPropKernelFunc, inputs, null, EluGrad)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Erf_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const erfGradConfig = {
    kernelName: Erf,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      const a = mul(exp(neg(square(x))), 2 / Math.sqrt(Math.PI));
      return {x: () => mul(dy, a)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Exp_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const expGradConfig = {
    kernelName: Exp,
    outputsToSave: [true],
    gradFunc: (dy, saved) => {
      const [y] = saved;
      return {x: () => mul(dy, y)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Expm1_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const expm1GradConfig = {
    kernelName: Expm1,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => mul(dy, exp(x))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Floor_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const floorGradConfig = {
    kernelName: Floor,
    gradFunc: (dy) => {
      return {x: () => zerosLike(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/FloorDiv_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const floorDivGradConfig = {
    kernelName: FloorDiv,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      const outShape = assertAndGetBroadcastShape(a.shape, b.shape);
      const derA = () => {
        const res = div(dy, cast(b, "float32"));
        const reduceAxes = getReductionAxes(a.shape, outShape);
        if (reduceAxes.length > 0) {
          return reshape(sum2(res, reduceAxes), a.shape);
        }
        return res;
      };
      const derB = () => {
        let res = mul(dy, cast(a, "float32"));
        const reduceAxes = getReductionAxes(b.shape, outShape);
        if (reduceAxes.length > 0) {
          res = reshape(sum2(res, reduceAxes), b.shape);
        }
        const tmp = square(b);
        return neg(div(res, cast(tmp, "float32")));
      };
      return {a: derA, b: derB};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/FusedBatchNorm_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const fusedBatchNormGradConfig = {
    kernelName: FusedBatchNorm,
    inputsToSave: ["x", "mean", "variance", "scale"],
    gradFunc: (dy, saved, attrs) => {
      const {varianceEpsilon} = attrs;
      const [x, mean5, variance, scale2] = saved;
      const scaleValue = scale2 == null ? scalar(1) : scale2;
      const reductionAxes = getReductionAxes(mean5.shape, x.shape);
      const tileShape = [];
      if (mean5.rank === 1) {
        for (let i = 0; i < x.shape.length - 1; ++i) {
          tileShape.push(x.shape[i]);
        }
        tileShape.push(1);
      }
      const xMinusMean = sub(x, mean5);
      const dyTimesScaleValue = mul(dy, scaleValue);
      const oneOverSqrtVariance = rsqrt(add2(variance, scalar(varianceEpsilon)));
      const minusHalfRCube = mul(mul(mul(oneOverSqrtVariance, oneOverSqrtVariance), oneOverSqrtVariance), scalar(-0.5));
      const derX = () => {
        if (mean5.rank === 1) {
          return reshape(mul(mul(dy, tile(reshape(oneOverSqrtVariance, [1, 1, 1, mean5.shape[0]]), tileShape)), scaleValue), x.shape);
        } else {
          return reshape(mul(mul(dy, oneOverSqrtVariance), scaleValue), x.shape);
        }
      };
      const derMean = () => {
        let meanDer = mul(mul(oneOverSqrtVariance, scalar(-1)), dyTimesScaleValue);
        if (mean5.rank === 1) {
          meanDer = sum2(meanDer, reductionAxes);
        }
        return reshape(meanDer, mean5.shape);
      };
      const derVariance = () => {
        let varianceDer = mul(mul(minusHalfRCube, xMinusMean), dyTimesScaleValue);
        if (mean5.rank === 1) {
          varianceDer = sum2(varianceDer, reductionAxes);
        }
        return reshape(varianceDer, mean5.shape);
      };
      const derScale = () => {
        const xMinusMean2TimesRsqrt = mul(xMinusMean, oneOverSqrtVariance);
        let scaleDer = mul(dy, xMinusMean2TimesRsqrt);
        if (mean5.rank === 1) {
          scaleDer = sum2(scaleDer, reductionAxes);
        }
        return reshape(scaleDer, mean5.shape);
      };
      const derOffset = () => {
        let offsetDer = dy;
        if (mean5.rank === 1) {
          offsetDer = sum2(offsetDer, reductionAxes);
        }
        return reshape(offsetDer, mean5.shape);
      };
      return {
        x: derX,
        mean: derMean,
        variance: derVariance,
        scale: derScale,
        offset: derOffset
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/GatherV2_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const gatherGradConfig = {
    kernelName: GatherV2,
    inputsToSave: ["x", "indices"],
    gradFunc: (dy, saved, attrs) => {
      const [x, indices] = saved;
      const {axis} = attrs;
      const parsedAxis = parseAxisParam(axis, x.shape)[0];
      const derX = () => {
        const paramsShape = x.shape;
        const indicesSize = indices.size;
        const outerShape = paramsShape.slice(0, parsedAxis);
        const outerDims = outerShape.length;
        const innerShape = paramsShape.slice(axis, paramsShape.length).slice(1);
        const innerDims = innerShape.length;
        const outerAxesIndices = arrayRange(0, outerDims);
        const innerAxesIndices = arrayRange(outerDims + 1, outerDims + 1 + innerDims);
        const valuesShape = arrayConcat([outerShape, [indicesSize], innerShape]);
        const values = reshape(dy, valuesShape);
        const reshapedIndices = reshape(indices, [indicesSize]);
        const transposeDims = arrayConcat([[outerDims], outerAxesIndices, innerAxesIndices]);
        const valuesTranspose = transpose(values, transposeDims);
        let paramsGrad = unsortedSegmentSum(valuesTranspose, reshapedIndices, x.shape[parsedAxis]);
        const invertTransposeDims = getUndoAxesPermutation(transposeDims);
        paramsGrad = transpose(paramsGrad, invertTransposeDims);
        return paramsGrad;
      };
      return {x: derX, indices: () => indices};
    }
  };
  function arrayRange(start, stop) {
    const result = [];
    for (let i = start; i < stop; ++i) {
      result.push(i);
    }
    return result;
  }
  function arrayConcat(arrays) {
    const result = [];
    for (let i = 0; i < arrays.length; ++i) {
      for (let j = 0; j < arrays[i].length; ++j) {
        result.push(arrays[i][j]);
      }
    }
    return result;
  }

  // node_modules/@tensorflow/tfjs-core/dist/gradients/GreaterEqual_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const greaterEqualGradConfig = {
    kernelName: GreaterEqual,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      return {a: () => zerosLike(a), b: () => zerosLike(b)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Identity_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const identityGradConfig = {
    kernelName: Identity,
    gradFunc: (dy) => {
      return {x: () => cast(dy, "float32")};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/IsFinite_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const isFiniteGradConfig = {
    kernelName: IsFinite,
    gradFunc: (dy) => {
      return {x: () => zerosLike(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/IsInf_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const isInfGradConfig = {
    kernelName: IsInf,
    gradFunc: (dy) => {
      return {x: () => zerosLike(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/IsNan_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const isNanGradConfig = {
    kernelName: IsNan,
    gradFunc: (dy) => {
      return {x: () => zerosLike(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Log1p_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const log1pGradConfig = {
    kernelName: Log1p,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => div(dy, add2(x, 1))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Log_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const logGradConfig = {
    kernelName: Log,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => div(dy, cast(x, "float32"))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/LogSoftmax_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const logSoftmaxGradConfig = {
    kernelName: LogSoftmax,
    inputsToSave: [],
    outputsToSave: [true],
    gradFunc: (dy, saved, attrs) => {
      const [value] = saved;
      const {axis} = attrs;
      return {
        logits: () => {
          const keepDims = true;
          const softmax3 = exp(value);
          return sub(dy, mul(sum2(dy, axis, keepDims), softmax3));
        }
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/ops/local_response_normalization_backprop.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function localResponseNormalizationBackprop_(x, y, dy, depthRadius = 5, bias = 1, alpha = 1, beta = 0.5) {
    const forward = (backend2) => backend2.LRNGrad(dy, x, y, depthRadius, bias, alpha, beta);
    const inputs = {x, y, dy};
    const attrs = {depthRadius, bias, alpha, beta};
    return ENGINE.runKernelFunc(forward, inputs, null, LRNBackprop, attrs);
  }
  const localResponseNormalizationBackprop = op({localResponseNormalizationBackprop_});

  // node_modules/@tensorflow/tfjs-core/dist/gradients/LRN_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const lrnGradConfig = {
    kernelName: LRN,
    inputsToSave: ["x"],
    outputsToSave: [true],
    gradFunc: (dy, saved, attrs) => {
      const [x, y] = saved;
      const {depthRadius, bias, alpha, beta} = attrs;
      return {
        x: () => localResponseNormalizationBackprop(x, y, dy, depthRadius, bias, alpha, beta)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/min_max_grad_util.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function gradForMinAndMax(dy, y, xOrig, origAxes, permutedAxes) {
    if (y.rank < xOrig.rank) {
      y = reshape(y, expandShapeToKeepDim(y.shape, origAxes));
    }
    if (dy.rank < xOrig.rank) {
      dy = reshape(dy, expandShapeToKeepDim(dy.shape, origAxes));
    }
    return {
      x: () => {
        const dx = mul(dy, cast(equal(xOrig, y), dy.dtype));
        return permutedAxes == null ? dx : transpose(dx, permutedAxes);
      }
    };
  }

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Max_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const maxGradConfig = {
    kernelName: Max,
    inputsToSave: ["x"],
    outputsToSave: [true],
    gradFunc: (dy, saved, attrs) => {
      const maxAttrs = attrs;
      const {reductionIndices} = maxAttrs;
      const [x, y] = saved;
      const origAxes = parseAxisParam(reductionIndices, x.shape);
      const permutedAxes = getAxesPermutation(origAxes, x.rank);
      const maxGrad = gradForMinAndMax(dy, y, x, origAxes, permutedAxes);
      return {
        x: () => {
          let out = maxGrad["x"]();
          if (permutedAxes != null) {
            out = transpose(out);
          }
          return out;
        }
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Maximum_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const maximumGradConfig = {
    kernelName: Maximum,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      const derA = () => mul(dy, cast(greaterEqual(a, b), "float32"));
      const derB = () => mul(dy, cast(less(a, b), "float32"));
      return {a: derA, b: derB};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/ops/max_pool_3d_backprop.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function maxPool3dBackprop_(dy, input, output, filterSize, strides, dilations = [1, 1, 1], pad8, dimRoundingMode) {
    const $dy = convertToTensor(dy, "dy", "maxPool3dBackprop");
    const $input = convertToTensor(input, "input", "maxPool3dBackprop");
    const $output = convertToTensor(output, "output", "maxPool3dBackprop");
    let dy5D = $dy;
    let input5D = $input;
    let output5D = $output;
    let reshapedTo5D = false;
    if ($input.rank === 4) {
      reshapedTo5D = true;
      dy5D = reshape($dy, [1, $dy.shape[0], $dy.shape[1], $dy.shape[2], $dy.shape[3]]);
      input5D = reshape($input, [
        1,
        $input.shape[0],
        $input.shape[1],
        $input.shape[2],
        $input.shape[3]
      ]);
      output5D = reshape($output, [
        1,
        $output.shape[0],
        $output.shape[1],
        $output.shape[2],
        $output.shape[3]
      ]);
    }
    assert(dy5D.rank === 5, () => `Error in maxPool3dBackprop: dy must be rank 5 but got rank ${dy5D.rank}.`);
    assert(input5D.rank === 5, () => `Error in maxPool3dBackprop: input must be rank 5 but got rank ${input5D.rank}.`);
    assert(output5D.rank === 5, () => `Error in maxPool3dBackprop: output must be rank 5 but got rank ${output5D.rank}.`);
    assert(eitherStridesOrDilationsAreOne(strides, dilations), () => `Error in maxPool3dBackprop: Either strides or dilations must be 1. Got strides ${strides} and dilations '${dilations}'`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in maxPool3dBackprop: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const forward = (backend2) => {
      const convInfo = computePool3DInfo(input5D.shape, filterSize, strides, dilations, pad8, dimRoundingMode);
      return backend2.maxPool3dBackprop(dy5D, input5D, output5D, convInfo);
    };
    const inputs = {dy: dy5D, input: input5D, output: output5D};
    const attrs = {filterSize, strides, dilations, pad: pad8, dimRoundingMode};
    const res = ENGINE.runKernelFunc(forward, inputs, null, MaxPool3DBackprop, attrs);
    if (reshapedTo5D) {
      return reshape(res, [res.shape[1], res.shape[2], res.shape[3], res.shape[4]]);
    }
    return res;
  }
  const maxPool3dBackprop = op({maxPool3dBackprop_});

  // node_modules/@tensorflow/tfjs-core/dist/gradients/MaxPool3D_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const maxPool3DGradConfig = {
    kernelName: MaxPool3D,
    inputsToSave: ["x"],
    outputsToSave: [true],
    gradFunc: (dy, saved, attrs) => {
      const [x, y] = saved;
      const {filterSize, strides, dilations, pad: pad8, dimRoundingMode} = attrs;
      const $dilations = dilations == null ? [1, 1, 1] : dilations;
      return {
        x: () => maxPool3dBackprop(dy, x, y, filterSize, strides, $dilations, pad8, dimRoundingMode)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/ops/max_pool_backprop.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  function maxPoolBackprop_(dy, input, output, filterSize, strides, pad8, dimRoundingMode) {
    const $dy = convertToTensor(dy, "dy", "maxPoolBackprop");
    const $input = convertToTensor(input, "input", "maxPoolBackprop");
    const $output = convertToTensor(output, "output", "maxPoolBackprop");
    assert($input.rank === $dy.rank, () => `Rank of input (${$input.rank}) does not match rank of dy (${$dy.rank})`);
    assert($dy.rank === 4, () => `Error in maxPoolBackprop: dy must be rank 4 but got rank ${$dy.rank}.`);
    assert($input.rank === 4, () => `Error in maxPoolBackprop: input must be rank 4 but got rank ${$input.rank}.`);
    if (dimRoundingMode != null) {
      assert(isInt(pad8), () => `Error in maxPoolBackprop: pad must be an integer when using, dimRoundingMode ${dimRoundingMode} but got pad ${pad8}.`);
    }
    const forward = (backend2) => {
      const convInfo = computePool2DInfo($input.shape, filterSize, strides, 1, pad8, dimRoundingMode);
      return backend2.maxPoolBackprop($dy, $input, $output, convInfo);
    };
    const inputs = {dy: $dy, input: $input, output: $output};
    const attrs = {filterSize, strides, pad: pad8, dimRoundingMode};
    return ENGINE.runKernelFunc(forward, inputs, null, MaxPoolBackprop, attrs);
  }
  const maxPoolBackprop = op({maxPoolBackprop_});

  // node_modules/@tensorflow/tfjs-core/dist/gradients/MaxPool_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const maxPoolGradConfig = {
    kernelName: MaxPool,
    inputsToSave: ["x"],
    outputsToSave: [true],
    gradFunc: (dy, saved, attrs) => {
      const [x, y] = saved;
      const {filterSize, strides, pad: pad8} = attrs;
      return {
        x: () => maxPoolBackprop(dy, x, y, filterSize, strides, pad8)
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Min_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const minGradConfig = {
    kernelName: Min,
    inputsToSave: ["x"],
    outputsToSave: [true],
    gradFunc: (dy, saved, attrs) => {
      const minAttrs = attrs;
      const {axis} = minAttrs;
      const [x, y] = saved;
      const origAxes = parseAxisParam(axis, x.shape);
      const permutedAxes = getAxesPermutation(origAxes, x.rank);
      const minGrad = gradForMinAndMax(dy, y, x, origAxes, permutedAxes);
      return {
        x: () => {
          let out = minGrad["x"]();
          if (permutedAxes != null) {
            out = transpose(out);
          }
          return out;
        }
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Minimum_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const minimumGradConfig = {
    kernelName: Minimum,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      const derA = () => mul(dy, cast(lessEqual(a, b), "float32"));
      const derB = () => mul(dy, cast(greater(a, b), "float32"));
      return {a: derA, b: derB};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Mod_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const modGradConfig = {
    kernelName: Mod,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      const outShape = assertAndGetBroadcastShape(a.shape, b.shape);
      const derA = () => {
        const reduceAxes = getReductionAxes(a.shape, outShape);
        if (reduceAxes.length > 0) {
          return reshape(sum2(dy, reduceAxes), a.shape);
        }
        return dy;
      };
      const derB = () => {
        const res = mul(dy, neg(floor(div(a, b))));
        const reduceAxes = getReductionAxes(b.shape, outShape);
        if (reduceAxes.length > 0) {
          return reshape(sum2(res, reduceAxes), b.shape);
        }
        return res;
      };
      return {a: derA, b: derB};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Multiply_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const multiplyGradConfig = {
    kernelName: Multiply,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      const outShape = assertAndGetBroadcastShape(a.shape, b.shape);
      const derA = () => {
        const res = mul(dy, cast(b, "float32"));
        const reduceAxes = getReductionAxes(a.shape, outShape);
        if (reduceAxes.length > 0) {
          return reshape(sum2(res, reduceAxes), a.shape);
        }
        return res;
      };
      const derB = () => {
        const res = mul(dy, cast(a, "float32"));
        const reduceAxes = getReductionAxes(b.shape, outShape);
        if (reduceAxes.length > 0) {
          return reshape(sum2(res, reduceAxes), b.shape);
        }
        return res;
      };
      return {a: derA, b: derB};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Negate_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const negateGradConfig = {
    kernelName: Negate,
    gradFunc: (dy) => {
      return {x: () => neg(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/OneHot_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const oneHotGradConfig = {
    kernelName: OneHot,
    inputsToSave: ["indices"],
    gradFunc: (dy, saved) => {
      const indices = saved[0];
      return {indices: () => zeros(indices.shape, "float32")};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/OnesLike_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const onesLikeGradConfig = {
    kernelName: OnesLike,
    gradFunc: (dy) => {
      return {x: () => zerosLike(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/PadV2_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const padV2GradConfig = {
    kernelName: PadV2,
    inputsToSave: ["x"],
    gradFunc: (dy, saved, attrs) => {
      const x = saved[0];
      const {paddings} = attrs;
      const begin = paddings.map((p) => p[0]);
      return {x: () => slice(dy, begin, x.shape)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Pow_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const powGradConfig = {
    kernelName: Pow,
    inputsToSave: ["a", "b"],
    outputsToSave: [true],
    gradFunc: (dy, saved) => {
      const [a, b, y] = saved;
      const base = a;
      const exp11 = b;
      const outShape = assertAndGetBroadcastShape(base.shape, exp11.shape);
      const derBase = () => {
        const expFloat = cast(exp11, "float32");
        let res = mul(dy, mul(expFloat, pow(base, sub(expFloat, scalar(1)))));
        const reduceAxes = getReductionAxes(base.shape, outShape);
        if (reduceAxes.length > 0) {
          res = sum2(res, reduceAxes);
        }
        return reshape(res, base.shape);
      };
      const derExp = () => {
        const condition = greater(base, 0);
        const logBase = where(condition, log(base), zerosLike(base));
        let res = mul(dy, mul(y, logBase));
        const reduceAxes = getReductionAxes(exp11.shape, outShape);
        if (reduceAxes.length > 0) {
          res = sum2(res, reduceAxes);
        }
        return reshape(res, exp11.shape);
      };
      return {a: derBase, b: derExp};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Prelu_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const preluGradConfig = {
    kernelName: Prelu,
    inputsToSave: ["x", "alpha"],
    gradFunc: (dy, saved) => {
      const [x, alpha] = saved;
      const mask = greater(x, 0);
      return {
        x: () => where(mask, dy, mul(dy, alpha)),
        alpha: () => {
          let res = where(mask, zerosLike(dy), mul(dy, x));
          const reduceAxes = getReductionAxes(alpha.shape, dy.shape);
          if (reduceAxes.length > 0) {
            res = sum2(res, reduceAxes);
          }
          return reshape(res, alpha.shape);
        }
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Reciprocal_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const reciprocalGradConfig = {
    kernelName: Reciprocal,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => div(dy, neg(square(x)))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Relu6_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const relu6GradConfig = {
    kernelName: Relu6,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      const mask = mul(lessEqual(x, 6), step(x));
      return {x: () => mul(dy, cast(mask, "float32"))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Relu_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const reluGradConfig = {
    kernelName: Relu,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => mul(dy, cast(step(x), "float32"))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Reshape_grad.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const reshapeGradConfig = {
    kernelName: Reshape,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => reshape(dy, x.shape)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/ResizeBilinear_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const resizeBilinearGradConfig = {
    kernelName: ResizeBilinear,
    inputsToSave: ["images"],
    gradFunc: (dy, saved, attrs) => {
      const [images] = saved;
      const backPropKernelFunc = (backend2) => {
        const {alignCorners} = attrs;
        return backend2.resizeBilinearBackprop(dy, images, alignCorners);
      };
      const inputs = {images};
      const imagesDer = () => ENGINE.runKernelFunc(backPropKernelFunc, inputs, null, ResizeBilinearGrad, attrs);
      return {images: imagesDer};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/ResizeNearestNeighbor_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const resizeNearestNeighborGradConfig = {
    kernelName: ResizeNearestNeighbor,
    inputsToSave: ["images"],
    gradFunc: (dy, saved, attrs) => {
      const [images] = saved;
      const backPropKernelFunc = (backend2) => {
        const {alignCorners} = attrs;
        return backend2.resizeNearestNeighborBackprop(dy, images, alignCorners);
      };
      const inputs = {images};
      const imagesDer = () => ENGINE.runKernelFunc(backPropKernelFunc, inputs, null, ResizeNearestNeighborGrad, attrs);
      return {images: imagesDer};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Reverse_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const reverseGradConfig = {
    kernelName: Reverse,
    gradFunc: (dy, saved, attrs) => {
      const {dims} = attrs;
      const axes = parseAxisParam(dims, dy.shape);
      return {x: () => reverse(dy, axes)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Round_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const roundGradConfig = {
    kernelName: Round,
    gradFunc: (dy) => {
      return {x: () => zerosLike(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Rsqrt_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const rsqrtGradConfig = {
    kernelName: Rsqrt,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => neg(div(dy, mul(pow(x, 1.5), 2)))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/SelectV2_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const selectV2PoolGradConfig = {
    kernelName: SelectV2,
    inputsToSave: ["condition"],
    gradFunc: (dy, saved) => {
      const [condition] = saved;
      return {
        condition: () => cast(zerosLike(condition), "float32"),
        t: () => mul(dy, cast(condition, dy.dtype)),
        e: () => mul(dy, cast(logicalNot(condition), dy.dtype))
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Selu_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const seluGradConfig = {
    kernelName: Selu,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {
        x: () => {
          const mask = greater(x, scalar(0));
          const scaleAlpha = scalar(SELU_SCALEALPHA);
          const scale2 = scalar(SELU_SCALE);
          const greaterThanZeroDer = mul(dy, scale2);
          const lessEqualZeroDer = mul(mul(dy, scaleAlpha), exp(cast(x, "float32")));
          return where(mask, greaterThanZeroDer, lessEqualZeroDer);
        }
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Sigmoid_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const sigmoidGradConfig = {
    kernelName: Sigmoid,
    outputsToSave: [true],
    gradFunc: (dy, saved) => {
      const [y] = saved;
      return {x: () => mul(dy, mul(y, sub(scalar(1), y)))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Sign_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const signGradConfig = {
    kernelName: Sign,
    gradFunc: (dy) => {
      return {x: () => zerosLike(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Sin_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const sinGradConfig = {
    kernelName: Sin,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => mul(cos(cast(x, "float32")), dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Sinh_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const sinhGradConfig = {
    kernelName: Sinh,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => mul(cosh(cast(x, "float32")), dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Slice_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const sliceGradConfig = {
    kernelName: Slice,
    inputsToSave: ["x"],
    gradFunc: (dy, saved, attrs) => {
      const [x] = saved;
      const {begin, size} = attrs;
      const inputShape = x.shape;
      const [begin_, size_] = parseSliceParams(x, begin, size);
      const paddings = [];
      for (let i = 0; i < dy.rank; i++) {
        paddings.push([begin_[i], inputShape[i] - begin_[i] - size_[i]]);
      }
      return {x: () => pad(dy, paddings)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Softmax_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const softmaxGradConfig = {
    kernelName: Softmax,
    outputsToSave: [true],
    gradFunc: (dy, saved, attrs) => {
      const [y] = saved;
      const {dim} = attrs;
      const keepDims = true;
      const dyTimesY = mul(dy, y);
      return {
        logits: () => sub(dyTimesY, mul(sum2(dyTimesY, [dim], keepDims), y))
      };
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Softplus_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const softplusGradConfig = {
    kernelName: Softplus,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => mul(dy, sigmoid(x))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/SpaceToBatchND_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const spaceToBatchNDGradConfig = {
    kernelName: SpaceToBatchND,
    gradFunc: (dy, saved, attrs) => {
      const {blockShape, paddings} = attrs;
      return {x: () => batchToSpaceND(dy, blockShape, paddings)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/SplitV_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const splitVGradConfig = {
    kernelName: SplitV,
    gradFunc: (dy, saved, attrs) => {
      const {axis} = attrs;
      return {x: () => concat(dy, axis)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Sqrt_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const sqrtGradConfig = {
    kernelName: Sqrt,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => div(dy, mul(sqrt(cast(x, "float32")), 2))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Square_grad.js
  /**
   * @license
   * Copyright 2019 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const squareGradConfig = {
    kernelName: Square,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => mul(dy, mul(cast(x, "float32"), 2))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/SquaredDifference_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const squaredDifferenceGradConfig = {
    kernelName: SquaredDifference,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      const two = scalar(2);
      const derA = () => mul(dy, mul(two, sub(a, b)));
      const derB = () => mul(dy, mul(two, sub(b, a)));
      return {a: derA, b: derB};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Step_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const stepGradConfig = {
    kernelName: Step,
    gradFunc: (dy) => {
      return {x: () => zerosLike(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Sub_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const subGradConfig = {
    kernelName: Sub,
    inputsToSave: ["a", "b"],
    gradFunc: (dy, saved) => {
      const [a, b] = saved;
      const outShape = assertAndGetBroadcastShape(a.shape, b.shape);
      const derA = () => {
        let res = dy;
        const reduceAxes = getReductionAxes(a.shape, outShape);
        if (reduceAxes.length > 0) {
          res = sum2(res, reduceAxes);
        }
        return reshape(res, a.shape);
      };
      const derB = () => {
        let res = dy;
        const reduceAxes = getReductionAxes(b.shape, outShape);
        if (reduceAxes.length > 0) {
          res = sum2(res, reduceAxes);
        }
        return reshape(neg(res), b.shape);
      };
      return {a: derA, b: derB};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Sum_grad.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const sumGradConfig = {
    kernelName: Sum,
    inputsToSave: ["x"],
    gradFunc: (dy, saved, attrs) => {
      const [x] = saved;
      const expandedDyShape = x.shape.slice();
      const {axis} = attrs;
      const axes = parseAxisParam(axis, x.shape);
      axes.forEach((axis2) => {
        expandedDyShape[axis2] = 1;
      });
      const expandedDy = reshape(dy, expandedDyShape);
      const derX = mul(expandedDy, ones2(x.shape, "float32"));
      return {x: () => derX};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Tan_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const tanGradConfig = {
    kernelName: Tan,
    inputsToSave: ["x"],
    gradFunc: (dy, saved) => {
      const [x] = saved;
      return {x: () => div(dy, square(cos(x)))};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Tanh_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const tanhGradConfig = {
    kernelName: Tanh,
    outputsToSave: [true],
    gradFunc: (dy, saved) => {
      const [y] = saved;
      return {x: () => mul(sub(scalar(1), square(y)), dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Tile_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const tileGradConfig = {
    kernelName: Tile,
    inputsToSave: ["x"],
    gradFunc: (dy, saved, attrs) => {
      const [x] = saved;
      const {reps} = attrs;
      const derX = () => {
        let xGrad = zerosLike(x);
        if (x.rank === 1) {
          for (let i = 0; i < reps[0]; ++i) {
            xGrad = add2(xGrad, slice(dy, [i * x.shape[0]], [x.shape[0]]));
          }
        } else if (x.rank === 2) {
          for (let i = 0; i < reps[0]; ++i) {
            for (let j = 0; j < reps[1]; ++j) {
              xGrad = add2(xGrad, slice(dy, [i * x.shape[0], j * x.shape[1]], [
                x.shape[0],
                x.shape[1]
              ]));
            }
          }
        } else if (x.rank === 3) {
          for (let i = 0; i < reps[0]; ++i) {
            for (let j = 0; j < reps[1]; ++j) {
              for (let k = 0; k < reps[2]; ++k) {
                xGrad = add2(xGrad, slice(dy, [i * x.shape[0], j * x.shape[1], k * x.shape[2]], [x.shape[0], x.shape[1], x.shape[2]]));
              }
            }
          }
        } else if (x.rank === 4) {
          for (let i = 0; i < reps[0]; ++i) {
            for (let j = 0; j < reps[1]; ++j) {
              for (let k = 0; k < reps[2]; ++k) {
                for (let l = 0; l < reps[3]; ++l) {
                  xGrad = add2(xGrad, slice(dy, [
                    i * x.shape[0],
                    j * x.shape[1],
                    k * x.shape[2],
                    l * x.shape[3]
                  ], [x.shape[0], x.shape[1], x.shape[2], x.shape[3]]));
                }
              }
            }
          }
        } else {
          throw new Error(`Gradient for tile operation is not implemented for rank-${x.rank} tensors yet.`);
        }
        return xGrad;
      };
      return {x: derX};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Transpose_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const transposeGradConfig = {
    kernelName: Transpose,
    gradFunc: (dy, saved, attrs) => {
      const transposeAttrs = attrs;
      const {perm} = transposeAttrs;
      const undoPerm = getUndoAxesPermutation(perm);
      return {x: () => transpose(dy, undoPerm)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/Unpack_grad.js
  /**
   * @license
   * Copyright 2020 Google Inc. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const unpackGradConfig = {
    kernelName: Unpack,
    gradFunc: (dy, saved, attrs) => {
      const unpackAttrs = attrs;
      const {axis} = unpackAttrs;
      return {value: () => stack(dy, axis)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/gradients/UnsortedSegmentSum_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const unsortedSegmentSumGradConfig = {
    kernelName: UnsortedSegmentSum,
    inputsToSave: ["segmentIds"],
    gradFunc: (dy, saved) => {
      const [segmentIds] = saved;
      const derX = () => {
        return gatherDropNegatives(dy, segmentIds);
      };
      return {x: derX};
    }
  };
  function gatherDropNegatives(x, indices) {
    const zeroClippedIndices = maximum(indices, zerosLike(indices));
    const gathered = gather(x, zeroClippedIndices);
    let isPositive = greaterEqual(indices, scalar(0, "int32"));
    const numIters = gathered.rank - isPositive.rank;
    for (let i = 0; i < numIters; ++i) {
      isPositive = expandDims(isPositive, i + 1);
    }
    isPositive = logicalAnd(isPositive, ones2(gathered.shape, "bool"));
    const zeroSlice = zerosLike(gathered);
    return where(isPositive, gathered, zeroSlice);
  }

  // node_modules/@tensorflow/tfjs-core/dist/gradients/ZerosLike_grad.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const zerosLikeGradConfig = {
    kernelName: ZerosLike,
    gradFunc: (dy) => {
      return {x: () => zerosLike(dy)};
    }
  };

  // node_modules/@tensorflow/tfjs-core/dist/register_all_gradients.js
  /**
   * @license
   * Copyright 2020 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */
  const gradConfigs = [
    absGradConfig,
    acosGradConfig,
    acoshGradConfig,
    addGradConfig,
    addNGradConfig,
    argMaxGradConfig,
    argMinGradConfig,
    asinGradConfig,
    asinhGradConfig,
    atan2GradConfig,
    atanGradConfig,
    atanhGradConfig,
    avgPool3DGradConfig,
    avgPoolGradConfig,
    batchMatMulGradConfig,
    batchToSpaceNDGradConfig,
    broadcastToGradConfig,
    castGradConfig,
    ceilGradConfig,
    clipByValueGradConfig,
    concatGradConfig,
    conv2DBackpropInputGradConfig,
    conv2DGradConfig,
    conv3DGradConfig,
    cosGradConfig,
    coshGradConfig,
    cumsumGradConfig,
    depthwiseConv2dNativeGradConfig,
    dilation2dGradConfig,
    divGradConfig,
    eluGradConfig,
    erfGradConfig,
    expGradConfig,
    expm1GradConfig,
    floorDivGradConfig,
    floorGradConfig,
    fusedBatchNormGradConfig,
    gatherGradConfig,
    greaterEqualGradConfig,
    identityGradConfig,
    isFiniteGradConfig,
    isInfGradConfig,
    isNanGradConfig,
    log1pGradConfig,
    logGradConfig,
    logSoftmaxGradConfig,
    lrnGradConfig,
    maxGradConfig,
    maxGradConfig,
    maximumGradConfig,
    maxPool3DGradConfig,
    maxPoolGradConfig,
    minGradConfig,
    minimumGradConfig,
    modGradConfig,
    multiplyGradConfig,
    negateGradConfig,
    oneHotGradConfig,
    onesLikeGradConfig,
    padV2GradConfig,
    padV2GradConfig,
    powGradConfig,
    preluGradConfig,
    reciprocalGradConfig,
    relu6GradConfig,
    reluGradConfig,
    reshapeGradConfig,
    resizeBilinearGradConfig,
    resizeNearestNeighborGradConfig,
    reverseGradConfig,
    roundGradConfig,
    rsqrtGradConfig,
    selectV2PoolGradConfig,
    seluGradConfig,
    sigmoidGradConfig,
    signGradConfig,
    sinGradConfig,
    sinhGradConfig,
    sliceGradConfig,
    softmaxGradConfig,
    softplusGradConfig,
    spaceToBatchNDGradConfig,
    spaceToBatchNDGradConfig,
    splitVGradConfig,
    splitVGradConfig,
    sqrtGradConfig,
    squaredDifferenceGradConfig,
    squareGradConfig,
    stepGradConfig,
    subGradConfig,
    sumGradConfig,
    tanGradConfig,
    tanhGradConfig,
    tileGradConfig,
    transposeGradConfig,
    unpackGradConfig,
    unsortedSegmentSumGradConfig,
    zerosLikeGradConfig
  ];
  for (const gradientConfig of gradConfigs) {
    registerGradient(gradientConfig);
  }

  // node_modules/@tensorflow/tfjs-core/dist/index.js
  const dist_exports = {};
  __export(dist_exports, {
    Abs: () => Abs,
    Acos: () => Acos,
    Acosh: () => Acosh,
    AdadeltaOptimizer: () => AdadeltaOptimizer,
    AdagradOptimizer: () => AdagradOptimizer,
    AdamOptimizer: () => AdamOptimizer,
    AdamaxOptimizer: () => AdamaxOptimizer,
    Add: () => Add,
    AddN: () => AddN,
    All: () => All,
    Any: () => Any,
    ArgMax: () => ArgMax,
    ArgMin: () => ArgMin,
    Asin: () => Asin,
    Asinh: () => Asinh,
    Atan: () => Atan,
    Atan2: () => Atan2,
    Atanh: () => Atanh,
    AvgPool: () => AvgPool,
    AvgPool3D: () => AvgPool3D,
    AvgPool3DBackprop: () => AvgPool3DBackprop,
    AvgPoolBackprop: () => AvgPoolBackprop,
    BatchMatMul: () => BatchMatMul,
    BatchToSpaceND: () => BatchToSpaceND,
    BroadcastTo: () => BroadcastTo,
    Cast: () => Cast,
    Ceil: () => Ceil,
    ClipByValue: () => ClipByValue,
    Complex: () => Complex,
    Concat: () => Concat,
    Conv2D: () => Conv2D,
    Conv2DBackpropFilter: () => Conv2DBackpropFilter,
    Conv2DBackpropInput: () => Conv2DBackpropInput,
    Conv3D: () => Conv3D,
    Conv3DBackpropFilterV2: () => Conv3DBackpropFilterV2,
    Conv3DBackpropInputV2: () => Conv3DBackpropInputV2,
    Cos: () => Cos,
    Cosh: () => Cosh,
    CropAndResize: () => CropAndResize,
    Cumsum: () => Cumsum,
    DataStorage: () => DataStorage,
    DepthToSpace: () => DepthToSpace,
    DepthwiseConv2dNative: () => DepthwiseConv2dNative,
    DepthwiseConv2dNativeBackpropFilter: () => DepthwiseConv2dNativeBackpropFilter,
    DepthwiseConv2dNativeBackpropInput: () => DepthwiseConv2dNativeBackpropInput,
    Diag: () => Diag,
    Dilation2D: () => Dilation2D,
    Dilation2DBackpropFilter: () => Dilation2DBackpropFilter,
    Dilation2DBackpropInput: () => Dilation2DBackpropInput,
    Div: () => Div,
    ENV: () => ENV,
    Elu: () => Elu,
    EluGrad: () => EluGrad,
    Environment: () => Environment,
    Equal: () => Equal,
    Erf: () => Erf,
    Exp: () => Exp,
    Expm1: () => Expm1,
    FFT: () => FFT,
    Fill: () => Fill,
    FlipLeftRight: () => FlipLeftRight,
    Floor: () => Floor,
    FloorDiv: () => FloorDiv,
    FromPixels: () => FromPixels,
    FusedBatchNorm: () => FusedBatchNorm,
    FusedConv2D: () => FusedConv2D,
    FusedDepthwiseConv2D: () => FusedDepthwiseConv2D,
    GatherNd: () => GatherNd,
    GatherV2: () => GatherV2,
    Greater: () => Greater,
    GreaterEqual: () => GreaterEqual,
    IFFT: () => IFFT,
    Identity: () => Identity,
    Imag: () => Imag,
    IsFinite: () => IsFinite,
    IsInf: () => IsInf,
    IsNan: () => IsNan,
    KernelBackend: () => KernelBackend,
    LRN: () => LRN,
    LRNBackprop: () => LRNBackprop,
    Less: () => Less,
    LessEqual: () => LessEqual,
    LinSpace: () => LinSpace,
    Log: () => Log,
    Log1p: () => Log1p,
    LogSoftmax: () => LogSoftmax,
    LogicalAnd: () => LogicalAnd,
    LogicalNot: () => LogicalNot,
    LogicalOr: () => LogicalOr,
    Max: () => Max,
    MaxPool: () => MaxPool,
    MaxPool3D: () => MaxPool3D,
    MaxPool3DBackprop: () => MaxPool3DBackprop,
    MaxPoolBackprop: () => MaxPoolBackprop,
    MaxPoolWithArgmax: () => MaxPoolWithArgmax,
    Maximum: () => Maximum,
    Mean: () => Mean,
    Min: () => Min,
    Minimum: () => Minimum,
    Mod: () => Mod,
    MomentumOptimizer: () => MomentumOptimizer,
    Multiply: () => Multiply,
    Negate: () => Negate,
    NonMaxSuppressionV3: () => NonMaxSuppressionV3,
    NonMaxSuppressionV4: () => NonMaxSuppressionV4,
    NonMaxSuppressionV5: () => NonMaxSuppressionV5,
    NotEqual: () => NotEqual,
    OneHot: () => OneHot,
    OnesLike: () => OnesLike,
    Optimizer: () => Optimizer,
    PadV2: () => PadV2,
    Pool: () => Pool,
    Pow: () => Pow,
    Prelu: () => Prelu,
    Prod: () => Prod,
    RMSPropOptimizer: () => RMSPropOptimizer,
    Range: () => Range,
    Rank: () => Rank,
    Real: () => Real,
    Reciprocal: () => Reciprocal,
    Reduction: () => Reduction,
    Relu: () => Relu,
    Relu6: () => Relu6,
    Reshape: () => Reshape,
    ResizeBilinear: () => ResizeBilinear,
    ResizeBilinearGrad: () => ResizeBilinearGrad,
    ResizeNearestNeighbor: () => ResizeNearestNeighbor,
    ResizeNearestNeighborGrad: () => ResizeNearestNeighborGrad,
    Reverse: () => Reverse,
    RotateWithOffset: () => RotateWithOffset,
    Round: () => Round,
    Rsqrt: () => Rsqrt,
    SGDOptimizer: () => SGDOptimizer,
    ScatterNd: () => ScatterNd,
    SelectV2: () => SelectV2,
    Selu: () => Selu,
    Sigmoid: () => Sigmoid,
    Sign: () => Sign,
    Sin: () => Sin,
    Sinh: () => Sinh,
    Slice: () => Slice,
    Softmax: () => Softmax,
    Softplus: () => Softplus,
    SpaceToBatchND: () => SpaceToBatchND,
    SparseToDense: () => SparseToDense,
    SplitV: () => SplitV,
    Sqrt: () => Sqrt,
    Square: () => Square,
    SquaredDifference: () => SquaredDifference,
    Step: () => Step,
    StridedSlice: () => StridedSlice,
    Sub: () => Sub,
    Sum: () => Sum,
    Tan: () => Tan,
    Tanh: () => Tanh,
    Tensor: () => Tensor,
    TensorBuffer: () => TensorBuffer,
    Tile: () => Tile,
    TopK: () => TopK,
    Transpose: () => Transpose,
    Unpack: () => Unpack,
    UnsortedSegmentSum: () => UnsortedSegmentSum,
    Variable: () => Variable,
    ZerosLike: () => ZerosLike,
    _FusedMatMul: () => _FusedMatMul,
    abs: () => abs,
    acos: () => acos,
    acosh: () => acosh,
    add: () => add2,
    addN: () => addN,
    addStrict: () => addStrict,
    all: () => all,
    any: () => any,
    argMax: () => argMax,
    argMin: () => argMin,
    asin: () => asin,
    asinh: () => asinh,
    atan: () => atan,
    atan2: () => atan2,
    atanh: () => atanh,
    avgPool: () => avgPool,
    avgPool3d: () => avgPool3d,
    backend: () => backend,
    backend_util: () => backend_util_exports,
    basicLSTMCell: () => basicLSTMCell,
    batchNorm: () => batchNorm,
    batchNorm2d: () => batchNorm2d,
    batchNorm3d: () => batchNorm3d,
    batchNorm4d: () => batchNorm4d,
    batchToSpaceND: () => batchToSpaceND,
    booleanMaskAsync: () => booleanMaskAsync,
    broadcastTo: () => broadcastTo,
    browser: () => browser_exports,
    buffer: () => buffer,
    cast: () => cast,
    ceil: () => ceil,
    clipByValue: () => clipByValue,
    clone: () => clone,
    complex: () => complex,
    concat: () => concat,
    concat1d: () => concat1d,
    concat2d: () => concat2d,
    concat3d: () => concat3d,
    concat4d: () => concat4d,
    conv1d: () => conv1d,
    conv2d: () => conv2d,
    conv2dTranspose: () => conv2dTranspose,
    conv3d: () => conv3d,
    conv3dTranspose: () => conv3dTranspose,
    cos: () => cos,
    cosh: () => cosh,
    cosineWindow: () => cosineWindow,
    cumsum: () => cumsum,
    customGrad: () => customGrad,
    deprecationWarn: () => deprecationWarn,
    depthToSpace: () => depthToSpace,
    depthwiseConv2d: () => depthwiseConv2d,
    device_util: () => device_util_exports,
    diag: () => diag,
    dilation2d: () => dilation2d,
    disableDeprecationWarnings: () => disableDeprecationWarnings,
    dispose: () => dispose,
    disposeVariables: () => disposeVariables,
    div: () => div,
    divNoNan: () => divNoNan,
    divStrict: () => divStrict,
    dot: () => dot,
    dropout: () => dropout,
    elu: () => elu,
    enableDebugMode: () => enableDebugMode,
    enableProdMode: () => enableProdMode,
    enclosingPowerOfTwo: () => enclosingPowerOfTwo,
    engine: () => engine22,
    env: () => env,
    equal: () => equal,
    equalStrict: () => equalStrict,
    erf: () => erf,
    exp: () => exp,
    expandDims: () => expandDims,
    expm1: () => expm1,
    eye: () => eye,
    fft: () => fft,
    fill: () => fill,
    findBackend: () => findBackend,
    findBackendFactory: () => findBackendFactory,
    floor: () => floor,
    floorDiv: () => floorDiv,
    fused: () => fused_ops_exports,
    gather: () => gather,
    gatherND: () => gatherND,
    gather_util: () => gather_nd_util_exports,
    getBackend: () => getBackend,
    getGradient: () => getGradient,
    getKernel: () => getKernel,
    getKernelsForBackend: () => getKernelsForBackend,
    grad: () => grad,
    grads: () => grads,
    greater: () => greater,
    greaterEqual: () => greaterEqual,
    greaterEqualStrict: () => greaterEqualStrict,
    greaterStrict: () => greaterStrict,
    ifft: () => ifft,
    imag: () => imag,
    image: () => image,
    inTopKAsync: () => inTopKAsync,
    io: () => io_exports,
    irfft: () => irfft,
    isFinite: () => isFinite2,
    isInf: () => isInf,
    isNaN: () => isNaN2,
    keep: () => keep,
    kernel_impls: () => kernel_impls_exports,
    leakyRelu: () => leakyRelu,
    less: () => less,
    lessEqual: () => lessEqual,
    lessEqualStrict: () => lessEqualStrict,
    lessStrict: () => lessStrict,
    linalg: () => linalg,
    linspace: () => linspace,
    localResponseNormalization: () => localResponseNormalization,
    log: () => log,
    log1p: () => log1p,
    logSigmoid: () => logSigmoid,
    logSoftmax: () => logSoftmax,
    logSumExp: () => logSumExp,
    logicalAnd: () => logicalAnd,
    logicalNot: () => logicalNot,
    logicalOr: () => logicalOr,
    logicalXor: () => logicalXor,
    losses: () => losses,
    matMul: () => matMul,
    math: () => math_exports,
    max: () => max,
    maxPool: () => maxPool,
    maxPool3d: () => maxPool3d,
    maxPoolWithArgmax: () => maxPoolWithArgmax,
    maximum: () => maximum,
    maximumStrict: () => maximumStrict,
    mean: () => mean,
    memory: () => memory,
    min: () => min,
    minimum: () => minimum,
    minimumStrict: () => minimumStrict,
    mod: () => mod,
    modStrict: () => modStrict,
    moments: () => moments,
    movingAverage: () => movingAverage,
    mul: () => mul,
    mulStrict: () => mulStrict,
    multiRNNCell: () => multiRNNCell,
    multinomial: () => multinomial,
    neg: () => neg,
    nextFrame: () => nextFrame,
    norm: () => norm,
    notEqual: () => notEqual,
    notEqualStrict: () => notEqualStrict,
    oneHot: () => oneHot,
    ones: () => ones2,
    onesLike: () => onesLike,
    op: () => op,
    outerProduct: () => outerProduct,
    pad: () => pad,
    pad1d: () => pad1d,
    pad2d: () => pad2d,
    pad3d: () => pad3d,
    pad4d: () => pad4d,
    pool: () => pool,
    pow: () => pow,
    powStrict: () => powStrict,
    prelu: () => prelu,
    print: () => print,
    prod: () => prod,
    profile: () => profile,
    rand: () => rand,
    randomGamma: () => randomGamma,
    randomNormal: () => randomNormal,
    randomUniform: () => randomUniform,
    range: () => range,
    ready: () => ready,
    real: () => real,
    reciprocal: () => reciprocal,
    registerBackend: () => registerBackend,
    registerGradient: () => registerGradient,
    registerKernel: () => registerKernel,
    relu: () => relu,
    relu6: () => relu6,
    removeBackend: () => removeBackend,
    reshape: () => reshape,
    reverse: () => reverse,
    reverse1d: () => reverse1d,
    reverse2d: () => reverse2d,
    reverse3d: () => reverse3d,
    reverse4d: () => reverse4d,
    rfft: () => rfft,
    round: () => round,
    rsqrt: () => rsqrt,
    scalar: () => scalar,
    scatterND: () => scatterND,
    scatter_util: () => scatter_nd_util_exports,
    selu: () => selu,
    separableConv2d: () => separableConv2d,
    serialization: () => serialization_exports,
    setBackend: () => setBackend,
    setPlatform: () => setPlatform,
    setdiff1dAsync: () => setdiff1dAsync,
    sigmoid: () => sigmoid,
    sign: () => sign,
    signal: () => signal,
    sin: () => sin,
    sinh: () => sinh,
    slice: () => slice,
    slice1d: () => slice1d,
    slice2d: () => slice2d,
    slice3d: () => slice3d,
    slice4d: () => slice4d,
    slice_util: () => slice_util_exports,
    softmax: () => softmax,
    softplus: () => softplus,
    spaceToBatchND: () => spaceToBatchND,
    sparseToDense: () => sparseToDense,
    spectral: () => spectral,
    split: () => split,
    sqrt: () => sqrt,
    square: () => square,
    squaredDifference: () => squaredDifference,
    squaredDifferenceStrict: () => squaredDifferenceStrict,
    squeeze: () => squeeze,
    stack: () => stack,
    step: () => step,
    stridedSlice: () => stridedSlice,
    sub: () => sub,
    subStrict: () => subStrict,
    sum: () => sum2,
    sumOutType: () => sumOutType,
    tan: () => tan,
    tanh: () => tanh2,
    tensor: () => tensor5,
    tensor1d: () => tensor1d,
    tensor2d: () => tensor2d,
    tensor3d: () => tensor3d,
    tensor4d: () => tensor4d,
    tensor5d: () => tensor5d,
    tensor6d: () => tensor6d,
    tensor_util: () => tensor_util_exports,
    test_util: () => test_util_exports,
    tidy: () => tidy,
    tile: () => tile,
    time: () => time,
    topk: () => topk,
    train: () => train,
    transpose: () => transpose,
    truncatedNormal: () => truncatedNormal,
    unregisterGradient: () => unregisterGradient,
    unregisterKernel: () => unregisterKernel,
    unsortedSegmentSum: () => unsortedSegmentSum,
    unstack: () => unstack,
    upcastType: () => upcastType,
    util: () => util_exports,
    valueAndGrad: () => valueAndGrad,
    valueAndGrads: () => valueAndGrads,
    variable: () => variable,
    variableGrads: () => variableGrads,
    version_core: () => version,
    where: () => where,
    whereAsync: () => whereAsync,
    zeros: () => zeros,
    zerosLike: () => zerosLike
  });
  /**
   * @license
   * Copyright 2017 Google LLC. All Rights Reserved.
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   * =============================================================================
   */

  // build/draw/drawContour.js
  function drawContour(ctx, points, isClosed = false) {
    ctx.beginPath();
    points.slice(1).forEach(({x, y}, prevIdx) => {
      const from = points[prevIdx];
      ctx.moveTo(from.x, from.y);
      ctx.lineTo(x, y);
    });
    if (isClosed) {
      const from = points[points.length - 1];
      const to = points[0];
      if (!from || !to) {
        return;
      }
      ctx.moveTo(from.x, from.y);
      ctx.lineTo(to.x, to.y);
    }
    ctx.stroke();
  }

  // build/classes/Dimensions.js
  class Dimensions {
    constructor(width, height) {
      if (!isValidNumber(width) || !isValidNumber(height)) {
        throw new Error(`Dimensions.constructor - expected width and height to be valid numbers, instead have ${JSON.stringify({width, height})}`);
      }
      this._width = width;
      this._height = height;
    }
    get width() {
      return this._width;
    }
    get height() {
      return this._height;
    }
    reverse() {
      return new Dimensions(1 / this.width, 1 / this.height);
    }
  }

  // build/utils/index.js
  const utils_exports = {};
  __export(utils_exports, {
    computeReshapedDimensions: () => computeReshapedDimensions,
    getCenterPoint: () => getCenterPoint,
    isDimensions: () => isDimensions,
    isEven: () => isEven,
    isFloat: () => isFloat,
    isTensor: () => isTensor,
    isTensor1D: () => isTensor1D,
    isTensor2D: () => isTensor2D,
    isTensor3D: () => isTensor3D,
    isTensor4D: () => isTensor4D,
    isValidNumber: () => isValidNumber,
    isValidProbablitiy: () => isValidProbablitiy,
    range: () => range4,
    round: () => round3
  });
  function isTensor(tensor17, dim) {
    return tensor17 instanceof Tensor && tensor17.shape.length === dim;
  }
  function isTensor1D(tensor17) {
    return isTensor(tensor17, 1);
  }
  function isTensor2D(tensor17) {
    return isTensor(tensor17, 2);
  }
  function isTensor3D(tensor17) {
    return isTensor(tensor17, 3);
  }
  function isTensor4D(tensor17) {
    return isTensor(tensor17, 4);
  }
  function isFloat(num) {
    return num % 1 !== 0;
  }
  function isEven(num) {
    return num % 2 === 0;
  }
  function round3(num, prec = 2) {
    const f = Math.pow(10, prec);
    return Math.floor(num * f) / f;
  }
  function isDimensions(obj) {
    return obj && obj.width && obj.height;
  }
  function computeReshapedDimensions({width, height}, inputSize) {
    const scale2 = inputSize / Math.max(height, width);
    return new Dimensions(Math.round(width * scale2), Math.round(height * scale2));
  }
  function getCenterPoint(pts) {
    return pts.reduce((sum26, pt) => sum26.add(pt), new Point(0, 0)).div(new Point(pts.length, pts.length));
  }
  function range4(num, start, step7) {
    return Array(num).fill(0).map((_, i) => start + i * step7);
  }
  function isValidNumber(num) {
    return !!num && num !== Infinity && num !== -Infinity && !isNaN(num) || num === 0;
  }
  function isValidProbablitiy(num) {
    return isValidNumber(num) && 0 <= num && num <= 1;
  }

  // build/classes/Point.js
  class Point {
    constructor(x, y) {
      this._x = x;
      this._y = y;
    }
    get x() {
      return this._x;
    }
    get y() {
      return this._y;
    }
    add(pt) {
      return new Point(this.x + pt.x, this.y + pt.y);
    }
    sub(pt) {
      return new Point(this.x - pt.x, this.y - pt.y);
    }
    mul(pt) {
      return new Point(this.x * pt.x, this.y * pt.y);
    }
    div(pt) {
      return new Point(this.x / pt.x, this.y / pt.y);
    }
    abs() {
      return new Point(Math.abs(this.x), Math.abs(this.y));
    }
    magnitude() {
      return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }
    floor() {
      return new Point(Math.floor(this.x), Math.floor(this.y));
    }
  }

  // build/classes/Box.js
  class Box {
    constructor(_box, allowNegativeDimensions = true) {
      const box = _box || {};
      const isBbox = [box.left, box.top, box.right, box.bottom].every(isValidNumber);
      const isRect = [box.x, box.y, box.width, box.height].every(isValidNumber);
      if (!isRect && !isBbox) {
        throw new Error(`Box.constructor - expected box to be IBoundingBox | IRect, instead have ${JSON.stringify(box)}`);
      }
      const [x, y, width, height] = isRect ? [box.x, box.y, box.width, box.height] : [box.left, box.top, box.right - box.left, box.bottom - box.top];
      Box.assertIsValidBox({x, y, width, height}, "Box.constructor", allowNegativeDimensions);
      this._x = x;
      this._y = y;
      this._width = width;
      this._height = height;
    }
    static isRect(rect) {
      return !!rect && [rect.x, rect.y, rect.width, rect.height].every(isValidNumber);
    }
    static assertIsValidBox(box, callee, allowNegativeDimensions = false) {
      if (!Box.isRect(box)) {
        throw new Error(`${callee} - invalid box: ${JSON.stringify(box)}, expected object with properties x, y, width, height`);
      }
      if (!allowNegativeDimensions && (box.width < 0 || box.height < 0)) {
        throw new Error(`${callee} - width (${box.width}) and height (${box.height}) must be positive numbers`);
      }
    }
    get x() {
      return this._x;
    }
    get y() {
      return this._y;
    }
    get width() {
      return this._width;
    }
    get height() {
      return this._height;
    }
    get left() {
      return this.x;
    }
    get top() {
      return this.y;
    }
    get right() {
      return this.x + this.width;
    }
    get bottom() {
      return this.y + this.height;
    }
    get area() {
      return this.width * this.height;
    }
    get topLeft() {
      return new Point(this.left, this.top);
    }
    get topRight() {
      return new Point(this.right, this.top);
    }
    get bottomLeft() {
      return new Point(this.left, this.bottom);
    }
    get bottomRight() {
      return new Point(this.right, this.bottom);
    }
    round() {
      const [x, y, width, height] = [this.x, this.y, this.width, this.height].map((val) => Math.round(val));
      return new Box({x, y, width, height});
    }
    floor() {
      const [x, y, width, height] = [this.x, this.y, this.width, this.height].map((val) => Math.floor(val));
      return new Box({x, y, width, height});
    }
    toSquare() {
      let {x, y, width, height} = this;
      const diff = Math.abs(width - height);
      if (width < height) {
        x -= diff / 2;
        width += diff;
      }
      if (height < width) {
        y -= diff / 2;
        height += diff;
      }
      return new Box({x, y, width, height});
    }
    rescale(s) {
      const scaleX = isDimensions(s) ? s.width : s;
      const scaleY = isDimensions(s) ? s.height : s;
      return new Box({
        x: this.x * scaleX,
        y: this.y * scaleY,
        width: this.width * scaleX,
        height: this.height * scaleY
      });
    }
    pad(padX, padY) {
      let [x, y, width, height] = [
        this.x - padX / 2,
        this.y - padY / 2,
        this.width + padX,
        this.height + padY
      ];
      return new Box({x, y, width, height});
    }
    clipAtImageBorders(imgWidth, imgHeight) {
      const {x, y, right, bottom} = this;
      const clippedX = Math.max(x, 0);
      const clippedY = Math.max(y, 0);
      const newWidth = right - clippedX;
      const newHeight = bottom - clippedY;
      const clippedWidth = Math.min(newWidth, imgWidth - clippedX);
      const clippedHeight = Math.min(newHeight, imgHeight - clippedY);
      return new Box({x: clippedX, y: clippedY, width: clippedWidth, height: clippedHeight}).floor();
    }
    shift(sx, sy) {
      const {width, height} = this;
      const x = this.x + sx;
      const y = this.y + sy;
      return new Box({x, y, width, height});
    }
    padAtBorders(imageHeight, imageWidth) {
      const w = this.width + 1;
      const h = this.height + 1;
      let dx = 1;
      let dy = 1;
      let edx = w;
      let edy = h;
      let x = this.left;
      let y = this.top;
      let ex = this.right;
      let ey = this.bottom;
      if (ex > imageWidth) {
        edx = -ex + imageWidth + w;
        ex = imageWidth;
      }
      if (ey > imageHeight) {
        edy = -ey + imageHeight + h;
        ey = imageHeight;
      }
      if (x < 1) {
        edy = 2 - x;
        x = 1;
      }
      if (y < 1) {
        edy = 2 - y;
        y = 1;
      }
      return {dy, edy, dx, edx, y, ey, x, ex, w, h};
    }
    calibrate(region) {
      return new Box({
        left: this.left + region.left * this.width,
        top: this.top + region.top * this.height,
        right: this.right + region.right * this.width,
        bottom: this.bottom + region.bottom * this.height
      }).toSquare().round();
    }
  }

  // build/classes/BoundingBox.js
  class BoundingBox extends Box {
    constructor(left, top, right, bottom, allowNegativeDimensions = false) {
      super({left, top, right, bottom}, allowNegativeDimensions);
    }
  }

  // build/classes/ObjectDetection.js
  class ObjectDetection {
    constructor(score, classScore, className, relativeBox, imageDims) {
      this._imageDims = new Dimensions(imageDims.width, imageDims.height);
      this._score = score;
      this._classScore = classScore;
      this._className = className;
      this._box = new Box(relativeBox).rescale(this._imageDims);
    }
    get score() {
      return this._score;
    }
    get classScore() {
      return this._classScore;
    }
    get className() {
      return this._className;
    }
    get box() {
      return this._box;
    }
    get imageDims() {
      return this._imageDims;
    }
    get imageWidth() {
      return this.imageDims.width;
    }
    get imageHeight() {
      return this.imageDims.height;
    }
    get relativeBox() {
      return new Box(this._box).rescale(this.imageDims.reverse());
    }
    forSize(width, height) {
      return new ObjectDetection(this.score, this.classScore, this.className, this.relativeBox, {width, height});
    }
  }

  // build/classes/FaceDetection.js
  class FaceDetection extends ObjectDetection {
    constructor(score, relativeBox, imageDims) {
      super(score, score, "", relativeBox, imageDims);
    }
    forSize(width, height) {
      const {score, relativeBox, imageDims} = super.forSize(width, height);
      return new FaceDetection(score, relativeBox, imageDims);
    }
  }

  // build/ops/iou.js
  function iou(box1, box2, isIOU = true) {
    const width = Math.max(0, Math.min(box1.right, box2.right) - Math.max(box1.left, box2.left));
    const height = Math.max(0, Math.min(box1.bottom, box2.bottom) - Math.max(box1.top, box2.top));
    const interSection = width * height;
    return isIOU ? interSection / (box1.area + box2.area - interSection) : interSection / Math.min(box1.area, box2.area);
  }

  // build/ops/minBbox.js
  function minBbox(pts) {
    const xs = pts.map((pt) => pt.x);
    const ys = pts.map((pt) => pt.y);
    const minX = xs.reduce((min5, x) => x < min5 ? x : min5, Infinity);
    const minY = ys.reduce((min5, y) => y < min5 ? y : min5, Infinity);
    const maxX = xs.reduce((max7, x) => max7 < x ? x : max7, 0);
    const maxY = ys.reduce((max7, y) => max7 < y ? y : max7, 0);
    return new BoundingBox(minX, minY, maxX, maxY);
  }

  // build/ops/nonMaxSuppression.js
  function nonMaxSuppression2(boxes, scores, iouThreshold, isIOU = true) {
    let indicesSortedByScore = scores.map((score, boxIndex) => ({score, boxIndex})).sort((c1, c2) => c1.score - c2.score).map((c) => c.boxIndex);
    const pick = [];
    while (indicesSortedByScore.length > 0) {
      const curr = indicesSortedByScore.pop();
      pick.push(curr);
      const indices = indicesSortedByScore;
      const outputs = [];
      for (let i = 0; i < indices.length; i++) {
        const idx = indices[i];
        const currBox = boxes[curr];
        const idxBox = boxes[idx];
        outputs.push(iou(currBox, idxBox, isIOU));
      }
      indicesSortedByScore = indicesSortedByScore.filter((_, j) => outputs[j] <= iouThreshold);
    }
    return pick;
  }

  // build/ops/normalize.js
  function normalize(x, meanRgb) {
    return tidy(() => {
      const [r, g, b] = meanRgb;
      const avg_r = fill([...x.shape.slice(0, 3), 1], r);
      const avg_g = fill([...x.shape.slice(0, 3), 1], g);
      const avg_b = fill([...x.shape.slice(0, 3), 1], b);
      const avg_rgb = concat([avg_r, avg_g, avg_b], 3);
      return sub(x, avg_rgb);
    });
  }

  // build/ops/padToSquare.js
  function padToSquare(imgTensor, isCenterImage = false) {
    return tidy(() => {
      const [height, width] = imgTensor.shape.slice(1);
      if (height === width) {
        return imgTensor;
      }
      const dimDiff = Math.abs(height - width);
      const paddingAmount = Math.round(dimDiff * (isCenterImage ? 0.5 : 1));
      const paddingAxis = height > width ? 2 : 1;
      const createPaddingTensor = (paddingAmount2) => {
        const paddingTensorShape = imgTensor.shape.slice();
        paddingTensorShape[paddingAxis] = paddingAmount2;
        return fill(paddingTensorShape, 0);
      };
      const paddingTensorAppend = createPaddingTensor(paddingAmount);
      const remainingPaddingAmount = dimDiff - paddingTensorAppend.shape[paddingAxis];
      const paddingTensorPrepend = isCenterImage && remainingPaddingAmount ? createPaddingTensor(remainingPaddingAmount) : null;
      const tensorsToStack = [
        paddingTensorPrepend,
        imgTensor,
        paddingTensorAppend
      ].filter((t) => !!t).map((t) => t.toFloat());
      return concat(tensorsToStack, paddingAxis);
    });
  }

  // build/ops/shuffleArray.js
  function shuffleArray(inputArray) {
    const array = inputArray.slice();
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      const x = array[i];
      array[i] = array[j];
      array[j] = x;
    }
    return array;
  }

  // build/ops/index.js
  function sigmoid6(x) {
    return 1 / (1 + Math.exp(-x));
  }
  function inverseSigmoid(x) {
    return Math.log(x / (1 - x));
  }

  // build/classes/Rect.js
  class Rect extends Box {
    constructor(x, y, width, height, allowNegativeDimensions = false) {
      super({x, y, width, height}, allowNegativeDimensions);
    }
  }

  // build/classes/FaceLandmarks.js
  const relX = 0.5;
  const relY = 0.43;
  const relScale = 0.45;
  class FaceLandmarks {
    constructor(relativeFaceLandmarkPositions, imgDims, shift = new Point(0, 0)) {
      const {width, height} = imgDims;
      this._imgDims = new Dimensions(width, height);
      this._shift = shift;
      this._positions = relativeFaceLandmarkPositions.map((pt) => pt.mul(new Point(width, height)).add(shift));
    }
    get shift() {
      return new Point(this._shift.x, this._shift.y);
    }
    get imageWidth() {
      return this._imgDims.width;
    }
    get imageHeight() {
      return this._imgDims.height;
    }
    get positions() {
      return this._positions;
    }
    get relativePositions() {
      return this._positions.map((pt) => pt.sub(this._shift).div(new Point(this.imageWidth, this.imageHeight)));
    }
    forSize(width, height) {
      return new this.constructor(this.relativePositions, {width, height});
    }
    shiftBy(x, y) {
      return new this.constructor(this.relativePositions, this._imgDims, new Point(x, y));
    }
    shiftByPoint(pt) {
      return this.shiftBy(pt.x, pt.y);
    }
    align(detection, options = {}) {
      if (detection) {
        const box = detection instanceof FaceDetection ? detection.box.floor() : new Box(detection);
        return this.shiftBy(box.x, box.y).align(null, options);
      }
      const {useDlibAlignment, minBoxPadding} = Object.assign({}, {useDlibAlignment: false, minBoxPadding: 0.2}, options);
      if (useDlibAlignment) {
        return this.alignDlib();
      }
      return this.alignMinBbox(minBoxPadding);
    }
    alignDlib() {
      const centers = this.getRefPointsForAlignment();
      const [leftEyeCenter, rightEyeCenter, mouthCenter] = centers;
      const distToMouth = (pt) => mouthCenter.sub(pt).magnitude();
      const eyeToMouthDist = (distToMouth(leftEyeCenter) + distToMouth(rightEyeCenter)) / 2;
      const size = Math.floor(eyeToMouthDist / relScale);
      const refPoint = getCenterPoint(centers);
      const x = Math.floor(Math.max(0, refPoint.x - relX * size));
      const y = Math.floor(Math.max(0, refPoint.y - relY * size));
      return new Rect(x, y, Math.min(size, this.imageWidth + x), Math.min(size, this.imageHeight + y));
    }
    alignMinBbox(padding) {
      const box = minBbox(this.positions);
      return box.pad(box.width * padding, box.height * padding);
    }
    getRefPointsForAlignment() {
      throw new Error("getRefPointsForAlignment not implemented by base class");
    }
  }

  // build/classes/FaceLandmarks5.js
  class FaceLandmarks5 extends FaceLandmarks {
    getRefPointsForAlignment() {
      const pts = this.positions;
      return [
        pts[0],
        pts[1],
        getCenterPoint([pts[3], pts[4]])
      ];
    }
  }

  // build/classes/FaceLandmarks68.js
  class FaceLandmarks68 extends FaceLandmarks {
    getJawOutline() {
      return this.positions.slice(0, 17);
    }
    getLeftEyeBrow() {
      return this.positions.slice(17, 22);
    }
    getRightEyeBrow() {
      return this.positions.slice(22, 27);
    }
    getNose() {
      return this.positions.slice(27, 36);
    }
    getLeftEye() {
      return this.positions.slice(36, 42);
    }
    getRightEye() {
      return this.positions.slice(42, 48);
    }
    getMouth() {
      return this.positions.slice(48, 68);
    }
    getRefPointsForAlignment() {
      return [
        this.getLeftEye(),
        this.getRightEye(),
        this.getMouth()
      ].map(getCenterPoint);
    }
  }

  // build/classes/FaceMatch.js
  class FaceMatch {
    constructor(label, distance) {
      this._label = label;
      this._distance = distance;
    }
    get label() {
      return this._label;
    }
    get distance() {
      return this._distance;
    }
    toString(withDistance = true) {
      return `${this.label}${withDistance ? ` (${round3(this.distance)})` : ""}`;
    }
  }

  // build/classes/LabeledBox.js
  class LabeledBox extends Box {
    constructor(box, label) {
      super(box);
      this._label = label;
    }
    static assertIsValidLabeledBox(box, callee) {
      Box.assertIsValidBox(box, callee);
      if (!isValidNumber(box.label)) {
        throw new Error(`${callee} - expected property label (${box.label}) to be a number`);
      }
    }
    get label() {
      return this._label;
    }
  }

  // build/classes/LabeledFaceDescriptors.js
  class LabeledFaceDescriptors {
    constructor(label, descriptors) {
      if (!(typeof label === "string")) {
        throw new Error("LabeledFaceDescriptors - constructor expected label to be a string");
      }
      if (!Array.isArray(descriptors) || descriptors.some((desc) => !(desc instanceof Float32Array))) {
        throw new Error("LabeledFaceDescriptors - constructor expected descriptors to be an array of Float32Array");
      }
      this._label = label;
      this._descriptors = descriptors;
    }
    get label() {
      return this._label;
    }
    get descriptors() {
      return this._descriptors;
    }
    toJSON() {
      return {
        label: this.label,
        descriptors: this.descriptors.map((d) => Array.from(d))
      };
    }
    static fromJSON(json) {
      const descriptors = json.descriptors.map((d) => {
        return new Float32Array(d);
      });
      return new LabeledFaceDescriptors(json.label, descriptors);
    }
  }

  // build/classes/PredictedBox.js
  class PredictedBox extends LabeledBox {
    constructor(box, label, score, classScore) {
      super(box, label);
      this._score = score;
      this._classScore = classScore;
    }
    static assertIsValidPredictedBox(box, callee) {
      LabeledBox.assertIsValidLabeledBox(box, callee);
      if (!isValidProbablitiy(box.score) || !isValidProbablitiy(box.classScore)) {
        throw new Error(`${callee} - expected properties score (${box.score}) and (${box.classScore}) to be a number between [0, 1]`);
      }
    }
    get score() {
      return this._score;
    }
    get classScore() {
      return this._classScore;
    }
  }

  // build/classes/index.js

  // build/factories/WithFaceDetection.js
  function isWithFaceDetection(obj) {
    return obj["detection"] instanceof FaceDetection;
  }
  function extendWithFaceDetection(sourceObj, detection) {
    const extension = {detection};
    return Object.assign({}, sourceObj, extension);
  }

  // build/env/createBrowserEnv.js
  function createBrowserEnv() {
    const fetch3 = window["fetch"] || function() {
      throw new Error("fetch - missing fetch implementation for browser environment");
    };
    const readFile = function() {
      throw new Error("readFile - filesystem not available for browser environment");
    };
    return {
      Canvas: HTMLCanvasElement,
      CanvasRenderingContext2D,
      Image: HTMLImageElement,
      ImageData,
      Video: HTMLVideoElement,
      createCanvasElement: () => document.createElement("canvas"),
      createImageElement: () => document.createElement("img"),
      fetch: fetch3,
      readFile
    };
  }

  // build/env/createFileSystem.js
  function createFileSystem(fs) {
    let requireFsError = "";
    if (!fs) {
      try {
        fs = require("fs");
      } catch (err) {
        requireFsError = err.toString();
      }
    }
    const readFile = fs ? function(filePath) {
      return new Promise((res, rej) => {
        fs.readFile(filePath, function(err, buffer10) {
          return err ? rej(err) : res(buffer10);
        });
      });
    } : function() {
      throw new Error(`readFile - failed to require fs in nodejs environment with error: ${requireFsError}`);
    };
    return {
      readFile
    };
  }

  // build/env/createNodejsEnv.js
  function createNodejsEnv() {
    const Canvas = global["Canvas"] || global["HTMLCanvasElement"];
    const Image = global["Image"] || global["HTMLImageElement"];
    const createCanvasElement = function() {
      if (Canvas) {
        return new Canvas();
      }
      throw new Error("createCanvasElement - missing Canvas implementation for nodejs environment");
    };
    const createImageElement = function() {
      if (Image) {
        return new Image();
      }
      throw new Error("createImageElement - missing Image implementation for nodejs environment");
    };
    const fetch3 = global["fetch"] || function() {
      throw new Error("fetch - missing fetch implementation for nodejs environment");
    };
    const fileSystem = createFileSystem();
    return __assign({
      Canvas: Canvas || class {
      },
      CanvasRenderingContext2D: global["CanvasRenderingContext2D"] || class {
      },
      Image: Image || class {
      },
      ImageData: global["ImageData"] || class {
      },
      Video: global["HTMLVideoElement"] || class {
      },
      createCanvasElement,
      createImageElement,
      fetch: fetch3
    }, fileSystem);
  }

  // build/env/isBrowser.js
  function isBrowser2() {
    return typeof window === "object" && typeof document !== "undefined" && typeof HTMLImageElement !== "undefined" && typeof HTMLCanvasElement !== "undefined" && typeof HTMLVideoElement !== "undefined" && typeof ImageData !== "undefined" && typeof CanvasRenderingContext2D !== "undefined";
  }

  // build/env/types.js

  // build/env/index.js
  const isNodejs = __toModule(require_isNodejs());
  let environment11;
  function getEnv() {
    if (!environment11) {
      throw new Error("getEnv - environment is not defined, check isNodejs() and isBrowser()");
    }
    return environment11;
  }
  function setEnv(env17) {
    environment11 = env17;
  }
  function initialize() {
    if (isBrowser2()) {
      return setEnv(createBrowserEnv());
    }
    if (isNodejs.isNodejs()) {
      return setEnv(createNodejsEnv());
    }
  }
  function monkeyPatch(env17) {
    if (!environment11) {
      initialize();
    }
    if (!environment11) {
      throw new Error("monkeyPatch - environment is not defined, check isNodejs() and isBrowser()");
    }
    const {Canvas = environment11.Canvas, Image = environment11.Image} = env17;
    environment11.Canvas = Canvas;
    environment11.Image = Image;
    environment11.createCanvasElement = env17.createCanvasElement || (() => new Canvas());
    environment11.createImageElement = env17.createImageElement || (() => new Image());
    environment11.ImageData = env17.ImageData || environment11.ImageData;
    environment11.Video = env17.Video || environment11.Video;
    environment11.fetch = env17.fetch || environment11.fetch;
    environment11.readFile = env17.readFile || environment11.readFile;
  }
  const env2 = {
    getEnv,
    setEnv,
    initialize,
    createBrowserEnv,
    createFileSystem,
    createNodejsEnv,
    monkeyPatch,
    isBrowser: isBrowser2,
    isNodejs: isNodejs.isNodejs
  };
  initialize();

  // build/dom/resolveInput.js
  function resolveInput(arg) {
    if (!env2.isNodejs() && typeof arg === "string") {
      return document.getElementById(arg);
    }
    return arg;
  }

  // build/dom/getContext2dOrThrow.js
  function getContext2dOrThrow(canvasArg) {
    const {Canvas, CanvasRenderingContext2D: CanvasRenderingContext2D2} = env2.getEnv();
    if (canvasArg instanceof CanvasRenderingContext2D2) {
      return canvasArg;
    }
    const canvas = resolveInput(canvasArg);
    if (!(canvas instanceof Canvas)) {
      throw new Error("resolveContext2d - expected canvas to be of instance of Canvas");
    }
    const ctx = canvas.getContext("2d");
    if (!ctx) {
      throw new Error("resolveContext2d - canvas 2d context is null");
    }
    return ctx;
  }

  // build/draw/DrawTextField.js
  var AnchorPosition;
  (function(AnchorPosition2) {
    AnchorPosition2["TOP_LEFT"] = "TOP_LEFT";
    AnchorPosition2["TOP_RIGHT"] = "TOP_RIGHT";
    AnchorPosition2["BOTTOM_LEFT"] = "BOTTOM_LEFT";
    AnchorPosition2["BOTTOM_RIGHT"] = "BOTTOM_RIGHT";
  })(AnchorPosition || (AnchorPosition = {}));
  class DrawTextFieldOptions {
    constructor(options = {}) {
      const {anchorPosition, backgroundColor, fontColor, fontSize, fontStyle, padding} = options;
      this.anchorPosition = anchorPosition || AnchorPosition.TOP_LEFT;
      this.backgroundColor = backgroundColor || "rgba(0, 0, 0, 0.5)";
      this.fontColor = fontColor || "rgba(255, 255, 255, 1)";
      this.fontSize = fontSize || 14;
      this.fontStyle = fontStyle || "Georgia";
      this.padding = padding || 4;
    }
  }
  class DrawTextField {
    constructor(text, anchor, options = {}) {
      this.text = typeof text === "string" ? [text] : text instanceof DrawTextField ? text.text : text;
      this.anchor = anchor;
      this.options = new DrawTextFieldOptions(options);
    }
    measureWidth(ctx) {
      const {padding} = this.options;
      return this.text.map((l) => ctx.measureText(l).width).reduce((w0, w1) => w0 < w1 ? w1 : w0, 0) + 2 * padding;
    }
    measureHeight() {
      const {fontSize, padding} = this.options;
      return this.text.length * fontSize + 2 * padding;
    }
    getUpperLeft(ctx, canvasDims) {
      const {anchorPosition} = this.options;
      const isShiftLeft = anchorPosition === AnchorPosition.BOTTOM_RIGHT || anchorPosition === AnchorPosition.TOP_RIGHT;
      const isShiftTop = anchorPosition === AnchorPosition.BOTTOM_LEFT || anchorPosition === AnchorPosition.BOTTOM_RIGHT;
      const textFieldWidth = this.measureWidth(ctx);
      const textFieldHeight = this.measureHeight();
      const x = isShiftLeft ? this.anchor.x - textFieldWidth : this.anchor.x;
      const y = isShiftTop ? this.anchor.y - textFieldHeight : this.anchor.y;
      if (canvasDims) {
        const {width, height} = canvasDims;
        const newX = Math.max(Math.min(x, width - textFieldWidth), 0);
        const newY = Math.max(Math.min(y, height - textFieldHeight), 0);
        return {x: newX, y: newY};
      }
      return {x, y};
    }
    draw(canvasArg) {
      const canvas = resolveInput(canvasArg);
      const ctx = getContext2dOrThrow(canvas);
      const {backgroundColor, fontColor, fontSize, fontStyle, padding} = this.options;
      ctx.font = `${fontSize}px ${fontStyle}`;
      const maxTextWidth = this.measureWidth(ctx);
      const textHeight = this.measureHeight();
      ctx.fillStyle = backgroundColor;
      const upperLeft = this.getUpperLeft(ctx, canvas);
      ctx.fillRect(upperLeft.x, upperLeft.y, maxTextWidth, textHeight);
      ctx.fillStyle = fontColor;
      this.text.forEach((textLine, i) => {
        const x = padding + upperLeft.x;
        const y = padding + upperLeft.y + (i + 1) * fontSize;
        ctx.fillText(textLine, x, y);
      });
    }
  }

  // build/draw/DrawBox.js
  class DrawBoxOptions {
    constructor(options = {}) {
      const {boxColor, lineWidth, label, drawLabelOptions} = options;
      this.boxColor = boxColor || "rgba(0, 0, 255, 1)";
      this.lineWidth = lineWidth || 2;
      this.label = label;
      const defaultDrawLabelOptions = {
        anchorPosition: AnchorPosition.BOTTOM_LEFT,
        backgroundColor: this.boxColor
      };
      this.drawLabelOptions = new DrawTextFieldOptions(Object.assign({}, defaultDrawLabelOptions, drawLabelOptions));
    }
  }
  class DrawBox {
    constructor(box, options = {}) {
      this.box = new Box(box);
      this.options = new DrawBoxOptions(options);
    }
    draw(canvasArg) {
      const ctx = getContext2dOrThrow(canvasArg);
      const {boxColor, lineWidth} = this.options;
      const {x, y, width, height} = this.box;
      ctx.strokeStyle = boxColor;
      ctx.lineWidth = lineWidth;
      ctx.strokeRect(x, y, width, height);
      const {label} = this.options;
      if (label) {
        new DrawTextField([label], {x: x - lineWidth / 2, y}, this.options.drawLabelOptions).draw(canvasArg);
      }
    }
  }

  // build/draw/drawDetections.js
  function drawDetections(canvasArg, detections) {
    const detectionsArray = Array.isArray(detections) ? detections : [detections];
    detectionsArray.forEach((det) => {
      const score = det instanceof FaceDetection ? det.score : isWithFaceDetection(det) ? det.detection.score : void 0;
      const box = det instanceof FaceDetection ? det.box : isWithFaceDetection(det) ? det.detection.box : new Box(det);
      const label = score ? `${round3(score)}` : void 0;
      new DrawBox(box, {label}).draw(canvasArg);
    });
  }

  // build/dom/isMediaLoaded.js
  function isMediaLoaded(media) {
    const {Image, Video} = env2.getEnv();
    return media instanceof Image && media.complete || media instanceof Video && media.readyState >= 3;
  }

  // build/dom/awaitMediaLoaded.js
  function awaitMediaLoaded(media) {
    return new Promise((resolve, reject) => {
      if (media instanceof env2.getEnv().Canvas || isMediaLoaded(media)) {
        return resolve();
      }
      function onLoad(e) {
        if (!e.currentTarget)
          return;
        e.currentTarget.removeEventListener("load", onLoad);
        e.currentTarget.removeEventListener("error", onError);
        resolve(e);
      }
      function onError(e) {
        if (!e.currentTarget)
          return;
        e.currentTarget.removeEventListener("load", onLoad);
        e.currentTarget.removeEventListener("error", onError);
        reject(e);
      }
      media.addEventListener("load", onLoad);
      media.addEventListener("error", onError);
    });
  }

  // build/dom/bufferToImage.js
  function bufferToImage(buf) {
    return new Promise((resolve, reject) => {
      if (!(buf instanceof Blob)) {
        return reject("bufferToImage - expected buf to be of type: Blob");
      }
      const reader = new FileReader();
      reader.onload = () => {
        if (typeof reader.result !== "string") {
          return reject("bufferToImage - expected reader.result to be a string, in onload");
        }
        const img = env2.getEnv().createImageElement();
        img.onload = () => resolve(img);
        img.onerror = reject;
        img.src = reader.result;
      };
      reader.onerror = reject;
      reader.readAsDataURL(buf);
    });
  }

  // build/dom/getMediaDimensions.js
  function getMediaDimensions(input) {
    const {Image, Video} = env2.getEnv();
    if (input instanceof Image) {
      return new Dimensions(input.naturalWidth, input.naturalHeight);
    }
    if (input instanceof Video) {
      return new Dimensions(input.videoWidth, input.videoHeight);
    }
    return new Dimensions(input.width, input.height);
  }

  // build/dom/createCanvas.js
  function createCanvas({width, height}) {
    const {createCanvasElement} = env2.getEnv();
    const canvas = createCanvasElement();
    canvas.width = width;
    canvas.height = height;
    return canvas;
  }
  function createCanvasFromMedia(media, dims) {
    const {ImageData: ImageData2} = env2.getEnv();
    if (!(media instanceof ImageData2) && !isMediaLoaded(media)) {
      throw new Error("createCanvasFromMedia - media has not finished loading yet");
    }
    const {width, height} = dims || getMediaDimensions(media);
    const canvas = createCanvas({width, height});
    if (media instanceof ImageData2) {
      getContext2dOrThrow(canvas).putImageData(media, 0, 0);
    } else {
      getContext2dOrThrow(canvas).drawImage(media, 0, 0, width, height);
    }
    return canvas;
  }

  // build/dom/imageTensorToCanvas.js
  async function imageTensorToCanvas(imgTensor, canvas) {
    const targetCanvas = canvas || env2.getEnv().createCanvasElement();
    const [height, width, numChannels] = imgTensor.shape.slice(isTensor4D(imgTensor) ? 1 : 0);
    const imgTensor3D = tidy(() => imgTensor.as3D(height, width, numChannels).toInt());
    await browser_exports.toPixels(imgTensor3D, targetCanvas);
    imgTensor3D.dispose();
    return targetCanvas;
  }

  // build/dom/isMediaElement.js
  function isMediaElement(input) {
    const {Image, Canvas, Video} = env2.getEnv();
    return input instanceof Image || input instanceof Canvas || input instanceof Video;
  }

  // build/dom/imageToSquare.js
  function imageToSquare(input, inputSize, centerImage = false) {
    const {Image, Canvas} = env2.getEnv();
    if (!(input instanceof Image || input instanceof Canvas)) {
      throw new Error("imageToSquare - expected arg0 to be HTMLImageElement | HTMLCanvasElement");
    }
    const dims = getMediaDimensions(input);
    const scale2 = inputSize / Math.max(dims.height, dims.width);
    const width = scale2 * dims.width;
    const height = scale2 * dims.height;
    const targetCanvas = createCanvas({width: inputSize, height: inputSize});
    const inputCanvas = input instanceof Canvas ? input : createCanvasFromMedia(input);
    const offset = Math.abs(width - height) / 2;
    const dx = centerImage && width < height ? offset : 0;
    const dy = centerImage && height < width ? offset : 0;
    getContext2dOrThrow(targetCanvas).drawImage(inputCanvas, dx, dy, width, height);
    return targetCanvas;
  }

  // build/dom/NetInput.js
  class NetInput {
    constructor(inputs, treatAsBatchInput = false) {
      this._imageTensors = [];
      this._canvases = [];
      this._treatAsBatchInput = false;
      this._inputDimensions = [];
      if (!Array.isArray(inputs)) {
        throw new Error(`NetInput.constructor - expected inputs to be an Array of TResolvedNetInput or to be instanceof tf.Tensor4D, instead have ${inputs}`);
      }
      this._treatAsBatchInput = treatAsBatchInput;
      this._batchSize = inputs.length;
      inputs.forEach((input, idx) => {
        if (isTensor3D(input)) {
          this._imageTensors[idx] = input;
          this._inputDimensions[idx] = input.shape;
          return;
        }
        if (isTensor4D(input)) {
          const batchSize = input.shape[0];
          if (batchSize !== 1) {
            throw new Error(`NetInput - tf.Tensor4D with batchSize ${batchSize} passed, but not supported in input array`);
          }
          this._imageTensors[idx] = input;
          this._inputDimensions[idx] = input.shape.slice(1);
          return;
        }
        const canvas = input instanceof env2.getEnv().Canvas ? input : createCanvasFromMedia(input);
        this._canvases[idx] = canvas;
        this._inputDimensions[idx] = [canvas.height, canvas.width, 3];
      });
    }
    get imageTensors() {
      return this._imageTensors;
    }
    get canvases() {
      return this._canvases;
    }
    get isBatchInput() {
      return this.batchSize > 1 || this._treatAsBatchInput;
    }
    get batchSize() {
      return this._batchSize;
    }
    get inputDimensions() {
      return this._inputDimensions;
    }
    get inputSize() {
      return this._inputSize;
    }
    get reshapedInputDimensions() {
      return range4(this.batchSize, 0, 1).map((_, batchIdx) => this.getReshapedInputDimensions(batchIdx));
    }
    getInput(batchIdx) {
      return this.canvases[batchIdx] || this.imageTensors[batchIdx];
    }
    getInputDimensions(batchIdx) {
      return this._inputDimensions[batchIdx];
    }
    getInputHeight(batchIdx) {
      return this._inputDimensions[batchIdx][0];
    }
    getInputWidth(batchIdx) {
      return this._inputDimensions[batchIdx][1];
    }
    getReshapedInputDimensions(batchIdx) {
      if (typeof this.inputSize !== "number") {
        throw new Error("getReshapedInputDimensions - inputSize not set, toBatchTensor has not been called yet");
      }
      const width = this.getInputWidth(batchIdx);
      const height = this.getInputHeight(batchIdx);
      return computeReshapedDimensions({width, height}, this.inputSize);
    }
    toBatchTensor(inputSize, isCenterInputs = true) {
      this._inputSize = inputSize;
      return tidy(() => {
        const inputTensors = range4(this.batchSize, 0, 1).map((batchIdx) => {
          const input = this.getInput(batchIdx);
          if (input instanceof Tensor) {
            let imgTensor = isTensor4D(input) ? input : input.expandDims();
            imgTensor = padToSquare(imgTensor, isCenterInputs);
            if (imgTensor.shape[1] !== inputSize || imgTensor.shape[2] !== inputSize) {
              imgTensor = image.resizeBilinear(imgTensor, [inputSize, inputSize]);
            }
            return imgTensor.as3D(inputSize, inputSize, 3);
          }
          if (input instanceof env2.getEnv().Canvas) {
            return browser_exports.fromPixels(imageToSquare(input, inputSize, isCenterInputs));
          }
          throw new Error(`toBatchTensor - at batchIdx ${batchIdx}, expected input to be instanceof tf.Tensor or instanceof HTMLCanvasElement, instead have ${input}`);
        });
        const batchTensor = stack(inputTensors.map((t) => t.toFloat())).as4D(this.batchSize, inputSize, inputSize, 3);
        return batchTensor;
      });
    }
  }

  // build/dom/toNetInput.js
  async function toNetInput(inputs) {
    if (inputs instanceof NetInput) {
      return inputs;
    }
    let inputArgArray = Array.isArray(inputs) ? inputs : [inputs];
    if (!inputArgArray.length) {
      throw new Error("toNetInput - empty array passed as input");
    }
    const getIdxHint = (idx) => Array.isArray(inputs) ? ` at input index ${idx}:` : "";
    const inputArray = inputArgArray.map(resolveInput);
    inputArray.forEach((input, i) => {
      if (!isMediaElement(input) && !isTensor3D(input) && !isTensor4D(input)) {
        if (typeof inputArgArray[i] === "string") {
          throw new Error(`toNetInput -${getIdxHint(i)} string passed, but could not resolve HTMLElement for element id ${inputArgArray[i]}`);
        }
        throw new Error(`toNetInput -${getIdxHint(i)} expected media to be of type HTMLImageElement | HTMLVideoElement | HTMLCanvasElement | tf.Tensor3D, or to be an element id`);
      }
      if (isTensor4D(input)) {
        const batchSize = input.shape[0];
        if (batchSize !== 1) {
          throw new Error(`toNetInput -${getIdxHint(i)} tf.Tensor4D with batchSize ${batchSize} passed, but not supported in input array`);
        }
      }
    });
    await Promise.all(inputArray.map((input) => isMediaElement(input) && awaitMediaLoaded(input)));
    return new NetInput(inputArray, Array.isArray(inputs));
  }

  // build/dom/extractFaces.js
  async function extractFaces(input, detections) {
    const {Canvas} = env2.getEnv();
    let canvas = input;
    if (!(input instanceof Canvas)) {
      const netInput = await toNetInput(input);
      if (netInput.batchSize > 1) {
        throw new Error("extractFaces - batchSize > 1 not supported");
      }
      const tensorOrCanvas = netInput.getInput(0);
      canvas = tensorOrCanvas instanceof Canvas ? tensorOrCanvas : await imageTensorToCanvas(tensorOrCanvas);
    }
    const ctx = getContext2dOrThrow(canvas);
    const boxes = detections.map((det) => det instanceof FaceDetection ? det.forSize(canvas.width, canvas.height).box.floor() : det).map((box) => box.clipAtImageBorders(canvas.width, canvas.height));
    return boxes.map(({x, y, width, height}) => {
      const faceImg = createCanvas({width, height});
      getContext2dOrThrow(faceImg).putImageData(ctx.getImageData(x, y, width, height), 0, 0);
      return faceImg;
    });
  }

  // build/dom/extractFaceTensors.js
  async function extractFaceTensors(imageTensor, detections) {
    if (!isTensor3D(imageTensor) && !isTensor4D(imageTensor)) {
      throw new Error("extractFaceTensors - expected image tensor to be 3D or 4D");
    }
    if (isTensor4D(imageTensor) && imageTensor.shape[0] > 1) {
      throw new Error("extractFaceTensors - batchSize > 1 not supported");
    }
    return tidy(() => {
      const [imgHeight, imgWidth, numChannels] = imageTensor.shape.slice(isTensor4D(imageTensor) ? 1 : 0);
      const boxes = detections.map((det) => det instanceof FaceDetection ? det.forSize(imgWidth, imgHeight).box : det).map((box) => box.clipAtImageBorders(imgWidth, imgHeight));
      const faceTensors = boxes.map(({x, y, width, height}) => slice3d(imageTensor.as3D(imgHeight, imgWidth, numChannels), [y, x, 0], [height, width, numChannels]));
      return faceTensors;
    });
  }

  // build/dom/fetchOrThrow.js
  async function fetchOrThrow(url, init) {
    const fetch3 = env2.getEnv().fetch;
    const res = await fetch3(url, init);
    if (!(res.status < 400)) {
      throw new Error(`failed to fetch: (${res.status}) ${res.statusText}, from url: ${res.url}`);
    }
    return res;
  }

  // build/dom/fetchImage.js
  async function fetchImage(uri) {
    const res = await fetchOrThrow(uri);
    const blob = await res.blob();
    if (!blob.type.startsWith("image/")) {
      throw new Error(`fetchImage - expected blob type to be of type image/*, instead have: ${blob.type}, for url: ${res.url}`);
    }
    return bufferToImage(blob);
  }

  // build/dom/fetchJson.js
  async function fetchJson(uri) {
    return (await fetchOrThrow(uri)).json();
  }

  // build/dom/fetchNetWeights.js
  async function fetchNetWeights(uri) {
    return new Float32Array(await (await fetchOrThrow(uri)).arrayBuffer());
  }

  // build/common/getModelUris.js
  function getModelUris(uri, defaultModelName) {
    const defaultManifestFilename = `${defaultModelName}-weights_manifest.json`;
    if (!uri) {
      return {
        modelBaseUri: "",
        manifestUri: defaultManifestFilename
      };
    }
    if (uri === "/") {
      return {
        modelBaseUri: "/",
        manifestUri: `/${defaultManifestFilename}`
      };
    }
    const protocol = uri.startsWith("http://") ? "http://" : uri.startsWith("https://") ? "https://" : "";
    uri = uri.replace(protocol, "");
    const parts = uri.split("/").filter((s) => s);
    const manifestFile = uri.endsWith(".json") ? parts[parts.length - 1] : defaultManifestFilename;
    let modelBaseUri = protocol + (uri.endsWith(".json") ? parts.slice(0, parts.length - 1) : parts).join("/");
    modelBaseUri = uri.startsWith("/") ? `/${modelBaseUri}` : modelBaseUri;
    return {
      modelBaseUri,
      manifestUri: modelBaseUri === "/" ? `/${manifestFile}` : `${modelBaseUri}/${manifestFile}`
    };
  }

  // build/dom/loadWeightMap.js
  async function loadWeightMap(uri, defaultModelName) {
    const {manifestUri, modelBaseUri} = getModelUris(uri, defaultModelName);
    let manifest = await fetchJson(manifestUri);
    return io_exports.loadWeights(manifest, modelBaseUri);
  }

  // build/dom/matchDimensions.js
  function matchDimensions(input, reference, useMediaDimensions = false) {
    const {width, height} = useMediaDimensions ? getMediaDimensions(reference) : reference;
    input.width = width;
    input.height = height;
    return {width, height};
  }

  // build/dom/types.js

  // build/dom/index.js

  // build/NeuralNetwork.js
  class NeuralNetwork {
    constructor(_name) {
      this._name = _name;
      this._params = void 0;
      this._paramMappings = [];
    }
    get params() {
      return this._params;
    }
    get paramMappings() {
      return this._paramMappings;
    }
    get isLoaded() {
      return !!this.params;
    }
    getParamFromPath(paramPath) {
      const {obj, objProp} = this.traversePropertyPath(paramPath);
      return obj[objProp];
    }
    reassignParamFromPath(paramPath, tensor17) {
      const {obj, objProp} = this.traversePropertyPath(paramPath);
      obj[objProp].dispose();
      obj[objProp] = tensor17;
    }
    getParamList() {
      return this._paramMappings.map(({paramPath}) => ({
        path: paramPath,
        tensor: this.getParamFromPath(paramPath)
      }));
    }
    getTrainableParams() {
      return this.getParamList().filter((param) => param.tensor instanceof Variable);
    }
    getFrozenParams() {
      return this.getParamList().filter((param) => !(param.tensor instanceof Variable));
    }
    variable() {
      this.getFrozenParams().forEach(({path, tensor: tensor17}) => {
        this.reassignParamFromPath(path, tensor17.variable());
      });
    }
    freeze() {
      this.getTrainableParams().forEach(({path, tensor: variable3}) => {
        const tensor17 = tensor5(variable3.dataSync());
        variable3.dispose();
        this.reassignParamFromPath(path, tensor17);
      });
    }
    dispose(throwOnRedispose = true) {
      this.getParamList().forEach((param) => {
        if (throwOnRedispose && param.tensor.isDisposed) {
          throw new Error(`param tensor has already been disposed for path ${param.path}`);
        }
        param.tensor.dispose();
      });
      this._params = void 0;
    }
    serializeParams() {
      return new Float32Array(this.getParamList().map(({tensor: tensor17}) => Array.from(tensor17.dataSync())).reduce((flat, arr) => flat.concat(arr)));
    }
    async load(weightsOrUrl) {
      if (weightsOrUrl instanceof Float32Array) {
        this.extractWeights(weightsOrUrl);
        return;
      }
      await this.loadFromUri(weightsOrUrl);
    }
    async loadFromUri(uri) {
      if (uri && typeof uri !== "string") {
        throw new Error(`${this._name}.loadFromUri - expected model uri`);
      }
      const weightMap = await loadWeightMap(uri, this.getDefaultModelName());
      this.loadFromWeightMap(weightMap);
    }
    async loadFromDisk(filePath) {
      if (filePath && typeof filePath !== "string") {
        throw new Error(`${this._name}.loadFromDisk - expected model file path`);
      }
      const {readFile} = env2.getEnv();
      const {manifestUri, modelBaseUri} = getModelUris(filePath, this.getDefaultModelName());
      const fetchWeightsFromDisk = (filePaths) => Promise.all(filePaths.map((filePath2) => readFile(filePath2).then((buf) => buf.buffer)));
      const loadWeights2 = io_exports.weightsLoaderFactory(fetchWeightsFromDisk);
      const manifest = JSON.parse((await readFile(manifestUri)).toString());
      const weightMap = await loadWeights2(manifest, modelBaseUri);
      this.loadFromWeightMap(weightMap);
    }
    loadFromWeightMap(weightMap) {
      const {paramMappings, params} = this.extractParamsFromWeigthMap(weightMap);
      this._paramMappings = paramMappings;
      this._params = params;
    }
    extractWeights(weights) {
      const {paramMappings, params} = this.extractParams(weights);
      this._paramMappings = paramMappings;
      this._params = params;
    }
    traversePropertyPath(paramPath) {
      if (!this.params) {
        throw new Error(`traversePropertyPath - model has no loaded params`);
      }
      const result = paramPath.split("/").reduce((res, objProp2) => {
        if (!res.nextObj.hasOwnProperty(objProp2)) {
          throw new Error(`traversePropertyPath - object does not have property ${objProp2}, for path ${paramPath}`);
        }
        return {obj: res.nextObj, objProp: objProp2, nextObj: res.nextObj[objProp2]};
      }, {nextObj: this.params});
      const {obj, objProp} = result;
      if (!obj || !objProp || !(obj[objProp] instanceof Tensor)) {
        throw new Error(`traversePropertyPath - parameter is not a tensor, for path ${paramPath}`);
      }
      return {obj, objProp};
    }
  }

  // build/common/depthwiseSeparableConv.js
  function depthwiseSeparableConv(x, params, stride) {
    return tidy(() => {
      let out = separableConv2d(x, params.depthwise_filter, params.pointwise_filter, stride, "same");
      out = add2(out, params.bias);
      return out;
    });
  }

  // build/faceFeatureExtractor/denseBlock.js
  function denseBlock3(x, denseBlockParams, isFirstLayer = false) {
    return tidy(() => {
      const out1 = relu(isFirstLayer ? add2(conv2d(x, denseBlockParams.conv0.filters, [2, 2], "same"), denseBlockParams.conv0.bias) : depthwiseSeparableConv(x, denseBlockParams.conv0, [2, 2]));
      const out2 = depthwiseSeparableConv(out1, denseBlockParams.conv1, [1, 1]);
      const in3 = relu(add2(out1, out2));
      const out3 = depthwiseSeparableConv(in3, denseBlockParams.conv2, [1, 1]);
      return relu(add2(out1, add2(out2, out3)));
    });
  }
  function denseBlock4(x, denseBlockParams, isFirstLayer = false, isScaleDown = true) {
    return tidy(() => {
      const out1 = relu(isFirstLayer ? add2(conv2d(x, denseBlockParams.conv0.filters, isScaleDown ? [2, 2] : [1, 1], "same"), denseBlockParams.conv0.bias) : depthwiseSeparableConv(x, denseBlockParams.conv0, isScaleDown ? [2, 2] : [1, 1]));
      const out2 = depthwiseSeparableConv(out1, denseBlockParams.conv1, [1, 1]);
      const in3 = relu(add2(out1, out2));
      const out3 = depthwiseSeparableConv(in3, denseBlockParams.conv2, [1, 1]);
      const in4 = relu(add2(out1, add2(out2, out3)));
      const out4 = depthwiseSeparableConv(in4, denseBlockParams.conv3, [1, 1]);
      return relu(add2(out1, add2(out2, add2(out3, out4))));
    });
  }

  // build/common/convLayer.js
  function convLayer(x, params, padding = "same", withRelu = false) {
    return tidy(() => {
      const out = add2(conv2d(x, params.filters, [1, 1], padding), params.bias);
      return withRelu ? relu(out) : out;
    });
  }

  // build/common/disposeUnusedWeightTensors.js
  function disposeUnusedWeightTensors(weightMap, paramMappings) {
    Object.keys(weightMap).forEach((path) => {
      if (!paramMappings.some((pm) => pm.originalPath === path)) {
        weightMap[path].dispose();
      }
    });
  }

  // build/common/extractConvParamsFactory.js
  function extractConvParamsFactory(extractWeights, paramMappings) {
    return function(channelsIn, channelsOut, filterSize, mappedPrefix) {
      const filters = tensor4d(extractWeights(channelsIn * channelsOut * filterSize * filterSize), [filterSize, filterSize, channelsIn, channelsOut]);
      const bias = tensor1d(extractWeights(channelsOut));
      paramMappings.push({paramPath: `${mappedPrefix}/filters`}, {paramPath: `${mappedPrefix}/bias`});
      return {filters, bias};
    };
  }

  // build/common/extractFCParamsFactory.js
  function extractFCParamsFactory(extractWeights, paramMappings) {
    return function(channelsIn, channelsOut, mappedPrefix) {
      const fc_weights = tensor2d(extractWeights(channelsIn * channelsOut), [channelsIn, channelsOut]);
      const fc_bias = tensor1d(extractWeights(channelsOut));
      paramMappings.push({paramPath: `${mappedPrefix}/weights`}, {paramPath: `${mappedPrefix}/bias`});
      return {
        weights: fc_weights,
        bias: fc_bias
      };
    };
  }

  // build/common/types.js
  class SeparableConvParams {
    constructor(depthwise_filter, pointwise_filter, bias) {
      this.depthwise_filter = depthwise_filter;
      this.pointwise_filter = pointwise_filter;
      this.bias = bias;
    }
  }

  // build/common/extractSeparableConvParamsFactory.js
  function extractSeparableConvParamsFactory(extractWeights, paramMappings) {
    return function(channelsIn, channelsOut, mappedPrefix) {
      const depthwise_filter = tensor4d(extractWeights(3 * 3 * channelsIn), [3, 3, channelsIn, 1]);
      const pointwise_filter = tensor4d(extractWeights(channelsIn * channelsOut), [1, 1, channelsIn, channelsOut]);
      const bias = tensor1d(extractWeights(channelsOut));
      paramMappings.push({paramPath: `${mappedPrefix}/depthwise_filter`}, {paramPath: `${mappedPrefix}/pointwise_filter`}, {paramPath: `${mappedPrefix}/bias`});
      return new SeparableConvParams(depthwise_filter, pointwise_filter, bias);
    };
  }
  function loadSeparableConvParamsFactory(extractWeightEntry) {
    return function(prefix) {
      const depthwise_filter = extractWeightEntry(`${prefix}/depthwise_filter`, 4);
      const pointwise_filter = extractWeightEntry(`${prefix}/pointwise_filter`, 4);
      const bias = extractWeightEntry(`${prefix}/bias`, 1);
      return new SeparableConvParams(depthwise_filter, pointwise_filter, bias);
    };
  }

  // build/common/extractWeightEntryFactory.js
  function extractWeightEntryFactory(weightMap, paramMappings) {
    return function(originalPath, paramRank, mappedPath) {
      const tensor17 = weightMap[originalPath];
      if (!isTensor(tensor17, paramRank)) {
        throw new Error(`expected weightMap[${originalPath}] to be a Tensor${paramRank}D, instead have ${tensor17}`);
      }
      paramMappings.push({originalPath, paramPath: mappedPath || originalPath});
      return tensor17;
    };
  }

  // build/common/extractWeightsFactory.js
  function extractWeightsFactory(weights) {
    let remainingWeights = weights;
    function extractWeights(numWeights) {
      const ret = remainingWeights.slice(0, numWeights);
      remainingWeights = remainingWeights.slice(numWeights);
      return ret;
    }
    function getRemainingWeights() {
      return remainingWeights;
    }
    return {
      extractWeights,
      getRemainingWeights
    };
  }

  // build/common/index.js

  // build/faceFeatureExtractor/extractorsFactory.js
  function extractorsFactory(extractWeights, paramMappings) {
    const extractConvParams = extractConvParamsFactory(extractWeights, paramMappings);
    const extractSeparableConvParams = extractSeparableConvParamsFactory(extractWeights, paramMappings);
    function extractDenseBlock3Params(channelsIn, channelsOut, mappedPrefix, isFirstLayer = false) {
      const conv0 = isFirstLayer ? extractConvParams(channelsIn, channelsOut, 3, `${mappedPrefix}/conv0`) : extractSeparableConvParams(channelsIn, channelsOut, `${mappedPrefix}/conv0`);
      const conv1 = extractSeparableConvParams(channelsOut, channelsOut, `${mappedPrefix}/conv1`);
      const conv22 = extractSeparableConvParams(channelsOut, channelsOut, `${mappedPrefix}/conv2`);
      return {conv0, conv1, conv2: conv22};
    }
    function extractDenseBlock4Params(channelsIn, channelsOut, mappedPrefix, isFirstLayer = false) {
      const {conv0, conv1, conv2: conv22} = extractDenseBlock3Params(channelsIn, channelsOut, mappedPrefix, isFirstLayer);
      const conv3 = extractSeparableConvParams(channelsOut, channelsOut, `${mappedPrefix}/conv3`);
      return {conv0, conv1, conv2: conv22, conv3};
    }
    return {
      extractDenseBlock3Params,
      extractDenseBlock4Params
    };
  }

  // build/faceFeatureExtractor/extractParams.js
  function extractParams(weights) {
    const paramMappings = [];
    const {extractWeights, getRemainingWeights} = extractWeightsFactory(weights);
    const {extractDenseBlock4Params} = extractorsFactory(extractWeights, paramMappings);
    const dense0 = extractDenseBlock4Params(3, 32, "dense0", true);
    const dense1 = extractDenseBlock4Params(32, 64, "dense1");
    const dense2 = extractDenseBlock4Params(64, 128, "dense2");
    const dense3 = extractDenseBlock4Params(128, 256, "dense3");
    if (getRemainingWeights().length !== 0) {
      throw new Error(`weights remaing after extract: ${getRemainingWeights().length}`);
    }
    return {
      paramMappings,
      params: {dense0, dense1, dense2, dense3}
    };
  }

  // build/common/loadConvParamsFactory.js
  function loadConvParamsFactory(extractWeightEntry) {
    return function(prefix) {
      const filters = extractWeightEntry(`${prefix}/filters`, 4);
      const bias = extractWeightEntry(`${prefix}/bias`, 1);
      return {filters, bias};
    };
  }

  // build/faceFeatureExtractor/loadParamsFactory.js
  function loadParamsFactory(weightMap, paramMappings) {
    const extractWeightEntry = extractWeightEntryFactory(weightMap, paramMappings);
    const extractConvParams = loadConvParamsFactory(extractWeightEntry);
    const extractSeparableConvParams = loadSeparableConvParamsFactory(extractWeightEntry);
    function extractDenseBlock3Params(prefix, isFirstLayer = false) {
      const conv0 = isFirstLayer ? extractConvParams(`${prefix}/conv0`) : extractSeparableConvParams(`${prefix}/conv0`);
      const conv1 = extractSeparableConvParams(`${prefix}/conv1`);
      const conv22 = extractSeparableConvParams(`${prefix}/conv2`);
      return {conv0, conv1, conv2: conv22};
    }
    function extractDenseBlock4Params(prefix, isFirstLayer = false) {
      const conv0 = isFirstLayer ? extractConvParams(`${prefix}/conv0`) : extractSeparableConvParams(`${prefix}/conv0`);
      const conv1 = extractSeparableConvParams(`${prefix}/conv1`);
      const conv22 = extractSeparableConvParams(`${prefix}/conv2`);
      const conv3 = extractSeparableConvParams(`${prefix}/conv3`);
      return {conv0, conv1, conv2: conv22, conv3};
    }
    return {
      extractDenseBlock3Params,
      extractDenseBlock4Params
    };
  }

  // build/faceFeatureExtractor/extractParamsFromWeigthMap.js
  function extractParamsFromWeigthMap(weightMap) {
    const paramMappings = [];
    const {extractDenseBlock4Params} = loadParamsFactory(weightMap, paramMappings);
    const params = {
      dense0: extractDenseBlock4Params("dense0", true),
      dense1: extractDenseBlock4Params("dense1"),
      dense2: extractDenseBlock4Params("dense2"),
      dense3: extractDenseBlock4Params("dense3")
    };
    disposeUnusedWeightTensors(weightMap, paramMappings);
    return {params, paramMappings};
  }

  // build/faceFeatureExtractor/FaceFeatureExtractor.js
  class FaceFeatureExtractor extends NeuralNetwork {
    constructor() {
      super("FaceFeatureExtractor");
    }
    forwardInput(input) {
      const {params} = this;
      if (!params) {
        throw new Error("FaceFeatureExtractor - load model before inference");
      }
      return tidy(() => {
        const batchTensor = input.toBatchTensor(112, true);
        const meanRgb = [122.782, 117.001, 104.298];
        const normalized = normalize(batchTensor, meanRgb).div(scalar(255));
        let out = denseBlock4(normalized, params.dense0, true);
        out = denseBlock4(out, params.dense1);
        out = denseBlock4(out, params.dense2);
        out = denseBlock4(out, params.dense3);
        out = avgPool(out, [7, 7], [2, 2], "valid");
        return out;
      });
    }
    async forward(input) {
      return this.forwardInput(await toNetInput(input));
    }
    getDefaultModelName() {
      return "face_feature_extractor_model";
    }
    extractParamsFromWeigthMap(weightMap) {
      return extractParamsFromWeigthMap(weightMap);
    }
    extractParams(weights) {
      return extractParams(weights);
    }
  }

  // build/common/fullyConnectedLayer.js
  function fullyConnectedLayer(x, params) {
    return tidy(() => add2(matMul(x, params.weights), params.bias));
  }

  // build/faceProcessor/extractParams.js
  function extractParams3(weights, channelsIn, channelsOut) {
    const paramMappings = [];
    const {extractWeights, getRemainingWeights} = extractWeightsFactory(weights);
    const extractFCParams = extractFCParamsFactory(extractWeights, paramMappings);
    const fc = extractFCParams(channelsIn, channelsOut, "fc");
    if (getRemainingWeights().length !== 0) {
      throw new Error(`weights remaing after extract: ${getRemainingWeights().length}`);
    }
    return {
      paramMappings,
      params: {fc}
    };
  }

  // build/faceProcessor/extractParamsFromWeigthMap.js
  function extractParamsFromWeigthMap3(weightMap) {
    const paramMappings = [];
    const extractWeightEntry = extractWeightEntryFactory(weightMap, paramMappings);
    function extractFcParams(prefix) {
      const weights = extractWeightEntry(`${prefix}/weights`, 2);
      const bias = extractWeightEntry(`${prefix}/bias`, 1);
      return {weights, bias};
    }
    const params = {
      fc: extractFcParams("fc")
    };
    disposeUnusedWeightTensors(weightMap, paramMappings);
    return {params, paramMappings};
  }

  // build/faceProcessor/util.js
  function seperateWeightMaps(weightMap) {
    const featureExtractorMap = {};
    const classifierMap = {};
    Object.keys(weightMap).forEach((key) => {
      const map = key.startsWith("fc") ? classifierMap : featureExtractorMap;
      map[key] = weightMap[key];
    });
    return {featureExtractorMap, classifierMap};
  }

  // build/faceProcessor/FaceProcessor.js
  class FaceProcessor extends NeuralNetwork {
    constructor(_name, faceFeatureExtractor) {
      super(_name);
      this._faceFeatureExtractor = faceFeatureExtractor;
    }
    get faceFeatureExtractor() {
      return this._faceFeatureExtractor;
    }
    runNet(input) {
      const {params} = this;
      if (!params) {
        throw new Error(`${this._name} - load model before inference`);
      }
      return tidy(() => {
        const bottleneckFeatures = input instanceof NetInput ? this.faceFeatureExtractor.forwardInput(input) : input;
        return fullyConnectedLayer(bottleneckFeatures.as2D(bottleneckFeatures.shape[0], -1), params.fc);
      });
    }
    dispose(throwOnRedispose = true) {
      this.faceFeatureExtractor.dispose(throwOnRedispose);
      super.dispose(throwOnRedispose);
    }
    loadClassifierParams(weights) {
      const {params, paramMappings} = this.extractClassifierParams(weights);
      this._params = params;
      this._paramMappings = paramMappings;
    }
    extractClassifierParams(weights) {
      return extractParams3(weights, this.getClassifierChannelsIn(), this.getClassifierChannelsOut());
    }
    extractParamsFromWeigthMap(weightMap) {
      const {featureExtractorMap, classifierMap} = seperateWeightMaps(weightMap);
      this.faceFeatureExtractor.loadFromWeightMap(featureExtractorMap);
      return extractParamsFromWeigthMap3(classifierMap);
    }
    extractParams(weights) {
      const cIn = this.getClassifierChannelsIn();
      const cOut = this.getClassifierChannelsOut();
      const classifierWeightSize = cOut * cIn + cOut;
      const featureExtractorWeights = weights.slice(0, weights.length - classifierWeightSize);
      const classifierWeights = weights.slice(weights.length - classifierWeightSize);
      this.faceFeatureExtractor.extractWeights(featureExtractorWeights);
      return this.extractClassifierParams(classifierWeights);
    }
  }

  // build/faceExpressionNet/FaceExpressions.js
  const FACE_EXPRESSION_LABELS = ["neutral", "happy", "sad", "angry", "fearful", "disgusted", "surprised"];
  class FaceExpressions {
    constructor(probabilities) {
      if (probabilities.length !== 7) {
        throw new Error(`FaceExpressions.constructor - expected probabilities.length to be 7, have: ${probabilities.length}`);
      }
      FACE_EXPRESSION_LABELS.forEach((expression, idx) => {
        this[expression] = probabilities[idx];
      });
    }
    asSortedArray() {
      return FACE_EXPRESSION_LABELS.map((expression) => ({expression, probability: this[expression]})).sort((e0, e1) => e1.probability - e0.probability);
    }
  }

  // build/faceExpressionNet/FaceExpressionNet.js
  class FaceExpressionNet extends FaceProcessor {
    constructor(faceFeatureExtractor = new FaceFeatureExtractor()) {
      super("FaceExpressionNet", faceFeatureExtractor);
    }
    forwardInput(input) {
      return tidy(() => softmax(this.runNet(input)));
    }
    async forward(input) {
      return this.forwardInput(await toNetInput(input));
    }
    async predictExpressions(input) {
      const netInput = await toNetInput(input);
      const out = await this.forwardInput(netInput);
      const probabilitesByBatch = await Promise.all(unstack(out).map(async (t) => {
        const data = await t.data();
        t.dispose();
        return data;
      }));
      out.dispose();
      const predictionsByBatch = probabilitesByBatch.map((probabilites) => new FaceExpressions(probabilites));
      return netInput.isBatchInput ? predictionsByBatch : predictionsByBatch[0];
    }
    getDefaultModelName() {
      return "face_expression_model";
    }
    getClassifierChannelsIn() {
      return 256;
    }
    getClassifierChannelsOut() {
      return 7;
    }
  }

  // build/faceExpressionNet/index.js

  // build/factories/WithFaceExpressions.js
  function isWithFaceExpressions(obj) {
    return obj["expressions"] instanceof FaceExpressions;
  }
  function extendWithFaceExpressions(sourceObj, expressions) {
    const extension = {expressions};
    return Object.assign({}, sourceObj, extension);
  }

  // build/draw/drawFaceExpressions.js
  function drawFaceExpressions(canvasArg, faceExpressions, minConfidence = 0.1, textFieldAnchor) {
    const faceExpressionsArray = Array.isArray(faceExpressions) ? faceExpressions : [faceExpressions];
    faceExpressionsArray.forEach((e) => {
      const expr = e instanceof FaceExpressions ? e : isWithFaceExpressions(e) ? e.expressions : void 0;
      if (!expr) {
        throw new Error("drawFaceExpressions - expected faceExpressions to be FaceExpressions | WithFaceExpressions<{}> or array thereof");
      }
      const sorted = expr.asSortedArray();
      const resultsToDisplay = sorted.filter((expr2) => expr2.probability > minConfidence);
      const anchor = isWithFaceDetection(e) ? e.detection.box.bottomLeft : textFieldAnchor || new Point(0, 0);
      const drawTextField = new DrawTextField(resultsToDisplay.map((expr2) => `${expr2.expression} (${round3(expr2.probability)})`), anchor);
      drawTextField.draw(canvasArg);
    });
  }

  // build/factories/WithFaceLandmarks.js
  function isWithFaceLandmarks(obj) {
    return isWithFaceDetection(obj) && obj["landmarks"] instanceof FaceLandmarks && obj["unshiftedLandmarks"] instanceof FaceLandmarks && obj["alignedRect"] instanceof FaceDetection;
  }
  function extendWithFaceLandmarks(sourceObj, unshiftedLandmarks) {
    const {box: shift} = sourceObj.detection;
    const landmarks = unshiftedLandmarks.shiftBy(shift.x, shift.y);
    const rect = landmarks.align();
    const {imageDims} = sourceObj.detection;
    const alignedRect = new FaceDetection(sourceObj.detection.score, rect.rescale(imageDims.reverse()), imageDims);
    const extension = {
      landmarks,
      unshiftedLandmarks,
      alignedRect
    };
    return Object.assign({}, sourceObj, extension);
  }

  // build/draw/DrawFaceLandmarks.js
  class DrawFaceLandmarksOptions {
    constructor(options = {}) {
      const {drawLines = true, drawPoints = true, lineWidth, lineColor, pointSize, pointColor} = options;
      this.drawLines = drawLines;
      this.drawPoints = drawPoints;
      this.lineWidth = lineWidth || 1;
      this.pointSize = pointSize || 2;
      this.lineColor = lineColor || "rgba(0, 255, 255, 1)";
      this.pointColor = pointColor || "rgba(255, 0, 255, 1)";
    }
  }
  class DrawFaceLandmarks {
    constructor(faceLandmarks, options = {}) {
      this.faceLandmarks = faceLandmarks;
      this.options = new DrawFaceLandmarksOptions(options);
    }
    draw(canvasArg) {
      const ctx = getContext2dOrThrow(canvasArg);
      const {drawLines, drawPoints, lineWidth, lineColor, pointSize, pointColor} = this.options;
      if (drawLines && this.faceLandmarks instanceof FaceLandmarks68) {
        ctx.strokeStyle = lineColor;
        ctx.lineWidth = lineWidth;
        drawContour(ctx, this.faceLandmarks.getJawOutline());
        drawContour(ctx, this.faceLandmarks.getLeftEyeBrow());
        drawContour(ctx, this.faceLandmarks.getRightEyeBrow());
        drawContour(ctx, this.faceLandmarks.getNose());
        drawContour(ctx, this.faceLandmarks.getLeftEye(), true);
        drawContour(ctx, this.faceLandmarks.getRightEye(), true);
        drawContour(ctx, this.faceLandmarks.getMouth(), true);
      }
      if (drawPoints) {
        ctx.strokeStyle = pointColor;
        ctx.fillStyle = pointColor;
        const drawPoint = (pt) => {
          ctx.beginPath();
          ctx.arc(pt.x, pt.y, pointSize, 0, 2 * Math.PI);
          ctx.fill();
        };
        this.faceLandmarks.positions.forEach(drawPoint);
      }
    }
  }
  function drawFaceLandmarks(canvasArg, faceLandmarks) {
    const faceLandmarksArray = Array.isArray(faceLandmarks) ? faceLandmarks : [faceLandmarks];
    faceLandmarksArray.forEach((f) => {
      const landmarks = f instanceof FaceLandmarks ? f : isWithFaceLandmarks(f) ? f.landmarks : void 0;
      if (!landmarks) {
        throw new Error("drawFaceLandmarks - expected faceExpressions to be FaceLandmarks | WithFaceLandmarks<WithFaceDetection<{}>> or array thereof");
      }
      new DrawFaceLandmarks(landmarks).draw(canvasArg);
    });
  }

  // build/draw/index.js
  const draw_exports = {};
  __export(draw_exports, {
    AnchorPosition: () => AnchorPosition,
    DrawBox: () => DrawBox,
    DrawBoxOptions: () => DrawBoxOptions,
    DrawFaceLandmarks: () => DrawFaceLandmarks,
    DrawFaceLandmarksOptions: () => DrawFaceLandmarksOptions,
    DrawTextField: () => DrawTextField,
    DrawTextFieldOptions: () => DrawTextFieldOptions,
    drawContour: () => drawContour,
    drawDetections: () => drawDetections,
    drawFaceExpressions: () => drawFaceExpressions,
    drawFaceLandmarks: () => drawFaceLandmarks
  });

  // build/xception/extractParams.js
  function extractorsFactory3(extractWeights, paramMappings) {
    const extractConvParams = extractConvParamsFactory(extractWeights, paramMappings);
    const extractSeparableConvParams = extractSeparableConvParamsFactory(extractWeights, paramMappings);
    function extractReductionBlockParams(channelsIn, channelsOut, mappedPrefix) {
      const separable_conv0 = extractSeparableConvParams(channelsIn, channelsOut, `${mappedPrefix}/separable_conv0`);
      const separable_conv1 = extractSeparableConvParams(channelsOut, channelsOut, `${mappedPrefix}/separable_conv1`);
      const expansion_conv = extractConvParams(channelsIn, channelsOut, 1, `${mappedPrefix}/expansion_conv`);
      return {separable_conv0, separable_conv1, expansion_conv};
    }
    function extractMainBlockParams(channels, mappedPrefix) {
      const separable_conv0 = extractSeparableConvParams(channels, channels, `${mappedPrefix}/separable_conv0`);
      const separable_conv1 = extractSeparableConvParams(channels, channels, `${mappedPrefix}/separable_conv1`);
      const separable_conv2 = extractSeparableConvParams(channels, channels, `${mappedPrefix}/separable_conv2`);
      return {separable_conv0, separable_conv1, separable_conv2};
    }
    return {
      extractConvParams,
      extractSeparableConvParams,
      extractReductionBlockParams,
      extractMainBlockParams
    };
  }
  function extractParams5(weights, numMainBlocks) {
    const paramMappings = [];
    const {extractWeights, getRemainingWeights} = extractWeightsFactory(weights);
    const {extractConvParams, extractSeparableConvParams, extractReductionBlockParams, extractMainBlockParams} = extractorsFactory3(extractWeights, paramMappings);
    const entry_flow_conv_in = extractConvParams(3, 32, 3, "entry_flow/conv_in");
    const entry_flow_reduction_block_0 = extractReductionBlockParams(32, 64, "entry_flow/reduction_block_0");
    const entry_flow_reduction_block_1 = extractReductionBlockParams(64, 128, "entry_flow/reduction_block_1");
    const entry_flow = {
      conv_in: entry_flow_conv_in,
      reduction_block_0: entry_flow_reduction_block_0,
      reduction_block_1: entry_flow_reduction_block_1
    };
    const middle_flow = {};
    range4(numMainBlocks, 0, 1).forEach((idx) => {
      middle_flow[`main_block_${idx}`] = extractMainBlockParams(128, `middle_flow/main_block_${idx}`);
    });
    const exit_flow_reduction_block = extractReductionBlockParams(128, 256, "exit_flow/reduction_block");
    const exit_flow_separable_conv = extractSeparableConvParams(256, 512, "exit_flow/separable_conv");
    const exit_flow = {
      reduction_block: exit_flow_reduction_block,
      separable_conv: exit_flow_separable_conv
    };
    if (getRemainingWeights().length !== 0) {
      throw new Error(`weights remaing after extract: ${getRemainingWeights().length}`);
    }
    return {
      paramMappings,
      params: {entry_flow, middle_flow, exit_flow}
    };
  }

  // build/xception/extractParamsFromWeigthMap.js
  function loadParamsFactory3(weightMap, paramMappings) {
    const extractWeightEntry = extractWeightEntryFactory(weightMap, paramMappings);
    const extractConvParams = loadConvParamsFactory(extractWeightEntry);
    const extractSeparableConvParams = loadSeparableConvParamsFactory(extractWeightEntry);
    function extractReductionBlockParams(mappedPrefix) {
      const separable_conv0 = extractSeparableConvParams(`${mappedPrefix}/separable_conv0`);
      const separable_conv1 = extractSeparableConvParams(`${mappedPrefix}/separable_conv1`);
      const expansion_conv = extractConvParams(`${mappedPrefix}/expansion_conv`);
      return {separable_conv0, separable_conv1, expansion_conv};
    }
    function extractMainBlockParams(mappedPrefix) {
      const separable_conv0 = extractSeparableConvParams(`${mappedPrefix}/separable_conv0`);
      const separable_conv1 = extractSeparableConvParams(`${mappedPrefix}/separable_conv1`);
      const separable_conv2 = extractSeparableConvParams(`${mappedPrefix}/separable_conv2`);
      return {separable_conv0, separable_conv1, separable_conv2};
    }
    return {
      extractConvParams,
      extractSeparableConvParams,
      extractReductionBlockParams,
      extractMainBlockParams
    };
  }
  function extractParamsFromWeigthMap5(weightMap, numMainBlocks) {
    const paramMappings = [];
    const {extractConvParams, extractSeparableConvParams, extractReductionBlockParams, extractMainBlockParams} = loadParamsFactory3(weightMap, paramMappings);
    const entry_flow_conv_in = extractConvParams("entry_flow/conv_in");
    const entry_flow_reduction_block_0 = extractReductionBlockParams("entry_flow/reduction_block_0");
    const entry_flow_reduction_block_1 = extractReductionBlockParams("entry_flow/reduction_block_1");
    const entry_flow = {
      conv_in: entry_flow_conv_in,
      reduction_block_0: entry_flow_reduction_block_0,
      reduction_block_1: entry_flow_reduction_block_1
    };
    const middle_flow = {};
    range4(numMainBlocks, 0, 1).forEach((idx) => {
      middle_flow[`main_block_${idx}`] = extractMainBlockParams(`middle_flow/main_block_${idx}`);
    });
    const exit_flow_reduction_block = extractReductionBlockParams("exit_flow/reduction_block");
    const exit_flow_separable_conv = extractSeparableConvParams("exit_flow/separable_conv");
    const exit_flow = {
      reduction_block: exit_flow_reduction_block,
      separable_conv: exit_flow_separable_conv
    };
    disposeUnusedWeightTensors(weightMap, paramMappings);
    return {params: {entry_flow, middle_flow, exit_flow}, paramMappings};
  }

  // build/xception/TinyXception.js
  function conv(x, params, stride) {
    return add2(conv2d(x, params.filters, stride, "same"), params.bias);
  }
  function reductionBlock(x, params, isActivateInput = true) {
    let out = isActivateInput ? relu(x) : x;
    out = depthwiseSeparableConv(out, params.separable_conv0, [1, 1]);
    out = depthwiseSeparableConv(relu(out), params.separable_conv1, [1, 1]);
    out = maxPool(out, [3, 3], [2, 2], "same");
    out = add2(out, conv(x, params.expansion_conv, [2, 2]));
    return out;
  }
  function mainBlock(x, params) {
    let out = depthwiseSeparableConv(relu(x), params.separable_conv0, [1, 1]);
    out = depthwiseSeparableConv(relu(out), params.separable_conv1, [1, 1]);
    out = depthwiseSeparableConv(relu(out), params.separable_conv2, [1, 1]);
    out = add2(out, x);
    return out;
  }
  class TinyXception extends NeuralNetwork {
    constructor(numMainBlocks) {
      super("TinyXception");
      this._numMainBlocks = numMainBlocks;
    }
    forwardInput(input) {
      const {params} = this;
      if (!params) {
        throw new Error("TinyXception - load model before inference");
      }
      return tidy(() => {
        const batchTensor = input.toBatchTensor(112, true);
        const meanRgb = [122.782, 117.001, 104.298];
        const normalized = normalize(batchTensor, meanRgb).div(scalar(256));
        let out = relu(conv(normalized, params.entry_flow.conv_in, [2, 2]));
        out = reductionBlock(out, params.entry_flow.reduction_block_0, false);
        out = reductionBlock(out, params.entry_flow.reduction_block_1);
        range4(this._numMainBlocks, 0, 1).forEach((idx) => {
          out = mainBlock(out, params.middle_flow[`main_block_${idx}`]);
        });
        out = reductionBlock(out, params.exit_flow.reduction_block);
        out = relu(depthwiseSeparableConv(out, params.exit_flow.separable_conv, [1, 1]));
        return out;
      });
    }
    async forward(input) {
      return this.forwardInput(await toNetInput(input));
    }
    getDefaultModelName() {
      return "tiny_xception_model";
    }
    extractParamsFromWeigthMap(weightMap) {
      return extractParamsFromWeigthMap5(weightMap, this._numMainBlocks);
    }
    extractParams(weights) {
      return extractParams5(weights, this._numMainBlocks);
    }
  }

  // build/ageGenderNet/extractParams.js
  function extractParams7(weights) {
    const paramMappings = [];
    const {extractWeights, getRemainingWeights} = extractWeightsFactory(weights);
    const extractFCParams = extractFCParamsFactory(extractWeights, paramMappings);
    const age = extractFCParams(512, 1, "fc/age");
    const gender = extractFCParams(512, 2, "fc/gender");
    if (getRemainingWeights().length !== 0) {
      throw new Error(`weights remaing after extract: ${getRemainingWeights().length}`);
    }
    return {
      paramMappings,
      params: {fc: {age, gender}}
    };
  }

  // build/ageGenderNet/extractParamsFromWeigthMap.js
  function extractParamsFromWeigthMap7(weightMap) {
    const paramMappings = [];
    const extractWeightEntry = extractWeightEntryFactory(weightMap, paramMappings);
    function extractFcParams(prefix) {
      const weights = extractWeightEntry(`${prefix}/weights`, 2);
      const bias = extractWeightEntry(`${prefix}/bias`, 1);
      return {weights, bias};
    }
    const params = {
      fc: {
        age: extractFcParams("fc/age"),
        gender: extractFcParams("fc/gender")
      }
    };
    disposeUnusedWeightTensors(weightMap, paramMappings);
    return {params, paramMappings};
  }

  // build/ageGenderNet/types.js
  var Gender;
  (function(Gender2) {
    Gender2["FEMALE"] = "female";
    Gender2["MALE"] = "male";
  })(Gender || (Gender = {}));

  // build/ageGenderNet/AgeGenderNet.js
  class AgeGenderNet extends NeuralNetwork {
    constructor(faceFeatureExtractor = new TinyXception(2)) {
      super("AgeGenderNet");
      this._faceFeatureExtractor = faceFeatureExtractor;
    }
    get faceFeatureExtractor() {
      return this._faceFeatureExtractor;
    }
    runNet(input) {
      const {params} = this;
      if (!params) {
        throw new Error(`${this._name} - load model before inference`);
      }
      return tidy(() => {
        const bottleneckFeatures = input instanceof NetInput ? this.faceFeatureExtractor.forwardInput(input) : input;
        const pooled = avgPool(bottleneckFeatures, [7, 7], [2, 2], "valid").as2D(bottleneckFeatures.shape[0], -1);
        const age = fullyConnectedLayer(pooled, params.fc.age).as1D();
        const gender = fullyConnectedLayer(pooled, params.fc.gender);
        return {age, gender};
      });
    }
    forwardInput(input) {
      return tidy(() => {
        const {age, gender} = this.runNet(input);
        return {age, gender: softmax(gender)};
      });
    }
    async forward(input) {
      return this.forwardInput(await toNetInput(input));
    }
    async predictAgeAndGender(input) {
      const netInput = await toNetInput(input);
      const out = await this.forwardInput(netInput);
      const ages = unstack(out.age);
      const genders = unstack(out.gender);
      const ageAndGenderTensors = ages.map((ageTensor, i) => ({
        ageTensor,
        genderTensor: genders[i]
      }));
      const predictionsByBatch = await Promise.all(ageAndGenderTensors.map(async ({ageTensor, genderTensor}) => {
        const age = (await ageTensor.data())[0];
        const probMale = (await genderTensor.data())[0];
        const isMale = probMale > 0.5;
        const gender = isMale ? Gender.MALE : Gender.FEMALE;
        const genderProbability = isMale ? probMale : 1 - probMale;
        ageTensor.dispose();
        genderTensor.dispose();
        return {age, gender, genderProbability};
      }));
      out.age.dispose();
      out.gender.dispose();
      return netInput.isBatchInput ? predictionsByBatch : predictionsByBatch[0];
    }
    getDefaultModelName() {
      return "age_gender_model";
    }
    dispose(throwOnRedispose = true) {
      this.faceFeatureExtractor.dispose(throwOnRedispose);
      super.dispose(throwOnRedispose);
    }
    loadClassifierParams(weights) {
      const {params, paramMappings} = this.extractClassifierParams(weights);
      this._params = params;
      this._paramMappings = paramMappings;
    }
    extractClassifierParams(weights) {
      return extractParams7(weights);
    }
    extractParamsFromWeigthMap(weightMap) {
      const {featureExtractorMap, classifierMap} = seperateWeightMaps(weightMap);
      this.faceFeatureExtractor.loadFromWeightMap(featureExtractorMap);
      return extractParamsFromWeigthMap7(classifierMap);
    }
    extractParams(weights) {
      const classifierWeightSize = 512 * 1 + 1 + (512 * 2 + 2);
      const featureExtractorWeights = weights.slice(0, weights.length - classifierWeightSize);
      const classifierWeights = weights.slice(weights.length - classifierWeightSize);
      this.faceFeatureExtractor.extractWeights(featureExtractorWeights);
      return this.extractClassifierParams(classifierWeights);
    }
  }

  // build/ageGenderNet/index.js

  // build/faceLandmarkNet/FaceLandmark68NetBase.js
  class FaceLandmark68NetBase extends FaceProcessor {
    postProcess(output, inputSize, originalDimensions) {
      const inputDimensions = originalDimensions.map(({width, height}) => {
        const scale2 = inputSize / Math.max(height, width);
        return {
          width: width * scale2,
          height: height * scale2
        };
      });
      const batchSize = inputDimensions.length;
      return tidy(() => {
        const createInterleavedTensor = (fillX, fillY) => stack([
          fill([68], fillX),
          fill([68], fillY)
        ], 1).as2D(1, 136).as1D();
        const getPadding = (batchIdx, cond) => {
          const {width, height} = inputDimensions[batchIdx];
          return cond(width, height) ? Math.abs(width - height) / 2 : 0;
        };
        const getPaddingX = (batchIdx) => getPadding(batchIdx, (w, h) => w < h);
        const getPaddingY = (batchIdx) => getPadding(batchIdx, (w, h) => h < w);
        const landmarkTensors = output.mul(fill([batchSize, 136], inputSize)).sub(stack(Array.from(Array(batchSize), (_, batchIdx) => createInterleavedTensor(getPaddingX(batchIdx), getPaddingY(batchIdx))))).div(stack(Array.from(Array(batchSize), (_, batchIdx) => createInterleavedTensor(inputDimensions[batchIdx].width, inputDimensions[batchIdx].height))));
        return landmarkTensors;
      });
    }
    forwardInput(input) {
      return tidy(() => {
        const out = this.runNet(input);
        return this.postProcess(out, input.inputSize, input.inputDimensions.map(([height, width]) => ({height, width})));
      });
    }
    async forward(input) {
      return this.forwardInput(await toNetInput(input));
    }
    async detectLandmarks(input) {
      const netInput = await toNetInput(input);
      const landmarkTensors = tidy(() => unstack(this.forwardInput(netInput)));
      const landmarksForBatch = await Promise.all(landmarkTensors.map(async (landmarkTensor, batchIdx) => {
        const landmarksArray = Array.from(await landmarkTensor.data());
        const xCoords = landmarksArray.filter((_, i) => isEven(i));
        const yCoords = landmarksArray.filter((_, i) => !isEven(i));
        return new FaceLandmarks68(Array(68).fill(0).map((_, i) => new Point(xCoords[i], yCoords[i])), {
          height: netInput.getInputHeight(batchIdx),
          width: netInput.getInputWidth(batchIdx)
        });
      }));
      landmarkTensors.forEach((t) => t.dispose());
      return netInput.isBatchInput ? landmarksForBatch : landmarksForBatch[0];
    }
    getClassifierChannelsOut() {
      return 136;
    }
  }

  // build/faceLandmarkNet/FaceLandmark68Net.js
  class FaceLandmark68Net extends FaceLandmark68NetBase {
    constructor(faceFeatureExtractor = new FaceFeatureExtractor()) {
      super("FaceLandmark68Net", faceFeatureExtractor);
    }
    getDefaultModelName() {
      return "face_landmark_68_model";
    }
    getClassifierChannelsIn() {
      return 256;
    }
  }

  // build/faceFeatureExtractor/extractParamsFromWeigthMapTiny.js
  function extractParamsFromWeigthMapTiny(weightMap) {
    const paramMappings = [];
    const {extractDenseBlock3Params} = loadParamsFactory(weightMap, paramMappings);
    const params = {
      dense0: extractDenseBlock3Params("dense0", true),
      dense1: extractDenseBlock3Params("dense1"),
      dense2: extractDenseBlock3Params("dense2")
    };
    disposeUnusedWeightTensors(weightMap, paramMappings);
    return {params, paramMappings};
  }

  // build/faceFeatureExtractor/extractParamsTiny.js
  function extractParamsTiny(weights) {
    const paramMappings = [];
    const {extractWeights, getRemainingWeights} = extractWeightsFactory(weights);
    const {extractDenseBlock3Params} = extractorsFactory(extractWeights, paramMappings);
    const dense0 = extractDenseBlock3Params(3, 32, "dense0", true);
    const dense1 = extractDenseBlock3Params(32, 64, "dense1");
    const dense2 = extractDenseBlock3Params(64, 128, "dense2");
    if (getRemainingWeights().length !== 0) {
      throw new Error(`weights remaing after extract: ${getRemainingWeights().length}`);
    }
    return {
      paramMappings,
      params: {dense0, dense1, dense2}
    };
  }

  // build/faceFeatureExtractor/TinyFaceFeatureExtractor.js
  class TinyFaceFeatureExtractor extends NeuralNetwork {
    constructor() {
      super("TinyFaceFeatureExtractor");
    }
    forwardInput(input) {
      const {params} = this;
      if (!params) {
        throw new Error("TinyFaceFeatureExtractor - load model before inference");
      }
      return tidy(() => {
        const batchTensor = input.toBatchTensor(112, true);
        const meanRgb = [122.782, 117.001, 104.298];
        const normalized = normalize(batchTensor, meanRgb).div(scalar(255));
        let out = denseBlock3(normalized, params.dense0, true);
        out = denseBlock3(out, params.dense1);
        out = denseBlock3(out, params.dense2);
        out = avgPool(out, [14, 14], [2, 2], "valid");
        return out;
      });
    }
    async forward(input) {
      return this.forwardInput(await toNetInput(input));
    }
    getDefaultModelName() {
      return "face_feature_extractor_tiny_model";
    }
    extractParamsFromWeigthMap(weightMap) {
      return extractParamsFromWeigthMapTiny(weightMap);
    }
    extractParams(weights) {
      return extractParamsTiny(weights);
    }
  }

  // build/faceLandmarkNet/FaceLandmark68TinyNet.js
  class FaceLandmark68TinyNet extends FaceLandmark68NetBase {
    constructor(faceFeatureExtractor = new TinyFaceFeatureExtractor()) {
      super("FaceLandmark68TinyNet", faceFeatureExtractor);
    }
    getDefaultModelName() {
      return "face_landmark_68_tiny_model";
    }
    getClassifierChannelsIn() {
      return 128;
    }
  }

  // build/faceLandmarkNet/index.js
  class FaceLandmarkNet extends FaceLandmark68Net {
  }

  // build/faceRecognitionNet/scaleLayer.js
  function scale(x, params) {
    return add2(mul(x, params.weights), params.biases);
  }

  // build/faceRecognitionNet/convLayer.js
  function convLayer2(x, params, strides, withRelu, padding = "same") {
    const {filters, bias} = params.conv;
    let out = conv2d(x, filters, strides, padding);
    out = add2(out, bias);
    out = scale(out, params.scale);
    return withRelu ? relu(out) : out;
  }
  function conv2(x, params) {
    return convLayer2(x, params, [1, 1], true);
  }
  function convNoRelu(x, params) {
    return convLayer2(x, params, [1, 1], false);
  }
  function convDown(x, params) {
    return convLayer2(x, params, [2, 2], true, "valid");
  }

  // build/faceRecognitionNet/extractParams.js
  function extractorsFactory5(extractWeights, paramMappings) {
    function extractFilterValues(numFilterValues, numFilters, filterSize) {
      const weights = extractWeights(numFilterValues);
      const depth = weights.length / (numFilters * filterSize * filterSize);
      if (isFloat(depth)) {
        throw new Error(`depth has to be an integer: ${depth}, weights.length: ${weights.length}, numFilters: ${numFilters}, filterSize: ${filterSize}`);
      }
      return tidy(() => transpose(tensor4d(weights, [numFilters, depth, filterSize, filterSize]), [2, 3, 1, 0]));
    }
    function extractConvParams(numFilterValues, numFilters, filterSize, mappedPrefix) {
      const filters = extractFilterValues(numFilterValues, numFilters, filterSize);
      const bias = tensor1d(extractWeights(numFilters));
      paramMappings.push({paramPath: `${mappedPrefix}/filters`}, {paramPath: `${mappedPrefix}/bias`});
      return {filters, bias};
    }
    function extractScaleLayerParams(numWeights, mappedPrefix) {
      const weights = tensor1d(extractWeights(numWeights));
      const biases = tensor1d(extractWeights(numWeights));
      paramMappings.push({paramPath: `${mappedPrefix}/weights`}, {paramPath: `${mappedPrefix}/biases`});
      return {
        weights,
        biases
      };
    }
    function extractConvLayerParams(numFilterValues, numFilters, filterSize, mappedPrefix) {
      const conv3 = extractConvParams(numFilterValues, numFilters, filterSize, `${mappedPrefix}/conv`);
      const scale2 = extractScaleLayerParams(numFilters, `${mappedPrefix}/scale`);
      return {conv: conv3, scale: scale2};
    }
    function extractResidualLayerParams(numFilterValues, numFilters, filterSize, mappedPrefix, isDown = false) {
      const conv1 = extractConvLayerParams((isDown ? 0.5 : 1) * numFilterValues, numFilters, filterSize, `${mappedPrefix}/conv1`);
      const conv22 = extractConvLayerParams(numFilterValues, numFilters, filterSize, `${mappedPrefix}/conv2`);
      return {conv1, conv2: conv22};
    }
    return {
      extractConvLayerParams,
      extractResidualLayerParams
    };
  }
  function extractParams9(weights) {
    const {extractWeights, getRemainingWeights} = extractWeightsFactory(weights);
    const paramMappings = [];
    const {extractConvLayerParams, extractResidualLayerParams} = extractorsFactory5(extractWeights, paramMappings);
    const conv32_down = extractConvLayerParams(4704, 32, 7, "conv32_down");
    const conv32_1 = extractResidualLayerParams(9216, 32, 3, "conv32_1");
    const conv32_2 = extractResidualLayerParams(9216, 32, 3, "conv32_2");
    const conv32_3 = extractResidualLayerParams(9216, 32, 3, "conv32_3");
    const conv64_down = extractResidualLayerParams(36864, 64, 3, "conv64_down", true);
    const conv64_1 = extractResidualLayerParams(36864, 64, 3, "conv64_1");
    const conv64_2 = extractResidualLayerParams(36864, 64, 3, "conv64_2");
    const conv64_3 = extractResidualLayerParams(36864, 64, 3, "conv64_3");
    const conv128_down = extractResidualLayerParams(147456, 128, 3, "conv128_down", true);
    const conv128_1 = extractResidualLayerParams(147456, 128, 3, "conv128_1");
    const conv128_2 = extractResidualLayerParams(147456, 128, 3, "conv128_2");
    const conv256_down = extractResidualLayerParams(589824, 256, 3, "conv256_down", true);
    const conv256_1 = extractResidualLayerParams(589824, 256, 3, "conv256_1");
    const conv256_2 = extractResidualLayerParams(589824, 256, 3, "conv256_2");
    const conv256_down_out = extractResidualLayerParams(589824, 256, 3, "conv256_down_out");
    const fc = tidy(() => transpose(tensor2d(extractWeights(256 * 128), [128, 256]), [1, 0]));
    paramMappings.push({paramPath: `fc`});
    if (getRemainingWeights().length !== 0) {
      throw new Error(`weights remaing after extract: ${getRemainingWeights().length}`);
    }
    const params = {
      conv32_down,
      conv32_1,
      conv32_2,
      conv32_3,
      conv64_down,
      conv64_1,
      conv64_2,
      conv64_3,
      conv128_down,
      conv128_1,
      conv128_2,
      conv256_down,
      conv256_1,
      conv256_2,
      conv256_down_out,
      fc
    };
    return {params, paramMappings};
  }

  // build/faceRecognitionNet/extractParamsFromWeigthMap.js
  function extractorsFactory6(weightMap, paramMappings) {
    const extractWeightEntry = extractWeightEntryFactory(weightMap, paramMappings);
    function extractScaleLayerParams(prefix) {
      const weights = extractWeightEntry(`${prefix}/scale/weights`, 1);
      const biases = extractWeightEntry(`${prefix}/scale/biases`, 1);
      return {weights, biases};
    }
    function extractConvLayerParams(prefix) {
      const filters = extractWeightEntry(`${prefix}/conv/filters`, 4);
      const bias = extractWeightEntry(`${prefix}/conv/bias`, 1);
      const scale2 = extractScaleLayerParams(prefix);
      return {conv: {filters, bias}, scale: scale2};
    }
    function extractResidualLayerParams(prefix) {
      return {
        conv1: extractConvLayerParams(`${prefix}/conv1`),
        conv2: extractConvLayerParams(`${prefix}/conv2`)
      };
    }
    return {
      extractConvLayerParams,
      extractResidualLayerParams
    };
  }
  function extractParamsFromWeigthMap9(weightMap) {
    const paramMappings = [];
    const {extractConvLayerParams, extractResidualLayerParams} = extractorsFactory6(weightMap, paramMappings);
    const conv32_down = extractConvLayerParams("conv32_down");
    const conv32_1 = extractResidualLayerParams("conv32_1");
    const conv32_2 = extractResidualLayerParams("conv32_2");
    const conv32_3 = extractResidualLayerParams("conv32_3");
    const conv64_down = extractResidualLayerParams("conv64_down");
    const conv64_1 = extractResidualLayerParams("conv64_1");
    const conv64_2 = extractResidualLayerParams("conv64_2");
    const conv64_3 = extractResidualLayerParams("conv64_3");
    const conv128_down = extractResidualLayerParams("conv128_down");
    const conv128_1 = extractResidualLayerParams("conv128_1");
    const conv128_2 = extractResidualLayerParams("conv128_2");
    const conv256_down = extractResidualLayerParams("conv256_down");
    const conv256_1 = extractResidualLayerParams("conv256_1");
    const conv256_2 = extractResidualLayerParams("conv256_2");
    const conv256_down_out = extractResidualLayerParams("conv256_down_out");
    const fc = weightMap["fc"];
    paramMappings.push({originalPath: "fc", paramPath: "fc"});
    if (!isTensor2D(fc)) {
      throw new Error(`expected weightMap[fc] to be a Tensor2D, instead have ${fc}`);
    }
    const params = {
      conv32_down,
      conv32_1,
      conv32_2,
      conv32_3,
      conv64_down,
      conv64_1,
      conv64_2,
      conv64_3,
      conv128_down,
      conv128_1,
      conv128_2,
      conv256_down,
      conv256_1,
      conv256_2,
      conv256_down_out,
      fc
    };
    disposeUnusedWeightTensors(weightMap, paramMappings);
    return {params, paramMappings};
  }

  // build/faceRecognitionNet/residualLayer.js
  function residual(x, params) {
    let out = conv2(x, params.conv1);
    out = convNoRelu(out, params.conv2);
    out = add2(out, x);
    out = relu(out);
    return out;
  }
  function residualDown(x, params) {
    let out = convDown(x, params.conv1);
    out = convNoRelu(out, params.conv2);
    let pooled = avgPool(x, 2, 2, "valid");
    const zeros9 = zeros(pooled.shape);
    const isPad = pooled.shape[3] !== out.shape[3];
    const isAdjustShape = pooled.shape[1] !== out.shape[1] || pooled.shape[2] !== out.shape[2];
    if (isAdjustShape) {
      const padShapeX = [...out.shape];
      padShapeX[1] = 1;
      const zerosW = zeros(padShapeX);
      out = concat([out, zerosW], 1);
      const padShapeY = [...out.shape];
      padShapeY[2] = 1;
      const zerosH = zeros(padShapeY);
      out = concat([out, zerosH], 2);
    }
    pooled = isPad ? concat([pooled, zeros9], 3) : pooled;
    out = add2(pooled, out);
    out = relu(out);
    return out;
  }

  // build/faceRecognitionNet/FaceRecognitionNet.js
  class FaceRecognitionNet extends NeuralNetwork {
    constructor() {
      super("FaceRecognitionNet");
    }
    forwardInput(input) {
      const {params} = this;
      if (!params) {
        throw new Error("FaceRecognitionNet - load model before inference");
      }
      return tidy(() => {
        const batchTensor = input.toBatchTensor(150, true).toFloat();
        const meanRgb = [122.782, 117.001, 104.298];
        const normalized = normalize(batchTensor, meanRgb).div(scalar(256));
        let out = convDown(normalized, params.conv32_down);
        out = maxPool(out, 3, 2, "valid");
        out = residual(out, params.conv32_1);
        out = residual(out, params.conv32_2);
        out = residual(out, params.conv32_3);
        out = residualDown(out, params.conv64_down);
        out = residual(out, params.conv64_1);
        out = residual(out, params.conv64_2);
        out = residual(out, params.conv64_3);
        out = residualDown(out, params.conv128_down);
        out = residual(out, params.conv128_1);
        out = residual(out, params.conv128_2);
        out = residualDown(out, params.conv256_down);
        out = residual(out, params.conv256_1);
        out = residual(out, params.conv256_2);
        out = residualDown(out, params.conv256_down_out);
        const globalAvg = out.mean([1, 2]);
        const fullyConnected = matMul(globalAvg, params.fc);
        return fullyConnected;
      });
    }
    async forward(input) {
      return this.forwardInput(await toNetInput(input));
    }
    async computeFaceDescriptor(input) {
      const netInput = await toNetInput(input);
      const faceDescriptorTensors = tidy(() => unstack(this.forwardInput(netInput)));
      const faceDescriptorsForBatch = await Promise.all(faceDescriptorTensors.map((t) => t.data()));
      faceDescriptorTensors.forEach((t) => t.dispose());
      return netInput.isBatchInput ? faceDescriptorsForBatch : faceDescriptorsForBatch[0];
    }
    getDefaultModelName() {
      return "face_recognition_model";
    }
    extractParamsFromWeigthMap(weightMap) {
      return extractParamsFromWeigthMap9(weightMap);
    }
    extractParams(weights) {
      return extractParams9(weights);
    }
  }

  // build/faceRecognitionNet/index.js
  function createFaceRecognitionNet(weights) {
    const net = new FaceRecognitionNet();
    net.extractWeights(weights);
    return net;
  }

  // build/factories/WithFaceDescriptor.js
  function extendWithFaceDescriptor(sourceObj, descriptor) {
    const extension = {descriptor};
    return Object.assign({}, sourceObj, extension);
  }

  // build/factories/WithAge.js
  function isWithAge(obj) {
    return typeof obj["age"] === "number";
  }
  function extendWithAge(sourceObj, age) {
    const extension = {age};
    return Object.assign({}, sourceObj, extension);
  }

  // build/factories/WithGender.js
  function isWithGender(obj) {
    return (obj["gender"] === Gender.MALE || obj["gender"] === Gender.FEMALE) && isValidProbablitiy(obj["genderProbability"]);
  }
  function extendWithGender(sourceObj, gender, genderProbability) {
    const extension = {gender, genderProbability};
    return Object.assign({}, sourceObj, extension);
  }

  // build/factories/index.js

  // build/ssdMobilenetv1/extractParams.js
  function extractorsFactory7(extractWeights, paramMappings) {
    function extractDepthwiseConvParams(numChannels, mappedPrefix) {
      const filters = tensor4d(extractWeights(3 * 3 * numChannels), [3, 3, numChannels, 1]);
      const batch_norm_scale = tensor1d(extractWeights(numChannels));
      const batch_norm_offset = tensor1d(extractWeights(numChannels));
      const batch_norm_mean = tensor1d(extractWeights(numChannels));
      const batch_norm_variance = tensor1d(extractWeights(numChannels));
      paramMappings.push({paramPath: `${mappedPrefix}/filters`}, {paramPath: `${mappedPrefix}/batch_norm_scale`}, {paramPath: `${mappedPrefix}/batch_norm_offset`}, {paramPath: `${mappedPrefix}/batch_norm_mean`}, {paramPath: `${mappedPrefix}/batch_norm_variance`});
      return {
        filters,
        batch_norm_scale,
        batch_norm_offset,
        batch_norm_mean,
        batch_norm_variance
      };
    }
    function extractConvParams(channelsIn, channelsOut, filterSize, mappedPrefix, isPointwiseConv) {
      const filters = tensor4d(extractWeights(channelsIn * channelsOut * filterSize * filterSize), [filterSize, filterSize, channelsIn, channelsOut]);
      const bias = tensor1d(extractWeights(channelsOut));
      paramMappings.push({paramPath: `${mappedPrefix}/filters`}, {paramPath: `${mappedPrefix}/${isPointwiseConv ? "batch_norm_offset" : "bias"}`});
      return {filters, bias};
    }
    function extractPointwiseConvParams(channelsIn, channelsOut, filterSize, mappedPrefix) {
      const {filters, bias} = extractConvParams(channelsIn, channelsOut, filterSize, mappedPrefix, true);
      return {
        filters,
        batch_norm_offset: bias
      };
    }
    function extractConvPairParams(channelsIn, channelsOut, mappedPrefix) {
      const depthwise_conv = extractDepthwiseConvParams(channelsIn, `${mappedPrefix}/depthwise_conv`);
      const pointwise_conv = extractPointwiseConvParams(channelsIn, channelsOut, 1, `${mappedPrefix}/pointwise_conv`);
      return {depthwise_conv, pointwise_conv};
    }
    function extractMobilenetV1Params() {
      const conv_0 = extractPointwiseConvParams(3, 32, 3, "mobilenetv1/conv_0");
      const conv_1 = extractConvPairParams(32, 64, "mobilenetv1/conv_1");
      const conv_2 = extractConvPairParams(64, 128, "mobilenetv1/conv_2");
      const conv_3 = extractConvPairParams(128, 128, "mobilenetv1/conv_3");
      const conv_4 = extractConvPairParams(128, 256, "mobilenetv1/conv_4");
      const conv_5 = extractConvPairParams(256, 256, "mobilenetv1/conv_5");
      const conv_6 = extractConvPairParams(256, 512, "mobilenetv1/conv_6");
      const conv_7 = extractConvPairParams(512, 512, "mobilenetv1/conv_7");
      const conv_8 = extractConvPairParams(512, 512, "mobilenetv1/conv_8");
      const conv_9 = extractConvPairParams(512, 512, "mobilenetv1/conv_9");
      const conv_10 = extractConvPairParams(512, 512, "mobilenetv1/conv_10");
      const conv_11 = extractConvPairParams(512, 512, "mobilenetv1/conv_11");
      const conv_12 = extractConvPairParams(512, 1024, "mobilenetv1/conv_12");
      const conv_13 = extractConvPairParams(1024, 1024, "mobilenetv1/conv_13");
      return {
        conv_0,
        conv_1,
        conv_2,
        conv_3,
        conv_4,
        conv_5,
        conv_6,
        conv_7,
        conv_8,
        conv_9,
        conv_10,
        conv_11,
        conv_12,
        conv_13
      };
    }
    function extractPredictionLayerParams() {
      const conv_0 = extractPointwiseConvParams(1024, 256, 1, "prediction_layer/conv_0");
      const conv_1 = extractPointwiseConvParams(256, 512, 3, "prediction_layer/conv_1");
      const conv_2 = extractPointwiseConvParams(512, 128, 1, "prediction_layer/conv_2");
      const conv_3 = extractPointwiseConvParams(128, 256, 3, "prediction_layer/conv_3");
      const conv_4 = extractPointwiseConvParams(256, 128, 1, "prediction_layer/conv_4");
      const conv_5 = extractPointwiseConvParams(128, 256, 3, "prediction_layer/conv_5");
      const conv_6 = extractPointwiseConvParams(256, 64, 1, "prediction_layer/conv_6");
      const conv_7 = extractPointwiseConvParams(64, 128, 3, "prediction_layer/conv_7");
      const box_encoding_0_predictor = extractConvParams(512, 12, 1, "prediction_layer/box_predictor_0/box_encoding_predictor");
      const class_predictor_0 = extractConvParams(512, 9, 1, "prediction_layer/box_predictor_0/class_predictor");
      const box_encoding_1_predictor = extractConvParams(1024, 24, 1, "prediction_layer/box_predictor_1/box_encoding_predictor");
      const class_predictor_1 = extractConvParams(1024, 18, 1, "prediction_layer/box_predictor_1/class_predictor");
      const box_encoding_2_predictor = extractConvParams(512, 24, 1, "prediction_layer/box_predictor_2/box_encoding_predictor");
      const class_predictor_2 = extractConvParams(512, 18, 1, "prediction_layer/box_predictor_2/class_predictor");
      const box_encoding_3_predictor = extractConvParams(256, 24, 1, "prediction_layer/box_predictor_3/box_encoding_predictor");
      const class_predictor_3 = extractConvParams(256, 18, 1, "prediction_layer/box_predictor_3/class_predictor");
      const box_encoding_4_predictor = extractConvParams(256, 24, 1, "prediction_layer/box_predictor_4/box_encoding_predictor");
      const class_predictor_4 = extractConvParams(256, 18, 1, "prediction_layer/box_predictor_4/class_predictor");
      const box_encoding_5_predictor = extractConvParams(128, 24, 1, "prediction_layer/box_predictor_5/box_encoding_predictor");
      const class_predictor_5 = extractConvParams(128, 18, 1, "prediction_layer/box_predictor_5/class_predictor");
      const box_predictor_0 = {
        box_encoding_predictor: box_encoding_0_predictor,
        class_predictor: class_predictor_0
      };
      const box_predictor_1 = {
        box_encoding_predictor: box_encoding_1_predictor,
        class_predictor: class_predictor_1
      };
      const box_predictor_2 = {
        box_encoding_predictor: box_encoding_2_predictor,
        class_predictor: class_predictor_2
      };
      const box_predictor_3 = {
        box_encoding_predictor: box_encoding_3_predictor,
        class_predictor: class_predictor_3
      };
      const box_predictor_4 = {
        box_encoding_predictor: box_encoding_4_predictor,
        class_predictor: class_predictor_4
      };
      const box_predictor_5 = {
        box_encoding_predictor: box_encoding_5_predictor,
        class_predictor: class_predictor_5
      };
      return {
        conv_0,
        conv_1,
        conv_2,
        conv_3,
        conv_4,
        conv_5,
        conv_6,
        conv_7,
        box_predictor_0,
        box_predictor_1,
        box_predictor_2,
        box_predictor_3,
        box_predictor_4,
        box_predictor_5
      };
    }
    return {
      extractMobilenetV1Params,
      extractPredictionLayerParams
    };
  }
  function extractParams11(weights) {
    const paramMappings = [];
    const {extractWeights, getRemainingWeights} = extractWeightsFactory(weights);
    const {extractMobilenetV1Params, extractPredictionLayerParams} = extractorsFactory7(extractWeights, paramMappings);
    const mobilenetv1 = extractMobilenetV1Params();
    const prediction_layer = extractPredictionLayerParams();
    const extra_dim = tensor3d(extractWeights(5118 * 4), [1, 5118, 4]);
    const output_layer = {
      extra_dim
    };
    paramMappings.push({paramPath: "output_layer/extra_dim"});
    if (getRemainingWeights().length !== 0) {
      throw new Error(`weights remaing after extract: ${getRemainingWeights().length}`);
    }
    return {
      params: {
        mobilenetv1,
        prediction_layer,
        output_layer
      },
      paramMappings
    };
  }

  // build/ssdMobilenetv1/extractParamsFromWeigthMap.js
  function extractorsFactory8(weightMap, paramMappings) {
    const extractWeightEntry = extractWeightEntryFactory(weightMap, paramMappings);
    function extractPointwiseConvParams(prefix, idx, mappedPrefix) {
      const filters = extractWeightEntry(`${prefix}/Conv2d_${idx}_pointwise/weights`, 4, `${mappedPrefix}/filters`);
      const batch_norm_offset = extractWeightEntry(`${prefix}/Conv2d_${idx}_pointwise/convolution_bn_offset`, 1, `${mappedPrefix}/batch_norm_offset`);
      return {filters, batch_norm_offset};
    }
    function extractConvPairParams(idx) {
      const mappedPrefix = `mobilenetv1/conv_${idx}`;
      const prefixDepthwiseConv = `MobilenetV1/Conv2d_${idx}_depthwise`;
      const mappedPrefixDepthwiseConv = `${mappedPrefix}/depthwise_conv`;
      const mappedPrefixPointwiseConv = `${mappedPrefix}/pointwise_conv`;
      const filters = extractWeightEntry(`${prefixDepthwiseConv}/depthwise_weights`, 4, `${mappedPrefixDepthwiseConv}/filters`);
      const batch_norm_scale = extractWeightEntry(`${prefixDepthwiseConv}/BatchNorm/gamma`, 1, `${mappedPrefixDepthwiseConv}/batch_norm_scale`);
      const batch_norm_offset = extractWeightEntry(`${prefixDepthwiseConv}/BatchNorm/beta`, 1, `${mappedPrefixDepthwiseConv}/batch_norm_offset`);
      const batch_norm_mean = extractWeightEntry(`${prefixDepthwiseConv}/BatchNorm/moving_mean`, 1, `${mappedPrefixDepthwiseConv}/batch_norm_mean`);
      const batch_norm_variance = extractWeightEntry(`${prefixDepthwiseConv}/BatchNorm/moving_variance`, 1, `${mappedPrefixDepthwiseConv}/batch_norm_variance`);
      return {
        depthwise_conv: {
          filters,
          batch_norm_scale,
          batch_norm_offset,
          batch_norm_mean,
          batch_norm_variance
        },
        pointwise_conv: extractPointwiseConvParams("MobilenetV1", idx, mappedPrefixPointwiseConv)
      };
    }
    function extractMobilenetV1Params() {
      return {
        conv_0: extractPointwiseConvParams("MobilenetV1", 0, "mobilenetv1/conv_0"),
        conv_1: extractConvPairParams(1),
        conv_2: extractConvPairParams(2),
        conv_3: extractConvPairParams(3),
        conv_4: extractConvPairParams(4),
        conv_5: extractConvPairParams(5),
        conv_6: extractConvPairParams(6),
        conv_7: extractConvPairParams(7),
        conv_8: extractConvPairParams(8),
        conv_9: extractConvPairParams(9),
        conv_10: extractConvPairParams(10),
        conv_11: extractConvPairParams(11),
        conv_12: extractConvPairParams(12),
        conv_13: extractConvPairParams(13)
      };
    }
    function extractConvParams(prefix, mappedPrefix) {
      const filters = extractWeightEntry(`${prefix}/weights`, 4, `${mappedPrefix}/filters`);
      const bias = extractWeightEntry(`${prefix}/biases`, 1, `${mappedPrefix}/bias`);
      return {filters, bias};
    }
    function extractBoxPredictorParams(idx) {
      const box_encoding_predictor = extractConvParams(`Prediction/BoxPredictor_${idx}/BoxEncodingPredictor`, `prediction_layer/box_predictor_${idx}/box_encoding_predictor`);
      const class_predictor = extractConvParams(`Prediction/BoxPredictor_${idx}/ClassPredictor`, `prediction_layer/box_predictor_${idx}/class_predictor`);
      return {box_encoding_predictor, class_predictor};
    }
    function extractPredictionLayerParams() {
      return {
        conv_0: extractPointwiseConvParams("Prediction", 0, "prediction_layer/conv_0"),
        conv_1: extractPointwiseConvParams("Prediction", 1, "prediction_layer/conv_1"),
        conv_2: extractPointwiseConvParams("Prediction", 2, "prediction_layer/conv_2"),
        conv_3: extractPointwiseConvParams("Prediction", 3, "prediction_layer/conv_3"),
        conv_4: extractPointwiseConvParams("Prediction", 4, "prediction_layer/conv_4"),
        conv_5: extractPointwiseConvParams("Prediction", 5, "prediction_layer/conv_5"),
        conv_6: extractPointwiseConvParams("Prediction", 6, "prediction_layer/conv_6"),
        conv_7: extractPointwiseConvParams("Prediction", 7, "prediction_layer/conv_7"),
        box_predictor_0: extractBoxPredictorParams(0),
        box_predictor_1: extractBoxPredictorParams(1),
        box_predictor_2: extractBoxPredictorParams(2),
        box_predictor_3: extractBoxPredictorParams(3),
        box_predictor_4: extractBoxPredictorParams(4),
        box_predictor_5: extractBoxPredictorParams(5)
      };
    }
    return {
      extractMobilenetV1Params,
      extractPredictionLayerParams
    };
  }
  function extractParamsFromWeigthMap11(weightMap) {
    const paramMappings = [];
    const {extractMobilenetV1Params, extractPredictionLayerParams} = extractorsFactory8(weightMap, paramMappings);
    const extra_dim = weightMap["Output/extra_dim"];
    paramMappings.push({originalPath: "Output/extra_dim", paramPath: "output_layer/extra_dim"});
    if (!isTensor3D(extra_dim)) {
      throw new Error(`expected weightMap['Output/extra_dim'] to be a Tensor3D, instead have ${extra_dim}`);
    }
    const params = {
      mobilenetv1: extractMobilenetV1Params(),
      prediction_layer: extractPredictionLayerParams(),
      output_layer: {
        extra_dim
      }
    };
    disposeUnusedWeightTensors(weightMap, paramMappings);
    return {params, paramMappings};
  }

  // build/ssdMobilenetv1/pointwiseConvLayer.js
  function pointwiseConvLayer(x, params, strides) {
    return tidy(() => {
      let out = conv2d(x, params.filters, strides, "same");
      out = add2(out, params.batch_norm_offset);
      return clipByValue(out, 0, 6);
    });
  }

  // build/ssdMobilenetv1/mobileNetV1.js
  const epsilon = 0.0010000000474974513;
  function depthwiseConvLayer(x, params, strides) {
    return tidy(() => {
      let out = depthwiseConv2d(x, params.filters, strides, "same");
      out = batchNorm(out, params.batch_norm_mean, params.batch_norm_variance, params.batch_norm_offset, params.batch_norm_scale, epsilon);
      return clipByValue(out, 0, 6);
    });
  }
  function getStridesForLayerIdx(layerIdx) {
    return [2, 4, 6, 12].some((idx) => idx === layerIdx) ? [2, 2] : [1, 1];
  }
  function mobileNetV1(x, params) {
    return tidy(() => {
      let conv11 = null;
      let out = pointwiseConvLayer(x, params.conv_0, [2, 2]);
      const convPairParams = [
        params.conv_1,
        params.conv_2,
        params.conv_3,
        params.conv_4,
        params.conv_5,
        params.conv_6,
        params.conv_7,
        params.conv_8,
        params.conv_9,
        params.conv_10,
        params.conv_11,
        params.conv_12,
        params.conv_13
      ];
      convPairParams.forEach((param, i) => {
        const layerIdx = i + 1;
        const depthwiseConvStrides = getStridesForLayerIdx(layerIdx);
        out = depthwiseConvLayer(out, param.depthwise_conv, depthwiseConvStrides);
        out = pointwiseConvLayer(out, param.pointwise_conv, [1, 1]);
        if (layerIdx === 11) {
          conv11 = out;
        }
      });
      if (conv11 === null) {
        throw new Error("mobileNetV1 - output of conv layer 11 is null");
      }
      return {
        out,
        conv11
      };
    });
  }

  // build/ssdMobilenetv1/nonMaxSuppression.js
  function nonMaxSuppression3(boxes, scores, maxOutputSize, iouThreshold, scoreThreshold) {
    const numBoxes = boxes.shape[0];
    const outputSize = Math.min(maxOutputSize, numBoxes);
    const candidates = scores.map((score, boxIndex) => ({score, boxIndex})).filter((c) => c.score > scoreThreshold).sort((c1, c2) => c2.score - c1.score);
    const suppressFunc = (x) => x <= iouThreshold ? 1 : 0;
    const selected = [];
    candidates.forEach((c) => {
      if (selected.length >= outputSize) {
        return;
      }
      const originalScore = c.score;
      for (let j = selected.length - 1; j >= 0; --j) {
        const iou3 = IOU(boxes, c.boxIndex, selected[j]);
        if (iou3 === 0) {
          continue;
        }
        c.score *= suppressFunc(iou3);
        if (c.score <= scoreThreshold) {
          break;
        }
      }
      if (originalScore === c.score) {
        selected.push(c.boxIndex);
      }
    });
    return selected;
  }
  function IOU(boxes, i, j) {
    const boxesData = boxes.arraySync();
    const yminI = Math.min(boxesData[i][0], boxesData[i][2]);
    const xminI = Math.min(boxesData[i][1], boxesData[i][3]);
    const ymaxI = Math.max(boxesData[i][0], boxesData[i][2]);
    const xmaxI = Math.max(boxesData[i][1], boxesData[i][3]);
    const yminJ = Math.min(boxesData[j][0], boxesData[j][2]);
    const xminJ = Math.min(boxesData[j][1], boxesData[j][3]);
    const ymaxJ = Math.max(boxesData[j][0], boxesData[j][2]);
    const xmaxJ = Math.max(boxesData[j][1], boxesData[j][3]);
    const areaI = (ymaxI - yminI) * (xmaxI - xminI);
    const areaJ = (ymaxJ - yminJ) * (xmaxJ - xminJ);
    if (areaI <= 0 || areaJ <= 0) {
      return 0;
    }
    const intersectionYmin = Math.max(yminI, yminJ);
    const intersectionXmin = Math.max(xminI, xminJ);
    const intersectionYmax = Math.min(ymaxI, ymaxJ);
    const intersectionXmax = Math.min(xmaxI, xmaxJ);
    const intersectionArea = Math.max(intersectionYmax - intersectionYmin, 0) * Math.max(intersectionXmax - intersectionXmin, 0);
    return intersectionArea / (areaI + areaJ - intersectionArea);
  }

  // build/ssdMobilenetv1/outputLayer.js
  function getCenterCoordinatesAndSizesLayer(x) {
    const vec = unstack(transpose(x, [1, 0]));
    const sizes = [
      sub(vec[2], vec[0]),
      sub(vec[3], vec[1])
    ];
    const centers = [
      add2(vec[0], div(sizes[0], scalar(2))),
      add2(vec[1], div(sizes[1], scalar(2)))
    ];
    return {
      sizes,
      centers
    };
  }
  function decodeBoxesLayer(x0, x1) {
    const {sizes, centers} = getCenterCoordinatesAndSizesLayer(x0);
    const vec = unstack(transpose(x1, [1, 0]));
    const div0_out = div(mul(exp(div(vec[2], scalar(5))), sizes[0]), scalar(2));
    const add0_out = add2(mul(div(vec[0], scalar(10)), sizes[0]), centers[0]);
    const div1_out = div(mul(exp(div(vec[3], scalar(5))), sizes[1]), scalar(2));
    const add1_out = add2(mul(div(vec[1], scalar(10)), sizes[1]), centers[1]);
    return transpose(stack([
      sub(add0_out, div0_out),
      sub(add1_out, div1_out),
      add2(add0_out, div0_out),
      add2(add1_out, div1_out)
    ]), [1, 0]);
  }
  function outputLayer(boxPredictions, classPredictions, params) {
    return tidy(() => {
      const batchSize = boxPredictions.shape[0];
      let boxes = decodeBoxesLayer(reshape(tile(params.extra_dim, [batchSize, 1, 1]), [-1, 4]), reshape(boxPredictions, [-1, 4]));
      boxes = reshape(boxes, [batchSize, boxes.shape[0] / batchSize, 4]);
      const scoresAndClasses = sigmoid(slice(classPredictions, [0, 0, 1], [-1, -1, -1]));
      let scores = slice(scoresAndClasses, [0, 0, 0], [-1, -1, 1]);
      scores = reshape(scores, [batchSize, scores.shape[1]]);
      const boxesByBatch = unstack(boxes);
      const scoresByBatch = unstack(scores);
      return {
        boxes: boxesByBatch,
        scores: scoresByBatch
      };
    });
  }

  // build/ssdMobilenetv1/boxPredictionLayer.js
  function boxPredictionLayer(x, params) {
    return tidy(() => {
      const batchSize = x.shape[0];
      const boxPredictionEncoding = reshape(convLayer(x, params.box_encoding_predictor), [batchSize, -1, 1, 4]);
      const classPrediction = reshape(convLayer(x, params.class_predictor), [batchSize, -1, 3]);
      return {
        boxPredictionEncoding,
        classPrediction
      };
    });
  }

  // build/ssdMobilenetv1/predictionLayer.js
  function predictionLayer(x, conv11, params) {
    return tidy(() => {
      const conv0 = pointwiseConvLayer(x, params.conv_0, [1, 1]);
      const conv1 = pointwiseConvLayer(conv0, params.conv_1, [2, 2]);
      const conv22 = pointwiseConvLayer(conv1, params.conv_2, [1, 1]);
      const conv3 = pointwiseConvLayer(conv22, params.conv_3, [2, 2]);
      const conv4 = pointwiseConvLayer(conv3, params.conv_4, [1, 1]);
      const conv5 = pointwiseConvLayer(conv4, params.conv_5, [2, 2]);
      const conv6 = pointwiseConvLayer(conv5, params.conv_6, [1, 1]);
      const conv7 = pointwiseConvLayer(conv6, params.conv_7, [2, 2]);
      const boxPrediction0 = boxPredictionLayer(conv11, params.box_predictor_0);
      const boxPrediction1 = boxPredictionLayer(x, params.box_predictor_1);
      const boxPrediction2 = boxPredictionLayer(conv1, params.box_predictor_2);
      const boxPrediction3 = boxPredictionLayer(conv3, params.box_predictor_3);
      const boxPrediction4 = boxPredictionLayer(conv5, params.box_predictor_4);
      const boxPrediction5 = boxPredictionLayer(conv7, params.box_predictor_5);
      const boxPredictions = concat([
        boxPrediction0.boxPredictionEncoding,
        boxPrediction1.boxPredictionEncoding,
        boxPrediction2.boxPredictionEncoding,
        boxPrediction3.boxPredictionEncoding,
        boxPrediction4.boxPredictionEncoding,
        boxPrediction5.boxPredictionEncoding
      ], 1);
      const classPredictions = concat([
        boxPrediction0.classPrediction,
        boxPrediction1.classPrediction,
        boxPrediction2.classPrediction,
        boxPrediction3.classPrediction,
        boxPrediction4.classPrediction,
        boxPrediction5.classPrediction
      ], 1);
      return {
        boxPredictions,
        classPredictions
      };
    });
  }

  // build/ssdMobilenetv1/SsdMobilenetv1Options.js
  class SsdMobilenetv1Options {
    constructor({minConfidence, maxResults} = {}) {
      this._name = "SsdMobilenetv1Options";
      this._minConfidence = minConfidence || 0.5;
      this._maxResults = maxResults || 100;
      if (typeof this._minConfidence !== "number" || this._minConfidence <= 0 || this._minConfidence >= 1) {
        throw new Error(`${this._name} - expected minConfidence to be a number between 0 and 1`);
      }
      if (typeof this._maxResults !== "number") {
        throw new Error(`${this._name} - expected maxResults to be a number`);
      }
    }
    get minConfidence() {
      return this._minConfidence;
    }
    get maxResults() {
      return this._maxResults;
    }
  }

  // build/ssdMobilenetv1/SsdMobilenetv1.js
  class SsdMobilenetv1 extends NeuralNetwork {
    constructor() {
      super("SsdMobilenetv1");
    }
    forwardInput(input) {
      const {params} = this;
      if (!params) {
        throw new Error("SsdMobilenetv1 - load model before inference");
      }
      return tidy(() => {
        const batchTensor = input.toBatchTensor(512, false).toFloat();
        const x = sub(mul(batchTensor, scalar(0.007843137718737125)), scalar(1));
        const features = mobileNetV1(x, params.mobilenetv1);
        const {boxPredictions, classPredictions} = predictionLayer(features.out, features.conv11, params.prediction_layer);
        return outputLayer(boxPredictions, classPredictions, params.output_layer);
      });
    }
    async forward(input) {
      return this.forwardInput(await toNetInput(input));
    }
    async locateFaces(input, options = {}) {
      const {maxResults, minConfidence} = new SsdMobilenetv1Options(options);
      const netInput = await toNetInput(input);
      const {boxes: _boxes, scores: _scores} = this.forwardInput(netInput);
      const boxes = _boxes[0];
      const scores = _scores[0];
      for (let i = 1; i < _boxes.length; i++) {
        _boxes[i].dispose();
        _scores[i].dispose();
      }
      const scoresData = Array.from(await scores.data());
      const iouThreshold = 0.5;
      const indices = nonMaxSuppression3(boxes, scoresData, maxResults, iouThreshold, minConfidence);
      const reshapedDims = netInput.getReshapedInputDimensions(0);
      const inputSize = netInput.inputSize;
      const padX = inputSize / reshapedDims.width;
      const padY = inputSize / reshapedDims.height;
      const boxesData = boxes.arraySync();
      const results = indices.map((idx) => {
        const [top, bottom] = [
          Math.max(0, boxesData[idx][0]),
          Math.min(1, boxesData[idx][2])
        ].map((val) => val * padY);
        const [left, right] = [
          Math.max(0, boxesData[idx][1]),
          Math.min(1, boxesData[idx][3])
        ].map((val) => val * padX);
        return new FaceDetection(scoresData[idx], new Rect(left, top, right - left, bottom - top), {
          height: netInput.getInputHeight(0),
          width: netInput.getInputWidth(0)
        });
      });
      boxes.dispose();
      scores.dispose();
      return results;
    }
    getDefaultModelName() {
      return "ssd_mobilenetv1_model";
    }
    extractParamsFromWeigthMap(weightMap) {
      return extractParamsFromWeigthMap11(weightMap);
    }
    extractParams(weights) {
      return extractParams11(weights);
    }
  }

  // build/ssdMobilenetv1/index.js
  function createSsdMobilenetv1(weights) {
    const net = new SsdMobilenetv1();
    net.extractWeights(weights);
    return net;
  }
  function createFaceDetectionNet(weights) {
    return createSsdMobilenetv1(weights);
  }
  class FaceDetectionNet extends SsdMobilenetv1 {
  }

  // build/tinyYolov2/const.js
  const IOU_THRESHOLD = 0.4;
  const BOX_ANCHORS = [
    new Point(0.738768, 0.874946),
    new Point(2.42204, 2.65704),
    new Point(4.30971, 7.04493),
    new Point(10.246, 4.59428),
    new Point(12.6868, 11.8741)
  ];
  const BOX_ANCHORS_SEPARABLE = [
    new Point(1.603231, 2.094468),
    new Point(6.041143, 7.080126),
    new Point(2.882459, 3.518061),
    new Point(4.266906, 5.178857),
    new Point(9.041765, 10.66308)
  ];
  const MEAN_RGB_SEPARABLE = [117.001, 114.697, 97.404];
  const DEFAULT_MODEL_NAME = "tiny_yolov2_model";
  const DEFAULT_MODEL_NAME_SEPARABLE_CONV = "tiny_yolov2_separable_conv_model";

  // build/tinyYolov2/config.js
  const isNumber2 = (arg) => typeof arg === "number";
  function validateConfig(config2) {
    if (!config2) {
      throw new Error(`invalid config: ${config2}`);
    }
    if (typeof config2.withSeparableConvs !== "boolean") {
      throw new Error(`config.withSeparableConvs has to be a boolean, have: ${config2.withSeparableConvs}`);
    }
    if (!isNumber2(config2.iouThreshold) || config2.iouThreshold < 0 || config2.iouThreshold > 1) {
      throw new Error(`config.iouThreshold has to be a number between [0, 1], have: ${config2.iouThreshold}`);
    }
    if (!Array.isArray(config2.classes) || !config2.classes.length || !config2.classes.every((c) => typeof c === "string")) {
      throw new Error(`config.classes has to be an array class names: string[], have: ${JSON.stringify(config2.classes)}`);
    }
    if (!Array.isArray(config2.anchors) || !config2.anchors.length || !config2.anchors.map((a) => a || {}).every((a) => isNumber2(a.x) && isNumber2(a.y))) {
      throw new Error(`config.anchors has to be an array of { x: number, y: number }, have: ${JSON.stringify(config2.anchors)}`);
    }
    if (config2.meanRgb && (!Array.isArray(config2.meanRgb) || config2.meanRgb.length !== 3 || !config2.meanRgb.every(isNumber2))) {
      throw new Error(`config.meanRgb has to be an array of shape [number, number, number], have: ${JSON.stringify(config2.meanRgb)}`);
    }
  }

  // build/tinyYolov2/leaky.js
  function leaky(x) {
    return tidy(() => {
      const min5 = mul(x, scalar(0.10000000149011612));
      return add2(relu(sub(x, min5)), min5);
    });
  }

  // build/tinyYolov2/convWithBatchNorm.js
  function convWithBatchNorm(x, params) {
    return tidy(() => {
      let out = pad(x, [[0, 0], [1, 1], [1, 1], [0, 0]]);
      out = conv2d(out, params.conv.filters, [1, 1], "valid");
      out = sub(out, params.bn.sub);
      out = mul(out, params.bn.truediv);
      out = add2(out, params.conv.bias);
      return leaky(out);
    });
  }

  // build/tinyYolov2/depthwiseSeparableConv.js
  function depthwiseSeparableConv3(x, params) {
    return tidy(() => {
      let out = pad(x, [[0, 0], [1, 1], [1, 1], [0, 0]]);
      out = separableConv2d(out, params.depthwise_filter, params.pointwise_filter, [1, 1], "valid");
      out = add2(out, params.bias);
      return leaky(out);
    });
  }

  // build/tinyYolov2/extractParams.js
  function extractorsFactory9(extractWeights, paramMappings) {
    const extractConvParams = extractConvParamsFactory(extractWeights, paramMappings);
    function extractBatchNormParams(size, mappedPrefix) {
      const sub32 = tensor1d(extractWeights(size));
      const truediv = tensor1d(extractWeights(size));
      paramMappings.push({paramPath: `${mappedPrefix}/sub`}, {paramPath: `${mappedPrefix}/truediv`});
      return {sub: sub32, truediv};
    }
    function extractConvWithBatchNormParams(channelsIn, channelsOut, mappedPrefix) {
      const conv3 = extractConvParams(channelsIn, channelsOut, 3, `${mappedPrefix}/conv`);
      const bn = extractBatchNormParams(channelsOut, `${mappedPrefix}/bn`);
      return {conv: conv3, bn};
    }
    const extractSeparableConvParams = extractSeparableConvParamsFactory(extractWeights, paramMappings);
    return {
      extractConvParams,
      extractConvWithBatchNormParams,
      extractSeparableConvParams
    };
  }
  function extractParams13(weights, config2, boxEncodingSize, filterSizes) {
    const {extractWeights, getRemainingWeights} = extractWeightsFactory(weights);
    const paramMappings = [];
    const {extractConvParams, extractConvWithBatchNormParams, extractSeparableConvParams} = extractorsFactory9(extractWeights, paramMappings);
    let params;
    if (config2.withSeparableConvs) {
      const [s0, s1, s2, s3, s4, s5, s6, s7, s8] = filterSizes;
      const conv0 = config2.isFirstLayerConv2d ? extractConvParams(s0, s1, 3, "conv0") : extractSeparableConvParams(s0, s1, "conv0");
      const conv1 = extractSeparableConvParams(s1, s2, "conv1");
      const conv22 = extractSeparableConvParams(s2, s3, "conv2");
      const conv3 = extractSeparableConvParams(s3, s4, "conv3");
      const conv4 = extractSeparableConvParams(s4, s5, "conv4");
      const conv5 = extractSeparableConvParams(s5, s6, "conv5");
      const conv6 = s7 ? extractSeparableConvParams(s6, s7, "conv6") : void 0;
      const conv7 = s8 ? extractSeparableConvParams(s7, s8, "conv7") : void 0;
      const conv8 = extractConvParams(s8 || s7 || s6, 5 * boxEncodingSize, 1, "conv8");
      params = {conv0, conv1, conv2: conv22, conv3, conv4, conv5, conv6, conv7, conv8};
    } else {
      const [s0, s1, s2, s3, s4, s5, s6, s7, s8] = filterSizes;
      const conv0 = extractConvWithBatchNormParams(s0, s1, "conv0");
      const conv1 = extractConvWithBatchNormParams(s1, s2, "conv1");
      const conv22 = extractConvWithBatchNormParams(s2, s3, "conv2");
      const conv3 = extractConvWithBatchNormParams(s3, s4, "conv3");
      const conv4 = extractConvWithBatchNormParams(s4, s5, "conv4");
      const conv5 = extractConvWithBatchNormParams(s5, s6, "conv5");
      const conv6 = extractConvWithBatchNormParams(s6, s7, "conv6");
      const conv7 = extractConvWithBatchNormParams(s7, s8, "conv7");
      const conv8 = extractConvParams(s8, 5 * boxEncodingSize, 1, "conv8");
      params = {conv0, conv1, conv2: conv22, conv3, conv4, conv5, conv6, conv7, conv8};
    }
    if (getRemainingWeights().length !== 0) {
      throw new Error(`weights remaing after extract: ${getRemainingWeights().length}`);
    }
    return {params, paramMappings};
  }

  // build/tinyYolov2/extractParamsFromWeigthMap.js
  function extractorsFactory10(weightMap, paramMappings) {
    const extractWeightEntry = extractWeightEntryFactory(weightMap, paramMappings);
    function extractBatchNormParams(prefix) {
      const sub32 = extractWeightEntry(`${prefix}/sub`, 1);
      const truediv = extractWeightEntry(`${prefix}/truediv`, 1);
      return {sub: sub32, truediv};
    }
    function extractConvParams(prefix) {
      const filters = extractWeightEntry(`${prefix}/filters`, 4);
      const bias = extractWeightEntry(`${prefix}/bias`, 1);
      return {filters, bias};
    }
    function extractConvWithBatchNormParams(prefix) {
      const conv3 = extractConvParams(`${prefix}/conv`);
      const bn = extractBatchNormParams(`${prefix}/bn`);
      return {conv: conv3, bn};
    }
    const extractSeparableConvParams = loadSeparableConvParamsFactory(extractWeightEntry);
    return {
      extractConvParams,
      extractConvWithBatchNormParams,
      extractSeparableConvParams
    };
  }
  function extractParamsFromWeigthMap13(weightMap, config2) {
    const paramMappings = [];
    const {extractConvParams, extractConvWithBatchNormParams, extractSeparableConvParams} = extractorsFactory10(weightMap, paramMappings);
    let params;
    if (config2.withSeparableConvs) {
      const numFilters = config2.filterSizes && config2.filterSizes.length || 9;
      params = {
        conv0: config2.isFirstLayerConv2d ? extractConvParams("conv0") : extractSeparableConvParams("conv0"),
        conv1: extractSeparableConvParams("conv1"),
        conv2: extractSeparableConvParams("conv2"),
        conv3: extractSeparableConvParams("conv3"),
        conv4: extractSeparableConvParams("conv4"),
        conv5: extractSeparableConvParams("conv5"),
        conv6: numFilters > 7 ? extractSeparableConvParams("conv6") : void 0,
        conv7: numFilters > 8 ? extractSeparableConvParams("conv7") : void 0,
        conv8: extractConvParams("conv8")
      };
    } else {
      params = {
        conv0: extractConvWithBatchNormParams("conv0"),
        conv1: extractConvWithBatchNormParams("conv1"),
        conv2: extractConvWithBatchNormParams("conv2"),
        conv3: extractConvWithBatchNormParams("conv3"),
        conv4: extractConvWithBatchNormParams("conv4"),
        conv5: extractConvWithBatchNormParams("conv5"),
        conv6: extractConvWithBatchNormParams("conv6"),
        conv7: extractConvWithBatchNormParams("conv7"),
        conv8: extractConvParams("conv8")
      };
    }
    disposeUnusedWeightTensors(weightMap, paramMappings);
    return {params, paramMappings};
  }

  // build/tinyYolov2/TinyYolov2Options.js
  var TinyYolov2SizeType;
  (function(TinyYolov2SizeType2) {
    TinyYolov2SizeType2[TinyYolov2SizeType2["XS"] = 224] = "XS";
    TinyYolov2SizeType2[TinyYolov2SizeType2["SM"] = 320] = "SM";
    TinyYolov2SizeType2[TinyYolov2SizeType2["MD"] = 416] = "MD";
    TinyYolov2SizeType2[TinyYolov2SizeType2["LG"] = 608] = "LG";
  })(TinyYolov2SizeType || (TinyYolov2SizeType = {}));
  class TinyYolov2Options {
    constructor({inputSize, scoreThreshold} = {}) {
      this._name = "TinyYolov2Options";
      this._inputSize = inputSize || 416;
      this._scoreThreshold = scoreThreshold || 0.5;
      if (typeof this._inputSize !== "number" || this._inputSize % 32 !== 0) {
        throw new Error(`${this._name} - expected inputSize to be a number divisible by 32`);
      }
      if (typeof this._scoreThreshold !== "number" || this._scoreThreshold <= 0 || this._scoreThreshold >= 1) {
        throw new Error(`${this._name} - expected scoreThreshold to be a number between 0 and 1`);
      }
    }
    get inputSize() {
      return this._inputSize;
    }
    get scoreThreshold() {
      return this._scoreThreshold;
    }
  }

  // build/tinyYolov2/TinyYolov2Base.js
  class TinyYolov2Base extends NeuralNetwork {
    constructor(config2) {
      super("TinyYolov2");
      validateConfig(config2);
      this._config = config2;
    }
    get config() {
      return this._config;
    }
    get withClassScores() {
      return this.config.withClassScores || this.config.classes.length > 1;
    }
    get boxEncodingSize() {
      return 5 + (this.withClassScores ? this.config.classes.length : 0);
    }
    runTinyYolov2(x, params) {
      let out = convWithBatchNorm(x, params.conv0);
      out = maxPool(out, [2, 2], [2, 2], "same");
      out = convWithBatchNorm(out, params.conv1);
      out = maxPool(out, [2, 2], [2, 2], "same");
      out = convWithBatchNorm(out, params.conv2);
      out = maxPool(out, [2, 2], [2, 2], "same");
      out = convWithBatchNorm(out, params.conv3);
      out = maxPool(out, [2, 2], [2, 2], "same");
      out = convWithBatchNorm(out, params.conv4);
      out = maxPool(out, [2, 2], [2, 2], "same");
      out = convWithBatchNorm(out, params.conv5);
      out = maxPool(out, [2, 2], [1, 1], "same");
      out = convWithBatchNorm(out, params.conv6);
      out = convWithBatchNorm(out, params.conv7);
      return convLayer(out, params.conv8, "valid", false);
    }
    runMobilenet(x, params) {
      let out = this.config.isFirstLayerConv2d ? leaky(convLayer(x, params.conv0, "valid", false)) : depthwiseSeparableConv3(x, params.conv0);
      out = maxPool(out, [2, 2], [2, 2], "same");
      out = depthwiseSeparableConv3(out, params.conv1);
      out = maxPool(out, [2, 2], [2, 2], "same");
      out = depthwiseSeparableConv3(out, params.conv2);
      out = maxPool(out, [2, 2], [2, 2], "same");
      out = depthwiseSeparableConv3(out, params.conv3);
      out = maxPool(out, [2, 2], [2, 2], "same");
      out = depthwiseSeparableConv3(out, params.conv4);
      out = maxPool(out, [2, 2], [2, 2], "same");
      out = depthwiseSeparableConv3(out, params.conv5);
      out = maxPool(out, [2, 2], [1, 1], "same");
      out = params.conv6 ? depthwiseSeparableConv3(out, params.conv6) : out;
      out = params.conv7 ? depthwiseSeparableConv3(out, params.conv7) : out;
      return convLayer(out, params.conv8, "valid", false);
    }
    forwardInput(input, inputSize) {
      const {params} = this;
      if (!params) {
        throw new Error("TinyYolov2 - load model before inference");
      }
      return tidy(() => {
        let batchTensor = input.toBatchTensor(inputSize, false).toFloat();
        batchTensor = this.config.meanRgb ? normalize(batchTensor, this.config.meanRgb) : batchTensor;
        batchTensor = batchTensor.div(scalar(256));
        return this.config.withSeparableConvs ? this.runMobilenet(batchTensor, params) : this.runTinyYolov2(batchTensor, params);
      });
    }
    async forward(input, inputSize) {
      return await this.forwardInput(await toNetInput(input), inputSize);
    }
    async detect(input, forwardParams = {}) {
      const {inputSize, scoreThreshold} = new TinyYolov2Options(forwardParams);
      const netInput = await toNetInput(input);
      const out = await this.forwardInput(netInput, inputSize);
      const out0 = tidy(() => unstack(out)[0].expandDims());
      const inputDimensions = {
        width: netInput.getInputWidth(0),
        height: netInput.getInputHeight(0)
      };
      const results = await this.extractBoxes(out0, netInput.getReshapedInputDimensions(0), scoreThreshold);
      out.dispose();
      out0.dispose();
      const boxes = results.map((res) => res.box);
      const scores = results.map((res) => res.score);
      const classScores = results.map((res) => res.classScore);
      const classNames = results.map((res) => this.config.classes[res.label]);
      const indices = nonMaxSuppression2(boxes.map((box) => box.rescale(inputSize)), scores, this.config.iouThreshold, true);
      const detections = indices.map((idx) => new ObjectDetection(scores[idx], classScores[idx], classNames[idx], boxes[idx], inputDimensions));
      return detections;
    }
    getDefaultModelName() {
      return "";
    }
    extractParamsFromWeigthMap(weightMap) {
      return extractParamsFromWeigthMap13(weightMap, this.config);
    }
    extractParams(weights) {
      const filterSizes = this.config.filterSizes || TinyYolov2Base.DEFAULT_FILTER_SIZES;
      const numFilters = filterSizes ? filterSizes.length : void 0;
      if (numFilters !== 7 && numFilters !== 8 && numFilters !== 9) {
        throw new Error(`TinyYolov2 - expected 7 | 8 | 9 convolutional filters, but found ${numFilters} filterSizes in config`);
      }
      return extractParams13(weights, this.config, this.boxEncodingSize, filterSizes);
    }
    async extractBoxes(outputTensor, inputBlobDimensions, scoreThreshold) {
      const {width, height} = inputBlobDimensions;
      const inputSize = Math.max(width, height);
      const correctionFactorX = inputSize / width;
      const correctionFactorY = inputSize / height;
      const numCells = outputTensor.shape[1];
      const numBoxes = this.config.anchors.length;
      const [boxesTensor, scoresTensor, classScoresTensor] = tidy(() => {
        const reshaped = outputTensor.reshape([numCells, numCells, numBoxes, this.boxEncodingSize]);
        const boxes = reshaped.slice([0, 0, 0, 0], [numCells, numCells, numBoxes, 4]);
        const scores = reshaped.slice([0, 0, 0, 4], [numCells, numCells, numBoxes, 1]);
        const classScores = this.withClassScores ? softmax(reshaped.slice([0, 0, 0, 5], [numCells, numCells, numBoxes, this.config.classes.length]), 3) : scalar(0);
        return [boxes, scores, classScores];
      });
      const results = [];
      const scoresData = await scoresTensor.array();
      const boxesData = await boxesTensor.array();
      for (let row = 0; row < numCells; row++) {
        for (let col = 0; col < numCells; col++) {
          for (let anchor = 0; anchor < numBoxes; anchor++) {
            const score = sigmoid6(scoresData[row][col][anchor][0]);
            if (!scoreThreshold || score > scoreThreshold) {
              const ctX = (col + sigmoid6(boxesData[row][col][anchor][0])) / numCells * correctionFactorX;
              const ctY = (row + sigmoid6(boxesData[row][col][anchor][1])) / numCells * correctionFactorY;
              const width2 = Math.exp(boxesData[row][col][anchor][2]) * this.config.anchors[anchor].x / numCells * correctionFactorX;
              const height2 = Math.exp(boxesData[row][col][anchor][3]) * this.config.anchors[anchor].y / numCells * correctionFactorY;
              const x = ctX - width2 / 2;
              const y = ctY - height2 / 2;
              const pos = {row, col, anchor};
              const {classScore, label} = this.withClassScores ? await this.extractPredictedClass(classScoresTensor, pos) : {classScore: 1, label: 0};
              results.push(__assign({
                box: new BoundingBox(x, y, x + width2, y + height2),
                score,
                classScore: score * classScore,
                label
              }, pos));
            }
          }
        }
      }
      boxesTensor.dispose();
      scoresTensor.dispose();
      classScoresTensor.dispose();
      return results;
    }
    async extractPredictedClass(classesTensor, pos) {
      const {row, col, anchor} = pos;
      const classesData = await classesTensor.array();
      return Array(this.config.classes.length).fill(0).map((_, i) => classesData[row][col][anchor][i]).map((classScore, label) => ({
        classScore,
        label
      })).reduce((max7, curr) => max7.classScore > curr.classScore ? max7 : curr);
    }
  }
  TinyYolov2Base.DEFAULT_FILTER_SIZES = [
    3,
    16,
    32,
    64,
    128,
    256,
    512,
    1024,
    1024
  ];

  // build/tinyYolov2/TinyYolov2.js
  class TinyYolov2 extends TinyYolov2Base {
    constructor(withSeparableConvs = true) {
      const config2 = Object.assign({}, {
        withSeparableConvs,
        iouThreshold: IOU_THRESHOLD,
        classes: ["face"]
      }, withSeparableConvs ? {
        anchors: BOX_ANCHORS_SEPARABLE,
        meanRgb: MEAN_RGB_SEPARABLE
      } : {
        anchors: BOX_ANCHORS,
        withClassScores: true
      });
      super(config2);
    }
    get withSeparableConvs() {
      return this.config.withSeparableConvs;
    }
    get anchors() {
      return this.config.anchors;
    }
    async locateFaces(input, forwardParams) {
      const objectDetections = await this.detect(input, forwardParams);
      return objectDetections.map((det) => new FaceDetection(det.score, det.relativeBox, {width: det.imageWidth, height: det.imageHeight}));
    }
    getDefaultModelName() {
      return this.withSeparableConvs ? DEFAULT_MODEL_NAME_SEPARABLE_CONV : DEFAULT_MODEL_NAME;
    }
    extractParamsFromWeigthMap(weightMap) {
      return super.extractParamsFromWeigthMap(weightMap);
    }
  }

  // build/tinyYolov2/types.js

  // build/tinyYolov2/index.js
  function createTinyYolov2(weights, withSeparableConvs = true) {
    const net = new TinyYolov2(withSeparableConvs);
    net.extractWeights(weights);
    return net;
  }

  // build/tinyFaceDetector/TinyFaceDetectorOptions.js
  class TinyFaceDetectorOptions extends TinyYolov2Options {
    constructor() {
      super(...arguments);
      this._name = "TinyFaceDetectorOptions";
    }
  }

  // build/globalApi/ComposableTask.js
  class ComposableTask {
    async then(onfulfilled) {
      return onfulfilled(await this.run());
    }
    async run() {
      throw new Error("ComposableTask - run is not implemented");
    }
  }

  // build/globalApi/extractFacesAndComputeResults.js
  async function extractAllFacesAndComputeResults(parentResults, input, computeResults, extractedFaces, getRectForAlignment = ({alignedRect}) => alignedRect) {
    const faceBoxes = parentResults.map((parentResult) => isWithFaceLandmarks(parentResult) ? getRectForAlignment(parentResult) : parentResult.detection);
    const faces = extractedFaces || (input instanceof Tensor ? await extractFaceTensors(input, faceBoxes) : await extractFaces(input, faceBoxes));
    const results = await computeResults(faces);
    faces.forEach((f) => f instanceof Tensor && f.dispose());
    return results;
  }
  async function extractSingleFaceAndComputeResult(parentResult, input, computeResult, extractedFaces, getRectForAlignment) {
    return extractAllFacesAndComputeResults([parentResult], input, async (faces) => computeResult(faces[0]), extractedFaces, getRectForAlignment);
  }

  // build/tinyFaceDetector/const.js
  const IOU_THRESHOLD2 = 0.4;
  const BOX_ANCHORS2 = [
    new Point(1.603231, 2.094468),
    new Point(6.041143, 7.080126),
    new Point(2.882459, 3.518061),
    new Point(4.266906, 5.178857),
    new Point(9.041765, 10.66308)
  ];
  const MEAN_RGB = [117.001, 114.697, 97.404];

  // build/tinyFaceDetector/TinyFaceDetector.js
  class TinyFaceDetector extends TinyYolov2Base {
    constructor() {
      const config2 = {
        withSeparableConvs: true,
        iouThreshold: IOU_THRESHOLD2,
        classes: ["face"],
        anchors: BOX_ANCHORS2,
        meanRgb: MEAN_RGB,
        isFirstLayerConv2d: true,
        filterSizes: [3, 16, 32, 64, 128, 256, 512]
      };
      super(config2);
    }
    get anchors() {
      return this.config.anchors;
    }
    async locateFaces(input, forwardParams) {
      const objectDetections = await this.detect(input, forwardParams);
      return objectDetections.map((det) => new FaceDetection(det.score, det.relativeBox, {width: det.imageWidth, height: det.imageHeight}));
    }
    getDefaultModelName() {
      return "tiny_face_detector_model";
    }
    extractParamsFromWeigthMap(weightMap) {
      return super.extractParamsFromWeigthMap(weightMap);
    }
  }

  // build/globalApi/nets.js
  const nets = {
    ssdMobilenetv1: new SsdMobilenetv1(),
    tinyFaceDetector: new TinyFaceDetector(),
    tinyYolov2: new TinyYolov2(),
    faceLandmark68Net: new FaceLandmark68Net(),
    faceLandmark68TinyNet: new FaceLandmark68TinyNet(),
    faceRecognitionNet: new FaceRecognitionNet(),
    faceExpressionNet: new FaceExpressionNet(),
    ageGenderNet: new AgeGenderNet()
  };
  const ssdMobilenetv1 = (input, options) => nets.ssdMobilenetv1.locateFaces(input, options);
  const tinyFaceDetector = (input, options) => nets.tinyFaceDetector.locateFaces(input, options);
  const tinyYolov23 = (input, options) => nets.tinyYolov2.locateFaces(input, options);
  const detectFaceLandmarks = (input) => nets.faceLandmark68Net.detectLandmarks(input);
  const detectFaceLandmarksTiny = (input) => nets.faceLandmark68TinyNet.detectLandmarks(input);
  const computeFaceDescriptor = (input) => nets.faceRecognitionNet.computeFaceDescriptor(input);
  const recognizeFaceExpressions = (input) => nets.faceExpressionNet.predictExpressions(input);
  const predictAgeAndGender = (input) => nets.ageGenderNet.predictAgeAndGender(input);
  const loadSsdMobilenetv1Model = (url) => nets.ssdMobilenetv1.load(url);
  const loadTinyFaceDetectorModel = (url) => nets.tinyFaceDetector.load(url);
  const loadTinyYolov2Model = (url) => nets.tinyYolov2.load(url);
  const loadFaceLandmarkModel = (url) => nets.faceLandmark68Net.load(url);
  const loadFaceLandmarkTinyModel = (url) => nets.faceLandmark68TinyNet.load(url);
  const loadFaceRecognitionModel = (url) => nets.faceRecognitionNet.load(url);
  const loadFaceExpressionModel = (url) => nets.faceExpressionNet.load(url);
  const loadAgeGenderModel = (url) => nets.ageGenderNet.load(url);
  const loadFaceDetectionModel = loadSsdMobilenetv1Model;
  const locateFaces = ssdMobilenetv1;
  const detectLandmarks = detectFaceLandmarks;

  // build/globalApi/PredictFaceExpressionsTask.js
  class PredictFaceExpressionsTaskBase extends ComposableTask {
    constructor(parentTask, input, extractedFaces) {
      super();
      this.parentTask = parentTask;
      this.input = input;
      this.extractedFaces = extractedFaces;
    }
  }
  class PredictAllFaceExpressionsTask extends PredictFaceExpressionsTaskBase {
    async run() {
      const parentResults = await this.parentTask;
      const faceExpressionsByFace = await extractAllFacesAndComputeResults(parentResults, this.input, async (faces) => await Promise.all(faces.map((face) => nets.faceExpressionNet.predictExpressions(face))), this.extractedFaces);
      return parentResults.map((parentResult, i) => extendWithFaceExpressions(parentResult, faceExpressionsByFace[i]));
    }
    withAgeAndGender() {
      return new PredictAllAgeAndGenderTask(this, this.input);
    }
  }
  class PredictSingleFaceExpressionsTask extends PredictFaceExpressionsTaskBase {
    async run() {
      const parentResult = await this.parentTask;
      if (!parentResult) {
        return;
      }
      const faceExpressions = await extractSingleFaceAndComputeResult(parentResult, this.input, (face) => nets.faceExpressionNet.predictExpressions(face), this.extractedFaces);
      return extendWithFaceExpressions(parentResult, faceExpressions);
    }
    withAgeAndGender() {
      return new PredictSingleAgeAndGenderTask(this, this.input);
    }
  }
  class PredictAllFaceExpressionsWithFaceAlignmentTask extends PredictAllFaceExpressionsTask {
    withAgeAndGender() {
      return new PredictAllAgeAndGenderWithFaceAlignmentTask(this, this.input);
    }
    withFaceDescriptors() {
      return new ComputeAllFaceDescriptorsTask(this, this.input);
    }
  }
  class PredictSingleFaceExpressionsWithFaceAlignmentTask extends PredictSingleFaceExpressionsTask {
    withAgeAndGender() {
      return new PredictSingleAgeAndGenderWithFaceAlignmentTask(this, this.input);
    }
    withFaceDescriptor() {
      return new ComputeSingleFaceDescriptorTask(this, this.input);
    }
  }

  // build/globalApi/PredictAgeAndGenderTask.js
  class PredictAgeAndGenderTaskBase extends ComposableTask {
    constructor(parentTask, input, extractedFaces) {
      super();
      this.parentTask = parentTask;
      this.input = input;
      this.extractedFaces = extractedFaces;
    }
  }
  class PredictAllAgeAndGenderTask extends PredictAgeAndGenderTaskBase {
    async run() {
      const parentResults = await this.parentTask;
      const ageAndGenderByFace = await extractAllFacesAndComputeResults(parentResults, this.input, async (faces) => await Promise.all(faces.map((face) => nets.ageGenderNet.predictAgeAndGender(face))), this.extractedFaces);
      return parentResults.map((parentResult, i) => {
        const {age, gender, genderProbability} = ageAndGenderByFace[i];
        return extendWithAge(extendWithGender(parentResult, gender, genderProbability), age);
      });
    }
    withFaceExpressions() {
      return new PredictAllFaceExpressionsTask(this, this.input);
    }
  }
  class PredictSingleAgeAndGenderTask extends PredictAgeAndGenderTaskBase {
    async run() {
      const parentResult = await this.parentTask;
      if (!parentResult) {
        return;
      }
      const {age, gender, genderProbability} = await extractSingleFaceAndComputeResult(parentResult, this.input, (face) => nets.ageGenderNet.predictAgeAndGender(face), this.extractedFaces);
      return extendWithAge(extendWithGender(parentResult, gender, genderProbability), age);
    }
    withFaceExpressions() {
      return new PredictSingleFaceExpressionsTask(this, this.input);
    }
  }
  class PredictAllAgeAndGenderWithFaceAlignmentTask extends PredictAllAgeAndGenderTask {
    withFaceExpressions() {
      return new PredictAllFaceExpressionsWithFaceAlignmentTask(this, this.input);
    }
    withFaceDescriptors() {
      return new ComputeAllFaceDescriptorsTask(this, this.input);
    }
  }
  class PredictSingleAgeAndGenderWithFaceAlignmentTask extends PredictSingleAgeAndGenderTask {
    withFaceExpressions() {
      return new PredictSingleFaceExpressionsWithFaceAlignmentTask(this, this.input);
    }
    withFaceDescriptor() {
      return new ComputeSingleFaceDescriptorTask(this, this.input);
    }
  }

  // build/globalApi/ComputeFaceDescriptorsTasks.js
  class ComputeFaceDescriptorsTaskBase extends ComposableTask {
    constructor(parentTask, input) {
      super();
      this.parentTask = parentTask;
      this.input = input;
    }
  }
  class ComputeAllFaceDescriptorsTask extends ComputeFaceDescriptorsTaskBase {
    async run() {
      const parentResults = await this.parentTask;
      const descriptors = await extractAllFacesAndComputeResults(parentResults, this.input, (faces) => Promise.all(faces.map((face) => nets.faceRecognitionNet.computeFaceDescriptor(face))), null, (parentResult) => parentResult.landmarks.align(null, {useDlibAlignment: true}));
      return descriptors.map((descriptor, i) => extendWithFaceDescriptor(parentResults[i], descriptor));
    }
    withFaceExpressions() {
      return new PredictAllFaceExpressionsWithFaceAlignmentTask(this, this.input);
    }
    withAgeAndGender() {
      return new PredictAllAgeAndGenderWithFaceAlignmentTask(this, this.input);
    }
  }
  class ComputeSingleFaceDescriptorTask extends ComputeFaceDescriptorsTaskBase {
    async run() {
      const parentResult = await this.parentTask;
      if (!parentResult) {
        return;
      }
      const descriptor = await extractSingleFaceAndComputeResult(parentResult, this.input, (face) => nets.faceRecognitionNet.computeFaceDescriptor(face), null, (parentResult2) => parentResult2.landmarks.align(null, {useDlibAlignment: true}));
      return extendWithFaceDescriptor(parentResult, descriptor);
    }
    withFaceExpressions() {
      return new PredictSingleFaceExpressionsWithFaceAlignmentTask(this, this.input);
    }
    withAgeAndGender() {
      return new PredictSingleAgeAndGenderWithFaceAlignmentTask(this, this.input);
    }
  }

  // build/globalApi/DetectFaceLandmarksTasks.js
  class DetectFaceLandmarksTaskBase extends ComposableTask {
    constructor(parentTask, input, useTinyLandmarkNet) {
      super();
      this.parentTask = parentTask;
      this.input = input;
      this.useTinyLandmarkNet = useTinyLandmarkNet;
    }
    get landmarkNet() {
      return this.useTinyLandmarkNet ? nets.faceLandmark68TinyNet : nets.faceLandmark68Net;
    }
  }
  class DetectAllFaceLandmarksTask extends DetectFaceLandmarksTaskBase {
    async run() {
      const parentResults = await this.parentTask;
      const detections = parentResults.map((res) => res.detection);
      const faces = this.input instanceof Tensor ? await extractFaceTensors(this.input, detections) : await extractFaces(this.input, detections);
      const faceLandmarksByFace = await Promise.all(faces.map((face) => this.landmarkNet.detectLandmarks(face)));
      faces.forEach((f) => f instanceof Tensor && f.dispose());
      return parentResults.map((parentResult, i) => extendWithFaceLandmarks(parentResult, faceLandmarksByFace[i]));
    }
    withFaceExpressions() {
      return new PredictAllFaceExpressionsWithFaceAlignmentTask(this, this.input);
    }
    withAgeAndGender() {
      return new PredictAllAgeAndGenderWithFaceAlignmentTask(this, this.input);
    }
    withFaceDescriptors() {
      return new ComputeAllFaceDescriptorsTask(this, this.input);
    }
  }
  class DetectSingleFaceLandmarksTask extends DetectFaceLandmarksTaskBase {
    async run() {
      const parentResult = await this.parentTask;
      if (!parentResult) {
        return;
      }
      const {detection} = parentResult;
      const faces = this.input instanceof Tensor ? await extractFaceTensors(this.input, [detection]) : await extractFaces(this.input, [detection]);
      const landmarks = await this.landmarkNet.detectLandmarks(faces[0]);
      faces.forEach((f) => f instanceof Tensor && f.dispose());
      return extendWithFaceLandmarks(parentResult, landmarks);
    }
    withFaceExpressions() {
      return new PredictSingleFaceExpressionsWithFaceAlignmentTask(this, this.input);
    }
    withAgeAndGender() {
      return new PredictSingleAgeAndGenderWithFaceAlignmentTask(this, this.input);
    }
    withFaceDescriptor() {
      return new ComputeSingleFaceDescriptorTask(this, this.input);
    }
  }

  // build/globalApi/DetectFacesTasks.js
  class DetectFacesTaskBase extends ComposableTask {
    constructor(input, options = new SsdMobilenetv1Options()) {
      super();
      this.input = input;
      this.options = options;
    }
  }
  class DetectAllFacesTask extends DetectFacesTaskBase {
    async run() {
      const {input, options} = this;
      const faceDetectionFunction = options instanceof TinyFaceDetectorOptions ? (input2) => nets.tinyFaceDetector.locateFaces(input2, options) : options instanceof SsdMobilenetv1Options ? (input2) => nets.ssdMobilenetv1.locateFaces(input2, options) : options instanceof TinyYolov2Options ? (input2) => nets.tinyYolov2.locateFaces(input2, options) : null;
      if (!faceDetectionFunction) {
        throw new Error("detectFaces - expected options to be instance of TinyFaceDetectorOptions | SsdMobilenetv1Options | MtcnnOptions | TinyYolov2Options");
      }
      return faceDetectionFunction(input);
    }
    runAndExtendWithFaceDetections() {
      return new Promise(async (res) => {
        const detections = await this.run();
        return res(detections.map((detection) => extendWithFaceDetection({}, detection)));
      });
    }
    withFaceLandmarks(useTinyLandmarkNet = false) {
      return new DetectAllFaceLandmarksTask(this.runAndExtendWithFaceDetections(), this.input, useTinyLandmarkNet);
    }
    withFaceExpressions() {
      return new PredictAllFaceExpressionsTask(this.runAndExtendWithFaceDetections(), this.input);
    }
    withAgeAndGender() {
      return new PredictAllAgeAndGenderTask(this.runAndExtendWithFaceDetections(), this.input);
    }
  }
  class DetectSingleFaceTask extends DetectFacesTaskBase {
    async run() {
      const faceDetections = await new DetectAllFacesTask(this.input, this.options);
      let faceDetectionWithHighestScore = faceDetections[0];
      faceDetections.forEach((faceDetection) => {
        if (faceDetection.score > faceDetectionWithHighestScore.score) {
          faceDetectionWithHighestScore = faceDetection;
        }
      });
      return faceDetectionWithHighestScore;
    }
    runAndExtendWithFaceDetection() {
      return new Promise(async (res) => {
        const detection = await this.run();
        return res(detection ? extendWithFaceDetection({}, detection) : void 0);
      });
    }
    withFaceLandmarks(useTinyLandmarkNet = false) {
      return new DetectSingleFaceLandmarksTask(this.runAndExtendWithFaceDetection(), this.input, useTinyLandmarkNet);
    }
    withFaceExpressions() {
      return new PredictSingleFaceExpressionsTask(this.runAndExtendWithFaceDetection(), this.input);
    }
    withAgeAndGender() {
      return new PredictSingleAgeAndGenderTask(this.runAndExtendWithFaceDetection(), this.input);
    }
  }

  // build/globalApi/detectFaces.js
  function detectSingleFace(input, options = new SsdMobilenetv1Options()) {
    return new DetectSingleFaceTask(input, options);
  }
  function detectAllFaces(input, options = new SsdMobilenetv1Options()) {
    return new DetectAllFacesTask(input, options);
  }

  // build/globalApi/allFaces.js
  async function allFacesSsdMobilenetv1(input, minConfidence) {
    console.warn("allFacesSsdMobilenetv1 is deprecated and will be removed soon, use the high level api instead");
    return await detectAllFaces(input, new SsdMobilenetv1Options(minConfidence ? {minConfidence} : {})).withFaceLandmarks().withFaceDescriptors();
  }
  async function allFacesTinyYolov2(input, forwardParams = {}) {
    console.warn("allFacesTinyYolov2 is deprecated and will be removed soon, use the high level api instead");
    return await detectAllFaces(input, new TinyYolov2Options(forwardParams)).withFaceLandmarks().withFaceDescriptors();
  }
  const allFaces = allFacesSsdMobilenetv1;

  // build/euclideanDistance.js
  function euclideanDistance(arr1, arr2) {
    if (arr1.length !== arr2.length)
      throw new Error("euclideanDistance: arr1.length !== arr2.length");
    const desc1 = Array.from(arr1);
    const desc2 = Array.from(arr2);
    return Math.sqrt(desc1.map((val, i) => val - desc2[i]).reduce((res, diff) => res + Math.pow(diff, 2), 0));
  }

  // build/globalApi/FaceMatcher.js
  class FaceMatcher {
    constructor(inputs, distanceThreshold = 0.6) {
      this._distanceThreshold = distanceThreshold;
      const inputArray = Array.isArray(inputs) ? inputs : [inputs];
      if (!inputArray.length) {
        throw new Error(`FaceRecognizer.constructor - expected atleast one input`);
      }
      let count = 1;
      const createUniqueLabel = () => `person ${count++}`;
      this._labeledDescriptors = inputArray.map((desc) => {
        if (desc instanceof LabeledFaceDescriptors) {
          return desc;
        }
        if (desc instanceof Float32Array) {
          return new LabeledFaceDescriptors(createUniqueLabel(), [desc]);
        }
        if (desc.descriptor && desc.descriptor instanceof Float32Array) {
          return new LabeledFaceDescriptors(createUniqueLabel(), [desc.descriptor]);
        }
        throw new Error(`FaceRecognizer.constructor - expected inputs to be of type LabeledFaceDescriptors | WithFaceDescriptor<any> | Float32Array | Array<LabeledFaceDescriptors | WithFaceDescriptor<any> | Float32Array>`);
      });
    }
    get labeledDescriptors() {
      return this._labeledDescriptors;
    }
    get distanceThreshold() {
      return this._distanceThreshold;
    }
    computeMeanDistance(queryDescriptor, descriptors) {
      return descriptors.map((d) => euclideanDistance(d, queryDescriptor)).reduce((d1, d2) => d1 + d2, 0) / (descriptors.length || 1);
    }
    matchDescriptor(queryDescriptor) {
      return this.labeledDescriptors.map(({descriptors, label}) => new FaceMatch(label, this.computeMeanDistance(queryDescriptor, descriptors))).reduce((best, curr) => best.distance < curr.distance ? best : curr);
    }
    findBestMatch(queryDescriptor) {
      const bestMatch = this.matchDescriptor(queryDescriptor);
      return bestMatch.distance < this.distanceThreshold ? bestMatch : new FaceMatch("unknown", bestMatch.distance);
    }
    toJSON() {
      return {
        distanceThreshold: this.distanceThreshold,
        labeledDescriptors: this.labeledDescriptors.map((ld) => ld.toJSON())
      };
    }
    static fromJSON(json) {
      const labeledDescriptors = json.labeledDescriptors.map((ld) => LabeledFaceDescriptors.fromJSON(ld));
      return new FaceMatcher(labeledDescriptors, json.distanceThreshold);
    }
  }

  // build/globalApi/types.js

  // build/globalApi/index.js

  // build/tinyFaceDetector/index.js
  function createTinyFaceDetector(weights) {
    const net = new TinyFaceDetector();
    net.extractWeights(weights);
    return net;
  }

  // build/resizeResults.js
  function resizeResults(results, dimensions) {
    const {width, height} = new Dimensions(dimensions.width, dimensions.height);
    if (width <= 0 || height <= 0) {
      throw new Error(`resizeResults - invalid dimensions: ${JSON.stringify({width, height})}`);
    }
    if (Array.isArray(results)) {
      return results.map((obj) => resizeResults(obj, {width, height}));
    }
    if (isWithFaceLandmarks(results)) {
      const resizedDetection = results.detection.forSize(width, height);
      const resizedLandmarks = results.unshiftedLandmarks.forSize(resizedDetection.box.width, resizedDetection.box.height);
      return extendWithFaceLandmarks(extendWithFaceDetection(results, resizedDetection), resizedLandmarks);
    }
    if (isWithFaceDetection(results)) {
      return extendWithFaceDetection(results, results.detection.forSize(width, height));
    }
    if (results instanceof FaceLandmarks || results instanceof FaceDetection) {
      return results.forSize(width, height);
    }
    return results;
  }
  return require_build();
})();
//# sourceMappingURL=face-api.js.map
